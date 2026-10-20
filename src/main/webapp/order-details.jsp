<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>ShopSphere - Order #${order.orderId}</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="/fragments/navbar.jsp" />

<div class="container">
  <a href="${pageContext.request.contextPath}/orders" class="btn btn-link px-0 mb-3">&larr; Back to my orders</a>
  <h2 class="mb-1">Order #${order.orderId}</h2>
  <p class="text-muted">Placed on ${order.createdAt}</p>

  <c:set var="status" value="${order.orderStatus}" />

  <c:choose>
    <c:when test="${status == 'CANCELLED'}">
      <div class="alert alert-danger">This order was cancelled.</div>
    </c:when>
    <c:otherwise>
      <div class="card mb-4">
        <div class="card-body">
          <div class="row text-center">
            <div class="col ${status == 'PLACED' || status == 'SHIPPED' || status == 'DELIVERED' ? 'text-success fw-bold' : 'text-muted'}">
              1. Placed
            </div>
            <div class="col ${status == 'SHIPPED' || status == 'DELIVERED' ? 'text-success fw-bold' : 'text-muted'}">
              2. Shipped
            </div>
            <div class="col ${status == 'DELIVERED' ? 'text-success fw-bold' : 'text-muted'}">
              3. Delivered
            </div>
          </div>
        </div>
      </div>
    </c:otherwise>
  </c:choose>

  <div class="row g-4">
    <div class="col-md-7">
      <div class="card">
        <div class="card-body">
          <h5 class="card-title">Items</h5>
          <table class="table align-middle mb-0">
            <thead>
              <tr>
                <th>Product</th>
                <th>Price</th>
                <th>Qty</th>
                <th>Total</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="item" items="${items}">
                <tr>
                  <td>${item.name}</td>
                  <td>Rs. <fmt:formatNumber value="${item.price}" minFractionDigits="2" maxFractionDigits="2" /></td>
                  <td>${item.quantity}</td>
                  <td>Rs. <fmt:formatNumber value="${item.lineTotal}" minFractionDigits="2" maxFractionDigits="2" /></td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
          <div class="d-flex justify-content-end mt-3 fw-bold">
            Total: Rs. <fmt:formatNumber value="${order.totalAmount}" minFractionDigits="2" maxFractionDigits="2" />
          </div>
        </div>
      </div>
    </div>

    <div class="col-md-5">
      <div class="card mb-4">
        <div class="card-body">
          <h5 class="card-title">Delivery address</h5>
          <p class="mb-0">${order.addressLine}<br>${order.city}, ${order.state} - ${order.pincode}</p>
        </div>
      </div>
      <div class="card">
        <div class="card-body">
          <h5 class="card-title">Payment</h5>
          <p class="mb-0">${order.paymentMethod} (${order.paymentStatus})</p>
        </div>
      </div>
    </div>
  </div>
</div>
<jsp:include page="/fragments/footer.jsp" />
</body>
</html>