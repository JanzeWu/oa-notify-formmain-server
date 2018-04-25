package com.ipin.formmain.bean;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 已经通知订阅者的请假单
 * Created by janze on 4/23/18.
 */
@Document(collection = "notified_formmain")
public class NotifiedFormmain {

    @Id
    private String id;

    private long formmainId;

    private long applicantId; //field0001 申请人

    private String applicantName;

    private long applicationDate;

    private long startDate; //field0014 开始时间

    private long endDate; // field0015 结束时间

    private BigDecimal leaveDays; // field0031 请假天数

    private List<String> subscriberMailAddr = new ArrayList<>();


    public NotifiedFormmain(long formmainId, long applicantId, String applicantName, long applicationDate, long startDate, long endDate, BigDecimal leaveDays) {
        this.formmainId = formmainId;
        this.applicantId = applicantId;
        this.applicantName = applicantName;
        this.applicationDate = applicationDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.leaveDays = leaveDays;
    }

    public void setSubscriberMailAddr(List<String> subscriberMailAddr) {
        this.subscriberMailAddr = subscriberMailAddr;
    }


    public long getFormmainId() {
        return formmainId;
    }

    public long getApplicantId() {
        return applicantId;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public long getApplicationDate() {
        return applicationDate;
    }

    public long getStartDate() {
        return startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public BigDecimal getLeaveDays() {
        return leaveDays;
    }

    public List<String> getSubscriberMailAddr() {
        return subscriberMailAddr;
    }


    public NotifiedFormmain copyForEmptySubscriberMailAddr(){
        NotifiedFormmain one = new NotifiedFormmain(this.formmainId,
                this.applicantId,
                this.applicantName,
                this.applicationDate,
                this.startDate,
                this.endDate,
                this.leaveDays);
        return one;
    }
}
