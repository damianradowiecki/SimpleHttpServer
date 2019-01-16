package pl.itandmusic.simplehttpserver.jsp;

import java.util.Objects;

public class Scriplet {
	
	private String scriplet;
	
	/*
	 * scriplet must not be null
	 * and must be of length greater or equals zero ("<%%>" minimum)
	 */
	public Scriplet(String scriplet) {
		Objects.requireNonNull(scriplet);
		if(scriplet.length() < 4) {
			throw new IllegalArgumentException();
		}
		this.scriplet = scriplet;
	}
	
	public String getScriplet() {
		return this.scriplet;
	}
	
	public String getCode() {
		int start = 2;
		int end = this.scriplet.length() - 2; 
		return this.scriplet.trim().substring(start, end);
	}
}
