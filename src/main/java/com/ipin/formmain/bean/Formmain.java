package com.ipin.formmain.bean;

import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by janze on 4/13/18.
 */
@Document(collection = "formmain")
public class Formmain {

    private long id;

    private long applicantId; //field0001 申请人

    private String applicantName;

    private Date applicationDate;

    private Timestamp startDate; //field0014 开始时间

    private Timestamp endDate; // field0015 结束时间

    private BigDecimal leaveDays; // field0031 请假天数

    private List<String> subscriberMailAddrs = new ArrayList<>();


    public Formmain(long id, long applicantId, Timestamp startDate, Timestamp endDate, BigDecimal leaveDays) {
        this.id = id;
        this.applicantId = applicantId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.leaveDays = leaveDays;
    }

    public long getId() {
        return id;
    }

    public long getApplicantId() {
        return applicantId;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public BigDecimal getLeaveDays() {
        return leaveDays;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public List<String> getSubscriberMailAddrs() {
        return subscriberMailAddrs;
    }

    public void setSubscriberMailAddrs(List<String> subscriberMailAddrs) {
        if( subscriberMailAddrs != null && !subscriberMailAddrs.isEmpty()){
            this.subscriberMailAddrs = new ArrayList<>(subscriberMailAddrs);
        }

    }

    @Override
    public String toString() {
        return "Formmain{" +
                "id=" + id +
                ", applicantId=" + applicantId +
                ", applicantName='" + applicantName + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", leaveDays=" + leaveDays +
                '}';
    }
}
