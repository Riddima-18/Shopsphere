<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>ShopSphere - Manage Orders</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="/fragments/navbar.jsp" />

<div class="container">
  <div class="d-flex justify-content-between align-items-center mb-3">
    <h2 class="mb-0">Orders</h2>
    <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
  </div>

  <c:if test="${not empty param.message}">
    <div class="alert alert-success"><c:out value="${param.message}" /></div>
  </c:if>
  <c:if test="${not empty param.error}">
    <div class="alert alert-danger"><c:out value="${param.error}" /></div>
  </c:if>

  <c:choose>
    <c:when test="${empty orders}">
      <div class="alert alert-info">No orders yet.</div>
    </c:when>
    <c:otherwise>
      <div class="table-responsive">
        <table class="table align-middle">
          <thead>
            <tr>
              <th>Order</th>
              <th>Customer</th>
              <th>Date</th>
              <th>Total</th>
              <th>Payment</th>
              <th>Status</th>
              <th>Change status</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="o" items="${orders}">
              <tr>
                <td>#${o.orderId}</td>
                <td>${o.customerName}</td>
                <td>${o.createdAt}</td>
                <td>Rs. <fmt:formatNumber value="${o.totalAmount}" minFractionDigits="2" maxFractionDigits="2" /></td>
                <td>${o.paymentMethod} (${o.paymentStatus})</td>
                <td><span class="badge bg-primary">${o.orderStatus}</span></td>
                <td>
                  <c:choose>
                    <c:when test="${o.orderStatus == 'PLACED'}">
                      <form class="d-flex gap-2" method="post" action="${pageContext.request.contextPath}/admin/orders">
                        <input type="hidden" name="action" value="status">
                        <input type="hidden" name="orderId" value="${o.orderId}">
                        <select class="form-select form-select-sm" name="status" required>
                          <option value="">Choose...</option>
                          <option value="SHIPPED">Mark shipped</option>
                          <option value="CANCELLED">Cancel order</option>
                        </select>
                        <button class="btn btn-sm btn-primary" type="submit">Update</button>
                      </form>
                    </c:when>
                    <c:when test="${o.orderStatus == 'SHIPPED'}">
                      <form class="d-flex gap-2" method="post" action="${pageContext.request.contextPath}/admin/orders">
                        <input type="hidden" name="action" value="status">
                        <input type="hidden" name="orderId" value="${o.orderId}">
                        <input type="hidden" name="status" value="DELIVERED">
                        <button class="btn btn-sm btn-success" type="submit">Mark delivered</button>
                      </form>
                    </c:when>
                    <c:otherwise>
                      <span class="text-muted small">No further changes</span>
                    </c:otherwise>
                  </c:choose>
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