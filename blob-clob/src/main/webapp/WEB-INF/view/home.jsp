<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>  
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

	<h1>Hello dear ;)</h1> <br>
	
	
	<table>
				<tr> 
					<th> Id </th>
					<th> File Name </th>
					<th> View/Download  </th>
					
				</tr>
				
				<c:forEach var="theCustomers" items="${allFiles}">
						<!-- for update customer  -->
					<c:url var="updateLink" value="/view">
						<c:param name="fileId" value="${theCustomers.id}"></c:param>
					</c:url>
					
						<!-- for delete customer  -->
					<c:url var="deleteLink" value="/download">
						<c:param name="fileId" value="${theCustomers.id }"></c:param>
					</c:url>
					
					<tr>
						<td> ${theCustomers.id} </td>
						<td> ${theCustomers.name} </td>
						<td> 
							<a href="${updateLink}" target="_blank">View</a>   
								|
							<a href="${deleteLink}" target="_blank">Download</a>	 
						
						</td>
					
					</tr>
					
				</c:forEach>
		
			</table> 
	<br> <br> <br>
	File upload <br>
	
	<form:form method="post" action="test" enctype="multipart/form-data">  
		<p><label for="image">Choose Image</label></p>  
		<p><input name="file" id="fileToUpload" type="file" multiple="multiple" /></p>  
		<p><input type="text" name="fileName"></p>
		<p><input type="submit" value="Upload"></p>  
	</form:form>  
	

</body>
</html>