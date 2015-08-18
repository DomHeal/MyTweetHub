// License: GPL. For details, see Readme.txt file.
package com.heal.dominic.MapInterface;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.Layer;
import org.openstreetmap.gui.jmapviewer.MapObjectImpl;
import org.openstreetmap.gui.jmapviewer.Style;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;

import com.heal.dominic.MainInterface.Application;

/**
 * A simple implementation of the {@link MapMarker} interface. Each map marker
 * is painted as a circle with a black border line and filled with a specified
 * color.
 *
 * @author Jan Peter Stotz
 *
 */
public class Marker extends MapObjectImpl implements MapMarker {

    private Coordinate coord;
    private double radius;
    private STYLE markerStyle;
	private String picture;

 
    public Marker(Layer layer, String name, Coordinate coord, double radius, STYLE markerStyle, Style style) {
        super(layer, name, style);
        this.markerStyle = markerStyle;
        this.coord = coord;
        this.radius = radius;
    }

    public Marker(Layer layer, String name,Coordinate coord, Style style, String picture) {
    	  super(layer, name, style);
          this.coord = coord;
          this.picture = picture;
	}

	@Override
    public Coordinate getCoordinate() {
        return coord;
    }

    @Override
    public double getLat() {
        return coord.getLat();
    }

    public String getPicture() {
		return picture;
	}

	@Override
    public double getLon() {
        return coord.getLon();
    }

    @Override
    public double getRadius() {
        return radius;
    }

    @Override
    public STYLE getMarkerStyle() {
        return markerStyle;
    }

    @Override
    public void paint(Graphics g, Point position, int radius) {

    	BufferedImage Image_Marker = null;
		try {
    		Image_Marker  = ImageIO.read(Application.class.getResource("/images/mapmarker.png"));
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	g.drawImage(Image_Marker, position.x-15, position.y - 20,null); 

        if (getLayer() == null || getLayer().isVisibleTexts()) paintText(g, position);
    }

    public static Style getDefaultStyle() {
        return new Style(Color.ORANGE, new Color(200, 200, 200, 200), null, getDefaultFont());
    }

    @Override
    public String toString() {
        return "MapMarker at " + getLat() + " " + getLon();
    }

    @Override
    public void setLat(double lat) {
        if (coord == null) coord = new Coordinate(lat, 0);
        else coord.setLat(lat);
    }

    @Override
    public void setLon(double lon) {
        if (coord == null) coord = new Coordinate(0, lon);
        else coord.setLon(lon);
    }
}
