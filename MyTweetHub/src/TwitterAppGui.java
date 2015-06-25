import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UnsupportedLookAndFeelException;
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

/**
 * @author Dominic
 */

public class TwitterAppGui extends JFrame {

	public TwitterAppGui() {
		setUndecorated(true);
		setTitle("MyTweetHub - Making Twitter Simple");
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				Application.class.getResource("twitter47.png")));
		initComponents();
		setLocationRelativeTo(null);
	}

	private static JTextField Pintxtbox;

	private static String tokenSecret;

	protected static int Access;

	protected static String Error;

	protected static int FavouriteCnt;

	protected static int FollowersCnt;

	protected static int FollowingCnt;

	protected static URL ProfilePic;

	protected static URL MiniProfilePic;

	protected static String token;

	protected static int TweetCnt;

	protected static Twitter twitter2;

	protected static String Username;

	protected static String Name;

	static JLabel lblVerify;

	static JButton btnLogin;

	static RequestToken requestToken;

	public static int getAccess() {
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

	public static String getUsername() {
		return Username;
	}

	@Override
	public String getName() {
		return Name;
	}

	public static AccessToken loadAccessToken(long useId) {
		return new AccessToken(token, tokenSecret);
	}

	public static void main(final String args[]) throws IOException {
		new TwitterAppGui().login();

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

	public static void setPintxtbox(JTextField pintxtbox) {
		Pintxtbox = pintxtbox;
	}

	public static void setProfilePic(URL string) {
		ProfilePic = string;
	}

	public static void setMiniProfilePic(URL string) {
		MiniProfilePic = string;
	}

	public static void setUsername(String username) {
		Username = username;
	}

	@Override
	public void setName(String name) {
		Name = name;
	}

	public static void storeAccessToken(long useId,
			final AccessToken accessToken) {
		token = accessToken.getToken();
		tokenSecret = accessToken.getTokenSecret();
	}

	private static void setTweetCount(int statusesCount) {
		TweetCnt = statusesCount;

	}

	private JLabel BackgroundLbl;
	private static Twitter twitter;

	private JLabel PinLbl;

	/**
	 * Creates new form TwitterAppGui
	 */
	AccessToken accessToken;

	static Thread x = new Splash();

	static Thread y = new Thread(new Application());

	public JLabel getPinLbl() {
		return PinLbl;
	}

	/**
	 * Drawing GUI
	 */

	public void initComponents() {

		PinLbl = new JLabel();
		btnLogin = new JButton();
		btnLogin.setEnabled(true);
		btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		Pintxtbox = new JTextField(5);
		Pintxtbox.setHorizontalAlignment(SwingConstants.CENTER);
		Pintxtbox.setBorder(new LineBorder(new Color(171, 173, 179)));
		BackgroundLbl = new JLabel();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(new Rectangle(0, 0, 0, 0));
		setMinimumSize(new Dimension(330, 450));
		setResizable(false);
		getContentPane().setLayout(null);
		PinLbl.setForeground(new Color(255, 255, 255));
		PinLbl.setText("Enter PIN:");
		getContentPane().add(PinLbl);
		PinLbl.setBounds(140, 270, 70, 14);

		btnLogin.setText("Check");
		btnLogin.setUI(new BEButtonUI()
				.setNormalColor(BEButtonUI.NormalColor.normal));
		btnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent evt) {
				try {
					LoginBtnActionPerformed(evt);
				} catch (final Exception e) {
					// do Nothing
				}

			}
		});
		getContentPane().add(btnLogin);
		btnLogin.setBounds(120, 340, 90, 20);
		getContentPane().add(Pintxtbox);
		Pintxtbox.setBounds(120, 300, 90, 20);

		final JButton btnClose = new JButton(new ImageIcon(getClass()
				.getResource(("close_button2.png"))));
		btnClose.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				System.exit(0);
			}
		});
		btnClose.setFocusPainted(false);
		btnClose.setBorderPainted(false);
		btnClose.setBounds(305, 0, 25, 25);
		btnClose.setBorder(BorderFactory.createEmptyBorder());
		btnClose.setContentAreaFilled(false);
		getContentPane().add(btnClose);

		lblVerify = new JLabel();
		lblVerify.setBounds(227, 303, 46, 14);
		lblVerify.setIcon(new ImageIcon(getClass().getResource("/OK.png")));
		lblVerify.setVisible(false);
		getContentPane().add(lblVerify);

		BackgroundLbl.setIcon(new ImageIcon(getClass().getResource(
				"/Twitte2r.png")));
		BackgroundLbl.setText("");
		getContentPane().add(BackgroundLbl);
		BackgroundLbl.setBounds(0, 0, 330, 450);

		pack();
	}

	public void setPinLbl(final JLabel pinLbl) {
		PinLbl = pinLbl;
	}

	private TwitterFactory login() throws IOException {
		{
			/* Credentials */
			final ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setDebugEnabled(true)
					.setOAuthConsumerKey("KxXwgeXCdJyUpVJNDPFsxdube")
					.setOAuthConsumerSecret(
							"XNbXtgxMcFArRo0Hej0nmduGaroU21PRbQbiW76rqA6nHGsmB0");

			java.awt.EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					new TwitterAppGui().setVisible(true);
				}
			});

			try {
				final TwitterFactory tf = new TwitterFactory(cb.build());
				twitter = tf.getInstance();

				try {
					AccessToken accessToken = null;
					final RequestToken requestToken = twitter
							.getOAuthRequestToken();
					/* Opens up the URL in default browser set be user */
					try {
						Desktop.getDesktop().browse(
								new URL(requestToken.getAuthorizationURL())
										.toURI());
					} catch (final Exception e) {
					}

					while (accessToken == null) {
						if (Access == 1) {

							String pin;
							pin = getPintxtbox().getText();

							try {
								if (pin.length() > 0) {
									accessToken = twitter.getOAuthAccessToken(
											requestToken, pin);
								} else {
									accessToken = twitter
											.getOAuthAccessToken(requestToken);
								}
							} catch (final TwitterException te) {
								Access = 0;

							}

						}
					}
					btnLogin.setText("Login");

					storeAccessToken(twitter.verifyCredentials().getId(),
							accessToken);
					System.out.println("|-----");
					System.out.println("Got access token.");
					System.out.println("Access token: "
							+ accessToken.getToken());
					System.out.println("Access token secret: "
							+ accessToken.getTokenSecret());
					System.out.println("|-----");

				} catch (final IllegalStateException ie) {
					// access token is already available, or consumer key/secret
					// is not set.
					if (!twitter.getAuthorization().isEnabled()) {
						System.out
								.println("OAuth consumer key/secret is not set.");
						System.exit(-1);
					}
				}
				setTwitter2(twitter);
				final User user = twitter.showUser(twitter.getId());
				setUsername(twitter.getScreenName());
				setFavouriteCnt(user.getFavouritesCount());
				setFollowersCnt(user.getFollowersCount());
				setFollowingCnt(user.getFriendsCount());
				setProfilePic(new URL(user.getProfileImageURL()));
				setMiniProfilePic(new URL(user.getMiniProfileImageURL()));
				setTweetCount(user.getStatusesCount());
				setName(user.getName());

				setAccess(1);
				lblVerify.setVisible(true);
				AudioInputStream audioInputStream;
				try {
					audioInputStream = AudioSystem
							.getAudioInputStream(getClass().getResource(
									"/blip1.wav"));
					final Clip clip = AudioSystem.getClip();
					clip.open(audioInputStream);
					clip.start();
					} catch (Exception e){
						// Do Nothing
					}

			} catch (TwitterException te) {
				te.printStackTrace();
				System.out
						.println("Failed to get timeline: " + te.getMessage());
				System.exit(-1);
			}

		}
		return null;
	}

	static Twitter getTwitter2() {
		return twitter2;
	}

	private static void setTwitter2(final Twitter twitter2) {
		TwitterAppGui.twitter2 = twitter2;
	}

	private void LoginBtnActionPerformed(final ActionEvent evt)
			throws Exception {
		if (btnLogin.getText() == "Check") {
			Access = 1;
		} else if (btnLogin.getText() == "Login") {
			this.setVisible(false);
			x.start();
			y.start();

			Access = 0;
		}

	}

	private void setAccess(final int i) {
		Access = i;

	}
}
