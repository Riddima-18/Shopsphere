<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sql" uri="jakarta.tags.sql" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>ShopSphere - JSTL SQL</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="/fragments/navbar.jsp" />

<div class="container">
  <h2 class="mb-2">JSTL SQL demo</h2>
  <p class="text-muted">This is a separate demonstration. The main app uses DAO classes and PreparedStatement.</p>

  <sql:setDataSource var="db"
      driver="com.mysql.cj.jdbc.Driver"
      url="jdbc:mysql://localhost:3306/shopsphere?serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false"
      user="root"
      password="Nine11$" />

  <sql:query dataSource="${db}" var="all">
    SELECT product_id, name, price, stock FROM products WHERE status = TRUE ORDER BY product_id
  </sql:query>

  <h5 class="mt-4">All active products (rows: ${all.rowCount})</h5>
  <table class="table">
    <thead><tr><th>ID</th><th>Name</th><th>Price</th><th>Stock</th></tr></thead>
    <tbody>
      <c:forEach var="row" items="${all.rows}">
        <tr>
          <td>${row.product_id}</td>
          <td>${row.name}</td>
          <td>Rs. ${row.price}</td>
          <td>${row.stock}</td>
        </tr>
      </c:forEach>
    </tbody>
  </table>

  <sql:query dataSource="${db}" var="pricey">
    SELECT name, price FROM products WHERE status = TRUE AND price >= ?
    <sql:param value="1000" />
  </sql:query>

  <h5 class="mt-4">Products priced 1000 or more (parameterized query)</h5>
  <ul>
    <c:forEach var="row" items="${pricey.rows}">
      <li>${row.name}: Rs. ${row.price}</li>
    </c:forEach>
  </ul>
</div>

<jsp:include page="/fragments/footer.jsp" />
</body>
</html>