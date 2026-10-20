<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ page import="com.shopsphere.util.I18n" %>
<%@ page import="java.util.Locale" %>
<%
  Locale currentLocale = I18n.localeOf(request);
  java.util.ResourceBundle msg = I18n.bundle(currentLocale);
  request.setAttribute("msg", msg);
  request.setAttribute("currentLang", currentLocale.getLanguage());
  String here = request.getRequestURI().substring(request.getContextPath().length());
  String query = request.getQueryString();
  request.setAttribute("backPath", query == null ? here : here + "?" + query);
%>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
  <div class="container">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/products">${msg['app.name']}</a>
    <form class="d-flex mx-auto" action="${pageContext.request.contextPath}/products" method="get">
      <input class="form-control me-2" type="search" name="q" value="${keyword}" placeholder="${msg['nav.search']}">
      <button class="btn btn-warning" type="submit">${msg['nav.search.button']}</button>
    </form>
    <div class="d-flex align-items-center gap-3">
      <a class="text-white text-decoration-none" href="${pageContext.request.contextPath}/cart">${msg['nav.cart']}</a>
      <c:choose>
        <c:when test="${not empty sessionScope.user}">
          <a class="text-white text-decoration-none" href="${pageContext.request.contextPath}/orders">${msg['nav.orders']}</a>
          <c:if test="${sessionScope.user.role == 'ADMIN'}">
            <a class="text-warning text-decoration-none" href="${pageContext.request.contextPath}/admin/dashboard">${msg['nav.admin']}</a>
          </c:if>
          <span class="text-white small">${msg['nav.hi']}, ${sessionScope.user.name}</span>
          <a class="btn btn-outline-light btn-sm" href="${pageContext.request.contextPath}/logout">${msg['nav.logout']}</a>
        </c:when>
        <c:otherwise>
          <a class="btn btn-outline-light btn-sm" href="${pageContext.request.contextPath}/login">${msg['nav.login']}</a>
        </c:otherwise>
      </c:choose>
      <div class="btn-group btn-group-sm" role="group" aria-label="${msg['nav.language']}">
        <a class="btn ${currentLang == 'en' ? 'btn-light' : 'btn-outline-light'}"
           href="${pageContext.request.contextPath}/language?lang=en&back=${fn:escapeXml(backPath)}">EN</a>
        <a class="btn ${currentLang == 'hi' ? 'btn-light' : 'btn-outline-light'}"
           href="${pageContext.request.contextPath}/language?lang=hi&back=${fn:escapeXml(backPath)}">हिंदी</a>
      </div>
    </div>
  </div>
</nav>