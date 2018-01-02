package monplay;

import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;

/**
 * Uses utilities in the vlcj package to play a variety of audio and video media
 * files. If an error is encountered in playback, no media plays and the user can
 * continue playing media files normally.
 * 
 * @author marsalad
 * @see MediaPlayer
 */
public class MonkeyMediaPlayer {

	/**
	 * Creates an {@code AudioMediaPlayerComponent} and plays the given audio media
	 * file. The app is unlocked upon completion of the audio stream or upon errors
	 * in playback.
	 * 
	 * @param fileName name of audio media file
	 * @param c navigation controller to unlock
	 */
	public static void playAudio(String fileName, Playtime.NavController c) {
		AudioMediaPlayerComponent mediaPlayerComponent = new AudioMediaPlayerComponent();
		c.setPlayer(mediaPlayerComponent.getMediaPlayer());
		mediaPlayerComponent.getMediaPlayer().playMedia(fileName);
	}

	/**
	 * Creates an {@code EmbeddedMediaPlayerComponent} and plays the given audio
	 * media file.
	 * 
	 * @param fileName name of video media file
	 * @param c navigation controller to unlock
	 */
	public static void playVideo(String fileName, Playtime.NavController c) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				playVideoThreaded(fileName, c);
			}
		});
	}
	
	/**
	 * Creates an {@code EmbeddedMediaPlayerComponent} and plays the given audio
	 * media file once multi-threading is invoked.
	 * 
	 * @param fileName name of video media file
	 * @param c navigation controller to unlock
	 */
	private static void playVideoThreaded(String fileName, Playtime.NavController c) {
		JFrame frame = new JFrame();
		EmbeddedMediaPlayerComponent mediaPlayerComponent = 
				new EmbeddedMediaPlayerComponent();
		mediaPlayerComponent.getMediaPlayer().addMediaPlayerEventListener(
				new MediaPlayerEventAdapter() {
					@Override
					public void finished(MediaPlayer mediaPlayer) {
						frame.setVisible(false);
						frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
					}
					
					@Override
					public void error(MediaPlayer mediaPlayer) {
						finished(mediaPlayer);
						System.out.println("Error playing media");
					}
				}
		);

		frame.setContentPane(mediaPlayerComponent);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
		c.setPlayer(mediaPlayerComponent.getMediaPlayer());
		mediaPlayerComponent.getMediaPlayer().playMedia(fileName);
	}

}