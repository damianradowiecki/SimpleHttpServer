package pl.itandmusic.simplehttpserver.request;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import pl.itandmusic.simplehttpserver.configuration.Configuration;
import pl.itandmusic.simplehttpserver.configuration.web.WebConfigurationLoader;
import pl.itandmusic.simplehttpserver.logger.Logger;
import pl.itandmusic.simplehttpserver.model.HttpServletRequestImpl;
import pl.itandmusic.simplehttpserver.model.HttpServletResponseImpl;
import pl.itandmusic.simplehttpserver.model.RequestContent;
import pl.itandmusic.simplehttpserver.utils.URIResolver;

public class RequestThread implements Runnable {

	private static final Logger logger = Logger.getLogger(WebConfigurationLoader.class);
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

		RequestContent content = requestContentReader.read(socket);
		if (content.empty()) {
			return;
		}
		request = requestContentConverter.convert(content, socket);
		response = new HttpServletResponseImpl();

		if (URIResolver.requestForDeafultPage(request)) {
			tryToLoadDefaultPage(socket);
		} 
		else if(URIResolver.requestForServerInfoPage(request)) {
			tryToLoadServerPage(socket);
		}
		else if(URIResolver.requestForResourceOnServer(request)){
			try {
				Class<?> servletClass = loadClass(request);
				Servlet servlet = (Servlet) servletClass.newInstance();
				servlet.service(request, response);
				sendResponse(socket, response);
			} catch (InstantiationException | IllegalAccessException | ServletException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		else {
			String requestURI = URIResolver.getRequsetURI(request);
			logger.warn("Resource not found: " + requestURI);
		}

		tryToCloseSocket(socket);
	}
	
	private void tryToCloseSocket(Socket socket) {
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sendResponse(Socket socket, HttpServletResponseImpl response) throws IOException {
		OutputStream os = socket.getOutputStream();
		PrintWriter writer = new PrintWriter(os);

		writer.println("HTTP/1.1 200 OK");
		writer.println("Content-Type: " + response.getContentType());
		writer.println();

		StringBuffer servletPrintContent = response.getStringWriter().getBuffer();

		writer.print(servletPrintContent);

		writer.flush();
		writer.close();

	}
	
	private void tryToLoadDefaultPage(Socket socket) {
		try {
			loadDefaultPage(socket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	private void loadDefaultPage(Socket socket) throws IOException {
		String appDirectory = Configuration.appDirectory;
		Path path = Paths.get(appDirectory);
		if (Files.exists(path)) {
			for (String dp : Configuration.defaultPages) {
				Path page = Paths.get(dp);
				Path fullPath = path.resolve(page);
				if (Files.exists(fullPath)) {
					BufferedReader reader = new BufferedReader(new FileReader(fullPath.toFile()));
					OutputStream os = socket.getOutputStream();
					PrintWriter writer = new PrintWriter(os);

					writer.println("HTTP/1.1 200 OK");
					writer.println("Content-Type: text/html");
					writer.println();

					String line;
					while ((line = reader.readLine()) != null) {
						writer.println(line);
					}
					writer.flush();
					writer.close();

					reader.close();

					return;
				}
			}
		}

	}
	
	private void tryToLoadServerPage(Socket socket) {
		try {
			loadServerPage(socket);;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void loadServerPage(Socket socket) throws IOException {

		OutputStream os = socket.getOutputStream();
		PrintWriter writer = new PrintWriter(os);

		writer.println("HTTP/1.1 200 OK");
		writer.println("Content-Type: text/html");
		writer.println();
		writer.println("<html>");
		writer.println("<head>");
		writer.println("<title>" + Configuration.serverName + "</title>");
		writer.println("</head>");
		writer.println("<body>");
		writer.println("Server is running on port " + Configuration.port);
		writer.println("</body>");
		writer.println("</html>");

		writer.flush();
		writer.close();

		return;

	}
	
	private Class<?> loadClass(HttpServletRequestImpl request) {
		String requestURI = URIResolver.getRequsetURI(request);
		return Configuration.servletsMappings.get(requestURI);
	}

}
