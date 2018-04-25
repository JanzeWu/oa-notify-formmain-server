package com.ipin.formmain.bean;

/**
 * iPIN 公司员工在OA上的个人信息
 * Created by janze on 4/13/18.
 */
public class OrgMember {

    private Long id;
    private String name; //姓名
    private String code; //编号

    private String department;//主岗部门
    private String post;//岗位

    private String phoneNum;
    private String mailAddr;//邮箱



    public OrgMember(Long id, String name, String code, String department, String post, String phoneNum, String mailAddr) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.department = department;
        this.post = post;
        this.phoneNum = phoneNum;
        this.mailAddr = mailAddr;
    }

    public OrgMember(Long id, String name, String mailAddr) {
        this.id = id;
        this.name = name;
        this.mailAddr = mailAddr;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getDepartment() {
        return department;
    }

    public String getPost() {
        return post;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getMailAddr() {
        return mailAddr;
    }



    @Override
    public String toString() {
        return "OrgMember{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", department='" + department + '\'' +
                ", post='" + post + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", mailAddr='" + mailAddr + '\'' +
                '}';
    }
}
