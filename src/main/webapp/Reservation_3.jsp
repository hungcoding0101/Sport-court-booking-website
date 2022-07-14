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
			table {
				border-color: blue;
				border-width: medium;
			}	
</style>
</head>
<body>

	<div id="blur" style="padding-left: 200px">
		
		<h1 style="text-align: center;">Please choose date, time and duration of your reservations</h1>
		<br>
		<h2 id="error">${errorMessage}</h2>
		<br>
			<h3>Court: ${chosenCourt.getCode()}<br>
			Hourly price: ${chosenCourt.getHourlyPrice()} $</h3>
		<br><br>

	<form:form modelAttribute="reservation" action="4" method="post">
			Date:
			<form:input path="date" type="date" value="${dateBoundaries[0]}" 
			min = "${dateBoundaries[0]}" max = "${dateBoundaries[1]}"/>
			&thinsp;(Click the calendar symbol to choose date)&thinsp;
			<form:errors path="date" id="error"/>
			
			<br><br>
			Time:&thinsp;
			<form:select path="StartTime" name="StartTime" >
				<c:forEach begin="8" end="21" varStatus="status">
						<option value='<c:choose>
													<c:when test="${status.index < 10}">
														0${status.index}
													</c:when>
													<c:otherwise>${status.index}</c:otherwise>
												</c:choose>'>
												
								<c:choose>
										<c:when test="${status.index < 10}">
												0${status.index}
										</c:when>
										<c:otherwise>${status.index}</c:otherwise>
								</c:choose>
						<option>
				</c:forEach>		
			</form:select>

			&thinsp;
			
			<form:select path="StartTime" name="StartTime" >
						<option value="00">00<option>
						<option value="30">30</option>
			</form:select>
			<form:errors path="StartTime" id="error"/>
			
			<br><br>
			Duration:&thinsp;
			<form:select path="duration" name="duration">
				<c:forEach begin="0" end="13" varStatus="status">
						<option value='<c:if test="${status.index < 10}">0</c:if>${status.index}'>
								<c:if test="${status.index < 10}">0</c:if>${status.index}
						<option>
				</c:forEach>		
			</form:select>
			Hours
			&thinsp;
			
			<form:select path="duration" name="duration">
						<option value="00">00<option>
						<option value="30">30</option>
			</form:select>
			Minutes
			<form:errors path="duration" id="error"></form:errors>
			<br><br>
		<input type="submit" value="Next" name="target_3" />
	</form:form>
		
			<form method="post" action="rollback">
				<input type="hidden" value="Reservation_2" name="target">
				<input type="submit" value="Back" >
			</form>
			
			<form method="get" action="cancel">
				<input type="submit" value="Cancel">
			</form>
			
		<br><br><br><br>
		
	<c:set var="madeReservations" value="${chosenCourt.getReservations()}"></c:set>
		
	<c:if test="${madeReservations != null && !madeReservations.isEmpty()}">
		<div id="error" style="align-content: center; align-items: center; text-align: center;">
		*The court you chose have been occupied at following time frames:
			<br><br>
			
			<table class="center" border="1">
						<tr>
							<th>Date</th>
							<th>From</th>
							<th>To</th>
						</tr>
	
							<c:forEach items="${madeReservations}" var="oldReservation">
						
								<c:set var="startTime" value="${oldReservation.getStartTime()}"></c:set>
								<c:set var="endTime" value="${oldReservation.getEndTime()}"></c:set>
								<c:set var="date" value="${oldReservation.getDate()}"></c:set>
								<c:set var="formatter" value="${func:getDateFormatter('dd-MM-yyyy')}"></c:set>
								<c:set var="dateString" value="${date.format(formatter)}"></c:set>
								<c:set var="status" value="${oldReservation.getStatus()}"></c:set>
								<c:set var="statusString" value="${status.toString()}"></c:set>
								
								<c:if test="${statusString.equals('UNCONFIRMED') || statusString.equals('CONFIRMED')}">
									<tr>			  
										<td>${dateString}</td>
										<td>${startTime}</td>
										<td>${endTime}</td>
									</tr>
								</c:if>
								
							 </c:forEach>
				</table>
			</div>
		</c:if>
	</div>
</body>
</html>