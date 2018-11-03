package pl.itandmusic.simplehttpserver.model;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;

public class ServletConfig implements javax.servlet.ServletConfig {

	private Servlet servlet;
	private String servletName;
	private String servletClass;
	private Map<String, Class<? extends Servlet>> servletMappings = new HashMap<>();
	private Map<String, String> initParams = new HashMap<>();
	private ServletContext servletContext;

	
	public ServletConfig(ServletContext servletContext) {
		this.servletContext = Objects.requireNonNull(servletContext);
	}
	
	public Map<String, String> getInitParams(){
		return this.initParams;
	}
	
	public Servlet getServlet() {
		return servlet;
	}

	public void setServlet(Servlet servlet) {
		this.servlet = servlet;
	}

	public String getServletClass() {
		return servletClass;
	}

	public void setServletClass(String servletClass) {
		this.servletClass = servletClass;
	}

	public Map<String, Class<? extends Servlet>> getServletMappings() {
		return servletMappings;
	}

	public void setServletMappings(Map<String, Class<? extends Servlet>> servletMappings) {
		this.servletMappings = servletMappings;
	}

	public void setServletName(String servletName) {
		this.servletName = servletName;
	}

	@Override
	public String getServletName() {
		return servletName;
	}

	@Override
	public ServletContext getServletContext() {
		return servletContext;
	}

	@Override
	public String getInitParameter(String name) {
		return initParams.get(name);
	}

	@Override
	public Enumeration<String> getInitParameterNames() {
		return new EnumerationImpl<String>(initParams.keySet());
	}

}
