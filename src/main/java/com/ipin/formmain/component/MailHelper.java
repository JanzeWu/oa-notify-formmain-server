package com.ipin.formmain.component;

import com.ipin.formmain.bean.MailEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;

/**
 * Created by janze on 4/19/18.
 */
@Component
public class MailHelper {

    private static Logger logger = LoggerFactory.getLogger(MailHelper.class);
    @Value("${mail.server.host}")
    private String mailServerHost;

    @Value("${mail.server.username}")
    private String mailServerUsername;

    @Value("${mail.server.api_key}")
    private String mailServerApiKey;

    @Value("${mail.server.api_secret}")
    private String mailServerApiSecret;

    @Autowired
    private RestTemplate restTemplate ;

    public void sendMail(String mailTo, String subject, String body){
        try {

            String queryParams = "api_key=" + this.mailServerApiKey
                    +"&randomstr=";
            URI uri = new URI(mailServerHost +"?"
                    + queryParams
                    + "&sign=" + this.getMD5Str(queryParams + "&key=" + mailServerApiSecret).toUpperCase());

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("charset", "UTF-8");
            HttpEntity<MailEntity> mailEntiry = new HttpEntity<>(new MailEntity(mailTo, subject, body), headers);
            restTemplate.postForEntity(uri,
                    mailEntiry,
                    null
                    );
        }catch (URISyntaxException e){
            e.printStackTrace();
        }
    }

    public String getMD5Str(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            logger.error("MD5加密出现错误，"+e.toString());
        }
        return null;
    }
}
