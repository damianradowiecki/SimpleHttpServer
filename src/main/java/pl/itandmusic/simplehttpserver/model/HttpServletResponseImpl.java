package pl.itandmusic.simplehttpserver.model;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import pl.itandmusic.simplehttpserver.buffer.ResponseOutputStreamBuffer;
import pl.itandmusic.simplehttpserver.utils.CookieConverter;

public class HttpServletResponseImpl implements HttpServletResponse {

	private String contentType = "text/html";
	private Map<String, String> headers = new HashMap<>();
	private ResponseOutputStreamBuffer outputStreamBuffer = new ResponseOutputStreamBuffer();
	private StringWriter writer = new StringWriter();
	private boolean redirectResponse;
	private String redirectURL;

	@Override
	public void flushBuffer() throws IOException {
		// TODO go to sending response
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
	
	public ResponseOutputStreamBuffer getOutputStreamBuffer() {
		return outputStreamBuffer;
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return outputStreamBuffer;
	}
	
	public StringWriter getStringWriter() {
		return this.writer;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		return new PrintWriter(writer);
	}

	@Override
	public boolean isCommitted() {
		return outputStreamBuffer.isCommited();
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
	public void setContentType(String contentType) {
		setHeader("Content-type", contentType);
	}

	@Override
	public void setLocale(Locale arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addCookie(Cookie cookie) {
		Pair<String, String> pair = CookieConverter.convertToKeyValuePair(cookie);
		addHeader(pair.getKey(), pair.getValue());
	}

	@Override
	public void addDateHeader(String name, long value) {
		headers.put(name, String.valueOf(value));
	}

	@Override
	public void addHeader(String name, String value) {
		String header = headers.get(name);
		if(header == null) {
			headers.put(name, value);
		}
		else {
			header += "," + value;
			headers.put(name, header);
		}
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
	public void sendRedirect(String url) throws IOException {
		if(outputStreamBuffer.isCommited()) {
			throw new IllegalStateException("Do not send data before redirecting");
		}
		else {
			this.redirectResponse = true;
			this.redirectURL = url;
		}
		
	}
	
	public boolean isRedirectResponse() {
		return redirectResponse;
	}

	public String getRedirectURL() {
		return redirectURL;
	}

	@Override
	public void setDateHeader(String arg0, long arg1) {
		// TODO Auto-generated method stub

	}
	
	public Map<String, String> getHeaders(){
		return headers;
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

}
