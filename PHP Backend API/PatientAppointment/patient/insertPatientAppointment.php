<?php

include '../database/config.php';

$response['error'] = TRUE;
$response['msg'] = "";

if ($_SERVER['REQUEST_METHOD'] == "POST" && isset($_POST['appointment_time'])) {

    $appointment_time = $_POST['appointment_time'];
    $patient_id = $_POST['patient_id'];
    $appointment_id = $_POST['appointment_id'];
   
	
	
			 $data = array(
			 'patient_id'=>$patient_id,
			 'appointment_id'=>$appointment_id
			 );
			 
			 
			  if(!$db->check_exist('patient_appointment_tbl',$data)){
				   $data = array(
			 'patient_id'=>$patient_id,
			 'appointment_id'=>$appointment_id,
			 'appointment_time'=>$appointment_time
			 );
				  if ($db->insert('patient_appointment_tbl', $data)) {
					$response['insert'] = TRUE;
					$response['msg'] = "Appointment Added Successfully.";
				} else {
					$response['insert'] = FALSE;
					$response['msg'] = "Appointment Not Added Successfully.";
				  } 
			  
			  }else{
				  $response['insert'] = FALSE;
				  $response['msg'] = "Appointment Already Taken.";
			  }
		
    

   

    
    $response['error'] = FALSE;
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}


echo json_encode($response);


?>