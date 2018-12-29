package pl.itandmusic.simplehttpserver.request;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Date;

import pl.itandmusic.simplehttpserver.logger.Logger;

public class NonBlockingRequestHandler {

	private static final Logger logger = Logger.getLogger(NonBlockingRequestHandler.class);
	private static final NonBlockingRequestHandler nonBlockingRequestHandler = new NonBlockingRequestHandler();
	private static boolean throwExceptionOnConstructorCall = false;
	private static final int initialResponseBufferSize = 10000;
	private Charset charset = Charset.forName("UTF-8");
	private CharsetEncoder encoder = charset.newEncoder();
	
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
		
		String headers ="HTTP/1.1 200 OK\r\n" + 
				"Date: " + new Date().toString() + "\r\n" + 
				"Server: Java HTTP Server\r\n" + 
				"Content-Length: 13\r\n" + 
				"Content-Type: text/html\r\n" + 
				"Connection: Closed\r\n\r\n";
		
		String content = "Hello, World!";
		
		CharBuffer buffer = CharBuffer.wrap(headers + content);

		return encoder.encode(buffer);
	}
	
	public void writeLine(WritableByteChannel writableByteChannel, String line) throws IOException {
		CharBuffer charBuffer = CharBuffer.wrap(line + "\r\n");
		ByteBuffer byteBuffer = encoder.encode(charBuffer);

		writableByteChannel.write(byteBuffer);
	}
}
