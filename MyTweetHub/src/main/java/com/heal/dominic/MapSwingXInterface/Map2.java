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
    static Set<SwingWaypoint> waypoints = new HashSet<SwingWaypoint>();
    private JLabel zoomValue;
    static boolean dataMiningRunning = true;
    protected static long latestTweetID;
    protected static Timer dataMine;

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

        GeoPosition UK = new GeoPosition(52.40912125231122, -2.0269775390625);

        // Set the focus
        mapViewer.setZoom(15);
        mapViewer.setAddressLocation(UK);

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

        buildGUI();
    }


    public void buildGUI(){

        getContentPane().setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        JPanel panelTop = new JPanel();
        panelTop.setBackground(new Color(102, 204, 255));
        JPanel panelBottom = new JPanel();
        panelBottom.setBackground(Color.LIGHT_GRAY);
        JPanel helpPanel = new JPanel();
        JLabel lblMarkers = new JLabel("MapMarkers Diaplayed: ");
        final JLabel lblMarkersValue = new JLabel("MapMarkers: ");
        JLabel lblMperName = new JLabel("Threads Running: ");
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

        panelTop.add(lblMarkers);
        panelTop.add(lblMarkersValue);

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
                    MapInputWindows.dataMine();
                } else {
                    dataMiningBtn.setUI(new BEButtonUI()
                            .setNormalColor(BEButtonUI.NormalColor.red));
                    dataMiningBtn.setText("Data Mining : OFF");
                    dataMiningRunning = true;
                    dataMine.stop();
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
        panelBottom.add(btnEnterCoordinates);

        JButton btnID = new JButton("Search Tweet ID");
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

        JButton hashtagBtn = new JButton("Search Hashtag");
        hashtagBtn.setForeground(Color.WHITE);
        hashtagBtn.setUI(new BEButtonUI()
                .setNormalColor(BEButtonUI.NormalColor.green));
        hashtagBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                MapInputWindows.InputHashtag();
            }
        });
        hashtagBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panelBottom.add(hashtagBtn);


        JButton clearMapMarkers = new JButton("Clear MapMarkers");
        clearMapMarkers.setForeground(Color.WHITE);
        clearMapMarkers.setUI(new BEButtonUI()
                .setNormalColor(BEButtonUI.NormalColor.red));
        clearMapMarkers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                mapViewer.removeAll();
                mapViewer.repaint();
                waypoints.clear();
                System.out.println(waypoints.size());
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
                zoomValue.setText(String.format("%s", mapViewer.getZoom()));
                lblMarkersValue.setText(String.valueOf(waypoints.size()));

            }
        });

        t.start();

    }
}

