package stepbystep.careful.util;

public class MyConvert {

	private final static String BASE64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
	private final static char[] CHAR_BASE64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
	        .toCharArray();

	public static int convertBase64ToDecimal(String strBase64) {
		int length = strBase64.length();
		int decimal = 0;

		for (char chr : strBase64.toCharArray()) {
			decimal += BASE64.indexOf(chr) * (int) Math.pow(64, length - 1);
			length--;
		}
		return decimal;
	}

	public static String convertDecimalToBase64(long value) {
		int convert = (int) value;

		StringBuilder result = new StringBuilder();
		
		do {
			result.append(CHAR_BASE64[convert % 64]);
		} while ((convert = (convert / 64)) > 0);
		
		result.reverse();
		return new String(result);
	}

}
