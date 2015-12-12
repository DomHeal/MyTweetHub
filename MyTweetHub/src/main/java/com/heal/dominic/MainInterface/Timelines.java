package com.heal.dominic.MainInterface;

import java.awt.Font;
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

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.heal.dominic.Login.UserData;
import com.heal.dominic.ResourceManager.SoundController;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.TwitterException;

public class Timelines extends Application {

	private static final long serialVersionUID = 2636386799000006042L;

	public static void getTimeLine() throws IllegalStateException, MalformedURLException{

		twitter = getTwitter();

		/* Creates and Displays Public Time */
		try {
			statusList = twitter.getHomeTimeline(new Paging().count(200));

			final String statusArr[] = new String[statusList.size()];
			final URL[] profileimages = new URL[statusList.size()];

			for (int i = 0; i < statusList.size(); i++) {

			/* If its Retweeted, Change text colour to GREEN */
				if (statusList.get(i).isRetweetedByMe()) {
					statusArr[i] = "<html><font color=green><b>"
							+ statusList.get(i).getUser().getName() + ": " + "</b>"
							+ statusList.get(i).getText() + "</font>  </html>";
					profileimages[i] = new URL(statusList.get(i).getUser()
							.getMiniProfileImageURL());
					dlm.addElement(new ListEntry(statusArr[i], new ImageIcon(
							profileimages[i])));
				}
			/* If its Favourited, Change text colour to ORANGE */
				else if (statusList.get(i).isFavorited()) {
					statusArr[i] = "<html><font color=orange><b>"
							+ statusList.get(i).getUser().getName() + ": " + "</b>"
							+ statusList.get(i).getText() + "</font>  </html>";
					profileimages[i] = new URL(statusList.get(i).getUser()
							.getMiniProfileImageURL());
					dlm.addElement(new ListEntry(statusArr[i], new ImageIcon(
							profileimages[i])));
				}
			/* If its Neither, Change text colour to BLACK */
				else {
					statusArr[i] = "<html><font color=black><b>"
							+ statusList.get(i).getUser().getName() + ": " + "</b>"
							+ statusList.get(i).getText() + "</font>  </html>";
					profileimages[i] = new URL(statusList.get(i).getUser()
							.getMiniProfileImageURL());
					dlm.addElement(new ListEntry(statusArr[i], new ImageIcon(
							profileimages[i])));

				}
			}
		} catch (TwitterException e) {
			JOptionPane.showMessageDialog(null,	"Too many requests! Try again in " + e.getRateLimitStatus().getSecondsUntilReset());
		}
	}

	public static void getUserTimeLine() throws IllegalStateException{
		twitter = getTwitter();

		try {
			final List<Status> statusList2 = twitter.getUserTimeline(new Paging().count(200));
			String statusAr[] = new String[statusList2.size()];
			final DefaultListModel<ListEntry> dlm2 = new DefaultListModel<ListEntry>();
			for (int i = 0; i < statusList2.size(); i++) {
				statusAr[i] = "<html><b>" + statusList2.get(i).getUser().getName()
						+ ":</b> " + "" + statusList2.get(i).getText() + "</html>";
				dlm2.addElement(new ListEntry(statusAr[i], new ImageIcon(
						UserData.getMiniProfilePic())));
			}
			final JList<ListEntry> userStatusJList = new JList<ListEntry>(dlm2);
			userStatusJList.addMouseMotionListener(new MouseAdapter() {
				public void mouseMoved(MouseEvent me) {
					Point p = new Point(me.getX(), me.getY());
					final SimpleDateFormat formatter = new SimpleDateFormat(
							"dd/MM/yy HH:mm");
					userStatusJList.setSelectedIndex(userStatusJList
							.locationToIndex(p));

					int index = userStatusJList.locationToIndex(p);
					if (index > -1) {
						userStatusJList.setToolTipText(null);
						Date usertweetDate = statusList2.get(
								userStatusJList.locationToIndex(p)).getCreatedAt();
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
			new PopUp().addPopup(userStatusJList, popupMenu);

		/* Listener for Popup Menu and Get Selected row */
			userStatusJList.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					if (SwingUtilities.isRightMouseButton(e)) {
						row = userStatusJList.locationToIndex(e.getPoint());
						userStatusJList.setSelectedIndex(row);
					}
				}

			});
			mntmGetInfo = new JMenuItem("Get Info");
			mntmGetInfo.setIcon(new ImageIcon(Application.class
					.getResource("/images/Note-Add_hover.png")));
			popupMenu.add(mntmGetInfo);
			mntmGetInfo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						new Info(statusList2.get(row));
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
						if (mntmGetInfo.isSelected() || mntmDelete.isArmed()) {
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

			mntmDelete = new JMenuItem("Delete");
			mntmDelete.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					long tweetid = statusList2.get(row).getId();

					try {
						twitter.destroyStatus(tweetid);
						dlm2.remove(row);
						statusList2.remove(row);

					} catch (TwitterException e1) {
						JOptionPane.showMessageDialog(frame,
								"Request Limit Reached! Try Again in " + e1.getRateLimitStatus().getSecondsUntilReset(),
								"Twitter Exception", JOptionPane.WARNING_MESSAGE);
						e1.printStackTrace();
					}


					SoundController.playDeleteSound();
				}
			});
			mntmDelete.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
					Application.class.getResource("/images/delete_normal.png"))));
			popupMenu.add(mntmDelete);

			mntmDelete.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					if (e.getSource() instanceof JMenuItem) {
						mntmDelete = (JMenuItem) e.getSource();
						if (mntmDelete.isSelected() || mntmDelete.isArmed()) {
							mntmDelete.setIcon(new ImageIcon(Toolkit
									.getDefaultToolkit().getImage(
											Application.class
													.getResource("/images/delete_on.png"))));
						} else {
							mntmDelete.setIcon(new ImageIcon(
									Toolkit.getDefaultToolkit()
											.getImage(
													Application.class
															.getResource("/images/delete_normal.png"))));
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
		}catch (TwitterException ex){
			JOptionPane.showMessageDialog(frame,
					"Request Limit Reached! Try Again in " + ex.getRateLimitStatus().getSecondsUntilReset(),
					"Twitter Exception", JOptionPane.WARNING_MESSAGE);
		}
		finally {
			panel_Timeline.add(usertimeline);
		}

	}
}
