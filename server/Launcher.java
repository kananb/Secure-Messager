package server;

public class Launcher {
	public static void main(String args[]) {
		new Server(12345).listen();
	}
}
