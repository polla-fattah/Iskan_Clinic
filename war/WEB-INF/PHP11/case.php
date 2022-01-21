<?PHP
	$called = true;
	include("checkhim.php");
	
	function getRow($query){
		$qq = mysql_query($query) or die(mysql_error());
		$qq = mysql_fetch_array($qq, MYSQL_ASSOC) or die(mysql_error());
		return $qq;
	}
	
	function getQuery($query){
		$categorys = mysql_query($query) or die(mysql_error());
		$result = "[";
		if($category = mysql_fetch_array($categorys, MYSQL_ASSOC))
			$result .= json_encode($category);
			
		while($category = mysql_fetch_array($categorys, MYSQL_ASSOC)){
			$result .= ", ";
			$result .= json_encode($category);
		}
		$result .= "]";
		
		return $result;
	}//end of getQuery
?>

<?PHP
	if($_REQUEST['require'] == 'loading'){
		$case = getRow("SELECT * FROM  `case` WHERE  `idCase` = $_REQUEST[idCase] LIMIT 1");
		$patient = getRow("Select * from `patient` where `idPatient` = $case[idPatient] limit 1");
		$complains = getQuery("Select idComplain, idSystem, notes from case_complain where idCase = $_REQUEST[idCase]");
		echo json_encode($case), "#2@!", json_encode($patient), "#2@!", $complains;
	}
	elseif($_REQUEST['require'] == 'adding'){
		$query = "Insert Into `case_complain`(`idCase`, `idSystem`, `idComplain`, `notes`) values('$_REQUEST[idCase]', '$_REQUEST[idSystem]', '$_REQUEST[idComplain]', '$_REQUEST[note]')";
		mysql_query($query) or die(mysql_errno());
		die("success");
	}
	elseif($_REQUEST['require'] == 'removing'){
		$query = "delete from case_complain where idCase = $_REQUEST[idCase] and idComplain = $_REQUEST[idComplain]";
		mysql_query($query) or die(mysql_errno());
		die("success");
	}
	elseif($_REQUEST['require'] == 'statusUpdate'){
		$query = "Update `case` set `status`= '$_REQUEST[currentStatus]' where idCase = $_REQUEST[idCase]";
		mysql_query($query) or die(mysql_errno());
		die("success");
	}

?>