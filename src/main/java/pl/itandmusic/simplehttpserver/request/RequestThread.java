package pl.itandmusic.simplehttpserver.request;

import java.io.IOException;
import java.net.Socket;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import pl.itandmusic.simplehttpserver.configuration.AppConfig;
import pl.itandmusic.simplehttpserver.configuration.Configuration;
import pl.itandmusic.simplehttpserver.configuration.web.WebConfigurationLoader;
import pl.itandmusic.simplehttpserver.logger.Logger;
import pl.itandmusic.simplehttpserver.model.HttpServletRequestImpl;
import pl.itandmusic.simplehttpserver.model.HttpServletResponseImpl;
import pl.itandmusic.simplehttpserver.model.RequestContent;
import pl.itandmusic.simplehttpserver.response.ResponseSendingService;
import pl.itandmusic.simplehttpserver.utils.URIResolver;
import pl.itandmusic.simplehttpserver.utils.URIUtils;

public class RequestThread implements Runnable {

	private static final Logger logger = Logger.getLogger(WebConfigurationLoader.class);
	private Socket socket;
	private HttpServletRequestImpl request;
	private HttpServletResponseImpl response;
	private RequestContentReader requestContentReader;
	private RequestContentConverter requestContentConverter;
	private ResponseSendingService responseSendingService;
	private AppConfig appConfig;

	public RequestThread(Socket socket) {
		this.socket = socket;
		this.requestContentReader = new RequestContentReader();
		this.requestContentConverter = new RequestContentConverter();
		this.responseSendingService = new ResponseSendingService();
	}

	@Override
	public void run() {

		RequestContent content = requestContentReader.read(socket);
		
		if (content.empty()) {
			return;
		}
		
		request = requestContentConverter.convert(content, socket);
		
		if (URIResolver.serverInfoRequest(request)) {
			responseSendingService.tryToLoadServerPage(socket);
		} 
		else if(URIResolver.defaultAppPageRequest(request)) {
			loadAppConfig();
			loadAppDefaultPage();
		}
		else if(URIResolver.knownAppRequest(request)){
			loadAppConfig();
			tryToSendResponse();
		}
		else {
			responseSendingService.tryToSendPageNotFoundResponse(socket);
		}

		tryToCloseSocket(socket);

	}
	
	private void loadAppDefaultPage() {

		if(URIResolver.properDefaultAppPageRequest(request)) {
			responseSendingService.tryToLoadDefaultPage(socket, appConfig);
		}
		else {
			String URI = URIResolver.getRequsetURI(request);
			String correctedURI = URIUtils.correctUnproperDefaultPageURI(URI);
			responseSendingService.tryToSendRedirectResponse(socket,  correctedURI);
		}
	}
	
	private void tryToSendResponse() {
		try {

			
			response = new HttpServletResponseImpl();

			Class<?> servletClass = loadClass(request);
			Servlet servlet = (Servlet) servletClass.newInstance();
			servlet.service(request, response);
			if (response.isRedirectResponse()) {
				responseSendingService.sendRedirectResponse(socket, response.getRedirectURL());
			} else {
				responseSendingService.sendResponse(socket, response);
			}

		} catch (InstantiationException | IllegalAccessException | ServletException | IOException e) {
			try {
				responseSendingService.sendInternalErrorResponse(socket);
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

	

	private Class<?> loadClass(HttpServletRequestImpl request) {
		String requestURI = URIResolver.getRequsetURI(request);
		return appConfig.getServletsMappings().get(requestURI);
	}
	
	private void loadAppConfig() {
		String requestURI = URIResolver.getRequsetURI(request);
		for(String an : Configuration.applications.keySet()) {
			if(requestURI.contains(an)) {
				this.appConfig = Configuration.applications.get(an);
			}
		}
	}

}
