<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="include.jsp"%>
<%@ include file="header.jsp"%>

<link rel="stylesheet" type="text/css" href="DataTables/datatables.min.css"/>
 
<script type="text/javascript" src="DataTables/datatables.min.js"></script> 

<script>
$(document).ready(function(){
	$("table,tr,td").css('width','auto');
  	var data =JSON.parse('${obj}');
    var table = $('#tableFormData').DataTable( {
    	 dom: 'Bfrtip',
         buttons: [{
             extend: 'csv',
             text: 'Export to CSV',
             filename: function(){
                 var d = new Date();
                 return '${form_name} ' + d;
             },
         }
         ],
          "aaData": data.data,
          "aoColumns": data.columns, 
          "paging":true,
          "pageLength":10,
          "ordering":true
        });
	$("#tableFormData").css('width','auto');

});
function deleterow(tableID) {
    var table = document.getElementById(tableID);
    var rowCount = table.rows.length;

    table.deleteRow(rowCount -1);
}
</script>

	 <head>
		 <title>${form_name}</title>
	 </head>
	 
<h1>${form_name}</h1>
<p>Data you submitted is shown below. <br> Names of the fields in the form are displayed in the top row and the corresponding data of the field is shown below the name of the field. </p>
<table id="tableFormData">
	
</table>