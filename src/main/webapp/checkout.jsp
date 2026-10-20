<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>ShopSphere - Checkout</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="/fragments/navbar.jsp" />

<div class="container">
  <h2 class="mb-3">Checkout</h2>

  <c:if test="${not empty error}">
    <div class="alert alert-danger"><c:out value="${error}" /></div>
  </c:if>
  <c:if test="${not empty appliedCode}">
    <div class="alert alert-success">Coupon <c:out value="${appliedCode}" /> applied.</div>
  </c:if>

  <div class="row g-4">
    <div class="col-md-7">
      <div class="card">
        <div class="card-body">
          <form action="${pageContext.request.contextPath}/checkout" method="post">
            <h5 class="card-title">Delivery address</h5>
            <div class="mb-3">
              <label class="form-label">Address</label>
              <input class="form-control" type="text" name="line" value="${param.line}" required>
            </div>
            <div class="row">
              <div class="col mb-3">
                <label class="form-label">City</label>
                <input class="form-control" type="text" name="city" value="${param.city}" required>
              </div>
              <div class="col mb-3">
                <label class="form-label">State</label>
                <input class="form-control" type="text" name="state" value="${param.state}" required>
              </div>
            </div>
            <div class="mb-3">
              <label class="form-label">Pincode (6 digits)</label>
              <input class="form-control" type="text" name="pincode" value="${param.pincode}" maxlength="6" required>
            </div>

            <h5 class="mt-4">Coupon</h5>
            <div class="input-group mb-3">
              <input class="form-control" type="text" name="coupon" value="${param.coupon}" placeholder="Enter coupon code">
              <button class="btn btn-outline-secondary" type="submit" name="action" value="apply">Apply</button>
            </div>

            <h5 class="mt-4">Payment</h5>
            <div class="form-check">
              <input class="form-check-input" type="radio" name="paymentMethod" id="cod" value="COD"
                     ${param.paymentMethod != 'CARD' ? 'checked' : ''}>
              <label class="form-check-label" for="cod">Cash on delivery</label>
            </div>
            <div class="form-check mb-3">
              <input class="form-check-input" type="radio" name="paymentMethod" id="card" value="CARD"
                     ${param.paymentMethod == 'CARD' ? 'checked' : ''}>
              <label class="form-check-label" for="card">Card (mock payment, always succeeds)</label>
            </div>

            <button class="btn btn-primary w-100" type="submit" name="action" value="place">Place order</button>
          </form>
        </div>
      </div>
    </div>

    <div class="col-md-5">
      <div class="card">
        <div class="card-body">
          <h5 class="card-title">Order summary</h5>
          <c:forEach var="item" items="${cartItems}">
            <div class="d-flex justify-content-between small mb-2">
              <span>${item.name} x ${item.quantity}</span>
              <span>Rs. <fmt:formatNumber value="${item.lineTotal}" minFractionDigits="2" maxFractionDigits="2" /></span>
            </div>
          </c:forEach>
          <hr>
          <div class="d-flex justify-content-between">
            <span>Subtotal</span>
            <span>Rs. <fmt:formatNumber value="${subtotal}" minFractionDigits="2" maxFractionDigits="2" /></span>
          </div>
          <c:if test="${discount > 0}">
            <div class="d-flex justify-content-between text-success">
              <span>Discount</span>
              <span>- Rs. <fmt:formatNumber value="${discount}" minFractionDigits="2" maxFractionDigits="2" /></span>
            </div>
          </c:if>
          <div class="d-flex justify-content-between fw-bold mt-2">
            <span>Total</span>
            <span>Rs. <fmt:formatNumber value="${finalTotal}" minFractionDigits="2" maxFractionDigits="2" /></span>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<jsp:include page="/fragments/footer.jsp" />
</body>
</html>