package client.gui.loading;

public class IncrementalTimer extends Thread {

	private int minMilli, maxMilli;
	private int maxPercentage;
	private KeyLoading kl;
	private boolean running;

	public IncrementalTimer(int minMilli, int maxMilli, int maxPercentage, KeyLoading kl) {
		this.minMilli = minMilli;
		this.maxMilli = maxMilli;
		this.maxPercentage = maxPercentage;
		this.kl = kl;
	}

	public void stopTimer() {
		running = false;
	}

	public void run() {
		running = true;
		while (running) {
			int r = randomNumber(minMilli, maxMilli);
			try {
				Thread.sleep(r);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
			if (kl.getBarPercentage() < maxPercentage) {
				kl.incrementPercentage((int)(Math.random()*3));
			} else return;
		}
	}

	private int randomNumber(int min, int max) {
		return (int)(Math.random() * (max-min)) + min;
	}
}
