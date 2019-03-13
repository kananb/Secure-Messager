package server;

public class Launcher {
	public static void main(String args[]) {
		int port = 12345;
		try {
			if (args.length >= 1) port = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			System.err.printf("The port must be a number.\nThe string \"%s\" is invalid.", args[0]);
			System.exit(1);
		}
		new Server(port).listen();
	}
}
