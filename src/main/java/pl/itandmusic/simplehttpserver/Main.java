package pl.itandmusic.simplehttpserver;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.xml.bind.JAXBException;

import pl.itandmusic.simplehttpserver.configuration.server.ServerConfigurationLoader;
import pl.itandmusic.simplehttpserver.configuration.web.WebConfigurationLoader;
import pl.itandmusic.simplehttpserver.cron.SessionDestroyer;
import pl.itandmusic.simplehttpserver.server.Server;

public class Main {

	public static void main(String[] args) throws JAXBException, ClassNotFoundException, IOException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		ServerConfigurationLoader.load();
		WebConfigurationLoader.load();
		SessionDestroyer.start();
		Server.start();
	}

}
