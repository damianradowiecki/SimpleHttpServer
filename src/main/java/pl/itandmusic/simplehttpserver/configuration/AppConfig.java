package pl.itandmusic.simplehttpserver.configuration;

import java.util.List;
import java.util.Map;

public class AppConfig {

	private Map<String, Class<?>> servletsMappings;
	private List<String> defaultPages;
	private String appName;

	public Map<String, Class<?>> getServletsMappings() {
		return servletsMappings;
	}

	public void setServletsMappings(Map<String, Class<?>> servletsMappings) {
		this.servletsMappings = servletsMappings;
	}

	public List<String> getDefaultPages() {
		return defaultPages;
	}

	public void setDefaultPages(List<String> defaultPages) {
		this.defaultPages = defaultPages;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

}
