package client.gui.loading;

import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JRootPane;
import javax.swing.UIManager;
import java.awt.SystemColor;

public class KeyLoading {

	private JFrame frame;
	private JProgressBar progressBar;
	private JLabel label;

	/**
	 * Create the application.
	 */
	public KeyLoading() {
		initialize();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setUndecorated(true);
		frame.getContentPane().setBackground(SystemColor.inactiveCaption);
		frame.setTitle("Generating RSA keys");
		frame.setBounds(100, 100, 518, 167);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		frame.getRootPane().setWindowDecorationStyle(JRootPane.INFORMATION_DIALOG);

		progressBar = new JProgressBar();
		progressBar.setForeground(UIManager.getColor("ToolBar.dockingForeground"));
		progressBar.setBackground(UIManager.getColor("TextField.inactiveBackground"));
		progressBar.setBounds(31, 71, 439, 36);
		frame.getContentPane().add(progressBar);

		label = new JLabel();
		label.setFont(new Font("Monospaced", Font.PLAIN, 15));
		label.setBounds(31, 13, 439, 45);
		frame.getContentPane().add(label);
	}

	public void setBarPercentage(int percentage) {
		progressBar.setValue(percentage);
	}

	public int getBarPercentage() {
		return progressBar.getValue();
	}

	public void incrementPercentage(int amount) {
		progressBar.setValue(progressBar.getValue() + amount);
	}

	public void setLabel(String text) {
		label.setText(text);
	}

	public void dispose() {
		frame.dispose();
	}
}
