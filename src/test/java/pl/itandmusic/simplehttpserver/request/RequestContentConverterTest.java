package pl.itandmusic.simplehttpserver.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class RequestContentConverterTest {

	private static RequestContentConverter requestContentConverter;
	private List<String> content_1;
	private List<String> content_2;

	@BeforeClass
	public static void prepare() {
		requestContentConverter = new RequestContentConverter();
	}

	@Before
	public void prepareContent() {
		content_1 = new ArrayList<>();
		content_2 = new ArrayList<>();
		content_1.add("GET /test/test.do HTTP/1.1");
		content_2.add("POST /any/resource/on/server/test HTTP/2.0");
		// TODO the rest
	}

	@Test
	public void testProtocolExtracting() {
		String protocol = requestContentConverter.extractProtocol(content_1);
		assertEquals("HTTP/1.1", protocol);
		protocol = requestContentConverter.extractProtocol(content_2);
		assertEquals("HTTP/2.0", protocol);
	}

	@Test
	public void testURIExtracting() {
		URI uri = requestContentConverter.extractURI(content_1);
		assertNotNull(uri);
		assertEquals("/test/test.do", uri.toASCIIString());
		uri = requestContentConverter.extractURI(content_2);
		assertNotNull(uri);
		assertEquals("/any/resource/on/server/test", uri.toASCIIString());
	}
}
