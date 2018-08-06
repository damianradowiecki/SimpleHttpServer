package pl.itandmusic.simplehttpserver.request;

import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletInputStream;

import pl.itandmusic.simplehttpserver.configuration.web.WebConfigurationLoader;
import pl.itandmusic.simplehttpserver.logger.Logger;
import pl.itandmusic.simplehttpserver.model.EnumerationImpl;
import pl.itandmusic.simplehttpserver.model.HttpMethod;
import pl.itandmusic.simplehttpserver.model.HttpServletRequestImpl;
import pl.itandmusic.simplehttpserver.model.RequestContent;
import pl.itandmusic.simplehttpserver.model.ServletContext;
import pl.itandmusic.simplehttpserver.model.ServletInputStreamImpl;
import pl.itandmusic.simplehttpserver.utils.URIResolver;

public class RequestContentConverter {

	private static final Logger logger = Logger.getLogger(WebConfigurationLoader.class);
	private static final String PARAMS_DELIMITER = "&";
	private static final String KEY_VALUE_DELIMITER = "=";
	private Pattern pattern;
	private Matcher matcher;
	private HttpMethod httpMethod;
	private String protocol;
	private URI requestURI;
	private StringBuffer requestURL;
	private String queryString;
	private Map<String, String> headers;
	private Enumeration<String> headerNames;
	private String remoteAddress;
	private ServletInputStream servletInputStream;
	private Map<String, String> parameters;

	public HttpServletRequestImpl convert(RequestContent content, Socket socket) {

		List<String> plainContent = content.getPlainContent();
		
		httpMethod = extractHttpMethod(plainContent);
		protocol = extractProtocol(plainContent);
		requestURI = extractURI(plainContent);
		requestURL = extractURL(plainContent);
		queryString = extractQueryString(plainContent);
		headers = extractHeaders(plainContent);
		headerNames = extractHeaderNames(plainContent);
		remoteAddress = extractRemoteHost(socket);
		servletInputStream = new ServletInputStreamImpl(socket);
		parameters = extractParameters(content, httpMethod);
		//TODO refactoring, inserting this data into request
		ServletContext servletContext = URIResolver.anyAppRequest(requestURI);
		boolean servetInfoRequest = URIResolver.serverInfoRequest(requestURI);
		boolean defaultAppPageRequest = URIResolver.defaultAppPageRequest(requestURI);
		HttpServletRequestImpl.Builder builder = HttpServletRequestImpl.Builder.newBuilder();

		return builder
				.setHttpMethod(httpMethod)
				.setProtocol(protocol)
				.setRequestURI(requestURI)
				.setRequestURL(requestURL)
				.setQueryString(queryString)
				.setHeaders(headers)
				.setHeaderNames(headerNames)
				.setRemoteAddress(remoteAddress)
				.setServletInputStream(servletInputStream)
				.setParameters(parameters)
				.build();
	}

	HttpMethod extractHttpMethod(List<String> content) {
		String firstLine = content.get(0);
		String[] parts = firstLine.split("\\s+");
		String methodName = parts[0];
		for (HttpMethod m : HttpMethod.values()) {
			if (methodName.contains(m.toString())) {
				return m;
			}
		}
		return HttpMethod.UNKNOWN;
	}

	String extractProtocol(List<String> content) {
		String firstLine = content.get(0);
		String[] parts = firstLine.split("\\s+");
		String protocol = parts[2];
		return protocol;
	}

	URI extractURI(List<String> content) {
		try {
			String queryString = extractQueryString(content);
			String[] queryStringParts = queryString.split("\\?");
			URI uri = new URI(queryStringParts[0]);
			return uri;
		} catch (URISyntaxException uriSyntaxException) {
			return null;
		}

	}

	StringBuffer extractURL(List<String> content) {
		StringBuffer stringBUffer = new StringBuffer(50);
		try {
			String localHost = InetAddress.getLocalHost().getHostAddress();
			stringBUffer.append(localHost);
			URI uri = extractURI(content);
			String uri_ = uri.toASCIIString();
			stringBUffer.append(uri_);
		} catch (UnknownHostException | NullPointerException exception) {
			logger.error(exception.getMessage());
		}
		return stringBUffer;
	}

	String extractQueryString(List<String> content) {
		String firstLine = content.get(0);
		String[] parts = firstLine.split("\\s+");
		String queryString = parts[1];
		return queryString;
	}

	Map<String, String> extractHeaders(List<String> content) {
		Map<String, String> headers = new HashMap<>();

		pattern = Pattern.compile("(?<NAME>.+?):(?<VALUES>(.+?,.+?)*)");

		for (String s : content) {
			matcher = pattern.matcher(s);
			if (matcher.matches()) {
				String headerName = matcher.group("NAME");
				headers.put(headerName, matcher.group("VALUES"));
			}
		}
		return headers;
	}
	
	Enumeration<String> extractHeaderNames(List<String> content){
		
		List<String> names = new ArrayList<>();
		
		pattern = Pattern.compile("(?<NAME>.+?):(.+?,?)*");

		for (String s : content) {
			matcher = pattern.matcher(s);
			if (matcher.matches()) {
				String headerName = matcher.group("NAME");
				names.add(headerName);
			}
		}
		
		return new EnumerationImpl<String>(names);
	}
	
	String extractRemoteHost(Socket socket) {
		return socket.getRemoteSocketAddress().toString();
	}
	
	Map<String, String> extractParameters(RequestContent content, HttpMethod httpMethod) {
		Map<String, String> result = new HashMap<>();
		if(httpMethod.equals(HttpMethod.GET)) {
			result = extractParameters(content.getPlainContent());
		}
		else if(httpMethod.equals(HttpMethod.POST)) {
			result = extractParameters(content.getPOSTData());
		}
		return result;
	}
	
	Map<String, String> extractParameters(List<String> content) {
		Map<String, String> result = new HashMap<>();
		String requestFirstLine = content.get(0);
		pattern = Pattern.compile("^.*?\\?(?<PARAMS>.*?)\\s.*$");
		matcher = pattern.matcher(requestFirstLine);
		if(matcher.matches()) {
			String paramsInOneString = matcher.group("PARAMS").trim();
			Map<String, String> params = extractParameters(paramsInOneString);
			result.putAll(params);
		}
		return result;
	}
	
	Map<String, String> extractParameters(String oneStringData) {
		Map<String, String> result = new HashMap<>();
		String [] params = oneStringData.split(PARAMS_DELIMITER);
		for(String p : params) {
			String [] keyValuePairs = p.split(KEY_VALUE_DELIMITER);
			String key = keyValuePairs[0].trim();
			String value = keyValuePairs[1].trim();
			result.put(key, value);
		}
		return result;
	}
}
