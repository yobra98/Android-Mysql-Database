
<?php
    //require_once('auth.php');
	require_once('locale.php');
?>
<?PHP
//check if the starting row variable was passed in the URL or not
if (!isset($_GET['startrow']) or !is_numeric($_GET['startrow'])) {
  //we give the value of the starting row to 0 because nothing was found in URL
  $startrow = 0;
//otherwise we take the value from the URL
} else {
  $startrow = (int)$_GET['startrow'];
}
?>
<?php
//checking connection and connecting to a database
require_once('connection/config.php');
//Connect to mysql server
    $link = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD);
    if(!$link) {
        die('Failed to connect to server: ' . mysqli_error());
    }
    
    //Select database
    $db = mysqli_select_db($link,DB_DATABASE);
    if(!$db) {
        die("Unable to select database");
    }
    //retrive promotions from the specials table
    $result=mysqli_query($link,"SELECT * FROM food_details,categories WHERE food_details.food_category=categories.category_id LIMIT $startrow, 5")
    or die("There are no records to display ... \n" . mysql_error()); 
?>
<?php
    //retrive categories from the categories table
    $categories=mysqli_query($link,"SELECT * FROM categories")
    or die("There are no records to display ... \n" . mysqli_error()); 
?>
<?php
    //retrive a currency from the currencies table
    //define a default value for flag_1
    $flag_1 = 1;
    $currencies=mysqli_query($link,"SELECT * FROM currencies WHERE flag='$flag_1'")
    or die("A problem has occured ... \n" . "Our team is working on it at the moment ... \n" . "Please check back after few hours."); 
?>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Foods</title>
<link href="stylesheets/admin_styles.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" src="validation/admin.js">
</script>
</head>
<body>
<div id="page">
<div id="header">
<h1>Foods Management </h1>
<a href="index.php">Home</a> | <a href="categories.php">Categories</a> | <a href="foods.php"> > Foods < </a> | <a href="accounts.php">Accounts</a> | <a href="orders.php">Orders</a> | <a href="reservations.php">Reservations</a> | <a href="specials.php">Specials</a> | <a href="allocation.php">Staff</a> | <a href="messages.php">Messages</a> | <a href="options.php">Options</a> | <a href="content.php">Content</a> | <a href="template.php">Template</a> | <a href="logout.php">Logout</a>
</div>
<div id="container">
<fieldset><legend>New Food</legend>
<table width="930" align="center">
<form name="foodsForm" id="foodsForm" action="foods-exec.php" method="post" enctype="multipart/form-data" onsubmit="return foodsValidate(this)">
<tr>
    <th>Name</th>
    <th>Description</th>
    <th>Price</th>
    <th>Category</th>
    <th>Photo</th>
    <th>Action(s)</th>
</tr>
<tr>
    <td><input type="text" name="name" id="name" class="textfield" maxlength="15" placeholder="name the food" required/></td>
    <td><textarea name="description" id="description" class="textfield" rows="2" cols="15" maxlength="45" placeholder="describe the food" required></textarea></td>
    <td><input type="number" name="price" id="price" class="textfield" maxlength="15" placeholder="price the food" required/></td>
    <td width="168"><select name="category" id="category">
    <option value="select">- select category -
    <?php 
    //loop through categories table rows
    while ($row=mysqli_fetch_array($categories)){
    echo "<option value=$row[category_id]>$row[category_name]"; 
    }
    ?>
    </select></td>
    <td><input type="file" name="photo" id="photo" required/></td>
    <td><input type="submit" name="Submit" value="Add" /></td>
</tr>
</form>
</table>
</fieldset>
<hr>
<fieldset><legend>Available Foods</legend>
<table width="930" align="center" border="1">
<tr>
	<td colspan="6" align="right">
		<?PHP
		//create a "Previous" link
		$prev = $startrow - 5;
		//only print a "Previous" link if a "Next" was clicked
		if ($prev >= 0)
		echo '<a href="'.$_SERVER['PHP_SELF'].'?startrow='.$prev.'"><-Previous</a>';
		
		if ($prev >= 0 AND $prev < 0)
		//create a separator
		echo ' | ';
		
		if ($prev < 0)
		//create a "Next" link
		echo '<a href="'.$_SERVER['PHP_SELF'].'?startrow='.($startrow+5).'">Next-></a>';
		?>
	</td>
</tr>
<tr>
<th>Food Photo</th>
<th>Food Name</th>
<th>Food Description</th>
<th>Food Price</th>
<th>Food Category</th>
<th>Action(s)</th>
</tr>

<?php
//loop through all table rows
$symbol=mysqli_fetch_assoc($currencies); //gets active currency
while ($row=mysqli_fetch_array($result)){
echo "<tr>";
echo '<td><img src=../images/'. $row['food_photo']. ' width="80" height="70"></td>';
echo "<td>" . $row['food_name']."</td>";
echo "<td>" . $row['food_description']."</td>";
echo "<td>" . $symbol['currency_symbol']. "" . $row['food_price']."</td>";
echo "<td>" . $row['category_name']."</td>";
echo '<td><a href="delete-food.php?id=' . $row['food_id'] . '">Remove</a></td>';
echo "</tr>";
}
mysqli_free_result($result);
mysqli_close($link);
?>
	<tr>
		<td colspan="6" align="right">
			<?PHP
			//create a "Previous" link
			$prev = $startrow - 5;
			//only print a "Previous" link if a "Next" was clicked
			if ($prev >= 0)
			echo '<a href="'.$_SERVER['PHP_SELF'].'?startrow='.$prev.'"><-Previous</a>';
			
			if ($prev >= 0 AND $prev < 0)
			//create a separator
			echo ' | ';
			
			if ($prev < 0)
			//create a "Next" link
			echo '<a href="'.$_SERVER['PHP_SELF'].'?startrow='.($startrow+5).'">Next-></a>';
			?>
			</td>
	</tr>
</table>
</fieldset>
<hr>
</div>
<div id="footer">
<div class="bottom_addr">&copy; <?php echo date("Y") . " " . $name ?>. All Rights Reserved</div>
</div>
</div>
</body>
</html>
