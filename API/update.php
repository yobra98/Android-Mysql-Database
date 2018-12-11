<?php
header('Content-type=application/json; charset=utf-8');
?>
﻿<?php
error_reporting( E_ALL | E_STRICT | E_NOTICE );
ini_set('display_errors', 1);
ini_set('html_errors', 1);


$response = array();

// check for required fields
if (isset($_POST['food_id']) && isset($_POST['food_name']) && isset($_POST['food_price']) && isset($_POST['food_description'])&& isset($_POST['food_category'])) {
    
    $food_id = $_POST['food_id'];
    $food_name = $_POST['food_name'];
    $food_price = $_POST['food_price'];
    $food_description = $_POST['food_description'];
$food_category = $_POST['food_category'];

    // include db connect class
    require_once('connection/config.php');


    // connecting to db
    $link = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD);
    if(!$link) {
        printf('Failed to connect to server: ' , mysqli_error($link));
    }
    

    $db = mysqli_select_db($link, DB_DATABASE);
    if(!$db) {
        printf("Unable to select database",  mysqli_error($db));
    }

$qry = "UPDATE food_details SET food_name = '$food_name', food_price = '$food_price', food_description = '$food_description', food_category = $food_category WHERE food_id = $food_id";

    // mysql update row with matched pid
    $result = mysqli_query($link, $qry);

    // check if row inserted or not
    if ($result) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "Product successfully updated.";
        
        // echoing JSON response
        echo json_encode($response);
    } else {
        
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    // echoing JSON response
    echo json_encode($response);
}
?>
