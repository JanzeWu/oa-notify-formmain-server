package com.ipin.formmain.bean;

import java.util.List;

/**
 * Created by janze on 4/17/18.
 */
public class MemberResult {

    private String department;
    private List<SubscribeMember> members;

    public MemberResult(String department, List<SubscribeMember> members) {
        this.department = department;
        this.members = members;
    }

    public String getDepartment() {
        return department;
    }

    public List<SubscribeMember> getMembers() {
        return members;
    }
}
