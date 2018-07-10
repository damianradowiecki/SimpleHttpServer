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
	private List<Servlet> servlets;
	@XmlElement(name = "servlet-mapping")
	private List<ServletMapping> servletMappings;

	@XmlElement(name = "context-param")
	private List<ContextParam> contextParams;

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

	public List<Servlet> getServlets() {
		return servlets;
	}

	public void setServlets(List<Servlet> servlets) {
		this.servlets = servlets;
	}

	public List<ServletMapping> getServletMappings() {
		return servletMappings;
	}

	public void setServletMappings(List<ServletMapping> servletMappings) {
		this.servletMappings = servletMappings;
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

	public List<ContextParam> getContextParams() {
		return contextParams;
	}

	public void setContextParams(List<ContextParam> contextParams) {
		this.contextParams = contextParams;
	}

}
