<?php

/*
 * Following code will list all the patients
 */

// array for JSON response
$response = array();


// include db connect class
require_once __DIR__ . '/db_connect.php';

// connecting to db
$db = new DB_CONNECT();

// get all patients from patient_data table
$result = mysql_query("SELECT * FROM patient_data ORDER BY pid") or die(mysql_error());

// check for empty result
if (mysql_num_rows($result) > 0) {
    // looping through all results
    // products node
    $response["patient_data"] = array();
    
    while ($row = mysql_fetch_array($result)) {
        // temp user array
        $patient= array();
        $patient["id"]=$row["id"];
        $patient["fname"] = $row["fname"];
        $patient["mname"] = $row["mname"];
        $patient["lname"] = $row["lname"];
        $patient["city"] = $row["city"];
       

        // push single product into final response array
        array_push($response["patient_data"], $patient);
    }
    // success
    $response["success"] = 1;

    // echoing JSON response
    echo json_encode($response);
} else {
    // no patient found
    $response["success"] = 0;
    $response["message"] = "No patient found";

    // echo no users JSON
    echo json_encode($response);
}
?>
