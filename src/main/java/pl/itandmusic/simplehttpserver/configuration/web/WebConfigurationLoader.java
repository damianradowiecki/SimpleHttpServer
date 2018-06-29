package pl.itandmusic.simplehttpserver.configuration.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import pl.itandmusic.simplehttpserver.configuration.Configuration;
import pl.itandmusic.simplehttpserver.logger.Logger;

public class WebConfigurationLoader {

	private static final Logger logger = Logger.getLogger(WebConfigurationLoader.class);
	
	public static void load() throws JAXBException, FileNotFoundException, MalformedURLException, ClassNotFoundException {
		
		logger.info("Web configuration loading.");
		
		File webXml = new File(Configuration.appDirectory + "/WEB-INF/web.xml");
		if (webXml.exists()) {
			JAXBContext context = JAXBContext.newInstance(WebApp.class);
			Unmarshaller um = context.createUnmarshaller();
			WebApp webApp = (WebApp) um.unmarshal(new FileReader(webXml));
			loadServletMappings(webApp);
			Configuration.defaultPages.addAll(webApp.getWelcomeFiles());
			Configuration.appName = webApp.getDisplayName();
		} else {
			throw new FileNotFoundException("Cannot find web.xml file");
		}
		
		logger.info("Web configuration loaded.");
		
	}

	private static void loadServletMappings(WebApp webApp) throws MalformedURLException, ClassNotFoundException {
		String servletName = webApp.getServlet().getServletName();
		String servletClassName = webApp.getServlet().getServletClass();

		
		URL classes = new File(Configuration.appDirectory + "/WEB-INF/classes").toURI().toURL();
		URL [] urls = new URL[]{classes};

		ClassLoader classLoader = new URLClassLoader(urls);
		
		Class<?> clazz = classLoader.loadClass(servletClassName);
		
		if(webApp.getServletMapping().getServletName().equals(servletName)) {
			Configuration.servletsMappings.put(webApp.getServletMapping().getUrlPattern(), clazz);
		}
		
	}
	
}
