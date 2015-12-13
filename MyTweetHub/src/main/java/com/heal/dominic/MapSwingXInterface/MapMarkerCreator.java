package com.heal.dominic.MapSwingXInterface;


import com.heal.dominic.ResourceManager.SoundController;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.WaypointPainter;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import twitter4j.*;

import javax.swing.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Dominic on 13-Dec-15.
 */
public class MapMarkerCreator extends Map2 {
    static Set<SwingWaypoint> waypoints = new HashSet<SwingWaypoint>();

    public static void locationMarkers() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                try {
                    QueryResult result = twitter.search(query);
                    for (int i = 0; result.getTweets().size() > i; i++) {
                        try {
                            GeoPosition tweetCoordinate = new GeoPosition(result.getTweets().get(i).getGeoLocation().getLatitude(),
                                    result.getTweets().get(i).getGeoLocation().getLongitude());
                            String username = result.getTweets().get(i).getUser().getScreenName();
                            String msg = result.getTweets().get(i).getText();
                            String picture = result.getTweets().get(i).getUser().getProfileImageURL();

                            int counts = result.getTweets().get(i).getFavoriteCount();
                            waypoints.add(new SwingWaypoint(tweetCoordinate, username, msg, picture, "Normal"));

                        } catch (Exception e) {
                        }
                    }
                } catch (TwitterException e1) {
                    System.out.println("Error getting results");
                    e1.printStackTrace();
                }
                drawMarkers(waypoints);

            }
        });

    }

    public static void mentionsMarkers(long tweetID) {
        Date tweetDate = null;
        try {
            Status sourceTweet = twitter.showStatus(tweetID);
            tweetDate = sourceTweet.getCreatedAt();
            GeoPosition sourceCoordinate = new GeoPosition(sourceTweet
                    .getGeoLocation().getLatitude(), sourceTweet.getGeoLocation().getLongitude());
            String username = sourceTweet.getUser().getScreenName();
            String msg = sourceTweet.getText();
            String picture = sourceTweet.getUser().getProfileImageURL();
            waypoints.add(new SwingWaypoint(sourceCoordinate, username, msg, picture, "Source"));
            // add
        } catch (TwitterException e1) {
            JOptionPane.showMessageDialog(null, "Twitter Exception",
                    "Opps! An Error has occurred!", JOptionPane.INFORMATION_MESSAGE);
        }
        try {
            ResponseList<Status> mentions = twitter.getMentionsTimeline(new Paging().count(100));
            for (int i = 0; mentions.size() > i; i++) {
                if (mentions.get(i).getCreatedAt().after(tweetDate) && mentions.get(i).getGeoLocation() != null) {
                    GeoPosition tweetCoordinate = new GeoPosition(mentions.get(i)
                            .getGeoLocation().getLatitude(), mentions.get(i)
                            .getGeoLocation().getLongitude());

                    String username = mentions.get(i).getUser().getScreenName();
                    String msg = mentions.get(i).getText();
                    String picture = mentions.get(i).getUser().getProfileImageURL();

                    waypoints.add(new SwingWaypoint(tweetCoordinate, username, msg, picture, "Normal"));
                }
            }
        } catch (TwitterException e) {
            JOptionPane.showMessageDialog(null, "Twitter Exception",
                    "Opps! An Error has occurred!", JOptionPane.INFORMATION_MESSAGE);
        }

        drawMarkers(waypoints);
        //connectionMarkers();
        SoundController.playTickSound();
        JOptionPane.showMessageDialog(null, "We have found " + (waypoints.size() - 1) + " Tweets",
                "Searching Complete", JOptionPane.INFORMATION_MESSAGE);
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
