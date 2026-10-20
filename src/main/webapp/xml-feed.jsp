<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="x" uri="jakarta.tags.xml" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>ShopSphere - XML Feed</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="/fragments/navbar.jsp" />

<div class="container">
  <h2 class="mb-3">JSTL XML: product feed</h2>

  <c:set var="feedXml">
<?xml version="1.0" encoding="UTF-8"?>
<products>
  <product id="4"><name>Wireless Earbuds</name><price>1799.10</price><stock>23</stock></product>
  <product id="5"><name>Smart Watch</name><price>2974.15</price><stock>10</stock></product>
  <product id="6"><name>Cotton T-Shirt</name><price>474.05</price><stock>50</stock></product>
</products>
  </c:set>

  <x:parse xml="${feedXml}" var="doc" />

  <p class="text-muted">Products in feed: <x:out select="count($doc/products/product)" /></p>

  <table class="table">
    <thead><tr><th>ID</th><th>Name</th><th>Price</th><th>Stock</th></tr></thead>
    <tbody>
      <x:forEach select="$doc/products/product" var="p">
        <tr>
          <td><x:out select="$p/@id" /></td>
          <td><x:out select="$p/name" /></td>
          <td>Rs. <x:out select="$p/price" /></td>
          <td><x:out select="$p/stock" /></td>
        </tr>
      </x:forEach>
    </tbody>
  </table>
</div>

<jsp:include page="/fragments/footer.jsp" />
</body>
</html>