package com.heal.dominic.Login;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import com.alee.laf.button.WebButton;
import com.alee.laf.button.WebButtonUI;
import com.heal.dominic.MainInterface.Application;
import com.heal.dominic.ResourceManager.ImageController;
import com.heal.dominic.ResourceManager.SoundController;
import com.heal.dominic.Splash.Splash;

public class LoginGUI extends JFrame {
	private static final long serialVersionUID = -6436102872206398435L;
	
	protected static boolean Access;
	protected static JButton btnLogin;
	protected static String Error, Name, token, tokenSecret, Username;
	protected static int FavouriteCnt, FollowersCnt, FollowingCnt;
	protected static JLabel lblVerify;
	protected static URL MiniProfilePic, ProfilePic;
	private static JTextField Pintxtbox;
	public static RequestToken requestToken;
	protected static int TweetCnt;
	protected static Twitter twitter;
	protected static Thread x = new Splash();
	public static Thread y = new Thread(new Application());
	public static Thread thread_UserTimeline;
	public static Thread thread_Timeline;
	public static Thread thread_TimelineOther;

	/**
	 * Creates new form TwitterAppGui
	 */
	public AccessToken accessToken;
	private JLabel BackgroundLbl, PinLbl;

	public LoginGUI() {
		setUndecorated(true);
		setTitle("MyTweetHub - Making Twitter Simple");
		setIconImage(new ImageIcon(getClass().getResource("/images/tweethub_icon.png")).getImage());
		initComponents();
		setLocationRelativeTo(null);
	}

	/**
	 * Drawing GUI
	 */
	public void initComponents() {


		btnLogin = new JButton();
		btnLogin.setEnabled(true);
		btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		Pintxtbox = new JTextField(5);
		Pintxtbox.setHorizontalAlignment(SwingConstants.CENTER);
		Pintxtbox.setBorder(new LineBorder(new Color(171, 173, 179)));
		BackgroundLbl = new JLabel();
		
		PinLbl = new JLabel();
		PinLbl.setForeground(new Color(255, 255, 255));
		PinLbl.setText("Enter PIN:");
		PinLbl.setBounds(140, 270, 70, 14);

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(new Rectangle(0, 0, 0, 0));
		setMinimumSize(new Dimension(330, 450));
		setResizable(false);
		getContentPane().setLayout(null);
		getContentPane().add(PinLbl);

		btnLogin.setText("Check");
		btnLogin.setUI(new BEButtonUI()
		.setNormalColor(BEButtonUI.NormalColor.normal));
		btnLogin.setFocusPainted(false);
		btnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent evt) {
				try {
					LoginBtnActionPerformed(evt);
				} catch (final Exception e) {
					System.out.println(e);
				}

			}
		});
		getContentPane().add(btnLogin);
		btnLogin.setBounds(120, 340, 90, 20);
		getContentPane().add(Pintxtbox);
		Pintxtbox.setBounds(120, 300, 90, 20);

		JButton btnClose = new JButton(ImageController.Image_Close);
		btnClose.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				System.exit(0);
			}
		});
		btnClose.setFocusPainted(false);
		btnClose.setBorderPainted(false);
		btnClose.setBorder(BorderFactory.createEmptyBorder());
		btnClose.setContentAreaFilled(false);
		btnClose.setSize(16,16);
		btnClose.setBounds(305, 0, 25, 25);
		((WebButtonUI)btnClose.getUI()).setUndecorated(true);
		getContentPane().add(btnClose);
		
		lblVerify = new JLabel();
		lblVerify.setBounds(227, 303, 46, 14);
		lblVerify.setIcon(new ImageIcon(getClass().getResource("/images/OK.png")));
		lblVerify.setVisible(false);
		getContentPane().add(lblVerify);

		BackgroundLbl.setIcon(new ImageIcon(getClass().getResource("/images/Twitte2r.png")));
		BackgroundLbl.setText("");
		getContentPane().add(BackgroundLbl);
		BackgroundLbl.setBounds(0, 0, 330, 450);

		pack();
	}
	public void login() throws IOException {
		final ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey("KxXwgeXCdJyUpVJNDPFsxdube")
		.setOAuthConsumerSecret("XNbXtgxMcFArRo0Hej0nmduGaroU21PRbQbiW76rqA6nHGsmB0");

		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new LoginGUI().setVisible(true);
			}
		});

		try {
			final TwitterFactory tf = new TwitterFactory(cb.build());
			twitter = tf.getInstance();

			try {
				AccessToken accessToken = null;
				final RequestToken requestToken = twitter.getOAuthRequestToken();
				/* Opens up the URL in default browser set be user */
				try {
					Desktop.getDesktop().browse(new URL(requestToken.getAuthorizationURL()).toURI());
				} catch (final Exception e) {
				}

				while (accessToken == null) {
					if (Access == true) {
						String pin = getPintxtbox().getText();

						try {
							if (pin.length() > 0) {
								accessToken = twitter.getOAuthAccessToken(requestToken, pin);
							} else {
								accessToken = twitter.getOAuthAccessToken(requestToken);
							}
						} catch (final TwitterException te) {
							Access = false;
						}
					}
				}
				btnLogin.setText("Login");

				storeAccessToken(twitter.verifyCredentials().getId(),accessToken);
				System.out.println("Got access token.");
				System.out.println("Access token: "	+ accessToken.getToken());
				System.out.println("Access token secret: " + accessToken.getTokenSecret());

			} catch (final IllegalStateException ie) {
				// access token is already available, or consumer key/secret
				// is not set.
				if (!twitter.getAuthorization().isEnabled()) {
					System.out
					.println("OAuth consumer key/secret is not set.");
					System.exit(-1);
				}
			}
			
			final User user = twitter.showUser(twitter.getId());
			setTwitter(twitter);
			setUsername(twitter.getScreenName());
			setFavouriteCnt(user.getFavouritesCount());
			setFollowersCnt(user.getFollowersCount());
			setFollowingCnt(user.getFriendsCount());
			setProfilePic(new URL(user.getProfileImageURL()));
			setMiniProfilePic(new URL(user.getMiniProfileImageURL()));
			setTweetCount(user.getStatusesCount());
			setName(user.getName());
			setAccess(true);
			
			lblVerify.setVisible(true);
			SoundController.playTickSound();

		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed: " + te.getMessage());
			System.exit(-1);
		}

	}

	private void LoginBtnActionPerformed(final ActionEvent evt)	throws Exception {
		if (btnLogin.getText() == "Check") {
			Access = true;
		} else if (btnLogin.getText() == "Login") {
			this.setVisible(false);

	        //x.start();

			final Application x = new Application();
		    y.start();

			Thread thread_UserTimeline = new Thread(x.userTimeLine());
			Thread thread_Timeline = new Thread(x.TimeLine());
			thread_UserTimeline.start();
			thread_Timeline.start();
	        Thread thread_Timelineg = new Thread(x.TimelineGUI());
	        thread_Timelineg.start();

			Access = false;
		}
	}

	private void setAccess(final boolean i) {
		Access = i;
	}
	
	@Override
	public void setName(String name) {
		Name = name;
	}
	public void setPinLbl(final JLabel pinLbl) {
		PinLbl = pinLbl;
	}

	public static boolean getAccess() {
		return Access;
	}

	public static int getFollowerCnt() {
		return FollowersCnt;
	}

	public static int getFollowersCnt() {
		return FavouriteCnt;
	}

	public static int getFollowingCnt() {
		return FollowingCnt;
	}

	public static JTextField getPintxtbox() {
		return Pintxtbox;
	}

	public static URL getProfilePic() {
		return ProfilePic;
	}

	public static int getTweetCnt() {
		return TweetCnt;
	}

	public static Twitter getTwitter() {
		return twitter;
	}

	public static String getUsername() {
		return Username;
	}

	public static AccessToken loadAccessToken(long useId) {
		return new AccessToken(token, tokenSecret);
	}

	public static void setFavouriteCnt(int favouriteCnt) {
		FavouriteCnt = favouriteCnt;
	}

	public static void setFollowersCnt(int followersCnt) {
		FollowersCnt = followersCnt;
	}

	public static void setFollowingCnt(int followingCnt) {
		FollowingCnt = followingCnt;
	}

	public static void setMiniProfilePic(URL string) {
		MiniProfilePic = string;
	}

	public static void setPintxtbox(JTextField pintxtbox) {
		Pintxtbox = pintxtbox;
	}

	public static void setProfilePic(URL string) {
		ProfilePic = string;
	}

	private static void setTweetCount(int statusesCount) {
		TweetCnt = statusesCount;
	}

	private static void setTwitter(final Twitter twitter) {
		LoginGUI.twitter = twitter;
	}


	/*
	 * This method is use to configure the Access Tokens for each user.
	 */	

	public static void setUsername(String username) {
		Username = username;
	}

	public static void storeAccessToken(long useId, final AccessToken accessToken) {
		token = accessToken.getToken();
		tokenSecret = accessToken.getTokenSecret();
	}

	@Override
	public String getName() {
		return Name;
	}

	public JLabel getPinLbl() {
		return PinLbl;
	}
}