package pl.itandmusic.simplehttpserver.configuration.web;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import pl.itandmusic.simplehttpserver.configuration.server.ServerConfigurationLoader;

public class WebConfigurationLoaderTest {
	
	
	@Test
	public void testWebConfigurationLoading() throws ClassNotFoundException, JAXBException, IOException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		ServerConfigurationLoader.load();
		WebConfigurationLoader.load();
	}
	
	
}
