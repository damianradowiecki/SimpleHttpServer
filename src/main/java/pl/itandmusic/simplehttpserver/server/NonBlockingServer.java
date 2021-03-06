package pl.itandmusic.simplehttpserver.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import pl.itandmusic.simplehttpserver.configuration.Configuration;
import pl.itandmusic.simplehttpserver.logger.Logger;
import pl.itandmusic.simplehttpserver.request.NonBlockingRequestHandler;

public class NonBlockingServer {

	private static final Logger logger = Logger.getLogger(NonBlockingServer.class);
	private static Map<SocketChannel, ByteBuffer> sockets = new ConcurrentHashMap<>();
	private static final int BUFFER_SIZE = 110;
	private StringBuilder requestContent = new StringBuilder(1000);
	
	
	private NonBlockingServer() {
		throw new RuntimeException("Consturctor call exception");
	}
	
	public static void start() throws IOException {
		
		NonBlockingRequestHandler nonBlockingRequestHandler = NonBlockingRequestHandler.getInstance();

		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

		serverSocketChannel.bind(new InetSocketAddress(Configuration.port));
		
		serverSocketChannel.configureBlocking(false);
		
		Selector selector = Selector.open();
		
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		
		logger.info("Server started");
		
		while(true) {
			logger.debug("before select");
			selector.select();
			logger.debug("after select");
			Set<SelectionKey> selectionKeys = selector.selectedKeys();
			Iterator<SelectionKey> it = selectionKeys.iterator();
			while(it.hasNext()) {
				SelectionKey sk = it.next();
				it.remove();
				if(sk.isValid()) {
					if(sk.isAcceptable()) {
						logger.debug("acceptable");
						ServerSocketChannel ssc = (ServerSocketChannel)sk.channel();
						logger.debug("SelectionKey:" + sk);
						logger.debug("ServerSocketChannel:" + ssc.hashCode());
						logger.debug("accepting connection");
						SocketChannel sc = ssc.accept();
						logger.debug("SocketChannel:" + sc);
						sc.configureBlocking(false);
						sc.register(sk.selector(), SelectionKey.OP_READ);
						sockets.put(sc, ByteBuffer.allocate(BUFFER_SIZE));
					}
					if(sk.isReadable()) {
						logger.debug("readable");
						SocketChannel sc = (SocketChannel)sk.channel();
						logger.debug("SelectionKey:" + sk);
						logger.debug("SocketChannel:" + sc);
						ByteBuffer bb = sockets.get(sc);
						System.out.println(new String(bb.array(), "UTF-8"));
						logger.debug("reading");
						int read = sc.read(bb);
						if(read == -1) {
							System.out.println("end");
							sc.close();
							sockets.remove(sc);
							continue;
						}
						else if(read == 0) {
							System.out.println("end on zero");
							bb.clear();
							continue;
						}
						bb.flip();
						if(sk.isValid()) {
							sk.interestOps(SelectionKey.OP_WRITE);
						}
						else {
							sc.close();
							sockets.remove(sc);
							logger.debug("Connection closed by client");
							continue;
						}
					}
					if(sk.isWritable()) {
						logger.debug("writable");
						SocketChannel sc = (SocketChannel)sk.channel();
						logger.debug("SelectionKey:" + sk);
						logger.debug("SocketChannel:" + sc);
						ByteBuffer bb = sockets.get(sc);
						ByteBuffer response = nonBlockingRequestHandler.generateResponse((ByteBuffer)bb.flip());
						logger.debug("writing");
						/*nonBlockingRequestHandler.writeLine(sc, "HTTP/1.1 200 OK");
						nonBlockingRequestHandler.writeLine(sc, "Date: " + new Date().toString());
						nonBlockingRequestHandler.writeLine(sc, "Server: Java HTTP Server");
						nonBlockingRequestHandler.writeLine(sc, "Content-Length: 13");
						nonBlockingRequestHandler.writeLine(sc, "Content-Type: text/html");
						nonBlockingRequestHandler.writeLine(sc, "Connection: Closed");
						nonBlockingRequestHandler.writeLine(sc, "");*/
						System.out.println(new String(response.array(), "UTF-8"));
						sc.write(response);
						if(!response.hasRemaining()) {
							sk.interestOps(SelectionKey.OP_READ);
							bb.compact();
						}
						//sk.interestOps(SelectionKey.OP_READ);
					}
				}
			}

			sockets.keySet().removeIf(sch -> !sch.isOpen());
			
		}
		
	
	}
}
