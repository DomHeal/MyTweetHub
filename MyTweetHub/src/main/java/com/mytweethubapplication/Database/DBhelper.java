package com.mytweethubapplication.Database;

import com.mytweethubapplication.MapSwingXInterface.DatabaseUser;
import com.mytweethubapplication.MapSwingXInterface.MapInterface;
import com.mytweethubapplication.MapSwingXInterface.SwingWaypoint;
import com.mytweethubapplication.MapSwingXInterface.SwingWaypointOverlayPainter;
import org.jdesktop.swingx.mapviewer.WaypointPainter;

import java.sql.*;
import java.util.Set;

/**
 * Created by Dominic on 17-Dec-15.
 */
public class DBhelper extends MapInterface{

    Connection c = null;
    Statement stmt = null;

    public static void read() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:data.db");
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery("select * from TWEETS;");
            while (rs.next()) {
                DatabaseUser x = new DatabaseUser();
                x.setScreenName(rs.getString("SCREEN_NAME"));
                x.setName(rs.getString("FULL_NAME"));
                x.setCreatedAt(rs.getString("CREATED_AT"));
                x.setTweetType(rs.getString("TWEET_TYPE"));
                x.setLongitude(rs.getString("LONGITUDE"));
                x.setLatitude(rs.getString("LATITUDE"));
                x.setText(rs.getString("TEXT"));
                x.setLikes(rs.getString("LIKES"));
                x.setRetweets(rs.getString("RETWEETS"));
                x.setPictureURL(rs.getString("PICTURE_URL"));
                waypoints.add(new SwingWaypoint(x));
                drawMarkers(waypoints);
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

            for(SwingWaypoint x : waypoints) {
                prep.setString(1, String.valueOf(x.getUser().getUser().getScreenName()));
                prep.setString(2, String.valueOf(x.getUser().getUser().getName()));
                prep.setString(3, String.valueOf(x.getUser().getCreatedAt()));
                prep.setString(4, String.valueOf(x.getTweetType()));
                prep.setString(5, String.valueOf(x.getUser().getGeoLocation().getLongitude()));
                prep.setString(6, String.valueOf(x.getUser().getGeoLocation().getLatitude()));
                prep.setString(7, String.valueOf(x.getUser().getText()));
                prep.setString(8, String.valueOf(x.getUser().getFavoriteCount()));
                prep.setString(9, String.valueOf(x.getUser().getRetweetCount()));
                prep.setString(10, String.valueOf(x.getUser().getUser().getProfileImageURL()));
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

    private static void drawMarkers(Set<SwingWaypoint> waypoints) {
        WaypointPainter<SwingWaypoint> swingWaypointPainter = new SwingWaypointOverlayPainter();
        swingWaypointPainter.setWaypoints(waypoints);
        mapViewer.setOverlayPainter(swingWaypointPainter);
        for (SwingWaypoint w : waypoints) {
            mapViewer.add(w.getButton());
        }
    }
}
