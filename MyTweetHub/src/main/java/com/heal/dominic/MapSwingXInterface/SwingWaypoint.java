package com.heal.dominic.MapSwingXInterface;

import com.alee.laf.WebLookAndFeel;
import net.java.balloontip.BalloonTip;
import net.java.balloontip.styles.BalloonTipStyle;
import net.java.balloontip.styles.MinimalBalloonStyle;
import org.jdesktop.swingx.mapviewer.DefaultWaypoint;
import org.jdesktop.swingx.mapviewer.GeoPosition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

/**
 * A waypoint that is represented by a button on the map.
 *
 * @author Daniel Stahr
 */
public class SwingWaypoint extends DefaultWaypoint {
    private JButton button = null;
    private final String username;
    private final String msg;
    private final String profilePicture;
    private final String tweetType;

    public SwingWaypoint(GeoPosition coord, String username, String msg, String profilePicture, String tweetType) {
        super(coord);
        this.username = username;
        this.msg = msg;
        this.profilePicture = profilePicture;
        this.tweetType = tweetType;

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

    JButton getButton() {
        return button;
    }

    private class SwingWaypointMouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            JOptionPane.showMessageDialog(button, "@" + username + ": " + msg);
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

            String html = "<html><body><img style='border-radius:50%;' src='" +
                    profilePicture +
                    "' width=48 height=48 align='middle'> " +
                    username + ": " + msg;

            button.setToolTipText(html);
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
}
