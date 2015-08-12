package com.heal.dominic.MainInterface;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;


import twitter4j.Status;
import twitter4j.TwitterException;


public class Info  {

	private JFrame frame;

	/**
	 * Create the application.
	 * @param status 
	 * @throws MalformedURLException 
	 */
	public Info(Status status) throws TwitterException, MalformedURLException {
		initialize(status);

	}

	/**
	 * Initialize the contents of the frame.
	 * @param status 
	 * @throws MalformedURLException 
	 */
	private void initialize(Status status) throws MalformedURLException {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.DARK_GRAY);
		frame.setAlwaysOnTop(true);
		frame.setUndecorated(true);
		frame.setBounds(100, 100, 392, 184);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblProfilePic = new JLabel();
		lblProfilePic.setBorder(new LineBorder(new Color(0, 0, 0)));
		lblProfilePic.setBackground(Color.DARK_GRAY);
		lblProfilePic.setIcon(new ImageIcon(new URL (status.getUser().getBiggerProfileImageURL())));
		lblProfilePic.setBounds(23, 22, 66, 64);
		frame.getContentPane().add(lblProfilePic);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setForeground(Color.WHITE);
		lblName.setBounds(99, 22, 66, 14);
		frame.getContentPane().add(lblName);
		
		JLabel lblScreenName = new JLabel("Screen Name:");
		lblScreenName.setForeground(Color.WHITE);
		lblScreenName.setBounds(99, 47, 106, 14);
		frame.getContentPane().add(lblScreenName);
		
		JLabel lblLocation = new JLabel("Location:");
		lblLocation.setForeground(Color.WHITE);
		lblLocation.setBounds(99, 72, 66, 14);
		frame.getContentPane().add(lblLocation);
		
		JLabel lblFollowers = new JLabel("Followers:");
		lblFollowers.setForeground(Color.WHITE);
		lblFollowers.setBounds(116, 159, 66, 14);
		frame.getContentPane().add(lblFollowers);
		
		JLabel lblFollowing = new JLabel("Following:");
		lblFollowing.setForeground(Color.WHITE);
		lblFollowing.setBounds(241, 159, 60, 14);
		frame.getContentPane().add(lblFollowing);
		
		JLabel lblNameAns  = new JLabel(status.getUser().getName());
		lblNameAns.setForeground(Color.WHITE);
		lblNameAns.setBounds(204, 22, 178, 14);
		frame.getContentPane().add(lblNameAns);
		
		JLabel lblScreenNameAns = new JLabel("@" + status.getUser().getScreenName());
		lblScreenNameAns.setForeground(Color.WHITE);
		lblScreenNameAns.setBounds(204, 47, 178, 14);
		frame.getContentPane().add(lblScreenNameAns);
		
		JLabel lblLocationAns = new JLabel(status.getUser().getLocation());
		lblLocationAns.setForeground(Color.WHITE);
		lblLocationAns.setBounds(204, 72, 178, 14);
		frame.getContentPane().add(lblLocationAns);
		
		JLabel lblFollowersAns = new JLabel(Integer.toString(status.getUser().getFollowersCount()));
		lblFollowersAns.setForeground(Color.WHITE);
		lblFollowersAns.setBounds(179, 159, 66, 14);
		frame.getContentPane().add(lblFollowersAns);
		
		JLabel lblFollowingAns = new JLabel(Integer.toString(status.getUser().getFriendsCount()));
		lblFollowingAns.setForeground(Color.WHITE);
		lblFollowingAns.setBounds(311, 159, 71, 14);
		frame.getContentPane().add(lblFollowingAns);
		
		JLabel lblTweets = new JLabel("Tweets:");
		lblTweets.setForeground(Color.WHITE);
		lblTweets.setBounds(10, 159, 46, 14);
		frame.getContentPane().add(lblTweets);
		
		JLabel lblTweetsAns = new JLabel(Integer.toString(status.getUser().getStatusesCount()));
		lblTweetsAns.setForeground(Color.WHITE);
		lblTweetsAns.setBounds(63, 159, 46, 14);
		frame.getContentPane().add(lblTweetsAns);
		
		JLabel lblJoinedTwitter = new JLabel("Joined Twitter:");
		lblJoinedTwitter.setForeground(Color.WHITE);
		lblJoinedTwitter.setBounds(99, 97, 99, 14);
		frame.getContentPane().add(lblJoinedTwitter);
		
		JButton btnClose = new JButton(new ImageIcon(Application.class.getResource("/images/close_button2.png")));
		btnClose.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnClose.setBounds(362, 0, 30, 23);
        btnClose.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		frame.dispose();
        	}
        });
        btnClose.setFocusPainted(false);
        btnClose.setBorderPainted(false);
        btnClose.setBorder(BorderFactory.createEmptyBorder());
        btnClose.setContentAreaFilled(false);
		frame.getContentPane().add(btnClose);
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
		JLabel lblJoinedTwitterAns = new JLabel(dateFormat.format(status.getCreatedAt()));
		lblJoinedTwitterAns.setForeground(Color.WHITE);
		lblJoinedTwitterAns.setBounds(204, 97, 178, 14);
		frame.getContentPane().add(lblJoinedTwitterAns);
		
		JLabel lblNewLabel = new JLabel("Tweet ID:");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setBounds(99, 122, 83, 14);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel label = new JLabel(Long.toString(status.getId()));
		label.setForeground(Color.WHITE);
		label.setBounds(204, 122, 178, 14);
		frame.getContentPane().add(label);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		
	}
}
