package pl.itandmusic.simplehttpserver.model;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;

public class ServletConfig implements javax.servlet.ServletConfig {

	private Map<String, Class<? extends Servlet>> servletMappings = new HashMap<>();
	private Map<String, ? extends Servlet> servlets = new HashMap<>();

	public Map<String, Class<? extends Servlet>> getServletMappings() {
		return servletMappings;
	}

	public void setServletMappings(Map<String, Class<? extends Servlet>> servletMappings) {
		this.servletMappings = servletMappings;
	}

	public Map<String, ? extends Servlet> getServlets() {
		return servlets;
	}

	public void setServlets(Map<String, ? extends Servlet> servlets) {
		this.servlets = servlets;
	}

	@Override
	public String getServletName() {
		// TODO Auto-generated method stub
		return null;
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
