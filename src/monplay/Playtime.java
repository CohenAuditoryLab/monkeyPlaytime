package monplay;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import monplay.layout.MonkeyMediaPanel;
import monplay.layout.MonkeyPanel;
import monplay.layout.MonkeyTaskPanel;

public class Playtime implements Runnable {
	private final JFrame frame = new JFrame();
	private final JTabbedPane tabbedPane = new JTabbedPane();
	private final NavController nav = new NavController();
	private boolean enabled = true;
	
	@Override
	public void run() {
		JPanel home = new JPanel();
		home.setLayout(new GridLayout(1,3));
		
		// add buttons to home tab
		for (int i = 0; i < 3; i++) {
			int index = i + 1;
			JButton temp = new JButton(PanelType.getType(index).getIcon());
			temp.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (nav.isEnabled()) {
						nav.navigate(index);
					}
				}
			});
			temp.setFocusable(false);
			home.add(temp);
		}
		tabbedPane.addTab("HOME", home);
		
		// build media and task tabs
		for (int i = 1; i < 4; i++) {
			MonkeyPanel panel;
			if (i == 3) {
				panel = new MonkeyTaskPanel(nav);
			} else {
				panel = new MonkeyMediaPanel(PanelType.getType(i), nav);
			}
			tabbedPane.addTab(panel.getName(), panel);
		}
		
		// display app on screen
		frame.add(tabbedPane);
		frame.pack();
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		try {
			PanelType.setup();
			MonkeyMediaPlayer.discover();
			SwingUtilities.invokeLater(new Playtime());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public class NavController {
		public boolean isEnabled() { return enabled; }
		public void setEnabled(boolean b) { enabled = b; }		
		public void navigate(int i) { tabbedPane.setSelectedIndex(i); }
	}
}