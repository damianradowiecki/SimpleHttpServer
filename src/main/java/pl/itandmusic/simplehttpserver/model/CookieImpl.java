package pl.itandmusic.simplehttpserver.model;

import java.util.Objects;

import javax.servlet.http.Cookie;

public class CookieImpl extends Cookie {

	private String name;
	private String value;
	
	public CookieImpl(String name, String value) {
		super(Objects.requireNonNull(name), 
				Objects.requireNonNull(value));
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getValue() {
		return value;
	}
	@Override
	public void setValue(String value) {
		this.value = value;
	}
	
}
