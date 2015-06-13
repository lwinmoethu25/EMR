<?php

/*
 * Following code will update a patient information
 * A patient is identified by patient id (id)
 */

// array for JSON response
 // include db connect class
    require_once __DIR__ . '/db_connect.php';

    // connecting to db
    $db = new DB_CONNECT();

$response = array();

// check for required fields
if (isset($_POST['id']) && isset($_POST['fname']) && isset($_POST['lname']) && isset($_POST['city'])) {
    
    $id = $_POST['id'];
    $fname = $_POST['fname'];
	$lname = $_POST['lname'];
    $city = $_POST['city'];

   
    // mysql update row with matched pid
    $result = mysql_query("UPDATE patient_data SET fname ='$fname',lname ='$lname', city='$city' WHERE id = $id");

    // check if row inserted or not
    if ($result) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "Patient successfully updated.";
        
        // echoing JSON response
        echo json_encode($response);
    } else {
        $response["success"]=0;
        $response["message"]= "No data updated";
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    // echoing JSON response
    echo json_encode($response);
}
?>
