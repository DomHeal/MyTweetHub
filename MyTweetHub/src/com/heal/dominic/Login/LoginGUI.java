package com.heal.dominic.Login;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import com.alee.laf.button.WebButtonUI;
import com.heal.dominic.MainInterface.Application;
import com.heal.dominic.ResourceManager.ImageController;
import com.heal.dominic.ResourceManager.SoundController;
import com.heal.dominic.Splash.Splash;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginGUI extends JFrame {
	private static final long serialVersionUID = -6436102872206398435L;
	
	protected static boolean buttonAccess;
	protected static JButton btnLogin;
	protected static String Error, Name, token, tokenSecret, Username;
	protected static int FavouriteCnt, FollowersCnt, FollowingCnt;
	protected static JLabel lblVerify;
	protected static URL MiniProfilePic, ProfilePic;
	public static JTextField Pintxtbox;
	public static RequestToken requestToken;
	protected static int TweetCnt;
	protected static Twitter twitter;
	public Thread thread_splashscreen;
	public AccessToken accessToken;
	private JLabel BackgroundLbl, PinLbl;

	private static JLabel lblBrowser;

	public LoginGUI() {
		setUndecorated(true);
		setTitle("MyTweetHub - Making Twitter Simple");
		setIconImage(new ImageIcon(getClass().getResource("/images/tweethub_icon.png")).getImage());
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(new Rectangle(0, 0, 0, 0));
		setMinimumSize(new Dimension(330, 450));
		setResizable(false);
		getContentPane().setLayout(null);
		initComponents();
		setLocationRelativeTo(null);
		pack();
	}

	/**
	 * Drawing GUI
	 */
	public void initComponents() {

		Pintxtbox = new JTextField(5);
		Pintxtbox.setHorizontalAlignment(SwingConstants.CENTER);
		Pintxtbox.setBounds(120, 300, 90, 20);
		getContentPane().add(Pintxtbox);

		PinLbl = new JLabel();
		PinLbl.setForeground(new Color(255, 255, 255));
		PinLbl.setText("Enter PIN:");
		PinLbl.setBounds(140, 270, 70, 14);
		getContentPane().add(PinLbl);

		btnLogin = new JButton();
		btnLogin.setEnabled(true);
		btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnLogin.setText("Check");
		btnLogin.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
		btnLogin.setFocusPainted(false);
		btnLogin.setBounds(120, 340, 90, 20);
		btnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent evt) {
				try {
					loginButtonPressed(evt);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		getContentPane().add(btnLogin);

		JButton btnClose = new JButton(ImageController.Image_Close);
		btnClose.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnClose.setFocusPainted(false);
		btnClose.setBorderPainted(false);
		btnClose.setBorder(BorderFactory.createEmptyBorder());
		btnClose.setContentAreaFilled(false);
		btnClose.setSize(16,16);
		btnClose.setBounds(305, 0, 25, 25);
		((WebButtonUI)btnClose.getUI()).setUndecorated(true);
		btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				System.exit(0);
			}
		});
		getContentPane().add(btnClose);
		
		lblVerify = new JLabel();
		lblVerify.setBounds(227, 303, 46, 14);
		lblVerify.setIcon(new ImageIcon(getClass().getResource("/images/OK.png")));
		lblVerify.setVisible(false);
		getContentPane().add(lblVerify);
		
		lblBrowser = new JLabel();
		lblBrowser.setIcon(new ImageIcon(getClass().getResource("/images/copylink.png")));
		lblBrowser.setToolTipText("Copy Auth Link");
		lblBrowser.setVisible(false);
		lblBrowser.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblBrowser.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mclicked) {
				String myString = requestToken.getAuthorizationURL().toString();
				StringSelection stringSelection = new StringSelection(myString);
				Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
				clpbrd.setContents(stringSelection, null);
			}
		});
		lblBrowser.setBounds(99, 303, 16, 14);
		getContentPane().add(lblBrowser);

		BackgroundLbl = new JLabel();
		BackgroundLbl.setIcon(new ImageIcon(getClass().getResource("/images/Twitte2r.png")));
		BackgroundLbl.setText("");
		BackgroundLbl.setBounds(0, 0, 330, 450);
		getContentPane().add(BackgroundLbl);
	}
	
	public void login() {
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
				requestToken = twitter.getOAuthRequestToken();
				/* Opens up the URL in default browser set be user */
				try {
					lblBrowser.setVisible(true);
					Desktop.getDesktop().browse(new URL(requestToken.getAuthorizationURL()).toURI());
				} catch (final Exception e) {
				}

				while (accessToken == null) {
					if (buttonAccess == true) {
						String pin = getPintxtbox().getText();
						try{
							accessToken = twitter.getOAuthAccessToken(requestToken, pin);
						} catch (final TwitterException te) {
							buttonAccess = false;
							JOptionPane.showMessageDialog(null,	te);
						}
					}
				}
				btnLogin.setText("Login");

				storeAccessToken(twitter.verifyCredentials().getId(),accessToken);
				printDeveloperTests(accessToken.getToken(), accessToken.getTokenSecret());

			} catch (final IllegalStateException ie) {
				// access token is already available, or consumer key/secret
				// is not set.
				if (!twitter.getAuthorization().isEnabled()) {
					System.out.println("OAuth consumer key/secret is not set.");
				}
			}
			
			final User user = twitter.showUser(twitter.getId());
			
			UserData storeuser = new UserData();
			storeuser.setTwitter(twitter);
			storeuser.setName(user.getName());
			storeuser.setUsername(twitter.getScreenName());
			storeuser.setFavouriteCnt(user.getFavouritesCount());
			storeuser.setFollowersCnt(user.getFollowersCount());
			storeuser.setFollowingCnt(user.getFriendsCount());
			storeuser.setProfilePic(new URL(user.getProfileImageURL()));
			storeuser.setMiniProfilePic(new URL(user.getMiniProfileImageURL()));
			storeuser.setTweetCount(user.getStatusesCount());
			
			setAccess(true);
			
			lblVerify.setVisible(true);
			SoundController.playTickSound();

		} catch (TwitterException te) {
			te.printStackTrace();;
			System.exit(-1);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void printDeveloperTests(String token, String tokenSecret) {
		System.out.println("Access token: "	+ token);
		System.out.println("Access token secret: " + tokenSecret);
		
	}

	private void loginButtonPressed(final ActionEvent evt)	throws Exception {
		if (btnLogin.getText() == "Check") {
			buttonAccess = true;
		} else if (btnLogin.getText() == "Login") {
			this.setVisible(false);

			thread_splashscreen = new Thread(new Splash());
			thread_splashscreen.start();
		    
			Thread thread_mainscreen = new Thread(new Application());
			thread_mainscreen.start();

			buttonAccess = false;
		}
	}

	private void setAccess(final boolean i) {
		buttonAccess = i;
	}
	
	public void setPinLbl(final JLabel pinLbl) {
		PinLbl = pinLbl;
	}

	public static boolean getAccess() {
		return buttonAccess;
	}


	public static Twitter getTwitter() {
		return twitter;
	}


	public static AccessToken loadAccessToken(long useId) {
		return new AccessToken(token, tokenSecret);
	}

	public static void setPintxtbox(JTextField pintxtbox) {
		Pintxtbox = pintxtbox;
	}
	
	public static JTextField getPintxtbox() {
		return Pintxtbox;
	}

	/*
	 * This method is use to configure the Access Tokens for each user.
	 */	

	public static void storeAccessToken(long useId, final AccessToken accessToken) {
		token = accessToken.getToken();
		tokenSecret = accessToken.getTokenSecret();
	}

	public JLabel getPinLbl() {
		return PinLbl;
	}
}