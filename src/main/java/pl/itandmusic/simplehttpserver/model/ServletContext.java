package pl.itandmusic.simplehttpserver.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import pl.itandmusic.simplehttpserver.configuration.Configuration;

public class ServletContext implements javax.servlet.ServletContext {

	private List<ServletConfig> servletConfigs = new ArrayList<>();
	private List<String> defaultPages;
	private String servletContextName;
	private String appPath;

	public List<ServletConfig> getServletConfigs() {
		return servletConfigs;
	}

	public List<String> getDefaultPages() {
		return defaultPages;
	}

	public void setDefaultPages(List<String> defaultPages) {
		this.defaultPages = defaultPages;
	}

	public String getServletContextName() {
		return servletContextName;
	}

	public void setServletContextName(String servletContextName) {
		this.servletContextName = servletContextName;
	}

	public String getAppPath() {
		return appPath;
	}

	public void setAppPath(String appPath) {
		this.appPath = appPath;
	}

	@Override
	public String getContextPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public javax.servlet.ServletContext getContext(String uripath) {
		return Configuration.applications.get(uripath);
	}

	@Override
	public int getMajorVersion() {
		return Configuration.SERVLET_MAJOR_VERSION;
	}

	@Override
	public int getMinorVersion() {
		return Configuration.SERVLET_MINOR_VERSION;
	}

	@Override
	public String getMimeType(String file) {
		try {
			Path path = Paths.get(file);
			return Files.probeContentType(path);
		} catch (IOException exception) {
			return "";
		}
	}

	@Override
	public Set getResourcePaths(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URL getResource(String path) throws MalformedURLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getResourceAsStream(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RequestDispatcher getNamedDispatcher(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Servlet getServlet(String name) throws ServletException {
		Servlet servlet = null;
		for (ServletConfig sc : servletConfigs) {
			servlet = sc.getServlets().get(name);
			if (servlet != null) {
				return servlet;
			}
		}
		return tryToInitServlet(name);

	}

	private Servlet tryToInitServlet(String name) {
		try {
			return initServlet(name);
		} catch (InstantiationException | IllegalAccessException | ServletException e) {
			e.printStackTrace();
			return null;
		}
	}

	private Servlet initServlet(String name) throws InstantiationException, IllegalAccessException, ServletException {
		for (ServletConfig sc : servletConfigs) {
			Class<?> clazz = sc.getServletMappings().get(name);
			if (clazz != null) {
				Object object = clazz.newInstance();
				return Servlet.class.cast(object);
			}
		}
		throw new ServletException("Servlet does not exists");
	}

	@Override
	public Enumeration getServlets() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration getServletNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void log(String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void log(Exception exception, String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void log(String message, Throwable throwable) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getRealPath(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServerInfo() {
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

	@Override
	public Object getAttribute(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration getAttributeNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAttribute(String name, Object object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeAttribute(String name) {
		// TODO Auto-generated method stub

	}

}
