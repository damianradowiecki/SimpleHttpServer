package pl.itandmusic.simplehttpserver.http;

public enum ResponseStatus {
	OK("HTTP/1.1 200 OK"), 
	NOT_FOUND("HTTP/1.1 404 Not Found"), 
	FOUND("HTTP/1.1 302 Found"), 
	INTERNAL_SERVER_ERROR("HTTP/1.1 500 Internal Server Error");
	
	private String header;
	
	private ResponseStatus(String header) {
		this.header = header;
	}
	
	@Override
	public String toString() {
		return this.header;
	}
}
