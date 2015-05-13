// License: GPL. For details, see Readme.txt file.


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.Layer;
import org.openstreetmap.gui.jmapviewer.MapObjectImpl;
import org.openstreetmap.gui.jmapviewer.Style;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;

/**
 * A simple implementation of the {@link MapMarker} interface. Each map marker
 * is painted as a circle with a black border line and filled with a specified
 * color.
 *
 * @author Jan Peter Stotz
 *
 */
public class SourceMarker extends MapObjectImpl implements MapMarker {

    Coordinate coord;
    double radius;
    STYLE markerStyle;

    public SourceMarker(Coordinate coord, double radius) {
        this(null, null, coord, radius);
    }
    public SourceMarker(String name, Coordinate coord, double radius) {
        this(null, name, coord, radius);
    }
    public SourceMarker(Layer layer, Coordinate coord, double radius) {
        this(layer, null, coord, radius);
    }
    public SourceMarker(double lat, double lon, double radius) {
        this(null, null, new Coordinate(lat,lon), radius);
    }
    public SourceMarker(Layer layer, double lat, double lon, double radius) {
        this(layer, null, new Coordinate(lat,lon), radius);
    }
    public SourceMarker(Layer layer, String name, Coordinate coord, double radius) {
        this(layer, name, coord, radius, STYLE.VARIABLE, getDefaultStyle());
    }
    public SourceMarker(Layer layer, String name, Coordinate coord, double radius, STYLE markerStyle, Style style) {
        super(layer, name, style);
        this.markerStyle = markerStyle;
        this.coord = coord;
        this.radius = radius;
    }

    public Coordinate getCoordinate(){
        return coord;
    }
    
    public double getLat() {
        return coord.getLat();
    }

    public double getLon() {
        return coord.getLon();
    }

    public double getRadius() {
        return radius;
    }

    public STYLE getMarkerStyle() {
        return markerStyle;
    }

    public void paint(Graphics g, Point position, int radio) {
        int size_h = radio;
        //g.setColor(Color.WHITE);
        //g.draw3DRect(position.x - 5, position.y - 5, 400, 120, true);
        g.setColor(Color.BLACK);
        //g.setFont(new Font("TimesRoman", Font.PLAIN, size_h/10));
        g.drawString(getName(), position.x + 10, position.y + 5);
        //g.drawString("30°C", position.x + size - 10, position.y + 37);
        g.setColor(Color.gray);
       // g.drawLine(position.x + size + 18, position.y - 4, position.x + size + 18 ,        position.y + 112);
    }

    public static Style getDefaultStyle(){
        return new Style(Color.ORANGE, new Color(200,200,200,200), null, getDefaultFont());
    }
    @Override
    public String toString() {
        return "MapMarker at " + getLat() + " " + getLon();
    }
    @Override
    public void setLat(double lat) {
        if(coord==null) coord = new Coordinate(lat,0);
        else coord.setLat(lat);
    }
    @Override
    public void setLon(double lon) {
        if(coord==null) coord = new Coordinate(0,lon);
        else coord.setLon(lon);
    }
}
