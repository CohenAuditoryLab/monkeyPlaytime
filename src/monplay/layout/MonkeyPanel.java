package monplay.layout;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import monplay.Playtime.ActionController;

/**
 * {@code MonkeyPanel} is a lightweight {@code JPanel} with additional
 * functionality for the {@code Playtime} app. It uses a {@code GridLayout} and
 * {@code MonkeyPanelType} to specify the appearance and behavior of buttons.
 * 
 * @author marsalad
 * @see JPanel
 * @see MonkeyPanelType
 * @see Playtime
 */
@SuppressWarnings("serial")
public abstract class MonkeyPanel extends JPanel {
	final MonkeyPanelType t;
	final ActionController c;

	/**
	 * Constructs a {@code MonkeyPanel} with access to lock and unlock the app. The
	 * type and number of buttons are specified by the user.
	 * 
	 * @param t type of panel to construct
	 * @param c navigation controller to lock and unlock
	 * @param height number of buttons in grid vertically
	 * @param width number of buttons in grid horizontally
	 */
	public MonkeyPanel(MonkeyPanelType t, ActionController c, int height, int width) {
		this.c = c;
		this.t = t;
		setLayout(new GridLayout(height, width));
		add(makeBackButton());
	}

	/**
	 * Returns the name of the panel, which is the same as the {@code MonkeyPanelType}.
	 * 
	 * @return name of panel
	 */
	public String getName() {
		return t.name();
	}

	/**
	 * Creates and returns a back button that, upon being pressed will return the
	 * user to the home tab by calling {@code back()}.
	 * 
	 * @return back button
	 */
	private JButton makeBackButton() {
		JButton back = new JButton();
		back.setBackground(Color.RED);
		back.setFocusable(false);
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (c.isEnabled()) {
					back();
				}
			}
		});
		return back;
	}

	/**
	 * Leave the current tab and go to the home tab.
	 */
	void back() {
		c.navigate(0);
	}
}