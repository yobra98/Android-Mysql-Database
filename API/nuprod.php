<?php
header('Content-type=application/json; charset=utf-8');
?>
<?php
 error_reporting( E_ALL | E_STRICT | E_NOTICE );
ini_set('display_errors', 1);
ini_set('html_errors', 1);

// array for JSON response
$response = array();

// check for required fields
if (isset($_POST['food_name']) && isset($_POST['food_price']) && isset($_POST['food_description']) && isset($_POST['food_category'])) {
    
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

$qry = "INSERT INTO food_details(food_name, food_price, food_description, food_category) VALUES('$food_name', '$food_price', '$food_description', $food_category)";

    // mysql inserting a new row
    $result = mysqli_query($link, $qry);

    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Product successfully created.";

        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";
        
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
