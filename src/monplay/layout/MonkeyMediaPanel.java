package monplay.layout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import monplay.PanelType;
import monplay.Playtime.NavController;

/**
 * {@code MonkeyMediaPanel} is a {@code MonkeyPanel} setup for a 2-by-3 grid of
 * audio or video buttons. It uses a {@code GridLayout} and {@code PanelType} to
 * specify the appearance and behavior of buttons.
 * 
 * @author marsalad
 * @see MonkeyPanel
 */
@SuppressWarnings("serial")
public class MonkeyMediaPanel extends MonkeyPanel {

	/**
	 * Constructs a {@code MonkeyMediaPanel} with access to lock and unlock the app.
	 * The panel is setup as a 2-by-3 grid of audio or video buttons.
	 * 
	 * @param t type of panel to construct
	 * @param c navigation controller to lock and unlock
	 * @throws IllegalArgumentException if {@code t} is set to {@code TASK}
	 */
	public MonkeyMediaPanel(PanelType t, NavController c) {
		super(t, c, 2, 3);
		if (t.equals(PanelType.TASK)) {
			throw new IllegalArgumentException();
		}
		for (int i = 1; i < 6; i++) {
			int index = i - 1;
			JButton temp = new JButton(t.getIcon());
			temp.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (c.isEnabled()) {
						t.playMedia(index, c);
					}
				}
			});
			temp.setFocusable(false);
			add(temp);
		}
	}

}