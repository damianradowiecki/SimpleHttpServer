package pl.itandmusic.simplehttpserver.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import pl.itandmusic.simplehttpserver.configuration.Configuration;
import pl.itandmusic.simplehttpserver.logger.Logger;
import pl.itandmusic.simplehttpserver.request.RequestThread;

public class Server {

	private final static Logger logger = Logger.getLogger(Server.class);
	
	public static void start() throws IOException {
		
		ServerSocket serverSocket = new ServerSocket(Configuration.port);
		
		logger.info("Server started.");
		
		while(!serverSocket.isClosed()) {
			Socket socket = serverSocket.accept();	
			new Thread(new RequestThread(socket)).start();
		}
		
		serverSocket.close();
		
		logger.info("Server stopped.");
		
		
		
	}
}
