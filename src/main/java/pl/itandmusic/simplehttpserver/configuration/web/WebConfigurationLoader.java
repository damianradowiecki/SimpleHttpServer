package pl.itandmusic.simplehttpserver.configuration.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public static void load() throws IOException, ClassNotFoundException, JAXBException {

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

	private static void loadConfigurationForEveryApp()
			throws MalformedURLException, ClassNotFoundException, FileNotFoundException, JAXBException {

		for (String fn : appFolderNames) {
			loadAppConfiguration(fn);
		}

	}

	private static void loadAppConfiguration(String appFolderName){
		
		ServletContext servletContext = new ServletContext();
		
		WebApp webApp = unmarshalWebXml(appFolderName);
		
		setAppName(servletContext, webApp, appFolderName);

		setDefaultAppPages(servletContext, webApp);

		setContextParams(servletContext, webApp);

		setServlets(servletContext, webApp);
		
		setServletMappings(servletContext, webApp);
		
		
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
		for(ContextParam cp : webApp.getContextParams()) {
			servletContext.setAttribute(cp.getName(), cp.getValue());
		}
	}
	
	private static void setServlets(ServletContext servletContext, WebApp webApp) {
		for(Servlet s : webApp.getServlets()) {
			ServletConfig servletConfig = new ServletConfig();
			servletConfig.setServletName(s.getServletName());
			servletConfig.setServletClass(s.getServletClass());
			servletContext.getServletConfigs().add(servletConfig);
		}
		
	}
	
	private static void setServletMappings(ServletContext servletContext, WebApp webApp) {
		for(ServletConfig sc : servletContext.getServletConfigs()) {
			for(ServletMapping sm : webApp.getServletMappings()) {
				if(sm.getServletName().equals(sc.getServletName())) {
					Class<?> clazz = getClassForUrlPattern(sm.getUrlPattern());
					sc.getServletMappings().put(sm.getServletName(), clazz);
				}
			}
		}
	}
	
	private static Class<?> getClassForUrlPattern(String urlPattern){
		try {
			return Class.forName(urlPattern);
		} catch (ClassNotFoundException e) {
			//TODO cos z tym trzeba zrobic
			e.printStackTrace();
			return null;
		}
	}

	

}
