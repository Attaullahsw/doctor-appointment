<?php

include '../database/config.php';

$response['error'] = TRUE;
$response['msg'] = "";
$response['patient'] = "";

if ($_SERVER['REQUEST_METHOD'] == "POST" && isset($_POST['checkPatient'])) {

    $username = $_POST['username'];
    $password = $_POST['password'];
   
	
		 
		 $data = array(
		 'patient_username'=>$username,
		 'patient_password'=>$password
		 );
		 
		 $result = $db->fetch_multi_row2('patient_tbl',$data);
		 $count = $result->rowCount();
			 if ($count>0) {
                $response['check'] = TRUE;
                $response['msg'] = "Valid Credentials";
				  foreach ($result as $key) {
						$response['patient'] = $key;
					}
            } else {
                $response['check'] = FALSE;
                $response['msg'] = "Invalid User Name or Password!";
            }
			 
		

   

    
    $response['error'] = FALSE;
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}


echo json_encode($response);


?>