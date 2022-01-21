<?php
	$called = true;
	include("checkhim.php");
	
	function getQuery($query){
		$categorys = mysql_query($query) or die("Error");
		echo "[";
		if($category = mysql_fetch_array($categorys, MYSQL_ASSOC))
			echo json_encode($category);
			
		while($category = mysql_fetch_array($categorys, MYSQL_ASSOC)){
			echo ", ";
			echo json_encode($category);
		}
		echo "]";
	}//end of getQuery
	
	function addRow($table, $row){
		$fields = "(";
		$values = "(";
		foreach($row as $k => $v){
			if($v != -1 ){
					
				$fields .= "`$k`,";
				$values .= "'$v',";
			}
		}
		$fields = substr($fields, 0, -1).")";
		$values = substr($values, 0, -1).")";
		$query = "INSERT INTO $table$fields VALUES $values";
		mysql_query($query) or die("Error". mysql_error());
		
		echo mysql_insert_id();
	}
	function editRow($table, $row, $id){
		$sets = "";
		foreach($row as $k => $v)
			$sets .= "`$k`= '$v',";
			
		$sets = substr($sets, 0, -1);
		$query = "UPDATE $table SET $sets WHERE $id = '".$row[$id]."'";
		mysql_query($query) or die("Error". mysql_error());
		
		echo "Success";
	}
	function deleteRow($table, $id, $idValue){
		$query = "DELETE FROM $table WHERE $id = '$idValue'";
		mysql_query($query) or die("Error". mysql_error());
		
		echo "Success";
	}
	
?>