<?php

include '.././database/config.php';

$response['error'] = TRUE;
$response['msg'] = "";

if ($_SERVER['REQUEST_METHOD'] == "POST" && isset($_POST['checkAdmin'])) {

    $username = $_POST['username'];
    $password = $_POST['password'];
   
	
		 
		 $data = array(
		 'admin_username'=>$username,
		 'admin_password'=>$password
		 );
		 
		 $result = $db->fetch_multi_row2('admin_tbl',$data);
		 $count = $result->rowCount();
			 if ($count>0) {
                $response['check'] = TRUE;
                $response['msg'] = "Valid Credentials";
            } else {
                $response['check'] = FALSE;
                $response['msg'] = "Invalid Credentials!";
            }
			 
		

   

    
    $response['error'] = FALSE;
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}


echo json_encode($response);


?>