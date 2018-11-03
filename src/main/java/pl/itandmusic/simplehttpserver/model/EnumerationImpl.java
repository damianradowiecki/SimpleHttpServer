package pl.itandmusic.simplehttpserver.model;

import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class EnumerationImpl<T> implements Enumeration<T> {

	private List<T> strings;
	private int currentPosition = 0;
	
	public EnumerationImpl(List<T> strings) {
		this.strings = Objects.requireNonNull(strings);
	}
	
	public EnumerationImpl(Set<T> strings) {
		this.strings = Objects
				.requireNonNull(strings)
				.stream()
				.collect(Collectors.toList());
	}

	@Override
	public boolean hasMoreElements() {
		return currentPosition + 1 <= strings.size();
	}

	@Override
	public T nextElement() {
		if(currentPosition > ( strings.size() -1)) throw new NoSuchElementException();
		return strings.get(currentPosition++);
	}

}
