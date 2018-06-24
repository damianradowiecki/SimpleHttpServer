package pl.itandmusic.simplehttpserver.configuration.server;

import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import org.junit.Test;

public class ServerConfigurationLoaderTest {
	
	@Test
	public void testServerConfigurationLoading() throws FileNotFoundException, JAXBException {
		ServerConfigurationLoader.load();
	}
	
}
