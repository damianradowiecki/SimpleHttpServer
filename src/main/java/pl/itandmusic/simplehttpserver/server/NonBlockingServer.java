package pl.itandmusic.simplehttpserver.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import pl.itandmusic.simplehttpserver.logger.Logger;

public class NonBlockingServer {

	private static final Logger logger = Logger.getLogger(NonBlockingServer.class);
	private static Map<SocketChannel, ByteBuffer> sockets = new ConcurrentHashMap<>();
	private static final int BUFFER_SIZE = 100000;
	
	public static void start() throws IOException {
		

		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

		serverSocketChannel.bind(new InetSocketAddress(4444));
		
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
						logger.debug("accepting connection");
						ServerSocketChannel ssc = (ServerSocketChannel)sk.channel();
						logger.debug("SelectionKey:" + sk);
						logger.debug("ServerSocketChannel:" + ssc.hashCode());
						SocketChannel sc = ssc.accept();
						logger.debug("SocketChannel:" + sc);
						sc.configureBlocking(false);
						sc.register(sk.selector(), SelectionKey.OP_READ);
						sockets.put(sc, ByteBuffer.allocate(BUFFER_SIZE));
					}
					if(sk.isReadable()) {
						logger.debug("reading");
						SocketChannel sc = (SocketChannel)sk.channel();
						logger.debug("SelectionKey:" + sk);
						logger.debug("SocketChannel:" + sc);
						ByteBuffer bb = sockets.get(sc);
						int read = sc.read(bb);
						if(read == -1) {
							System.out.println("end");
							sc.close();
							sockets.remove(sc);
						}
						else if(read == 0) {
							System.out.println("end on zero");
						}
						bb.flip();
						if(sk.isValid()) {
							sk.interestOps(SelectionKey.OP_WRITE);
						}
						else {
							logger.debug("Connection closed by client");
							continue;
						}
					}
					if(sk.isWritable()) {
						logger.debug("writing");
						SocketChannel sc = (SocketChannel)sk.channel();
						logger.debug("SelectionKey:" + sk);
						logger.debug("SocketChannel:" + sc);
						ByteBuffer bb = sockets.get(sc);
						sc.write(bb);
						if(!bb.hasRemaining()) {
							bb.compact();
							sk.interestOps(SelectionKey.OP_READ);
						}
					}
				}
			}

			sockets.keySet().removeIf(sch -> !sch.isOpen());
			
		}
		
	
	}
}
