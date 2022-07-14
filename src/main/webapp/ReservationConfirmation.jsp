<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
  <%@ page isELIgnored="false" %>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
  <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
  <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
  <%@taglib uri="http://lol.com/functions" prefix="func" %>
  
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
		<c:set var="formatter" value="${func:getDateFormatter('dd-MM-yyyy')}"></c:set>
		<c:choose>
		<c:when test="${reservation != null}">
				<h2 >Your reservation has been confirmed successfully:<br><br>
						Court: ${reservation.getCourt().getCode()}<br>
						<c:set var="date" value="${reservation.getDate()}"></c:set>
						Date:  ${date.format(formatter)}<br>
						From: ${reservation.getStartTime()}   To: ${reservation.getEndTime()}<br>
						Rental: ${reservation.getRental()}&thinsp;$
						
				</h2>
		</c:when>
		<c:otherwise>
				<h2>The reservation doesn't exist or not eligible to be confirmed </h2>
		</c:otherwise>
	</c:choose>
	</div>

</body>
</html>