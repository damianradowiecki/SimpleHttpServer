package pl.itandmusic.simplehttpserver.utils;

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pl.itandmusic.simplehttpserver.model.HttpServletRequestImpl;

public class URIResolverTest {
	
	private List<HttpServletRequestImpl> requests = new ArrayList<>();
	
	
	@Before
	public void prepare() throws URISyntaxException {
		HttpServletRequestImpl.Builder builder = new HttpServletRequestImpl.Builder();
		HttpServletRequestImpl request = builder
			.setRequestURI(new URI("/test"))
			.build();
		requests.add(request);
		
		HttpServletRequestImpl request_2 = builder
				.setRequestURI(new URI("/test/"))
				.build();
		requests.add(request_2);
		
		HttpServletRequestImpl request_3 = builder
				.setRequestURI(new URI("/test/test"))
				.build();
			requests.add(request_3);
			
	}
	
	@Test
	public void testDefaultAppPageRequest() {
		assertTrue(URIResolver.defaultAppPageRequest(requests.get(0)));
		assertTrue(URIResolver.defaultAppPageRequest(requests.get(1)));
		assertFalse(URIResolver.defaultAppPageRequest(requests.get(2)));
	}
}
