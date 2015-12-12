package com.heal.dominic.MapSwingXInterface;

import com.heal.dominic.MainInterface.Application;
import com.heal.dominic.MapInterface.MapInputWindows;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.OSMTileFactoryInfo;
import org.jdesktop.swingx.input.*;
import org.jdesktop.swingx.mapviewer.*;
import org.openstreetmap.gui.jmapviewer.interfaces.TileSource;
import org.openstreetmap.gui.jmapviewer.tilesources.BingAerialTileSource;
import org.openstreetmap.gui.jmapviewer.tilesources.MapQuestOpenAerialTileSource;
import org.openstreetmap.gui.jmapviewer.tilesources.MapQuestOsmTileSource;
import org.openstreetmap.gui.jmapviewer.tilesources.OsmTileSource;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Dominic on 12-Dec-15.
 */
public class Map2 extends JFrame{

    private final JXMapViewer mapViewer;

    public Map2 (){
        // Create a TileFactoryInfo for OSM
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        tileFactory.setThreadPoolSize(8);

        // Setup local file cache
        File cacheDir = new File(System.getProperty("user.home") + File.separator + ".jxmapviewer2");
        LocalResponseCache.installResponseCache(info.getBaseURL(), cacheDir, false);

        // Setup JXMapViewer
        mapViewer = new JXMapViewer();
        mapViewer.setTileFactory(tileFactory);

        GeoPosition frankfurt = new GeoPosition(50, 7, 0, 8, 41, 0);
        GeoPosition wiesbaden = new GeoPosition(50, 5, 0, 8, 14, 0);
        GeoPosition mainz = new GeoPosition(50, 0, 0, 8, 16, 0);
        GeoPosition darmstadt = new GeoPosition(49, 52, 0, 8, 39, 0);
        GeoPosition offenbach = new GeoPosition(50, 6, 0, 8, 46, 0);

        // Set the focus
        mapViewer.setZoom(10);
        mapViewer.setAddressLocation(frankfurt);

        // Add interactions
        MouseInputListener mia = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);
        mapViewer.addMouseListener(new CenterMapListener(mapViewer));
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(mapViewer));
        mapViewer.addKeyListener(new PanKeyListener(mapViewer));

        // Create waypoints from the geo-positions
        Set<SwingWaypoint> waypoints = new HashSet<SwingWaypoint>(Arrays.asList(
                new SwingWaypoint("Frankfurt", frankfurt),
                new SwingWaypoint("Wiesbaden", wiesbaden),
                new SwingWaypoint("Mainz", mainz),
                new SwingWaypoint("Darmstadt", darmstadt),
                new SwingWaypoint("Offenbach", offenbach)));

        // Set the overlay painter
        WaypointPainter<SwingWaypoint> swingWaypointPainter = new SwingWaypointOverlayPainter();
        swingWaypointPainter.setWaypoints(waypoints);
        mapViewer.setOverlayPainter(swingWaypointPainter);

        // Add the JButtons to the map viewer
        for (SwingWaypoint w : waypoints) {
            mapViewer.add(w.getButton());
        }

        // Display the viewer in a JFrame

        setVisible(true);
        setPreferredSize(new Dimension(1000, 800));
        setMinimumSize(new Dimension(600, 600));
        setSize(new Dimension(667, 649));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("MyTweetHub - Making Twitter Simple");
        setIconImage(Toolkit.getDefaultToolkit().getImage(
                Application.class.getResource("/images/tweethub_icon.png")));

        buildGUI();

    }

    public void buildGUI(){

        getContentPane().setLayout(new BorderLayout());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        JPanel panel = new JPanel();
        JPanel panelTop = new JPanel();
        panelTop.setBackground(new Color(102, 204, 255));
        JPanel panelBottom = new JPanel();
        panelBottom.setBackground(Color.LIGHT_GRAY);
        JPanel helpPanel = new JPanel();
        JLabel lblMperName = new JLabel("Meters/Pixels: ");
        JLabel lblMperValue = new JLabel(String.format("%s", 2233));

        JLabel zoomLabel = new JLabel("Zoom: ");
        JLabel zoomValue = new JLabel(String.format("%s", mapViewer.getZoom()));

        getContentPane().add(panel, BorderLayout.NORTH);
        getContentPane().add(helpPanel, BorderLayout.SOUTH);
        panel.setLayout(new BorderLayout());
        panel.add(panelTop, BorderLayout.NORTH);
        panel.add(panelBottom, BorderLayout.SOUTH);
        helpPanel.setLayout(new BorderLayout(0, 0));
        JLabel helpLabel = new JLabel("Use right mouse button to move,\n "
                + "left double click or mouse wheel to zoom.");
        helpLabel.setHorizontalAlignment(SwingConstants.CENTER);
        helpPanel.add(helpLabel);

        final JLabel lblCoordinateValue = new JLabel("0");
        lblCoordinateValue.setHorizontalAlignment(SwingConstants.CENTER);
        helpPanel.add(lblCoordinateValue, BorderLayout.NORTH);
        JButton button = new JButton("Fit Map Markers");
        button.setForeground(new Color(255, 255, 255));
        button.setUI(new BEButtonUI()
                .setNormalColor(BEButtonUI.NormalColor.green));
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
//                mapViewer.setDisplayToFitMapMarkers();
            }
        });
        JComboBox<TileSource> tileSourceSelector = new JComboBox<TileSource>(
                new TileSource[] { new OsmTileSource.Mapnik(),
                        new OsmTileSource.CycleMap(),
                        new BingAerialTileSource(),
                        new MapQuestOsmTileSource(),
                        new MapQuestOpenAerialTileSource() });
        tileSourceSelector.addItemListener(new ItemListener() {
            public void itemStateChanged(final ItemEvent e) {
//                mapViewer.setTileSource((TileSource) e.getItem());
            }
        });
        panelTop.add(tileSourceSelector);

        final JCheckBox showMapMarker = new JCheckBox("Map markers visible");
        showMapMarker.setBackground(Color.LIGHT_GRAY);
//        showMapMarker.setSelected(mapViewer.getMapMarkersVisible());
            showMapMarker.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                map().setMapMarkerVisible(showMapMarker.isSelected());
            }
        });

        JButton btnEnterCoordinates = new JButton("Enter Coordinates");
        btnEnterCoordinates.setForeground(Color.WHITE);
        btnEnterCoordinates.setUI(new BEButtonUI()
                .setNormalColor(BEButtonUI.NormalColor.green));
        btnEnterCoordinates.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                MapInputWindows.InputCord();
            }
        });

        JButton btnID = new JButton("Enter Tweet ID");
        btnID.setForeground(Color.WHITE);
        btnID.setUI(new BEButtonUI()
                .setNormalColor(BEButtonUI.NormalColor.green));
        btnID.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                MapInputWindows.InputID();
            }
        });
        btnID.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panelBottom.add(btnID);
        panelBottom.add(btnEnterCoordinates);

        JCheckBox chckbxStatusVisible = new JCheckBox("Status visible");
        chckbxStatusVisible.setBackground(Color.LIGHT_GRAY);
        chckbxStatusVisible.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                if (map().getZoom() > 14){
////                    layer.setVisibleTexts(chckbxStatusVisible.isSelected());
//                }
            }
        });
        panelBottom.add(chckbxStatusVisible);

        JCheckBox showConnection = new JCheckBox("Show Connections");
        showConnection.setBackground(Color.LIGHT_GRAY);
        showConnection.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                map().setMapPolygonsVisible(showConnection.isSelected());

            }
        });

        panelBottom.add(showConnection);
        panelBottom.add(showMapMarker);

        final JCheckBox showToolTip = new JCheckBox("ToolTip visible");
        showToolTip.setBackground(Color.LIGHT_GRAY);
        showToolTip.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                map().setToolTipText(null);
            }
        });
        panelBottom.add(showToolTip);

        final JCheckBox showZoomControls = new JCheckBox("Show zoom controls");
        showZoomControls.setBackground(Color.LIGHT_GRAY);
//        showZoomControls.setSelected(map().getZoomControlsVisible());
        showZoomControls.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                map().setZoomContolsVisible(showZoomControls.isSelected());
            }
        });
        panelBottom.add(showZoomControls);

        panelBottom.add(button);

        panelTop.add(zoomLabel);
        panelTop.add(zoomValue);
        panelTop.add(lblMperName);
        panelTop.add(lblMperValue);

        getContentPane().add(mapViewer, BorderLayout.CENTER);
//
//        map().addMouseMotionListener(new MouseAdapter() {
//            @Override
//            public void mouseMoved(MouseEvent e) {
//                Point p = e.getPoint();
//                lblCoordinateValue.setText(map().getPosition(p).toString());
//            }
//        });
    }

    public static void main(String[] args) {
        new Map2();
    }
}

