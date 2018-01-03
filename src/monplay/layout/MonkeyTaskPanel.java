package monplay.layout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import monplay.Playtime.ActionController;

/**
 * {@code MonkeyTaskPanel} is a {@code MonkeyPanel} setup for a 2-by-2 grid of
 * buttons, one of which plays tones and two of which are response buttons.
 * Interfaces with hardware to dispense rewards upon correct responses.
 * 
 * @author marsalad
 * @see MonkeyPanel
 */
@SuppressWarnings("serial")
public class MonkeyTaskPanel extends MonkeyPanel {
	private int toneLevel = -1; // correct response: -1 = none, 0 = lo, 1 = hi

	/**
	 * Constructs a {@code MonkeyTaskPanel} with access to lock and unlock the app.
	 * The panel is setup as a 2-by-2 grid of buttons with a single tone button and
	 * two response buttons. A reward is dispensed if the response is correct.
	 * 
	 * @param c navigation controller to lock and unlock
	 */
	public MonkeyTaskPanel(ActionController c) {
		super(MonkeyPanelType.TASK, c, 2, 2);

		// tone button
		JButton tone = new JButton(t.getIcon());
		tone.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (c.isEnabled()) {
					// select lo or hi tone randomly and play
					toneLevel = ThreadLocalRandom.current().nextInt(0, 2);
					c.saveAction(t, 0, toneLevel);
					t.playMedia(toneLevel, c);
				}
			}
		});
		tone.setFocusable(false);
		add(tone);

		// response buttons
		for (int i = 0; i < 2; i++) {
			int guess = i;
			JButton temp = new JButton();
			temp.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					c.saveAction(t, guess, -1);
					if (guess == toneLevel) {
						JOptionPane.showMessageDialog(null, "Reward!!!");
					}
					toneLevel = -1;
				}
			});
			temp.setFocusable(false);
			add(temp);
		}
	}

	@Override
	void back() {
		super.back();
		toneLevel = -1;
	}
}