package monplay;

import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

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
                
                mediaPlayerComponent.getMediaPlayer().playMedia(fileName);
            }
        });
	}
	
	public static void displayError() {
		System.out.println("Error playing media");
	}
	
	public static void discover() {
		if (new NativeDiscovery().discover()) {
			System.out.println();
			System.out.println("FOUND FILES");
			System.out.println();
		}
		
		NativeLibrary.addSearchPath("libvlc", "C:/Program Files (x86)/VideoLAN/VLC");
		System.out.println();
		System.out.println("libvlc plain:");
		System.out.println("-----------------------------------------------------");
		System.out.println(LibVlc.INSTANCE.libvlc_get_version());
		System.out.println("-----------------------------------------------------");
		
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "C:/Program Files (x86)/VideoLAN/VLC");
		System.out.println();
		System.out.println("RuntimeUtil:");
		System.out.println("-----------------------------------------------------");
		System.out.println(LibVlc.INSTANCE.libvlc_get_version());
		System.out.println("-----------------------------------------------------");
	}
}
