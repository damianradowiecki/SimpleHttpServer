package pl.itandmusic.simplehttpserver.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pl.itandmusic.simplehttpserver.configuration.Configuration;
import pl.itandmusic.simplehttpserver.configuration.server.ServerConfigurationLoader;
import pl.itandmusic.simplehttpserver.configuration.web.WebConfigurationLoader;
import pl.itandmusic.simplehttpserver.model.ServletContext;
import pl.itandmusic.simplehttpserver.model.HttpServletRequestImpl;
import pl.itandmusic.simplehttpserver.model.ServletConfig;

public class URIResolverTest {

	private static List<HttpServletRequestImpl> requests;
	private static ServletContext servletContext_1;
	private static ServletContext servletContext_2;

	
	@BeforeClass
	public static void init() throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, IOException, JAXBException {
		requests = new ArrayList<>();
		servletContext_1 = new ServletContext();
		servletContext_2 = new ServletContext();
	}
	
	@Before
	public void prepare() throws URISyntaxException {

		HttpServletRequestImpl.Builder builder = new HttpServletRequestImpl.Builder();
		HttpServletRequestImpl request = builder.setRequestURI(new URI("/test")).build();
		requests.add(request);

		HttpServletRequestImpl request_2 = builder.setRequestURI(new URI("/test/")).build();
		requests.add(request_2);

		HttpServletRequestImpl request_3 = builder.setRequestURI(new URI("/test/test")).build();
		requests.add(request_3);

		HttpServletRequestImpl request_4 = builder.setRequestURI(new URI("/")).build();
		requests.add(request_4);

		HttpServletRequestImpl request_5 = builder.setRequestURI(new URI("/TestApp/page")).build();
		requests.add(request_5);

		HttpServletRequestImpl request_6 = builder.setRequestURI(new URI("/SecondTestApp/anotherPage.do")).build();
		requests.add(request_6);

		HttpServletRequestImpl request_7 = builder.setRequestURI(new URI("/SecondTestApp/notexistsing")).build();
		requests.add(request_7);
		
		
		ServletConfig servletConfig_1 = new ServletConfig(servletContext_1);
		
		Map<String, Class<? extends Servlet>> servletMappings_1 = new HashMap<>();

		servletMappings_1.put("/TestApp/WybierzPiwo.do", Servlet.class);
		servletMappings_1.put("/TestApp/page", Servlet.class);
		
		servletConfig_1.setServletMappings(servletMappings_1);

		ServletConfig servletConfig_2 = new ServletConfig(servletContext_2);
		
		Map<String, Class<? extends Servlet>> servletMappings_2 = new HashMap<>();
		
		servletMappings_2.put("/SecondTestApp/anotherPage.do", Servlet.class);
		servletMappings_2.put("/SecondTestApp/WybierzPiwo123.do", Servlet.class);

		servletConfig_2.setServletMappings(servletMappings_2);
		
		servletContext_1.getServletConfigs().add(servletConfig_1);
		
		servletContext_2.getServletConfigs().add(servletConfig_2);
		
		Configuration.applications.put("TestApp", servletContext_1);
		Configuration.applications.put("SecondTestApp", servletContext_2);
	}

	@Test
	public void testServerInfoRequest() {
		assertTrue(URIResolver.serverInfoRequest(requests.get(3)));
	}

	@Test
	public void testDefaultAppPageRequest() {
		assertTrue(URIResolver.defaultAppPageRequest(requests.get(0)));
		assertTrue(URIResolver.defaultAppPageRequest(requests.get(1)));
		assertFalse(URIResolver.defaultAppPageRequest(requests.get(2)));
	}

	@Test
	public void testProperDeafultAppPageRequest() {
		assertFalse(URIResolver.properDefaultAppPageRequest(requests.get(0)));
		assertTrue(URIResolver.properDefaultAppPageRequest(requests.get(1)));
		assertFalse(URIResolver.properDefaultAppPageRequest(requests.get(2)));
	}

	@Test
	public void testAnyAppRequest() {
		//assertTrue(URIResolver.anyAppRequest(requests.get(4)));
		//assertTrue(URIResolver.anyAppRequest(requests.get(5)));
		//assertFalse(URIResolver.anyAppRequest(requests.get(6)));
	}
}
