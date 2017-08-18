function getAbsolutePath() {
    var loc = window.location;
    var pathName = loc.pathname.substring(0, loc.pathname.lastIndexOf('/'));
    return loc.href.substring(0, loc.href.length - ((loc.pathname + loc.search + loc.hash).length - pathName.length));
}

  var temp,form;
  var fieldName = null;
  var dialog;
  var fieldNames=[];
  var v;
  var isAjax = [];
  var clickedSaveFieldBtn=false;
  function subfrm(){
		submitThisForm();
	}

	function submitThisForm() {
		//document.getElementById("submitBtn").disabled=true;
		document.getElementById("frm").submit();
	}
	
  $(document).ready(function() {
	  $("#searchFilter").keyup(function () {
		  var input, filter, table, tr, td, i;
		  input = $(this).val();
		  filter = input.toUpperCase();
		  table = document.getElementById("tblFields");
		  tr = $('#tblFields tr');

		  // Loop through all table rows, and hide those who don't match the search query
		  for (i = 0; i < tr.length; i++) {
			  fn = $(tr[i]).find("#fName").val();
			  if(fn.toUpperCase().indexOf(filter) > -1){
				  tr[i].style.display = "";
			  }
			  else {
			        tr[i].style.display = "none";
			      }
		  }
		});
	  
	  $("#tblFields #fName").each(function(index){
		  fieldNames[index] =$(this).val();
	  });

	  $.validator.addMethod("fieldNameValidityCheck", function (value, element) {
	        if($("#fieldName").attr("error") == "true"){
	        	return false
	        }
	        else{ 
	        	return true;
	        	}
	    },"");
		
		$.validator.addMethod(
		        "checkCommaSeparatedValues",
		        function(value, element, regexp) {
		            var re = new RegExp(regexp);
		            return re.test(value);
		        },
		        "Please check your input"
		);
		
		v = $("#AddFieldForm").validate({
		   rules: {
			   fieldLabel:
			   {
			   required: true
			   },
			   fieldName:
				   {
				   required: true,
				   fieldNameValidityCheck: true
				   },
			   fieldTypeId:
		     { 
		    	 required: true
		     },
		     radioModelOrList:
		     {
		    	 required: true
		     },
		     modelRestUrl:
		    	 {
		    	 required: true
		    	 },
		     txtValues:
	    		 {
		    	 required: true,
		    	 checkCommaSeparatedValues: "(\\w+)(,\\s*\\w+\\s*)*$"
	    		 }
		   },
		   messages: {
			   fieldLabel:{
				   required: "Required"
			   },
			   fieldName:{
				   required: "Required"
				   },
			   fieldTypeId: "Please select a Type",
			   radioModelOrList: "Required",
			   modelRestUrl: "Required",
			   txtValues:{
				   required:"Required"		
			   }
		   },
		   errorPlacement: function(error, element) 
	        {
	            if ( element.is(":radio") ) 
	            {
	            	error.insertBefore(element);
	            }
	            else
	            	 error.insertAfter( element );
	        }
	});
		
	  
	  $('#fieldName').bind('input', function(){
		  makeFNInputValid();
		});
	 
	  $("#searchFields").on("autocompletechange", function(event,ui) {
			 if($("#searchFields").val()==""){
				 fieldName=null;
				 temp = null;
			 }
	    });
	  
	  
	  $("#fieldName").focusout(function(){
		  var dataToSend = $.trim($("#fieldName").val());
		  var exists=false;
		  var ignored = false;
		  var pickedName = $.trim($("#"+$("#edittrid").val()).find("#fName").val());		  
		  for(var i=0; i<fieldNames.length; i++){
			  if($("#isedit").val() == "true"){
				  console.log("true1");
				  if($.trim($("#"+$("#edittrid").val()).find("#fName").val()) != fieldNames[i]){
					  console.log("true2");
					  if($.trim(fieldNames[i]).toUpperCase() == dataToSend.toUpperCase()){
						  console.log("true3");
						  makeFNInputInvalid();
						  exists=true;
					  }
				  }
				  else ignored = true;
			  }
			  else{
				  console.log("true4");
				  if($.trim(fieldNames[i]) == dataToSend){
					  makeFNInputInvalid();
					  exists=true;
				  }
			  }
		  }
		  if(!exists && (dataToSend!="" && !(pickedName.toUpperCase() == dataToSend.toUpperCase())) ){
			  console.log("true5");
			  $("#loadingDiv").show();
			  $.ajax({
				  contentType: "application/json; charset=utf-8",
				  type:'POST',
				  async:false,
				  url: getAbsolutePath()+'/ajax/checkFieldExists.htm',
				  data: dataToSend ,
				  dataType: 'text',
				  success:function(data){
					  $("#loadingDiv").hide();
					  if(data == "true"){
						  makeFNInputInvalid();
					  }
					  else if(data == "false"){
						  makeFNInputValid();
					  }
					  if(clickedSaveFieldBtn == true){
						  persistFields();
						  clickedSaveFieldBtn = false;
					  }
				  },
				  error: function (callback) {
		            	$("#loadingDiv").hide();
		                alert("Some error occurred")
		            }
			  });
		  }
	  });
	  
	  var count = 0;
	 
	  $("#searchFields").autocomplete({
	        minLength: 1,
	        delay: 500,
	        //define callback to format results
	        source: function (request, response) {
	            $.getJSON(getAbsolutePath()+"/ajax/getAllFields.htm", request, function(result) {
	                response($.map(result, function(item) {
	                	if(item != null){
		                	return {
		                		fieldId : item.fieldId,
		                		// following property gets displayed in drop down
		                        label: item.fieldName + " ( "+ item.fieldType +") ",
		                        // following property gets entered in the textbox
		                        value: item.fieldName,
		                        
		                        type : item.fieldType,
		                        
		                        fieldTypeId : item.fieldTypeId,
		                        
		                        modelOrList : item.modelOrList,
		                        
		                        modelName : item.modelName,
		                        
		                        fieldOptions : item.fieldOptions,
		                        
		                        fieldLabel : item.fieldLabel	                        
		                        
		                     }
	                	}
	                }));
	            });
	        },
	        //define select handler
	         select : function(event, ui) {
	            if (ui.item) {
	            	var exists = false;
	            	for(var i=0; i<fieldNames.length; i++){
	        			  if($.trim(fieldNames[i]) == $.trim(ui.item.value)){
	        				  alert("Please choose a different name");
	        				  exists=true;
	        			  }
	        		}
	        		if(!exists){	        			
	        			$("#fieldLabel").val(ui.item.fieldLabel);
	        			$("#fieldIdHidden").val(ui.item.fieldId);
	        	    	$("#fieldTypeIdHidden").val(ui.item.fieldTypeId);
	        	  		$("#fieldName").val(ui.item.value);
	        	  		$("#fieldTypeName").val(ui.item.type);
	        	  		$("#radioModelOrList[value='"+ui.item.modelOrList+"']").prop("checked",true);
	        	  		if(ui.item.modelOrList == 'list'){
	        	  			$("#txtValues").val(ui.item.fieldOptions);
	        	  		}
	        	  		addFieldRow();
	        	  		form[0].reset();
	        	   }
	        		else{
	            	
	            	/*temp = ui.item;
	            	fieldName = null; 
	                return true;*/
		            }
		            }
	        },
	        close: function(el) {
	            el.target.value = '';
	        }
	    });
	  
	  
	  $('#tblFields').on('click','#fName',function() { 
		    $("#htmltextarea").insertAtCaret('&lt;field name = "'+$(this).val()+'"&gt;');
		    return false
		  });
	  
	  $.fn.insertAtCaret = function (myValue) {
		  return this.each(function(){
			  
			  tinymce.get("htmltextarea").execCommand('mceInsertContent', false, myValue);
		  //IE support
		  if (document.selection) {
		    this.focus();
		    sel = document.selection.createRange();
		    sel.text = myValue;
		    this.focus();
		  }
		  //MOZILLA / NETSCAPE support
		  else if (this.selectionStart || this.selectionStart == '0') {
		    var startPos = this.selectionStart;
		    var endPos = this.selectionEnd;
		    var scrollTop = this.scrollTop;
		    this.value = this.value.substring(0, startPos)+ myValue+ this.value.substring(endPos,this.value.length);
		    this.focus();
		    this.selectionStart = startPos + myValue.length;
		    this.selectionEnd = startPos + myValue.length;
		    this.scrollTop = scrollTop;
		  } else {
		    this.value += myValue;
		    this.focus();
		  }
		  });
		  };
		  
	var count = 0, b=1;
	 
	function validateForm()
	{
		var isErrors = false;
		if($("#fieldName").val() == "" || $("#fieldName").attr("error") == "true" || $("#fieldTypeId").val() <= 0){
			isErrors = true;
			return false;
		}
		else return true;
	}
	
	function addFieldRow(){
		firstRowId = parseInt($("#tblFields tr:first").attr("id"));
		if(firstRowId > 1)
			count = firstRowId + 1;
		else
			count = parseInt($("#tblFields tr:last").attr("id"))+1;
    	if(isNaN(count))
    		count = 0;
    	fieldNames[count] = $("#fieldName").val();
    	
    	$("#tblFields tbody" ).prepend( 
        	"<tr id="+count+">"
            +"<td>" 
            +"<spring:bind path='command.fieldsList'>"
            +"<input style='border: 0; text-align: center; cursor:pointer;' type='text' id='fName' name='fieldsList["+count+"].fieldName' value='"+$.trim($("#fieldName").val())+"' readonly>"
            +"</spring:bind>"
            +"</td>"
            
            +"<td align='center'><a class='linkiconS iconedit' href='#' id='editFieldBtn' onclick='editFieldTr(this);'></a></td>"
            +"<td align='center'><a class='linkiconS iconDelete' href='#' onclick='deleteFieldTr(this);'></a></td>"
            
            +"<td align='center' style='display: none;'>"
            +"<spring:bind path='command.fieldsList'>"
            +"<input type='hidden' id='fTypeName' name='fieldsList["+count+"].fieldType.name' value='"+$("#fieldTypeName").val()+"'>"
            +"<input type='hidden' id='fTypeId' name='fieldsList["+count+"].fieldType.id' value='"+$("#fieldTypeIdHidden").val()+"'>"
            
            +"<input type='hidden' id='fModelOrList' name='fieldsList["+count+"].modelOrList' value='"+($("#radioModelOrList:checked").length > 0 ? $("#radioModelOrList:checked").val() : "") +"'>"
            +"<input type='hidden' id='fModelName' name='fieldsList["+count+"].modelName' value='"+ ($("#selectModel option:selected").length ? $("#selectModel option:selected").val() : "") +"'>"
            +"<input type='hidden' id='fListOptions' name='fieldsList["+count+"].fieldOptionsCommaDelimited' value='"+$("#txtValues").val()+"'>"
            
            +"<input type='hidden' id='fieldId' name='fieldsList["+count+"].id' value='"+$("#fieldIdHidden").val()+"'>"
            +"<input type='hidden' id='fieldLabelHidden' name='fieldsList["+count+"].fieldLabel' value='"+$("#fieldLabel").val()+"'>"
            +"</spring:bind>"
            +"</td>"
            
            +"</tr>"
            )
          
	}
    function addUser() {
    	if($("#AddFieldForm").valid()){
	    	if($("#isedit").val() == "true")
	    		{
	    			editField();
	    		}
	    	else{
		    	addFieldRow();
	    	}
    	resetForm();
        $("#AddFieldForm").find("span").html("");
        dialog.dialog( "close" );
    	}
            
    } 
 
    function editField()
    {
    	updateArrayAfterFieldNameEdited();
    	$("#"+$("#edittrid").val()).find("#fieldLabelHidden").val($("#fieldLabel").val());
    	$("#"+$("#edittrid").val()).find("#fName").val($("#fieldName").val());
    	$("#"+$("#edittrid").val()).find("#fTypeId").val($("#fieldTypeId").val());
    	$("#"+$("#edittrid").val()).find("#fTypeName").val($("#fieldTypeName").val());
    	
    	/*New add - start*/
    	$("#"+$("#edittrid").val()).find("#fModelOrList").val($("#radioModelOrList:checked").val());
    	$("#"+$("#edittrid").val()).find("#fModelName").val($("#selectModel option:selected").val());
    	$("#"+$("#edittrid").val()).find("#fListOptions").val($("#txtValues").val());
    	$("#isedit").val("");
    	/*New add - end*/
    }
    
    function persistFields(){
    	clickedSaveFieldBtn = true;
    	var formData = {};
    	if( $("#AddFieldForm").valid()){
	        $("#AddFieldForm input[name][type='text'],input[name][style*='display: none']").each(function (index, node) {
	            formData[node.name] = node.value;
	        });
	        formData["radioModelOrList"] = $("#AddFieldForm #radioModelOrList:checked").length > 0 ? $("#AddFieldForm #radioModelOrList:checked").val() : null;
	        
	        $("#AddFieldForm select[name]").each(function (index, node) {
	            formData[node.name] = node.value;
	        });
	        
	        $.ajax({
				  contentType: "application/json; charset=utf-8",
				  type:'POST',
				  url: getAbsolutePath()+'/ajax/createField.htm',
				  data: JSON.stringify(formData) ,
				  dataType: 'json',
				  success:function(data){
					  if(data != null){
						  persistFields = false;
						  $("#fieldIdHidden").val(data);
		    	  		  if($("#isedit").val() == "true")
		    	  			editField();
		    	  		  else addFieldRow();
		    	  		dialog.dialog( "close" );
					  }
					  else{
						  alert("Some error occurred and field could not be persisted.");
					  }
				  },
		            error: function (callback) {
		            	persistFields=false;
		            	/*$("#loadingDiv").hide();*/
		                alert("Some error occurred and field could not be persisted.")
		            }
			  });
    	}
    }
    
    dialog = $( "#dialog-form" ).dialog({
      autoOpen: false,
      height: 400,
      width: 600,
      modal: true,
      buttons: {
    	"Save and Add to Form":persistFields,
        "Add to Form only": addUser,
        Cancel: function() {
          dialog.dialog( "close" );
          form[ 0 ].reset();
/*          $("#AddFieldForm").find("span").html("");
*/        }
      },
      close: function() {
        form[ 0 ].reset();
        resetForm();
/*        $("#AddFieldForm").find("span").html("");
*/      }
    });
 
    form = dialog.find( "form" ).on( "submit", function( event ) {
      event.preventDefault();
      addUser();
    });
    
    
    $("#editFieldBtn").on("click",function(){
      $("#fieldName").val($(obj).closest('tr').find("#fName").val());
  	  $("#fieldTypeId").val($(obj).closest('tr').find("#fTypeId").val());
  	  dialog.dialog("open");
    });
    
    $( "#fieldTypeId" ).change(function() {
    	$("#fieldTypeName").val($("#fieldTypeId option:selected").text());
    	$("#fieldTypeIdHidden").val($("#fieldTypeId option:selected").val());
    	
    	if($("#fieldTypeId option:selected").text() == "select"){
    		$("#trModelOrList").css('display','table-row');
    	}
    	else{
    		hideModelOrListTrs();
    	}
    	});
    
    $('input[type="radio"]').click(function(){
        if ($(this).val() === 'model')
        {
        	$("#trList").css("display","none");
        	$("#trModel").css("display","table-row");
        	$("#txtValues").val("");
        }
        else if($(this).val() === 'list'){
        	$("#trModel").css("display","none");
        	$("#trList").css("display","table-row");
        	$("#modelRestUrl").val("");
        }
      });
    
    $("#searchFieldsBtn").on("click", function(){
    	dialog.dialog("open");
    });
   
  });
  
  function previewhtml()
  {
	  var t = tinyMCE.activeEditor.getContent();
     /* alert(t.replace(/&lt;/gi,'<').replace(/&gt;/gi, '>').replace(/&nbsp;/gi,' '));*/
	  $("#action").val("preview");

//	  $('#htmltextarea').val(''/*$.base64Encode(t)*/);
//	  
//	  alert($('#htmltextarea').val());
	  //Cookies.set("tblFields", $("#tblFields tbody").html());
	  
	  $("#frm").submit();
  }
  
  function submitFrmWithData()
  {
	  $("#action").val("submit");
	  $("#frm").submit();
  }
  
  function submitFrmOnly()
  {
	  $("#action").val("submitFormOnly");
	  $("#frm").submit();
  }
  
  function deleteFieldTr(obj)
  {
	  var row = $(obj).closest('tr');
	  var fieldname = $.trim(row.find("#fName").val());
	  
	  row.find("#fieldLabelHidden").val("deleted");
	  
/*	  row.find("#fTypeId").val(null);
	  row.find("#fTypeName").val("dfgdffh"); */	  
	  row.hide();	  
	  for(var i=0; i<fieldNames.length; i++){
		  if(fieldNames[i] == fieldname){
			  fieldNames.splice(i,1);
		  }
	  }
  }
  
  function editFieldTr(obj){
	  $("#isedit").val("true");
	  copyFieldValuesFromTrToModalForm(obj);
/*	  var obj = $(obj).closest('tr');
	  $("#edittrid").val(obj.attr("id"));
	  console.log("**************** gdfhfghfg = "+ obj.find("#fieldId").val());
	  $("#fieldIdHidden").val(obj.find("#fieldId").val());
	  console.log("**************** fgj = "+ $("#fieldIdHidden").val());
	  $("#fieldLabel").val(obj.find("#fieldLabelHidden").val());
	  $("#fieldName").val(obj.find("#fName").val());
	  $("#fieldTypeId").val(obj.find("#fTypeId").val());
	  $("#fieldTypeName").val(obj.find("#fTypeName").val());
	  if(obj.find("#fTypeName").val() == "select"){
		  console.log("*** in select ******");
		  $("#trModelOrList").css('display','table-row');
		  if(obj.find("#fModelOrList").val() == "model"){
			  $('#radioModelOrList[value=' + obj.find("#fModelOrList").val() + ']').prop('checked',true);
			  $("#trModel").css("display","table-row");
			  $("#selectModel").val(obj.find("#fModelName").val());
		  }
		  else if(obj.find("#fModelOrList").val() == "list"){
			  console.log("*** in list ******");
			  $('#radioModelOrList[value=' + obj.find("#fModelOrList").val() + ']').prop('checked',true);
			  $("#trList").css("display","table-row");
			  $("#txtValues").val(obj.find("#fListOptions").val());
		  }
	  }*/
	  dialog.dialog("open");
  }
  
  function copyFieldValuesFromTrToModalForm(obj){
	  var obj = $(obj).closest('tr');
	  $("#edittrid").val(obj.attr("id"));
	  $("#fieldIdHidden").val(obj.find("#fieldId").val());
	  $("#fieldLabel").val(obj.find("#fieldLabelHidden").val());
	  $("#fieldName").val(obj.find("#fName").val());
	  $("#fieldTypeId").val(obj.find("#fTypeId").val());
	  $("#fieldTypeName").val(obj.find("#fTypeName").val());
	  if(obj.find("#fTypeName").val() == "select"){
		  $("#trModelOrList").css('display','table-row');
		  if(obj.find("#fModelOrList").val() == "model"){
			  $('#radioModelOrList[value="' + obj.find("#fModelOrList").val() + '"]').prop('checked',true);
			  $("#trModel").css("display","table-row");
			  $("#selectModel").val(obj.find("#fModelName").val());
		  }
		  else if(obj.find("#fModelOrList").val() == "list"){
			  $('#radioModelOrList[value="' + obj.find("#fModelOrList").val() + '"]').prop('checked',true);
			  $("#trList").css("display","table-row");
			  $("#txtValues").val(obj.find("#fListOptions").val());
		  }
	  }
  }

  /*function createFromExistingField(obj){
	  $("#isedit").val("");
	  copyFieldValuesFromTrToModalForm(obj);
	  dialog.dialog("open");
  }*/
  function makeFNInputInvalid(){
	  $("#fNameError").html("Please choose a different name");
	  $("#fieldName").attr("error","true");
  }
  
  function makeFNInputValid(){
	  $("#fNameError").html("");
	  $("#fieldName").attr("error","false");
  }
  
  function resetForm(){
	  form[0].reset();
	  var f = $("#AddFieldForm");
	  form.find('.error,.valid').css('border-color', '').removeClass('error').removeClass('valid');
	  v.resetForm();
	  $("[id$=-error]").remove();
      makeFNInputValid();
      $("#isedit").val("");
      $("#trModelOrList").css("display","none");
      $("#trModel").css("display","none");
      $("#trList").css("display","none");
  }
  
  function hideModelOrListTrs(){
	  $("#trModelOrList").css('display','none');
      $("#trModel").css("display","none");
      $('input:checked').removeAttr('checked');
      $("#trList").css("display","none");
      
      $("#selectModel option:eq(0)").prop("selected", true);
      $("#txtValues").val("");
      $("#radioModelOrList-error").hide();
  }
  
  function updateArrayAfterFieldNameEdited(){
	  origFieldName = $("#"+$("#edittrid").val()).find("#fName").val();
  	  editedFieldName = $("#fieldName").val();
  	  if(origFieldName !== editedFieldName){
  		  for(var i=0; i<fieldNames.length; i++){
  			  if(fieldNames[i] === origFieldName){
  				fieldNames[i] = editedFieldName;
  			  }
  		  }
  	  }
  }