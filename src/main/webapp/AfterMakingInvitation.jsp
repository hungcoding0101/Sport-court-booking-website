<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
  <%@ page isELIgnored="false" %>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
		<div id="blur", style="text-align: center; align-items: center; align-content: center;">
			<h1 style="text-align: center;">Thank you very much ^_^</h1>
			<h1 style="text-align: center;">Your invitation has been sent.</h1>
					<form action="">
						<input type="submit" value="Inviting more new member!">
					</form>
		</div>
</body>
</html>