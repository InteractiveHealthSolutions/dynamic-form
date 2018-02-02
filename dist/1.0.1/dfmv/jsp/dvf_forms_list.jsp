<%@ include file="include.jsp"%>
<%@ include file="header.jsp"%>


<script>
function deleteForm(id){
	$.ajax({
		contentType: "application/json; charset=utf-8",
		  type:'GET',
		  /* url: getAbsolutePath()+'/ajax/getColumns', */
		  url: getAbsolutePath()+'/ajax/deleteForm?id='+id,
		  success:function(data){
			  if(data > 0){
				  $(".class"+data).hide();
				  alert("Form deleted successfully");
			  }
			  else if(data < 0)
				  alert("Form could not be deleted. Some error occurred.");
		  }
	})
}
</script>
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
	<a href="${pageContext.request.contextPath}/dfm/addform.htm">Create a new form</a>
	<br><br>
</div>
<c:choose>
	<c:when test="${fn:length(forms) eq 0}">
		<c:out value="No Forms to Display"></c:out>
	</c:when>
	
	<c:otherwise>
		<table class="center">
			<c:forEach items='${forms}' var="form">
				<tr class="class${form[1]}">
					<td>
						<a>${form[0]}</a>
					</td>
					<td>
						<a class="no-underline" href="${pageContext.request.contextPath}/dfm/editform.htm?formid=${form[1]}">Edit Form</a>
					</td>
					<td>
						<a class="no-underline" href="${pageContext.request.contextPath}/dfm/showform.htm?name=${form[0]}">Fill Data</a>
					</td>
					<td>
						<a class="no-underline" href="${pageContext.request.contextPath}/dfm/showformdata.htm?id=${form[1]}">View data</a>
					</td>
					<td>
						<a class="no-underline" href="#" onclick="deleteForm(${form[1]});">Delete</a>
					</td>
				</tr>
			</c:forEach>
		</table>
	</c:otherwise>
</c:choose>