package com.mytweethubapplication.Login;

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

import com.mytweethubapplication.MainInterface.Application;
import com.mytweethubapplication.ResourceManager.ImageController;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import com.alee.laf.button.WebButtonUI;
import com.mytweethubapplication.ResourceManager.SoundController;
import com.mytweethubapplication.Splash.Splash;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginGUI extends JFrame {
    private static final long serialVersionUID = -6436102872206398435L;

    private static JButton btnLogin;
    protected static String token, tokenSecret;
    private static JLabel lblVerify;
    public static JTextField textboxPin;
    public static RequestToken requestToken;
    protected static Twitter twitter;
    public Thread splashScreenThread;
    public AccessToken accessToken;
    private JLabel PinLbl;

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

        textboxPin = new JTextField(5);
        textboxPin.setHorizontalAlignment(SwingConstants.CENTER);
        textboxPin.setBounds(120, 300, 90, 20);
        getContentPane().add(textboxPin);

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
        btnClose.setSize(16, 16);
        btnClose.setBounds(305, 0, 25, 25);
        ((WebButtonUI) btnClose.getUI()).setUndecorated(true);
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

        JLabel lblBackground = new JLabel();
        lblBackground.setIcon(new ImageIcon(getClass().getResource("/images/Twitte2r.png")));
        lblBackground.setText("");
        lblBackground.setBounds(0, 0, 330, 450);
        getContentPane().add(lblBackground);
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

            accessToken = null;
            requestToken = twitter.getOAuthRequestToken();
                /* Opens up the URL in default browser set be user */
            try {
                lblBrowser.setVisible(true);
                Desktop.getDesktop().browse(new URL(requestToken.getAuthorizationURL()).toURI());
            } catch (final Exception e) {
                //TO-DO
            }
        } catch (TwitterException te) {
            te.printStackTrace();
        }
    }

    private void storeUserDetails() {
        final User user;
        try {
            user = twitter.showUser(twitter.getId());

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

        } catch (TwitterException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        lblVerify.setVisible(true);
        SoundController.playTickSound();
    }

    private void printDeveloperTests(String token, String tokenSecret) {
        System.out.println("Access token: " + token);
        System.out.println("Access token secret: " + tokenSecret);

    }

    private void loginButtonPressed(final ActionEvent evt) throws Exception {
        if (accessToken == null) {
            try {
                String pin = getTextboxPin().getText();
                accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                btnLogin.setText("Login");

                storeAccessToken(twitter.verifyCredentials().getId(), accessToken);
                printDeveloperTests(accessToken.getToken(), accessToken.getTokenSecret());
                storeUserDetails();
            } catch (final TwitterException te) {
                JOptionPane.showMessageDialog(null, te);
            }
        } else {
            this.setVisible(false);

            splashScreenThread = new Thread(new Splash());
            splashScreenThread.start();

            Thread thread_mainscreen = new Thread(new Application());
            thread_mainscreen.start();
        }
    }

    public static Twitter getTwitter() {
        return twitter;
    }

    public static JTextField getTextboxPin() {
        return textboxPin;
    }

    public static void storeAccessToken(long useId, final AccessToken accessToken) {
        token = accessToken.getToken();
        tokenSecret = accessToken.getTokenSecret();
    }
}