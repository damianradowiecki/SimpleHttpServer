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
import java.util.List;

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

		if (URIResolver.serverInfoRequest(request)) {
			tryToLoadServerPage();

		} else {
			if (loadAppConfig()) {
				if (URIResolver.defaultAppPageRequest(request)) {
					tryToLoadDefaultPage();
				} else {
					tryToSendResponse();
				}
			}
		}

		tryToCloseSocket(socket);

	}
	
	private void tryToSendResponse() {
		try {

			RequestContent content = requestContentReader.read(socket);
			if (content.empty()) {
				return;
			}
			request = requestContentConverter.convert(content, socket);
			response = new HttpServletResponseImpl();

			Class<?> servletClass = loadClass(request);
			Servlet servlet = (Servlet) servletClass.newInstance();
			servlet.service(request, response);
			if (response.isRedirectResponse()) {
				sendRedirectResponse(socket, response.getRedirectURL());
			} else {
				sendResponse(socket, response);
			}

		} catch (InstantiationException | IllegalAccessException | ServletException | IOException e) {
			try {
				sendInternalErrorResponse(socket);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
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

		String mainHeader = "HTTP/1.1 200 OK";
		String newLine = "\n";

		os.write(mainHeader.getBytes());
		os.write(newLine.getBytes());
		for (String headerName : response.getHeaders().keySet()) {
			String header = headerName + " : " + response.getHeaders().get(headerName);
			os.write(header.getBytes());
			os.write(newLine.getBytes());
		}

		os.write(newLine.getBytes());

		List<Integer> buffer = response.getOutputStreamBuffer().getBuffer();

		for (int i : buffer) {
			os.write(i);
		}

		os.close();

	}

	private void sendRedirectResponse(Socket socket, String redirectURL) throws IOException {
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

	private void sendInternalErrorResponse(Socket socket) throws IOException {
		OutputStream os = socket.getOutputStream();

		String error500Header = "HTTP/1.1 500 Internal Server Error";
		String newLine = "\n";

		os.write(error500Header.getBytes());
		os.write(newLine.getBytes());

		os.close();

	}

	private void tryToLoadDefaultPage() {
		try {
			loadDefaultPage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void loadDefaultPage() throws IOException {
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

	private void tryToLoadServerPage() {
		try {
			loadServerPage();
			;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void loadServerPage() throws IOException {

		OutputStream os = socket.getOutputStream();
		PrintWriter writer = new PrintWriter(os);

		writer.println("HTTP/1.1 200 OK");
		writer.println("Content-Type: text/html");
		writer.println();
		writer.println("<html>");
		writer.println("<head>");
		writer.println("<title>" + Configuration.SERVER_NAME + "</title>");
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
	
	private boolean loadAppConfig() {
		String requestURI = URIResolver.getRequsetURI(request);
	}

}
