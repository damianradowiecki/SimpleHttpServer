package pl.itandmusic.simplehttpserver.request;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.itandmusic.simplehttpserver.model.HttpMethod;
import pl.itandmusic.simplehttpserver.model.HttpServletRequestImpl;

public class RequestContentConverter {

	private HttpMethod httpMethod;
	private String protocol;
	private URI requestURI;
	private Pattern pattern;
	private Matcher matcher;

	public HttpServletRequestImpl convert(List<String> content) {

		httpMethod = extractHttpMethod(content);
		protocol = extractProtocol(content);
		requestURI = extractURI(content);

		HttpServletRequestImpl.Builder builder = new HttpServletRequestImpl.Builder();

		return 
			builder
			.setHttpMethod(httpMethod)
			.setProtocol(protocol)
			.setRequestURI(requestURI)
			.build();
	}

	HttpMethod extractHttpMethod(List<String> content) {
		String firstLine = content.get(0);
		for (HttpMethod m : HttpMethod.values()) {
			if (firstLine.contains(m.toString())) {
				return m;
			}
		}
		return HttpMethod.UNKNOWN;
	}

	String extractProtocol(List<String> content) {
		String firstLine = content.get(0);
		pattern = Pattern.compile(".*(?<PROTOCOL>HTTP/\\d\\.\\d).*");
		matcher = pattern.matcher(firstLine);
		matcher.matches();
		return matcher.group("PROTOCOL");
	}

	URI extractURI(List<String> content) {
		try {
			String firstLine = content.get(0);
			String [] parts = firstLine.split("\\s+");
			String uriString = parts[1];
			URI uri = new URI(uriString);
			return uri;
		} catch (URISyntaxException uriSyntaxException) {
			return null;
		}

	}
}
