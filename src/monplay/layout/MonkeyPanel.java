package monplay.layout;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import monplay.PanelType;
import monplay.Playtime.NavController;

@SuppressWarnings("serial")
public abstract class MonkeyPanel extends JPanel {
	final PanelType t;
	final NavController c;

	public MonkeyPanel(PanelType t, NavController c, int height, int width) {
		this.c = c;
		this.t = t;
		setLayout(new GridLayout(height, width));
		add(makeBackButton());
	}
	
	public String getName() {
		return t.name();
	}
	
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
	
	void back() {
		c.navigate(0);
	}
}
