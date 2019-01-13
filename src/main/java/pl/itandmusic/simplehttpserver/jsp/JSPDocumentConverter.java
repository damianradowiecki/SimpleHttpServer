package pl.itandmusic.simplehttpserver.jsp;

import java.io.File;
import java.io.IOException;

public interface JSPDocumentConverter {

	String convert(File file) throws IOException;
	
}
