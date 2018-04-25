package com.ipin.formmain.bean;

/**
 * Created by janze on 4/19/18.
 */
public class SubscribeMember {

    private String id;

    private String name;

    private boolean isSubscribed;

    public SubscribeMember(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isSubscribed() {
        return isSubscribed;
    }

    public void setSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
    }
}
