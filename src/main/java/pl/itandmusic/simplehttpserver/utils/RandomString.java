package pl.itandmusic.simplehttpserver.utils;

import java.util.Random;

public class RandomString {

	private static final int DEFAULT_LENGTH = 10;
	private static final Random DEFAULT_RANDOM = new Random();
	private static final String UPPERCASE_LETTERS = "ABCDEFGHIJKLMNOPRSTUWZ";
	private static final String LOWERCASE_LETTERS = UPPERCASE_LETTERS.toLowerCase();
	private static final String DIGITS = "123456789";
	private static final String ALL = UPPERCASE_LETTERS + LOWERCASE_LETTERS + DIGITS;
	private static final int allLength = ALL.length();

	private RandomString() {
		throw new RuntimeException("Consturctor call exception");
	}

	public static String generate() {
		return generate(DEFAULT_LENGTH);
	}

	public static String generate(int length) {
		StringBuilder stringBuilder = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			int index = DEFAULT_RANDOM.nextInt(allLength);
			char c = ALL.charAt(index);
			stringBuilder.append(c);
		}

		return stringBuilder.toString();
	}

}
