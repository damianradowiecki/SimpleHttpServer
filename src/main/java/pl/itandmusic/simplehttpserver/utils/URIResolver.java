package pl.itandmusic.simplehttpserver.utils;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.itandmusic.simplehttpserver.configuration.Configuration;
import pl.itandmusic.simplehttpserver.model.HttpServletRequestImpl;
import pl.itandmusic.simplehttpserver.model.ServletContext;

public class URIResolver {

	private static Pattern pattern;
	private static Matcher matcher;
	
	private URIResolver() {
		throw new RuntimeException("Consturctor call exception");
	}

	public static boolean serverInfoRequest(String requestURI) {
		return requestURI.equals("/") || requestURI.equals("\\");
	}
	
	public static boolean serverInfoRequest(HttpServletRequestImpl request) {
		String requestURI = getRequestURI(request);
		return serverInfoRequest(requestURI.toString());
	}
	
	public static boolean serverInfoRequest(URI requestURI) {
		return serverInfoRequest(requestURI.toString());
	}

	public static boolean defaultAppPageRequest(String requestURI) {
		pattern = Pattern.compile("^/.*?/$");
		matcher = pattern.matcher(requestURI);
		if (matcher.matches()) {
			return true;
		} else {
			pattern = Pattern.compile("^/.*?/.*$");
			matcher = pattern.matcher(requestURI);
			return !matcher.matches();
		}
	}
	
	public static boolean defaultAppPageRequest(URI requestURI) {
		return defaultAppPageRequest(requestURI.toString());
	}
	
	public static boolean defaultAppPageRequest(HttpServletRequestImpl request) {
		String requestURI = getRequestURI(request);
		return defaultAppPageRequest(requestURI.toString());
	}

	public static boolean properDefaultAppPageRequest(HttpServletRequestImpl request) {
		String requestURI = getRequestURI(request);
		pattern = Pattern.compile("^/.*?/$");
		matcher = pattern.matcher(requestURI);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}

	public static String getRequestURI(HttpServletRequestImpl request) {
		return request.getRequestURI().toString();
	}

	public static boolean anyAppRequest(HttpServletRequestImpl request) {
		String requestURI = getRequestURI(request);
		return anyAppRequest(requestURI) != null;
	}
	
	public static ServletContext anyAppRequest(String requestURI) {
		for (ServletContext sc : Configuration.applications.values()) {
			String appRequestPrefix = "/" + sc.getServletContextName();
			if(requestURI.startsWith(appRequestPrefix)) {
				return sc;
			}
		}
		return null;
	}
	
	public static ServletContext anyAppRequest(URI requestURI) {
		return anyAppRequest(requestURI.toString());
	}
	
}
