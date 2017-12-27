package monplay.layout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import monplay.PanelType;
import monplay.Playtime.NavController;

@SuppressWarnings("serial")
public class MonkeyMediaPanel extends MonkeyPanel {
	public MonkeyMediaPanel(PanelType t, NavController c) {
		super(t, c, 2, 3);
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