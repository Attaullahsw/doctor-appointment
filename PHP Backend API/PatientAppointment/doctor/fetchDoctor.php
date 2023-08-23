<?php

include '../database/config.php';

$response['error'] = FALSE;
$response['msg'] = "";
$hospital = array();
$doctor = array();

if ($_SERVER['REQUEST_METHOD'] == "POST" && isset($_POST['doctor_id'])) {
	
	$doctor_id = $_POST['doctor_id'];
	
    $doctorResult = $db->fetch_multi_row('doctor_tbl',array("*"),array('doctor_id'=>$doctor_id));

  
    foreach ($doctorResult as $key) {
		$hospitaldepatmetResult = $db->fetch_single_row('hospital_department_tbl',"hospital_assign_id",$key->hospital_department_id);
		
		$hospital_id = $hospitaldepatmetResult->hospital_id;
		$department_id = $hospitaldepatmetResult->department_id;
		
		$hospital = $db->fetch_single_row('hospital_tbl','hospital_id',$hospital_id);
		$department = $db->fetch_single_row('department_tbl',"department_id",$department_id);
		$result = $key;
		$result->department_name = $department->department_title;
		$result->hospital_name = $hospital->hospital_name;
        $doctor[] = $result;
    }
    $response['doctor'] = $doctor;
	
    $response['error'] = FALSE;
    
    
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}

echo json_encode($response);

?>
