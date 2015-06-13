<?php

/*
 * Following code will create a new patient row
 * All patient details are read from HTTP Post Request
 */

// array for JSON response
$response = array();



    // include db connect class
    require_once __DIR__ . '/db_connect.php';

    // connecting to db
    $db = new DB_CONNECT();

// check for required fields
if (isset($_POST['fname']) && isset($_POST['lname']) && isset($_POST['DOB']) && isset($_POST['sex']) && isset($_POST['city'])) {
    
  
    $fname = $_POST['fname'];
    $lname = $_POST['lname'];
    $DOB = $_POST['DOB'];
    $sex = $_POST['sex'];
	$city= $_POST['city'];

	$sql = mysql_query("SELECT MAX(pid) as value FROM patient_data");
	$row = mysql_fetch_array($sql);
	$pid=$row['value']+1;
	//$pid=$pid+1;
	

    // mysql inserting a new row
    $result = mysql_query("INSERT INTO patient_data(fname, lname, DOB, sex, city, pubpid, pid) VALUES('$fname', '$lname', '$DOB', '$sex', '$city', $pid, $pid)");


	
	

    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = $result;
        
        // echoing JSON response
        echo json_encode($response);
    } else {
		
		$message="Invalid query:".mysql_error;
		echo $message;
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = $message;
        
        // echoing JSON response
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    // echoing JSON response
    echo json_encode($response);
}
?>
