package pl.itandmusic.simplehttpserver.configuration.web;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import pl.itandmusic.simplehttpserver.configuration.server.ServerConfigurationLoader;

public class WebConfigurationLoaderTest {
	
	
	@Test
	public void testWebConfigurationLoading() throws ClassNotFoundException, JAXBException, IOException, InstantiationException, IllegalAccessException {
		ServerConfigurationLoader.load();
		WebConfigurationLoader.load();
	}
	
	
}
