package pl.itandmusic.simplehttpserver.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Objects;

import javax.servlet.ServletInputStream;

import pl.itandmusic.simplehttpserver.logger.LogLevel;
import pl.itandmusic.simplehttpserver.logger.Logger;

public class ServletInputStreamImpl extends ServletInputStream {

	private static final Logger logger = Logger.getLogger(ServletInputStreamImpl.class);
	private InputStream inputStream;
	
	
	public ServletInputStreamImpl(Socket clientSocket) {
		try {
			inputStream = Objects
					.requireNonNull(clientSocket)
					.getInputStream();
		} catch (IOException e) {
			logger.logException("ServletInputStreamImpl - constructor", e, LogLevel.ERROR);
		}
	}
	
	@Override
	public int read() throws IOException {
		return inputStream.read();
	}

}
