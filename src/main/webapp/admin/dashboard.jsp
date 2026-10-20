<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>ShopSphere - Admin Dashboard</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="/fragments/navbar.jsp" />

<div class="container">
  <div class="d-flex justify-content-between align-items-center mb-3">
    <h2 class="mb-0">Admin dashboard</h2>
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/admin/products">Manage products</a>
    <a class="btn btn-outline-primary ms-2" href="${pageContext.request.contextPath}/admin/orders">Manage orders</a>
    <a class="btn btn-outline-primary ms-2" href="${pageContext.request.contextPath}/admin/coupons">Manage coupons</a>
  </div>

  <div class="row row-cols-1 row-cols-md-3 g-3 mb-4">
    <div class="col">
      <div class="card text-center"><div class="card-body">
        <div class="text-muted small">Products</div>
        <div class="fs-3 fw-bold">${productCount}</div>
      </div></div>
    </div>
    <div class="col">
      <div class="card text-center"><div class="card-body">
        <div class="text-muted small">Orders</div>
        <div class="fs-3 fw-bold">${orderCount}</div>
      </div></div>
    </div>
    <div class="col">
      <div class="card text-center"><div class="card-body">
        <div class="text-muted small">Paid revenue (card)</div>
        <div class="fs-3 fw-bold">Rs. <fmt:formatNumber value="${revenue}" minFractionDigits="2" maxFractionDigits="2" /></div>
      </div></div>
    </div>
     <div class="col">
       <div class="card text-center"><div class="card-body">
        <div class="text-muted small">Active sessions</div>
        <div class="fs-3 fw-bold">${activeSessions}</div>
      </div></div>
    </div>
  </div>

  <div class="card">
    <div class="card-body">
      <h5 class="card-title">Low stock (5 or fewer)</h5>
      <c:choose>
        <c:when test="${empty lowStock}">
          <p class="text-muted mb-0">All products are well stocked.</p>
        </c:when>
        <c:otherwise>
          <table class="table mb-0">
            <thead><tr><th>Product</th><th>Stock</th><th></th></tr></thead>
            <tbody>
              <c:forEach var="p" items="${lowStock}">
                <tr>
                  <td>${p.name}</td>
                  <td><span class="badge bg-danger">${p.stock}</span></td>
                  <td><a href="${pageContext.request.contextPath}/admin/products?action=edit&id=${p.productId}">Restock</a></td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</div>
<jsp:include page="/fragments/footer.jsp" />
</body>
</html>