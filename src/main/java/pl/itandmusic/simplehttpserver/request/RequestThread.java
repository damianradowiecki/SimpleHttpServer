package pl.itandmusic.simplehttpserver.request;

import java.io.IOException;
import java.net.Socket;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import pl.itandmusic.simplehttpserver.configuration.Configuration;
import pl.itandmusic.simplehttpserver.logger.Logger;
import pl.itandmusic.simplehttpserver.model.HttpServletRequestImpl;
import pl.itandmusic.simplehttpserver.model.HttpServletResponseImpl;
import pl.itandmusic.simplehttpserver.model.RequestContent;
import pl.itandmusic.simplehttpserver.model.ServletContext;
import pl.itandmusic.simplehttpserver.response.ResponseSendingService;
import pl.itandmusic.simplehttpserver.utils.URIResolver;
import pl.itandmusic.simplehttpserver.utils.URIUtils;

public class RequestThread implements Runnable {

	private final Logger logger = Logger.getLogger(RequestThread.class);
	private Socket socket;
	private HttpServletRequestImpl servletRequest;
	private HttpServletResponseImpl servletResponse;
	private RequestContentReader requestContentReader;
	private RequestContentConverter requestContentConverter;
	private ResponseSendingService responseSendingService;
	private ServletContext servletContext;

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
		
		//TODO use flag requestType from request
		if (URIResolver.serverInfoRequest(servletRequest)) {
			responseSendingService.tryToLoadServerPage(socket);
		} 
		else if(URIResolver.defaultAppPageRequest(servletRequest)) {
			//maybe this method should be in service?
			loadAppDefaultPage();
		}
		else if(URIResolver.anyAppRequest(servletRequest)){
			//maybe this method should be in service?
			tryToServiceRequestUsingServlet(content);
		}
		else {
			responseSendingService.tryToSendPageNotFoundResponse(socket);
		}
		
		tryToCloseSocket(socket);

	}
	
	private void loadAppDefaultPage() {

		loadAppConfig();
		
		if(URIResolver.properDefaultAppPageRequest(servletRequest)) {
			responseSendingService.tryToLoadDefaultPage(socket, servletContext);
		}
		else {
			String URI = URIResolver.getRequestURI(servletRequest);
			String correctedURI = URIUtils.correctUnproperDefaultPageURI(URI);
			responseSendingService.tryToSendRedirectResponse(socket,  correctedURI);
		}
	}
	
	private void tryToServiceRequestUsingServlet(RequestContent content) {
		try {
			
			loadAppConfig();
			
			servletRequest = requestContentConverter.convert(content, socket);
			servletResponse = new HttpServletResponseImpl();			
			Servlet servlet = servletContext.getServletByUrlPattern(servletRequest.getRequestURI());
			servlet.service(servletRequest, servletResponse);
			
			if (servletResponse.isRedirectResponse()) {
				responseSendingService.sendRedirectResponse(socket, servletResponse.getRedirectURL());
			} else {
				responseSendingService.sendOKResponse(socket, servletResponse);
			}

		} catch (ServletException | IOException e) {
			
			logger.error("Could not service request using servlet.");
			logger.error("Error message: " + e.getMessage());
			
			responseSendingService.tryToSendInternalErrorResponse(socket);
		}
	}

	private void tryToCloseSocket(Socket socket) {
		try {
			socket.close();
		} catch (IOException e) {
			logger.warn("Conuld not close socket");
		}
	}
	
	private void loadAppConfig() {
		String requestURI = URIResolver.getRequestURI(servletRequest);
		for(String an : Configuration.applications.keySet()) {
			//TODO change this condition. It can make some problems
			if(requestURI.contains(an)) {
				this.servletContext = Configuration.applications.get(an);
			}
		}
	}

}
