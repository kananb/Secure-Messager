package server;

public class Timeout extends Thread {

	private int timeout;
	private Connection c;
	private boolean running;

	public Timeout(int timeout, Connection c) {
		this.timeout = timeout;
		this.c = c;
	}

	public void stopTimer() {
		running = false;
	}

	public void run() {
		running = true;
		int currentTime = 0;
		int wait = 5;
		boolean done = false;
		System.out.println("Starting timer.");
		while (running) {
			try {
				Thread.sleep(wait);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
			currentTime += wait;
			if (currentTime >= timeout) {
				done = true;
				System.out.println("Timer finished.");
				break;
			}
		}
		if (done) {
			c.timedOut();
		}
	}
}
