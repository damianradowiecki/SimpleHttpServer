package pl.itandmusic.simplehttpserver.request;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import pl.itandmusic.simplehttpserver.model.HttpServletRequestImpl;
import pl.itandmusic.simplehttpserver.model.HttpServletResponseImpl;

public class RequestThread implements Runnable {

	private Socket socket;
	private HttpServletRequestImpl request;
	private HttpServletResponseImpl response;
	private RequestContentReader requestContentReader;
	private RequestContentConverter requestContentConverter;

	public RequestThread(Socket socket) {
		this.socket = socket;
		this.requestContentReader = new RequestContentReader();
		this.requestContentConverter = new RequestContentConverter();
	}

	@Override
	public void run() {
		List<String> content = requestContentReader.read(socket);
		request = requestContentConverter.convert(content, socket);
		
	}

}
