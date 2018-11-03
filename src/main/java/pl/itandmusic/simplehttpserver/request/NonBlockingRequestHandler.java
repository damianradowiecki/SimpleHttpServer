package pl.itandmusic.simplehttpserver.request;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import pl.itandmusic.simplehttpserver.logger.Logger;

public class NonBlockingRequestHandler {

	private static final Logger logger = Logger.getLogger(NonBlockingRequestHandler.class);
	private static final NonBlockingRequestHandler nonBlockingRequestHandler = new NonBlockingRequestHandler();
	private static boolean throwExceptionOnConstructorCall = false;
	private static final int initialResponseBufferSize = 10000;
	
	private NonBlockingRequestHandler() {
		if(throwExceptionOnConstructorCall) {
			throw new RuntimeException("Singleton constructor call");
		}
		throwExceptionOnConstructorCall = true;
	}
	
	public static NonBlockingRequestHandler getInstance() {
		return nonBlockingRequestHandler;
	}
	
	public ByteBuffer generateResponse(ByteBuffer requestByteBuffer) throws CharacterCodingException{
		
		logger.debug("generating response");
		
		
		

		
		String headers ="HTTP/1.1 200 OK\n" + 
				"Date: Mon, 27 Jul 2009 12:28:53 GMT\n" + 
				"Server: Apache/2.2.14 (Win32)\n" + 
				"Last-Modified: Wed, 22 Jul 2009 19:15:56 GMT\n" + 
				"Content-Length: 88\n" + 
				"Content-Type: text/html\n" + 
				"Connection: Closed";
		
		String content = "<html>\n" + 
				"<body>\n" + 
				"<h1>Hello, World!</h1>\n" + 
				"</body>\n" + 
				"</html>";
		
		CharBuffer buffer = CharBuffer.wrap(headers + content);
		
		Charset charset = Charset.forName("UTF-8");
		CharsetEncoder encoder = charset.newEncoder();

		
		return encoder.encode(buffer);
	}
}
