<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ attribute name="value" type="java.lang.Integer" required="true" %>

<span class="text-warning" aria-label="${value} out of 5">
  <c:forEach begin="1" end="5" var="i">
    <c:choose>
      <c:when test="${i <= value}">&#9733;</c:when>
      <c:otherwise>&#9734;</c:otherwise>
    </c:choose>
  </c:forEach>
</span>