package com.mytweethubapplication.MapSwingXInterface;

import com.alee.laf.WebLookAndFeel;
import com.vdurmont.emoji.EmojiParser;
import org.jdesktop.swingx.mapviewer.DefaultWaypoint;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import twitter4j.Status;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Arc2D;
import java.net.MalformedURLException;
import java.net.URL;

public class SwingWaypoint extends DefaultWaypoint {
    private String retweetCount;
    private String likeCount;
    private String createdAt;
    private Status user = null;
    private JButton button = null;
    private String username = null;
    private String msg = null;
    private String profilePicture = null;
    private String tweetType = null;
    private DatabaseUser databaseUser = null;


    public String getTweetType() {
        return tweetType;
    }

    public Status getUser() {
        return user;
    }
    public SwingWaypoint(DatabaseUser user){
        super(Double.parseDouble(user.getLatitude()), Double.parseDouble(user.getLongitude()));
        this.databaseUser = user;
        this.tweetType = user.getTweetType();
        this.msg = user.getText();
        this.profilePicture = user.getPictureURL();
        this.likeCount = user.getLikes();
        this.retweetCount = user.getRetweets();
        this.createdAt = String.valueOf(user.getCreatedAt());
        this.username = user.getScreenName();

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

            button = new JButton();
            button.addMouseListener(new SwingWaypointMouseListener());
            ImageIcon cup = null;
            if (tweetType.equals("Source")) {
                cup = new ImageIcon(SwingWaypoint.class.getResource("/images/map-pins-source.png"));
            } else if (tweetType.equals("Normal")) {
                cup = new ImageIcon(SwingWaypoint.class.getResource("/images/map-pins-bird.png"));
            }

            button.setIcon(cup);
            button.setBorder(BorderFactory.createEmptyBorder());
            button.setContentAreaFilled(false);
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            button.setBorderPainted(false);
            button.setFocusPainted(false);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            WebLookAndFeel.install();
        }
    }

    public SwingWaypoint(GeoPosition coord, Status user, String tweetType) {
        super(coord);
        this.user = user;
        this.username = user.getUser().getScreenName();
        this.msg = user.getText();
        this.profilePicture = user.getUser().getProfileImageURL();
        this.tweetType = tweetType;
        this.likeCount = String.valueOf(user.getFavoriteCount());
        this.retweetCount = String.valueOf(user.getRetweetCount());
        this.createdAt = String.valueOf(user.getCreatedAt());

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

            button = new JButton();
            button.addMouseListener(new SwingWaypointMouseListener());
            ImageIcon cup = null;
            if (tweetType.equals("Source")) {
                cup = new ImageIcon(SwingWaypoint.class.getResource("/images/map-pins-source.png"));
            } else if (tweetType.equals("Normal")) {
                cup = new ImageIcon(SwingWaypoint.class.getResource("/images/map-pins-bird.png"));
            }

            button.setIcon(cup);
            button.setBorder(BorderFactory.createEmptyBorder());
            button.setContentAreaFilled(false);
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            button.setBorderPainted(false);
            button.setFocusPainted(false);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            WebLookAndFeel.install();
        }
    }

    public JButton getButton() {
        return button;
    }

    private class SwingWaypointMouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            try {
                String msg_without_emoji = EmojiParser.removeAllEmojis(msg);
                JOptionPane.showMessageDialog(button, "@" + username + ": " + msg_without_emoji, "@"+username ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(new URL(profilePicture)));
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            ToolTipManager.sharedInstance().setInitialDelay(0);
            ToolTipManager.sharedInstance().setReshowDelay(0);
            ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);

            String msg_without_emoji = EmojiParser.removeAllEmojis(msg);
            String html = "<html><body style='background-color: #2C3E50; color: white; margin: 0px auto; font-family: Calibri'>" +
                    "<div><img src='" + profilePicture + "' height='64'align='middle'> " + username + ": " +
                    msg_without_emoji + "</div><div style='text-align:right;'> <img src='" +
                    getClass().getResource("/images/like.png") +"' height='16'; width='14'/>" + likeCount +
                    " - <img src='" + getClass().getResource("/images/retweet.png") + "' height='16' width='14'/>" +
                    retweetCount + "</div> " +
                    "<div style='font-size: 8px;'> Created On " + createdAt + "</div>";

            button.setToolTipText(html);
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
}
