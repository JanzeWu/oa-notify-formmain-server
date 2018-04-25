package com.ipin.formmain.service;

import com.google.gson.Gson;
import com.ipin.formmain.bean.*;
import com.ipin.formmain.consts.ConstantSet;
import com.ipin.formmain.dao.FormmainDao;
import com.ipin.formmain.dao.NotifiedFormmainRepository;
import com.ipin.formmain.dao.OrgMemberDao;
import com.ipin.formmain.dao.SubscribeShipRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * Created by janze on 4/17/18.
 */
@Service
public class SubscribeService {

    @Autowired
    private OrgMemberDao orgMemberDao;

    @Autowired
    private FormmainDao formmainDao;

    @Autowired
    private SubscribeShipRepository subscribeShipRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private NotifiedFormmainRepository notifiedFormmainRepository;


    public List<MemberResult> departmentMembers(String username){


        Map<String, List<SubscribeMember>> dm = new HashMap<>();
        List<OrgMember> members = orgMemberDao.listAll();
        List<SubscribeShip> subscribeShips = subscribeShipRepository
                .findBySubscriberMail(username.concat(ConstantSet.IPIN_MAIL_SUFFIX));

        Set<Long> memberIds = new HashSet<>();
        subscribeShips.stream().forEach( e -> memberIds.add(e.getMemberId()));
        members.stream().forEach(e -> {
            if( dm.get(e.getDepartment()) == null){
                dm.put(e.getDepartment(), new ArrayList<>());
            }
            SubscribeMember m = new SubscribeMember(e.getId() + "", e.getName());
            if(memberIds.contains(e.getId())){
                m.setSubscribed(true);
            }
            dm.get(e.getDepartment()).add(m);
        });

        List<MemberResult> res = new ArrayList<>();
        dm.keySet().stream().forEach( e -> {
            res.add(new MemberResult(e, dm.get(e)));
        });

        return res;
    }

    public boolean saveSubscribeShips(String username, List<Long> memberIds){
        List<SubscribeShip> subscribeShips = new ArrayList<>();
        String mailAddr = username.concat(ConstantSet.IPIN_MAIL_SUFFIX);
        OrgMember member = orgMemberDao.findByMailAddr(mailAddr);
        memberIds.stream().forEach( e -> {
            if(e != -1){
                subscribeShips.add(new SubscribeShip(member.getId(), member.getMailAddr(), e));
            }

        });
        List<SubscribeShip> subscribeShipList = subscribeShipRepository.findBySubscriberMail(mailAddr);
        subscribeShipRepository.deleteAll(subscribeShipList);
        return subscribeShipRepository.saveAll(subscribeShips).size() > 0 ? true : false;
    }

    public void notifySubscriber(String username, List<Long> memberIds) {

        Set<Long> set = new HashSet<>(memberIds);
        String mailAddr = username.concat(ConstantSet.IPIN_MAIL_SUFFIX);
        List<Formmain> formmainList = formmainDao.listNewLeaveInfo(new Date(Calendar.getInstance().getTimeInMillis()));
        List<String> subscriberMailAddrs = new ArrayList<>();
        subscriberMailAddrs.add(mailAddr);

        List<NotifiedFormmain> updateNotifiedFormmains = new ArrayList<>();
        List<NotifiedFormmain> mailNotifiedFormmains = new ArrayList<>();
        try {
            NotifiedFormmainRepository.LOCK.lock();
            formmainList.stream()
                    .filter( e -> set.contains(e.getApplicantId()))
                    .forEach( e -> {
                        e.setSubscriberMailAddrs(subscriberMailAddrs);
                        NotifiedFormmain n = notifiedFormmainRepository.findByFormmainId(e.getId());
                        if( n == null ){
                            n = new NotifiedFormmain(e.getId(), e.getApplicantId(), e.getApplicantName(), e.getApplicationDate().getTime(),
                                    e.getStartDate().getTime(), e.getEndDate().getTime(), e.getLeaveDays());
                        }
                        if( !n.getSubscriberMailAddr().contains(mailAddr) ){
                            n.getSubscriberMailAddr().add(mailAddr);
                            updateNotifiedFormmains.add(n);
                        }
                    });
            if( !updateNotifiedFormmains.isEmpty() ){
                notifiedFormmainRepository.saveAll(updateNotifiedFormmains);
                updateNotifiedFormmains.stream().forEach( e -> e.setSubscriberMailAddr( new ArrayList<String>(){{add(mailAddr);}}));
            }
        }finally {
            NotifiedFormmainRepository.LOCK.unlock();
        }

        rabbitTemplate.convertAndSend(ConstantSet.EXCHANGE_NAME, "", new Gson().toJson(updateNotifiedFormmains));
    }
}
