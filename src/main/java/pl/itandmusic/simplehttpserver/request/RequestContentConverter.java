package pl.itandmusic.simplehttpserver.request;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.itandmusic.simplehttpserver.model.HeaderValues;
import pl.itandmusic.simplehttpserver.model.HttpMethod;
import pl.itandmusic.simplehttpserver.model.HttpServletRequestImpl;

public class RequestContentConverter {

	private Pattern pattern;
	private Matcher matcher;
	private HttpMethod httpMethod;
	private String protocol;
	private URI requestURI;
	private StringBuffer requestURL;
	private String queryString;
	private Map<String, Enumeration<String>> headers;

	public HttpServletRequestImpl convert(List<String> content) {

		httpMethod = extractHttpMethod(content);
		protocol = extractProtocol(content);
		requestURI = extractURI(content);
		requestURL = extractURL(content);
		queryString = extractQueryString(content);
		headers = extractHeaders(content);

		HttpServletRequestImpl.Builder builder = new HttpServletRequestImpl.Builder();

		return builder.setHttpMethod(httpMethod).setProtocol(protocol).setRequestURI(requestURI)
				.setRequestURL(requestURL).setQueryString(queryString).setHeaders(headers).build();
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
			System.out.println(exception.getMessage());
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
}