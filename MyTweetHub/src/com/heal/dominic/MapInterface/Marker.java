// License: GPL. For details, see Readme.txt file.
package com.heal.dominic.MapInterface;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.openstreetmap.gui.jmapviewer.*;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;

import com.heal.dominic.MainInterface.Application;

public class Marker extends MapObjectImpl implements MapMarker {

    private JMapViewer map = null;
    private String name;
    private int count = 0;
    private Coordinate coord;
    private double radius;
    private STYLE markerStyle;
	private String picture;

    public Marker(Layer layer, String name,Coordinate coord, int count, String picture) {
    	  super(layer, name, null);
          this.count = count;
          this.coord = coord;
          this.name = name;
          this.picture = picture;
	}
    public Marker(Layer layer, String name,Coordinate coord, Style style, String picture) {
        super(layer, name, null);
        this.coord = coord;
        this.picture = picture;
    }

    public Marker(Layer layer, String name, Coordinate coord, int count, String picture, JMapViewer map) {
        super(layer, name, null);
        this.count = count;
        this.coord = coord;
        this.name = name;
        this.picture = picture;
        this.map = map;
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
    		Image_Marker  = ImageIO.read(Application.class.getResource("/images/map-pins-empty-blue.png"));
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	g.drawImage(Image_Marker, position.x-15, position.y-35, null);
        g.setColor(Color.WHITE);
        int count = this.count;
        if (count != -1) {
            int length = String.valueOf(count).length();
            switch (length) {
                case 1:
                    g.drawString(String.valueOf(count), position.x, position.y - 12);
                    break;
                case 2:
                    g.drawString(String.valueOf(count), position.x - 3, position.y - 12);
                    break;
                case 3:
                    g.drawString(String.valueOf(count), position.x - 5, position.y - 12);
                    break;
                case 4:
                    g.drawString(String.valueOf(count), position.x - 7, position.y - 12);
                    break;
                case 5:
                    g.drawString(String.valueOf(count), position.x - 10, position.y - 12);
                    break;
            }
        }
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
