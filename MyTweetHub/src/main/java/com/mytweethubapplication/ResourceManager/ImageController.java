package com.mytweethubapplication.ResourceManager;

import java.awt.Toolkit;

import javax.swing.ImageIcon;

import com.mytweethubapplication.MainInterface.Application;
import com.mytweethubapplication.MapSwingXInterface.MapInterface;

public class ImageController {
		//General
	  	public static final ImageIcon Image_Close = new ImageIcon(Toolkit.getDefaultToolkit().getImage(Application.class.getResource("/images/close_button.png")));
		//Map Specific
	    public static final ImageIcon Image_Add = new ImageIcon(Toolkit.getDefaultToolkit().getImage(MapInterface.class.getResource("/images/Add.png")));
	    public static final ImageIcon Image_Search = new ImageIcon(Toolkit.getDefaultToolkit().getImage(MapInterface.class.getResource("/images/search.png")));
	    public static final ImageIcon Image_Cancel = new ImageIcon(Toolkit.getDefaultToolkit().getImage(MapInterface.class.getResource("/images/Cancel.png")));
	    public static final ImageIcon Image_Db_add = new ImageIcon(Toolkit.getDefaultToolkit().getImage(MapInterface.class.getResource("/images/db_add.png")));
	    public static final ImageIcon Image_Db_Remove = new ImageIcon(Toolkit.getDefaultToolkit().getImage(MapInterface.class.getResource("/images/db_remove.png")));
	    public static final ImageIcon Image_Db_Download = new ImageIcon(Toolkit.getDefaultToolkit().getImage(MapInterface.class.getResource("/images/db_update.png")));

		
	    
	    
}
