package pl.itandmusic.simplehttpserver.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import pl.itandmusic.simplehttpserver.configuration.Configuration;
import pl.itandmusic.simplehttpserver.logger.LogLevel;
import pl.itandmusic.simplehttpserver.logger.Logger;
import pl.itandmusic.simplehttpserver.request.RequestHandler;

public class ThreadPoolServer {

	private final static Logger logger = Logger.getLogger(ThreadPoolServer.class);
	
	public static void start() throws IOException {
		
		Executor executor = Executors.newCachedThreadPool();
		
		logger.info("Server started.");
		
		try (ServerSocket serverSocket = new ServerSocket(Configuration.port)){
			while(!serverSocket.isClosed()) {
				logger.log("Waiting for connections", LogLevel.DEBUG);
				Socket socket = serverSocket.accept();	
				logger.log("Connection accepted", LogLevel.DEBUG);
				executor.execute(new RequestHandler(socket));
			}
		}catch(IOException exception) {
			logger.error("Server stopped");
			logger.error("Error message: " + exception.getMessage());
		}finally {
			logger.info("Server stopped.");
		}
		
		
		
		
		
		
	}
}
