package com.heal.dominic.MapSwingXInterface;

import com.heal.dominic.MapInterface.Marker;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.WaypointPainter;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import twitter4j.QueryResult;
import twitter4j.TwitterException;

import javax.swing.*;
import java.util.Arrays;
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
                            waypoints.add(new SwingWaypoint(tweetCoordinate, username, msg,  picture));

                        } catch (Exception e) {
                        }
                    }
                    WaypointPainter<SwingWaypoint> swingWaypointPainter = new SwingWaypointOverlayPainter();
                    swingWaypointPainter.setWaypoints(waypoints);
                    mapViewer.setOverlayPainter(swingWaypointPainter);
                    for (SwingWaypoint w : waypoints) {
                        mapViewer.add(w.getButton());
                    }
                } catch (TwitterException e1) {
                    System.out.println("Error getting results");
                    e1.printStackTrace();
                }
            }
        });
        
    }
}
