package com.heal.dominic.MainInterface;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.TwitterException;

import com.heal.dominic.Login.LoginGUI;
import com.heal.dominic.Login.UserData;
import com.heal.dominic.MapInterface.Map;
import com.heal.dominic.ResourceManager.ImageController;
import com.heal.dominic.ResourceManager.SoundController;
import com.heal.dominic.Splash.Splash;

@SuppressWarnings("serial")
public class Application extends LoginGUI implements Runnable {
	public Application() {
		setTitle("MyTweetHub - Making Twitter, Simple");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1054, 590);
		setLocationRelativeTo(null);
	}

	private static JPanel contentPane;
	private static JScrollPane hometimeline;
	protected static JScrollPane usertimeline;
	private static JList<String> trends = null;
	protected static JTextField txtPostATweet;
	private JButton BtnToggleTimeline;
	protected static JPanel panel_Timeline = new JPanel();
	private static JPanel panel_Statistics = new JPanel();
	static String statusArr[];
	static JFrame frame;
	private static JLabel lblStatisticsBackground;
	private static int CardCounter = 0;
	static int row = 0;
	private static JMenuItem mntmRetweet;
	private static JMenuItem mntmFavourite;
	private static JMenuItem mntmClose;
	static JMenuItem mntmDelete;
	protected static JMenuItem mntmGetInfo;
	static String POST_MESSAGE = "POST A TWEET HERE...";
	static List<Status> statusList;
	static DefaultListModel<ListEntry> dlm = new DefaultListModel<ListEntry>();
	static SimpleDateFormat formatter = new SimpleDateFormat(
			"dd/MM/yy HH:mm");
	static URL[] profileimages;
	static int refresh_timer = 120;

	/* Shows Public Timeline */


	public void TimelineGUI(){

		final JList<ListEntry> imagestatusList = new JList<ListEntry>(dlm);

		/* Mouse Listener for ToolTip - Date and Time Posted */

		imagestatusList.addMouseMotionListener(new MouseAdapter() {
			public void mouseMoved(MouseEvent me) {
				Point p = new Point(me.getX(), me.getY());
				imagestatusList.setSelectedIndex(imagestatusList
						.locationToIndex(p));

				int index = imagestatusList.locationToIndex(p);
				if (index > -1) {
					imagestatusList.setToolTipText(null);
					Date tweetDate = statusList.get(
							imagestatusList.locationToIndex(p)).getCreatedAt();
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
		new PopUp().addPopup(imagestatusList, popupMenu);

		mntmGetInfo = new JMenuItem("Get Info");
		mntmGetInfo.setIcon(new ImageIcon(Toolkit
				.getDefaultToolkit().getImage(Application.class
						.getResource("/images/Note-Add_hover.png"))));
		popupMenu.add(mntmGetInfo);
		mntmGetInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					new Info(statusList.get(row));

				} catch (TwitterException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		mntmGetInfo.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (e.getSource() instanceof JMenuItem) {
					mntmGetInfo = (JMenuItem) e.getSource();
					if (mntmGetInfo.isSelected() || mntmGetInfo.isArmed()) {
						mntmGetInfo.setIcon(new ImageIcon(Toolkit
								.getDefaultToolkit().getImage(
										Application.class
										.getResource("/images/Note-Add.png"))));
					} else {
						mntmGetInfo.setIcon(new ImageIcon(
								Toolkit.getDefaultToolkit()
								.getImage(
										Application.class
										.getResource("/images/Note-Add_hover.png"))));
					}
				}
			}
		});
		popupMenu.addSeparator();

		/* Listener for Popup Menu and Get Selected row */

		imagestatusList.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
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
					if (mntmRetweet.isSelected() || mntmRetweet.isArmed()) {
						mntmRetweet.setIcon(new ImageIcon(Toolkit
								.getDefaultToolkit().getImage(
										Application.class
										.getResource("/images/retweet_on.png"))));
					} else {
						mntmRetweet.setIcon(new ImageIcon(
								Toolkit.getDefaultToolkit()
								.getImage(
										Application.class
										.getResource("/images/retweet_hover.png"))));
					}
				}
			}
		});

		mntmRetweet.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				Application.class.getResource("/images/retweet_hover.png"))));
		mntmRetweet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				long tweetid = statusList.get(row).getId();

				try {
					twitter.retweetStatus(tweetid);
					statusArr[row] = "<html><font color=green><b>"
							+ statusList.get(row).getUser().getName() + ": "
							+ "</b>" + statusList.get(row).getText()
							+ "</font>  </html>";
					dlm.set(row, new ListEntry(statusArr[row], new ImageIcon(profileimages[row])));

				} catch (TwitterException e1) {
					System.out.println("Could Not Retweet Tweet");
					e1.printStackTrace();
				}
			}
		});
		popupMenu.add(mntmRetweet);

		/* Create Favourite JMenuItem and Assign Listener */

		mntmFavourite = new JMenuItem("Favourite");
		mntmFavourite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				long tweetid = statusList.get(row).getId();

				try {
					twitter.createFavorite(tweetid);
					statusArr[row] = "<html><font color=orange><b>"
							+ statusList.get(row).getUser().getName() + ": "
							+ "</b>" + statusList.get(row).getText()
							+ "</font>  </html>";
					dlm.set(row, new ListEntry(statusArr[row], new ImageIcon(profileimages[row])));
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
					if (mntmFavourite.isSelected() || mntmFavourite.isArmed()) {
						mntmFavourite.setIcon(new ImageIcon(Application.class
								.getResource("/images/favorite_on.png")));
					} else {
						mntmFavourite.setIcon(new ImageIcon(Application.class
								.getResource("/images/favorite_hover.png")));
					}
				}

			}
		});
		mntmFavourite.setIcon(new ImageIcon(Application.class
				.getResource("/images/favorite_hover.png")));
		popupMenu.add(mntmFavourite);
		popupMenu.addSeparator();

		/* Create Close JMenuItem and Assign Listener */

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
					if (mntmClose.isSelected() || mntmClose.isArmed()) {
						mntmClose.setIcon(new ImageIcon(Application.class
								.getResource("/images/dialog_cancel.png")));
					} else {
						mntmClose.setIcon(new ImageIcon(Application.class
								.getResource("/images/dialog_close.png")));
					}
				}

			}
		});
		mntmClose.setIcon(new ImageIcon(Application.class
				.getResource("/images/dialog_close.png")));
		popupMenu.add(mntmClose);
		setHomeTimeLine(scrollPane);
		panel_Timeline.add(hometimeline);
	}


	/* Create the frame.
	 * 
	 * @throws UnsupportedLookAndFeelException
	 */
	public void run() {
		try {
			Trending.getTrending();
			Timelines.getTimeLine();
			TimelineGUI();
			Timelines.getUserTimeLine();

		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (TwitterException e) {
			e.printStackTrace();
		}

		contentPane = new JPanel();
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		JButton btnClose = new JButton(ImageController.Image_Close);
		btnClose.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnClose.setSize(16, 16);
		btnClose.setLocation(1029, 3);
		btnClose.setFocusPainted(false);
		btnClose.setBorder(BorderFactory.createEmptyBorder());
		btnClose.setBorderPainted(false);
		btnClose.setContentAreaFilled(false);
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});

		getContentPane().add(btnClose);

		panel_Statistics.setBorder(null);
		panel_Statistics.setBackground(new Color(102, 204, 255));
		panel_Statistics.setBounds(0, 0, 208, 152);;
		panel_Statistics.setLayout(null);
		contentPane.add(panel_Statistics);

		JLabel lblWelcome = new JLabel(UserData.getName());
		lblWelcome.setFont(new Font("Myriad Pro", Font.PLAIN, 12));
		lblWelcome.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		lblWelcome.setBounds(68, 11, 96, 14);
		lblWelcome.setForeground(new Color(255, 255, 255));
		panel_Statistics.add(lblWelcome);

		JLabel lblUsername = new JLabel("@" + UserData.getUsername());
		lblUsername.setHorizontalAlignment(SwingConstants.TRAILING);
		lblUsername.setFont(new Font("Myriad Pro", Font.ITALIC, 10));
		lblUsername.setVerticalAlignment(SwingConstants.TOP);
		lblUsername.setBounds(61, 30, 103, 21);
		lblUsername.setForeground(new Color(255, 255, 255));
		panel_Statistics.add(lblUsername);

		final JLabel lblTweets = new JLabel("TOTAL TWEETS:");
		lblTweets.setForeground(Color.WHITE);
		lblTweets.setFont(new Font("Segoe UI", Font.BOLD, 10));
		lblTweets.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		lblTweets.setBounds(45, 125, 100, 14);
		panel_Statistics.add(lblTweets);

		JLabel lblFavourite = new JLabel("FAVOURITES");
		lblFavourite.setHorizontalAlignment(SwingConstants.CENTER);
		lblFavourite.setForeground(new Color(128, 128, 128));
		lblFavourite.setFont(new Font("Segoe UI", Font.BOLD, 8));
		lblFavourite.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		lblFavourite.setBounds(136, 90, 67, 14);
		panel_Statistics.add(lblFavourite);

		JLabel lblFavouriteCount = new JLabel(Integer.toString(UserData.getFavouriteCn()));
		lblFavouriteCount.setHorizontalAlignment(SwingConstants.CENTER);
		lblFavouriteCount.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lblFavouriteCount.setCursor(Cursor
				.getPredefinedCursor(Cursor.TEXT_CURSOR));
		lblFavouriteCount.setForeground(new Color(0, 191, 255));
		lblFavouriteCount.setBounds(135, 73, 68, 14);
		panel_Statistics.add(lblFavouriteCount);

		JLabel lblFollowers = new JLabel("FOLLOWERS");
		lblFollowers.setHorizontalAlignment(SwingConstants.CENTER);
		lblFollowers.setForeground(new Color(128, 128, 128));
		lblFollowers.setFont(new Font("Segoe UI", Font.BOLD, 8));
		lblFollowers.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		lblFollowers.setBounds(0, 90, 70, 14);
		panel_Statistics.add(lblFollowers);

		JLabel lblFollowerCount = new JLabel(Integer.toString(UserData.getFollowerCnt()));
		lblFollowerCount.setHorizontalAlignment(SwingConstants.CENTER);
		lblFollowerCount.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lblFollowerCount.setCursor(Cursor
				.getPredefinedCursor(Cursor.TEXT_CURSOR));
		lblFollowerCount.setForeground(new Color(0, 191, 255));
		lblFollowerCount.setBounds(4, 73, 65, 14);
		panel_Statistics.add(lblFollowerCount);

		JLabel lblFollowing = new JLabel("FOLLOWING");
		lblFollowing.setHorizontalAlignment(SwingConstants.CENTER);
		lblFollowing.setForeground(new Color(128, 128, 128));
		lblFollowing.setFont(new Font("Segoe UI", Font.BOLD, 8));
		lblFollowing.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		lblFollowing.setBounds(71, 90, 64, 14);
		panel_Statistics.add(lblFollowing);

		JLabel lblFollowingCount = new JLabel(Integer.toString(UserData.getFollowingCnt()));
		lblFollowingCount.setHorizontalAlignment(SwingConstants.CENTER);
		lblFollowingCount.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lblFollowingCount.setCursor(Cursor
				.getPredefinedCursor(Cursor.TEXT_CURSOR));
		lblFollowingCount.setForeground(new Color(0, 191, 255));
		lblFollowingCount.setBounds(71, 73, 63, 14);
		panel_Statistics.add(lblFollowingCount);

		JLabel lblTweetsCount = new JLabel(Integer.toString(UserData.getTweetCnt()));
		lblTweetsCount.setHorizontalAlignment(SwingConstants.LEFT);
		lblTweetsCount.setFont(new Font("Segoe UI", Font.BOLD, 10));
		lblTweetsCount
		.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		lblTweetsCount.setForeground(Color.WHITE);
		lblTweetsCount.setBounds(143, 125, 57, 14);
		panel_Statistics.add(lblTweetsCount);

		final JLabel lblHomebutton = new JLabel();
		lblHomebutton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblHomebutton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				BtnToggleTimeline.doClick();
			}
		});
		lblHomebutton.setBounds(174, 7, 30, 57);
		panel_Statistics.add(lblHomebutton);

		JLabel lblDefault_Pic = new JLabel();
		lblDefault_Pic.setBounds(10, 11, 48, 48);
		lblDefault_Pic.setBorder(new LineBorder(Color.WHITE, 2, true));
		lblDefault_Pic.setToolTipText("Its you!");
		lblDefault_Pic.setIcon(new ImageIcon(UserData.getProfilePic()));
		lblDefault_Pic.setIcon(new ImageIcon(UserData.getProfilePic()));
		panel_Statistics.add(lblDefault_Pic);

		lblStatisticsBackground = new JLabel();
		lblStatisticsBackground.setBorder(null);
		lblStatisticsBackground.setIcon(new ImageIcon(getClass().getResource(
				"/images/Statistics_panel_new.png")));
		lblStatisticsBackground.setBounds(0, 0, 208, 152);
		panel_Statistics.add(lblStatisticsBackground);

		final Panel panelStatistics = new Panel();
		panelStatistics.setLayout(null);
		panelStatistics.setBackground(new Color(102, 204, 255));

		JLabel lblWelcomeTitle = new JLabel("Welcome:");
		lblWelcomeTitle.setForeground(Color.WHITE);
		lblWelcomeTitle.setBounds(5, 5, 149, 14);
		panelStatistics.add(lblWelcomeTitle);

		JLabel lblRealName = new JLabel((String) null);
		lblRealName.setForeground(Color.WHITE);
		lblRealName.setBounds(68, 5, 86, 14);
		panelStatistics.add(lblRealName);

		JLabel lblStatisticsTitle = new JLabel("Your Statistics:");
		lblStatisticsTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblStatisticsTitle.setBounds(0, 91, 154, 14);
		panelStatistics.add(lblStatisticsTitle);

		JLabel lblFavouritesTitle = new JLabel("Favourites:");
		lblFavouritesTitle.setBounds(5, 156, 70, 14);
		panelStatistics.add(lblFavouritesTitle);

		JLabel lblFavouritesValue = new JLabel("0");
		lblFavouritesValue.setBounds(70, 156, 53, 14);
		panelStatistics.add(lblFavouritesValue);

		JLabel lblFollowersTitle = new JLabel("Followers:");
		lblFollowersTitle.setBounds(5, 116, 70, 14);
		panelStatistics.add(lblFollowersTitle);

		JLabel lblFollowersValue = new JLabel("0");
		lblFollowersValue.setBounds(70, 116, 46, 14);
		panelStatistics.add(lblFollowersValue);

		JLabel lblFollowingTitle = new JLabel("Following:");
		lblFollowingTitle.setBounds(5, 136, 70, 14);
		panelStatistics.add(lblFollowingTitle);

		JLabel lblFollowingValue = new JLabel("0");
		lblFollowingValue.setBounds(70, 136, 46, 14);
		panelStatistics.add(lblFollowingValue);

		JLabel lblTweetCount = new JLabel();
		lblTweetCount.setBounds(5, 30, 149, 134);
		panelStatistics.add(lblTweetCount);

		final JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnRefresh.setEnabled(true);
				refresh_timer = 5;
				new SwingWorker<Object, Object>(){
					@Override
					protected Object doInBackground() {
						try {

							panel_Timeline.remove(hometimeline);
							panel_Timeline.remove(usertimeline);
							
							lblStatisticsBackground.setIcon(new ImageIcon(getClass().getResource(
									"/images/Statistics_panel_new.png")));
							Timelines.getTimeLine();
							TimelineGUI();
							Timelines.getUserTimeLine();
							
							panel_Timeline.updateUI();
							panel_Statistics.updateUI();

						} catch (MalformedURLException e) {
							e.printStackTrace();
						} catch (TwitterException e) {
							e.printStackTrace();
						}

						disableRefreshButton(btnRefresh);
						SoundController.playPopSound();
						
						return null;
					}
				}.execute();
			}
		});
		btnRefresh.setUI(new BEButtonUI()
		.setNormalColor(BEButtonUI.NormalColor.normal));
		btnRefresh.setBounds(4, 160, 200, 23);
		contentPane.add(btnRefresh);

		JPanel panelTrending = new JPanel();
		panelTrending.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		panelTrending.setForeground(new Color(255, 255, 255));
		panelTrending.setOpaque(false);
		panelTrending.setFont(new Font("Segoe UI", Font.BOLD, 12));
		panelTrending.setBackground(new Color(255, 255, 255));
		panelTrending.setBounds(33, 219, 175, 278);
		contentPane.add(panelTrending);

		JButton btnLogout = new JButton("Logout");
		btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				logoutButton(evt);
			}
		});

		btnLogout.setForeground(new Color(255, 255, 255));
		btnLogout.setBackground(new Color(255, 255, 255));
		btnLogout.setBounds(6, 556, 199, 23);
		btnLogout.setUI(new BEButtonUI()
		.setNormalColor(BEButtonUI.NormalColor.red));
		contentPane.add(btnLogout);

		panel_Timeline.setBounds(215, 55, 836, 524);
		contentPane.add(panel_Timeline);

		trends.setFont(new Font("SansSerif", Font.PLAIN, 11));
		panelTrending.add(trends);
		panelTrending.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 5));

		txtPostATweet = new JTextField(140);
		txtPostATweet.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				txtPostATweet.setText("");
			}
		});
		txtPostATweet.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtPostATweet.setText(POST_MESSAGE);
		txtPostATweet.setBounds(215, 29, 740, 23);
		txtPostATweet.setColumns(10);
		contentPane.add(txtPostATweet);

		JButton btnPost = new JButton("Post");
		btnPost.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Posting();
			}
		});
		btnPost.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnPost.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnPost.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
		btnPost.setBounds(965, 30, 80, 23);
		contentPane.add(btnPost);

		panelTrending = new JPanel();
		panelTrending.setBackground(new Color(102, 204, 255));
		panelTrending.setBounds(0, 194, 208, 303);
		contentPane.add(panelTrending);
		panelTrending.setLayout(null);

		JLabel lblTrendingBg = new JLabel();
		lblTrendingBg.setBounds(0, 0, 208, 303);
		lblTrendingBg.setIcon(new ImageIcon(getClass().getResource(
				"/images/Trending_panel.png")));
		panelTrending.add(lblTrendingBg);

		BtnToggleTimeline = new JButton("Switch Timeline");
		BtnToggleTimeline.setCursor(Cursor
				.getPredefinedCursor(Cursor.HAND_CURSOR));
		BtnToggleTimeline.setBorder(null);
		BtnToggleTimeline.setBackground(Color.WHITE);
		BtnToggleTimeline.setForeground(new Color(255, 255, 255));
		BtnToggleTimeline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CardLayout c1 = (CardLayout) (panel_Timeline.getLayout());
				c1.next(panel_Timeline);
				if (CardCounter % 2 == 0) {
					lblStatisticsBackground.setIcon(new ImageIcon(getClass().getResource(
							"/images/Statistics_panel_friends.png")));
				} else {
					lblStatisticsBackground.setIcon(new ImageIcon(getClass().getResource(
							"/images/Statistics_panel_new.png")));
				}
				CardCounter++;
				disableButton(BtnToggleTimeline);
			}
		});
		BtnToggleTimeline.setBounds(6, 504, 199, 23);
		BtnToggleTimeline.setUI(new BEButtonUI()
		.setNormalColor(BEButtonUI.NormalColor.green));
		contentPane.add(BtnToggleTimeline);

		JButton btnGlobalView = new JButton("Globe View");
		btnGlobalView.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnGlobalView.setBorder(null);
		btnGlobalView.setForeground(new Color(255, 255, 255));
		btnGlobalView.setUI(new BEButtonUI()
		.setNormalColor(BEButtonUI.NormalColor.lightBlue));
		btnGlobalView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Map().setVisible(true);
			}
		});
		btnGlobalView.setBounds(6, 531, 199, 23);
		contentPane.add(btnGlobalView);

		JLabel lblLeftBg = new JLabel("");
		lblLeftBg.setBounds(0, 0, 208, 592);
		lblLeftBg.setOpaque(true);
		lblLeftBg.setBackground(new Color(0x0099c5));
		contentPane.add(lblLeftBg);

		JLabel lblRightBg = new JLabel("");
		lblRightBg.setBounds(208, 0, 849, 593);
		lblRightBg.setIcon(new ImageIcon(getClass().getResource("/images/BG.png")));
		contentPane.add(lblRightBg);

		Splash.frame.dispose();
		SoundController.playLoginSound();
		setVisible(true);
	}

	/*
	 * This method prevents users from spamming buttons
	 */

	private static void disableButton(final JButton b) {
		((Component) b).setEnabled(false);
		new SwingWorker<Object, Object>() {
			@Override
			protected Object doInBackground() throws Exception {
				Thread.sleep(500);
				return null;
			}
			@Override
			protected void done() {
				 b.setEnabled(true);
			}
		}.execute();
	}

	/*
	 * This method sets the timer for the Refresh button to avoid exceeding
	 * API call count.
	 */
	private static void disableRefreshButton(final JButton b) {
		b.setEnabled(false);

		new SwingWorker<Object, Object>() {
			@Override protected Object doInBackground() throws Exception {
				int refresh_timer = 120;
				while(refresh_timer != 0){
					b.setText("Refreshing Enabled in: " + refresh_timer);
					Thread.sleep(1000);
					refresh_timer--;
				}
				b.setEnabled(true);
				b.setText("Refresh");
				return null;
			}
			@Override protected void done() {
				((Component) b).setEnabled(true);
			}
		}.execute();
	}
	
	protected void logoutButton(ActionEvent evt) {
		this.dispose();
		new LoginGUI().setVisible(true);
	}


	protected static void setTrendingList(JList<String> trendingList) {
		trends = trendingList;

	}

	private static void setHomeTimeLine(JScrollPane scrollPane) {
		hometimeline = scrollPane;

	}

	protected static void setUserTimeLine(JScrollPane scrollPane2) {
		usertimeline = scrollPane2;

	}
}
