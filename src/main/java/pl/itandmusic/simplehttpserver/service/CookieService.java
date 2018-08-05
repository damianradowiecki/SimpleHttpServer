package pl.itandmusic.simplehttpserver.service;

import java.util.Optional;

import javax.servlet.http.Cookie;

import pl.itandmusic.simplehttpserver.utils.CookieConverter;

public class CookieService {

	public static Optional<Cookie> getSessionIdCookie(Cookie[] cookies){
		return CookieConverter
				.convertToList(cookies)
				.stream()
				.filter(c -> c.getName().equalsIgnoreCase("JSESSIONID"))
				.findFirst();
	}
	
	public static Optional<String> getSessionId(Cookie[] cookies){
		return CookieConverter
				.convertToList(cookies)
				.stream()
				.filter(c -> c.getName().equalsIgnoreCase("JSESSIONID"))
				.map(c -> c.getValue())
				.findFirst();
	}
}
