<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ attribute name="product" type="com.shopsphere.model.Product" required="true" %>

<div class="card h-100">
  <div class="card-body">
    <h5 class="card-title">${product.name}</h5>
    <p class="text-muted mb-1">${product.brand}</p>
    <p class="card-text">${product.description}</p>
    <p class="mb-1">
      <strong>Rs. <fmt:formatNumber value="${product.finalPrice}" minFractionDigits="2" maxFractionDigits="2" /></strong>
      <c:if test="${product.discount > 0}">
        <span class="badge bg-success">${product.discount}% off</span>
      </c:if>
    </p>
    <p class="small mb-2">
      <c:choose>
        <c:when test="${product.stock > 0}">In stock: ${product.stock}</c:when>
        <c:otherwise><span class="text-danger">Out of stock</span></c:otherwise>
      </c:choose>
    </p>
    <t:rating value="${product.productId % 5 + 1}" />
    <a href="${pageContext.request.contextPath}/products?id=${product.productId}"
       class="btn btn-outline-primary btn-sm mt-2">View details</a>
  </div>
</div>