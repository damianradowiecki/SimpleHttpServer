package pl.itandmusic.simplehttpserver.model;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

public class ServletConfig implements javax.servlet.ServletConfig {

	private String servletName;
	private String servletClass;
	private Map<String, Class<?>> servletMappings = new HashMap<>();

	public String getServletClass() {
		return servletClass;
	}

	public void setServletClass(String servletClass) {
		this.servletClass = servletClass;
	}

	public Map<String, Class<?>> getServletMappings() {
		return servletMappings;
	}

	public void setServletMappings(Map<String, Class<?>> servletMappings) {
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInitParameter(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration getInitParameterNames() {
		// TODO Auto-generated method stub
		return null;
	}

}
