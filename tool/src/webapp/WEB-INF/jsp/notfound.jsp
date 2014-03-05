<%@ page info="404==not found" %>
<%
  response.setStatus(HttpServletResponse.SC_NOT_FOUND);
  response.addHeader("Content-Type", "text/html; charset=UTF8");
  %>
<html>
<head>
  <title>404 not found</title>
</head>
<body>
<p>Not found</p>
</body>
</html>