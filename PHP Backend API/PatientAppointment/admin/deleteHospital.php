<?php

include '.././database/config.php';

$response['error'] = TRUE;
$response['msg'] = "";

if ($_SERVER['REQUEST_METHOD'] == "POST" && isset($_POST['deleteHospital'])) {

    $hospital_id = $_POST['hospital_id'];

	if($db->delete('hospital_tbl','hospital_id',$hospital_id)){
		$response['delete'] = TRUE;
		$response['msg'] = "Hospital Deleted Successfully.";
		
	}else{
                $response['delete'] = FALSE;
                $response['msg'] = "Hospital Not Deleted Successfully.";
		 }
		 
		
   

    
    $response['error'] = FALSE;
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}


echo json_encode($response);


?>