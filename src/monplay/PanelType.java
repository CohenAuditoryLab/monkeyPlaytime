package monplay;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public enum PanelType {
	AUDIO ("triangle.png", new String[5]), 
	VIDEO ("square.png",   new String[5]), 
	TASK  ("circle.png",   new String[2]);
	
	private static final int DIM = 500;
	private final String icon;
	private final String[] media;
	private PanelType(String fileName, String[] media) {
		this.icon = fileName;
		this.media = media;
	}
	
	public Icon getIcon() {
		Image img = new ImageIcon(Playtime.class.getResource("/" + icon)).getImage() ;  
	    Image scaled = img.getScaledInstance(DIM, DIM, java.awt.Image.SCALE_SMOOTH) ;  
	    return new ImageIcon(scaled);
	}
	
	public void playMedia(int i, Playtime.NavController c) {
		String mediaFile = new File("media/" + media[i]).getAbsolutePath();
		if (this.equals(PanelType.VIDEO)) {
			MonkeyMediaPlayer.playVideo(mediaFile, c);
		} else {
			MonkeyMediaPlayer.playAudio(mediaFile, c);
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
	
	static void setup() throws IOException {
		BufferedReader r = new BufferedReader(new FileReader("media/mediaFiles.txt"));
		r.readLine();
		for (PanelType t : new PanelType[] {AUDIO, VIDEO, TASK}) {
			for (int i = 0; i < t.media.length; i++) {
				t.media[i] = r.readLine().trim();
			}
			r.readLine();
			r.readLine();
		}
		r.close();
	}
}
