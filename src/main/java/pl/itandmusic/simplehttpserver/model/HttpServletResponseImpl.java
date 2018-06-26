package pl.itandmusic.simplehttpserver.model;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class HttpServletResponseImpl implements HttpServletResponse {

	private String contentType;
	private Map<String, String> headers;
	private PrintWriter printWriter;
	private ServletOutputStream servletOutputStream;

	private HttpServletResponseImpl(String contentType, Map<String, String> headers, PrintWriter printWriter,
			ServletOutputStream servletOutputStream) {
		this.contentType = contentType;
		this.headers = headers;
		this.printWriter = printWriter;
		this.servletOutputStream = servletOutputStream;
	}

	@Override
	public void flushBuffer() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getBufferSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getCharacterEncoding() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getContentType() {
		return contentType;
	}

	@Override
	public Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return servletOutputStream;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		return printWriter;
	}

	@Override
	public boolean isCommitted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resetBuffer() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setBufferSize(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setCharacterEncoding(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setContentLength(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setContentType(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLocale(Locale arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addCookie(Cookie arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addDateHeader(String name, long value) {
		headers.put(name, String.valueOf(value));
	}

	@Override
	public void addHeader(String name, String value) {
		headers.put(name, value);
	}

	@Override
	public void addIntHeader(String name, int value) {
		headers.put(name, String.valueOf(value));
	}

	@Override
	public boolean containsHeader(String name) {
		return headers.get(name) != null;
	}

	@Override
	public String encodeRedirectURL(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String encodeRedirectUrl(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String encodeURL(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String encodeUrl(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendError(int arg0) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendError(int arg0, String arg1) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendRedirect(String arg0) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDateHeader(String arg0, long arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setHeader(String name, String value) {
		headers.put(name, value);
	}

	@Override
	public void setIntHeader(String name, int value) {
		headers.put(name, String.valueOf(value));
	}

	@Override
	public void setStatus(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setStatus(int arg0, String arg1) {
		// TODO Auto-generated method stub

	}
	
	public static class Builder{
		private String contentType;
		private Map<String, String> headers;
		private PrintWriter printWriter;
		private ServletOutputStream servletOutputStream;
		
		public HttpServletResponseImpl build() {
			
			return new HttpServletResponseImpl(contentType, headers, printWriter, servletOutputStream);
		}
		
		public Builder setContentType(String contentType) {
			this.contentType = contentType;
			return this;
		}
		
		public Builder setHeaders(Map<String, String> headers) {
			this.headers = headers;
			return this;
		}
		
		public Builder setPrintWriter(PrintWriter printWriter) {
			this.printWriter = printWriter;
			return this;
		}
		
		public Builder setServletOutputStream(ServletOutputStream servletOutputStream) {
			this.servletOutputStream = servletOutputStream;
			return this;
		}
	}

}
