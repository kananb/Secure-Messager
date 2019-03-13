package server.encryption;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

public class RSA {

	public static final int SECURE_KEY_SIZE = 4096;
	public static final String DEFAULT_FILE_NAME = "rsakeys.txt";
	public static final String LOCAL_DIR = "LOCAL";


	public static String encrypt(String message, String e, String n) throws NumberFormatException {
		return encrypt(message, new BigInteger(e), new BigInteger(n));
	}
	public static String encrypt(String message, BigInteger e, BigInteger n) throws NumberFormatException {
		BigInteger m = new BigInteger(message);
		return m.modPow(e, n).toString();
	}

	public static String decrypt(String message, String d, String n) throws NumberFormatException {
		return decrypt(message, new BigInteger(d), new BigInteger(n));
	}
	public static String decrypt(String message, BigInteger d, BigInteger n) throws NumberFormatException {
		BigInteger m = new BigInteger(message);
		return m.modPow(d, n).toString();
	}


	public static boolean isValidKey(String key) {
		try {
			new BigInteger(key);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
