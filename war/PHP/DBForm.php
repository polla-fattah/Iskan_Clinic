<?PHP
	$called = true;
	include("checkhim.php");

	function getQuery($table, $fields, $idName, $idValue){
		$categorys = mysql_query("Select $fields from `$table` Where `$idName` = 'idValue';") or die("Error");
		if($category = mysql_fetch_array($categorys, MYSQL_ASSOC))
			echo json_encode($category);
		else
			echo "Not Available";
	}
	
	function editRow($table, $id, $idValue, $field, $fieldValue ){
		$query = "UPDATE `$table` SET `$field` = '$fieldValue' WHERE `$id` = '$idValue'";
		mysql_query($query) or die("Error". $query);
		
		echo "Success";
	}
	function addRow($table, $fields, $values ){
		$query = "Insert into `$table`($fields) values($values)";
		mysql_query($query) or die("Error". mysql_error());
		
		echo mysql_insert_id();
	}
	
?>
<?PHP
	if($_REQUEST['require'] == 'loading'){
		getQuery ($_REQUEST['table'], substr($_REQUEST['fields'],0, -2), $_REQUEST['id'],$_REQUEST['idValue']);
	}
	elseif($_REQUEST['require'] == 'editing'){
		editRow($_REQUEST['table'], $_REQUEST['id'], $_REQUEST['idValue'], $_REQUEST['field'], $_REQUEST['fieldValue']);
	}
	elseif($_REQUEST['require'] == 'adding'){
		
		addRow($_REQUEST['table'], stripslashes($_REQUEST['fields']), stripslashes($_REQUEST['values']));
	}
?>
