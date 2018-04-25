package com.ipin.formmain.dao;

import com.ipin.formmain.bean.SubscribeShip;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by janze on 4/18/18.
 */
public interface SubscribeShipRepository extends MongoRepository<SubscribeShip, String> {

    List<SubscribeShip> findBySubscriberMail(String subscriberMail);

    List<SubscribeShip> findByMemberId(long memberId);

}
