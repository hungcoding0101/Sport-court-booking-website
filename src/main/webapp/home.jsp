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
</head>
<body>

	<c:if test="${justRegisterred != null}">
		<div id="blur" style="padding-left: 200px">
			<p style="font-size: 30px; text-align: center;">
				Welcome new member!
				<br>
				You have been rewarded 50$
				<br>
				Thank you for becoming one of our members!
			</p>
		</div>
	</c:if>

</body>
</html>