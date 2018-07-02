package pl.itandmusic.simplehttpserver.utils;

public class URIUtils {

	public static String correctUnproperDefaultPageURI(String uri) {
		return uri += "/";
	}
}
