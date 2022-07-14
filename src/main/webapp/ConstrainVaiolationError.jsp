<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
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
	
	<h1>Error</h1>
	<h2>You attempted to add invalid data to these fields:</h2>
	<c:forEach items="${violations}" var="violation">
		<h3>${violation.getPropertyPath()}:  ${violation.getMessage()}</h3> <br>
	</c:forEach>
	Please try again with valid data
				<form method="post" action="rollback">
					<input type="hidden" value="Reservation_3" name="target">
					<input type="submit" value="Back" >
				</form>
			
			<form method="get" action="cancel">
				<input type="submit" value="Cancel">
			</form>
	
</body>
</html>