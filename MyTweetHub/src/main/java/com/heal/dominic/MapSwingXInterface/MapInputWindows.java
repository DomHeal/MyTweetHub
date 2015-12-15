package com.heal.dominic.MapSwingXInterface;

import com.heal.dominic.MapInterface.MapMarkers;
import twitter4j.GeoLocation;
import twitter4j.Query;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

public class MapInputWindows extends Map2 {

	private static final long serialVersionUID = 1L;

	public static void InputCord() {
		double res = 5;
		JPanel myPanel = new JPanel();
		myPanel.add(new JLabel("Latitude:"));
		myPanel.add(xField);
		myPanel.add(Box.createHorizontalStrut(15));
		myPanel.add(new JLabel("Longitude:"));
		myPanel.add(yField);

		int result = JOptionPane.showConfirmDialog(null, myPanel,
				"Please Enter Latitude and Longitude Values",
				JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			query = new Query();
			query.setGeoCode(new GeoLocation((Double.parseDouble(xField
					.getText())), Double.parseDouble(yField
					.getText())), res, Query.Unit.mi);
			query.count(100);
			MapMarkerCreator.locationMarkers();
		}
	}

	public static void InputID() {
		JTextField iD = new JTextField(15);
		iD.setText("574940850662285312");
		JPanel myPanel = new JPanel();
		myPanel.add(new JLabel("Enter ID"));
		myPanel.add(iD);

		int result = JOptionPane.showConfirmDialog(null, myPanel,
				"Please Enter the Tweet ID", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			mapViewer.removeAll();
			mapViewer.repaint();
			MapMarkerCreator.mentionsMarkers(Long.parseLong(iD.getText()));
		}
	}
	
	public static void InputUsernameID() {
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				JTextField iD = new JTextField(15);
				iD.setText("");
				JPanel myPanel = new JPanel();
				myPanel.add(new JLabel("Enter Username"));
				myPanel.add(iD);

				int result = JOptionPane.showConfirmDialog(null, myPanel,
						"Please Enter the Tweet ID", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					MapMarkers.usernameMarkers(iD.getText());

				}
			}

		});
	}


	public static void InputHashtag() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				final JTextField iD = new JTextField(15);
				iD.setText("");
				JPanel myPanel = new JPanel();
				myPanel.add(new JLabel("Enter Hashtag"));
				myPanel.add(iD);

				int result = JOptionPane.showConfirmDialog(null, myPanel,
						"Please Enter the Tweet ID", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					MapMarkers.hashtagMarkers(iD.getText());
					query = new Query();
					query.setQuery(iD.getText());
//					query.setGeoCode(new GeoLocation((Double.parseDouble(xField
//							.getText())), Double.parseDouble(yField
//							.getText())), Integer.MAX_VALUE, Query.Unit.mi);

					// TEST - UK
					query.setGeoCode(new GeoLocation(52.40912125231122, -2.0269775390625), Integer.MAX_VALUE, Query.Unit.mi);
					query.count(100);
					MapMarkerCreator.locationMarkers();
				}
			}

		});
	}

	public static void dataMine(){
		dataMine = new Timer(30000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				query = new Query();
				query.setSinceId(latestTweetID);
				query.setGeoCode(new GeoLocation(52.40912125231122, -2.0269775390625), Integer.MAX_VALUE, Query.Unit.mi);
				query.count(100);
				MapMarkerCreator.locationMarkers();
			}
		});
		dataMine.start();
	}


}
