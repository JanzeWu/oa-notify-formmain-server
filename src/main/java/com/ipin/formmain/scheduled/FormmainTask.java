package com.ipin.formmain.scheduled;

import com.google.gson.Gson;
import com.ipin.formmain.bean.Formmain;
import com.ipin.formmain.bean.NotifiedFormmain;
import com.ipin.formmain.bean.SubscribeShip;
import com.ipin.formmain.consts.ConstantSet;
import com.ipin.formmain.dao.FormmainDao;
import com.ipin.formmain.dao.NotifiedFormmainRepository;
import com.ipin.formmain.dao.SubscribeShipRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Predicate;

/**
 * Created by janze on 4/16/18.
 */
@Component
public class FormmainTask {

    private static Logger logger = LoggerFactory.getLogger(FormmainTask.class);

    @Autowired
    private NotifiedFormmainRepository notifiedFormmainRepository;

    @Autowired
    private SubscribeShipRepository subscribeShipRepository;

    @Autowired
    private FormmainDao formmainDao;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Scheduled(fixedRate = 1000 * 60 * 10)
    public void listNewFormmain() {


        /** 取Mysql 请假结束时间在当前时间之后的请假记录 与 Mongo 中的记录去重
         * 请假结束时间在当前时间之前的请假记录, 在Mongo中删除
         * 去重后放入MQ消息队列, 存入Mongo
         */

        // mongo 删除过期请假单
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);

        try {
            NotifiedFormmainRepository.LOCK.lock();

            notifiedFormmainRepository.deleteByEndDateLessThan(c.getTimeInMillis());

            List<Formmain> formmainList = this.listEndDateAfterNowFormmains();

            // mysql请假单
            List<NotifiedFormmain> mysqlNotifiedFormmains = this.convertTo(formmainList);
            logger.debug("mysql获取请假单:" + mysqlNotifiedFormmains.size());
            mysqlNotifiedFormmains.stream().forEach(e -> logger.debug(e.toString()));

            // mongo已经通知订阅者请假单
            List<NotifiedFormmain> mongoNotifiedFormmains = notifiedFormmainRepository.findByEndDateGreaterThan(Calendar.getInstance().getTimeInMillis());
            Set<Long> mongoFormmainIdSet = this.formmainIdSet(mongoNotifiedFormmains);
            logger.debug("mongo已经通知订阅者请假单:" + mongoNotifiedFormmains.size());
            mongoNotifiedFormmains.stream().forEach(e -> logger.debug(e.toString()));

            // 未通知订阅者的请假单
            List<NotifiedFormmain> newNotifiedFormmains = new ArrayList<>();

            mysqlNotifiedFormmains.stream()
                    .filter(e -> !mongoFormmainIdSet.contains(e.getFormmainId()))
                    .forEach(e -> newNotifiedFormmains.add(e));

            logger.debug("未通知订阅者的请假单:" + newNotifiedFormmains.size());
            newNotifiedFormmains.stream().forEach(e -> {
                logger.debug(e.toString());
                List<SubscribeShip> subscribeShips = subscribeShipRepository.findByMemberId(e.getApplicantId());
                if (!subscribeShips.isEmpty()) {
                    List<String> subscriberMailAddrs = new ArrayList<>();
                    subscribeShips.forEach(s -> subscriberMailAddrs.add(s.getSubscriberMail()));
                    e.setSubscriberMailAddr(subscriberMailAddrs);
                }
            });


            //已经通知的请假单,是否还有未通知的订阅者
            List<NotifiedFormmain> listForNotify = new ArrayList<>();
            mongoNotifiedFormmains.stream()
                    .forEach( e -> {
                        List<String> allMail = new ArrayList<>();
                        subscribeShipRepository.findByMemberId(e.getApplicantId()).stream()
                                .forEach(s -> allMail.add(s.getSubscriberMail()));

                        // 移除已经通知的订阅者
                        allMail.removeAll(e.getSubscriberMailAddr());

                        NotifiedFormmain n = e.copyForEmptySubscriberMailAddr();
                        n.setSubscriberMailAddr(allMail);
                        if (!n.getSubscriberMailAddr().isEmpty())
                            listForNotify.add(n);

                        // 将新的订阅者加入Mongo消息记录中
                        e.getSubscriberMailAddr().addAll(allMail);

                    });

            listForNotify.addAll(newNotifiedFormmains);
            if (!listForNotify.isEmpty()) {
                rabbitTemplate.convertAndSend(ConstantSet.EXCHANGE_NAME, "", new Gson().toJson(listForNotify));
            }

            mongoNotifiedFormmains.addAll(newNotifiedFormmains);
            if(!mongoNotifiedFormmains.isEmpty())
                notifiedFormmainRepository.saveAll(mongoNotifiedFormmains);
        } catch (Exception e) {

        } finally {
            NotifiedFormmainRepository.LOCK.unlock();
        }
    }


    /**
     * 请假结束时间在当前时间之后的请假记录
     *
     * @return
     */
    private List<Formmain> listEndDateAfterNowFormmains() {
        Calendar currentDate = Calendar.getInstance();
        // 请假结束时间在当前时间之后
        java.sql.Date endDate = new java.sql.Date(currentDate.getTimeInMillis());
        List<Formmain> formmainList = formmainDao.listNewLeaveInfo(endDate);
        return formmainList;
    }


    private List<NotifiedFormmain> convertTo(List<Formmain> formmains) {
        List<NotifiedFormmain> notifiedFormmain = new ArrayList<>();
        formmains.stream().forEach(e -> notifiedFormmain.add(
                new NotifiedFormmain(e.getId(),
                        e.getApplicantId(),
                        e.getApplicantName(),
                        e.getApplicationDate().getTime(),
                        e.getStartDate().getTime(),
                        e.getEndDate().getTime(),
                        e.getLeaveDays()
                )));
        return notifiedFormmain;
    }

    private Set<Long> formmainIdSet(List<NotifiedFormmain> notifyFormmains) {
        Set<Long> set = new HashSet<>();
        notifyFormmains.stream().forEach(e -> set.add(e.getFormmainId()));
        return set;
    }

}
