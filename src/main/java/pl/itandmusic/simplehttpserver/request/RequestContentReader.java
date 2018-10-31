package pl.itandmusic.simplehttpserver.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import pl.itandmusic.simplehttpserver.logger.LogLevel;
import pl.itandmusic.simplehttpserver.logger.Logger;
import pl.itandmusic.simplehttpserver.model.RequestContent;

public class RequestContentReader {

	private static RequestContentReader requestContentReader = new RequestContentReader();
	private static final Logger logger = Logger.getLogger(RequestContentReader.class);
	private static boolean throwExceptionOnConstructorCall = false;
	
	private RequestContentReader() {
		if(throwExceptionOnConstructorCall) {
			throw new RuntimeException("Singleton constructor call");
		}
		throwExceptionOnConstructorCall = true;
	}
	
	public static RequestContentReader getRequestContentReader() {
		return requestContentReader;
	}

	public RequestContent read(Socket socket) {
		logger.debug("reading from socket: " + socket);
		RequestContent content = new RequestContent();
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			List<String> plainContent = readPlainContent(bufferedReader);
			String postData = readPostData(plainContent, bufferedReader);

			content.setPlainContent(plainContent);
			content.setPOSTData(postData);

		} catch (IOException ioException) {
			logger.logException("RequestContentReader - read(Socket socket)", ioException, LogLevel.ERROR);
		}

		return content;
	}

	private List<String> readPlainContent(BufferedReader bufferedReader) {
		
		List<String> plainContent = new ArrayList<>();
		String line;
		
		try {
			while ((line = bufferedReader.readLine()) != null && (line.length() != 0)) {
				plainContent.add(line);
			}
		} catch (IOException ioException) {
			logger.logException("RequestContentReader - readPlainContent()", ioException, LogLevel.ERROR);
		}
		
		return plainContent;

	}

	private String readPostData(List<String> plainContent, BufferedReader bufferedReader) {
		try {
			int contentLengthHeaderValue = -1;

			for (String line : plainContent) {
				if (line != null && hasContentLengthHeader(line)) {
					contentLengthHeaderValue = getContentLengthValue(line);
					break;
				}
			}
			return readPostData(bufferedReader, contentLengthHeaderValue);
		} catch (IOException ioException) {
			logger.logException("RequestContentReader - readPostData()", ioException, LogLevel.ERROR);
			return null;
		}
	}

	private boolean hasContentLengthHeader(String line) {
		return line.contains("Content-Length:");
	}

	private int getContentLengthValue(String line) {
		int result = -1;
		try {
			String value = line.replace("Content-Length:", "").trim();
			result = Integer.valueOf(value);
		} catch (Exception exception) {
			logger.warn(exception.getMessage());
		}
		return result;
	}

	private String readPostData(BufferedReader bufferedReader, int contentLengthHeaderValue) throws IOException {
		String postData = "";
		if (contentLengthHeaderValue > 0) {
			char[] charArray = new char[contentLengthHeaderValue];
			bufferedReader.read(charArray, 0, contentLengthHeaderValue);
			postData = new String(charArray);
		}
		return postData;
	}
}
