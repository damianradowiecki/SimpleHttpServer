package pl.itandmusic.simplehttpserver.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration {
	
	public static String appName;
	public static int port;
	public static String appDirectory;
	public static Map<String, Class<?>> servletsMappings = new HashMap<>();
	public static List<String> defaultPages = new ArrayList<>();
}
