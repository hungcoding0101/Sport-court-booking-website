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

<style type="text/css">
		td {
				align-content: center;
				align-items: center;
			}
		table {
				border-color: blue;
				border-width: medium;
			}	
		input.checkbox{
			width: 1em;
			height: 1em;
		}	
		
</style>

</head>
<body>
		<div id="blur" style="padding-left: 200px; padding-top: 40px">
			<h2>Your informations</h2>
			<br>
			Name: ${verifiedPlayer.getName()}
			<br><br>
			Email: ${verifiedPlayer.getEmail()} 
			<br><br>
			Account balance: ${verifiedPlayer.getCoins()}$
			<br><br>
			Passwords: ${verifiedPlayer.getPassword()}
			<br><br>
			<h2 id="error">${Message}</h2>
			<br>
			<h2>Change email or passwords</h2>
			<form action="changingInfo" method="post">
				Enter your current passwords: <input type="password" name="currentPasswords"> &thinsp; <em id="error">${changeInfoarg2}</em>
															  <em id="error">${wrongPasswords}</em>
				<br><br>
				New email: <input type="text" name="newEmail"> &thinsp; <em id="error">${changeInfoarg0}</em> <br><br>
				New passwords: <input type="password" name="newPasswords" >&thinsp;<em id="error">${changeInfoarg1}</em>
				<br><br>
				<input type="submit" value="Confirm">
			</form>
			<br>
			<br>
			<h2>Your reservations</h2>
	
		
			<form action="CancellingReservation" method="post" >
				<table border="1" >
					<tr>
						<th>Code</th>
						<th>Court</th>
						<th>Date</th>
						<th>From</th>
						<th>To</th>
						<th>Status</th>
						<th>Cancel</th>
					</tr>
					
					<c:forEach items="${verifiedPlayer.getReservations()}" var="reservation">
						<c:set var="startTime" value="${reservation.getStartTime()}"></c:set>
									<c:set var="code" value="${reservation.getReservationCode()}"></c:set>
									<c:set var="endTime" value="${reservation.getEndTime()}"></c:set>
									<c:set var="court" value="${reservation.getCourt()}"></c:set>
									<c:set var="date" value="${reservation.getDate()}"></c:set>
									<spring:eval expression="T(java.time.format.DateTimeFormatter).
										ofPattern('dd-MM-yyyy')" var="formatter"></spring:eval>
									<c:set value="${date.format(formatter)}" var="dateString"></c:set>
									<c:set var="status" value="${reservation.getStatus()}"></c:set>
									<c:set var="statusString" value="${status.toString()}"></c:set>
<%-- 						<c:if test="${statusString.equals('UNCONFIRMED') || statusString.equals('CONFIRMED')}"> --%>
							<tr>
								<td>${code}</td>
								<td>${court.getCode()}</td>
								<td>${dateString}</td>
								<td>${startTime}</td>
								<td>${endTime}</td>
								<td>${statusString}</td>
								<c:if test="${statusString.equals('UNCONFIRMED')}">
									<td><input type="checkbox" name="cancelled" class="checkbox"
										value="${reservation.getReservationCode()}">
									</td>
								</c:if>
							</tr>
<%-- 						</c:if> --%>
					</c:forEach>
				</table>
				<br>
			<input type="submit" value="cancel the chosen reservations">
		</form>	
	
		</div>
		
		
</body>
</html>