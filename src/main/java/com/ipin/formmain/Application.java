package com.ipin.formmain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * Created by janze on 4/13/18.
 */

@Configuration
@EnableAutoConfiguration
@ComponentScan
@EnableScheduling //开启定时任务
@SpringBootConfiguration
public class Application{

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

}
