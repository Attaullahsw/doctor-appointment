<?php

include '.././database/config.php';

$response['error'] = TRUE;
$response['msg'] = "";

if ($_SERVER['REQUEST_METHOD'] == "POST" && isset($_POST['deleteAppointment'])) {

    $appointment_id = $_POST['appointment_id'];

	if($db->delete('patient_appointment_tbl','patient_appointment_id',$appointment_id)){
		$response['delete'] = TRUE;
		$response['msg'] = "Appointment Canceled Successfully.";
		
	}else{
                $response['delete'] = FALSE;
                $response['msg'] = "Appointment Not Canceled Successfully.";
		 }
		 
		
   

    
    $response['error'] = FALSE;
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}


echo json_encode($response);


?>