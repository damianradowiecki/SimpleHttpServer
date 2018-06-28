package pl.itandmusic.simplehttpserver.response;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import pl.itandmusic.simplehttpserver.model.HttpServletRequestImpl;
import pl.itandmusic.simplehttpserver.model.HttpServletResponseImpl;
import pl.itandmusic.simplehttpserver.model.RequestContent;

public class ResponseGenerator {
	
	public HttpServletResponseImpl generate(RequestContent content, HttpServletRequestImpl request, Socket clientSocket){
		
		HttpServletResponseImpl.Builder builder = new HttpServletResponseImpl.Builder();
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(clientSocket.getOutputStream());
			writer.println("HTTP/1.1 200 OK");
			writer.println();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ServletOutputStream outputStream = null; // TODO ... clientSocket.getOutputStream();
		
		Map<String, String> responseHeaders = new HashMap<>();
		//responseHeaders.put("SESSION-ID", request.getSession().getId());
		
		return builder
				.setContentType("text/html")
				.setHeaders(responseHeaders)
				.setServletOutputStream(outputStream)
				.setPrintWriter(writer)
				.build();
	}
}
