import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.JMapViewerTree;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.events.JMVCommandEvent;
import org.openstreetmap.gui.jmapviewer.interfaces.JMapViewerEventListener;
import org.openstreetmap.gui.jmapviewer.interfaces.TileSource;
import org.openstreetmap.gui.jmapviewer.tilesources.BingAerialTileSource;
import org.openstreetmap.gui.jmapviewer.tilesources.MapQuestOpenAerialTileSource;
import org.openstreetmap.gui.jmapviewer.tilesources.MapQuestOsmTileSource;
import org.openstreetmap.gui.jmapviewer.tilesources.OsmTileSource;


public class Map extends JFrame implements JMapViewerEventListener {
	private JFrame frame;
    private JLabel zoomLabel=null;
    private JLabel zoomValue=null;

    private JLabel mperpLabelName=null;
    private JLabel mperpLabelValue = null;
    
    private JMapViewerTree treeMap = null;

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
	        mperpLabelName=new JLabel("Meters/Pixels: ");
	        mperpLabelValue=new JLabel(String.format("%s",map().getMeterPerPixel()));

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
	        panelTop.add(mperpLabelName);
	        panelTop.add(mperpLabelValue);

	        getContentPane().add(treeMap, BorderLayout.CENTER);

	        map().addMouseMotionListener(new MouseAdapter() {
	            @Override
	            public void mouseMoved(MouseEvent e) {
	                Point p = e.getPoint();
	                boolean cursorHand = map().getAttribution().handleAttributionCursor(p);
	                if (cursorHand) {
	                    map().setCursor(new Cursor(Cursor.HAND_CURSOR));
	                } else {
	                    map().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	                }
	                if(showToolTip.isSelected()) map().setToolTipText(map().getPosition(p).toString());
	            }
	        });
	        map().addMapMarker(new MapMarkerDot(51.6291, -4));

	}

    private JMapViewer map(){
        return treeMap.getViewer();
    }
    private static Coordinate c(double lat, double lon){
        return new Coordinate(lat, lon);
    }

    /**
     * @param args
     */
    private void updateZoomParameters() {
        if (mperpLabelValue!=null)
            mperpLabelValue.setText(String.format("%s",map().getMeterPerPixel()));
        if (zoomValue!=null)
            zoomValue.setText(String.format("%s", map().getZoom()));
    }

    @Override
    public void processCommand(JMVCommandEvent command) {
        if (command.getCommand().equals(JMVCommandEvent.COMMAND.ZOOM) ||
                command.getCommand().equals(JMVCommandEvent.COMMAND.MOVE)) {
            updateZoomParameters();
        }
    }
    
}
