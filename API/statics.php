
<?php
header('Content-type=application/json; charset=utf-8');
?>
<?php
error_reporting(E_ALL | E_STRICT);
ini_set('display_errors', 1);
ini_set('html_errors', 1);
/*
/*
 * Following code will list all the products
 */

// array for JSON response
$response = array();


// include db connect class
require_once('connection/config.php');
//Connect to mysql server
    $link = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD);
    if(!$link) {
        die('Failed to connect to server: ' . mysqli_error());
    }
    
    //Select database
    $db = mysqli_select_db($link, DB_DATABASE);
    if(!$db) {
        die("Unable to select database");
    }

// get all products from products table
//selecting all records from the food_details table. Return an error if there are no records in the table
$result=mysqli_query($link,"SELECT * FROM food_details,categories WHERE food_details.food_category=categories.category_id")
or die("A problem has occured ... \n" . "Our team is working on it at the moment ... \n" . "Please check back after few hours."); 

// check for empty result
if (mysqli_num_rows($result) > 0) {
    // looping through all results
    // products node
$response["products"] = array();
    
    while ($row = mysqli_fetch_array($result)) {
        // temp user array
        $product = array();
        $product["food id"] = $row["food_id"];
        $product["food name"] = $row["food_name"];
        $product["food price"] = $row["food_price"];
        $product["food description"] = $row["food_description"];
        $product["food category"] = $row["food_category"];
       



        // push single product into final response array
        array_push($response["products"], $product);
    }
    // success
    $response["success"] = 1;

    // echoing JSON response
    echo json_encode($response);
} else {
    // no products found
    $response["success"] = 0;
    $response["message"] = "No products found";

    // echo no users JSON
    echo json_encode($response);
}
?>
