package pl.itandmusic.simplehttpserver.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import pl.itandmusic.simplehttpserver.configuration.Configuration;
import pl.itandmusic.simplehttpserver.logger.LogLevel;
import pl.itandmusic.simplehttpserver.logger.Logger;
import pl.itandmusic.simplehttpserver.request.RequestThread;

public class Server {

	private final static Logger logger = Logger.getLogger(Server.class);
	
	public static void start() throws IOException {
		
		logger.info("Server started.");
		
		try (ServerSocket serverSocket = new ServerSocket(Configuration.port)){
			while(!serverSocket.isClosed()) {
				logger.log("Waiting for connections", LogLevel.DEBUG);
				Socket socket = serverSocket.accept();	
				logger.log("Connection accepted", LogLevel.DEBUG);
				new Thread(new RequestThread(socket)).start();
			}
		}catch(IOException exception) {
			logger.error("Server stopped");
			logger.error("Error message: " + exception.getMessage());
		}finally {
			logger.info("Server stopped.");
		}
		
		
		
		
		
		
	}
}
