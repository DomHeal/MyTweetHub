package com.mytweethubapplication.MainInterface;

import com.mytweethubapplication.Login.LoginGUI;
import twitter4j.TwitterException;

import javax.swing.*;

/**
 * Created by Dominic on 20-Nov-15.
 */
public class Posting extends Application {

	/* Method Which Controls Posting a Tweet */
    public Posting() {
        if (txtPostATweet.getText() != POST_MESSAGE	&& txtPostATweet.getText() != "") {
            try {

                LoginGUI.twitter.updateStatus(txtPostATweet.getText());

                txtPostATweet.setText("");

                JOptionPane.showMessageDialog(null, "You have successfully posted a tweet.");
            } catch (TwitterException te) {
                te.printStackTrace();
                JOptionPane.showMessageDialog(null,	"Failed to post tweet, Try Again!");
            }
        } else {
            JOptionPane.showMessageDialog(null,	"Please enter text before sending a Tweet.");
        }
    }
}
