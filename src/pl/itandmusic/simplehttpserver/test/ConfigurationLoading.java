package pl.itandmusic.simplehttpserver.test;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import pl.itandmusic.simplehttpserver.configuration.server.ServerConfigurationLoader;
import pl.itandmusic.simplehttpserver.configuration.web.WebConfigurationLoader;

public class ConfigurationLoading {
	
	@Test
	public void testServerConfigurationLoading() throws FileNotFoundException, JAXBException {
		ServerConfigurationLoader.load();
	}
	
	@Test
	public void testWebConfigurationLoading() throws FileNotFoundException, MalformedURLException, ClassNotFoundException, JAXBException {
		ServerConfigurationLoader.load();
		WebConfigurationLoader.load();
	}
	
	
}
