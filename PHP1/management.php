<?php
	$called = true;
	include("checkhim.php");
	include("dbconnectivity.php");

	if($_REQUEST['require'] == 'loading'){
		if(isset($_REQUEST['user'])){
			getQuery("Select idItems, number, price, model.name, model.idModel, items.idBrand from model, items where model.idMOdel = items.idModel and $_REQUEST[idStore]");
			}
		else
			getQuery("Select * from $_REQUEST[table]");
	}
	elseif($_REQUEST['require'] == 'adding'){
		$row = json_decode( stripslashes ($_POST['row']), true);
		if(isset($_POST['id']))
			$row[$_POST['id']] = -1;
		addRow($_REQUEST['table'], $row);
	}
	elseif($_REQUEST['require'] == 'editing'){
		$row = json_decode( stripslashes ($_POST['row']), true);
		editRow($_REQUEST['table'], $row,$_POST['id']);
	}	
	elseif($_REQUEST['require'] == 'deleting'){
		deleteRow($_REQUEST['table'], $_POST['id'],$_POST['idvalue']);	
	}

?>