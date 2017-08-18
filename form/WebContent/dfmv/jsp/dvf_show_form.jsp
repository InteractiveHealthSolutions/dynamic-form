<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="include.jsp"%>
<%@ include file="header.jsp"%>

<script>

$(document).ready(function(){
	$("table").css("width","auto");
	$("table").css("height","auto");
	$("tr,td").css("width","auto");
	$("tr,td").css("height","auto");
});

</script>
 <head>
		 <title>${form.formName}</title>
	 </head>
<div>
	<form method="post" action="${pageContext.request.contextPath}/dfm/showform.htm">
		<input type="hidden" name="id" value="${form.id}">
		<c:out value="${form.processedHtml}" escapeXml="false"></c:out> 
		
		<br><br>
		<div style="text-align: center;">
			<input class="button" type="submit" value="Submit">
		</div>
	</form>
</div>