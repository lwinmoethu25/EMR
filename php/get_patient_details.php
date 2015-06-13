<?php

/*
 * Following code will get single patient details
 * A patient is identified by patient id (id)
 */

// array for JSON response
$response = array();


// include db connect class
require_once __DIR__ . '/db_connect.php';

// connecting to db
$db = new DB_CONNECT();

// check for post data
if (isset($_GET['id'])) {
    $id = $_GET['id'];

    // get a patient from patient table
    $result = mysql_query("SELECT * FROM patient_data WHERE id=$id");

    if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {

            $result = mysql_fetch_array($result);

            $patient = array();
            $patient["id"] = $result["id"];
            $patient["fname"] = $result["fname"];
            $patient["lname"] = $result["lname"];
            $patient["city"]  = $result["city"];
           
            // success
            $response["success"] = 1;

            // user node
            $response["patient_data"] = array();

            array_push($response["patient_data"], $patient);

            // echoing JSON response
            echo json_encode($response);
        } else {
            // no product found
            $response["success"] = 0;
            $response["message"] = "No product found";

            // echo no users JSON
            echo json_encode($response);
        }
    } else {
        // no patient found
        $response["success"] = 0;
        $response["message"] = "No product found";

        // echo no users JSON
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
