package com.mytweethubapplication.ResourceManager;

import java.awt.Toolkit;

import javax.swing.ImageIcon;

import com.mytweethubapplication.MainInterface.Application;
import com.mytweethubapplication.MapInterface.Map;

public class ImageController {
		//General
	  	public static final ImageIcon Image_Close = new ImageIcon(Application.class.getResource("/images/close_button.png"));
		//Map Specific
	    public static final ImageIcon Image_Add = new ImageIcon(Toolkit.getDefaultToolkit().getImage(Map.class.getResource("/images/Add.png")));
	    public static final ImageIcon Image_Search = new ImageIcon(Toolkit.getDefaultToolkit().getImage(Map.class.getResource("/images/search.png")));
	    public static final ImageIcon Image_Cancel = new ImageIcon(Toolkit.getDefaultToolkit().getImage(Map.class.getResource("/images/Cancel.png")));
	    public static final ImageIcon Image_Database = new ImageIcon(Toolkit.getDefaultToolkit().getImage(Map.class.getResource("/images/database.png")));
	  
		
	    
	    
}
