package pl.itandmusic.simplehttpserver.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import pl.itandmusic.simplehttpserver.configuration.Configuration;

public class Server {

	public static void start() throws IOException {
		
		ServerSocket serverSocket = new ServerSocket(Configuration.port);
		
		System.out.println("Server started.");
		
		Socket socket = serverSocket.accept();
		
		serverSocket.close();
		
		PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
		
		printWriter.println("HTTP/1.1 OK 200");
		printWriter.println("Content-Type: text/html");
		printWriter.println("\r\n");
		printWriter.print("<p> Hello World </p>");
		
		printWriter.close();
		
		socket.close();
		
	}
}
