package com.mytweethubapplication.Database;

import com.mytweethubapplication.MapSwingXInterface.SwingWaypoint;

import java.sql.*;
import java.util.Set;

/**
 * Created by Dominic on 17-Dec-15.
 */
public class DBhelper {

    Connection c = null;
    Statement stmt = null;

    public static void read() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:data.db");
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery("select * from TWEETS;");
            while (rs.next()) {
                System.out.println("SCREEN_NAME = " + rs.getString("SCREEN_NAME") + " | FULL_NAME = " + rs.getString("FULL_NAME"));
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void insert(Set<SwingWaypoint> waypoints) {
        System.out.println("inserting");
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:data.db");
            Statement stat = conn.createStatement();

            stat.executeUpdate("create table IF NOT EXISTS TWEETS (SCREEN_NAME, FULL_NAME, CREATED_AT, TWEET_TYPE, LONGITUDE, LATITUDE , TEXT, LIKES, RETWEETS, PICTURE_URL);");
            PreparedStatement prep = conn.prepareStatement(
                    "insert into TWEETS values (?,?,?,?,?,?,?,?,?,?);");

            for(SwingWaypoint x :waypoints) {
                prep.setString(1, String.valueOf(x.getUser().getUser().getScreenName()));
                prep.setString(2, String.valueOf(x.getUser().getUser().getName()));
                prep.setString(3, String.valueOf(x.getUser().getCreatedAt()));
                prep.setString(4, String.valueOf(x.getTweetType()));
                prep.setString(5, String.valueOf(x.getUser().getGeoLocation().getLongitude()));
                prep.setString(6, String.valueOf(x.getUser().getGeoLocation().getLatitude()));
                prep.setString(7, String.valueOf(x.getUser().getText()));
                prep.setString(8, String.valueOf(x.getUser().getFavoriteCount()));
                prep.setString(9, String.valueOf(x.getUser().getRetweetCount()));
                prep.setString(10, String.valueOf(x.getUser().getUser().getOriginalProfileImageURL()));
                prep.addBatch();
            }
            conn.setAutoCommit(false);
            prep.executeBatch();
            conn.setAutoCommit(true);

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void delete() {

    }
}
