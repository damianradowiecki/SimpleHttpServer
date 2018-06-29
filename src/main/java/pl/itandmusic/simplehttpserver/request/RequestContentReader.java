package pl.itandmusic.simplehttpserver.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import pl.itandmusic.simplehttpserver.configuration.web.WebConfigurationLoader;
import pl.itandmusic.simplehttpserver.logger.Logger;
import pl.itandmusic.simplehttpserver.model.RequestContent;

public class RequestContentReader {

	private static final Logger logger = Logger.getLogger(WebConfigurationLoader.class);
	private Socket socket;
	private InputStreamReader inputStreamReader;
	private BufferedReader bufferedReader;
	private RequestContent content = new RequestContent();
	private List<String> plainContent = new ArrayList<>();
	private String postData = "";

	public RequestContent read(Socket socket) {
		this.socket = socket;
		try {
			inputStreamReader = new InputStreamReader(this.socket.getInputStream());
			bufferedReader = new BufferedReader(inputStreamReader);

			readPlainContent();
			readPostData();

			content.setPlainContent(plainContent);
			content.setPOSTData(postData);

		} catch (IOException ioException) {
			ioException.printStackTrace();
		}

		return content;
	}

	private void readPlainContent() {
		try {

			String line;

			while ((line = bufferedReader.readLine()) != null && (line.length() != 0)) {
				plainContent.add(line);
			}

		} catch (IOException ioException) {
			ioException.printStackTrace();
		}

	}

	private void readPostData() {
		try {
			int contentLengthHeaderValue = -1;

			for (String line : plainContent) {
				if (hasContentLengthHeader(line)) {
					contentLengthHeaderValue = getContentLengthValue(line);
					break;
				}
			}
			postData = readPostData(bufferedReader, contentLengthHeaderValue);
		} catch (IOException ioException) {
			ioException.printStackTrace();
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
