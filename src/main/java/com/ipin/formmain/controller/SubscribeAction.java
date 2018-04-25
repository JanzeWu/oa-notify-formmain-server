package com.ipin.formmain.controller;

import com.ipin.formmain.bean.MemberResult;
import com.ipin.formmain.service.SubscribeService;
import com.sun.net.httpserver.HttpsServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * Created by janze on 4/17/18.
 */

@RestController
@RequestMapping("/oa")
public class SubscribeAction {


    private static Logger logger = LoggerFactory.getLogger(SubscribeAction.class);
    @Autowired
    private SubscribeService subscribeService;

    @GetMapping("/members")
    public List<MemberResult> members(HttpServletRequest request){

        // String username = SecurityContextHolder.getContext().getAuthentication().getName();

        List<MemberResult> res = subscribeService.departmentMembers((String) request.getSession().getAttribute("username"));
        logger.info("公司成员名单，部门数" + res.size() + "。");
        return res;
    }


}
