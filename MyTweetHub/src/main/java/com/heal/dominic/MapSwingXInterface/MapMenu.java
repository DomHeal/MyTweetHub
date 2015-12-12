package com.heal.dominic.MapSwingXInterface;

import com.alee.extended.menu.DynamicMenuType;
import com.alee.extended.menu.WebDynamicMenu;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.time.ClockType;
import com.alee.extended.time.WebClock;
import com.alee.managers.notification.NotificationIcon;
import com.alee.managers.notification.NotificationManager;
import com.alee.managers.notification.WebNotificationPopup;
import com.heal.dominic.MapInterface.MapInputWindows;
import com.heal.dominic.ResourceManager.ImageController;
import com.heal.dominic.ResourceManager.SoundController;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

public class MapMenu {

	public static void showMenu(MouseEvent e, Point p) {
	      final WebDynamicMenu menu = new WebDynamicMenu ();
	      menu.setType(DynamicMenuType.shutter);
	      menu.setHideType(DynamicMenuType.roll);
	      menu.setRadius(75);
	      menu.addItem(ImageController.Image_Add, new ActionListener ()
	      {
	    	    @Override
	    	    public void actionPerformed ( final ActionEvent e )
	    	    {
	                final WebNotificationPopup notificationPopup = new WebNotificationPopup ();
	                notificationPopup.setIcon ( NotificationIcon.clock );
	                notificationPopup.setDisplayTime ( 5000 );
	                
	                final WebClock clock = new WebClock();
	                clock.setClockType(ClockType.timer);
	                clock.setTimeLeft(6000);
	                clock.setTimePattern ( "'Coordinates Have been Saved!'");
	                notificationPopup.setContent(new GroupPanel(clock));

	                NotificationManager.showNotification (notificationPopup);
	                clock.start ();
	                SoundController.playTickSound();
	    	    }
	     });
	      
	      menu.addItem(ImageController.Image_Search, new ActionListener ()
	      {
	    	    @Override
	    	    public void actionPerformed ( final ActionEvent e )
	    	    {
	    	        MapInputWindows.InputUsernameID();
	    	    }
	     });;
	     
	     
	      menu.addItem(ImageController.Image_Cancel, new ActionListener ()
	      {
	    	    @Override
	    	    public void actionPerformed ( final ActionEvent e )
	    	    {
	    	        System.out.println("Cancel");
	    	    }
	     });;
	     
	      menu.addItem(ImageController.Image_Database);
	      menu.showMenu ( e.getComponent(), p);
	}

}
