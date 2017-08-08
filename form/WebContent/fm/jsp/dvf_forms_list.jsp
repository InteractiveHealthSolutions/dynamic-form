<%@ include file="include.jsp"%>
<%@ include file="header.jsp"%>  
<style>
/* table {
    border-collapse: collapse;
}

table, th, td {
	padding:10px;
    border-bottom: 1px solid black;
}
tr:nth-child(even) {background-color: #f2f2f2} */
table,th,td{
	border: 1px solid #D6DADA;
}
</style>

 <head>
		 <title>Forms</title>
	 </head>
	 
<h1 class="light" align="center">Forms</h1>
<div align="center">
	<a href="${pageContext.request.contextPath}/fm/addform.htm">Create a new form</a>
	<br><br>
</div>
<c:choose>
	<c:when test="${fn:length(forms) eq 0}">
		<c:out value="No Forms to Display"></c:out>
	</c:when>
	
	<c:otherwise>
		<table class="center">
			<c:forEach items='${forms}' var="form">
				<tr>
					<td>
						<a>${form[0]}</a>
					</td>
					<td>
						<a class="no-underline" href="${pageContext.request.contextPath}/fm/editform.htm?formid=${form[1]}">Edit Form</a>
					</td>
					<td>
						<a class="no-underline" href="${pageContext.request.contextPath}/fm/showform.htm?name=${form[0]}">Fill Data</a>
					</td>
					<td>
						<a class="no-underline" href="${pageContext.request.contextPath}/fm/showformdata.htm?id=${form[1]}">View data</a>
					</td>
				</tr>
			</c:forEach>
		</table>
	</c:otherwise>
</c:choose>