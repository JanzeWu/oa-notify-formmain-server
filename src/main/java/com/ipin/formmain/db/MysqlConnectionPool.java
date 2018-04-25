package com.ipin.formmain.db;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Created by janze on 4/13/18.
 */

@Component
public class MysqlConnectionPool {


    @Value("${spring.datasource.url}")
    private String mysqlUrl;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;


    private static MysqlConnectionPoolDataSource pooled ;



    private void initMysqlConnectionPool() throws SQLException{
        pooled = new MysqlConnectionPoolDataSource();
        pooled.setUser(username);
        pooled.setPassword(password);
        pooled.setURL(mysqlUrl);
        pooled.setCharacterEncoding("utf-8");
    }

    public Connection getConnection(){
        Connection conn = null;
        try {
            if(pooled == null){
                synchronized (this){
                    if(pooled == null){
                        this.initMysqlConnectionPool();
                    }
                }
            }
            conn = pooled.getConnection();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return conn;
    }


    public void release(Connection conn, Statement statement){
        try {
            if(conn != null){
                conn.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        try {
            if(statement != null){
                statement.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}
