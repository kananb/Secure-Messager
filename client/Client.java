package client;

import java.io.File;

import client.gui.GUI;
import client.encryption.polyalphabetic.Polyalphabetic;
import client.encryption.rsa.RSA;

public class Client {

	private GUI gui;
	private StreamHandler sh;
	private RSA rsa;
	private Polyalphabetic pa;

	public Client() {
		gui = new GUI(this);
		sh = new StreamHandler(this, gui);

		gui.setStreamHandler(sh);
		gui.displayStartupMessage();

		File savedir = new File("C:/Users/" + System.getProperty("user.name") + "/Desktop/SDMPT-resources/");

		rsa = new RSA(RSA.SECURE_KEY_SIZE);
		int result = rsa.recoverKeys(savedir, RSA.DEFAULT_FILE_NAME);
		if (result != 0) {
			rsa.generateComponents();
			rsa.saveKeys(savedir, RSA.DEFAULT_FILE_NAME);
		}
		gui.frame.setVisible(true);

		pa = new Polyalphabetic();
	}

	public String getN() {
		return rsa.getN();
	}

	public String getE() {
		return rsa.getE();
	}

	public String encryptRSA(String message) {
		return rsa.encrypt(message);
	}

	public String decryptRSA(String message) {
		return rsa.decrypt(message);
	}

	public void setPAKey(String key) {
		pa.setKey(key);
	}

	public String encode(String message) {
		if (pa.hasKey()) {
			return pa.encode(message);
		} else {
			return null;
		}
	}

	public String decode(String message) {
		if (pa.hasKey()) {
			return pa.decode(message);
		} else {
			return null;
		}
	}

	public Polyalphabetic getPA() {
		return pa;
	}
}
