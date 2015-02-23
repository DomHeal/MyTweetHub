import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.DefaultMapController;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.JMapViewerTree;
import org.openstreetmap.gui.jmapviewer.Layer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapObjectImpl;
import org.openstreetmap.gui.jmapviewer.Style;
import org.openstreetmap.gui.jmapviewer.events.JMVCommandEvent;
import org.openstreetmap.gui.jmapviewer.interfaces.JMapViewerEventListener;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;
import org.openstreetmap.gui.jmapviewer.interfaces.TileSource;
import org.openstreetmap.gui.jmapviewer.tilesources.BingAerialTileSource;
import org.openstreetmap.gui.jmapviewer.tilesources.MapQuestOpenAerialTileSource;
import org.openstreetmap.gui.jmapviewer.tilesources.MapQuestOsmTileSource;
import org.openstreetmap.gui.jmapviewer.tilesources.OsmTileSource;

import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;


public class Map extends JFrame implements JMapViewerEventListener {

	private static final long serialVersionUID = 1L;
	private JLabel zoomLabel=null;
    private JLabel zoomValue=null;

    private JLabel lblMperName=null;
    private JLabel lblMperValue = null;
    
    private static JMapViewerTree treeMap = null;
    static Double lat;
    static Double lon;
    String jlabels[];
    static String Status;
    static Twitter twitter = TwitterAppGui.getTwitter2();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Map().setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Map() {
			setVisible(true);
			setPreferredSize(new Dimension(1000, 800));
			setMinimumSize(new Dimension(600, 600));
			setSize(new Dimension(667, 649));
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setLocationRelativeTo(null);
			setTitle("MyTweetHub - Making Twitter Simple");
			setIconImage(Toolkit.getDefaultToolkit().getImage(Application.class.getResource("twitter47.png")));
		
	        treeMap = new JMapViewerTree("Zones");
	        treeMap.getViewer().setBackground(Color.WHITE);

	        // Listen to the map viewer for user operations so components will
	        // receive events and update
	        map().addJMVListener(this);

	        getContentPane().setLayout(new BorderLayout());
	        setExtendedState(JFrame.MAXIMIZED_BOTH);
	        JPanel panel = new JPanel();
	        JPanel panelTop = new JPanel();
	        panelTop.setBackground(new Color(102, 204, 255));
	        JPanel panelBottom = new JPanel();
	        panelBottom.setBackground(Color.LIGHT_GRAY);
	        JPanel helpPanel = new JPanel();
	        
	        // Displaying Information
	        lblMperName = new JLabel("Meters/Pixels: ");
	        lblMperValue = new JLabel(String.format("%s",map().getMeterPerPixel()));

	        zoomLabel = new JLabel("Zoom: ");
	        zoomValue = new JLabel(String.format("%s", map().getZoom()));
	        

	        getContentPane().add(panel, BorderLayout.NORTH);
	        getContentPane().add(helpPanel, BorderLayout.SOUTH);
	        panel.setLayout(new BorderLayout());
	        panel.add(panelTop, BorderLayout.NORTH);
	        panel.add(panelBottom, BorderLayout.SOUTH);
	        JLabel helpLabel = new JLabel("Use right mouse button to move,\n "
	                + "left double click or mouse wheel to zoom.");
	        helpPanel.add(helpLabel);
	        JButton button = new JButton("Fit Map Markers");
	        button.setForeground(new Color(255, 255, 255));
	        button.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
	        button.addActionListener(new ActionListener() {

	            public void actionPerformed(ActionEvent e) {
	                map().setDisplayToFitMapMarkers();
	            }
	        });
/*           JComboBox<TileSource> tileSourceSelector = new JComboBox<>(new TileSource[] {
	                new OsmTileSource.Mapnik(),
	                new OsmTileSource.CycleMap(),
	                new BingAerialTileSource(),
	                new MapQuestOsmTileSource(),
	                new MapQuestOpenAerialTileSource() });
	        tileSourceSelector.addItemListener(new ItemListener() {
	            public void itemStateChanged(ItemEvent e) {
	                map().setTileSource((TileSource) e.getItem());
	            }
	        });

	        panelTop.add(tileSourceSelector);*/

	        final JCheckBox showMapMarker = new JCheckBox("Map markers visible");
	        showMapMarker.setBackground(Color.LIGHT_GRAY);
	        showMapMarker.setSelected(map().getMapMarkersVisible());
	        showMapMarker.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                map().setMapMarkerVisible(showMapMarker.isSelected());
	            }
	        });
	        
	        JButton btnEnterCoordinates = new JButton("Enter Coordinates");
	        btnEnterCoordinates.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent arg0) {
	        		InputCord();
	        	}
	        });
	        panelBottom.add(btnEnterCoordinates);
	        
	        JCheckBox chckbxStatusVisible = new JCheckBox("Status visible");
	        chckbxStatusVisible.setBackground(Color.LIGHT_GRAY);
	        chckbxStatusVisible.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                //map().;
	            }
	        });
	        
	        panelBottom.add(chckbxStatusVisible);
	        
	        panelBottom.add(showMapMarker);

	        final JCheckBox showToolTip = new JCheckBox("ToolTip visible");
	        showToolTip.setBackground(Color.LIGHT_GRAY);
	        showToolTip.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                map().setToolTipText(null);
	            }
	        });
	        panelBottom.add(showToolTip);

	        final JCheckBox showZoomControls = new JCheckBox("Show zoom controls");
	        showZoomControls.setBackground(Color.LIGHT_GRAY);
	        showZoomControls.setSelected(map().getZoomContolsVisible());
	        showZoomControls.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                map().setZoomContolsVisible(showZoomControls.isSelected());
	            }
	        });
	        panelBottom.add(showZoomControls);

	        panelBottom.add(button);

	        panelTop.add(zoomLabel);
	        panelTop.add(zoomValue);
	        panelTop.add(lblMperName);
	        panelTop.add(lblMperValue);

	        getContentPane().add(treeMap, BorderLayout.CENTER);

	        map().addMouseMotionListener(new MouseAdapter() {
	            @Override
	            public void mouseMoved(MouseEvent e) {
	                Point p = e.getPoint();
	                if(showToolTip.isSelected()) map().setToolTipText(map().getPosition(p).toString());
	            }
	        });
	        
	            
	        new DefaultMapController(map()){

	            @Override
	            public void mouseClicked(MouseEvent e) {
	                System.out.println(map.getPosition(e.getPoint()));
	            }
	        };
	        
	}

    private static JMapViewer map(){
        return treeMap.getViewer();
    }

    /**
     * @param args
     */
    private void updateZoomParameters() {
        if (lblMperValue != null)
            lblMperValue.setText(String.format("%s",map().getMeterPerPixel()));
        if (zoomValue != null)
            zoomValue.setText(String.format("%s", map().getZoom()));
    }

    @Override
    public void processCommand(JMVCommandEvent command) {
        if (command.getCommand().equals(JMVCommandEvent.COMMAND.ZOOM) ||
                command.getCommand().equals(JMVCommandEvent.COMMAND.MOVE)) {
            updateZoomParameters();
        }
    }
    public static void InputCord() {
        JTextField xField = new JTextField(5);
        JTextField yField = new JTextField(5);
        xField.setText("51.60974");
        yField.setText("-3.98034");
        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("Latitude:"));
        myPanel.add(xField);
        myPanel.add(Box.createHorizontalStrut(15));
        myPanel.add(new JLabel("Longitude:"));
        myPanel.add(yField);

        int result = JOptionPane.showConfirmDialog(null, myPanel, 
                 "Please Enter Latitude and Longitude Values", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
           System.out.println("Latitude: " + xField.getText());
           System.out.println("Longitude: " + yField.getText());
           map().removeAllMapMarkers();
           paintMarkers(Double.parseDouble(xField.getText()), Double.parseDouble(yField.getText()));
        }
    }

	public static void paintMarkers(double lat, double lon) {
		double res = 5;
		String resUnit = "mi";
		Query query = new Query().geoCode(new GeoLocation(lat, lon), res, resUnit);
		query.count(100);
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm");
		try {
			QueryResult result = twitter.search(query);
			for (int i = 0; result.getTweets().size() > i; i++) {
				Date tweetDate = result.getTweets().get(i).getCreatedAt();
				System.out.println("Latitude = " + result.getTweets().get(i).getGeoLocation().getLatitude()	+ " " 
						+ " Longitude = "	+ result.getTweets().get(i).getGeoLocation().getLongitude() + " "
						+ formatter.format(tweetDate) + " - " 
						+ "@" + result.getTweets().get(i).getUser().getScreenName()
						+ ": " + result.getTweets().get(i).getText());
				
				setStatus(result.getTweets().get(i).getText());
				Coordinate tweetCoordinate = new Coordinate(result.getTweets().get(i).getGeoLocation().getLatitude(), result.getTweets().get(i).getGeoLocation().getLongitude());
				map().addMapMarker(new MapMarkerDot("@" + result.getTweets().get(i).getUser().getScreenName(), tweetCoordinate));
				map().addMapMarker(new SourceMarker(tweetCoordinate, res));
				//map().addMapMarker(new MapMarkerDot(Color.RED, result.getTweets().get(i).getGeoLocation().getLatitude(), result.getTweets().get(i).getGeoLocation().getLongitude()));
			}
			System.out.println("----------------------");
			System.out.println("Finished Tweet Query!");
			System.out.println("----------------------");

		} catch (TwitterException e1) {
			System.out.println("Error getting results");
			e1.printStackTrace();
		}
	}

    public static void setStatus(String status) {
		Status = status;
	}
    public static String getStatus() {
  		return Status;
  	}
}