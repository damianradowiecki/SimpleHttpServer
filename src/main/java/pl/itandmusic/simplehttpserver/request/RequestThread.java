package pl.itandmusic.simplehttpserver.request;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import pl.itandmusic.simplehttpserver.configuration.Configuration;
import pl.itandmusic.simplehttpserver.logger.LogLevel;
import pl.itandmusic.simplehttpserver.logger.Logger;
import pl.itandmusic.simplehttpserver.model.HttpServletRequestImpl;
import pl.itandmusic.simplehttpserver.model.HttpServletResponseImpl;
import pl.itandmusic.simplehttpserver.model.RequestContent;
import pl.itandmusic.simplehttpserver.response.ResponseGenerator;

public class RequestThread implements Runnable {

	private Socket socket;
	private HttpServletRequestImpl request;
	private HttpServletResponseImpl response;
	private RequestContentReader requestContentReader;
	private RequestContentConverter requestContentConverter;
	private ResponseGenerator responseGenerator;

	public RequestThread(Socket socket) {
		this.socket = socket;
		this.requestContentReader = new RequestContentReader();
		this.requestContentConverter = new RequestContentConverter();
		this.responseGenerator = new ResponseGenerator();
	}

	@Override
	public void run() {
		

		RequestContent content = requestContentReader.read(socket);
		if(content.empty()) {
			return;
		}
		request = requestContentConverter.convert(content, socket);
		response = responseGenerator.generate(content, request, socket);
		String  requestURI = request.getRequestURI().toString();
		
		if(requestURI.equals("/") || requestURI.equals("\\")) {
			try {
				loadDefaultPage(socket);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			try {
				Class<?> servletClass = Configuration.servletsMappings.get(requestURI);
				if(servletClass == null) {
					Logger.log("Resource not found: " + requestURI, LogLevel.WARN);
					return;
				}
				Servlet servlet = (Servlet)servletClass.newInstance();
				servlet.service(request, response);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadDefaultPage(Socket socket) throws IOException {
		String appDirectory = Configuration.appDirectory;
		Path path = Paths.get(appDirectory);
		if(Files.exists(path)) {
			for(String dp : Configuration.defaultPages) {
				Path page = Paths.get(dp);
				Path fullPath = path.resolve(page);
				if(Files.exists(fullPath)) {
					BufferedReader reader = new BufferedReader(new FileReader(fullPath.toFile()));
					OutputStream os = socket.getOutputStream();
					PrintWriter writer = new PrintWriter(os);
					
					
					writer.println("HTTP/1.1 200 OK");
					writer.println("Content-Type: text/html");
					writer.println();
					
					String line;
					while((line = reader.readLine()) != null) {
						writer.println(line);
						writer.flush();
					}
					writer.flush();
					writer.close();
					
					reader.close();
					
					return;
				}
			}
		}
		
	}

}
