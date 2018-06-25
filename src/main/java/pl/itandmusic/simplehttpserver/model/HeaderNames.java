package pl.itandmusic.simplehttpserver.model;

import java.util.Enumeration;
import java.util.List;

public class HeaderNames implements Enumeration<String> {

	private List<String> names;
	private int currentPosition = 0;
	
	public HeaderNames(List<String> names) {
		this.names = names;
	}
	
	@Override
	public boolean hasMoreElements() {
		return currentPosition + 1 <= names.size();
	}

	@Override
	public String nextElement() {
		return names.get(currentPosition++);
	}

}
