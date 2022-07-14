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

	<div id="blur">			
			<h2 style="text-align: center;">Thank you for inviting new members to our community \\(^_^)// </h2>
			
			<div style="text-align: center; align-content: center;">
				<form action="Inviting" method="post">
					Email address of your friend:
					<input  type="email" name="newBieEmail" required="required"> &thinsp; <span id="error">${sendInvitationMailarg0}</span><br>
					Your nickname shown to that friend: 
					<input  type="text" name="introducerNickname" required="required">&thinsp;<span id="error">${sendInvitationMailarg3}</span><br>
					<input type="submit" value="Submit">
				</form> 
			</div>
			<h3 align="center" id="error">An email containing an invitation link would be sent to your friend.
											If your friend click on that link then successfully create an account using that email address,
											You will receive a reward of 30$.</h3>
			
	</div>
	
</body>
</html>