<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="include.jsp"%>
<%@ include file="header.jsp"%>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/dfm/resources/DataTables/datatables.min.css"/>
 
<script type="text/javascript" src="${pageContext.request.contextPath}/dfm/resources/DataTables/datatables.min.js"></script> 

<script type="text/javascript" src="https://cdn.datatables.net/buttons/1.5.1/js/dataTables.buttons.min.js"></script>
<script type="text/javascript" src="https://cdn.datatables.net/buttons/1.5.1/js/buttons.flash.min.js"></script>
<style>
p.lighter{
font-style: italic;
font-size: 14px;
}
</style>

<script>

function getAbsolutePath() {
    var loc = window.location;
    var pathName = loc.pathname.substring(0, loc.pathname.lastIndexOf('/'));
    return loc.href.substring(0, loc.href.length - ((loc.pathname + loc.search + loc.hash).length - pathName.length));
}

function editClicked(fs_id){
	window.location.href = getAbsolutePath() + "/editFormData?fsid="+fs_id;
	console.log(fs_id);
}

$(document).ready(function(){
	
	$.ajax({
		  contentType: "application/json; charset=utf-8",
		  type:'POST',
		  url: getAbsolutePath()+'/ajax/getColumns',
		  data: '${form_id}',
		  dataType: 'text',
		  success:function(data){
			  console.log(JSON.parse(data));
			  console.log(data);
			  var table = $('#tableFormData').DataTable( {
				  	"scrollX":true,
				  	"scrollY":"600px",
			    	serverSide: true,
					 ajax: {
					        url: '${pageContext.request.contextPath}/dfm/ajax/dtsource',
					        data: {'form_id':"${form_id}"},
					        type: 'POST'
					    },
					    
			    	 columnDefs: [ {
			            targets: 0,
			            orderable:false,
			            sortable:false,
			            width:"10px",
			            render: function ( data, type, row ) {
			                return '<a href="#" class="linkiconS iconedit" onclick="editClicked('+data+')">'
			            	/* return data.substr( 0, 10 ); */
			            }
			        },
			        {targets: "_all",
			        render: function ( data, type, row ) {
			            return type === 'display' && data.length > 10 ? data.substr( 0, 10 ) + "..." :data;
			        }
			        }
			        ], 
			    
			    	 dom: 'lrtip',
			          buttons: [{
			             extend: 'csv',
			             text: 'Export to CSV',
			             filename: function(){
			                 var d = new Date();
			                 return '${form_name} ' + d;
			             }
			         }
			         ], 
			         
			         "pageLength": 10,
			         "paging":true,
			         "processing":true,
			         "columns":JSON.parse(data)/* [
			                    {"data":"created_date"},
			                    {"data":"select_field"},
			                    {"data":"radiomultiple"}] */
			        /*   "columns":'${aaColumns}'  */
			         /*  "aaData": data.data,
			          "aoColumns": data.columns, 
			          "paging":true,
			          "pageLength":10,
			          "ordering":true */
			        });
			  
		  },
		  error: function (callback) {
          	$("#loadingDiv").hide();
              alert("Some error occurred")
              console.log("${id}");
          }
	  });
	
	function editClicked(fs_id){
		console.log(fs_id);
	}
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
<p class="lighter">Data you submitted is shown below. <br> Names of the fields in the form are displayed in the top row and the corresponding data of the field is shown below the name of the field. </p>

<table id="tableFormData" class="table table-striped table-bordered" style="border-collapse:collapse; width:100%; min-width:100%; max-width: 100%;">
<!-- <tbody></tbody> -->
<tfoot></tfoot>
</table>