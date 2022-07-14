<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    %>
    
  <%@ page isELIgnored="false" %>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
  <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
  <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
  
  <c:import url="WEB-INF/resources/IncludedFiles/Header.jsp"></c:import>
  
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<link rel="stylesheet" href="<spring:url value="/resources/styles/style.css" />">
</head>
<body>

	<div id="blur">
	
		<p style="text-align: center; font-size: 30px"><spring:message code="WelcomePage.title"/></p>
		<h2 id="error" style="text-align: center;"><strong>${errorMessage}</strong></h2>
		<form:form method="post" modelAttribute="player" action="login">

				<h2><spring:message code="WelcomePage.login.title"/></h2><br>
				
				<spring:message code="WelcomePage.login.playername.title"/>
				<form:input  type="text" path="name" title="${player.getName()}"/> <em><form:errors path="name" id="error"/></em>
				<br>
				<spring:message code="WelcomePage.login.password.title"/>  
				<form:input  type="password" path="password" title="${player.getPassword()}"/> <em><form:errors path="password" id="error"/></em>
				<br>

				<input type="submit" value="<spring:message code="WelcomePage.login.submit.title"/>">
		</form:form>
		
		<br><br><br><br>
		
		<form:form method="post"  modelAttribute="creatingPlayer" action="creatingPlayer">
				<h2><spring:message code="WelcomePage.signup.title"/></h2><br>
				<spring:message code="WelcomePage.signup.playername.title"/>
				<form:input  type="text" path="name" /> <em><form:errors path="name" id="error"/><br></em>
				<spring:message code="WelcomePage.signup.email.title"/>
				<form:input  type="email" path="email" value="${newBieEmail}"/> <em><form:errors path="email" id="error"/><br></em>
				<spring:message code="WelcomePage.signup.password.title"/>
				<form:input  type="password" path="password"/> <em><form:errors path="password" id="error"/><br></em>
				<input type="submit" value="<spring:message code="WelcomePage.signup.submit.title"/>">
		</form:form>
	</div>


</body>
</html>