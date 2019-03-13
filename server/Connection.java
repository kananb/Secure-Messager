package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import server.encryption.Converter;
import server.encryption.Polyalphabetic;
import server.encryption.RSA;

public class Connection extends Thread {

	private int id;
	private Socket socket;
	private Server server;

	private BufferedReader in;
	private PrintWriter out;

	private String username;

	private String n;
	private String publicKey;
	private Polyalphabetic pa;

	public Connection(Socket socket, Server server, int id) {
		this.socket = socket;
		this.server = server;
		this.id = id;
		this.pa = new Polyalphabetic();
	}

	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Timeout timeout;
		String response = null;
		while (publicKey == null || n == null) {
			out.println("REQ KEYPAIR");
			try {
				timeout = new Timeout(5000, this);
				timeout.start();
				response = in.readLine();
				timeout.stopTimer();
				timeout.interrupt();
			} catch (IOException e) {
				return;
			}
			System.out.println(response);
			if (response.equals("EXT")) clearConnection();
			if (response != null && response.startsWith("REP")) {
				String[] split = response.split(" ");
				if (split.length >= 3) {
					publicKey = split[1];
					n = split[2];
					if (!RSA.isValidKey(publicKey) || !RSA.isValidKey(n)) {
						publicKey = null;
						n = null;
					}
				}
			}
		}

		out.println("ACK");

		response = null;
		while (true) {
			try {
				timeout = new Timeout(5000, this);
				timeout.start();
				response = in.readLine();
				timeout.stopTimer();
				timeout.interrupt();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if (response != null) {
				if (response.equals("EXT")) clearConnection();
				if (response.indexOf("REQ") != -1 && response.indexOf("KEY") != -1) {
					pa.setKey(Polyalphabetic.generateKey(128));
					String encryptedKey = RSA.encrypt(Converter.encode(pa.getKey()), publicKey, n);
					out.println("REP " + encryptedKey);
					break;
				} else {
					out.println("PSH Unexpected command, terminating connection.");
					out.println("EXT");
					clearConnection();
				}
			}
		}

		response = null;
		boolean invalid, badResponse;
		int nullcount = 0;
		int invalidcount = 0;
		while (username == null) {
			invalid = false;
			try {
				out.println("REQ USRNAME");
				timeout = new Timeout(30000, this);
				timeout.start();
				response = in.readLine();
				timeout.stopTimer();
				timeout.interrupt();
			} catch (IOException e) {
				return;
			}
			if (response == null) nullcount++;
			if (nullcount >= 50) clearConnection();
			if (response != null) {
				if (response.equals("EXT")) clearConnection();
				badResponse = false;
				if (response.startsWith("REP")) {
					nullcount = 0;
					System.out.println("User " + id + " " + response);
					username = pa.decode(response.substring(response.indexOf(" ") + 1));
					if (username.length() > 15) {
						invalid = true;
						out.println("PSH " + pa.encode("Username must be less than 16 characters."));
					} else if (username.trim().equals("")) {
						invalid = true;
						out.println("PSH " + pa.encode("Username must contain non-whitespace characters."));
					}
					else {
						synchronized (this) {
							for (Connection c : server.connections) {
								if (c != null && c != this && c.getUsername() != null && c.getUsername().toLowerCase().equals(username.toLowerCase())) {
									invalid = true;
									out.println("PSH " + pa.encode("That username is already taken."));
								}
							}
						}
					}
				}
			} else {
				badResponse = true;
			}
			if (invalid) {
				invalidcount++;
				if (invalidcount >= 10) {
					out.println("PSH " + pa.encode("Too many attempts, terminating connection."));
					out.println("EXT");
					clearConnection();
				}
				if (badResponse) out.println("PSH " + pa.encode("That response is invalid."));
				username = null;
			}
		}

		addToConnections();

		int connectedUsers = 0;
		synchronized (this) {
			for (Connection c : server.connections) {
				if (c != null && c != this) {
					connectedUsers++;
				}
			}
		}

		System.out.println("User " + id + " " + username + " has connected.");

		synchronized (this) {
			for (Connection c : server.connections) {
				if (c != null) {
					if (c != this) {
						c.getOutputStream().println("PSH " + c.encrypt(username + " has connected."));
						c.getOutputStream().println("PSH " + c.encrypt("There are now " + connectedUsers + " other connected users."));
					} else {
						out.println("PSH " + pa.encode("You have connected."));
						out.println("PSH " + pa.encode("There are currently " + connectedUsers + " other connected users."));
					}
				}
			}
		}

		String line = null;
		String command = null;
		while (true) {
			try {
				line = in.readLine().trim();
			} catch (IOException e) {
				break;
			}
			if (line != null) {
				System.out.println("User " + id + " " + username + ": " + line);
				if (line.indexOf(" ") == -1) {
					command = line;
				} else {
					command = line.substring(0, line.indexOf(" "));
				}
				if (command.equals("PSH")) {
					String message = pa.decode(line.substring(line.indexOf(" ") + 1));

					synchronized (this) {
						for (Connection c : server.connections) {
							if (c != null) {
								if (c != this) {
									c.getOutputStream().println("PSH " + c.encrypt(username + ": " + message));
								} else {
									out.println("PSH " + pa.encode(username + ": " + message));
								}
							}
						}
					}
				} else if (command.equals("EXT")) {
					out.println("EXT");
					break;
				} else {
					out.println("ERR " + pa.encode("Command not recognized."));
				}
			} else {
				break;
			}
		}

		System.out.println("User " + id + " " + username + " disconnected.");
		connectedUsers = -1;
		synchronized (this)

		{
			for (Connection c : server.connections) {
				if (c != null && c != this) {
					connectedUsers++;
				}
			}
		}

		synchronized (this) {
			for (Connection c : server.connections) {
				if (c != null && c != this) {
					c.getOutputStream().println("PSH " + c.encrypt(username + " has disconnected."));
					c.getOutputStream().println("PSH " + c.encrypt("There are now " + connectedUsers + " other connected users."));
				}
			}
		}

		out.println("PSH " + pa.encode("You were disconnected."));

		clearConnection();
	}

	public void addToConnections() {
		boolean foundSpot = false;
		synchronized(this) {
			for (int i = 0; i < server.connections.length; i++) {
				if (server.connections[i] == null) {
					server.connections[i] = this;
					foundSpot = true;
					break;
				}
			}
			server.waiting.remove(this);
			if (!foundSpot) {
				if (out != null) {
					if (!pa.hasKey()) out.println("PSH Server full, try again later.");
					else out.println("PSH " + pa.encode("Server full, try again later."));
					out.println("EXT");
				}
				clearConnection();
			}
		}
	}

	public void timedOut() {
		System.out.println("User " + id + " timed out.");
		if (out != null) {
			if (!pa.hasKey()) out.println("PSH You timed out.");
			else out.println("PSH " + pa.encode("You timed out."));
		}
		clearConnection();
	}

	public void clearConnection() {
		System.out.println("Clearing User " + id + "'s connection...");
		if (out != null) out.println("EXT");
		synchronized(this) {
			for (int i = 0; i < server.connections.length; i++) {
				if (server.connections[i] == this) {
					server.connections[i] = null;
					break;
				}
			}
		}
		closeConnection();
	}

	public void closeConnection() {
		out.close();
		try {
			in.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public PrintWriter getOutputStream() {
		return out;
	}

	public String getUsername() {
		return username;
	}

	public String encrypt(String message) {
		return pa.encode(message);
	}
}
