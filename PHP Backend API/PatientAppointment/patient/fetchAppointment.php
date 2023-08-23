<?php

include '../database/config.php';

$response['error'] = FALSE;
$response['msg'] = "";
$hospital = array();
$appointment = array();

if ($_SERVER['REQUEST_METHOD'] == "POST" && isset($_POST['doctor_id'])) {
	
	$doctor_id = $_POST['doctor_id'];
	$q = "select * from appointment_tbl where doctor_id='$doctor_id' and DATEDIFF(appointment_date,DATE(NOW()))>=0";
    $AppointmentResult = $db->custom_query($q);

  
    foreach ($AppointmentResult as $key) {
        $appointment[] = $key;
    }
    $response['appointment'] = $appointment;
	
    $response['error'] = FALSE;
    
    
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}

echo json_encode($response);

?>
