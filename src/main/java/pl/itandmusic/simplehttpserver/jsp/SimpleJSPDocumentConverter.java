package pl.itandmusic.simplehttpserver.jsp;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

public class SimpleJSPDocumentConverter implements JSPDocumentConverter {

	private ScripletSearcher searcher;
	private ScripletCompiler compiler;
	private ScripletReplacer replacer;

	@Override
	public String convert(File file) throws IOException {
		byte[] encoded = Files.readAllBytes(file.toPath());
		String document = new String(encoded, Charset.defaultCharset());

		List<Scriplet> scriplets = searcher.search(file);
		for (Scriplet s : scriplets) {
			String compiledCodeResult = compiler.compile(s);
			replacer.replace(document, s, compiledCodeResult);
		}
		return document;
	}

}
