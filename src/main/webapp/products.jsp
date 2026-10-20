<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>ShopSphere - Products</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="/fragments/navbar.jsp" />

<div class="container">
  <h2 class="mb-3">Products</h2>

  <c:if test="${empty products}">
    <div class="alert alert-info">No products found.</div>
  </c:if>

  <div class="row row-cols-1 row-cols-md-3 g-4">
    <c:forEach var="product" items="${products}">
      <div class="col">
        <t:productCard product="${product}" />
      </div>
    </c:forEach>
  </div>
</div>

<c:if test="${not empty recentProducts}">
<div class="container">
  <h4 class="mt-5">Recently viewed</h4>
  <div class="d-flex flex-wrap gap-2">
    <c:forEach var="rp" items="${recentProducts}">
      <a class="btn btn-outline-secondary btn-sm"
         href="${pageContext.request.contextPath}/products?id=${rp.productId}">${rp.name}</a>
    </c:forEach>
  </div>
</div>
</c:if>
<jsp:include page="/fragments/footer.jsp" />
</body>
</html>