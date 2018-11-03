package pl.itandmusic.simplehttpserver.utils;

public class URIUtils {

	private URIUtils() {
		throw new RuntimeException("Consturctor call exception");
	}
	
	public static String correctUnproperDefaultPageURI(String uri) {
		return uri += "/";
	}
}
