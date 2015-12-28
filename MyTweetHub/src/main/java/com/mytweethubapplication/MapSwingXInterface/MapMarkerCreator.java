package com.mytweethubapplication.MapSwingXInterface;


import com.mytweethubapplication.ResourceManager.SoundController;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.WaypointPainter;
import twitter4j.*;

import javax.swing.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by Dominic on 13-Dec-15.
 */
public class MapMarkerCreator extends MapInterface {

    public static void locationMarkers() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    QueryResult result = twitter.search(query);
                    latestTweetID = result.getMaxId();
                    for (int i = 0; result.getTweets().size() > i; i++) {
                        try {
                            if (result.getTweets().get(i).getGeoLocation() !=null) {
                                GeoPosition tweetCoordinate = new GeoPosition(result.getTweets().get(i).getGeoLocation().getLatitude(),
                                        result.getTweets().get(i).getGeoLocation().getLongitude());

                                Status user = result.getTweets().get(i);
                                waypoints.add(new SwingWaypoint(tweetCoordinate, user, "Normal"));
                            }
                        } catch (Exception e) {
                            System.out.println("error");
                            e.printStackTrace();
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

            Status user = sourceTweet;
            waypoints.add(new SwingWaypoint(sourceCoordinate, user, "Source"));
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

                    Status user = mentions.get(i);
                    waypoints.add(new SwingWaypoint(tweetCoordinate, user, "Normal"));
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
