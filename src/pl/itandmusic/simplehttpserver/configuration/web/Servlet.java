package pl.itandmusic.simplehttpserver.configuration.web;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "servlet")
public class Servlet {

	private String servletName;
	private String servletClass;

	@XmlElement(name = "servlet-name")
	public String getServletName() {
		return servletName;
	}

	public void setServletName(String servletName) {
		this.servletName = servletName;
	}

	@XmlElement(name = "servlet-class")
	public String getServletClass() {
		return servletClass;
	}

	public void setServletClass(String servletClass) {
		this.servletClass = servletClass;
	}

}
