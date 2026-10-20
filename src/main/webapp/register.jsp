<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>ShopSphere - Register</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
<nav class="navbar navbar-dark bg-dark mb-4">
  <div class="container">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/products">ShopSphere</a>
  </div>
</nav>

<div class="container" style="max-width: 420px;">
  <div class="card shadow-sm">
    <div class="card-body p-4">
      <h3 class="mb-3">Create account</h3>

      <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
      </c:if>

      <form action="${pageContext.request.contextPath}/register" method="post">
        <div class="mb-3">
          <label class="form-label">Full name</label>
          <input class="form-control" type="text" name="name" value="${param.name}" required>
        </div>
        <div class="mb-3">
          <label class="form-label">Email</label>
          <input class="form-control" type="email" name="email" value="${param.email}" required>
        </div>
        <div class="mb-3">
          <label class="form-label">Mobile (optional)</label>
          <input class="form-control" type="text" name="mobile" value="${param.mobile}">
        </div>
        <div class="mb-3">
          <label class="form-label">Password (min 6 characters)</label>
          <input class="form-control" type="password" name="password" minlength="6" required>
        </div>
        <button class="btn btn-primary w-100" type="submit">Register</button>
      </form>

      <p class="mt-3 mb-0 small">Already have an account?
        <a href="${pageContext.request.contextPath}/login">Login</a></p>
    </div>
  </div>
</div>
<jsp:include page="/fragments/footer.jsp" />
</body>
</html>