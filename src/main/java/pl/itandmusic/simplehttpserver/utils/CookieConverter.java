package pl.itandmusic.simplehttpserver.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.Cookie;

import pl.itandmusic.simplehttpserver.model.CookieImpl;

public class CookieConverter {

	private static final String SEPARATOR = ";";
	private static final String NAME_VALUE_SEPARATOR = "=";

	public static List<Cookie> convertToList(String cookieHeader) {
		List<Cookie> cookies = new ArrayList<>();
		for (String s : cookieHeader.split(SEPARATOR)) {
			String[] nameValuePair = s.trim().split(NAME_VALUE_SEPARATOR);
			if (nameValuePair.length == 2) {
				cookies.add(new CookieImpl(nameValuePair[0], nameValuePair[1]));
			}
		}

		return cookies;
	}
	
	public static List<Cookie> convertToList(Cookie[] cookies){
		return Arrays.asList(cookies);
 	}

	public static Cookie[] convertToArray(String cookieHeader) {
		return convertToList(cookieHeader).toArray(new Cookie[0]);
	}
}
