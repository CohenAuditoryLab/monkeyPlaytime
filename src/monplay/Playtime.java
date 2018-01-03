package monplay;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import com.opencsv.CSVWriter;

import monplay.layout.MonkeyMediaPanel;
import monplay.layout.MonkeyPanel;
import monplay.layout.MonkeyPanelType;
import monplay.layout.MonkeyTaskPanel;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaPlayer;

/**
 * <p>
 * The {@code Playtime} class is a {@code Runnable} that initializes media
 * variables in {@code MonkeyPanelType}, discovers native dependencies for media
 * playback using {@code NativeDiscovery}, and creates and shows the GUI using
 * {@code JTabbedPane}.
 * </p>
 * 
 * <p>
 * The app is locked to disable input during playback and users can freely
 * navigate between the home tab and media/task tabs. It includes an inner class
 * {@code NavController} to safely manage this functionality within external
 * classes.
 * </p>
 * 
 * @author marsalad
 * @see JTabbedPane
 * @see NativeDiscovery
 */
public class Playtime implements Runnable {
	private final JFrame frame = new JFrame(); // main component frame, contains tabbedPane
	private final JTabbedPane tabbedPane = new JTabbedPane(); // tabs for different pages
	private ActionController c; // controller for action data, locking, and navigation

	@Override
	public void run() {
		String monkey = getSessionMonkey();
		String fileName = writeGeneralSessionData(monkey);
		c = new ActionController(fileName);
		createAndShowGUI();
	}
	
	/**
	 * Prompts user for the name of the monkey playing this session.
	 * 
	 * @return name of monkey playing this session
	 */
	private String getSessionMonkey() {
		try (BufferedReader r = new BufferedReader(new FileReader("data/monkeys.txt"))) {
			String[] monkeys = new String[Integer.parseInt(r.readLine())];
			for (int i = 0; i < monkeys.length; i++) {
				monkeys[i] = r.readLine().trim();
			}
			return (String) JOptionPane.showInputDialog(null, "Who is playing?", "Name",
					JOptionPane.QUESTION_MESSAGE, null, monkeys, monkeys[0]);
		} catch (IOException | NumberFormatException e) {
			exitWithMsg("Error occurred trying to read monkeys.txt");
		}
		
		exitWithMsg("No monkey was selected");
		return null; // unreachable
	}
	
	/**
	 * Creates and initializes the data file for this session with basic information
	 * such as monkey name, datetime, and file names of media files used for this
	 * session.
	 * 
	 * @param monkey name of monkey playing this session
	 * @return name of session data file
	 */
	private String writeGeneralSessionData(String monkey) {
		DateTimeFormatter dtfEx = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm");
		DateTimeFormatter dtfIn = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime time = LocalDateTime.now();
		String fileName = "data/" + monkey + "_" + dtfEx.format(time) + ".csv";

		try (CSVWriter w = new CSVWriter(new FileWriter(fileName))) {
			w.writeNext(new String[] { monkey, dtfIn.format(time) });
			for (int i = 1; i < 4; i++) {
				MonkeyPanelType t = MonkeyPanelType.getType(i);
				String[] mediaFileNames = new String[i == 3 ? 3 : 6];
				mediaFileNames[0] = t.toString();
				for (int j = 1; j < mediaFileNames.length; j++) {
					mediaFileNames[j] = t.getMediaName(j - 1);
				}
				w.writeNext(mediaFileNames);
			}
		} catch (IOException e) {
			exitWithMsg("Error occurred trying to create data file");
		}

		return fileName;
	}
	
	/**
	 * Creates and displays the tabbed GUI once data file has been initialized.
	 */
	private void createAndShowGUI() {
		JPanel home = new JPanel();
		home.setLayout(new GridLayout(1, 3));

		// add buttons to home tab
		for (int i = 0; i < 3; i++) {
			int index = i + 1;
			JButton temp = new JButton(MonkeyPanelType.getType(index).getIcon());
			temp.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (c.isEnabled()) {
						c.navigate(index);
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
				panel = new MonkeyTaskPanel(c);
			} else {
				panel = new MonkeyMediaPanel(MonkeyPanelType.getType(i), c);
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
			MonkeyPanelType.setup(); // setup media files
			new NativeDiscovery().discover(); // discover vlcj dependencies
			SwingUtilities.invokeLater(new Playtime()); // create and show app
		} catch (IOException e) {
			exitWithMsg("Error occurred trying to read mediaFiles.txt");
		}
	}
	
	/**
	 * Display an error message to user before exiting app.
	 * 
	 * @param msg error message to display
	 */
	private static void exitWithMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
		System.exit(0);
	}

	/**
	 * {@code ActionController} manages the lock and unlocked state of the app and
	 * enables navigation to different tabs. Should be passed as an argument to
	 * external classes to enable safe navigation and locking/unlocking. The controller
	 * also writes data to the session data file when instructed by external classes.
	 * 
	 * @author marsalad
	 */
	public class ActionController {
		BufferedWriter w; // data file writer
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss"); // data time format
		MediaPlayer mediaPlayer; // most recent media player
		
		/**
		 * Creates the {@code ActionController} to manage the locked status of the app,
		 * navigation across tabs, and saving action data to the specified file.
		 * 
		 * @param fileName name of created data file
		 */
		private ActionController(String fileName) {
			try {
				w = new BufferedWriter(new FileWriter(fileName, true));
			} catch (IOException e) {
				exitWithMsg("Error occurred trying to find data file");
			}
		}

		/**
		 * Returns if the app is unlocked or not. The app is unlocked if there is no
		 * media currently being played.
		 * 
		 * @return {@code true} if the app is unlocked; otherwise {@code false}
		 */
		public boolean isEnabled() {
			return mediaPlayer == null || !mediaPlayer.isPlaying();
		}

		/**
		 * Updates the current {@code MediaPlayer}. Should be called prior to playback
		 * of any media.
		 * 
		 * @param mediaPlayer {@code MediaPlayer} media will be played on
		 */
		public void setPlayer(MediaPlayer mediaPlayer) {
			this.mediaPlayer = mediaPlayer;
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
		
		/**
		 * Writes a user action to the data file.
		 * 
		 * @param t type of panel action occurred in
		 * @param button index of button pressed
		 * @param media index of media played
		 */
		public void saveAction(MonkeyPanelType t, int button, int media) {
			try {
				w.newLine();
				String time = "\"" + dtf.format(LocalDateTime.now()) + "\"";
				w.write(time + ",\"" + t.toString() + "\"," + button + "," + media);
				w.flush(); // ensure when app is closed, all data has already been written
			} catch (IOException e) {
				exitWithMsg("Error occurred trying to write data to file");
			}
		}
	}
}