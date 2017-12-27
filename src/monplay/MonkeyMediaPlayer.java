package monplay;

import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;

public class MonkeyMediaPlayer {
	
	public static void playAudio(String fileName, Playtime.NavController c) {
        AudioMediaPlayerComponent mediaPlayerComponent = new AudioMediaPlayerComponent();
        mediaPlayerComponent.getMediaPlayer().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
			@Override
			public void finished(MediaPlayer mediaPlayer) {
				c.setEnabled(true);
			}
			@Override
		    public void error(MediaPlayer mediaPlayer) {
				finished(mediaPlayer);
				displayError();
		    }
		});
        mediaPlayerComponent.getMediaPlayer().playMedia(fileName);
	}
	
	public static void playVideo(String fileName, Playtime.NavController c) {
		new NativeDiscovery().discover();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	JFrame frame = new JFrame();
                EmbeddedMediaPlayerComponent mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
                
                mediaPlayerComponent.getMediaPlayer().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
                    @Override
                    public void finished(MediaPlayer mediaPlayer) {
                    	frame.setVisible(false);
                    	frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                    	c.setEnabled(true);
                    }
                    @Override
                    public void error(MediaPlayer mediaPlayer) {
                    	finished(mediaPlayer);
                    	displayError();
                    }
                });
                
                frame.setContentPane(mediaPlayerComponent);
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                frame.setVisible(true);
                
                mediaPlayerComponent.getMediaPlayer().playMedia("resources/test.mp4");
            }
        });
	}
	
	public static void displayError() {
		System.out.println("Error playing media");
	}
}
