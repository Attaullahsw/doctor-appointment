<?php

include '../database/config.php';

$response['error'] = FALSE;
$response['msg'] = "";
$hospital = array();
$appointment = array();

if ($_SERVER['REQUEST_METHOD'] == "POST" && isset($_POST['patient_id'])) {
	
	$patient_id = $_POST['patient_id'];
	

    $AppointmentResult = $db->fetch_multi_row_order('patient_appointment_tbl',array("*"),
	array('patient_id'=>$patient_id),"DESC",'patient_appointment_id');

  
    foreach ($AppointmentResult as $key) {
		$appointment_id = $key->appointment_id;
		$appointment_time = $key->appointment_time;
		
		
		$appointment_result = $db->fetch_single_row('appointment_tbl','appointment_id',$key->appointment_id);
		$doctor_result = $db->fetch_single_row('doctor_tbl','doctor_id',$appointment_result->doctor_id);
		
		$data = $key;
		$data->doctor_name = $doctor_result->doctor_name;
		$data->doctor_contact_no = $doctor_result->doctor_contact_no;
		$data->appointment_date = $appointment_result->appointment_date;
		
        $appointment[] = $data;
    }
    $response['appointment'] = $appointment;
	
    $response['error'] = FALSE;
    
    
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}

echo json_encode($response);

?>
