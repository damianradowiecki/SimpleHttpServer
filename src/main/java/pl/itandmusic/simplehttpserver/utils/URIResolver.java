package pl.itandmusic.simplehttpserver.utils;

import pl.itandmusic.simplehttpserver.configuration.Configuration;
import pl.itandmusic.simplehttpserver.model.HttpServletRequestImpl;

public class URIResolver {

	public static boolean requestForDeafultPage(HttpServletRequestImpl request) {
		String requestURI = getRequsetURI(request);
		return requestURI.equals("/") || requestURI.equals("\\");
	}

	public static boolean requestForServerInfoPage(HttpServletRequestImpl request) {
		String requestURI = getRequsetURI(request);
		return requestURI.equals("/server-info") || requestURI.equals("/server-info/");
	}

	public static boolean requestForResourceOnServer(HttpServletRequestImpl request) {
		String requestURI = getRequsetURI(request);
		Class<?> servletClass = Configuration.servletsMappings.get(requestURI);
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
