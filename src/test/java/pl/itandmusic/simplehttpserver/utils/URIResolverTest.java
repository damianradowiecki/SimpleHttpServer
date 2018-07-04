package pl.itandmusic.simplehttpserver.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pl.itandmusic.simplehttpserver.configuration.AppConfig;
import pl.itandmusic.simplehttpserver.configuration.Configuration;
import pl.itandmusic.simplehttpserver.model.HttpServletRequestImpl;

public class URIResolverTest {

	private static List<HttpServletRequestImpl> requests;
	private static Map<String, Class<?>> servletMappings_1;
	private static Map<String, Class<?>> servletMappings_2;
	private static AppConfig appConfig_1;
	private static AppConfig appConfig_2;

	
	@BeforeClass
	public static void init() {
		requests = new ArrayList<>();
		servletMappings_1 = new HashMap<>();
		servletMappings_2 = new HashMap<>();
		appConfig_1 = new AppConfig();
		appConfig_2 = new AppConfig();
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

		servletMappings_1.put("/TestApp/WybierzPiwo.do", Object.class);
		servletMappings_1.put("/TestApp/page", Object.class);
		servletMappings_2.put("/SecondTestApp/anotherPage.do", Object.class);
		servletMappings_2.put("/SecondTestApp/WybierzPiwo123.do", Object.class);

		appConfig_1.setServletsMappings(servletMappings_1);
		appConfig_2.setServletsMappings(servletMappings_2);

		Configuration.applications.put("TestApp", appConfig_1);
		Configuration.applications.put("SecondTestApp", appConfig_2);
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
		assertTrue(URIResolver.anyAppRequest(requests.get(4)));
		assertTrue(URIResolver.anyAppRequest(requests.get(5)));
		assertFalse(URIResolver.anyAppRequest(requests.get(6)));
	}
}
