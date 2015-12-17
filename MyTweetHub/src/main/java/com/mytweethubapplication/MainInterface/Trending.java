package com.mytweethubapplication.MainInterface;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import com.mytweethubapplication.Login.LoginGUI;
import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.alee.laf.list.WebList;

public class Trending extends Application {

	private static final long serialVersionUID = 4885425671602620520L;

	public static void getTrending() {
		try {
			Twitter twitter = LoginGUI.getTwitter();
			DefaultListModel<String> model = new DefaultListModel<String>();
			Trends trends = twitter.getPlaceTrends(23424975);
			for (Trend trend : trends.getTrends()) {
				model.addElement(trend.getName());
			}
			
			final WebList trendingList = new WebList();
			
			trendingList.setModel(model);	
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
			setTrendingList(trendingList);

		} catch (TwitterException te) {
			JOptionPane.showMessageDialog(frame,
					"Request Limit Reached! Try Again in " + te.getRateLimitStatus().getSecondsUntilReset(),
					"Twitter Exception", JOptionPane.WARNING_MESSAGE);
		}
	}
}
