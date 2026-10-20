<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>ShopSphere - Product Form</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="/fragments/navbar.jsp" />

<div class="container" style="max-width: 720px;">
  <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-link px-0 mb-2">&larr; Back to products</a>
  <h2 class="mb-3">
    <c:choose>
      <c:when test="${product.productId > 0}">Edit product</c:when>
      <c:otherwise>Add product</c:otherwise>
    </c:choose>
  </h2>

  <c:if test="${not empty error}">
    <div class="alert alert-danger"><c:out value="${error}" /></div>
  </c:if>

  <form method="post" action="${pageContext.request.contextPath}/admin/products">
    <input type="hidden" name="action" value="save">
    <input type="hidden" name="productId" value="${product.productId}">

    <div class="mb-3">
      <label class="form-label">Name</label>
      <input class="form-control" type="text" name="name" value="${product.name}" required>
    </div>

    <div class="mb-3">
      <label class="form-label">Category</label>
      <select class="form-select" name="categoryId" required>
        <option value="">Choose...</option>
        <c:forEach var="cat" items="${categories}">
          <option value="${cat.categoryId}" ${product.categoryId == cat.categoryId ? 'selected' : ''}>${cat.name}</option>
        </c:forEach>
      </select>
    </div>

    <div class="row">
      <div class="col-md-6 mb-3">
        <label class="form-label">Brand</label>
        <input class="form-control" type="text" name="brand" value="${product.brand}">
      </div>
      <div class="col-md-6 mb-3">
        <label class="form-label">Image path (optional)</label>
        <input class="form-control" type="text" name="imageUrl" value="${product.imageUrl}" placeholder="images/earbuds.jpg">
      </div>
    </div>

    <div class="mb-3">
      <label class="form-label">Description</label>
      <textarea class="form-control" name="description" rows="3">${product.description}</textarea>
    </div>

    <div class="row">
      <div class="col-md-4 mb-3">
        <label class="form-label">Price (Rs.)</label>
        <input class="form-control" type="number" step="0.01" min="0.01" name="price" value="${product.price}" required>
      </div>
      <div class="col-md-4 mb-3">
        <label class="form-label">Discount (%)</label>
        <input class="form-control" type="number" step="0.01" min="0" max="100" name="discount" value="${product.discount}">
      </div>
      <div class="col-md-4 mb-3">
        <label class="form-label">Stock</label>
        <input class="form-control" type="number" min="0" name="stock" value="${product.stock}" required>
      </div>
    </div>

    <div class="form-check mb-4">
      <input class="form-check-input" type="checkbox" name="status" value="true" id="status"
             ${product.status ? 'checked' : ''}>
      <label class="form-check-label" for="status">Active (visible to customers)</label>
    </div>

    <button class="btn btn-primary" type="submit">Save product</button>
  </form>
</div>
<jsp:include page="/fragments/footer.jsp" />
</body>
</html>