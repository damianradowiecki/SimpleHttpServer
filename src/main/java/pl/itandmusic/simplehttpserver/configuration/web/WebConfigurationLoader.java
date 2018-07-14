package pl.itandmusic.simplehttpserver.configuration.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import pl.itandmusic.simplehttpserver.configuration.Configuration;
import pl.itandmusic.simplehttpserver.logger.Logger;
import pl.itandmusic.simplehttpserver.model.ServletConfig;
import pl.itandmusic.simplehttpserver.model.ServletContext;

public class WebConfigurationLoader {

	private static final Logger logger = Logger.getLogger(WebConfigurationLoader.class);
	private static List<String> appFolderNames = new ArrayList<>();

	public static void load()
			throws IOException, ClassNotFoundException, JAXBException, InstantiationException, IllegalAccessException {

		logger.info("Web configuration loading.");

		loadAllAppFolderNames();

		loadConfigurationForEveryApp();

		logger.info("Web configuration loaded.");

	}

	private static void loadAllAppFolderNames() throws IOException {
		DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(Configuration.appsDirectory));
		for (Path path : directoryStream) {
			if (Files.isDirectory(path)) {
				String folderName = path.getFileName().toString();
				appFolderNames.add(folderName);
			}
		}

	}

	private static void loadConfigurationForEveryApp() throws MalformedURLException, ClassNotFoundException,
			FileNotFoundException, JAXBException, InstantiationException, IllegalAccessException {

		for (String fn : appFolderNames) {
			loadAppConfiguration(fn);
		}

	}

	private static void loadAppConfiguration(String appFolderName) throws MalformedURLException, ClassNotFoundException,
			FileNotFoundException, JAXBException, InstantiationException, IllegalAccessException {

		ServletContext servletContext = new ServletContext();

		WebApp webApp = unmarshalWebXml(appFolderName);

		setAppName(servletContext, webApp, appFolderName);

		setDefaultAppPages(servletContext, webApp);

		setContextParams(servletContext, webApp);

		setListeners(servletContext, webApp);

		setServlets(servletContext, webApp);

		setServletMappings(servletContext, webApp);

		runServletInitializedMethodOnListeners(servletContext);

		Configuration.applications.put(servletContext.getServletContextName(), servletContext);

	}

	private static WebApp unmarshalWebXml(String appFolder)
			throws MalformedURLException, ClassNotFoundException, FileNotFoundException, JAXBException {
		File webXml = new File(Configuration.appsDirectory + "/" + appFolder + "/WEB-INF/web.xml");
		if (webXml.exists()) {
			return unmarshalWebXml(webXml);
		} else {
			throw new FileNotFoundException("Cannot find web.xml file");
		}
	}

	private static WebApp unmarshalWebXml(File webXml) throws JAXBException, FileNotFoundException {
		JAXBContext context = JAXBContext.newInstance(WebApp.class);
		Unmarshaller um = context.createUnmarshaller();
		return (WebApp) um.unmarshal(new FileReader(webXml));
	}

	private static void setAppName(ServletContext servletContext, WebApp webApp, String folderName) {
		String appName = webApp.getDisplayName() != null ? webApp.getDisplayName() : folderName;
		servletContext.setServletContextName(appName);
	}

	private static void setDefaultAppPages(ServletContext servletContext, WebApp webApp) {
		servletContext.setDefaultPages(webApp.getWelcomeFiles());
	}

	private static void setContextParams(ServletContext servletContext, WebApp webApp) {
		Map<String, String> initParams = new HashMap<>();
		for (ContextParam cp : webApp.getContextParams()) {
			initParams.put(cp.getName(), cp.getValue());
		}
	}

	private static void setServlets(ServletContext servletContext, WebApp webApp) {
		for (Servlet s : webApp.getServlets()) {
			ServletConfig servletConfig = new ServletConfig(servletContext);
			servletConfig.setServletName(s.getServletName());
			servletConfig.setServletClass(s.getServletClass());
			setServletInitParams(s, servletConfig);
			servletContext.getServletConfigs().add(servletConfig);
		}
	}

	private static void setServletInitParams(Servlet servlet, ServletConfig servletConfig) {
		Map<String, String> initParams = servletConfig.getInitParams();
		for (InitParam ip : servlet.getInitParams()) {
			initParams.put(ip.getName(), ip.getValue());
		}
	}

	private static void setServletMappings(ServletContext servletContext, WebApp webApp) {
		for (ServletConfig sc : servletContext.getServletConfigs()) {
			for (ServletMapping sm : webApp.getServletMappings()) {
				if (sm.getServletName().equals(sc.getServletName())) {
					Class<? extends javax.servlet.Servlet> clazz = getClassForServletClass(sc.getServletClass());
					sc.getServletMappings().put(sm.getUrlPattern(), clazz);
				}
			}
		}
	}

	private static void setListeners(ServletContext servletContext, WebApp webApp) {
		for (Listener l : webApp.getListeners()) {
			Class<? extends EventListener> clazz = getClassForListener(l.getListenerClass());
			servletContext.getListeners().add(clazz);
		}

	}

	private static void runServletInitializedMethodOnListeners(ServletContext servletContext)
			throws InstantiationException, IllegalAccessException {
		for (Class<? extends EventListener> l : servletContext.getListeners()) {
			for (Class<?> i : l.getInterfaces()) {
				if (i.getName().equals("javax.servlet.ServletContextListener")) {
					ServletContextListener servletContextListener = ServletContextListener.class.cast(l.newInstance());
					ServletContextEvent servletContextEvent = new ServletContextEvent(servletContext);
					servletContextListener.contextInitialized(servletContextEvent);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static Class<? extends EventListener> getClassForListener(String listenerClass) {
		try {
			return (Class<? extends EventListener>) Class.forName(listenerClass);
		} catch (ClassNotFoundException e) {
			// TODO cos z tym trzeba zrobic
			// chyba trzeba pociagnac te wyjatki w gore
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private static Class<javax.servlet.Servlet> getClassForServletClass(String urlPattern) {
		try {
			return (Class<javax.servlet.Servlet>) Class.forName(urlPattern);
		} catch (ClassNotFoundException e) {
			// TODO cos z tym trzeba zrobic
			// chyba trzeba pociagnac te wyjatki w gore
			e.printStackTrace();
			return null;
		}
	}

}
