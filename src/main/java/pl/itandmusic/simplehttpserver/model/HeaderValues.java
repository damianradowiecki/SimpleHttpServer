package pl.itandmusic.simplehttpserver.model;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

public class HeaderValues implements Enumeration<String> {

	private List<String> headers = new ArrayList<>();
	private int currentPosition = 0;

	public HeaderValues(String notSplitedHeaders) {
		Objects.requireNonNull(notSplitedHeaders);
		for (String s : notSplitedHeaders.split(",")) {
			s = s.trim();
			headers.add(s);
		}
	}

	@Override
	public boolean hasMoreElements() {
		return currentPosition + 1 <= headers.size();
	}

	@Override
	public String nextElement() {
		return headers.get(currentPosition++);
	}

}
