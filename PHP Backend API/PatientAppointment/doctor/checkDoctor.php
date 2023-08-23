<?php

include '.././database/config.php';

$response['error'] = TRUE;
$response['msg'] = "";
$response['doctor'] = "";

if ($_SERVER['REQUEST_METHOD'] == "POST" && isset($_POST['checkDoctor'])) {

    $username = $_POST['username'];
    $password = $_POST['password'];
   
	
		 
		 $data = array(
		 'doctor_username'=>$username,
		 'doctor_password'=>$password
		 );
		 
		 $result = $db->fetch_multi_row2('doctor_tbl',$data);
		 $count = $result->rowCount();
			 if ($count>0) {
                $response['check'] = TRUE;
                $response['msg'] = "Valid Credentials";
				  foreach ($result as $key) {
						$response['doctor'] = $key;
					}
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