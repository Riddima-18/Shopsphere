<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>ShopSphere - Cart</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="/fragments/navbar.jsp" />

<div class="container">
  <h2 class="mb-3">Your cart</h2>

  <c:if test="${not empty param.message}">
    <div class="alert alert-success"><c:out value="${param.message}" /></div>
  </c:if>
  <c:if test="${not empty param.error}">
    <div class="alert alert-danger"><c:out value="${param.error}" /></div>
  </c:if>

  <c:choose>
    <c:when test="${empty cartItems}">
      <div class="alert alert-info">
        Your cart is empty.
        <a href="${pageContext.request.contextPath}/products">Continue shopping</a>
      </div>
    </c:when>

    <c:otherwise>
      <div class="table-responsive">
        <table class="table align-middle">
          <thead>
            <tr>
              <th>Product</th>
              <th>Price</th>
              <th>Quantity</th>
              <th>Total</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="item" items="${cartItems}">
              <tr>
                <td>${item.name}</td>
                <td>Rs. <fmt:formatNumber value="${item.finalPrice}" minFractionDigits="2" maxFractionDigits="2" /></td>
                <td>
                  <form class="d-flex gap-2" action="${pageContext.request.contextPath}/cart" method="post">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="productId" value="${item.productId}">
                    <input class="form-control form-control-sm" style="width: 80px;" type="number"
                           name="quantity" min="0" max="${item.stock}" value="${item.quantity}">
                    <button class="btn btn-sm btn-outline-secondary" type="submit">Update</button>
                  </form>
                </td>
                <td>Rs. <fmt:formatNumber value="${item.lineTotal}" minFractionDigits="2" maxFractionDigits="2" /></td>
                <td>
                  <form action="${pageContext.request.contextPath}/cart" method="post">
                    <input type="hidden" name="action" value="remove">
                    <input type="hidden" name="productId" value="${item.productId}">
                    <button class="btn btn-sm btn-outline-danger" type="submit">Remove</button>
                  </form>
                </td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </div>

      <div class="d-flex justify-content-end align-items-center gap-3">
        <h5 class="mb-0">Total: Rs. <fmt:formatNumber value="${cartTotal}" minFractionDigits="2" maxFractionDigits="2" /></h5>
       <a class="btn btn-primary" href="${pageContext.request.contextPath}/checkout">Proceed to checkout</a> 
      </div>
      <p class="text-muted small mt-2">Set quantity to 0 to remove an item.</p>
    </c:otherwise>
  </c:choose>
</div>
<jsp:include page="/fragments/footer.jsp" />
</body>
</html>