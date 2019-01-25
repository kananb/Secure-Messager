package client.encryption.polyalphabetic;

public class Polyalphabetic {

	public static final String ENCODING_ALPHABET = "aAbBcCdDeEfFgGhHiIjJkKlLmMnNoOpPqQrRsStTuUvVwWxXyYzZ 1234567890-=!@#$%^&*()_+[]{};':\",./<>?\\|`~";

	private String key;

	public Polyalphabetic() {}
	public Polyalphabetic(String key) {
		this.key = key;
	}

	public String encode(String message) {
		if (key == null || key.equals("")) return null;
		String thisKey = extended(message.length());

		StringBuilder encoded = new StringBuilder(message.length());
		for (int i=0;i<message.length();i++) {
			int mIndex = ENCODING_ALPHABET.indexOf(message.charAt(i));
			int kIndex = ENCODING_ALPHABET.indexOf(thisKey.charAt(i));
			int index = (mIndex + kIndex) % ENCODING_ALPHABET.length();
			if (index < 0) {
				System.out.println(mIndex + "; " + message.charAt(i));
			}
			encoded.append(ENCODING_ALPHABET.charAt(index));
		}

		return encoded.toString();
	}

	public String decode(String message) {
		if (key == null || key.equals("")) return null;
		String thisKey = extended(message.length());

		StringBuilder decoded = new StringBuilder(message.length());
		for (int i=0;i<message.length();i++) {
			int mIndex = ENCODING_ALPHABET.indexOf(message.charAt(i));
			int kIndex = ENCODING_ALPHABET.indexOf(thisKey.charAt(i));
			int index = mIndex - kIndex;
			if (index < 0) index = ENCODING_ALPHABET.length() + index;
			if (index >= ENCODING_ALPHABET.length()) return null;

			decoded.append(ENCODING_ALPHABET.charAt(index));
		}

		return decoded.toString();
	}

	private String extended(int length) {
		StringBuilder extendedKey = new StringBuilder();
		while (extendedKey.length() < length) {
			extendedKey.append(key);
		}
		return extendedKey.toString();
	}

	public static String generateKey(int keySize) {
		StringBuilder key = new StringBuilder(keySize);
		int randIndex;
		for (int i=0;i<keySize;i++) {
			randIndex = (int)(Math.random() * (ENCODING_ALPHABET.length() - 1));
			key.append(ENCODING_ALPHABET.charAt(randIndex));
		}
		return key.toString();
	}

	public boolean hasKey() {
		return key != null;
	}

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
}
