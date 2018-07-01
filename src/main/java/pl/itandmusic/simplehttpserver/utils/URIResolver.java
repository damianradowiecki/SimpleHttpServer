package pl.itandmusic.simplehttpserver.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.itandmusic.simplehttpserver.configuration.Configuration;
import pl.itandmusic.simplehttpserver.model.HttpServletRequestImpl;

public class URIResolver {
	
	private static Pattern pattern;
	private static Matcher matcher;

	public static boolean serverInfoRequest(HttpServletRequestImpl request) {
		String requestURI = getRequsetURI(request);
		return requestURI.equals("/") || requestURI.equals("\\");
	}
	
	public static boolean defaultAppPageRequest(HttpServletRequestImpl request) {
		String requestURI = getRequsetURI(request);
		pattern = Pattern.compile("^/.*?/$");
		matcher = pattern.matcher(requestURI);
		if(matcher.matches()) {
			return true;
		}
		else {
			pattern = Pattern.compile("^/.*?/.*$");
			matcher = pattern.matcher(requestURI);
			return !matcher.matches();
		}
	}

	public static boolean requestForResourceOnServer(HttpServletRequestImpl request) {
		String requestURI = getRequsetURI(request);
		Class<?> servletClass = null; //Configuration.servletsMappings.get(requestURI);
		if (servletClass == null) {
			return false;
		} else {
			return true;
		}
	}

	public static String getRequsetURI(HttpServletRequestImpl request) {
		return request.getRequestURI().toString();
	}
}
