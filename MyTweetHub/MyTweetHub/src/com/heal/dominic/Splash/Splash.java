package com.heal.dominic.Splash;


 
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import com.heal.dominic.MainInterface.Application;
 
public class Splash extends Thread {
	public static JLabel loadingResourcelbl = new JLabel("");
	private final JLabel label = new JLabel("");
	public static JProgressBar progressBar = new JProgressBar();;
	public static JFrame frame;
	public static int splashCounter = 0;

	// Thread created to display Splash page
	public void run() {
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/tweethub_icon.png")));
		frame.setUndecorated(true);
		frame.setSize(444, 212);
		frame.getContentPane().setLayout(null);
		frame.setBackground(new Color(0, 0, 0, 0));
		
		label.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/Splash.png"))));
		label.setBounds(0, 0, 428, 212);
		
		progressBar.setStringPainted(true);
		progressBar.setForeground(Color.CYAN);
		progressBar.setBounds(149, 119, 194, 14);

		loadingResourcelbl = new JLabel("Loading Resources...");
		loadingResourcelbl.setFont(new Font("HelveticaNeue", Font.PLAIN, 10));
		loadingResourcelbl.setForeground(Color.WHITE);
		loadingResourcelbl.setBounds(149, 70, 211, 28);
		
		frame.getContentPane().add(loadingResourcelbl);
		frame.getContentPane().add(progressBar);
		frame.getContentPane().add(label);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}
}

