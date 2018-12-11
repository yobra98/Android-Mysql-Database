
<?php
header('Content-type=application/json; charset=utf-8');
?>
<?php
 error_reporting( E_ALL | E_STRICT | E_NOTICE );
ini_set('display_errors', 1);
ini_set('html_errors', 1);


// array for JSON response
$response = array();



require_once('connection/config.php');


 $link = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD);
    if(!$link) {
        printf('Failed to connect to server: ' , mysqli_error($link));
    }
    

    $db = mysqli_select_db($link, DB_DATABASE);
    if(!$db) {
        printf("Unable to select database",  mysqli_error($db));
    }

// check for post data
if (isset($_GET['food_id'])) {
if(!$_GET){
    $food_id = $_GET['food_id'];} else{printf("nyamo haija Setiwa", mysqli_error($_GET));}

$qry = "SELECT * FROM food_details,categories WHERE  food_details.food_category=categories.category_id food_id= '$food_id'";
    // get a product from products table
   
 $result = mysqli_query($link, $qry); 
	if (!$result) {
    printf("Error: %s\n", mysqli_error($link));
    exit();
}

$result = mysqli_fetch_array($result);

     if (!empty($result)) {
      printf(mysqli_error($result));
        if (mysqli_num_rows($result) > 0) {
 
            $result = mysqli_fetch_array($result);
 
            $products = array();
            $products["food id"] = $result["food_id"];
            $products["food name"] = $result["food_name"];
            $products["food price"] = $result["food_price"];
            $products["food description"] = $result["food_description"];
            $products["category"] = $result["food_category"];
            // success
            $response["success"] = 1;
 
            // user node
            $response["product"] = array();
 
            array_push($response["product"], $product);
 
     
            echo json_encode($response);
        } else {
            // no product found
            $response["success"] = 0;
            $response["message"] = "No product1 found";
 
            // echo no users JSON
            echo json_encode($response);
        }
    } else {
        // no product found
        $response["success"] = 0;
        $response["message"] = "No product2 found";
 
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


