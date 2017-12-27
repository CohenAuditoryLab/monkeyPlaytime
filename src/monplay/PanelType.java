package monplay;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Encapsulates information for different media types, including icons and media
 * file locations. The number of files should mimic the number of media buttons
 * displayed on a {@code MonkeyPanel}. It also initiates playback of these media
 * files.
 * 
 * @author marsalad
 * @see MonkeyPanel
 * @see MonkeyMediaPlayer
 */
public enum PanelType {
	AUDIO("triangle.png", new String[5]), 
	VIDEO("square.png", new String[5]), 
	TASK("circle.png", new String[2]);

	private static final int DIM = 500; // icon height and width dimension
	private final String icon;
	private final String[] media;

	/**
	 * Creates a new {@code PanelType} given an icon file and initialized array
	 * of media files.
	 * 
	 * @param fileName name of image file for button icons
	 * @param media media file names, initially empty
	 */
	private PanelType(String fileName, String[] media) {
		this.icon = fileName;
		this.media = media;
	}

	/**
	 * Returns the icon associated with the {@code PanelType}.
	 * 
	 * @return icon associated with {@code PanelType}
	 */
	public Icon getIcon() {
		Image img = new ImageIcon(Playtime.class.getResource("/" + icon)).getImage();
		Image scaled = img.getScaledInstance(DIM, DIM, java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(scaled);
	}

	/**
	 * Plays the media with the given index. Locks the app prior to playback.
	 * 
	 * @param i media index
	 * @param c navigation controller to unlock
	 */
	public void playMedia(int i, Playtime.NavController c) {
		c.setEnabled(false);
		String mediaFile = new File("media/" + media[i]).getAbsolutePath();
		if (this.equals(PanelType.VIDEO)) {
			MonkeyMediaPlayer.playVideo(mediaFile, c);
		} else {
			MonkeyMediaPlayer.playAudio(mediaFile, c);
		}
	}

	/**
	 * Returns the {@code PanelType} associated with the given tab index.
	 * 
	 * @param i tab index
	 * @return {@code PanelType} associated with the index
	 * @throws IllegalArgumentException if there is given an invalid tab index
	 */
	public static PanelType getType(int i) {
		if (i == 1) {
			return AUDIO;
		} else if (i == 2) {
			return VIDEO;
		} else if (i == 3) {
			return TASK;
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Called by {@code Playtime} at the start of the program in order to initialize
	 * audio and video media files from the provided txt file.
	 * 
	 * @throws IOException if there is an error in reading
	 */
	static void setup() throws IOException {
		BufferedReader r = new BufferedReader(new FileReader("media/mediaFiles.txt"));
		r.readLine();
		for (PanelType t : new PanelType[] { AUDIO, VIDEO, TASK }) {
			for (int i = 0; i < t.media.length; i++) {
				t.media[i] = r.readLine().trim();
			}
			r.readLine();
			r.readLine();
		}
		r.close();
	}
}