<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>

  <%@ page isELIgnored="false" %>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
  <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
  <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
  <%@taglib uri="http://lol.com/functions" prefix="func" %>
  
  <c:import url="WEB-INF/resources/IncludedFiles/Header.jsp"></c:import>
  
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<link rel="stylesheet" href="<spring:url value="/resources/styles/style.css" />">

<style type="text/css">
		h1 {
				text-align: center;
			}
</style>
</head>
<body>

	<div id="blur">
	<c:set var="formatter" value="${func:getDateFormatter('dd-MM-yyyy')}"></c:set>
			<h1>Your reservation has been made successfully! </h1>
	<br>
	<h2 id="error" style="text-align: center;">
							A confirmation email has been sent to you. <br>
							Please click the link in the mail to confirm this reservation.<br>
							If you don't confirm before 18:00 tomorrow, this reservation will be cancelled automatically
	</h2>
	<br>
	<c:set var="court" value="${successfulReservation.getCourt()}"></c:set>
	<c:set var="player" value="${successfulReservation.getPlayer()}"></c:set>
	<h3>
		Your account balance: ${player.getCoins()} $
		<br><br>
		Reservation code: ${successfulReservation.getReservationCode()}
		<br><br>
		Rental: ${successfulReservation.getRental()} $
		<br><br>
		Court: ${court.getCode()}
		<br><br>
		<c:set var="date" value="${successfulReservation.getDate()}"></c:set>
		Date: ${date.format(formatter)}
		<br><br>
		 Time: ${successfulReservation.getStartTime()} To ${successfulReservation.getEndTime()}
		
		
	</h3>
	</div>
	
</body>
</html>