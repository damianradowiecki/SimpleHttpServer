package pl.itandmusic.simplehttpserver.configuration.web;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "servlet-mapping")
public class ServletMapping {

	private String servletName;
	private String urlPattern;

	@XmlElement(name = "servlet-name")
	public String getServletName() {
		return servletName;
	}

	public void setServletName(String servletName) {
		this.servletName = servletName;
	}

	@XmlElement(name = "url-pattern")
	public String getUrlPattern() {
		return urlPattern;
	}

	public void setUrlPattern(String urlPattern) {
		this.urlPattern = urlPattern;
	}

}
