@XmlSchema(namespace = "http://java.sun.com/xml/ns/javaee", elementFormDefault = XmlNsForm.QUALIFIED, xmlns = {
		@XmlNs(prefix = "xsi", namespaceURI = "http://www.w3.org/2001/XMLSchema-instance") })
package pl.itandmusic.simplehttpserver.configuration.web;

import java.util.List;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "web-app")
@XmlAccessorType(XmlAccessType.FIELD)
class WebApp {

	@XmlAttribute(name = "id")
	private String id;

	@XmlAttribute(name = "version")
	private String version;

	@XmlElement(name = "display-name")
	private String displayName;
	@XmlElement(name = "servlet")
	private Servlet servlet;
	@XmlElement(name = "servlet-mapping")
	private ServletMapping servletMapping;

	@XmlElementWrapper(name = "welcome-file-list")
	@XmlElement(name = "welcome-file")
	private List<String> welcomeFiles;

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public List<String> getWelcomeFiles() {
		return welcomeFiles;
	}

	public void setWelcomeFiles(List<String> welcomeFiles) {
		this.welcomeFiles = welcomeFiles;
	}

	public Servlet getServlet() {
		return servlet;
	}

	public void setServlet(Servlet servlet) {
		this.servlet = servlet;
	}

	public ServletMapping getServletMapping() {
		return servletMapping;
	}

	public void setServletMapping(ServletMapping servletMapping) {
		this.servletMapping = servletMapping;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
