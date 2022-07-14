<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
  <%@ page isELIgnored="false" %>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
  <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
  <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Hung's Sport Center</title>

<link rel="stylesheet"  href="<spring:url value="/resources/styles/style.css"></spring:url>">

</head>
<body>

	 <section id="blur">
	 <a href='<spring:url value="/home"></spring:url>'>Home Page</a>
	 &thinsp; &thinsp;
	 
	 <div class="dropcontainer">
		<button class="dropbtn">Activities</button>
		<div class="dropcontent">
			<a href='<spring:url value="/Player/"></spring:url>'>Your account</a>
			<a href='<spring:url value="/reservation/1"></spring:url>'>Making Reservations</a>
			<a href='<spring:url value="/Invitation/"></spring:url>'>Inviting new member(and get rewarded)</a>
		</div>	
	</div>
<!-- 	 &thinsp; &thinsp; -->
<!-- 	 <a href="?language=en" >English</a>|<a href="?language=fr" >French</a> -->
	 &thinsp; &thinsp;
	 <a href='<spring:url value="/"></spring:url>'>Log in</a>
	&thinsp; &thinsp;|&thinsp; &thinsp;
	 <a href='<spring:url value="/logout"></spring:url>'>Log out</a>
	&thinsp; &thinsp;|&thinsp; &thinsp;
	Member: ${verifiedPlayer.getName()}
	 </section>
 
 
</body>
</html>