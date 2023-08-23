<?php

include '../database/config.php';

$response['error'] = TRUE;
$response['msg'] = "";

if ($_SERVER['REQUEST_METHOD'] == "POST" && isset($_POST['username'])) {

    
    $username = $_POST['username'];
   
	if($db->check_exist('patient_tbl',array('patient_username'=>$username))){
		$response['check'] = TRUE;
		$response['msg'] = "User Name Already Exits.";
		
	}else{
                $response['insert'] = FALSE;
                $response['msg'] = "User Not Added Successfully.";
          
		 }
	
    

   

    
    $response['error'] = FALSE;
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}


echo json_encode($response);


?>