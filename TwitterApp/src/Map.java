import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Cursor;
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
import java.util.Iterator;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
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
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.Status;


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
	private JButton btnEnterCoordinates;
	private JButton btnID;
    static String Status;
    static Twitter twitter = TwitterAppGui.getTwitter2();
	private static Query query;
	static double res = 5;
	private static Coordinate tweetCoordinate;
	private static QueryResult result;
	private static List<Status> mentions;
	private static List<Status> mentions2;
	private static Coordinate sourceCoordinate;
	private static Layer layer = new Layer("test");
	private static Layer layer2;
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
           JComboBox<TileSource> tileSourceSelector = new JComboBox<>(new TileSource[] {
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

	        panelTop.add(tileSourceSelector);

	        final JCheckBox showMapMarker = new JCheckBox("Map markers visible");
	        showMapMarker.setBackground(Color.LIGHT_GRAY);
	        showMapMarker.setSelected(map().getMapMarkersVisible());
	        showMapMarker.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                map().setMapMarkerVisible(showMapMarker.isSelected());
	            }
	        });
	        
	        btnEnterCoordinates = new JButton("Enter Coordinates");
	        btnEnterCoordinates.setForeground(Color.WHITE);
	        btnEnterCoordinates.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
	        btnEnterCoordinates.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent arg0) {
	        		InputCord();
	        	}
	        });
	        
	        btnID = new JButton("Enter Tweet ID");
	        btnID.setForeground(Color.WHITE);
	        btnID.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
	        btnID.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent arg0) {
	        		InputID();
	        	}
	        });
	        btnID.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	        panelBottom.add(btnID);
	        panelBottom.add(btnEnterCoordinates);
	        
	        final JCheckBox chckbxStatusVisible = new JCheckBox("Status visible");
	        chckbxStatusVisible.setBackground(Color.LIGHT_GRAY);
	        chckbxStatusVisible.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            	   layer.setVisibleTexts(chckbxStatusVisible.isSelected());
            
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
	            public void mouseMoved(MouseEvent e) {

	                     Point p = e.getPoint();
	                        int X = p.x+3;
	                        int Y = p.y+3;
	                        List<MapMarker> ar = map.getMapMarkerList();
	                        Iterator<MapMarker> i = ar.iterator();
	                        while (i.hasNext()) {

	                            MapMarker mapMarker = (MapMarker) i.next();

	                            Point MarkerPosition = map.getMapPosition(mapMarker.getLat(), mapMarker.getLon());
	                            if( MarkerPosition != null){

	                                int centerX =  MarkerPosition.x;
	                                int centerY = MarkerPosition.y;

	                                // calculate the radius from the touch to the center of the dot
	                                double radCircle  = Math.sqrt( (((centerX-X)*(centerX-X)) + (centerY-Y)*(centerY-Y)));

	                                // if the radius is smaller then 23 (radius of a ball is 5), then it must be on the dot
	                                if (radCircle < 8){
	                                	//System.out.println(mapMarker.toString() + " is clicked");
	                                	treeMap.getViewer().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	                                	map().setToolTipText(mapMarker.getName());
	                                	repaint();
	                                    }
	                                else if (radCircle > 8) {
	                                	treeMap.getViewer().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	                                	repaint();
	                                	map().setToolTipText(null);
	                                }
	                            }
	                        
	                }
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

	   	   String resUnit = "mi";
	   	   query = new Query().geoCode(new GeoLocation((Double.parseDouble(xField.getText())), Double.parseDouble(yField.getText())), res, resUnit);
	   	   query.count(100);
           paintMarkers();
        }
    }
    public static void InputID() {
        JTextField iD = new JTextField(15);
        iD.setText("574940850662285312");
        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("Enter ID"));
        myPanel.add(iD);

        int result = JOptionPane.showConfirmDialog(null, myPanel, 
                 "Please Enter the Tweet ID", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
           map().removeAllMapMarkers();
           mentions(Long.parseLong(iD.getText()));
        }
    }

	public static void paintMarkers() {

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm");
		try {
			result = twitter.search(query);
			for (int i = 0; result.getTweets().size() > i; i++) {
				Date tweetDate = result.getTweets().get(i).getCreatedAt();
				tweetCoordinate = new Coordinate(result.getTweets().get(i).getGeoLocation().getLatitude(), result.getTweets().get(i).getGeoLocation().getLongitude());
				Style style = new Style(Color.BLACK, Color.YELLOW, null, null);
				//layer2 = new Layer("tester");
				map().addMapMarker(new MapMarkerDot(layer, result.getTweets().get(i).getUser().getScreenName() + ": " + result.getTweets().get(i).getText()  , tweetCoordinate, style));
			}
		} catch (TwitterException e1) {
			System.out.println("Error getting results");
			e1.printStackTrace();
		}
	}

/*	public static void paintStatus() {
			for (int i = 0; result.getTweets().size() > i; i++) {
				tweetCoordinate = new Coordinate(result.getTweets().get(i).getGeoLocation().getLatitude(), result.getTweets().get(i).getGeoLocation().getLongitude());
				map().addMapMarker(new SourceMarker("@" + result.getTweets().get(i).getUser().getScreenName() + ": " + result.getTweets().get(i).getText(), tweetCoordinate, res));
				
			}
	}  */
	
	public static void mentions(long tweetID){
		Date tweetDate = null;
		try {
			tweetDate = twitter.showStatus(tweetID).getCreatedAt();
			sourceCoordinate = new Coordinate(twitter.showStatus(tweetID).getGeoLocation().getLatitude(), twitter.showStatus(tweetID).getGeoLocation().getLongitude());
			
			Style style = new Style(Color.BLACK, Color.RED, null, null);
			map().addMapMarker(new MapMarkerDot(null, null, sourceCoordinate, style));
		} catch (TwitterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			mentions = twitter.getMentionsTimeline(new Paging().count(100));
			for (int i = 0; mentions.size() > i; i++) {	
				System.out.println(mentions.size());
				if (mentions.get(i).getCreatedAt().after(tweetDate) == true )
					tweetCoordinate = new Coordinate(mentions.get(i).getGeoLocation().getLatitude(), mentions.get(i).getGeoLocation().getLongitude());
					Style style = new Style(Color.BLACK, Color.YELLOW, null, null);
					map().addMapMarker(new MapMarkerDot(layer,  mentions.get(i).getUser().getScreenName() + ": " + mentions.get(i).getText() ,tweetCoordinate, style));
					//map().addMapMarker(new SourceMarker( "@" + mentions.get(i).getUser().getScreenName() + ": " + mentions.get(i).getText(), tweetCoordinate, res));
					
			}
			
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JOptionPane.showMessageDialog (null, "We have found " + (map().getMapMarkerList().size() - 1)/2 + " Tweets" , "Searching Complete", JOptionPane.INFORMATION_MESSAGE);
		for  ( MapMarker name :map().getMapMarkerList() ){
			System.out.println(name.getCoordinate());
			
		}
	}	
}