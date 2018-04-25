package com.ipin.formmain;

import com.ipin.formmain.bean.NotifiedFormmain;
import com.ipin.formmain.component.MailHelper;
import com.ipin.formmain.dao.NotifiedFormmainRepository;
import com.ipin.formmain.dao.SubscribeShipRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by janze on 4/19/18.
 */



@RunWith(SpringRunner.class)
@SpringBootTest
public class MailHelperTest {


    @Autowired
    MailHelper mailHelper;

    @Autowired
    SubscribeShipRepository subscribeShipRepository;
    @Test
    public void contextLoads() throws Exception {

//        mailHelper.sendMail();

        subscribeShipRepository.findByMemberId(8892415436795822734L).stream().forEach(e -> System.out.println(e.toString()));

    }

}