<?php

include '.././database/config.php';

$response['error'] = TRUE;
$response['msg'] = "";

if ($_SERVER['REQUEST_METHOD'] == "POST" && isset($_POST['doctor_id'])) {

    $doctor_id = $_POST['doctor_id'];

	if($db->delete('doctor_tbl','department_id',$doctor_id)){
		$response['delete'] = TRUE;
		$response['msg'] = "Doctor Deleted Successfully.";
		
	}else{
                $response['delete'] = FALSE;
                $response['msg'] = "Doctor Not Deleted Successfully.";
		 }
		 
		
   

    
    $response['error'] = FALSE;
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}


echo json_encode($response);


?>