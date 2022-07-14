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
<title>Insert title here</title>

<link rel="stylesheet" href="<spring:url value="/resources/styles/style.css" />">

</head>
<body>

	<form:form action="Upload" method="post" enctype="multipart/form-data">
			name: <input type="text" name="name"><br>
			file: <input type="file" name="pic"><br>
			<input type="submit" value="send">
	</form:form>
	

</body>
</html>