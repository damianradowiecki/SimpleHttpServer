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
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import pl.itandmusic.simplehttpserver.configuration.Configuration;
import pl.itandmusic.simplehttpserver.logger.LogLevel;
import pl.itandmusic.simplehttpserver.logger.Logger;

public class ServletContext implements javax.servlet.ServletContext {

	private Logger logger = Logger.getLogger(ServletContext.class);
	private List<ServletConfig> servletConfigs = new ArrayList<>();
	private List<String> defaultPages = new ArrayList<>();
	private String servletContextName;
	private String appPath;
	private Map<String, String> initParams = new HashMap<>();
	private List<Class<? extends EventListener>> listeners = new ArrayList<>();

	public List<Class<? extends EventListener>> getListeners() {
		return listeners;
	}

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
		for (ServletConfig sc : servletConfigs) {
			if (sc.getServletName().equals(name)) {
				if (sc.getServlet() != null) {
					return sc.getServlet();
				} else {
					tryToInitServlet(sc);
					return getServlet(name);
				}
			}
		}
		throw new ServletException("Servlet not found");
	}

	public Servlet getServletByUrlPattern(String urlPattern) throws ServletException {
		for (ServletConfig sc : servletConfigs) {
			if (sc.getServletMappings().containsKey(urlPattern)) {
				return getServlet(sc.getServletName());
			}
		}
		throw new ServletException("Servlet not found");
	}

	private void tryToInitServlet(ServletConfig servletConfig) {
		try {
			initServlet(servletConfig);
		} catch (InstantiationException | IllegalAccessException | ServletException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void initServlet(ServletConfig servletConfig)
			throws InstantiationException, IllegalAccessException, ServletException, ClassNotFoundException {
		Class<?> clazz = Class.forName(servletConfig.getServletClass());
		Object object = clazz.newInstance();
		Servlet servlet = Servlet.class.cast(object);
		servlet.init(servletConfig);
		servletConfig.setServlet(servlet);
	}

	@Override
	public Enumeration<Servlet> getServlets() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration<Servlet> getServletNames() {
		// TODO
		return null;
	}

	@Override
	public void log(String msg) {
		logger.log(msg, LogLevel.INFO);
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
		return Configuration.SERVER_INFO;
	}

	@Override
	public String getInitParameter(String name) {
		return initParams.get(name);
	}

	@Override
	public Enumeration<String> getInitParameterNames() {
		return new EnumerationImpl<String>(initParams.keySet());
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
