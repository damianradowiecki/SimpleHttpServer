package pl.itandmusic.simplehttpserver.model;

import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EnumerationImpl<T> implements Enumeration<T> {

	private List<T> strings;
	private int currentPosition = 0;
	
	public EnumerationImpl(List<T> strings) {
		this.strings = strings;
	}
	
	public EnumerationImpl(Set<T> strings) {
		this.strings = strings
				.stream()
				.collect(Collectors.toList());
	}

	@Override
	public boolean hasMoreElements() {
		return currentPosition + 1 <= strings.size();
	}

	@Override
	public T nextElement() {
		return strings.get(currentPosition++);
	}

}
