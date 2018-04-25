package com.ipin.formmain.dao;


import com.ipin.formmain.bean.Formmain;
import com.ipin.formmain.db.MysqlConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by janze on 4/13/18.
 */
@Repository
public class FormmainDao {


    private static Logger logger = LoggerFactory.getLogger(Formmain.class);
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private MysqlConnectionPool connectionPool;

    public Formmain fetchOne(){
        Formmain formmain = null;
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            String sql = "SELECT id, field0001, field0014, field0015, field0031 FROM v5.formmain_0041 limit 1;";
            conn = connectionPool.getConnection();
            pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()){
                formmain = new Formmain(rs.getLong("id"),
                        rs.getLong("field0001"),
                        rs.getTimestamp("field0014"),
                        rs.getTimestamp("field0015"),
                        rs.getBigDecimal("field0031"));
                logger.info(formmain.toString());
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            connectionPool.release(conn, pst);
        }
        return formmain;
    }


    /**
     * @param endDate
     * @return
     */
    public List<Formmain> listNewLeaveInfo(Date endDate){
        List<Formmain> formmainList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            String sql = "select\n" +
                    "    formmain.id,\n" +
                    "    formmain.applicant_id,\n" +
                    "    member.NAME,\n" +
                    "    formmain.application_date,\n" +
                    "    formmain.start_date,\n" +
                    "    formmain.end_date,\n" +
                    "    formmain.leader_option,\n" +
                    "    formmain.leave_days\n" +
                    "from (\n" +
                    "    select\n" +
                    "        ID\n" +
                    "            as id,\n" +
                    "        field0001\n" +
                    "            as applicant_id,\n" +
                    "        field0002\n" +
                    "            as application_date,\n" +
                    "        left(coalesce(field0010, field0012, field0013, field0032), 4)\n" +
                    "            as leader_option,\n" +
                    "        field0014\n" +
                    "            as start_date,\n" +
                    "        field0015\n" +
                    "            as end_date,\n" +
                    "        field0031\n" +
                    "            as leave_days\n" +
                    "    from\n" +
                    "        v5.formmain_0041\n" +
                    "    where\n" +
                    "        field0001 is not null\n" +
                    "        and field0002 is not null\n" +
                    "        and field0014 is not null\n" +
                    "        and field0015 is not null\n" +
                    "        and field0015 >=?\n" +
                    ") formmain\n" +
                    "left join\n" +
                    "    v5.org_member member\n" +
                    "on\n" +
                    "    member.ID = formmain.applicant_id\n" +
                    "where\n" +
                    "    formmain.leader_option = '【同意】'\n" +
                    "order by\n" +
                    "    application_date;";

            conn = connectionPool.getConnection();
            pst = conn.prepareStatement(sql);
            pst.setDate(1, endDate, Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai")));
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                //long id, long applicantId, Timestamp startDate, Timestamp endDate, BigDecimal leaveDays
                Formmain f = new Formmain(
                        rs.getLong("id"),
                        rs.getLong("applicant_id"),
                        rs.getTimestamp("start_date"),
                        rs.getTimestamp("end_date"),
                        rs.getBigDecimal("leave_days")
                );
                f.setApplicantName(rs.getString("NAME"));
                f.setApplicationDate(rs.getDate("application_date"));
                formmainList.add(f);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            connectionPool.release(conn, pst);
        }
        return formmainList;
    }
}
