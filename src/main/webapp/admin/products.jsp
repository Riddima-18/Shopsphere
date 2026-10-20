<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>ShopSphere - Manage Products</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="/fragments/navbar.jsp" />

<div class="container">
  <div class="d-flex justify-content-between align-items-center mb-3">
    <h2 class="mb-0">Products</h2>
    <div class="d-flex gap-2">
      <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
      <a class="btn btn-primary" href="${pageContext.request.contextPath}/admin/products?action=new">Add product</a>
    </div>
  </div>

  <c:if test="${not empty param.message}">
    <div class="alert alert-success"><c:out value="${param.message}" /></div>
  </c:if>
  <c:if test="${not empty param.error}">
    <div class="alert alert-danger"><c:out value="${param.error}" /></div>
  </c:if>

  <div class="table-responsive">
    <table class="table align-middle">
      <thead>
        <tr>
          <th>ID</th><th>Name</th><th>Price</th><th>Discount</th><th>Stock</th><th>Status</th><th></th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="p" items="${products}">
          <tr>
            <td>${p.productId}</td>
            <td>${p.name}</td>
            <td>Rs. <fmt:formatNumber value="${p.price}" minFractionDigits="2" maxFractionDigits="2" /></td>
            <td>${p.discount}%</td>
            <td>${p.stock}</td>
            <td>
              <c:choose>
                <c:when test="${p.status}"><span class="badge bg-success">Active</span></c:when>
                <c:otherwise><span class="badge bg-secondary">Disabled</span></c:otherwise>
              </c:choose>
            </td>
            <td class="d-flex gap-2">
              <a class="btn btn-sm btn-outline-primary"
                 href="${pageContext.request.contextPath}/admin/products?action=edit&id=${p.productId}">Edit</a>
              <form action="${pageContext.request.contextPath}/admin/products" method="post">
                <input type="hidden" name="action" value="toggle">
                <input type="hidden" name="id" value="${p.productId}">
                <button class="btn btn-sm ${p.status ? 'btn-outline-danger' : 'btn-outline-success'}" type="submit">
                  ${p.status ? 'Disable' : 'Enable'}
                </button>
              </form>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </div>
</div>
<jsp:include page="/fragments/footer.jsp" />
</body>
</html>