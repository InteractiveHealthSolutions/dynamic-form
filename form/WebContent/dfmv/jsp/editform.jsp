
  
  <%@ include file="header.jsp"%>  
<%@ include file="include.jsp"%>

<!-- <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">  -->
   

 

<%@ include file="denf_newfield.jsp" %>

 <head>
 	<c:if test="${command.id>0}">
		 <title>Edit Form</title>
    </c:if>
    <c:if test="${command.id <=0 }">
    	<title>Create a Form</title>
    </c:if>
</head>
	 
<form method="post" id="frm" name="frm" >
	<input id="action" name="action" type="hidden">
		<table id="fieldsDiv" class="denform-h">
		<tr>
	     	<td>Form Name <span class="mendatory-field">*</span></td>
	        <td>
	        <spring:bind path='command.formName'>
	        <!-- MUST be named as centerVisitDate: used in plt_vaccine_schedule for autopopulating date incase of status VACCINATED -->
	        <input class="required" type="text" id="formName" name="${status.expression }" value="${status.value}"/>
			<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
			</td>
		</tr>
		
		<tr>
			<td>Search Field Name:</td>
			<td>
				<input id="searchFields" name="searchFields">
			</td>
			<td> <a href="#" id="searchFieldsBtn">Add Field</a> <!-- <input type="button" id="searchFieldsBtn" value="Add Field"> --></td>
		</tr>
		
		<tr>
			<td colspan="3" align="center">
				<table style="border-collapse: collapse;" id="tblFields">
					<tr></tr>
					 <c:if test="${fn:length(command.fieldsList) != 0}">
			 <c:forEach items="${command.fieldsList}" var="fi" varStatus="i">
	             <tr style="background-color: #EEDDAF; " id="${i.index}">   
		             <td>
		             	<spring:bind path="command.fieldsList[${i.index}].fieldName">                         
		                 	<input style="border: 0; text-align: center;" type='text' id='fName' name='${status.expression}' value="${status.value }" readonly/>
		                 </spring:bind>
		             </td>
		             <td align='center'><i id='editFieldBtn' onclick='editFieldTr(this);' class='material-icons' style='color:green; cursor:pointer'>&#xe3c9;</i></td>
            		 <td align='center'><i onclick='deleteFieldTr(this);' class='material-icons' style='color:red; cursor:pointer'>&#xe872;</i></td>
		             <td>
		                 <spring:bind path="command.fieldsList[${i.index}].fieldType.id">
		                 	<input type="hidden" id='fTypeId' name='${status.expression}' value="${status.value }"/>
		                 </spring:bind>
		                 
		                 <spring:bind path="command.fieldsList[${i.index}].fieldType.name"> 
		                 	<input type="hidden" id='fTypeName' name='${status.expression}' value="${status.value }"/>
		                 </spring:bind>
		                 
		                 <spring:bind path="command.fieldsList[${i.index}].modelOrList">
		                 <input type='hidden' id='fModelOrList' name='${status.expression}' value="${status.value }">
            			</spring:bind>
            			
            			<spring:bind path="command.fieldsList[${i.index}].modelName">
            			<input type='hidden' id='fModelName' name='${status.expression}' value="${status.value }">
            			</spring:bind>
            			
            			<spring:bind path="command.fieldsList[${i.index}].fieldOptionsCommaDelimited">
            			<input type='hidden' id='fListOptions' name='${status.expression}' value="${status.value }">
		             </spring:bind>  
		             </td>
	             </tr>
		     </c:forEach>
	     </c:if> 
				</table>
			</td>
		</tr>
		
		<tr>
			<td colspan="3" style="vertical-align: top;">
			Enter HTML:
			</td>
		</tr>
		<tr>
			<td colspan="3">
			<spring:bind path="command.rawHtml">
				<textarea id="htmltextarea" rows="20" name="rawHtml" style="width: 100%"><c:out value="${status.value}"></c:out></textarea>
				<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
			</spring:bind>
			</td>
		</tr>
		
		<tr><td style=" "><input type="button" onclick="previewhtml();" value="Preview"></td></tr>
		
		<tr>
			<td colspan="3">
			<hr>
			Preview:
			<hr>
			<div id="previewhtmlarea">
				<c:out value="${command.processedHtml}" escapeXml="False"></c:out>
			</div>
			</td>
		</tr>
		
		<tr>
	        <td><input type="button" onclick="submitFrm()" id="submitBtn" value="Submit Data"></td>
		</tr>
		
		</table>
</form>
 
