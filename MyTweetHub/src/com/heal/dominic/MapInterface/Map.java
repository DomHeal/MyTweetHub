package com.heal.dominic.MapInterface;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.DefaultMapController;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.JMapViewerTree;
import org.openstreetmap.gui.jmapviewer.Layer;
import org.openstreetmap.gui.jmapviewer.events.JMVCommandEvent;
import org.openstreetmap.gui.jmapviewer.interfaces.JMapViewerEventListener;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;
import org.openstreetmap.gui.jmapviewer.interfaces.TileSource;
import org.openstreetmap.gui.jmapviewer.tilesources.BingAerialTileSource;
import org.openstreetmap.gui.jmapviewer.tilesources.MapQuestOpenAerialTileSource;
import org.openstreetmap.gui.jmapviewer.tilesources.MapQuestOsmTileSource;
import org.openstreetmap.gui.jmapviewer.tilesources.OsmTileSource;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;

import com.alee.laf.WebLookAndFeel;
import com.heal.dominic.Login.LoginGUI;
import com.heal.dominic.MainInterface.Application;

public class Map extends JFrame implements JMapViewerEventListener {

	private static final long serialVersionUID = 1L;

	private JLabel zoomLabel = null;
	private JLabel zoomValue = null;
	private JLabel lblMperName = null;
	private JLabel lblMperValue = null;

	private static JMapViewerTree treeMap = null;
	static Double lat;
	static Double lon;
	String jlabels[];
	private JButton btnEnterCoordinates;
	private JButton btnID;
	protected static JCheckBox showConnection;
	static String Status;
	static Twitter twitter = LoginGUI.getTwitter();
	protected static Query query;
	protected static Coordinate tweetCoordinate;
	protected static QueryResult result;
	protected static List<Status> mentions;
	protected static Coordinate sourceCoordinate;
	protected static Layer layer = new Layer("test");
	protected static AbstractButton chckbxStatusVisible;
	protected static Layer layer2;
	protected static JTextField yField = new JTextField(15);
	protected static JTextField xField = new JTextField(15);

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
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				Application.class.getResource("/images/tweethub_icon.png")));

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
		lblMperValue = new JLabel(String.format("%s", map().getMeterPerPixel()));

		zoomLabel = new JLabel("Zoom: ");
		zoomValue = new JLabel(String.format("%s", map().getZoom()));

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
				map().setDisplayToFitMapMarkers();
			}
		});
		JComboBox<TileSource> tileSourceSelector = new JComboBox<TileSource>(
				new TileSource[] { new OsmTileSource.Mapnik(),
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
		btnEnterCoordinates.setUI(new BEButtonUI()
		.setNormalColor(BEButtonUI.NormalColor.green));
		btnEnterCoordinates.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MapInputWindows.InputCord();
			}
		});

		btnID = new JButton("Enter Tweet ID");
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

		chckbxStatusVisible = new JCheckBox("Status visible");
		chckbxStatusVisible.setBackground(Color.LIGHT_GRAY);
		chckbxStatusVisible.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (map().getZoom() > 14){
					layer.setVisibleTexts(chckbxStatusVisible.isSelected());
				}
			}
		});
		panelBottom.add(chckbxStatusVisible);

		showConnection = new JCheckBox("Show Connections");
		showConnection.setBackground(Color.LIGHT_GRAY);
		showConnection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				map().setMapPolygonsVisible(showConnection.isSelected());

			}
		});

		panelBottom.add(showConnection);
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
		showZoomControls.setSelected(map().getZoomControlsVisible());
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
				lblCoordinateValue.setText(map().getPosition(p).toString());
			}
		});

		map().addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON3)
				{
					Point p = e.getPoint();
					yField.setText(String.format("%s" ,(map().getPosition(p).getLon())));
					xField.setText(String.format("%s" ,(map().getPosition(p).getLat())));
					MapMenu.showMenu(e, p);
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON3){
					setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON3){
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			}
		});

		new DefaultMapController(map()) {

			@Override
			public void mouseMoved(final MouseEvent e) {
				Point p = e.getPoint();
				int X = p.x + 3;
				int Y = p.y + 3;
				List<MapMarker> ar = map.getMapMarkerList();
				Iterator<MapMarker> i = ar.iterator();
				while (i.hasNext()) {

					final Marker mapMarker = (Marker) i.next();

					Point MarkerPosition = map.getMapPosition(mapMarker.getLat(), mapMarker.getLon());
					if (MarkerPosition != null) {

						int centerX = MarkerPosition.x;
						int centerY = MarkerPosition.y;

						// calculate the radius from the touch to the center of
						// the dot
						double radCircle = Math.sqrt((((centerX - X) * (centerX - X)) + (centerY - Y) * (centerY - Y)));
						//System.out.println(radCircle);
						// if the radius is smaller then 23 (radius of a ball is
						// 5), then it must be on the dot
						if (radCircle < 20) {
							System.out.println(mapMarker.toString() + " is clicked");


							treeMap.getViewer().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
							ToolTipManager.sharedInstance().setInitialDelay(0);
							ToolTipManager.sharedInstance().setReshowDelay(0);
							ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
							
							String html = "<html><body><img src='" +
									mapMarker.getPicture() +
									"' width=48 height=48 align='middle'> " +
									mapMarker.getName();
							map().setToolTipText(html);

						} else if (radCircle > 20) {
							treeMap.getViewer().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
							repaint();
							map().setToolTipText(null);
						}
					}
				}

			}
		};
	}

	public static void main (String[] args){
		WebLookAndFeel.install();
		new Map().setVisible(true);
	}

	protected static JMapViewer map() {
		return treeMap.getViewer();
	}

	private void updateZoomParameters() {
		if (lblMperValue != null)
			lblMperValue.setText(String.format("%s", map().getMeterPerPixel()));
		if (zoomValue != null)
			zoomValue.setText(String.format("%s", map().getZoom()));
		if (map().getZoom() > 14){
			layer.setVisibleTexts(chckbxStatusVisible.isSelected());
		}
		else {
			layer.setVisibleTexts(false);
		}
	}

	@Override
	public void processCommand(JMVCommandEvent command) {
		if (command.getCommand().equals(JMVCommandEvent.COMMAND.ZOOM)
				|| command.getCommand().equals(JMVCommandEvent.COMMAND.MOVE)) {
			updateZoomParameters();
		}
	}
}