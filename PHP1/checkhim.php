<?php
	header('Content-Type: text/javascript');
	header('Cache-Control: no-cache');
	header('Pragma: no-cache');
	if(!isset($called) OR $called != true)
		die("You are doomed");
	
	mysql_connect("localhost", "root", "sanar") or die("Error in Connection");
	mysql_select_db("Medical") or die("Error1");
	
	if(isset($login) and $login == true){
		$getUser = mysql_query("Select name,spetiality, degree FROM doctor where username = '$_REQUEST[userName]' AND  password = '$_REQUEST[password]'") or die("Error2");
		if($user = mysql_fetch_array($getUser)){
			$rnd = rand(1,1000);
			mysql_query("UPDATE doctor set currentKey = '$rnd' where username = '$_REQUEST[userName]' AND  password = '$_REQUEST[password]'") or die("Error3");
			die("$rnd#2@!$user[spetiality]#2@!$user[name]#2@!$user[degree]");
		}
		else{
			die("Error4");
		}
	}
	else{
		$getUser = mysql_query("Select idDoctor FROM doctor where username = '$_REQUEST[userName]' AND  currentKey = '$_REQUEST[key]'") or die("Error");
		if(!($user = mysql_fetch_array($getUser)))
			die("Error5");
	}
?>