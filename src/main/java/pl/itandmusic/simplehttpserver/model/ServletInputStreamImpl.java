package pl.itandmusic.simplehttpserver.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import javax.servlet.ServletInputStream;

public class ServletInputStreamImpl extends ServletInputStream {

	private InputStream inputStream;
	
	
	public ServletInputStreamImpl(Socket clietnSocket) {
		try {
			inputStream = clietnSocket.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public int read() throws IOException {
		return inputStream.read();
	}

}
