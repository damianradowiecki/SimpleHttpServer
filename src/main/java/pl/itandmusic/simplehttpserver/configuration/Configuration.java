package pl.itandmusic.simplehttpserver.configuration;

import java.util.HashMap;
import java.util.Map;

import pl.itandmusic.simplehttpserver.model.ServletContext;

public class Configuration {
	
	public final static Map<String, ServletContext> applications = new HashMap<>();
	public final static String SERVER_INFO = "Mini HTTP Server (has servlets compability)";
	public static int port;
	public static String appsDirectory;
	public static final int SERVLET_MAJOR_VERSION = 2;
	public static final int SERVLET_MINOR_VERSION = 1;
	
}
