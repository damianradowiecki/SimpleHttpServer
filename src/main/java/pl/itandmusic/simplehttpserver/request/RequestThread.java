package pl.itandmusic.simplehttpserver.request;

import java.io.IOException;
import java.net.Socket;

import pl.itandmusic.simplehttpserver.enummeration.RequestType;
import pl.itandmusic.simplehttpserver.logger.LogLevel;
import pl.itandmusic.simplehttpserver.logger.Logger;
import pl.itandmusic.simplehttpserver.model.HttpServletRequestImpl;
import pl.itandmusic.simplehttpserver.model.RequestContent;
import pl.itandmusic.simplehttpserver.response.ResponseSendingService;

public class RequestThread implements Runnable {

	private final Logger logger = Logger.getLogger(RequestThread.class);
	private Socket socket;
	private HttpServletRequestImpl servletRequest;
	private RequestContentReader requestContentReader;
	private RequestContentConverter requestContentConverter;
	private ResponseSendingService responseSendingService;

	public RequestThread(Socket socket) {
		this.socket = socket;
		this.requestContentReader = RequestContentReader.getRequestContentReader();
		this.requestContentConverter = RequestContentConverter.getRequestContentConverter();
		this.responseSendingService = ResponseSendingService.getResponseSendingService();
	}

	@Override
	public void run() {
		
		logger.log("RequestThread started", LogLevel.DEBUG);

		RequestContent content = requestContentReader.read(socket);
		
		if (content.empty()) {
			return;
		}
		
		servletRequest = requestContentConverter.convert(content, socket);
		
		if (servletRequest.getRequestType().equals(RequestType.SERVER_INFO_REQUEST)) {
			logger.debug("server info requested");
			responseSendingService.tryToLoadServerPage(socket);
		} 
		else if(servletRequest.getRequestType().equals(RequestType.DEFAULT_APP_PAGE_REQUEST)) {
			logger.debug("default app page requested");
			responseSendingService.loadAppDefaultPage(servletRequest, socket);	
		}
		else if(servletRequest.getRequestType().equals(RequestType.APP_PAGE_REQUEST)){
			logger.debug("app page requested");
			responseSendingService.tryToServiceRequestUsingServlet(servletRequest, socket, content);
		}
		else {
			logger.debug("app page not found");
			responseSendingService.tryToSendPageNotFoundResponse(socket);
		}
		
		tryToCloseSocket(socket);

	}

	private void tryToCloseSocket(Socket socket) {
		try {
			logger.debug("closing socket: " + socket);
			socket.close();
		} catch (IOException e) {
			logger.warn("Conuld not close socket");
		}
	}


}
