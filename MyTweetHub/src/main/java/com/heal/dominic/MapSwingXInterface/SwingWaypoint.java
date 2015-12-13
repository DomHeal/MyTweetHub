package com.heal.dominic.MapSwingXInterface;

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
    private final JButton button;
    private final String username;
    private final String msg;
    private final String profilePicture;

    public SwingWaypoint(GeoPosition coord, String username, String msg , String profilePicture) {
        super(coord);
        this.username = username;
        this.msg = msg;
        this.profilePicture = profilePicture;

        button = new JButton();
        button.addMouseListener(new SwingWaypointMouseListener());
        button.setVisible(true);
        ImageIcon cup = new ImageIcon(SwingWaypoint.class.getResource("/images/map-pins-empty-blue.png"));
        button.setIcon(cup);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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
        public void mouseEntered(MouseEvent e) { button.setToolTipText("@" + username + ": " + msg);
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
}
