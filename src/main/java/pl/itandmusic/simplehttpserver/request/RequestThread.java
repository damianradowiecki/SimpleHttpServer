package pl.itandmusic.simplehttpserver.request;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import pl.itandmusic.simplehttpserver.model.HttpServletRequestImpl;

public class RequestThread implements Runnable {

	private Socket socket;
	private RequestContentReader requestContentReader;
	private RequestContentConverter requestContentConverter;

	public RequestThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		PrintWriter printWriter;
		requestContentReader = new RequestContentReader();
		requestContentConverter = new RequestContentConverter();
		try {
			
			List<String> content = requestContentReader.read(socket);
			HttpServletRequestImpl request = requestContentConverter.convert(content, socket);
			
			printWriter = new PrintWriter(socket.getOutputStream());
			printWriter.println("HTTP/1.1 OK 200");
			printWriter.println("Content-Type: text/html");
			printWriter.println("\r\n");
			printWriter.print("<p> " + request.getMethod() + " || " + request.getRequestURI() 
			+ " || " + request.getProtocol() 
			+ " || " + request.getRequestURL().toString() + " || " +" </p>");

			printWriter.close();

			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
