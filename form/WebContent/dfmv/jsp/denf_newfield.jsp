<%@ page import="org.ihs.form.constants.DynamicFormConstants" %>

<div id="dialog-form" title="Create or Edit a Field">
  <p>All form fields are required.</p>
 
 <input id="count" type="hidden" value=0>
  <form id="AddFieldForm">
  	<input id="isedit" type="hidden">
  	<input id="edittrid" type="hidden">
  	<table>
  	<tr>
  	<td>Field Label:</td>
    <td> <input type="text" id="fieldLabel" name="fieldLabel"> </td>
    </tr>
    <tr>
    <td>Field Name:</td>  <td> <input error="" type="text" id="fieldName" name="fieldName"> <span id="fNameError" class="error-message"></span></td>
    </tr>
    <tr>
    <td>Field Type: </td> 
    <td><select id="fieldTypeId" name="fieldTypeId">
    	            <option></option>
    	            <c:forEach items="${fieldTypes}" var="fieldtype"> 
     	           <option value="${fieldtype.id}">${fieldtype.name}</option>
   		         	</c:forEach> 
   	    </select>
   	    </td>
  	</tr>
  	
  	<tr id="trModelOrList" style="display: none;">
  		<td><input style="width: auto;" id="radioModelOrList" type="radio" name="radioModelOrList" value=<%=DynamicFormConstants.RADIO_MODEL_VALUE %>>Model</td> 
  		<td><input style="width:auto;" id="radioModelOrList" type="radio" name="radioModelOrList" value=<%=DynamicFormConstants.RADIO_LIST_VALUE %>>List</td>
  	</tr>
  	
  	<tr style="display: none;" id="trCheckboxValue">
  		<td>Enter value for checkbox</td>
  		<td><input type="text" id=""></td>
  	</tr>
  	<tr style="display: none;" id="trList">
  		<td>Enter values</td>
  		<td><input type="text" id="txtValues" name="txtValues" placeholder="Enter comma separated values"></td>
  	</tr>
  	<tr style="display: none;" id="trModel">
  		<td>Enter Rest URL:</td>
  		<td>
  			<input name="modelRestUrl" id="modelRestUrl" type="text">

  		</td>
  	</tr>
  	<tr>
    <td><input style="display: none;" id="fieldTypeName" name="fieldTypeName"></td>
    <td><input style="display: none;" id="fieldTypeIdHidden" name="fieldTypeIdHidden" ></td>
	<td><input style="display: none;" id="fieldIdHidden" name="fieldIdHidden" value="0"></td>
    </tr>
      <!-- Allow form submission with keyboard without duplicating the dialog button -->
    <tr><td><input value="hello" type="submit" tabindex="-1" style="position:absolute; top:-1000px"></td></tr>
   </table>
  </form>
</div>

<div id="loadingDiv" style="display: none;"></div>