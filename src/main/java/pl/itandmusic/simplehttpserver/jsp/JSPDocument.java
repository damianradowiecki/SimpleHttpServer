package pl.itandmusic.simplehttpserver.jsp;

import java.io.File;
import java.util.Objects;

public class JSPDocument {
	
	private File file;
	
	public JSPDocument(File file) {
		this.file = Objects.requireNonNull(file);
	}
}
