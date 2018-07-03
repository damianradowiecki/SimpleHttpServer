package pl.itandmusic.simplehttpserver.configuration.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
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

	public static void load() throws JAXBException, FileNotFoundException {

		logger.info("Server configuration loading.");

		Path serverXmlFilePath = resolveServerXmlFilePath();

		if (Files.exists(serverXmlFilePath)) {
			if(tryToLoadServerConfig(serverXmlFilePath.toFile())) {
				Configuration.port = serverConfig.getPort();
				Configuration.appsDirectory = serverConfig.getAppsDirectory();
			}
		} else {
			throw new FileNotFoundException("Cannot find server.xml file");
		}

		logger.info("Server configuration loaded.");

	}

	private static Path resolveServerXmlFilePath() {
		Path rootPath = Paths.get("");
		Path serverXmlFilePath = rootPath.resolve("config").resolve("server.xml");
		return serverXmlFilePath;
	}

	private static boolean tryToLoadServerConfig(File serverConfigFile) {
		try {
			JAXBContext context = JAXBContext.newInstance(ServerConfig.class);
			Unmarshaller um;

			um = context.createUnmarshaller();
			serverConfig = (ServerConfig) um.unmarshal(new FileReader(serverConfigFile));
			return true;
		} catch (JAXBException | FileNotFoundException e) {
			logger.info("Couldn't read server.xml file. Server can not run.");
			logger.info("Exception message: " + e.getMessage());
			return false;
		}
	}
}
