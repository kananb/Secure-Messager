package client;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import client.encryption.encoding.Converter;
import client.gui.GUI;

public class StreamHandler extends Thread {

	private String ip;
	private int port;
	private Socket socket;
	private boolean connecting;
	private boolean secure;

	private BufferedReader in;
	private PrintWriter out;

	private GUI gui;
	private Client client;

	private String alias;

	public StreamHandler(Client client, GUI gui) {
		this.client = client;
		this.gui = gui;
		this.secure = false;
	}

	public int connect(String ip, int port) {
		try {
			closeStreams();
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.ip = ip;
		this.port = port;
		connecting = true;
		int counter = 0;
		socket = new Socket();

		gui.clearMessageArea();
		try {
			while (connecting && (socket == null  || !socket.isBound())) {
				gui.pushMessage("Attempting connection to " + this.ip + " on port " + this.port);
				try {
					socket.connect(new InetSocketAddress(ip, port), 2000);
					break;
				} catch (IOException e) {}
				if (++counter >= 3) {
					gui.pushMessage("Connection aborted; too many failed attempts.");
					return 1;
				}
			}

			initializeStreams();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void initializeStreams() throws IOException {
		if (socket != null && socket.isBound()) {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
		}
	}

	public void closeStreams() throws IOException {
		if (in != null) in.close();
		if (out != null) out.close();
		in = null;
		out = null;
	}

	public int disconnect() {
		int ret = 1;
		if (out != null) {
			out.flush();
			out.println("EXT");
		}
		try {
			ret = closeConnections();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public int closeConnections() throws IOException {
		int closing = 0;
		if (in != null) {
			in.close();
		} else closing = 1;
		if (out != null) {
			out.flush();
			out.close();
		} else closing = 1;
		if (socket != null) {
			socket.close();
		} else closing = 1;
		return closing;
	}

	public void run() {
		String line = null;
		String command = null;
		String message = null;
		while (true) {
			try {
				line = in.readLine();
			} catch (IOException e) {
				if (e.getMessage().equals("Stream closed")) {
					while (true) {
						try {
							line = in.readLine();
							break;
						} catch (IOException e1) {}
					}
				} else {
					break;
				}
			}
			if (socket.isClosed()) return;
			if (line != null) {
				if (line.indexOf(" ") == -1) {
					command = line;
				} else {
					command = line.substring(0, line.indexOf(" "));
				}
				if (command.equals("EXT")) {
					 handleEXT();
					 break;
				} else if (command.equals("ERR")) {
					handleERR(line);
				} else {
					message = line.substring(line.indexOf(" ") + 1);
					if (command.equals("PSH")) {
						handlePSH(message);
					} else if (command.equals("REQ")) {
						handleREQ(message);
					}
				}
			} else {
				break;
			}
		}
		try {
			closeConnections();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void handleEXT() {
		gui.actionPerformed(new ActionEvent(gui.getDisconnectButton(), 0, "FORCED"));
	}
	public void handleERR(String line) {
		String message = line.substring(line.indexOf(" ") + 1);
		if (secure) message = client.decode(message);
		JLabel text = new JLabel("An error occured: \n" + message);
		text.setFont(GUI.JPANE_FONT);
		JOptionPane.showMessageDialog(null, text);
	}
	public void handlePSH(String message) {
		if (secure) {
			String decrypted = client.decode(message);
			gui.pushMessage(decrypted);
		} else {
			gui.pushMessage(message);
		}
	}
	public void handleREQ(String message) {
		if (message.equals("USRNAME")) {
			JLabel text = new JLabel("Enter an alias:");
			text.setFont(GUI.JPANE_FONT);
			alias = JOptionPane.showInputDialog(text);
			if (alias == null) {
				gui.actionPerformed(new ActionEvent(gui.getDisconnectButton(), 0, ""));
				out.println("EXT");
			} else {
				out.println("REP " + ( (secure) ? client.encode(alias) : alias));
			}
		} else if (message.equals("KEYPAIR")) {
			out.println("REP " + client.getE() + " " + client.getN());
			String response;
			while (true) {
				try {
					response = in.readLine();
				} catch (IOException e) {
					shutdown();
					return;
				}
				if (response != null && response.equals("ACK")) {
					out.println("REQ KEY");
					break;
				}
			}
			response = null;
			try {
				response = in.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (response != null && response.indexOf("REP") != -1 && response.indexOf(" ") != -1) {
				String key = response.substring(response.indexOf(" ") + 1);
				client.setPAKey(Converter.decode(client.decryptRSA(key)));
				secure = true;
			} else {
				shutdown();
			}
		}
	}

	private void shutdown() {
		gui.pushMessage("The server may be using outdated protocols, terminating connection.");
		gui.actionPerformed(new ActionEvent(gui.getDisconnectButton(), 0, ""));
	}

	public void sendMessage(String message) {
		if (out != null) {
			out.println("PSH " + ( (secure) ? client.encode(message) : message));
		} else {
			JLabel text = new JLabel("You must connect to a server before you can send messages.");
			text.setFont(GUI.JPANE_FONT);
			JOptionPane.showMessageDialog(null, text);
		}
	}

	public void setConnecting(boolean connecting) {
		this.connecting = connecting;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getAlias() {
		return alias;
	}
}
