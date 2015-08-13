package com.heal.dominic.Login;

import java.io.IOException;

import com.alee.laf.WebLookAndFeel;

public class AppLauncher {
	
	/*
	 * This method is used to Launch the Application
	 */
	public static void main (String[] args) throws IOException{
		WebLookAndFeel.install();
		new LoginGUI().login();
		
	}
}
