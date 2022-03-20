<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html lang="en">

<body>
	<c:url value="/resources/text.txt" var="url"/>
	<spring:url value="/resources/text.txt" htmlEscape="true" var="springUrl" />
	Spring URL: ${springUrl} at ${time}
	<br>
	JSTL URL: ${url}
	<br>
	Message: ${message}
  <h1>Sign in</h1>
  <%
      java.util.Date date = new java.util.Date();
  %>
  <h2>
      Now is
      <%=date.toString()%>
  </h2>
  <form id="loginForm" action="/auth/login" method="post">
      <fieldset>
          <div class="form-group">
              <label for="email">Email</label>
              <input type="email" class="form-control" name="email" id="email"
                     placeholder="Your email" autofocus="autofocus" required>
          </div>
          <div class="form-group">
              <label for="password">Password</label>
              <input type="password" class="form-control" name="password" id="password"
                     placeholder="Your Password" required>
          </div>
          <div class="form-group clearfix">
              <label for="remember-me">
                  <input type="checkbox" name="remember-me" id="remember-me"> <span>Keep me logged in</span>
              </label>
              <div class="float-right">
                  <button type="submit" class="btn btn-primary btn-block">Sign in</button>
              </div>
          </div>
      </fieldset>
  </form>
</body>
</html>