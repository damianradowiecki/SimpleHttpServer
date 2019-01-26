package pl.itandmusic.simplehttpserver.jsp;

import java.util.List;

public class SimpleJSPDocumentConverter implements JSPDocumentConverter {

	@Override
	public List<String> convert(JSPDocument document){
		//sax parser builds an HttpServletClass using HttpSErvletClassBuilder
		return null;
	}

}
