package pl.itandmusic.simplehttpserver.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pl.itandmusic.simplehttpserver.model.HeaderValues;
import pl.itandmusic.simplehttpserver.model.HttpMethod;
import pl.itandmusic.simplehttpserver.model.RequestContent;

public class RequestContentConverterTest {

	private static RequestContentConverter requestContentConverter;
	private List<String> content_1;
	private List<String> content_2;
	private String POSTData;
	private RequestContent getRequestContent;
	private RequestContent postRequestContent;

	@BeforeClass
	public static void prepare() {
		requestContentConverter = RequestContentConverter.getRequestContentConverter();
	}

	@Before
	public void prepareContent() {
		content_1 = new ArrayList<>();
		content_1.add("GET /test/test.do?test=test&test2=wew HTTP/1.1");
		content_1.add("Host: wickedlysmart.com");
		content_1.add("Accept-Language: en-us, en;q=0.5");
		content_1.add("Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.7");
		
		getRequestContent = new RequestContent();
		getRequestContent.setPlainContent(content_1);
		
		
		
		content_2 = new ArrayList<>();
		content_2.add("POST /any/resource/on/server/test HTTP/2.0");
		
		POSTData = "kolor = ciemny& smak=slodkawy";
		
		postRequestContent = new RequestContent();
		postRequestContent.setPlainContent(content_2);
		postRequestContent.setPOSTData(POSTData);
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
	
	@Test
	public void testURLExtracting() {
		//from /etc/hosts
		//127.0.0.1 localhost
		//127.0.1.1	damian-Lenovo-G780
		StringBuffer stringBuffer = requestContentConverter.extractURL(content_1);
		assertEquals("127.0.1.1/test/test.do", stringBuffer.toString());
		stringBuffer = requestContentConverter.extractURL(content_2);
		assertEquals("127.0.1.1/any/resource/on/server/test", stringBuffer.toString());
	}
	
	@Test
	public void testQueryStringExtracting() {
		String queryString = requestContentConverter.extractQueryString(content_1);
		assertEquals("/test/test.do?test=test&test2=wew", queryString);
		assertNotEquals("test", queryString);
		queryString = requestContentConverter.extractQueryString(content_2);
		assertEquals("/any/resource/on/server/test", queryString);
		assertNotEquals("/any/resource/on/server/", queryString);
	}
	
	@Test
	public void testHeadersExtracting() {
		Map<String, String> headers = requestContentConverter.extractHeaders(content_1);
		assertTrue(headers.containsKey("Accept-Charset"));
		assertTrue(headers.containsKey("Accept-Language"));
		for(String key : headers.keySet()) {
			if(key.equals("Accept-Charset")) {
				HeaderValues headerValues = new HeaderValues(headers.get(key));
				int elementsCount = 0;
				while(headerValues.hasMoreElements()) {
					String value = headerValues.nextElement();
					assertTrue(value.equals("ISO-8859-1") || value.equals("utf-8;q=0.7") || value.equals("*;q=0.7"));
					elementsCount++;
				}
				assertEquals(3, elementsCount);
			}
			else if(key.equals("Accept-Language")) {
				HeaderValues headerValues = new HeaderValues(headers.get(key));
				int elementsCount = 0;
				while(headerValues.hasMoreElements()) {
					String value = headerValues.nextElement();
					assertTrue(value.equals("en-us") || value.equals("en;q=0.5"));
					elementsCount++; 
				}
				assertEquals(2, elementsCount);
			}
			
		}
	}
	
	@Test
	public void testHeaderNamesExtracting() {
		Enumeration<String> headerNames = requestContentConverter.extractHeaderNames(content_1);
		
		List<String> headerNames_ = new ArrayList<>();
		
		while(headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			headerNames_.add(headerName);
		}
		
		assertTrue(headerNames_.contains("Accept-Charset"));
		assertTrue(headerNames_.contains("Accept-Language"));
		assertTrue(headerNames_.contains("Host"));
		
	}
	
	@Test
	public void testPOSTParametersExtracting() {
		Map<String, String> POSTParams = requestContentConverter.extractParameters(POSTData);
		assertTrue(POSTParams.containsKey("kolor"));
		assertEquals("ciemny", POSTParams.get("kolor"));
		assertTrue(POSTParams.containsKey("smak"));
		assertEquals("slodkawy", POSTParams.get("smak"));
	}
	
	@Test
	public void testHttpMethodExtracting() {
		HttpMethod method_1 = requestContentConverter.extractHttpMethod(content_1);
		assertEquals(HttpMethod.GET, method_1);
		HttpMethod method_2 = requestContentConverter.extractHttpMethod(content_2);
		assertEquals(HttpMethod.POST, method_2);
	}
	
	@Test
	public void testParametersExtracting() {
		Map<String, String> content_1Params = requestContentConverter.extractParameters(getRequestContent, HttpMethod.GET);
		assertTrue(content_1Params.containsKey("test"));
		assertTrue(content_1Params.get("test").equals("test"));
		assertTrue(content_1Params.containsKey("test2"));
		assertTrue(content_1Params.get("test2").equals("wew"));
		assertTrue(!content_1Params.containsKey("wew"));
		Map<String, String> content_2Params = requestContentConverter.extractParameters(postRequestContent, HttpMethod.POST);
		assertTrue(content_2Params.containsKey("kolor"));
		assertTrue(content_2Params.get("kolor").equals("ciemny"));
		assertTrue(content_2Params.containsKey("smak"));
		assertTrue(content_2Params.get("smak").equals("slodkawy"));
		assertTrue(!content_1Params.containsKey("slodkawy"));
	}

}
