package pl.itandmusic.simplehttpserver.jsp;

import java.util.ArrayList;
import java.util.List;

public class HttpServletClassBuilder {
	
	private List<String> lines = new ArrayList<>();
	
	public HttpServletClassBuilder() {
		this.lines = new ArrayList<>();
	}
	
	public HttpServletClassBuilder addImports() {
		lines.add("import java.io.IOException;");
		lines.add("import javax.servlet.ServletException;");
		lines.add("import javax.servlet.http.HttpServlet;");
		lines.add("import javax.servlet.http.HttpServletRequest;");
		lines.add("import javax.servlet.http.HttpServletResponse;");
		lines.add("import javax.servlet.http.HttpSession;");
		return this;
	}
	
	public HttpServletClassBuilder addEmptyLine() {
		lines.add("");
		return this;
	}
	
	public HttpServletClassBuilder addClassDefinition(String classname) {
		lines.add("public class " + classname + " extends extends HttpServlet {");
		return this;
	}
	
	public HttpServletClassBuilder addClosingBrace() {
		lines.add("}");
		return this;
	}
	
	public HttpServletClassBuilder addDoGetMethod(List<String> code) {
		lines.add("protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {");
		lines.addAll(code);
		lines.add("}");
		return this;
	}
	
	public HttpServletClassBuilder addDoPostMethod(List<String> code) {
		lines.add("protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {");
		lines.addAll(code);
		lines.add("}");
		return this;
	}
	
	public List<String> build() {
		return lines;
	}
	
}
