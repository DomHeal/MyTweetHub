import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import twitter4j.GeoLocation;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.UIManager;


@SuppressWarnings("serial")
public class Application extends TwitterAppGui {

	private static JPanel contentPane;
	private static JScrollPane hometimeline;
	private static JScrollPane usertimeline;
	private static JList trends;
	private static JTextField txtPostATweet;
	private JTextField textField;
	private JPanel panel_4;
	private static JPanel panel_Timeline = new JPanel();
	private static JPanel panel_Statistics = new JPanel();
	static ImageIcon[] profileimages;
	String statusArr[];
	private static JLabel lblNewLabel;
	private static int CardCounter = 0;
	static int row = 0;
	private static JMenuItem mntmRetweet;
	private static JMenuItem mntmFavourite;
	private static JMenuItem mntmClose;
	private static JMenuItem mntmDelete;

	
	/* Shows Public Timeline */

	@SuppressWarnings("null")
	public static void TimeLine() throws IllegalStateException, TwitterException, MalformedURLException{
		
		Twitter twitter = twitter2;
		
		/* Creates and Displays Public Time */
		
		final List<Status> statusList = twitter.getHomeTimeline();
		final String statusArr[] = new String[statusList.size()];
		final URL[] profileimages = new URL[statusList.size()];
		final DefaultListModel dlm = new DefaultListModel();
		final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm");
	    	for (int i = 0; i < statusList.size(); i++) {
	    		
	    		/* If its Retweeted, Change text colour to GREEN */
	    		if(statusList.get(i).isRetweetedByMe() == true){
		              statusArr[i] = "<html><font color=green><b>" + statusList.get(i).getUser().getName() + ": " + "</b>" + statusList.get(i).getText() + "</font>  </html>";
					  profileimages[i] = new URL(statusList.get(i).getUser().getMiniProfileImageURL());
					  dlm.addElement(new ListEntry(statusArr[i], new ImageIcon (profileimages[i])));
	    		}
	    		/* If its Favourited, Change text colour to ORANGE */
	    		else if (statusList.get(i).isFavorited() == true){
		              statusArr[i] = "<html><font color=orange><b>" + statusList.get(i).getUser().getName() + ": " + "</b>" + statusList.get(i).getText() + "</font>  </html>";
					  profileimages[i] = new URL(statusList.get(i).getUser().getMiniProfileImageURL());
					  dlm.addElement(new ListEntry(statusArr[i], new ImageIcon (profileimages[i])));
	    		}
	    		/* If its Neither, Change text colour to BLACK */
	    		else {
		              statusArr[i] = "<html><font color=black><b>" + statusList.get(i).getUser().getName() + ": " + "</b>" + statusList.get(i).getText() + "</font>  </html>";
					  profileimages[i] = new URL(statusList.get(i).getUser().getMiniProfileImageURL());
					  dlm.addElement(new ListEntry(statusArr[i], new ImageIcon (profileimages[i])));
					  
	    		}
	        }
	    final JList imagestatusList = new JList(dlm);
	    	
	    /* Mouse Listener for ToolTip - Date and Time Posted */
	    
	    imagestatusList.addMouseMotionListener(new MouseAdapter() {
	    	public void mouseMoved(MouseEvent me) {
		    	Point p = new Point(me.getX(),me.getY());
		    	ListModel model = imagestatusList.getModel();
		    	imagestatusList.setSelectedIndex(imagestatusList.locationToIndex(p));
		    	
		    	int index = imagestatusList.locationToIndex(p);
		            if (index > -1) {
		            	imagestatusList.setToolTipText(null);
		            	Date tweetDate = statusList.get(imagestatusList.locationToIndex(p)).getCreatedAt();
		                String text = formatter.format(tweetDate);
		                imagestatusList.setToolTipText(text);
		            }
	    	}
	    });
	    	
	    /* Layout Control */
	    
	    panel_Timeline.setLayout(new CardLayout(0, 0));
	    imagestatusList.setCellRenderer(new ListEntryCellRenderer());
	    imagestatusList.setFont(new Font("SansSerif", Font.PLAIN, 11));
	    imagestatusList.setFixedCellHeight(27);
	    JScrollPane scrollPane = new JScrollPane(imagestatusList,
	                  JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, // vertical bar
	                  JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    
	    final JPopupMenu popupMenu = new JPopupMenu();
	    addPopup(imagestatusList, popupMenu);
	    
	    JMenuItem mntmGetInfo = new JMenuItem("Get Info");
	    popupMenu.add(mntmGetInfo);
	    popupMenu.addSeparator();
	    
	    
	    
	    /* Listener for Popup Menu and Get Selected row */
	    
	    imagestatusList.addMouseListener( new MouseAdapter()
	    {
	        public void mousePressed(MouseEvent e)
	        {
	            if ( SwingUtilities.isRightMouseButton(e) )
	            {
	                row = imagestatusList.locationToIndex(e.getPoint());
	                imagestatusList.setSelectedIndex(row);
	            }
	        }

	    });
	    
	    /* Create Retweet JMenuItem and Assign Listener */
	    
	    mntmRetweet = new JMenuItem("Retweet");
	    mntmRetweet.addChangeListener(new ChangeListener() {
	    	public void stateChanged(ChangeEvent e) {
	    		if (e.getSource() instanceof JMenuItem) {
	    			mntmRetweet = (JMenuItem) e.getSource();
		    		if (mntmRetweet.isSelected() || mntmRetweet.isArmed())
		    		{
		    			mntmRetweet.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Application.class.getResource("retweet_on.png"))));
		    		}
		    		else
		    		{
		    			mntmRetweet.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Application.class.getResource("retweet_hover.png"))));
		    		}
				}
	    	}
	    });
	    
	    mntmRetweet.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Application.class.getResource("retweet_hover.png"))));
	    mntmRetweet.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
   		
	    		long tweetid = statusList.get(row).getId();

	            try {
					twitter2.retweetStatus(tweetid);


				} catch (TwitterException e1) {
					System.out.println("Could Not Retweet Tweet");
					e1.printStackTrace();
				}      
	    	}
	    });
	    popupMenu.add(mntmRetweet);
	    
	    /* Create Favourite JMenuItem and Assign Listener*/
	    
	    mntmFavourite = new JMenuItem("Favourite");
	    mntmFavourite.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
   		
	    		long tweetid = statusList.get(row).getId();

	            try {
					twitter2.createFavorite(tweetid);
				} catch (TwitterException e1) {
					System.out.println("Could Not Favourite Tweet");
					e1.printStackTrace();
				}      
	    	}
	    });
	    mntmFavourite.addChangeListener(new ChangeListener() {
	    	public void stateChanged(ChangeEvent e) {
	    		if (e.getSource() instanceof JMenuItem) {
	    			mntmFavourite = (JMenuItem) e.getSource();
		    		if (mntmFavourite.isSelected() || mntmFavourite.isArmed())
		    		{
		    			mntmFavourite.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Application.class.getResource("favorite_on.png"))));
		    		}
		    		else
		    		{
		    			mntmFavourite.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Application.class.getResource("favorite_hover.png"))));
		    		}
	    		}
	    	
	    	}
	    });
	    mntmFavourite.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Application.class.getResource("favorite_hover.png"))));
	    popupMenu.add(mntmFavourite);
	    popupMenu.addSeparator();
	    
	    
	    /* Create Close JMenuItem and Assign Listener*/
	    
	    mntmClose = new JMenuItem("Close");
	    mntmClose.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
	    		popupMenu.setVisible(false);
	    	}
	    });
	    mntmClose.addChangeListener(new ChangeListener() {
	    	public void stateChanged(ChangeEvent e) {
	    		if (e.getSource() instanceof JMenuItem) {
	    			mntmClose = (JMenuItem) e.getSource();
		    		if (mntmClose.isSelected() ||  mntmClose.isArmed())
		    		{
		    			mntmClose.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Application.class.getResource("dialog_cancel.png"))));
		    		}
		    		else
		    		{
		    			mntmClose.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Application.class.getResource("dialog_close.png"))));
		    		}
	    		}
	    	
	    	}
	    });
	    mntmClose.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Application.class.getResource("dialog_close.png"))));
	    popupMenu.add(mntmClose);
	    setHomeTimeLine(scrollPane);
	    
	    panel_Timeline.add(hometimeline, "home");


	}
	
    /** Returns an ImageIcon, or null if the path was invalid. */
	
    protected static ImageIcon createImageIcon(URL path) {
    	java.net.URL imgURL = path;
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
                return null;
        }
    }

    /* Displays Logged-in User Tweets */

	public static void userTimeLine() throws IllegalStateException, TwitterException{
		Twitter twitter = twitter2;
		final List<Status> statusList2 = twitter.getUserTimeline();
		String statusAr[] = new String[statusList2.size()];
		final DefaultListModel dlm2 = new DefaultListModel();
	    	for (int i = 0; i < statusList2.size(); i++) {
	              statusAr[i] = "<html><b>" + statusList2.get(i).getUser().getName() + ": " + "</b>" + statusList2.get(i).getText() + "</html>";
				  dlm2.addElement(new ListEntry(statusAr[i], new ImageIcon (MiniProfilePic)));
	        }
	    	final JList userStatusJList = new JList(dlm2);
	    	userStatusJList.addMouseMotionListener(new MouseAdapter() {
		    	public void mouseMoved(MouseEvent me) {
			    	Point p = new Point(me.getX(),me.getY());
			    	ListModel model = userStatusJList.getModel();
			    	final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm");
			    	userStatusJList.setSelectedIndex(userStatusJList.locationToIndex(p));
			    	
			    	int index = userStatusJList.locationToIndex(p);
			            if (index > -1) {
			            	userStatusJList.setToolTipText(null);
			            	Date usertweetDate = statusList2.get(userStatusJList.locationToIndex(p)).getCreatedAt();
			                String text = formatter.format(usertweetDate);
			                userStatusJList.setToolTipText(text);
			            }
		    	}
		    });
	          
	    	userStatusJList.setCellRenderer(new ListEntryCellRenderer());
	    	userStatusJList.setFont(new Font("SansSerif", Font.PLAIN, 11));
	    	userStatusJList.setFixedCellHeight(27);
	          JScrollPane scrollPane2 = new JScrollPane(userStatusJList,
	                  JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, // vertical bar
	                  JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	          
	          final JPopupMenu popupMenu = new JPopupMenu();
	          addPopup(userStatusJList, popupMenu);
	  	    /* Listener for Popup Menu and Get Selected row */
	  	    
	        userStatusJList.addMouseListener( new MouseAdapter()
	        {
	        	public void mousePressed(MouseEvent e)
	  	        {
	  	            if (SwingUtilities.isRightMouseButton(e) )
	  	            {
	  	              row = userStatusJList.locationToIndex(e.getPoint());
	  	              userStatusJList.setSelectedIndex(row);
	  	            }
	  	        }

	  	    });
	        
	          
	         mntmDelete = new JMenuItem("Delete");
	         mntmDelete.addActionListener(new ActionListener() {
	        	  public void actionPerformed(ActionEvent e) {
	   		
		    		long tweetid = statusList2.get(row).getId();

		            try {
		            	System.out.println("row");
						System.out.println(tweetid);
						System.out.println(statusList2.get(row).getId());
						twitter2.destroyStatus(tweetid);
						dlm2.remove(row);
						statusList2.remove(row);
												
					} catch (TwitterException e1) {
						System.out.println("Could Not Delete Tweet");
						e1.printStackTrace();
					}      
		            
					String soundName = "pop.wav";    
					AudioInputStream audioInputStream;
					try {
						audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getClass().getResource("/Delete.wav"));
						Clip clip = AudioSystem.getClip();
						clip.open(audioInputStream);
						clip.start();
					} catch (UnsupportedAudioFileException ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					} catch (IOException ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					} catch (LineUnavailableException ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					}
	        	  }
	          });
	          mntmDelete.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Application.class.getResource("delete_normal.png"))));
	          popupMenu.add(mntmDelete);
	          
	          mntmDelete.addChangeListener(new ChangeListener() {
		    	public void stateChanged(ChangeEvent e) {
		    		if (e.getSource() instanceof JMenuItem) {
		    			mntmDelete = (JMenuItem) e.getSource();
			    		if (mntmDelete.isSelected() || mntmDelete.isArmed())
			    		{
			    			mntmDelete.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Application.class.getResource("delete_on.png"))));
			    		}
			    		else
			    		{
			    			mntmDelete.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Application.class.getResource("delete_normal.png"))));
			    		}
		    		}
		    	}
	  	    });
	          
	          
	          
	          JMenuItem mntmClose = new JMenuItem("Close");
	          popupMenu.add(mntmClose);
	          mntmClose.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent arg0) {
		    		popupMenu.setVisible(false);
		    	}
		    });
	          setUserTimeLine(scrollPane2);
	          panel_Timeline.add(usertimeline, "user");


	}
	public static void Trending() {
        try {
        	Twitter twitter = twitter2;
        	DefaultListModel model = new DefaultListModel();
            Trends trends = twitter.getPlaceTrends(23424975);
            List<String> xlist = new ArrayList<>();
            //System.out.println("Showing current trends");
            //System.out.println("As of : " + trends.getAsOf());
            for (Trend trend : trends.getTrends()) {
            	model.addElement(trend.getName());
            }
            JList trendingList = new JList();
            trendingList.setModel(model);
            setTrendingList(trendingList);

        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get current trends: " + te.getMessage());
        }
	}
	public static void Posting() {
		try {
			Twitter twitter = twitter2;
			twitter.updateStatus(txtPostATweet.getText());
			txtPostATweet.setText("");
		}
			catch (TwitterException te) {
	            te.printStackTrace();
	            System.out.println("Failed to post: " + te.getMessage());
	        }
	}

	private static void setTrendingList(JList trendingList) {
		trends = trendingList;
		
	}
	public static boolean setLocalRetweeted(Status status){
		return true;
		
	}
	public static boolean isLocalRetweeted(Status status){
		return true;
		
	}

	private static void setHomeTimeLine(JScrollPane scrollPane) {
		hometimeline = scrollPane;
		
	}

	private static void setUserTimeLine(JScrollPane scrollPane2) {
		usertimeline = scrollPane2;
		
	}
    public JPanel getTimeLinePanel() {
        return panel_Timeline;
    }
	/**
	 * Create the frame.
	 */
	public Application() {
		setTitle("MyTweetHub - Making Twitter, Simple");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1061, 618);
		setLocationRelativeTo(null);
		setIconImage(Toolkit.getDefaultToolkit().getImage(Application.class.getResource("twitter47.png")));
		//setSize(1061, 618);
	    //setShape(new RoundRectangle2D.Double(0, 0, 1061, 610, 50, 50));
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		panel_Statistics.setBorder(null);
		
		panel_Statistics.setBackground(new Color(102, 204, 255));
		panel_Statistics.setBounds(10, 10, 200, 139);
		contentPane.add(panel_Statistics);
		panel_Statistics.setLayout(null);

		
		JLabel lblDefault_Pic = new JLabel();
		lblDefault_Pic.setBorder(new LineBorder(Color.WHITE, 2, true));
		lblDefault_Pic.setToolTipText("Its you!");
		lblDefault_Pic.setBounds(10, 3, 48, 48);
		lblDefault_Pic.setIcon(new ImageIcon(ProfilePic));
		panel_Statistics.add(lblDefault_Pic);
		
		JLabel lblWelcome = new JLabel(Name);
		lblWelcome.setFont(new Font("Myriad Pro", Font.PLAIN, 12));
		lblWelcome.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		lblWelcome.setBounds(68, 11, 96, 14);
		lblWelcome.setForeground(new Color(255, 255, 255));
		panel_Statistics.add(lblWelcome);
		
		JLabel lblUsername = new JLabel("@" + Username);
		lblUsername.setHorizontalAlignment(SwingConstants.TRAILING);
		lblUsername.setFont(new Font("Myriad Pro", Font.ITALIC, 10));
		lblUsername.setVerticalAlignment(SwingConstants.TOP);
		lblUsername.setBounds(61, 30, 103, 21);
		lblUsername.setForeground(new Color(255, 255, 255));
		panel_Statistics.add(lblUsername);
		
		final JLabel lblTweets = new JLabel("Total Tweets:");
		lblTweets.setForeground(Color.WHITE);
		lblTweets.setFont(new Font("Ubuntu", Font.PLAIN, 12));
		lblTweets.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		lblTweets.setBounds(35, 115, 100, 14);
		panel_Statistics.add(lblTweets);
		
		JLabel lblFavourite = new JLabel("Favourites");
		lblFavourite.setHorizontalAlignment(SwingConstants.CENTER);
		lblFavourite.setForeground(new Color(128, 128, 128));
		lblFavourite.setFont(new Font("Ubuntu", Font.PLAIN, 12));
		lblFavourite.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		lblFavourite.setBounds(130, 86, 70, 14);
		panel_Statistics.add(lblFavourite);
		
		JLabel lblFavouriteCount = new JLabel(Integer.toString(FavouriteCnt));
		lblFavouriteCount.setHorizontalAlignment(SwingConstants.CENTER);
		lblFavouriteCount.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblFavouriteCount.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		lblFavouriteCount.setForeground(new Color(0, 191, 255));
		lblFavouriteCount.setBounds(130, 66, 70, 14);
		panel_Statistics.add(lblFavouriteCount);
		
		JLabel lblFollowers = new JLabel("Followers");
		lblFollowers.setHorizontalAlignment(SwingConstants.CENTER);
		lblFollowers.setForeground(new Color(128, 128, 128));
		lblFollowers.setFont(new Font("Ubuntu", Font.PLAIN, 12));
		lblFollowers.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		lblFollowers.setBounds(0, 86, 70, 14);
		panel_Statistics.add(lblFollowers);
		
		JLabel lblFollowerCount = new JLabel(Integer.toString(FollowersCnt));
		lblFollowerCount.setHorizontalAlignment(SwingConstants.CENTER);
		lblFollowerCount.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblFollowerCount.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		lblFollowerCount.setForeground(new Color(0, 191, 255));
		lblFollowerCount.setBounds(0, 66, 64, 14);
		panel_Statistics.add(lblFollowerCount);
		
		JLabel lblFollowing = new JLabel("Following");
		lblFollowing.setHorizontalAlignment(SwingConstants.CENTER);
		lblFollowing.setForeground(new Color(128, 128, 128));
		lblFollowing.setFont(new Font("Ubuntu", Font.PLAIN, 12));
		lblFollowing.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		lblFollowing.setBounds(65, 86, 70, 14);
		panel_Statistics.add(lblFollowing);
		
		JLabel lblFollowingCount = new JLabel(Integer.toString(FollowingCnt));
		lblFollowingCount.setHorizontalAlignment(SwingConstants.CENTER);
		lblFollowingCount.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblFollowingCount.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		lblFollowingCount.setForeground(new Color(0, 191, 255));
		lblFollowingCount.setBounds(65, 66, 65, 14);
		panel_Statistics.add(lblFollowingCount);
				
		JLabel lblTweetsCount =  new JLabel(Integer.toString(TweetCnt));
		lblTweetsCount.setHorizontalAlignment(SwingConstants.LEFT);
		lblTweetsCount.setFont(new Font("Ubuntu", Font.PLAIN, 12));
		lblTweetsCount.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		lblTweetsCount.setForeground(Color.WHITE);
		lblTweetsCount.setBounds(140, 115, 57, 14);
		panel_Statistics.add(lblTweetsCount);
		
		JLabel lblHomebutton = new JLabel("");
		lblHomebutton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblHomebutton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				CardLayout c1 = (CardLayout)(panel_Timeline.getLayout());
				c1.next(panel_Timeline);
				if (CardCounter % 2 == 0){
					lblNewLabel.setIcon(new ImageIcon(getClass().getResource("/twitter-profile3.png")));
				}
				else {
					lblNewLabel.setIcon(new ImageIcon(getClass().getResource("/twitter-profile2.png")));
				}
				CardCounter++;
			}
		});
		lblHomebutton.setBounds(170, 3, 30, 48);
		panel_Statistics.add(lblHomebutton);
		
		lblNewLabel = new JLabel();
		lblNewLabel.setBorder(null);
		lblNewLabel.setIcon(new ImageIcon(getClass().getResource("/twitter-profile2.png")));
		lblNewLabel.setBounds(0, 0, 200, 138);
		panel_Statistics.add(lblNewLabel);
		
		final Panel panel_2 = new Panel();
		panel_2.setLayout(null);
		panel_2.setBackground(new Color(102, 204, 255));
		
		JLabel label_1 = new JLabel("Welcome:");
		label_1.setForeground(Color.WHITE);
		label_1.setBounds(5, 5, 149, 14);
		panel_2.add(label_1);
		
		JLabel label_2 = new JLabel((String) null);
		label_2.setForeground(Color.WHITE);
		label_2.setBounds(68, 5, 86, 14);
		panel_2.add(label_2);
		
		JLabel label_3 = new JLabel("Your Statistics:");
		label_3.setHorizontalAlignment(SwingConstants.CENTER);
		label_3.setBounds(0, 91, 154, 14);
		panel_2.add(label_3);
		
		JLabel label_4 = new JLabel("Favourites:");
		label_4.setBounds(5, 156, 70, 14);
		panel_2.add(label_4);
		
		JLabel label_5 = new JLabel("0");
		label_5.setBounds(70, 156, 53, 14);
		panel_2.add(label_5);
		
		JLabel label_6 = new JLabel("Followers:");
		label_6.setBounds(5, 116, 70, 14);
		panel_2.add(label_6);
		
		JLabel label_7 = new JLabel("0");
		label_7.setBounds(70, 116, 46, 14);
		panel_2.add(label_7);
		
		JLabel label_8 = new JLabel("Following:");
		label_8.setBounds(5, 136, 70, 14);
		panel_2.add(label_8);
		
		JLabel label_9 = new JLabel("0");
		label_9.setBounds(70, 136, 46, 14);
		panel_2.add(label_9);
		
		JLabel label_10 = new JLabel();
		label_10.setBounds(5, 30, 149, 134);
		panel_2.add(label_10);

		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					try {
						panel_Timeline.remove(hometimeline);
						panel_Timeline.remove(usertimeline);
						lblNewLabel.setIcon(new ImageIcon(getClass().getResource("/twitter-profile2.png")));
						TimeLine();
						userTimeLine();
						panel_Timeline.updateUI();
						panel_Statistics.updateUI();
						
						
					} catch (IllegalStateException | TwitterException | MalformedURLException e) {
						e.printStackTrace();
					}
					String soundName = "pop.wav";    
					AudioInputStream audioInputStream;
					try {
						audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getClass().getResource("/pop.wav"));
						Clip clip = AudioSystem.getClip();
						clip.open(audioInputStream);
						clip.start();
					} catch (UnsupportedAudioFileException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (LineUnavailableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}


			}
		});
		btnRefresh.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
		btnRefresh.setBounds(38, 160, 154, 23);
		contentPane.add(btnRefresh);
		
		JPanel panel_Trending = new JPanel();
		panel_Trending.setFont(new Font("SansSerif", Font.PLAIN, 11));
		panel_Trending.setBackground(new Color(255, 255, 255));
		panel_Trending.setBounds(10, 219, 200, 178);
		contentPane.add(panel_Trending);
		
		JButton btnLogout = new JButton("Logout");
		btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				btnLogoutActionPerformed(evt);
			}
		});

		btnLogout.setForeground(new Color(255, 255, 255));
		btnLogout.setBackground(new Color(255, 255, 255));
		btnLogout.setBounds(38, 584, 154, 23);
		btnLogout.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
		contentPane.add(btnLogout);
		
		
			try {
				TimeLine();
				userTimeLine();
				Trending();
			} catch (IllegalStateException | TwitterException | MalformedURLException e) {
				e.printStackTrace();
			}


		panel_Timeline.setBounds(215, 37, 830, 540);
		contentPane.add(panel_Timeline);
		
		trends.setFont(new Font("SansSerif", Font.PLAIN, 11));
		panel_Trending.add(trends);
		panel_Trending.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 5));
		
		txtPostATweet = new JTextField();
		txtPostATweet.setBorder(null);
		txtPostATweet.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				txtPostATweet.setText("");
			}
		});
		txtPostATweet.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtPostATweet.setText("Post A Tweet Here....");
		txtPostATweet.setBounds(215, 10, 740, 23);
		contentPane.add(txtPostATweet);
		txtPostATweet.setColumns(10);
		
		JButton btnPost = new JButton("Post");
		btnPost.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Posting();
				JOptionPane.showMessageDialog(null,"You have successfully posted a tweet.");

			}
		});
		btnPost.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnPost.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnPost.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
		btnPost.setBounds(965, 11, 80, 23);
		contentPane.add(btnPost);
		
		JPanel panel_Query = new JPanel();
		panel_Query.setBackground(new Color(102, 204, 255));
		panel_Query.setBounds(10, 408, 200, 98);
		contentPane.add(panel_Query);
		panel_Query.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(10, 26, 180, 20);
		panel_Query.add(textField);
		textField.setColumns(10);
		
		JLabel lblSearchQuery = new JLabel("Search / Query");
		lblSearchQuery.setHorizontalAlignment(SwingConstants.CENTER);
		lblSearchQuery.setForeground(Color.WHITE);
		lblSearchQuery.setBounds(0, 11, 200, 14);
		panel_Query.add(lblSearchQuery);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
		btnSearch.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnSearch.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnSearch.setBounds(60, 50, 80, 23);
		panel_Query.add(btnSearch);
		
		panel_4 = new JPanel();
		panel_4.setBackground(new Color(102, 204, 255));
		panel_4.setBounds(10, 194, 200, 204);
		contentPane.add(panel_4);
		
		JLabel lblTrending = new JLabel("Trending");
		panel_4.add(lblTrending);
		lblTrending.setForeground(new Color(255, 255, 255));
		
		JCheckBox chckbxRetweets = new JCheckBox("Remove Retweets");
		chckbxRetweets.setOpaque(false);
		chckbxRetweets.setBackground(new Color(0, 153, 204));
		chckbxRetweets.setForeground(Color.WHITE);
		chckbxRetweets.setFont(new Font("SansSerif", Font.PLAIN, 10));
		chckbxRetweets.setBounds(259, 584, 121, 23);
		contentPane.add(chckbxRetweets);
		
		JCheckBox chckbxHyperlinks = new JCheckBox("Remove Hyperlinks");
		chckbxHyperlinks.setOpaque(false);
		chckbxHyperlinks.setFont(new Font("SansSerif", Font.PLAIN, 10));
		chckbxHyperlinks.setBackground(new Color(0, 153, 204));
		chckbxHyperlinks.setForeground(Color.WHITE);
		chckbxHyperlinks.setBounds(398, 584, 121, 23);
		contentPane.add(chckbxHyperlinks);
		
		JButton BtnToggleTimeline = new JButton("Switch Timeline");
		BtnToggleTimeline.setBorder(null);
		BtnToggleTimeline.setBackground(Color.WHITE);
		BtnToggleTimeline.setForeground(new Color(255, 255, 255));
		BtnToggleTimeline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CardLayout c1 = (CardLayout)(panel_Timeline.getLayout());
				c1.next(panel_Timeline);
				if (CardCounter % 2 == 0){
					lblNewLabel.setIcon(new ImageIcon(getClass().getResource("/twitter-profile3.png")));
				}
				else {
					lblNewLabel.setIcon(new ImageIcon(getClass().getResource("/twitter-profile2.png")));
				}
				CardCounter++;
			}
		});
		BtnToggleTimeline.setBounds(560, 584, 154, 23);
		BtnToggleTimeline.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
		contentPane.add(BtnToggleTimeline);
		
		JLabel lblVersion = new JLabel("Version 0.45 Free Edition");
		lblVersion.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblVersion.setHorizontalAlignment(SwingConstants.RIGHT);
		lblVersion.setForeground(new Color(255, 255, 255));
		lblVersion.setBounds(881, 577, 164, 14);
		contentPane.add(lblVersion);
		
		JButton btnNewButton = new JButton("Globe View");
		btnNewButton.setBorder(null);
		btnNewButton.setForeground(new Color(255, 255, 255));
		btnNewButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Map().setVisible(true);
			}
		});
		btnNewButton.setBounds(723, 584, 164, 23);
		contentPane.add(btnNewButton);
		
		JLabel lblBackground = new JLabel("");
		lblBackground.setBorder(null);
		lblBackground.setBounds(0, 0, 1061, 618);
		lblBackground.setIcon(new ImageIcon(getClass().getResource("/blurred backgrounds.png"))); 
		contentPane.add(lblBackground);
		
		String soundName = "welcome.wav";    
		AudioInputStream audioInputStream;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getClass().getResource("/welcome.wav"));
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
	protected void btnLogoutActionPerformed(ActionEvent evt) {
		this.dispose();
		new TwitterAppGui().setVisible(true);
	}
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}



