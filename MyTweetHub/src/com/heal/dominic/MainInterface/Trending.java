package com.heal.dominic.MainInterface;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;

import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.alee.laf.list.WebList;

public class Trending extends Application {
	public static void getTrending() {
		try {
			Twitter twitter = getTwitter();
			DefaultListModel<String> model = new DefaultListModel<String>();
			Trends trends = twitter.getPlaceTrends(23424975);
			for (Trend trend : trends.getTrends()) {
				model.addElement(trend.getName());
			}
			
			final WebList trendingList = new WebList();
			trendingList.setModel(model);
			setTrendingList(trendingList);
			trendingList.setFixedCellHeight(30);
			trendingList.setOpaque(false);
			trendingList.setCellRenderer(new JlistRenderer());
			trendingList.setDecorateSelection(false);
			

			trendingList.addMouseMotionListener(new MouseAdapter() {
				public void mouseMoved(MouseEvent me) {
					Point p = new Point(me.getX(), me.getY());
					trendingList.setSelectedIndex(trendingList
							.locationToIndex(p));
				}
			});

		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to get current trends: "
					+ te.getMessage());
		}
		
		System.out.println("Trending Thread: Done");
	}
}
