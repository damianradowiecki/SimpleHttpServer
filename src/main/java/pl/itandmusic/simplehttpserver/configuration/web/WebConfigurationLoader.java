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
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import pl.itandmusic.simplehttpserver.configuration.AppConfig;
import pl.itandmusic.simplehttpserver.configuration.Configuration;
import pl.itandmusic.simplehttpserver.logger.Logger;

public class WebConfigurationLoader {

	private static final Logger logger = Logger.getLogger(WebConfigurationLoader.class);

	public static void load() {

		logger.info("Web configuration loading.");

		List<String> folderNames = new ArrayList<>();

		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(Configuration.appsDirectory))) {
			for (Path path : directoryStream) {
				if (Files.isDirectory(path)) {
					String folderName = path.getFileName().toString();
					folderNames.add(folderName);
				}
			}
		} catch (IOException ex) {
			// TODO warn about appsDirectory unproper value
		}

		try {
			for (String fn : folderNames) {
				AppConfig appConfig = new AppConfig();
				loadAppConfig(fn, appConfig);
				Configuration.applications.put(appConfig.getAppName(), appConfig);
			}

		} catch (MalformedURLException | ClassNotFoundException | FileNotFoundException | JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Web configuration loaded.");

	}

	//TODO refactoring
	private static void loadAppConfig(String appFolder, AppConfig appConfig)
			throws MalformedURLException, ClassNotFoundException, FileNotFoundException, JAXBException {
		File webXml = new File(Configuration.appsDirectory + "/" + appFolder + "/WEB-INF/web.xml");
		if (webXml.exists()) {
			JAXBContext context = JAXBContext.newInstance(WebApp.class);
			Unmarshaller um = context.createUnmarshaller();
			WebApp webApp = (WebApp) um.unmarshal(new FileReader(webXml));
			loadServletMappings(appFolder, webApp, appConfig);
			appConfig.setDefaultPages(webApp.getWelcomeFiles());
			String appName = webApp.getDisplayName() != null ? webApp.getDisplayName() : appFolder;
			appConfig.setAppName(appName);
		} else {
			throw new FileNotFoundException("Cannot find web.xml file");
		}
	}

	private static void loadServletMappings(String appFolder, WebApp webApp, AppConfig appConfig)
			throws MalformedURLException, ClassNotFoundException {
		String servletName = webApp.getServlet().getServletName();
		String servletClassName = webApp.getServlet().getServletClass();

		URL classes = new File(Configuration.appsDirectory + "/" + appFolder + "/WEB-INF/classes").toURI().toURL();
		URL[] urls = new URL[] { classes };

		ClassLoader classLoader = new URLClassLoader(urls);

		Class<?> clazz = classLoader.loadClass(servletClassName);

		Map<String, Class<?>> servletsMappings = new HashMap<>();

		if (webApp.getServletMapping().getServletName().equals(servletName)) {
			servletsMappings.put(webApp.getServletMapping().getUrlPattern(), clazz);
		}

		appConfig.setServletsMappings(servletsMappings);

	}

}
