package pl.itandmusic.simplehttpserver.response;

import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import pl.itandmusic.simplehttpserver.model.HttpServletRequestImpl;
import pl.itandmusic.simplehttpserver.model.HttpServletResponseImpl;

public class ResponseGenerator {
	
	public HttpServletResponseImpl generate(List<String> content, HttpServletRequestImpl request, Socket clientSocket) {
		
		HttpServletResponseImpl.Builder builder = new HttpServletResponseImpl.Builder();
		ServletOutputStream outputStream = null; // TODO ... clientSocket.getOutputStream();
		
		Map<String, String> responseHeaders = new HashMap<>();
		responseHeaders.put("SESSION-ID", request.getSession().getId());
		
		return builder
				.setContentType("text/html")
				.setHeaders(responseHeaders)
				.setServletOutputStream(outputStream)
				.build();
	}
}
