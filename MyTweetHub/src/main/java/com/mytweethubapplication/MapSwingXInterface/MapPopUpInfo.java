package com.mytweethubapplication.MapSwingXInterface;

import com.alee.global.StyleConstants;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.ComponentUpdater;
import com.mytweethubapplication.ResourceManager.ImageController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MapPopUpInfo extends JComponent{
	
	   private final ImageIcon wheelIcon;
       private int angle = 0;
       public MapPopUpInfo(){
        super ();
        SwingUtils.setOrientation ( this );
        setOpaque ( false );

        // Loading icon
        wheelIcon = ImageController.Image_Search;

        // Wheel rotation updater
        ComponentUpdater.install ( this, "WheelImage.animator", StyleConstants.animationDelay, new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                angle += 1;
                repaint ();
            }
        } );
    }

    @Override
    protected void paintComponent ( final Graphics g )
    {
        super.paintComponent ( g );

        final Graphics2D g2d = ( Graphics2D ) g;
        final Object iq = GraphicsUtils.setupImageQuality ( g2d );

        g2d.translate ( wheelIcon.getIconWidth () / 2, wheelIcon.getIconHeight () / 2 );
        if ( angle != 0 )
        {
            g2d.rotate ( angle * Math.PI / 180 );
        }
        g2d.drawImage ( wheelIcon.getImage (), -wheelIcon.getIconWidth () / 2, -wheelIcon.getIconHeight () / 2,
                wheelIcon.getImageObserver () );

        GraphicsUtils.restoreImageQuality ( g2d, iq );
    }
}
