<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>ShopSphere - My Orders</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="/fragments/navbar.jsp" />

<div class="container">
  <h2 class="mb-3">My orders</h2>

  <c:if test="${not empty param.placed}">
    <div class="alert alert-success">Order #<c:out value="${param.placed}" /> placed successfully.</div>
  </c:if>

  <c:choose>
    <c:when test="${empty orders}">
      <div class="alert alert-info">
        You have no orders yet.
        <a href="${pageContext.request.contextPath}/products">Start shopping</a>
      </div>
    </c:when>
    <c:otherwise>
      <div class="table-responsive">
        <table class="table align-middle">
          <thead>
            <tr>
              <th>Order</th>
              <th>Date</th>
              <th>Total</th>
              <th>Payment</th>
              <th>Status</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="order" items="${orders}">
              <tr>
                <td>#${order.orderId}</td>
                <td>${order.createdAt}</td>
                <td>Rs. <fmt:formatNumber value="${order.totalAmount}" minFractionDigits="2" maxFractionDigits="2" /></td>
                <td>${order.paymentMethod} (${order.paymentStatus})</td>
                <td><span class="badge bg-primary">${order.orderStatus}</span></td>
                <td>
                  <a class="btn btn-sm btn-outline-primary"
                     href="${pageContext.request.contextPath}/orders?id=${order.orderId}">View</a>
                </td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </div>
    </c:otherwise>
  </c:choose>
</div>
<jsp:include page="/fragments/footer.jsp" />
</body>
</html>