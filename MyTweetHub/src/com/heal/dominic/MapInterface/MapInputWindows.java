package com.heal.dominic.MapInterface;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import twitter4j.GeoLocation;
import twitter4j.Query;

public class MapInputWindows extends Map {
	private static final long serialVersionUID = 1L;

	public static void InputCord() {
		double res = 5;
		String resUnit = "mi";

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
			map().removeAllMapMarkers();
			map().removeAllMapPolygons();

			query = new Query().geoCode(new GeoLocation((Double.parseDouble(xField
					.getText())), Double.parseDouble(yField
							.getText())), res, resUnit);
			query.count(100);
			MapMarkers.locationMarkers();
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
			map().removeAllMapMarkers();
			map().removeAllMapPolygons();
			MapMarkers.mentionsMarkers(Long.parseLong(iD.getText()));
		}
	}
	
	public static void InputUsernameID() {
		JTextField iD = new JTextField(15);
		iD.setText("");
		JPanel myPanel = new JPanel();
		myPanel.add(new JLabel("Enter Username"));
		myPanel.add(iD);

		int result = JOptionPane.showConfirmDialog(null, myPanel,
				"Please Enter the Tweet ID", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			map().removeAllMapMarkers();
			map().removeAllMapPolygons();
			MapMarkers.usernameMarkers(iD.getText());
		}
	}
	
}
