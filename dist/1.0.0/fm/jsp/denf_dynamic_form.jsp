<%@ include file="header.jsp"%> 

<script>

</script>

<head>
<c:choose>
	<c:when test="${command.id>0}">
		<title>Edit Form</title>
	</c:when>
	<c:otherwise>
		<title>Create a Form</title>
	</c:otherwise>
</c:choose>
</head>

<h1 class="light" align="center">Create a Form</h1>
<%@ include file="denf_newfield.jsp" %>

<form method="post" id="frm" name="frm" >
	<input id="action" name="action" type="hidden">
		<table>
			<tr>
				<td colspan="1">Enter form name <span class="mendatory-field">*</span></td>
	        <td colspan="4">
		        <spring:bind path='command.formName'>
			        <input class="required" type="text" id="formName" name="${status.expression }" value="${status.value}"/>
					<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
				</spring:bind>
			</td>
			</tr>
			
			<tr>
			<td colspan="1">Search existing field name:</td>
			<td colspan="4">
				<input id="searchFields" name="searchFields">
			</td>
			<!-- <td> <a href="#" id="searchFieldsBtn">Add Field</a></td> -->
		</tr>
		</table> 
		<table id="fieldsDiv">
		<%-- <tr>
	     	<td colspan="1">Enter form name <span class="mendatory-field">*</span></td>
	        <td colspan="4">
		        <spring:bind path='command.formName'>
			        <input class="required" type="text" id="formName" name="${status.expression }" value="${status.value}"/>
					<span class="error-message"><c:out	value="${status.errorMessage}" /></span>
				</spring:bind>
			</td>
		</tr>
		
		<tr>
			<td colspan="1">Search existing field name:</td>
			<td colspan="4">
				<input id="searchFields" name="searchFields">
			</td>
			<!-- <td> <a href="#" id="searchFieldsBtn">Add Field</a></td> -->
		</tr> --%>
		
		<tr>
			<td colspan="5">Did not find the required field? <a id="searchFieldsBtn" href="#">Create</a> a new field</td>
			<!-- <td colspan="5" align="center"><input type="button" id="searchFieldsBtn" value="Add Field"></td> -->
		</tr>		
	
		<tr>
			<td colspan="1" style="vertical-align: top;">Design your form (enter valid html)</td>
		</tr>
			<tr>
				<td valign="top" colspan="1">
					<spring:bind path="command.rawHtml">
						<textarea id="htmltextarea" rows="20" name="rawHtml" style="width: 500px; overflow: auto;"><c:out value="${status.value}"></c:out></textarea>
						<span class="error-message cover-width" ><c:out	value="${status.errorMessage}" /></span>
					</spring:bind>
				</td>
			
			 <td colspan="3" valign="top">
			 	<p style=" font-style: italic;">The fields you add will be listed here. P.S. These fields would not show up in your form unless you specify them in your html.</p>
				<input type="text" id="searchFilter" placeholder="Type to filter" />
				<br/>
				<div style="max-height: 536px; overflow-y:scroll; width: 300px; ">
					<table style="border-collapse: collapse;" id="tblFields">
						<tbody>
						 	<c:if test="${fn:length(command.fieldsList) != 0}">
								 <c:forEach items="${command.fieldsList}" var="fi" varStatus="i">
						             <tr id="${i.index}">   
						             <!-- <td onclick="createFromExistingField(this);">Create</td> -->
							             <td>
							             	<spring:bind path="command.fieldsList[${i.index}].fieldName">                         
							                 	<input style="border: 0; text-align: center; cursor: pointer;" type='text' id='fName' name='${status.expression}' value="${status.value }" readonly/>
							                 </spring:bind>
							                 
							                 
							                 
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
							             
								             <spring:bind path="command.fieldsList[${i.index}].fieldLabel">
							           			<input type='hidden' id='fieldLabelHidden' name='${status.expression}' value="${status.value }">
								             </spring:bind>  
								             
								             <spring:bind path="command.fieldsList[${i.index}].id">
							           			<input type='hidden' id='fieldId' name='${status.expression}' value="${status.value }">
								             </spring:bind>  
								             
								             
							             </td>
							             <td align='center'><a class="linkiconS iconedit" href="#" id='editFieldBtn' onclick='editFieldTr(this);'></a></td>
					            		 <td align='center'><a class="linkiconS iconDelete" href="#" onclick='deleteFieldTr(this);'></a></td>
							             <%-- <td style="visibility:hidden; border-style: none;">
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
							             
								             <spring:bind path="command.fieldsList[${i.index}].fieldLabel">
							           			<input type='hidden' id='fieldLabelHidden' name='${status.expression}' value="${status.value }">
								             </spring:bind>  
								             
								             <spring:bind path="command.fieldsList[${i.index}].id">
							           			<input type='hidden' id='fieldId' name='${status.expression}' value="${status.value }">
								             </spring:bind>  
							             </td> --%>
						             </tr>
							     </c:forEach>
		     				</c:if> 
		     			</tbody>
					</table>
				</div>
			</td> 
		</tr>
		
		<tr><td colspan="5" align="center"><input class="button" type="button" onclick="previewhtml();" value="Preview"></td></tr>
		
		<tr>
			<td colspan="5" align="center">
				<hr>Preview:<hr>
			</td>
		</tr>
		<tr>
			<td colspan="5">
				<c:choose>
					<c:when test="${command.processedHtml != null}">
						<div style="border: 1px solid #D6DADA; padding: 5px; max-width:1560px; overflow-x:scroll;" id="previewhtmlarea">
					</c:when>
					<c:otherwise>
						<div id="previewhtmlarea">
					</c:otherwise>
				</c:choose>
				
<!-- 				<div style="border: 1px solid gray; padding: 5px;" id="previewhtmlarea">-->					
					<c:out value="${command.processedHtml}" escapeXml="False"><span style="font-style: italic;">Preview can be seen here.</span></c:out>
				</div>
			</td>
		</tr>
		
		<tr>
 	        <td colspan="3" align="center"><input class="button" type="button" onclick="submitFrmOnly()" id="submitFormOnlyBtn" value="Save Form"> <!-- <input class="button" type="button" onclick="submitFrmWithData()" id="submitBtn" value="Save Form with Data"> --></td>
		</tr>
		
		</table>
</form>
 
