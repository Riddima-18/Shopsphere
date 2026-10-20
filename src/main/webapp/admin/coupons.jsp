<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>ShopSphere - Coupons</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="/fragments/navbar.jsp" />

<div class="container">
  <div class="d-flex justify-content-between align-items-center mb-3">
    <h2 class="mb-0">Coupons</h2>
    <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
  </div>

  <c:if test="${not empty param.message}">
    <div class="alert alert-success"><c:out value="${param.message}" /></div>
  </c:if>
  <c:if test="${not empty param.error}">
    <div class="alert alert-danger"><c:out value="${param.error}" /></div>
  </c:if>

  <div class="card mb-4">
    <div class="card-body">
      <h5 class="card-title">New coupon</h5>
      <form method="post" action="${pageContext.request.contextPath}/admin/coupons" class="row g-2">
        <input type="hidden" name="action" value="save">
        <div class="col-md-3">
          <input class="form-control" type="text" name="code" placeholder="Code, e.g. SAVE10" required>
        </div>
        <div class="col-md-2">
          <select class="form-select" name="discountType" required>
            <option value="PERCENT">Percent (%)</option>
            <option value="FLAT">Flat (Rs.)</option>
          </select>
        </div>
        <div class="col-md-2">
          <input class="form-control" type="number" step="0.01" min="0.01" name="discountValue" placeholder="Value" required>
        </div>
        <div class="col-md-2">
          <input class="form-control" type="number" step="0.01" min="0" name="minimumOrder" placeholder="Min order (Rs.)">
        </div>
        <div class="col-md-3">
          <input class="form-control" type="number" step="0.01" min="0.01" name="maximumDiscount" placeholder="Max discount (optional)">
        </div>
        <div class="col-md-3">
          <input class="form-control" type="date" name="expiryDate" title="Expiry date (optional)">
        </div>
        <div class="col-md-2">
          <button class="btn btn-primary w-100" type="submit">Create</button>
        </div>
      </form>
    </div>
  </div>

  <div class="table-responsive">
    <table class="table align-middle">
      <thead>
        <tr>
          <th>Code</th><th>Discount</th><th>Min order</th><th>Max discount</th><th>Expires</th><th>Status</th><th></th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="c" items="${coupons}">
          <tr>
            <td><strong>${c.code}</strong></td>
            <td>${c.discountValue} ${c.discountType == 'PERCENT' ? '%' : 'Rs.'}</td>
            <td>Rs. ${c.minimumOrder}</td>
            <td>
              <c:choose>
                <c:when test="${empty c.maximumDiscount}">No cap</c:when>
                <c:otherwise>Rs. ${c.maximumDiscount}</c:otherwise>
              </c:choose>
            </td>
            <td>
              <c:choose>
                <c:when test="${empty c.expiryDate}">Never</c:when>
                <c:otherwise>${c.expiryDate}</c:otherwise>
              </c:choose>
            </td>
            <td>
              <c:choose>
                <c:when test="${c.status}"><span class="badge bg-success">Active</span></c:when>
                <c:otherwise><span class="badge bg-secondary">Disabled</span></c:otherwise>
              </c:choose>
            </td>
            <td>
              <form method="post" action="${pageContext.request.contextPath}/admin/coupons">
                <input type="hidden" name="action" value="toggle">
                <input type="hidden" name="id" value="${c.couponId}">
                <input type="hidden" name="current" value="${c.status}">
                <button class="btn btn-sm ${c.status ? 'btn-outline-danger' : 'btn-outline-success'}" type="submit">
                  ${c.status ? 'Disable' : 'Enable'}
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