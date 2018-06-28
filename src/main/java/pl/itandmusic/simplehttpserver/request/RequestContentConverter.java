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

import pl.itandmusic.simplehttpserver.logger.LogLevel;
import pl.itandmusic.simplehttpserver.logger.Logger;
import pl.itandmusic.simplehttpserver.model.HeaderNames;
import pl.itandmusic.simplehttpserver.model.HeaderValues;
import pl.itandmusic.simplehttpserver.model.HttpMethod;
import pl.itandmusic.simplehttpserver.model.HttpServletRequestImpl;
import pl.itandmusic.simplehttpserver.model.RequestContent;
import pl.itandmusic.simplehttpserver.model.ServletInputStreamImpl;

public class RequestContentConverter {

	private static final String PARAMS_DELIMITER = "&";
	private static final String KEY_VALUE_DELIMITER = "=";
	private Pattern pattern;
	private Matcher matcher;
	private HttpMethod httpMethod;
	private String protocol;
	private URI requestURI;
	private StringBuffer requestURL;
	private String queryString;
	private Map<String, Enumeration<String>> headers;
	private Enumeration<String> headerNames;
	private String remoteAddress;
	private ServletInputStream servletInputStream;
	private Map<String, String> parameters;

	public HttpServletRequestImpl convert(RequestContent content, Socket socket) {

		List<String> plainContent = content.getPlainContent();
		String postData = content.getPOSTData();
		
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

		HttpServletRequestImpl.Builder builder = new HttpServletRequestImpl.Builder();

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
			Logger.log(exception.getMessage(), LogLevel.ERROR);
		}
		return stringBUffer;
	}

	String extractQueryString(List<String> content) {
		String firstLine = content.get(0);
		String[] parts = firstLine.split("\\s+");
		String queryString = parts[1];
		return queryString;
	}

	Map<String, Enumeration<String>> extractHeaders(List<String> content) {
		Map<String, Enumeration<String>> headers = new HashMap<>();

		pattern = Pattern.compile("(?<NAME>.+?):(?<VALUES>(.+?,.+?)*)");

		for (String s : content) {
			matcher = pattern.matcher(s);
			if (matcher.matches()) {
				String headerName = matcher.group("NAME");
				HeaderValues headerValues = new HeaderValues(matcher.group("VALUES"));
				headers.put(headerName, headerValues);
			}
		}
		return headers;
	}
	
	Enumeration<String> extractHeaderNames(List<String> content){
		
		List<String> names = new ArrayList<>();
		
		pattern = Pattern.compile("(?<NAME>.+?):(.+?,.+?)*");

		for (String s : content) {
			matcher = pattern.matcher(s);
			if (matcher.matches()) {
				String headerName = matcher.group("NAME");
				names.add(headerName);
			}
		}
		
		return new HeaderNames(names);
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
		return result;
	}
	
	Map<String, String> extractParameters(String postData) {
		Map<String, String> result = new HashMap<>();
		String [] params = postData.split(PARAMS_DELIMITER);
		for(String p : params) {
			String [] keyValuePairs = p.split(KEY_VALUE_DELIMITER);
			String key = keyValuePairs[0].trim();
			String value = keyValuePairs[1].trim();
			result.put(key, value);
		}
		return result;
	}
}
