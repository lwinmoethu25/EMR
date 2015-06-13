<?php

/*
 * Following code will delete a patient from table
 * A patient is identified by patient id (id)
 */

// array for JSON response
$response = array();

// check for required fields
if (isset($_POST['id'])) {
    $id = $_POST['id'];

    // include db connect class
    require_once __DIR__ . '/db_connect.php';

    // connecting to db
    $db = new DB_CONNECT();

    // mysql update row with matched id
    $result = mysql_query("DELETE FROM patient_data WHERE id = $id");
    
    // check if row deleted or not
    if (mysql_affected_rows() > 0) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "Patient successfully deleted";

        // echoing JSON response
        echo json_encode($response);
    } else {
        // no patient found
        $response["success"] = 0;
        $response["message"] = "No patient found";

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
