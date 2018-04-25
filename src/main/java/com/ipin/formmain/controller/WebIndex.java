package com.ipin.formmain.controller;

import com.ipin.formmain.service.SubscribeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by janze on 4/2/18.
 */
@Controller
public class WebIndex {


    Logger logger = LoggerFactory.getLogger(WebIndex.class);
    @Autowired
    private SubscribeService subscribeService;

    @GetMapping("/index")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/subscribe")
    public String subscribe(@RequestParam("link") List<Long> memberIds, HttpServletRequest request){

        String username = (String) request.getSession().getAttribute("username");
        final StringBuffer buf = new StringBuffer();
        memberIds.stream().forEach(e -> {
            if ( e != null ){
                buf.append(e + ",");
            }
        });
        logger.info(username + "订阅名单:" + buf.toString());
        subscribeService.saveSubscribeShips(username, memberIds);
        subscribeService.notifySubscriber(username, memberIds);
        return "index";
    }

}
