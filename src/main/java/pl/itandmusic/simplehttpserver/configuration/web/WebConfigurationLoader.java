package pl.itandmusic.simplehttpserver.configuration.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EventListener;
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
import pl.itandmusic.simplehttpserver.session.SessionManager;

public class WebConfigurationLoader {

	private static final Logger logger = Logger.getLogger(WebConfigurationLoader.class);
	private static List<String> appFolderNames = new ArrayList<>();
	private static final int DEFAULT_SESSION_TIMEOUT = 30;

	public static void load()
			throws IOException, ClassNotFoundException, JAXBException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

		logger.info("Web configuration loading.");

		loadAppFolderNames();

		addClassesDirectoriesToClasspath();
		
		loadConfigurationForEveryApp();

		logger.info("Web configuration loaded.");

	}

	private static void loadAppFolderNames() throws IOException {
		DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(Configuration.appsDirectory));
		for (Path path : directoryStream) {
			if (Files.isDirectory(path)) {
				String folderName = path.getFileName().toString();
				appFolderNames.add(folderName);
			}
		}
		directoryStream.close();

	}

	private static void loadConfigurationForEveryApp() throws ClassNotFoundException,
			JAXBException, InstantiationException, IllegalAccessException, IOException {

		for (String fn : appFolderNames) {
			loadAppConfiguration(fn);
		}

	}

	private static void addClassesDirectoriesToClasspath() throws MalformedURLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException{
		for (String fn : appFolderNames) {
			addClassesDirectoryToClasspath(fn);
		}
	}
	
	private static void addClassesDirectoryToClasspath(String appFolderName) throws MalformedURLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Path pathToClasses = Paths.get(Configuration.appsDirectory, appFolderName, "WEB-INF", "classes");

		if(pathToClasses.toFile().exists()) {
			addClassesDirectoryToClasspath(pathToClasses);
		}
	}
	
	private static void addClassesDirectoryToClasspath(Path path) throws MalformedURLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		URL classesURL = path.toFile().toURI().toURL();
		URLClassLoader classLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();
		Class<URLClassLoader> urlClassLoaderClass = URLClassLoader.class;
		Method addURLMethod = urlClassLoaderClass.getDeclaredMethod("addURL", new Class[] {URL.class});
		addURLMethod.setAccessible(true);
		addURLMethod.invoke(classLoader, classesURL);
	}
	
	private static void loadAppConfiguration(String appFolderName) throws ClassNotFoundException,
			JAXBException, InstantiationException, IllegalAccessException, IOException {

		ServletContext servletContext = new ServletContext();

		WebApp webApp = unmarshalWebXml(appFolderName);

		setAppName(servletContext, webApp, appFolderName);
		
		setAppPath(servletContext, appFolderName);

		setDefaultAppPages(servletContext, webApp);

		setContextParams(servletContext, webApp);

		setListeners(servletContext, webApp);

		setServlets(servletContext, webApp);

		setServletMappings(servletContext, webApp);
		
		setSessionConfiguration(servletContext, webApp);

		runServletInitializedMethodOnListeners(servletContext);

		Configuration.applications.put(servletContext.getServletContextName(), servletContext);

	}

	private static WebApp unmarshalWebXml(String appFolder)
			throws MalformedURLException, FileNotFoundException, JAXBException {
		Path path = Paths.get(Configuration.appsDirectory, appFolder, "WEB-INF", "web.xml");
		if (path.toFile().exists()) {
			return unmarshalWebXml(path.toFile());
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
	
	private static void setAppPath(ServletContext servletContext, String appFolderName) {
		servletContext.setAppPath(Configuration.appsDirectory + File.separator + appFolderName);
	}

	private static void setDefaultAppPages(ServletContext servletContext, WebApp webApp) {
		servletContext.setDefaultPages(webApp.getWelcomeFiles());
	}

	private static void setContextParams(ServletContext servletContext, WebApp webApp) {
		for (Param cp : webApp.getContextParams()) {
			servletContext.getInitParameters().put(cp.getName(), cp.getValue());
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
		for (Param ip : servlet.getInitParams()) {
			initParams.put(ip.getName(), ip.getValue());
		}
	}

	@SuppressWarnings("unchecked")
	private static void setServletMappings(ServletContext servletContext, WebApp webApp) throws ClassNotFoundException, IOException {
		for (ServletConfig sc : servletContext.getServletConfigs()) {
			for (ServletMapping sm : webApp.getServletMappings()) {
				if (sm.getServletName().equals(sc.getServletName())) {
					Class<? extends javax.servlet.Servlet> clazz = (Class<? extends javax.servlet.Servlet>) loadClassFromAppClassesFolder(
							servletContext, sc.getServletClass());
					sc.getServletMappings().put("/" + servletContext.getServletContextName() + sm.getUrlPattern(), clazz);
				}
			}
		}
	}
	
	private static void setSessionConfiguration(ServletContext servletContext, WebApp webApp) {
		SessionManager sessionManager = new SessionManager();
		if(webApp.getSessionConfig().getSessionTimeout() > 0) {
			sessionManager.setSessionTimeout(webApp.getSessionConfig().getSessionTimeout());
		}
		else {
			sessionManager.setSessionTimeout(DEFAULT_SESSION_TIMEOUT);
		}
		
		servletContext.setSessionManager(sessionManager);
	}

	@SuppressWarnings("unchecked")
	private static void setListeners(ServletContext servletContext, WebApp webApp) throws ClassNotFoundException, IOException {
		for (Listener l : webApp.getListeners()) {
			Class<? extends EventListener> clazz = (Class<? extends EventListener>) loadClassFromAppClassesFolder(
					servletContext, l.getListenerClass());
			servletContext.getListeners().add(clazz);
		}

	}

	private static void runServletInitializedMethodOnListeners(ServletContext servletContext)
			throws InstantiationException, IllegalAccessException {
		for (Class<? extends EventListener> l : servletContext.getListeners()) {
			for (Class<?> i : l.getInterfaces()) {
				if (i == ServletContextListener.class) {
					ServletContextListener servletContextListener = ServletContextListener.class.cast(l.newInstance());
					ServletContextEvent servletContextEvent = new ServletContextEvent(servletContext);
					servletContextListener.contextInitialized(servletContextEvent);
				}
			}
		}
	}

	private static Class<?> loadClassFromAppClassesFolder(ServletContext servletContext, String urlPattern) throws IOException, ClassNotFoundException{
		Path path = Paths.get(Configuration.appsDirectory, servletContext.getServletContextName(), "WEB-INF", "classes");
		URL url = path.toUri().toURL();
		try (URLClassLoader classLoader = new URLClassLoader(new URL[] { url });){
			return classLoader.loadClass(urlPattern);
		}
		catch(ClassNotFoundException exception){
			//TODO handling exception
			//Think about this. try catch finally block lets closing class loader even if exception is thrown
			//?? Should i rethrow this exception
			throw exception;
		}
		catch(IOException exception_) {
			//TODO handling exception
			//Think about this. try catch finally block lets closing class loader even if exception is thrown
			//?? Should i rethrow this exception
			throw exception_;
		}
			
	}

}
