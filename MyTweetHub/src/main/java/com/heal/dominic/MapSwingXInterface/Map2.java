package com.heal.dominic.MapSwingXInterface;

import com.alee.laf.WebLookAndFeel;
import com.heal.dominic.Login.LoginGUI;
import com.heal.dominic.MainInterface.Application;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.OSMTileFactoryInfo;
import org.jdesktop.swingx.VirtualEarthTileFactoryInfo;
import org.jdesktop.swingx.input.*;
import org.jdesktop.swingx.mapviewer.*;
import twitter4j.Query;
import twitter4j.Twitter;


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

    protected static JXMapViewer mapViewer;
    protected static JTextField yField = new JTextField(15);
    protected static JTextField xField = new JTextField(15);
    static Twitter twitter = LoginGUI.getTwitter();
    protected static Query query;
//    private final Set<SwingWaypoint> waypoints;
    private JLabel zoomValue;
    static boolean dataMiningRunning = true;

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
        mapViewer.setZoom(7);
        mapViewer.setAddressLocation(frankfurt);

        // Add interactions
        MouseInputListener mia = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);
        mapViewer.addMouseListener(new CenterMapListener(mapViewer));
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(mapViewer));
        mapViewer.addKeyListener(new PanKeyListener(mapViewer));

        mapViewer.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    Point p = e.getPoint();
                    yField.setText(String.format("%s" ,(mapViewer.convertPointToGeoPosition(p).getLongitude())));
                    xField.setText(String.format("%s" ,(mapViewer.convertPointToGeoPosition(p).getLatitude())));
                    MapMenu.showMenu(e, p);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

//        Create waypoints from the geo-positions
//        waypoints = new HashSet<SwingWaypoint>(Arrays.asList(
//                new SwingWaypoint(frankfurt, "Frankfurt", null, null, "Normal")));
//                new SwingWaypoint("Wiesbaden", wiesbaden, null),
//                new SwingWaypoint("Mainz", mainz, null),
//                new SwingWaypoint("Darmstadt", darmstadt, null),
//                new SwingWaypoint("Offenbach", offenbach, null))


        // Set the overlay painter
//        WaypointPainter<SwingWaypoint> swingWaypointPainter = new SwingWaypointOverlayPainter();
//        swingWaypointPainter.setWaypoints(waypoints);
//        mapViewer.setOverlayPainter(swingWaypointPainter);
//
//        // Add the JButtons to the map viewer
//        for (SwingWaypoint w : waypoints) {
//            mapViewer.add(w.getButton());
//        }

//        testMarkers();
        buildGUI();
    }
//
//    public void testMarkers(){
//        Set<SwingWaypoint> waypoints = new HashSet<SwingWaypoint>();
//        for (int i = 10; i < 600 ; i++) {
//            waypoints.add(new SwingWaypoint(new GeoPosition(i,i), "" , null, null, "Normal"));
//            System.out.print(i);
//        }
//
//        WaypointPainter<SwingWaypoint> swingWaypointPainter = new SwingWaypointOverlayPainter();
//        swingWaypointPainter.setWaypoints(waypoints);
//        mapViewer.setOverlayPainter(swingWaypointPainter);
//
//        // Add the JButtons to the map viewer
//        for (SwingWaypoint w : waypoints) {
//            mapViewer.add(w.getButton());
//        }
//    }

    public void buildGUI(){

        getContentPane().setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        JPanel panelTop = new JPanel();
        panelTop.setBackground(new Color(102, 204, 255));
        JPanel panelBottom = new JPanel();
        panelBottom.setBackground(Color.LIGHT_GRAY);
        JPanel helpPanel = new JPanel();
        JLabel lblMperName = new JLabel("Threads Running: ");
        zoomValue = new JLabel(String.format("%s", mapViewer.getZoom()));
        final JLabel lblMperValue = new JLabel();

        JLabel zoomLabel = new JLabel("Zoom: ");
        zoomValue = new JLabel(String.format("%s", mapViewer.getZoom()));

        getContentPane().add(panel, BorderLayout.NORTH);
        getContentPane().add(helpPanel, BorderLayout.SOUTH);
        panel.setLayout(new BorderLayout());
        panel.add(panelTop, BorderLayout.NORTH);
        panel.add(panelBottom, BorderLayout.SOUTH);
        helpPanel.setLayout(new BorderLayout(0, 0));
        JLabel helpLabel = new JLabel("Use left mouse button to move,\n "
                + "left double click or mouse wheel to zoom.");
        helpLabel.setHorizontalAlignment(SwingConstants.CENTER);
        helpPanel.add(helpLabel);

        final JLabel lblCoordinateValue = new JLabel("0");
        lblCoordinateValue.setHorizontalAlignment(SwingConstants.CENTER);
        helpPanel.add(lblCoordinateValue, BorderLayout.NORTH);

        final JButton dataMiningBtn = new JButton("Data Mining : OFF");
        dataMiningBtn.setForeground(new Color(255, 255, 255));
        dataMiningBtn.setUI(new BEButtonUI()
                .setNormalColor(BEButtonUI.NormalColor.red));
        dataMiningBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(dataMiningRunning) {
                    dataMiningBtn.setUI(new BEButtonUI()
                            .setNormalColor(BEButtonUI.NormalColor.green));
                    dataMiningBtn.setText("Data Mining : ON");
                    dataMiningRunning = false;
                } else {
                    dataMiningBtn.setUI(new BEButtonUI()
                            .setNormalColor(BEButtonUI.NormalColor.red));
                    dataMiningBtn.setText("Data Mining : OFF");
                    dataMiningRunning = true;
                }
            }
        });

        TileFactoryInfo osmInfo = new OSMTileFactoryInfo();
        TileFactoryInfo veInfo = new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.MAP);
        JComboBox<DefaultTileFactory> tileSourceSelector = new JComboBox<DefaultTileFactory>(
                new DefaultTileFactory[] {
                        new DefaultTileFactory(osmInfo),
                        new DefaultTileFactory(veInfo)
                });
        tileSourceSelector.addItemListener(new ItemListener() {
            public void itemStateChanged(final ItemEvent e) {
                mapViewer.setTileFactory((DefaultTileFactory) e.getItem());
            }
        });
        panelTop.add(tileSourceSelector);

        final JCheckBox showMapMarker = new JCheckBox("Map markers visible");
        showMapMarker.setBackground(Color.LIGHT_GRAY);
//        showMapMarker.setSelected(mapViewer.getMapMarkersVisible());
            showMapMarker.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

//                mapViewer.setMapMarkerVisible(showMapMarker.isSelected());
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

        JButton clearMapMarkers = new JButton("Clear MapMarkers");
        clearMapMarkers.setForeground(Color.WHITE);
        clearMapMarkers.setUI(new BEButtonUI()
                .setNormalColor(BEButtonUI.NormalColor.red));
        clearMapMarkers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                mapViewer.removeAll();
                mapViewer.repaint();
            }
        });
        clearMapMarkers.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panelBottom.add(clearMapMarkers);

        JCheckBox showConnection = new JCheckBox("Show Connections");
        showConnection.setBackground(Color.LIGHT_GRAY);
        showConnection.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                map().setMapPolygonsVisible(showConnection.isSelected());
            }
        });

        panelBottom.add(showConnection);
        panelBottom.add(showMapMarker);
        panelBottom.add(dataMiningBtn);

        panelTop.add(zoomLabel);
        panelTop.add(zoomValue);
        panelTop.add(lblMperName);
        panelTop.add(lblMperValue);

        getContentPane().add(mapViewer, BorderLayout.CENTER);

        mapViewer.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point p = e.getPoint();
                lblCoordinateValue.setText(mapViewer.convertPointToGeoPosition(p).toString());
            }
        });

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
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        Timer t = new Timer(500, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Set<Thread> threads = Thread.getAllStackTraces().keySet();
                lblMperValue.setText("Threads: " + threads.size());
            }
        });

        t.start();

    }

    public static void main(String[] args) {
        WebLookAndFeel.install();
        new Map2();
    }
}

