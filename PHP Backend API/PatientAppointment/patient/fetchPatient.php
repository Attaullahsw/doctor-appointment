<?php

include '../database/config.php';

$response['error'] = FALSE;
$response['msg'] = "";
$hospital = array();
$patient = array();

if ($_SERVER['REQUEST_METHOD'] == "POST" && isset($_POST['patient_id'])) {
	
	$patient_id = $_POST['patient_id'];

    $patientResult = $db->fetch_multi_row('patient_tbl',array("*"),array('patient_id'=>$patient_id));

  
    foreach ($patientResult as $key) {
        $patient[] = $key;
    }
    $response['patient'] = $patient;
	
    $response['error'] = FALSE;
    
    
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}

echo json_encode($response);

?>
