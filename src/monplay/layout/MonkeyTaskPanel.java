package monplay.layout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import monplay.PanelType;
import monplay.Playtime.NavController;

@SuppressWarnings("serial")
public class MonkeyTaskPanel extends MonkeyPanel {
	private int toneLevel = -1;
	
	public MonkeyTaskPanel(NavController c) {
		super(PanelType.TASK, c, 2, 2);
		
		JButton tone = new JButton(t.getIcon());
		tone.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (c.isEnabled()) {
					toneLevel = ThreadLocalRandom.current().nextInt(0, 2);
					t.playMedia(toneLevel, c);
				}
			}
		});
		tone.setFocusable(false);
		add(tone);
		
		for (int i = 0; i <  2; i++) {
			int guess = i;
			JButton temp = new JButton();
			temp.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
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