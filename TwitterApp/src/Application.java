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
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


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
	ImageIcon[] profileimages;
	String statusArr[];
	private static JLabel lblNewLabel;
	private static int CardCounter = 0;
	private static JList imagestatusList = new JList();

	/* Shows Public Timeline */

	@SuppressWarnings("null")
	public static void TimeLine() throws IllegalStateException, TwitterException, MalformedURLException{
		Twitter twitter = twitter2;
		List<Status> statusList = twitter.getHomeTimeline();
		String statusArr[] = new String[statusList.size()];
		URL[] profileimages = new URL[statusList.size()];
		DefaultListModel dlm = new DefaultListModel();
	    	for (int i = 0; i < statusList.size(); i++) {
	              Date tweetDate = statusList.get(i).getCreatedAt();
	              SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm");
	              statusArr[i] = formatter.format(tweetDate) + " - " + statusList.get(i).getUser().getName() + ": " + statusList.get(i).getText();
				  profileimages[i] = new URL(statusList.get(i).getUser().getMiniProfileImageURL());
				  dlm.addElement(new ListEntry(statusArr[i], new ImageIcon (profileimages[i])));
	        }
	    	final JList imagestatusList = new JList(dlm);
	    	
/*	    	imagestatusList.addMouseMotionListener(new MouseAdapter() {
	    		public void mousePressed(MouseEvent me) {
		    		Point p = new Point(me.getX(),me.getY());
		    		imagestatusList.setSelectedIndex(imagestatusList.locationToIndex(p));
		    		System.out.println(imagestatusList.locationToIndex(p));
	    		}
	    		});*/
	   
	    	//For Printing out in Console
/*	        for (Status status : statusList) {
	            System.out.println(status.getUser().getName() + ":" +
	                               status.getText());
	        }*/
	    	
	    panel_Timeline.setLayout(new CardLayout(0, 0));
	    imagestatusList.setCellRenderer(new ListEntryCellRenderer());
	    imagestatusList.setFont(new Font("SansSerif", Font.PLAIN, 11));
	    imagestatusList.setFixedCellHeight(25);
	    JScrollPane scrollPane = new JScrollPane(imagestatusList,
	                  JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, // vertical bar
	                  JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
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



	public static void userTimeLine() throws IllegalStateException, TwitterException{
		Twitter twitter = twitter2;
		List<Status> statusList2 = twitter.getUserTimeline();
		String statusAr[] = new String[statusList2.size()];
	    	for (int i = 0; i < statusList2.size(); i++) {
	              Date tweetDate = statusList2.get(i).getCreatedAt();
	              SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm");
	              statusAr[i] = formatter.format(tweetDate) + " - " + statusList2.get(i).getUser().getName() + ": " + statusList2.get(i).getText();
	        }
	    	//For Printing out in Console
/*	        for (Status status : statusList2) {
	            System.out.println(status.getUser().getName() + ":" +
	                               status.getText());
	        }*/
	          JList statusJList2 = new JList(statusAr);
	          statusJList2.setFont(new Font("SansSerif", Font.PLAIN, 11));
	          statusJList2.setFixedCellHeight(25);
	          JScrollPane scrollPane2 = new JScrollPane(statusJList2,
	                  JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, // vertical bar
	                  JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
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
               // System.out.println(" " + trend.getName());
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
		
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 153, 204));
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
		lblTweets.setFont(new Font("Miriam", Font.PLAIN, 12));
		lblTweets.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		lblTweets.setBounds(35, 115, 70, 14);
		panel_Statistics.add(lblTweets);
		
		JLabel lblFavourite = new JLabel("Favourites");
		lblFavourite.setHorizontalAlignment(SwingConstants.CENTER);
		lblFavourite.setForeground(new Color(128, 128, 128));
		lblFavourite.setFont(new Font("Miriam", Font.PLAIN, 12));
		lblFavourite.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		lblFavourite.setBounds(130, 86, 70, 14);
		panel_Statistics.add(lblFavourite);
		
		JLabel lblFavouriteCount = new JLabel(Integer.toString(FavouriteCnt));
		lblFavouriteCount.setHorizontalAlignment(SwingConstants.CENTER);
		lblFavouriteCount.setFont(new Font("Miriam", Font.BOLD, 14));
		lblFavouriteCount.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		lblFavouriteCount.setForeground(new Color(0, 191, 255));
		lblFavouriteCount.setBounds(130, 66, 70, 14);
		panel_Statistics.add(lblFavouriteCount);
		
		JLabel lblFollowers = new JLabel("Followers");
		lblFollowers.setHorizontalAlignment(SwingConstants.CENTER);
		lblFollowers.setForeground(new Color(128, 128, 128));
		lblFollowers.setFont(new Font("Miriam", Font.PLAIN, 12));
		lblFollowers.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		lblFollowers.setBounds(0, 86, 70, 14);
		panel_Statistics.add(lblFollowers);
		
		JLabel lblFollowerCount = new JLabel(Integer.toString(FollowersCnt));
		lblFollowerCount.setHorizontalAlignment(SwingConstants.CENTER);
		lblFollowerCount.setFont(new Font("Miriam", Font.BOLD, 14));
		lblFollowerCount.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		lblFollowerCount.setForeground(new Color(0, 191, 255));
		lblFollowerCount.setBounds(0, 66, 64, 14);
		panel_Statistics.add(lblFollowerCount);
		
		JLabel lblFollowing = new JLabel("Following");
		lblFollowing.setHorizontalAlignment(SwingConstants.CENTER);
		lblFollowing.setForeground(new Color(128, 128, 128));
		lblFollowing.setFont(new Font("Miriam", Font.PLAIN, 12));
		lblFollowing.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		lblFollowing.setBounds(65, 86, 70, 14);
		panel_Statistics.add(lblFollowing);
		
		JLabel lblFollowingCount = new JLabel(Integer.toString(FollowingCnt));
		lblFollowingCount.setHorizontalAlignment(SwingConstants.CENTER);
		lblFollowingCount.setFont(new Font("Miriam", Font.BOLD, 14));
		lblFollowingCount.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		lblFollowingCount.setForeground(new Color(0, 191, 255));
		lblFollowingCount.setBounds(65, 66, 65, 14);
		panel_Statistics.add(lblFollowingCount);
				
		JLabel lblTweetsCount = new JLabel(Integer.toString(TweetCnt));
		lblTweetsCount.setHorizontalAlignment(SwingConstants.LEFT);
		lblTweetsCount.setFont(new Font("Miriam", Font.PLAIN, 12));
		lblTweetsCount.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		lblTweetsCount.setForeground(Color.WHITE);
		lblTweetsCount.setBounds(107, 115, 57, 14);
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
						TimeLine();
						userTimeLine();
						panel_Timeline.updateUI();
						panel_Statistics.updateUI();
					} catch (IllegalStateException | TwitterException | MalformedURLException e) {
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
		btnLogout.setBounds(38, 556, 154, 23);
		btnLogout.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
		contentPane.add(btnLogout);
		
		
			try {
				TimeLine();
				userTimeLine();
				Trending();
			} catch (IllegalStateException | TwitterException | MalformedURLException e) {
				e.printStackTrace();
			}


		panel_Timeline.setBounds(215, 37, 830, 512);
		contentPane.add(panel_Timeline);
		
		trends.setFont(new Font("SansSerif", Font.PLAIN, 11));
		panel_Trending.add(trends);
		panel_Trending.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 5));
		
		txtPostATweet = new JTextField();
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
		panel_Query.setBounds(10, 451, 200, 98);
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
		panel_4.setBounds(10, 194, 200, 203);
		contentPane.add(panel_4);
		
		JLabel lblTrending = new JLabel("Trending");
		panel_4.add(lblTrending);
		lblTrending.setForeground(new Color(255, 255, 255));
		
		JCheckBox chckbxRetweets = new JCheckBox("Remove Retweets");
		chckbxRetweets.setOpaque(false);
		chckbxRetweets.setBackground(new Color(0, 153, 204));
		chckbxRetweets.setForeground(Color.WHITE);
		chckbxRetweets.setFont(new Font("SansSerif", Font.PLAIN, 10));
		chckbxRetweets.setBounds(259, 556, 121, 23);
		contentPane.add(chckbxRetweets);
		
		JCheckBox chckbxHyperlinks = new JCheckBox("Remove Hyperlinks");
		chckbxHyperlinks.setOpaque(false);
		chckbxHyperlinks.setFont(new Font("SansSerif", Font.PLAIN, 10));
		chckbxHyperlinks.setBackground(new Color(0, 153, 204));
		chckbxHyperlinks.setForeground(Color.WHITE);
		chckbxHyperlinks.setBounds(398, 556, 121, 23);
		contentPane.add(chckbxHyperlinks);
		
		JButton BtnToggleTimeline = new JButton("Switch Timeline");
		BtnToggleTimeline.setBackground(Color.WHITE);
		BtnToggleTimeline.setForeground(new Color(255, 255, 255));
		BtnToggleTimeline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CardLayout c1 = (CardLayout)(panel_Timeline.getLayout());
				c1.next(panel_Timeline);
			}
		});
		BtnToggleTimeline.setBounds(559, 556, 154, 23);
		BtnToggleTimeline.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
		contentPane.add(BtnToggleTimeline);
		
		JLabel lblVersion = new JLabel("Version 0.45 Free Edition");
		lblVersion.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblVersion.setHorizontalAlignment(SwingConstants.RIGHT);
		lblVersion.setForeground(new Color(255, 255, 255));
		lblVersion.setBounds(881, 576, 164, 14);
		contentPane.add(lblVersion);
		
		JButton btnNewButton = new JButton("Globe View");
		btnNewButton.setForeground(new Color(255, 255, 255));
		btnNewButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Map().setVisible(true);
			}
		});
		btnNewButton.setBounds(723, 556, 164, 23);
		contentPane.add(btnNewButton);
		
		JLabel lblBackground = new JLabel("");
		lblBackground.setBorder(null);
		lblBackground.setBounds(0, 0, 1055, 590);
		lblBackground.setIcon(new ImageIcon(getClass().getResource("/twitterbackground.png"))); 
		contentPane.add(lblBackground);

        JPopupMenu popup = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Get Info");
        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = imagestatusList.locationToIndex(e.getPoint());
                    System.out.println("Double clicked on Item " + index);
                 }
            }
        };
        imagestatusList.addMouseListener(mouseListener);
            
       
        
/*      JMenuItem menuItem = new JMenuItem("Favourite");
      
        JMenuItem menuItem = new JMenuItem("Retweet");*/
        
		


	}
	protected void btnLogoutActionPerformed(ActionEvent evt) {
		this.dispose();
		new TwitterAppGui().setVisible(true);
	}
}



