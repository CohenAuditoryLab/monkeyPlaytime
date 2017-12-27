package monplay;

import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public enum PanelType {
	AUDIO ("triangle.png", new String[] {"lo.mp3", "lo.mp3", "lo.mp3", "lo.mp3", "lo.mp3"}), 
	VIDEO ("square.png",   new String[] {"test.mp4", "test.mp4", "test.mp4", "test.mp4", "test.mp4"}), 
	TASK  ("circle.png",   new String[] {"lo.mp3", "hi.mp3"});
	
	private static final int DIM = 500;
	private final String fileName;
	private final String[] media;
	private PanelType(String fileName, String[] media) {
		this.fileName = fileName;
		this.media = media;
	}
	
	public Icon getIcon() {
		Image img = new ImageIcon("resources/" + fileName).getImage() ;  
	    Image scaled = img.getScaledInstance(DIM, DIM, java.awt.Image.SCALE_SMOOTH) ;  
	    return new ImageIcon(scaled);
	}
	
	public void playMedia(int i, Playtime.NavController c) {
		if (this.equals(PanelType.VIDEO)) {
			MonkeyMediaPlayer.playVideo("resources/" + media[i], c);
		} else {
			MonkeyMediaPlayer.playAudio("resources/" + media[i], c);
		}
	}
	
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
}
