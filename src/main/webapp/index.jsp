<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<body>
  <h2>ShopSphere home page</h2>
  <p>Server time: <%= new java.util.Date() %></p>
  <p><a href="${pageContext.request.contextPath}/products">Browse products</a></p>
</body>
</html>