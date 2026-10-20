<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>${product.name} - ShopSphere</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="/fragments/navbar.jsp" />

<div class="container">
  <a href="${pageContext.request.contextPath}/products" class="btn btn-link px-0 mb-3">&larr; Back to products</a>

  <div class="row">
    <div class="col-md-6">
      <div class="card">
        <div class="card-body">
          <h2 class="card-title">${product.name}</h2>
          <p class="text-muted mb-1">${product.brand}</p>
          <p>${product.description}</p>

          <p class="mb-2">
            <span class="fs-4 fw-bold">Rs. <fmt:formatNumber value="${product.finalPrice}" minFractionDigits="2" maxFractionDigits="2" /></span>
            <c:if test="${product.discount > 0}">
              <span class="text-decoration-line-through text-muted ms-2">Rs. <fmt:formatNumber value="${product.price}" minFractionDigits="2" maxFractionDigits="2" /></span>
              <span class="badge bg-success ms-2">${product.discount}% off</span>
            </c:if>
          </p>

          <p>
            <c:choose>
              <c:when test="${product.stock > 0}">
                <span class="text-success">In stock (${product.stock} left)</span>
              </c:when>
              <c:otherwise>
                <span class="text-danger">Out of stock</span>
              </c:otherwise>
            </c:choose>
          </p>

          <c:choose>
            <c:when test="${product.stock > 0}">
              <form class="d-flex gap-2 align-items-center" action="${pageContext.request.contextPath}/cart" method="post">
                <input type="hidden" name="action" value="add">
                <input type="hidden" name="productId" value="${product.productId}">
                <input class="form-control" style="width: 90px;" type="number" name="quantity"
                       value="1" min="1" max="${product.stock}">
                <button class="btn btn-primary" type="submit">Add to Cart</button>
              </form>
            </c:when>
            <c:otherwise>
              <button class="btn btn-secondary" type="button" disabled>Out of stock</button>
            </c:otherwise>
          </c:choose>
        </div>
      </div>
    </div>
  </div>
</div>
<jsp:include page="/fragments/footer.jsp" />
</body>
</html>