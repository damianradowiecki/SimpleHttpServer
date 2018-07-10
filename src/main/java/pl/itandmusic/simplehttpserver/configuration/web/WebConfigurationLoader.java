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

		loadAppFolderNames();

		loadAppConfigurations();

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

	}

	private static void loadAppConfigurations()
			throws MalformedURLException, ClassNotFoundException, FileNotFoundException, JAXBException {

		for (String fn : appFolderNames) {
			loadAppConfiguration(fn);
		}

	}

	private static void loadAppConfiguration(String appFolderName)
			throws MalformedURLException, ClassNotFoundException, FileNotFoundException, JAXBException {
		ServletContext servletContext = new ServletContext();
		loadAppsConfig(appFolderName, servletContext);
		Configuration.applications.put(servletContext.getServletContextName(), servletContext);

	}

	private static void loadAppsConfig(String appFolder, ServletContext servletContext)
			throws MalformedURLException, ClassNotFoundException, FileNotFoundException, JAXBException {
		File webXml = new File(Configuration.appsDirectory + "/" + appFolder + "/WEB-INF/web.xml");
		if (webXml.exists()) {
			WebApp webApp = unmarshalWebXml(webXml);
			fillAppsConfig(servletContext, appFolder, webApp);
		} else {
			throw new FileNotFoundException("Cannot find web.xml file");
		}
	}

	private static WebApp unmarshalWebXml(File webXml) throws JAXBException, FileNotFoundException {
		JAXBContext context = JAXBContext.newInstance(WebApp.class);
		Unmarshaller um = context.createUnmarshaller();
		return (WebApp) um.unmarshal(new FileReader(webXml));
	}

	private static void fillAppsConfig(ServletContext servletContext, String appFolder, WebApp webApp)
			throws MalformedURLException, ClassNotFoundException {
		String appName = webApp.getDisplayName() != null ? webApp.getDisplayName() : appFolder;
		servletContext.setServletContextName(appName);
		servletContext.setDefaultPages(webApp.getWelcomeFiles());
		servletContext.setAppPath(Configuration.appsDirectory + "/" + appFolder);
		ServletConfig servletConfig = new ServletConfig();
		loadServletMappings(appFolder, appName, webApp, servletConfig);
		servletContext.getServletConfigs().add(servletConfig);
	}

	@SuppressWarnings({ "resource", "unchecked" })
	private static void loadServletMappings(String appFolder, String appName, WebApp webApp, ServletConfig servletConfig)
			throws MalformedURLException, ClassNotFoundException {
		String servletName = webApp.getServlet().getServletName();
		String servletClassName = webApp.getServlet().getServletClass();

		URL classes = new File(Configuration.appsDirectory + "/" + appFolder + "/WEB-INF/classes").toURI().toURL();
		URL[] urls = new URL[] { classes };

		
		ClassLoader classLoader = new URLClassLoader(urls);

		Class<? extends javax.servlet.Servlet> clazz = (Class<? extends javax.servlet.Servlet>) classLoader.loadClass(servletClassName);

		Map<String, Class<? extends javax.servlet.Servlet>> servletsMappings = new HashMap<>();

		if (webApp.getServletMapping().getServletName().equals(servletName)) {
			servletsMappings.put("/" + appName + webApp.getServletMapping().getUrlPattern(), clazz);
		}

		servletConfig.setServletMappings(servletsMappings);

	}

}
