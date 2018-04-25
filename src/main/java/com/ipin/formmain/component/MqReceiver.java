package com.ipin.formmain.component;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ipin.formmain.bean.NotifiedFormmain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by janze on 4/19/18.
 */

@Component
public class MqReceiver {


    private static Logger logger = LoggerFactory.getLogger(MqReceiver.class);

    @Autowired
    private MailHelper mailHelper;

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void receiveMessage(String message) {

        logger.info("MQ接收信息 " + message);
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<NotifiedFormmain>>() {}.getType();
        List<NotifiedFormmain> notifiedFormmains = gson.fromJson(message, listType);

        Map<String, List<NotifiedFormmain>> map = this.convertToMap(notifiedFormmains);

        map.keySet().stream().forEach(m -> {

            List<NotifiedFormmain> formmains = map.get(m);
            StringBuffer mailBody = new StringBuffer();
            formmains.stream().forEach(e -> {
                // 发送邮件
                mailBody.append(e.getApplicantName())
                        .append("于")
                        .append(dateFormat.format(e.getApplicationDate()))
                        .append("提出请假申请，请假开始时间")
                        .append(timestampFormat.format(e.getStartDate()))
                        .append("，请假结束时间")
                        .append(timestampFormat.format(e.getEndDate()))
                        .append("，一共请假")
                        .append(e.getLeaveDays())
                        .append("天。")
                        .append("\n");
            });
            mailHelper.sendMail(m, "OA请假订阅通知", mailBody.toString());
        });
    }

    // 同个订阅者， 合并通知
    private Map<String, List<NotifiedFormmain>> convertToMap(List<NotifiedFormmain> formmains) {
        Map<String, List<NotifiedFormmain>> map = new HashMap<>();
        if (formmains != null) {
            formmains.stream().forEach(e -> {
                e.getSubscriberMailAddr().stream().forEach(m -> {
                    if (map.get(m) == null) {
                        map.put(m, new ArrayList<>());
                    }
                    map.get(m).add(e);
                });
            });
        }
        return map;
    }

}
