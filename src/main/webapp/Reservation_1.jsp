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
<style type="text/css">

		table {
				border-color: blue;
				border-width: medium;
				text-align: left;
			}	
</style>
</head>
<body>
	
	<div id="blur"  style="align-content: center; align-items: center; text-align: center;">
		<h1 >
			Please choose the sport you prefer
			</h1>
			
			<form method="post" action="2">
				<table border="1" class="center">
				<c:forEach items="${availableSports}" var="sport">
							<tr>
								<td>
									<input type="radio" name="chosen_Sport" value="${sport.getName()}"
										checked="checked">
								</td>
								<td>${sport.getName()}</td>
							</tr>
								
						</c:forEach>
				</table>
				<br>
							
				<input type="submit" value="Next" name="target_1" >			
			</form>
				
				<form method="get" action="cancel">
					<input type="submit" value="Cancel">
				</form>
	</div>

</body>
</html>