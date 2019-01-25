package client.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;

import client.Client;
import client.StreamHandler;

public class GUI implements ActionListener, MouseListener {

	public static final Font JPANE_FONT = new Font("MONOSPACED", Font.PLAIN, 15);

	private Client client;
	private StreamHandler sh;

	public JFrame frame;
	private JTextField messageField;
	private JTextArea messageArea;
	private JScrollPane scrollpane;
	private JButton sendButton;
	private JButton disconnectButton;
	private JButton connectButton;
	private JButton themeButton;
	private JTextField ipField;
	private JTextField portField;
	private JLabel ipLabel;
	private JLabel portLabel;
	private Border darkBorder, darkBorder2, lightBorder, lightBorder2;
	private Color lightSelection, darkSelection, hackerSelection;

	private int currentTheme = 0;

	/**
	 * Create the application.
	 */
	public GUI(Client client) {
		this.client = client;
		initialize();
		cycleTheme();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("SDMTP StreamHandler");
		frame.setUndecorated(true);
		frame.setBounds(100, 100, 755, 650);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().addMouseListener(this);

		darkBorder = BorderFactory.createLineBorder(new Color(50, 50, 50));
		darkBorder2 = BorderFactory.createLineBorder(new Color(30, 30, 30));
		lightBorder = BorderFactory.createLineBorder(new Color(180, 180, 230));
		lightBorder2 = BorderFactory.createLineBorder(new Color(135, 135, 145));

		lightSelection = new Color(153, 180, 209);
		darkSelection = new Color(175, 124, 0);
		hackerSelection = new Color(0, 255, 0);

		messageField = new JTextField();
		messageField.setBounds(12, 579, 558, 25);
		messageField.setColumns(10);
		messageField.addActionListener(this);
		frame.getContentPane().add(messageField);

		sendButton = new JButton("Send");
		sendButton.setBounds(582, 578, 156, 27);
		sendButton.addActionListener(this);
		sendButton.addMouseListener(this);
		frame.getContentPane().add(sendButton);

		messageArea = new JTextArea();
		messageArea.setTabSize(4);
		messageArea.setWrapStyleWord(true);
		messageArea.setLineWrap(true);
		messageArea.setEditable(false);
		messageArea.addMouseListener(this);

		DefaultCaret caret = (DefaultCaret) messageArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		scrollpane = new JScrollPane(messageArea);
		scrollpane.setBounds(12, 13, 558, 553);
		scrollpane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
		frame.getContentPane().add(scrollpane);

		disconnectButton = new JButton("Disconnect");
		disconnectButton.setBounds(582, 472, 156, 35);
		disconnectButton.addActionListener(this);
		disconnectButton.addMouseListener(this);
		disconnectButton.setEnabled(false);
		frame.getContentPane().add(disconnectButton);

		connectButton = new JButton("Connect");
		connectButton.setBounds(582, 434, 156, 35);
		connectButton.addActionListener(this);
		connectButton.addMouseListener(this);
		frame.getContentPane().add(connectButton);

		themeButton = new JButton("Theme");
		themeButton.setBounds(582, 13, 156, 35);
		themeButton.addActionListener(this);
		themeButton.addMouseListener(this);
		frame.getContentPane().add(themeButton);

		portLabel = new JLabel("Server port:");
		portLabel.setFont(new Font("Monospaced", Font.PLAIN, 15));
		portLabel.setBounds(576, 311, 162, 25);
		portLabel.addMouseListener(this);
		frame.getContentPane().add(portLabel);

		ipLabel = new JLabel("Server IP:");
		ipLabel.setFont(new Font("Monospaced", Font.PLAIN, 15));
		ipLabel.setBounds(576, 215, 162, 25);
		ipLabel.addMouseListener(this);
		frame.getContentPane().add(ipLabel);

		ipField = new JTextField();
		ipField.setBounds(576, 253, 162, 30);
		ipField.setColumns(10);
		ipField.addActionListener(this);
		ipField.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				ipField.selectAll();
			}
			@Override
			public void focusLost(FocusEvent e) {
				ipField.select(0, 0);
			}
		});
		frame.getContentPane().add(ipField);

		portField = new JTextField();
		portField.setText("12345");
		portField.setBounds(576, 349, 162, 30);
		portField.setColumns(10);
		portField.addActionListener(this);
		portField.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				portField.selectAll();
			}
			@Override
			public void focusLost(FocusEvent e) {
				portField.select(0, 0);
			}
		});
		frame.getContentPane().add(portField);
	}

	public void setStreamHandler(StreamHandler sh) {
		this.sh = sh;
	}

	public void pushMessage(String message) {
		if (!message.trim().equals("")) {
			messageArea.setText(messageArea.getText() + message + "\n");
		}
	}

	public void clearMessageArea() {
		messageArea.setText("");
	}

	public void cycleTheme() {
		switch (currentTheme) {
		case 0:
			frame.getRootPane().setWindowDecorationStyle(JRootPane.INFORMATION_DIALOG);
			frame.getContentPane().setBackground(SystemColor.inactiveCaption);

			messageField.setFont(new Font("Monospaced", Font.PLAIN, 14));
			messageField.setBackground(SystemColor.inactiveCaptionBorder);
			messageField.setForeground(new Color(0, 0, 51));
			messageField.setBorder(lightBorder);
			messageField.setSelectionColor(lightSelection);

			sendButton.setFont(new Font("Monospaced", Font.PLAIN, 13));
			sendButton.setBackground(UIManager.getColor("Button.light"));
			sendButton.setBorder(lightBorder2);
			sendButton.setForeground(new Color(0, 0, 51));

			messageArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
			messageArea.setBackground(SystemColor.inactiveCaptionBorder);
			messageArea.setForeground(new Color(0, 0, 51));
			messageArea.setBorder(lightBorder);
			messageArea.setSelectionColor(lightSelection);
			scrollpane.setBorder(lightBorder);

			disconnectButton.setFont(new Font("Monospaced", Font.PLAIN, 13));
			disconnectButton.setForeground(new Color(0, 0, 51));
			disconnectButton.setBackground(UIManager.getColor("Button.light"));
			disconnectButton.setBorder(lightBorder2);

			connectButton.setFont(new Font("Monospaced", Font.PLAIN, 13));
			connectButton.setForeground(new Color(0, 0, 51));
			connectButton.setBackground(UIManager.getColor("Button.light"));
			connectButton.setBorder(lightBorder2);

			themeButton.setFont(new Font("Monospaced", Font.PLAIN, 13));
			themeButton.setForeground(new Color(0, 0, 51));
			themeButton.setBackground(UIManager.getColor("Button.light"));
			themeButton.setBorder(lightBorder2);

			ipLabel.setFont(new Font("Monospaced", Font.PLAIN, 13));
			ipLabel.setForeground(new Color(0, 0, 51));

			portLabel.setFont(new Font("Monospaced", Font.PLAIN, 13));
			portLabel.setForeground(new Color(0, 0, 51));

			ipField.setFont(new Font("Monospaced", Font.PLAIN, 15));
			ipField.setBackground(SystemColor.inactiveCaptionBorder);
			ipField.setForeground(new Color(0, 0, 51));
			ipField.setBorder(lightBorder);
			ipField.setSelectionColor(lightSelection);

			portField.setFont(new Font("Monospaced", Font.PLAIN, 15));
			portField.setBackground(SystemColor.inactiveCaptionBorder);
			portField.setForeground(new Color(0, 0, 51));
			portField.setBorder(lightBorder);
			portField.setSelectionColor(lightSelection);
			break;
		case 1:
			frame.getRootPane().setWindowDecorationStyle(JRootPane.WARNING_DIALOG);
			frame.getContentPane().setBackground(new Color(45, 45, 51));

			messageField.setFont(new Font("Monospaced", Font.PLAIN, 14));
			messageField.setBackground(new Color(27, 27, 32));
			messageField.setForeground(new Color(255, 204, 0));
			messageField.setBorder(darkBorder);
			messageField.setSelectionColor(darkSelection);

			sendButton.setFont(new Font("Monospaced", Font.PLAIN, 13));
			sendButton.setBackground(new Color(70, 70, 75));
			sendButton.setBorder(darkBorder2);
			sendButton.setForeground(SystemColor.textText);

			messageArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
			messageArea.setBackground(new Color(30, 30, 35));
			messageArea.setForeground(new Color(255, 204, 0));
			messageArea.setBorder(darkBorder);
			messageArea.setSelectionColor(darkSelection);
			scrollpane.setBorder(darkBorder);

			disconnectButton.setFont(new Font("Monospaced", Font.PLAIN, 13));
			disconnectButton.setForeground(SystemColor.textText);
			disconnectButton.setBackground(new Color(70, 70, 75));
			disconnectButton.setBorder(darkBorder2);

			connectButton.setFont(new Font("Monospaced", Font.PLAIN, 13));
			connectButton.setForeground(SystemColor.textText);
			connectButton.setBackground(new Color(70, 70, 75));
			connectButton.setBorder(darkBorder2);

			themeButton.setFont(new Font("Monospaced", Font.PLAIN, 13));
			themeButton.setForeground(SystemColor.textText);
			themeButton.setBackground(new Color(70, 70, 75));
			themeButton.setBorder(darkBorder2);

			ipLabel.setFont(new Font("Monospaced", Font.PLAIN, 13));
			ipLabel.setForeground(new Color(109, 109, 109));

			portLabel.setFont(new Font("Monospaced", Font.PLAIN, 13));
			portLabel.setForeground(new Color(109, 109, 109));

			ipField.setFont(new Font("Monospaced", Font.PLAIN, 15));
			ipField.setBackground(new Color(27, 27, 32));
			ipField.setForeground(new Color(109, 109, 109));
			ipField.setBorder(darkBorder);
			ipField.setSelectionColor(darkSelection);

			portField.setFont(new Font("Monospaced", Font.PLAIN, 15));
			portField.setBackground(new Color(27, 27, 32));
			portField.setForeground(new Color(109, 109, 109));
			portField.setBorder(darkBorder);
			portField.setSelectionColor(darkSelection);
			break;
		case 2:
			frame.getRootPane().setWindowDecorationStyle(JRootPane.COLOR_CHOOSER_DIALOG);
			frame.getContentPane().setBackground(new Color(0, 0, 0));

			messageField.setFont(new Font("DialogInput", Font.PLAIN, 14));
			messageField.setBackground(new Color(0, 0, 0));
			messageField.setForeground(Color.GREEN);
			messageField.setBorder(darkBorder);
			messageField.setSelectionColor(hackerSelection);

			sendButton.setFont(new Font("DialogInput", Font.PLAIN, 13));
			sendButton.setBackground(new Color(21, 21, 21));
			sendButton.setBorder(darkBorder);
			sendButton.setForeground(Color.GREEN);

			messageArea.setFont(new Font("DialogInput", Font.PLAIN, 16));
			messageArea.setBackground(new Color(0, 0, 0));
			messageArea.setForeground(Color.GREEN);
			messageArea.setBorder(darkBorder);
			messageArea.setSelectionColor(hackerSelection);
			scrollpane.setBorder(darkBorder);

			disconnectButton.setFont(new Font("DialogInput", Font.PLAIN, 13));
			disconnectButton.setForeground(Color.GREEN);
			disconnectButton.setBackground(new Color(21, 21, 21));
			disconnectButton.setBorder(darkBorder);

			connectButton.setFont(new Font("DialogInput", Font.PLAIN, 13));
			connectButton.setForeground(Color.GREEN);
			connectButton.setBackground(new Color(21, 21, 21));
			connectButton.setBorder(darkBorder);

			themeButton.setFont(new Font("DialogInput", Font.PLAIN, 13));
			themeButton.setForeground(Color.GREEN);
			themeButton.setBackground(new Color(21, 21, 21));
			themeButton.setBorder(darkBorder);

			ipLabel.setFont(new Font("Monospaced", Font.PLAIN, 13));
			ipLabel.setForeground(Color.GREEN);

			portLabel.setFont(new Font("Monospaced", Font.PLAIN, 13));
			portLabel.setForeground(Color.GREEN);

			ipField.setFont(new Font("Monospaced", Font.PLAIN, 15));
			ipField.setBackground(new Color(21, 21, 21));
			ipField.setForeground(Color.GREEN);
			ipField.setBorder(darkBorder);
			ipField.setSelectionColor(hackerSelection);

			portField.setFont(new Font("Monospaced", Font.PLAIN, 15));
			portField.setBackground(new Color(21, 21, 21));
			portField.setForeground(Color.GREEN);
			portField.setBorder(darkBorder);
			portField.setSelectionColor(hackerSelection);
			break;
		}
		currentTheme++;
		currentTheme %= 3;
	}

	public JButton getDisconnectButton() {
		return disconnectButton;
	}

	public void setDisconnectButton(JButton disconnectButton) {
		this.disconnectButton = disconnectButton;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JLabel text = null;
		if (sh != null) {
			if (e.getSource() == messageField || e.getSource() == sendButton) {
				if (!messageField.getText().trim().equals("")) {
					sh.sendMessage(messageField.getText());
					messageField.setText("");
				}
			} else if ((e.getSource() == connectButton || e.getSource() == ipField || e.getSource() == portField) && connectButton.isEnabled()) {
				if (sh.getSocket() == null || !sh.getSocket().isBound() || sh.getSocket().isClosed()) {
					String ip = ipField.getText();
					if (ip != null && ip.equals("local") || ip.trim().equals("")) ip = "127.0.0.1";
					if (ip != null && !ip.trim().equals("")) {
						int port = -1;
						try {
							port = Integer.parseInt(portField.getText());
						} catch (NumberFormatException e1) {
							port = -1;
						}
						if (port > 0) {
							int result = sh.connect(ip, port);
							if (result == 0) {
								try {
									sh.start();
								} catch (IllegalThreadStateException e1) {
									try {
										sh.initializeStreams();
									} catch (IOException e2) {
										e2.printStackTrace();
									}
								}
								disconnectButton.setEnabled(true);
								connectButton.setEnabled(false);
							}
						} else {
							pushMessage("The port that was entered is invalid.");
						}
					} else {
						pushMessage("The IP that was entered is invalid.");
					}
				} else {
					pushMessage("You are already connected to a server. You must first disconnect.");
				}
				mouseClicked(null);
			} else if (e.getSource() == disconnectButton) {
				if (!e.getActionCommand().equals("FORCED")) pushMessage("Disconnecting from the server...");
				int result = sh.disconnect();
				if (result == 0) {
					if (!e.getActionCommand().equals("FORCED")) pushMessage("Closed all streams.");
					if (!e.getActionCommand().equals("FORCED")) pushMessage("Successfully disconnected.");
					sh = new StreamHandler(client, this);
					disconnectButton.setEnabled(false);
					connectButton.setEnabled(true);
				} else {
					if (!e.getActionCommand().equals("FORCED")) pushMessage("An error occured while disconnecting.");
					if (!e.getActionCommand().equals("FORCED")) pushMessage("Restart the application if any problems arise during use.");
				}
				try {
					sh.closeStreams();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				mouseClicked(null);
			}
		} else {
			text = new JLabel("There is an error with your sh. Please contact a developer for a fixed product.");
			text.setFont(JPANE_FONT);
			JOptionPane.showMessageDialog(null, text);
		}
		if (e.getSource() == themeButton) {
			cycleTheme();
		}
	}

	public void displayStartupMessage() {
		pushMessage("Launched SDMTP Client v0.2.1");
		pushMessage("Connect to a server to start sending messages");
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int i = messageField.getText().length() - 1;
		messageField.select(i + 1, i + 1);
		messageField.grabFocus();
		messageField.requestFocus();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
}
