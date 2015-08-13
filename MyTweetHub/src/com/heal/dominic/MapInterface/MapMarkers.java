package com.heal.dominic.MapInterface;

import java.awt.Color;
import java.util.Date;
import java.util.List;
import twitter4j.Status;

import javax.swing.JOptionPane;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import org.openstreetmap.gui.jmapviewer.Style;

import com.heal.dominic.ResourceManager.SoundController;

import twitter4j.Paging;
import twitter4j.TwitterException;

public class MapMarkers extends Map{

	private static final long serialVersionUID = -9196714448884139090L;

	public static void locationMarkers() {
		try {
			result = twitter.search(query);
			for (int i = 0; result.getTweets().size() > i; i++) {
				try{
				tweetCoordinate = new Coordinate(result.getTweets().get(i).getGeoLocation().getLatitude(),
						result.getTweets().get(i).getGeoLocation().getLongitude());
				Style style = new Style(Color.BLACK, Color.YELLOW, null, null);
				 String TweetUsername = result.getTweets().get(i).getUser().getScreenName() + ": " + result.getTweets().get(i).getText();
				map().addMapMarker(new Marker(layer, TweetUsername,tweetCoordinate, style));
				} catch(Exception e){}
			}
			layer.setVisibleTexts(chckbxStatusVisible.isSelected());
			if (chckbxStatusVisible.isSelected()){
				
			}
		} catch (TwitterException e1) {
			System.out.println("Error getting results");
			e1.printStackTrace();
		}
	}

	public static void mentionsMarkers(long tweetID) {
		Date tweetDate = null;
		try {
			tweetDate = twitter.showStatus(tweetID).getCreatedAt();
			sourceCoordinate = new Coordinate(twitter.showStatus(tweetID)
					.getGeoLocation().getLatitude(), twitter
					.showStatus(tweetID).getGeoLocation().getLongitude());
			Style style = new Style(Color.BLACK, Color.RED, null, null);
			map().addMapMarker(
					new SourceMarker(null, "Source", sourceCoordinate, style));
		} catch (TwitterException e1) {
			JOptionPane.showMessageDialog(null, "Twitter Exception",
					"Opps! An Error has occurred!", JOptionPane.INFORMATION_MESSAGE);
		}
		try {
			mentions = twitter.getMentionsTimeline(new Paging().count(100));
			for (int i = 0; mentions.size() > i; i++) {
				if (mentions.get(i).getCreatedAt().after(tweetDate) == true
						&& mentions.get(i).getGeoLocation() != null) {
					tweetCoordinate = new Coordinate(mentions.get(i)
							.getGeoLocation().getLatitude(), mentions.get(i)
							.getGeoLocation().getLongitude());
					Style style = new Style(Color.BLACK, Color.BLUE, null,
							null);
					map().addMapMarker(new Marker(layer, mentions.get(i).getUser().getScreenName()+ ": " + mentions.get(i).getText(),	tweetCoordinate, style));
				}
			}

		} catch (TwitterException e) {
			JOptionPane.showMessageDialog(null, "Twitter Exception",
					"Opps! An Error has occurred!", JOptionPane.INFORMATION_MESSAGE);
		
		}
		connectionMarkers();
		layer.setVisibleTexts(chckbxStatusVisible.isSelected());
		SoundController.playTickSound();
		JOptionPane.showMessageDialog(null, "We have found "
				+ (map().getMapMarkerList().size() - 1) + " Tweets",
				"Searching Complete", JOptionPane.INFORMATION_MESSAGE);
	}

	public static void connectionMarkers() {
		for (int i = 1; i < map().getMapMarkerList().size(); i++) {
			System.out.println(map().getMapMarkerList().get(i));
			map().addMapPolygon(new MapPolygonImpl(layer2, null, sourceCoordinate, 
					map().getMapMarkerList().get(i).getCoordinate(), 
					map().getMapMarkerList().get(i).getCoordinate()));
		}
		map().setMapPolygonsVisible(showConnection.isSelected());
	}

	public static void usernameMarkers(String username) {
		try {
			List<Status> statuses;
			statuses = twitter.getUserTimeline(username);
			
			 for (Status status : statuses) {
					
				 if (status.getGeoLocation() != null){
					 Coordinate tweetCoordinate = new Coordinate(status.getGeoLocation().getLatitude(), status.getGeoLocation().getLongitude());
					 map().addMapMarker(new Marker(layer, status.getUser().getScreenName()+ ": " + status.getText(),	tweetCoordinate, null));
				 }
		        }
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}
}