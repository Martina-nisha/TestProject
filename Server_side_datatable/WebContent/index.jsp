<!DOCTYPE html>
<html lang="en">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Servlet pagination using data tables</title>
<link rel="stylesheet" type="text/css" href="//cdn.datatables.net/1.10.0/css/jquery.dataTables.css">
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/v/dt/dt-1.10.21/datatables.min.css"/>
<script type="text/javascript" src="//code.jquery.com/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="//cdn.datatables.net/1.10.0/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="https://cdn.datatables.net/v/dt/dt-1.10.21/datatables.min.js"></script>
<script type="text/javascript">

$(document).ready(function() {
	
	originalString = "One, Two, Three, Four, Five six"; 
    separatedArray = originalString.split(', '); 

    alert(separatedArray[0]);
	
	
	var x = "240,<button type='button' class='btn btn-info glyphicon glyphicon-edit' data-toggle='modal' data-target=''#edit_policy'>";
	
	x = x.split(",");
	
	//x = x.split("");
	
	alert("====== "+x[0]);
     
    $("#example").dataTable( {
    	
    	"bProcessing" : "Loading",
    	"bServerSide" : true,
    	"deferLoad": true,
        "sAjaxSource": "./DatatableControlServlet",
         "aoColumns": [
            { "mData": "messages" }
        ]
       
            } );

} );

</script>
</head>
<body>
<form action="">
<table width="70%" style="border: 3px;background: rgb(243, 244, 248);"><tr><td>
    <table id="example" class="display" cellspacing="0" width="100%">
        <thead>
            <tr>
                <th>Messages</th>
                
            </tr>
        </thead>       
    </table>
    </td></tr></table>
</form>
</body>
</html>