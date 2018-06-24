package pl.itandmusic.simplehttpserver.configuration.web;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import pl.itandmusic.simplehttpserver.configuration.server.ServerConfigurationLoader;

public class WebConfigurationLoaderTest {
	
	
	@Test
	public void testWebConfigurationLoading() throws FileNotFoundException, MalformedURLException, ClassNotFoundException, JAXBException {
		ServerConfigurationLoader.load();
		WebConfigurationLoader.load();
	}
	
	
}
