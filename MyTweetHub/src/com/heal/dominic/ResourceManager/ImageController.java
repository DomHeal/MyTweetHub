package com.heal.dominic.ResourceManager;

import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.heal.dominic.MainInterface.Application;
import com.heal.dominic.MapInterface.Map;

public class ImageController {
		//General
	  	public static final ImageIcon Image_Close = new ImageIcon(Application.class.getResource("/images/close_button.png"));
		//Map Specific
	    public static final ImageIcon Image_Add = new ImageIcon(Toolkit.getDefaultToolkit().getImage(Map.class.getResource("/images/Add.png")));
	    public static final ImageIcon Image_Search = new ImageIcon(Toolkit.getDefaultToolkit().getImage(Map.class.getResource("/images/search.png")));
	    public static final ImageIcon Image_Cancel = new ImageIcon(Toolkit.getDefaultToolkit().getImage(Map.class.getResource("/images/Cancel.png")));
	  
		
	    
	    
}
