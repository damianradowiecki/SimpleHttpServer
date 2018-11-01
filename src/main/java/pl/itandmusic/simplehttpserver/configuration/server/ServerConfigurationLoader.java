package pl.itandmusic.simplehttpserver.configuration.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import pl.itandmusic.simplehttpserver.configuration.Configuration;
import pl.itandmusic.simplehttpserver.logger.Logger;

public class ServerConfigurationLoader {

	private static final Logger logger = Logger.getLogger(ServerConfigurationLoader.class);
	private static ServerConfig serverConfig;

	
	private ServerConfigurationLoader() {}
	
	public static void load(){

		logger.info("Server configuration loading.");

		Path serverConfigurationPath = resolveServerXmlFilePath();
		
		if(serverConfigurationPath.toFile().exists()) {
			if(tryToLoadServerConfig(serverConfigurationPath.toFile())) {
				Configuration.port = serverConfig.getPort();
				Configuration.appsDirectory = serverConfig.getAppsDirectory();
				Configuration.logLevels = serverConfig.getLogLevel().split(",");
				Configuration.nonBlockingMode = serverConfig.getNonBlockingMode();
			}
		} else {
			logger.error("Cannot find server.xml file. Should be in {rootDirectory}" + File.separator+"config directory.");
		}

		logger.info("Server configuration loaded.");

	}

	private static Path resolveServerXmlFilePath() {
		Path rootPath = Paths.get("");
		return rootPath.resolve("config").resolve("server.xml");
	}

	private static boolean tryToLoadServerConfig(File serverConfigFile) {
		try {
			JAXBContext context = JAXBContext.newInstance(ServerConfig.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			serverConfig = (ServerConfig) unmarshaller.unmarshal(new FileReader(serverConfigFile));
			return true;
		} catch (JAXBException | FileNotFoundException e) {
			logger.info("Couldn't read server.xml file. Server can not run.");
			logger.info("Exception message: " + e.getMessage());
			return false;
		}
	}
}
