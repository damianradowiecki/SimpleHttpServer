package pl.itandmusic.simplehttpserver.response;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import pl.itandmusic.simplehttpserver.configuration.Configuration;
import pl.itandmusic.simplehttpserver.logger.LogLevel;
import pl.itandmusic.simplehttpserver.logger.Logger;
import pl.itandmusic.simplehttpserver.model.HttpServletRequestImpl;
import pl.itandmusic.simplehttpserver.model.HttpServletResponseImpl;
import pl.itandmusic.simplehttpserver.model.RequestContent;
import pl.itandmusic.simplehttpserver.model.ServletContext;
import pl.itandmusic.simplehttpserver.utils.URIResolver;
import pl.itandmusic.simplehttpserver.utils.URIUtils;

public class ResponseSendingService {

	private static final Logger logger = Logger.getLogger(ResponseSendingService.class);
	private static ResponseSendingService responseSendingService;
	
	private ResponseSendingService() {}
	
	public static ResponseSendingService getResponseSendingService() {
		if(responseSendingService == null) {
			responseSendingService = new ResponseSendingService();
		}
		return responseSendingService;
	}
	
	public void sendOKResponse(Socket socket, HttpServletResponseImpl response) throws IOException {

		OutputStream os = socket.getOutputStream();
		PrintWriter writer = new PrintWriter(os);
		String mainHeader = "HTTP/1.1 200 OK";

		writer.println(mainHeader);

		for (String headerName : response.getHeaders().keySet()) {
			String header = headerName + " : " + response.getHeaders().get(headerName);
			writer.println(header);
		}

		writer.println();

		StringWriter stringWriter = response.getStringWriter();
		StringBuffer stringBuffer = stringWriter.getBuffer();

		writer.print(stringBuffer);

		writer.close();

	}

	public void tryToSendRedirectResponse(Socket socket, String redirectURL) {
		try {
			sendRedirectResponse(socket, redirectURL);
		} catch (IOException e) {
			logger.warn("Could not send redirect response to " + redirectURL);
			logger.logException(e, LogLevel.WARN);
			e.printStackTrace();
		}
	}

	public void sendRedirectResponse(Socket socket, String redirectURL) throws IOException {
		OutputStream os = socket.getOutputStream();

		String mainHeader = "HTTP/1.1 302 Found";
		String newLine = "\n";
		String locationHeader = "Location" + " : " + redirectURL;

		os.write(mainHeader.getBytes());
		os.write(newLine.getBytes());
		os.write(locationHeader.getBytes());
		os.write(newLine.getBytes());

		os.close();

	}

	public void tryToSendInternalErrorResponse(Socket socket) {
		try {
			sendInternalErrorResponse(socket);
		} catch (IOException e) {
			logger.warn("Could not send internal error response.");
			logger.logException(e, LogLevel.WARN);
		}
	}

	public void sendInternalErrorResponse(Socket socket) throws IOException {
		OutputStream os = socket.getOutputStream();

		String error500Header = "HTTP/1.1 500 Internal Server Error";
		String newLine = "\n";

		os.write(error500Header.getBytes());
		os.write(newLine.getBytes());

		os.close();

	}

	public void tryToSendPageNotFoundResponse(Socket socket) {
		try {
			sendPageNotFoundResponse(socket);
		} catch (IOException e) {
			logger.warn("Could not send page not found response");
			logger.logException(e, LogLevel.WARN);
		}
	}

	public void sendPageNotFoundResponse(Socket socket) throws IOException {
		OutputStream os = socket.getOutputStream();

		String error500Header = "HTTP/1.1 404 Not Found";
		String newLine = "\n";

		os.write(error500Header.getBytes());
		os.write(newLine.getBytes());

		os.close();
	}

	public void tryToLoadDefaultPage(Socket socket, ServletContext servletContext) {
		try {
			loadDefaultPage(socket, servletContext);
		} catch (IOException e) {
			logger.warn("Could not load default page");
			logger.logException(e, LogLevel.WARN);
		}
	}

	public void loadDefaultPage(Socket socket, ServletContext servletContext) throws IOException {
		String appDirectory = servletContext.getAppPath();
		Path path = Paths.get(appDirectory);
		if (Files.exists(path)) {
			for (String dp : servletContext.getDefaultPages()) {
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

	public void tryToLoadServerPage(Socket socket) {
		try {
			loadServerPage(socket);
			;
		} catch (IOException e) {
			logger.warn("Could not load server page");
			logger.logException(e, LogLevel.WARN);
		}
	}

	public void loadServerPage(Socket socket) throws IOException {

		OutputStream os = socket.getOutputStream();
		PrintWriter writer = new PrintWriter(os);

		writer.println("HTTP/1.1 200 OK");
		writer.println("Content-Type: text/html");
		writer.println();
		writer.println("<html>");
		writer.println("<head>");
		writer.println("<title>" + Configuration.SERVER_INFO + "</title>");
		writer.println("</head>");
		writer.println("<body>");
		writer.println("Server is running on port " + Configuration.port);
		writer.println("</body>");
		writer.println("</html>");

		writer.flush();
		writer.close();

		return;

	}
	
	public void loadAppDefaultPage(HttpServletRequestImpl servletRequest, Socket socket) {

		ServletContext servletContext = loadAppConfig(servletRequest);
		
		if(URIResolver.properDefaultAppPageRequest(servletRequest)) {
			tryToLoadDefaultPage(socket, servletContext);
		}
		else {
			String URI = URIResolver.getRequestURI(servletRequest);
			String correctedURI = URIUtils.correctUnproperDefaultPageURI(URI);
			tryToSendRedirectResponse(socket,  correctedURI);
		}
	}
	
	public void tryToServiceRequestUsingServlet(HttpServletRequestImpl servletRequest, Socket socket, RequestContent content) {
		try {
			
			ServletContext servletContext = loadAppConfig(servletRequest);
			
			HttpServletResponseImpl servletResponse = new HttpServletResponseImpl();			
			Servlet servlet = servletContext.getServletByUrlPattern(servletRequest.getRequestURI());
			servlet.service(servletRequest, servletResponse);
			
			if (servletResponse.isRedirectResponse()) {
				sendRedirectResponse(socket, servletResponse.getRedirectURL());
			} else {
				sendOKResponse(socket, servletResponse);
			}

		} catch (ServletException | IOException e) {
			
			logger.error("Could not service request using servlet.");
			logger.error("Error message: " + e.getMessage());
			
			tryToSendInternalErrorResponse(socket);
		}
	}
	
	private ServletContext loadAppConfig(HttpServletRequestImpl servletRequest) {
		String requestURI = URIResolver.getRequestURI(servletRequest);
		for(String an : Configuration.applications.keySet()) {
			//TODO change this condition. It can make some problems
			if(requestURI.contains(an)) {
				return Configuration.applications.get(an);
			}
		}
		return null;
	}
}
