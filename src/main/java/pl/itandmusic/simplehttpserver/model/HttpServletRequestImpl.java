package pl.itandmusic.simplehttpserver.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.Principal;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import pl.itandmusic.simplehttpserver.logger.Logger;

public class HttpServletRequestImpl implements HttpServletRequest {

	private final Logger logger = Logger.getLogger(HttpServletRequestImpl.class);
	private HttpMethod method;
	private URI requestURI;
	private String protocol;
	private StringBuffer requestURL;
	private String queryString;
	private Map<String,String> headers;
	private Enumeration<String> headerNames;
	private String remoteAddress;
	private ServletInputStream servletInputStream;
	private Map<String, String> parameters;
	private Map<String, Object> attributes;

	private HttpServletRequestImpl(HttpMethod method, URI requestURI, String protocol, StringBuffer requestURL,
			String queryString, Map<String,String> headers, Enumeration<String> headerNames,
			String remoteAddress, ServletInputStream servletInputStream, Map<String, String> parameters) {
		this.method = method;
		this.requestURI = requestURI;
		this.protocol = protocol;
		this.requestURL = requestURL;
		this.queryString = queryString;
		this.headers = headers;
		this.headerNames = headerNames;
		this.remoteAddress = remoteAddress;
		this.servletInputStream = servletInputStream;
		this.parameters = parameters;
	}

	@Override
	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		return new EnumerationImpl<String>(attributes.keySet());
	}

	@Override
	public String getCharacterEncoding() {
		return headers.get("Content-Encoding");
	}

	@Override
	public int getContentLength() {
		return Integer.parseInt(headers.getOrDefault("Content-Length", "0"));
	}

	@Override
	public String getContentType() {
		return headers.get("Content-Type");
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return servletInputStream;
	}

	@Override
	public String getLocalAddr() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLocalName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLocalPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration getLocales() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParameter(String name) {
		return parameters.get(name);
	}

	@Override
	public Map<String, String> getParameterMap() {
		return parameters;
	}

	@Override
	public Enumeration getParameterNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getParameterValues(String arg0) {
		return parameters.values().toArray(new String[0]);
	}

	@Override
	public String getProtocol() {
		return protocol;
	}

	@Override
	public BufferedReader getReader() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRealPath(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRemoteAddr() {
		return remoteAddress;
	}

	@Override
	public String getRemoteHost() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRemotePort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getScheme() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServerName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getServerPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isSecure() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeAttribute(String name) {
		attributes.remove(name);

	}

	@Override
	public void setAttribute(String name, Object value) {
		attributes.put(name, value);
	}

	@Override
	public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getAuthType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getContextPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cookie[] getCookies() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getDateHeader(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getHeader(String name) {
		return headers.get(name);
	}

	@Override
	public Enumeration<String> getHeaderNames() {
		return headerNames;
	}

	@Override
	public Enumeration<String> getHeaders(String name) {
		return new HeaderValues(headers.get(name));
	}

	@Override
	public int getIntHeader(String name) {
		int result = 0;
		try {
			result = Integer.parseInt(headers.get(name));
		}
		catch(Exception exception) {
			logger.warn("Couldn't get int header. Returning 0.");
		}
		return result;
	}

	@Override
	public String getMethod() {
		return method.toString();
	}

	@Override
	public String getPathInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPathTranslated() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getQueryString() {
		return queryString;
	}

	@Override
	public String getRemoteUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequestURI() {
		return requestURI.toASCIIString();
	}

	@Override
	public StringBuffer getRequestURL() {
		return requestURL;
	}

	@Override
	public String getRequestedSessionId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServletPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpSession getSession() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpSession getSession(boolean arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Principal getUserPrincipal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromUrl() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isUserInRole(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public static class Builder {
		private HttpMethod method;
		private String protocol;
		private URI requestURI;
		private StringBuffer requestURL;
		private String queryString;
		private Map<String,String> headers;
		private Enumeration<String> headerNames;
		private String remoteAddress;
		private ServletInputStream servletInputStream;
		private Map<String, String> parameters;
		
		public static Builder newBuilder() {
			return new Builder();
		}
		
		public HttpServletRequestImpl build() {
			return new HttpServletRequestImpl(method, requestURI, protocol, requestURL, 
					queryString, headers, headerNames, remoteAddress, servletInputStream,
					parameters);
		}

		public Builder setHttpMethod(HttpMethod method) {
			this.method = method;
			return this;
		}

		public Builder setProtocol(String protocol) {
			this.protocol = protocol;
			return this;
		}

		public Builder setRequestURI(URI requestURI) {
			this.requestURI = requestURI;
			return this;
		}

		public Builder setRequestURL(StringBuffer requestURL) {
			this.requestURL = requestURL;
			return this;
		}

		public Builder setQueryString(String queryString) {
			this.queryString = queryString;
			return this;
		}
		
		public Builder setHeaders(Map<String,String> headers) {
			this.headers = headers;
			return this;
		}
		
		public Builder setHeaderNames(Enumeration<String> headerNames) {
			this.headerNames = headerNames;
			return this;
		}
		
		public Builder setRemoteAddress(String remoteAddress) {
			this.remoteAddress = remoteAddress;
			return this;
		}
		
		public Builder setServletInputStream(ServletInputStream servletInputStream) {
			this.servletInputStream = servletInputStream;
			return this;
		}
		
		public Builder setParameters(Map<String, String> parameters) {
			this.parameters = parameters;
			return this;
		}
	}

}
