<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.Date" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>ShopSphere - JSP Demo</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="/fragments/navbar.jsp" />

<div class="container">
  <h2 class="mb-3">JSP syntax and JSTL functions</h2>

  <%-- JSP comment: removed by the server, never sent to the browser --%>

  <%!
    // Declaration: adds a field and a method to the generated servlet class.
    // The field is shared by every request to this page until Tomcat restarts.
    private int pageHits = 0;

    private String greet(String name) {
        return "Hello, " + name + "!";
    }
  %>

  <%
    // Scriptlet: plain Java that runs on every request
    pageHits++;
    String student = "Buddy";
    String today = new Date().toString();
  %>

  <h5 class="mt-4">Traditional JSP elements</h5>
  <ul>
    <li>Scriptlet variable (expression): <%= student %></li>
    <li>Declaration method (expression): <%= greet(student) %></li>
    <li>Page hits since Tomcat started: <%= pageHits %></li>
    <li>Current date (expression): <%= today %></li>
  </ul>

  <%-- Scriptlet variables are not visible to EL like this: ${student} would be empty.
       Use the c:set tag below when you want EL to read a value. --%>

  <c:set var="course" value="Advanced Java" />
  <c:set var="shopName" value="ShopSphere" scope="request" />

  <h5 class="mt-4">Expression Language and JSTL</h5>
  <ul>
    <li>c:set value: ${course}</li>
    <li>Request-scoped value: ${shopName}</li>
    <li>fn:length: ${fn:length(course)} characters</li>
    <li>fn:toUpperCase: ${fn:toUpperCase(course)}</li>
    <li>fn:contains "Java": ${fn:contains(course, 'Java')}</li>
    <li>fn:startsWith "Adv": ${fn:startsWith(course, 'Adv')}</li>
    <li>fn:replace: ${fn:replace(course, 'Advanced', 'Modern')}</li>
    <li>fn:split and fn:length (word count): ${fn:length(fn:split(course, ' '))}</li>
    <li>fn:join: ${fn:join(fn:split(course, ' '), ' | ')}</li>
    <li>fn:escapeXml: ${fn:escapeXml('<b>not bold</b>')}</li>
  </ul>

  <h5 class="mt-4">Words in the phrase (c:forEach over fn:split)</h5>
  <ol>
    <c:forEach var="word" items="${fn:split(course, ' ')}">
      <li>${word}</li>
    </c:forEach>
  </ol>
</div>

<jsp:include page="/fragments/footer.jsp" />
</body>
</html>