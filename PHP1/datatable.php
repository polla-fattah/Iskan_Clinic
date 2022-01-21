<?PHP
	$called = true;
	include("checkhim.php");
	
	function getWhere($stmt){
		if($stmt == "")
			return "";
			
		$condition = "";
		foreach($stmt as $o){
			$colon = $o->operator != " IN " ? "'" : "";
			$condition .= $o->whereCondition ."  `". $o->field. "`". $o->operator. $colon.$o->value.$colon."  ";
		}
		$condition = substr($condition, 4,-1);
		return $condition;
	}
	function getOrder($stmt){
		if($stmt == "")
			return "";
			
		$order = "";
		foreach($stmt as $o){
			$order .= "`". $o->field. "` ". $o->sorting.", ";
		}
		$order = substr($order, 0,-2);
		return $order;
	}
	function getQuery($table, $fields, $cond, $from, $to, $order){
		if (trim($cond) != "")
			$cond = " Where $cond ";
		if (trim($order) != "")
			$order = " ORDER BY $order ";
		$qqq = "Select $fields from $table $cond $order Limit $from, $to;";
		$categorys = mysql_query($qqq) or die(mysql_error() . " " . $qqq);
		echo "[";
		if($category = mysql_fetch_array($categorys, MYSQL_ASSOC))
			echo json_encode($category);
			
		while($category = mysql_fetch_array($categorys, MYSQL_ASSOC)){
			echo ", ";
			echo json_encode($category);
		}
		
		echo "]";
	}//end of getQuery
	
	function editRow($table, $id, $idValue, $field, $fieldValue ){
		$query = "UPDATE $table SET `$field` = '$fieldValue' WHERE `$id` = '$idValue'";
		mysql_query($query) or die("Error". mysql_error());
		
		echo "Success";
	}
	
?>

<?PHP
	if($_REQUEST['require'] == 'loading'){
		//die(stripslashes($_POST['filters']));
		if( trim($_POST['filters']) != "[]"){
			$cond = json_decode( stripslashes($_POST['filters']));
			$cond = getWhere($cond);
		}
		else{
			$cond = "";
		}
		if( trim($_POST['orders']) != "[]"){
			$order = json_decode( stripslashes($_POST['orders']));
			$order = getOrder($order);
		}
		else
			$order = "";	
		
		getQuery ($_REQUEST['table'], substr($_REQUEST['fields'],0, -2), $cond, $_REQUEST['from'], $_REQUEST['to'], $order);
	}
	elseif($_REQUEST['require'] == 'editing'){
		editRow($_REQUEST['table'], $_REQUEST['id'], $_REQUEST['idValue'], $_REQUEST['field'], $_REQUEST['fieldValue']);
	}
	elseif($_REQUEST['require'] == 'removing'){
		$query = "delete from `$_REQUEST[table]` where `$_REQUEST[id]` = '$_REQUEST[idValue]';";
		mysql_query($query) or die(mysql_errno()."");
		die("success");
	}

?>
