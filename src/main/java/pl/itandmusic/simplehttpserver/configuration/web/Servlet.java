package pl.itandmusic.simplehttpserver.configuration.web;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "servlet")
public class Servlet {

	private String servletName;
	private String servletClass;
	private List<Param> initParams = new ArrayList<>();

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

	@XmlElement(name = "init-param")
	public List<Param> getInitParams() {
		return initParams;
	}

	public void setInitParams(List<Param> initParams) {
		this.initParams = initParams;
	}
	
	

}
