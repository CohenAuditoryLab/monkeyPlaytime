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
 * files. The app is locked just prior to beginning playback and correspondingly
 * unlocked upon completion of playback. If an error is encountered in playback,
 * the app is unlocked and continues normally.
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
		mediaPlayerComponent.getMediaPlayer().addMediaPlayerEventListener(
				new MonkeyMediaPlayerEventAdapter(null, c));
		mediaPlayerComponent.getMediaPlayer().playMedia(fileName);
	}

	/**
	 * Creates an {@code EmbeddedMediaPlayerComponent} and plays the given audio
	 * media file. The app is unlocked upon completion of the audio stream or upon
	 * errors in playback.
	 * 
	 * @param fileName name of video media file
	 * @param c navigation controller to unlock
	 */
	public static void playVideo(String fileName, Playtime.NavController c) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame frame = new JFrame();
				EmbeddedMediaPlayerComponent mediaPlayerComponent = 
						new EmbeddedMediaPlayerComponent();
				mediaPlayerComponent.getMediaPlayer().addMediaPlayerEventListener(
						new MonkeyMediaPlayerEventAdapter(frame, c));

				frame.setContentPane(mediaPlayerComponent);
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				frame.setVisible(true);
				mediaPlayerComponent.getMediaPlayer().playMedia(fileName);
			}
		});
	}

	/**
	 * Adapts the {@code MediaPlayerEventAdapter} class to lock and unlock the app
	 * based on media status.
	 * 
	 * @author marsalad
	 * @see MediaPlayerEventAdapter
	 */
	private static class MonkeyMediaPlayerEventAdapter extends MediaPlayerEventAdapter {
		JFrame frame;
		Playtime.NavController c;

		/**
		 * {@inheritDoc}
		 * 
		 * @param frame enclosing frame for video media, {@code null} for audio media
		 * @param c navigation controller to unlock
		 */
		private MonkeyMediaPlayerEventAdapter(JFrame frame, Playtime.NavController c) {
			this.frame = frame;
			this.c = c;
		}

		@Override
		public void finished(MediaPlayer mediaPlayer) {
			if (frame != null) {
				frame.setVisible(false);
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
			c.setEnabled(true);
		}

		@Override
		public void error(MediaPlayer mediaPlayer) {
			finished(mediaPlayer);
			System.out.println("Error playing media");
		}
	}

}