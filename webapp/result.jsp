<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Wynik</title>
</head>
<body>
	<h1 align="center">JSP z rekomendacjami dla piwa</h1>
	<p>
		<%
			List<String> brands = (List<String>)request.getAttribute("brands");
			for(String s : brands){
				out.print("<br />Spróbuj: " + s);
			}
		%>
	</p>
</body>
</html>