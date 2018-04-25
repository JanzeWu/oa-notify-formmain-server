package com.ipin.formmain.dao;

import com.ipin.formmain.bean.OrgMember;
import com.ipin.formmain.db.MysqlConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by janze on 4/17/18.
 */
@Repository
public class OrgMemberDao {

    private static Logger logger = LoggerFactory.getLogger(OrgMemberDao.class);

    @Autowired
    private MysqlConnectionPool mysqlConnectionPool;

    public List<OrgMember> listAll(){

        List<OrgMember> orgMemberList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pst = null;

        try {
            conn = mysqlConnectionPool.getConnection();
            String sql = "select\n" +
                    "    m.ID\n" +
                    "        as id,\n" +
                    "    m.NAME \n" +
                    "        as name,\n" +
                    "    m.CODE \n" +
                    "        as code,\n" +
                    "    u.NAME \n" +
                    "        as department,\n" +
                    "    p.NAME \n" +
                    "        as post,\n" +
                    "    m.EXT_ATTR_1\n" +
                    "        as phone_num,\n" +
                    "    m.EXT_ATTR_2\n" +
                    "        as mail_addr\n" +
                    "from\n" +
                    "    v5.org_member m\n" +
                    "left join\n" +
                    "    v5.org_unit u\n" +
                    "on  \n" +
                    "    u.ID = m.ORG_DEPARTMENT_ID\n" +
                    "left join\n" +
                    "    v5.org_post p\n" +
                    "on  \n" +
                    "    p.ID = m.ORG_POST_ID\n" +
                    "where\n" +
                    "    m.NAME is not null\n" +
                    "    and m.STATE = 1\n" +
                    "    and m.IS_LOGINABLE = 1\n" +
                    "    and length(m.EXT_ATTR_2) > 0 \n" +
                    "    and m.ID <> -2346628314917286204\n" +
                    "order by department, post;";
            pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                OrgMember m = new OrgMember(rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("code"),
                        rs.getString("department"),
                        rs.getString("post"),
                        rs.getString("phone_num"),
                        rs.getString("mail_addr"));
                orgMemberList.add(m);

            }

        }catch (SQLException e){
            e.printStackTrace();
            logger.error(e.getMessage());
        }finally {
            mysqlConnectionPool.release(conn, pst);
        }

        return orgMemberList;

    }

    public OrgMember findByMailAddr(String mailAddr ){
        OrgMember orgMember = null;
        Connection con = null;
        PreparedStatement pst = null;
        try{
            con = mysqlConnectionPool.getConnection();
            String sql = "select\n" +
                    "    ID\n" +
                    "        as id,\n" +
                    "    NAME\n" +
                    "        as name,\n" +
                    "    EXT_ATTR_2\n" +
                    "        as mail_addr\n" +
                    "from\n" +
                    "    v5.org_member\n" +
                    "where\n" +
                    "    EXT_ATTR_2 = ?\n" +
                    "limit 1;\n";
            pst = con.prepareStatement(sql);
            pst.setString(1, mailAddr);

            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                orgMember = new OrgMember(rs.getLong("id"),
                        rs.getString("name" ),
                        rs.getString("mail_addr"));
            }
        }catch (SQLException e){
            e.printStackTrace();
            logger.error(e.getMessage());
        }finally {
            mysqlConnectionPool.release(con, pst);
        }
        return orgMember;
    }

}
