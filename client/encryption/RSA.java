package client.encryption;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

import client.gui.loading.IncrementalTimer;
import client.gui.loading.KeyLoading;

public class RSA {

	public static final int SECURE_KEY_SIZE = 4096;
	public static final String DEFAULT_FILE_NAME = "rsakeys.txt";
	public static final String LOCAL_DIR = "LOCAL";

	private int keySize;
	private BigInteger p, q;
	private BigInteger n, phi;
	private BigInteger e, d;

	public RSA(int keySize) {
		this.keySize = keySize;
	}

	public void generateComponents() {
		Random r = new Random();
		IncrementalTimer timer;
		KeyLoading kl = new KeyLoading();

		kl.setLabel("Generating prime numbers (1/2)...");
		kl.setBarPercentage(0);
		timer = new IncrementalTimer(50, 400, 15, kl);
		timer.start();
		p = new BigInteger(keySize/2, 100, r);
		timer.stopTimer();
		kl.setBarPercentage(15);

		kl.setLabel("Generating prime numbers (2/2)...");
		timer = new IncrementalTimer(50, 400, 30, kl);
		timer.start();
		q = new BigInteger(keySize/2, 100, r);
		timer.stopTimer();
		kl.setBarPercentage(30);

		n = p.multiply(q);
		phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

		kl.setLabel("Generating public key...");
		timer = new IncrementalTimer(100, 600, 98, kl);
		timer.start();
		do {
			e = new BigInteger(phi.bitLength(), 20, r);
		} while (phi.gcd(e) != BigInteger.ONE && e.compareTo(BigInteger.ONE) <= 0 && e.compareTo(phi) >= 0);
		timer.stopTimer();
		kl.setBarPercentage(65);

		kl.setLabel("Generating private key...");
		timer = new IncrementalTimer(0, 50, 100, kl);
		timer.start();
		d = e.modInverse(phi);
		timer.stopTimer();
		kl.setBarPercentage(100);
		kl.setLabel("Complete.");
		try {
			Thread.sleep(250);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		kl.dispose();
	}

	
	public String encrypt(String message) throws NumberFormatException {
		BigInteger m = new BigInteger(message);
		return m.modPow(e, n).toString();
	}

	public String decrypt(String message) throws NumberFormatException {
		BigInteger m = new BigInteger(message);
		return m.modPow(d, n).toString();
	}

	public int saveKeys(File path, String filename) {
		FileWriter fw = null;
		path.mkdirs();
		try {
			fw = new FileWriter(new File(path, filename), false);
		} catch (IOException e) {
			e.printStackTrace();
			return 1;
		}
		try {
			fw.write(keySize + "\n");
			fw.write(e.toString() + "\n");
			fw.write(n.toString() + "\n");
			fw.write(d.toString());
		} catch (IOException e) {
			e.printStackTrace();
			try {
				fw.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return 1;
		}
		try {
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
			return 1;
		}
		return 0;
	}

	public int recoverKeys(File path, String filename) {
		FileReader fr = null;
		try {
			fr = new FileReader(new File(path, filename));
		} catch (IOException e) {
			e.printStackTrace();
			return 1;
		}
		BufferedReader br = new BufferedReader(fr);
		try {
			int keySize = Integer.parseInt(br.readLine());
			if (this.keySize != keySize) {
				try {
					br.close();
					fr.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return 1;
			}
			e = new BigInteger(br.readLine());
			n = new BigInteger(br.readLine());
			d = new BigInteger(br.readLine());
		} catch (IOException | NumberFormatException e) {
			e.printStackTrace();
			try {
				br.close();
				fr.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return 1;
		}
		try {
			br.close();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
			return 1;
		}
		return 0;
	}


	public String[] getPublicKeyPair() {
		return new String[] {e.toString(), n.toString()};
	}

	public String[] getPrivateKeyPair() {
		return new String[] {d.toString(), n.toString()};
	}

	public String getN() {
		return n.toString();
	}
	public String getE() {
		return e.toString();
	}
	public String getD() {
		return d.toString();
	}
}
