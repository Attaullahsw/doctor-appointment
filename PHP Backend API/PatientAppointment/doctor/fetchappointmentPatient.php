
<?php

include '../database/config.php';

$response['error'] = FALSE;
$response['msg'] = "";
$hospital = array();
$patient = array();

if ($_SERVER['REQUEST_METHOD'] == "POST" && isset($_POST['AppointmentPatient'])) {
	
	$appointment_id = $_POST['appointment_id'];
	$appointment_time = $_POST['appointment_time'];
	
	

    $patientResult = $db->fetch_multi_row('patient_appointment_tbl',array("*"),
	array('appointment_id'=>$appointment_id,"appointment_time"=>$appointment_time));

  
    foreach ($patientResult as $key) {
		$result = $db->fetch_single_row('patient_tbl','patient_id',$key->patient_id);
        $patient[] = $result;
    }
    $response['patient'] = $patient;
	
    $response['error'] = FALSE;
    
    
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}

echo json_encode($response);

?>