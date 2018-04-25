package com.ipin.formmain.bean;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by janze on 4/18/18.
 */

@Document(collection = "subscribe_ship")
public class SubscribeShip {


    @Id
    private String id;

    //订阅者
    private Long subscriberId;

    //订阅者的邮箱
    private String subscriberMail;

    //被订阅的人
    private Long memberId;

    public SubscribeShip(Long subscriberId, String subscriberMail, Long memberId) {
        this.subscriberId = subscriberId;
        this.subscriberMail = subscriberMail;
        this.memberId = memberId;
    }

    public Long getSubscriberId() {
        return subscriberId;
    }

    public String getSubscriberMail() {
        return subscriberMail;
    }

    public Long getMemberId() {
        return memberId;
    }

    @Override
    public String toString() {
        return "SubscribeShip{" +
                "subscriberId=" + subscriberId +
                ", subscriberMail=" + subscriberMail +
                ", memberId=" + memberId +
                '}';
    }
}
