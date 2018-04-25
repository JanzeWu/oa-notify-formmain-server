package com.ipin.formmain.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by janze on 4/19/18.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class MailEntity {

    private String mailto;

    private String subject;

    private String body;


    public MailEntity(String mailto, String subject, String body) {
        this.mailto = mailto;
        this.subject = subject;
        this.body = body;
    }

    public String getMailto() {
        return mailto;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "MailEntity{" +
                "mailto='" + mailto + '\'' +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
