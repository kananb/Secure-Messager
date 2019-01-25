package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class Server {

	public static final int MAX_CONNECTIONS = 5;

	private int port;
	private ServerSocket server;
	private Socket client;

	public Connection[] connections;
	public ArrayList<Connection> waiting;

	public Server(int port) {

		this.port = port;
		System.out.println("Opening server socket on port " + port + "...");
		try {
			server = new ServerSocket(this.port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Socket opened.");
		connections = new Connection[MAX_CONNECTIONS];
		waiting = new ArrayList<Connection>();
	}

	public void listen() {
		int count;
		System.out.println("Waiting for connections...");
		while (true) {
			client = null;
			try {
				client = server.accept();
			} catch (SocketTimeoutException e) {
				System.out.println("Socket timed out...");
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (client != null && client.isBound()) {
				for (count = 0;count < MAX_CONNECTIONS; count++) {
					if (connections[count] == null) {
						waiting.add(new Connection(client, this, count));
						waiting.get(waiting.size()-1).start();
						break;
					}
				}
				if (count >= MAX_CONNECTIONS) {
					try {
						PrintWriter out = new PrintWriter(client.getOutputStream(), true);
						out.println("PSH Server full, try again later.");
						out.println("EXT");
						out.close();
						client.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
