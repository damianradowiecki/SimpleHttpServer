package pl.itandmusic.simplehttpserver;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import pl.itandmusic.simplehttpserver.configuration.server.ServerConfigurationLoader;
import pl.itandmusic.simplehttpserver.configuration.web.WebConfigurationLoader;
import pl.itandmusic.simplehttpserver.server.Server;

public class Main {

	public static void main(String[] args) throws JAXBException, ClassNotFoundException, IOException, InstantiationException, IllegalAccessException {
		
		ServerConfigurationLoader.load();
		WebConfigurationLoader.load();
		Server.start();
	}

}
