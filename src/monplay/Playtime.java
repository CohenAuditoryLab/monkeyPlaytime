package monplay;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import monplay.layout.MonkeyMediaPanel;
import monplay.layout.MonkeyPanel;
import monplay.layout.MonkeyTaskPanel;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;

/**
 * <p>
 * The {@code Playtime} class is a {@code Runnable} that initializes media
 * variables in {@code PanelType}, discovers native dependencies for media
 * playback using {@code NativeDiscovery}, and creates and shows the GUI using
 * {@code JTabbedPane}.
 * </p>
 * 
 * <p>
 * The app can be locked and unlocked to disable input during playback and users
 * can freely navigate between the home tab and media/task tabs. It includes an
 * inner class {@code NavController} to safely manage this functionality within
 * external classes.
 * </p>
 * 
 * @author marsalad
 * @see JTabbedPane
 * @see NativeDiscovery
 */
public class Playtime implements Runnable {
	private final JFrame frame = new JFrame(); // main component frame, contains tabbedPane
	private final JTabbedPane tabbedPane = new JTabbedPane(); // tabs for different pages
	private final NavController nav = new NavController(); // navigation controller to pass
	private boolean enabled = true; // managed by nav; is the app unlocked?

	@Override
	public void run() {
		JPanel home = new JPanel();
		home.setLayout(new GridLayout(1, 3));

		// add buttons to home tab
		for (int i = 0; i < 3; i++) {
			int index = i + 1;
			JButton temp = new JButton(PanelType.getType(index).getIcon());
			temp.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (nav.isEnabled()) {
						nav.navigate(index);
					}
				}
			});
			temp.setFocusable(false);
			home.add(temp);
		}
		tabbedPane.addTab("HOME", home);

		// build media and task tabs
		for (int i = 1; i < 4; i++) {
			MonkeyPanel panel;
			if (i == 3) {
				panel = new MonkeyTaskPanel(nav);
			} else {
				panel = new MonkeyMediaPanel(PanelType.getType(i), nav);
			}
			tabbedPane.addTab(panel.getName(), panel);
		}

		// display app on screen
		frame.add(tabbedPane);
		frame.pack();
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		try {
			PanelType.setup(); // setup media files
			new NativeDiscovery().discover(); // discover vlcj dependencies
			SwingUtilities.invokeLater(new Playtime()); // create and show app
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * {@code NavController} manages the lock and unlocked state of the app and
	 * enables navigation to different tabs. Should be passed as an argument to
	 * external classes to enable safe navigation and locking/unlocking.
	 * 
	 * @author marsalad
	 */
	public class NavController {

		/**
		 * Returns if the app is unlocked or not.
		 * 
		 * @return {@code true} if the app is unlocked; otherwise {@code false}
		 */
		public boolean isEnabled() {
			return enabled;
		}

		/**
		 * Locks or unlocks the app, depending on the value of {code b}.
		 * 
		 * @param b if {@code true}, the app is unlocked; otherwise the app is locked
		 */
		public void setEnabled(boolean b) {
			enabled = b;
		}

		/**
		 * Sets the selected index for the main {@code JTabbedPane}. The index must be a
		 * valid tab index.
		 * 
		 * @param i tab index to navigate to
		 */
		public void navigate(int i) {
			tabbedPane.setSelectedIndex(i);
		}

	}
}