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

import pl.itandmusic.simplehttpserver.enummeration.RequestType;
import pl.itandmusic.simplehttpserver.logger.LogLevel;
import pl.itandmusic.simplehttpserver.logger.Logger;
import pl.itandmusic.simplehttpserver.model.EnumerationImpl;
import pl.itandmusic.simplehttpserver.model.HttpMethod;
import pl.itandmusic.simplehttpserver.model.HttpServletRequestImpl;
import pl.itandmusic.simplehttpserver.model.RequestContent;
import pl.itandmusic.simplehttpserver.model.ServletContext;
import pl.itandmusic.simplehttpserver.model.ServletInputStreamImpl;
import pl.itandmusic.simplehttpserver.utils.URIResolver;

public class RequestContentConverter {

	private static RequestContentConverter requestContentConverter = new RequestContentConverter();
	private static final Logger logger = Logger.getLogger(RequestContentConverter.class);
	private static final String PARAMS_DELIMITER = "&";
	private static final String KEY_VALUE_DELIMITER = "=";
	private static boolean throwExceptionOnConstructorCall = false;

	private RequestContentConverter() {
		if(throwExceptionOnConstructorCall) {
			throw new RuntimeException("Singleton constructor call");
		}
		throwExceptionOnConstructorCall = true;
	}
	
	public static RequestContentConverter getInstance() {
		return requestContentConverter;
	}
	
	
	
	public synchronized HttpServletRequestImpl convert(RequestContent content, Socket socket) {

		logger.debug("converting requestContet of socket: " + socket);
		
		List<String> plainContent = content.getPlainContent();
		
		HttpMethod httpMethod = extractHttpMethod(plainContent);
		String protocol = extractProtocol(plainContent);
		URI requestURI = extractURI(plainContent);
		StringBuffer requestURL = extractURL(plainContent);
		String contextPath = extractContextPath(plainContent);
		String queryString = extractQueryString(plainContent);
		Map<String, String> headers = extractHeaders(plainContent);
		Enumeration<String> headerNames = extractHeaderNames(plainContent);
		String remoteAddress = extractRemoteHost(socket);
		ServletInputStream servletInputStream = new ServletInputStreamImpl(socket);
		Map<String, String> parameters = extractParameters(content, httpMethod);
		ServletContext servletContext = URIResolver.anyAppRequest(requestURI);
		RequestType requestType = determineRequestType(requestURI, servletContext);
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
				.setServletContext(servletContext)
				.setRequestType(requestType)
				.setContextPath(contextPath)
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
		try {
			String firstLine = content.get(0);
			String[] parts = firstLine.split("\\s+");
			return parts[2];
		}catch(ArrayIndexOutOfBoundsException e) {
			logger.log("Protocol not found", LogLevel.ERROR);
			return "";
		}
	}

	URI extractURI(List<String> content) {
		//TODO it should work like in docs:
		/**
		Returns the part of this request's URL from the protocol name up to the query string in the first line of the HTTP request. The web container does not decode this String. For example:
		First line of HTTP request	Returned Value
		POST /some/path.html HTTP/1.1		/some/path.html
		GET http://foo.bar/a.html HTTP/1.0		/a.html
		HEAD /xyz?a=b HTTP/1.1		/xyz
		 */
		try {
			String queryString = extractQueryString(content);
			String[] queryStringParts = queryString.split("\\?");
			return new URI(queryStringParts[0]);
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
			String uriASCIIString = uri.toASCIIString();
			stringBUffer.append(uriASCIIString);
		} catch (UnknownHostException | NullPointerException exception) {
			logger.error(exception.getMessage());
		}
		return stringBUffer;
	}
	
	String extractContextPath(List<String> content) {
			String queryString = extractQueryString(content);
			String[] queryStringParts = queryString.split("\\?");
			return queryStringParts[0];
	}

	String extractQueryString(List<String> content) {
		String firstLine = content.get(0);
		String[] parts = firstLine.split("\\s+");
		return parts[1];
	}

	Map<String, String> extractHeaders(List<String> content) {
		Map<String, String> headers = new HashMap<>();

		Pattern pattern = Pattern.compile("(?<NAME>.+?):(?<VALUES>(.+?,?)*)");

		for (String s : content) {
			Matcher matcher = pattern.matcher(s);
			if (matcher.matches()) {
				String headerName = matcher.group("NAME");
				headers.put(headerName, matcher.group("VALUES"));
			}
		}
		return headers;
	}
	
	Enumeration<String> extractHeaderNames(List<String> content){
		
		List<String> names = new ArrayList<>();
		
		Pattern pattern = Pattern.compile("(?<NAME>.+?):(.+?,?)*");

		for (String s : content) {
			Matcher matcher = pattern.matcher(s);
			if (matcher.matches()) {
				String headerName = matcher.group("NAME");
				names.add(headerName);
			}
		}
		
		return new EnumerationImpl<>(names);
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
		Pattern pattern = Pattern.compile("^.*?\\?(?<PARAMS>.*?)\\s.*$");
		Matcher matcher = pattern.matcher(requestFirstLine);
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
			if(keyValuePairs.length == 2) {
				String key = keyValuePairs[0].trim();
				String value = keyValuePairs[1].trim();
				result.put(key, value);
			}
			else {
				logger.log("Couldn't extract param: " + p, LogLevel.WARN);
			}
		}
		return result;
	}
	
	RequestType determineRequestType(URI requestURI, ServletContext servletContext) {
		if(servletContext != null) {
			if(URIResolver.defaultAppPageRequest(requestURI)) {
				return RequestType.DEFAULT_APP_PAGE_REQUEST;
			}
			else {
				return RequestType.APP_PAGE_REQUEST;
			}
		}
		else if(URIResolver.serverInfoRequest(requestURI)) {
			return RequestType.SERVER_INFO_REQUEST;
		}
		else {
			return RequestType.UNKNOWN;
		}
	}
	
}
