package pl.itandmusic.simplehttpserver.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.Cookie;

import pl.itandmusic.simplehttpserver.model.CookieImpl;
import pl.itandmusic.simplehttpserver.model.Pair;

public class CookieConverter {

	private static final String COOKIE_HEADER_NAME = "Set-Cookie";
	private static final String SEPARATOR = ";";
	private static final String NAME_VALUE_SEPARATOR = "=";

	private CookieConverter() {
		throw new RuntimeException("Consturctor call exception");
	}
	
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

	public static List<Cookie> convertToList(Cookie[] cookies) {
		return Arrays.asList(cookies);
	}

	public static Cookie[] convertToArray(String cookieHeader) {
		return convertToList(cookieHeader).toArray(new Cookie[0]);
	}

	public static Pair<String, String> convertToKeyValuePair(Cookie cookie) {
		
		List<String> cookieValues = new ArrayList<>();
		
		Optional<String> optionalName = Optional.ofNullable(cookie.getName());
		Optional<String> optionalValue = Optional.ofNullable(cookie.getValue());
		
		if(optionalName.isPresent() && optionalValue.isPresent()) {
			cookieValues.add(optionalName.get() + NAME_VALUE_SEPARATOR + optionalValue.get());
		}
		
		Optional<String> optionalDomain = Optional.ofNullable(cookie.getDomain());
		
		if(optionalDomain.isPresent()) {
			cookieValues.add(CookieHeader.DOMAIN.getName() + NAME_VALUE_SEPARATOR + optionalDomain.get());
		}
		
		Optional<Integer> optionalMaxAge = Optional.ofNullable(cookie.getMaxAge());
		
		if(optionalMaxAge.isPresent()) {
			cookieValues.add(CookieHeader.MAX_AGE.getName() + NAME_VALUE_SEPARATOR + optionalMaxAge.get());
		}
		
		Optional<String> optionalPath = Optional.ofNullable(cookie.getPath());
		
		if(optionalPath.isPresent()) {
			cookieValues.add(CookieHeader.PATH.getName() + NAME_VALUE_SEPARATOR + optionalPath.get());
		}
		
		Optional<String> optionalComment = Optional.ofNullable(cookie.getComment());
		
		if(optionalComment.isPresent()) {
			cookieValues.add(CookieHeader.COMMENT.getName() + NAME_VALUE_SEPARATOR + optionalComment.get());
		}
		
		Optional<Boolean> optionalSecure = Optional.ofNullable(cookie.getSecure());
		
		if(optionalSecure.isPresent()) {
			cookieValues.add(CookieHeader.SECURE.getName() + NAME_VALUE_SEPARATOR + optionalSecure.get());
		}
		
		Optional<Integer> optionalVersion = Optional.ofNullable(cookie.getVersion());
		
		if(optionalVersion.isPresent()) {
			cookieValues.add(CookieHeader.VERSION.getName() + NAME_VALUE_SEPARATOR + optionalVersion.get());
		}
		
		String value = String.join(SEPARATOR, cookieValues.toArray(new String[0]));
		
		return new Pair<String, String>(COOKIE_HEADER_NAME, value);
	}
}

enum CookieHeader{
	DOMAIN("Domain"),
	MAX_AGE("Max-Age"),
	PATH("Path"),
	COMMENT("Comment"),
	SECURE("Secure"),
	VERSION("Version");
	
	private String name;
	
	private CookieHeader(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}
