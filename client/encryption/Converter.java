package client.encryption.encoding;

import java.math.BigInteger;

public class Converter {

	public static String encode(String s) {
		String alphabet = "aAbBcCdDeEfFgGhHiIjJkKlLmMnNoOpPqQrRsStTuUvVwWxXyYzZ 1234567890-=!@#$%^&*()_+[] {};':\",./<>?\\|`~";
		StringBuilder encoded = new StringBuilder(s.length()*2);
		for (char c : s.toCharArray()) {
			int i = alphabet.indexOf(c);
			if (i == -1) {
				System.out.println(c);
			}
			if (i < 10) {
				encoded.append("0" + i);
			} else {
				encoded.append("" + i);
			}
		}

		return encoded.toString();
	}

	public static String decode(BigInteger i) {
		String alphabet = "aAbBcCdDeEfFgGhHiIjJkKlLmMnNoOpPqQrRsStTuUvVwWxXyYzZ 1234567890-=!@#$%^&*()_+[] {};':\",./<>?\\|`~";

		String num = i.toString();
		if (num.length() % 2 != 0) {
			num = "0" + num;
		}
		int indices[] = new int[num.length()/2];

		for (int j=0;j<num.length();j+=2) {
			try {
				indices[j/2] = Integer.parseInt("" + num.charAt(j) + num.charAt(j+1));
			} catch (NumberFormatException e) {return "";}
			catch (Exception e) {break;}
		}

		StringBuilder decoded = new StringBuilder(indices.length);
		for (int index : indices) {
			decoded.append(alphabet.charAt(index));
		}

		return decoded.toString();
	}

	public static String decode(String i) {
		return decode(new BigInteger(i));
	}
}
