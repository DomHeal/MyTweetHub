package com.mytweethubapplication.Database;

import java.sql.*;

/**
 * Created by Dominic on 17-Dec-15.
 */
public class DBhelper {

    Connection c = null;
    Statement stmt = null;

    public void createDB(){
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("meow");
            c = DriverManager.getConnection("jdbc:sqlite:");
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "CREATE TABLE TWEETS " +
                    "(ID INT PRIMARY KEY             NOT NULL," +
                    " SCREEN_NAME            TEXT    NOT NULL," +
                    " FULL_NAME              TEXT    NOT NULL," +
                    " CREATED_AT             TEXT    NOT NULL," +
                    " TWEET_TYPE             TEXT    NOT NULL," +
                    " LONGITUDE              INT     NOT NULL," +
                    " LATITUDE               INT     NOT NULL," +
                    " TEXT                   TEXT    NOT NULL," +
                    " LIKES                  TEXT    NOT NULL," +
                    " RETWEETS               TEXT    NOT NULL," +
                    " PICTURE_URL            TEXT    NOT NULL)";
            stmt.executeUpdate(sql);
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        System.out.println("Table created successfully");
    }

    public void insert(){

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "INSERT INTO TWEETS (SCREEN_NAME, FULL_NAME, CREATED_AT" +
                    ", TWEET_TYPE, LONGITUDE, LATITUDE , TEXT, LIKES, RETWEETS, PICTURE_URL) " +
                    "VALUES ('Paul', 'Paul', 'California', 'Paul', 1, 2, 'Paul', 'Paul', 'Paul', 'Paul' );";
            stmt.executeUpdate(sql);
            c.commit();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        System.out.println("Records created successfully");
    }

    public void print(){
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery("select * from TWEETS;");
            while (rs.next()) {
                System.out.println("name = " + rs.getString("name"));
                System.out.println("job = " + rs.getString("occupation"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void test() {
        try {
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:data.db");
        Statement stat = conn.createStatement();

        stat.executeUpdate("create table IF NOT EXISTS TWEETS (SCREEN_NAME, FULL_NAME, CREATED_AT, TWEET_TYPE, LONGITUDE, LATITUDE , TEXT, LIKES, RETWEETS, PICTURE_URL);");
        PreparedStatement prep = conn.prepareStatement(
                "insert into TWEETS values (?,?,?,?,?,?,?,?,?,?);");

        prep.setString(1, "@DomHeal");
        prep.setString(2, "Dominic Heal");
        prep.setString(3, "29/12/1992");
        prep.setString(4, "Normal");
        prep.setString(5, "52.40912125231122");
        prep.setString(6, "-2.0269775390625");
        prep.setString(7, "Hello");
        prep.setString(8, "1");
        prep.setString(9, "2");
        prep.setString(10, "URL");
        prep.addBatch();
            prep.addBatch();

        conn.setAutoCommit(false);
        prep.executeBatch();
        conn.setAutoCommit(true);

        ResultSet rs = stat.executeQuery("select * from TWEETS;");
        while (rs.next()) {
            System.out.print("SCREEN_NAME= " + rs.getString("SCREEN_NAME") + "FULL_NAME = " + rs.getString("FULL_NAME"));
        }
        rs.close();
        conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
