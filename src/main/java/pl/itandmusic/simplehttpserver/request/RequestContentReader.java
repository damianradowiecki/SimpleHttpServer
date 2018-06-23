package pl.itandmusic.simplehttpserver.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class RequestContentReader {

	private Socket socket;
	private List<String> content = new ArrayList<>();

	public List<String> read(Socket socket) {
		this.socket = socket;

		try {
			InputStreamReader inputStreamReader = new InputStreamReader(this.socket.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String line;

			while (!(line = bufferedReader.readLine()).equals("")) {
				content.add(line);
			}

		} catch (IOException ioException) {
			ioException.printStackTrace();
		}

		return content;
	}
}
