<?php

include '../database/config.php';

$response['error'] = FALSE;
$response['msg'] = "";
$hospital = array();
$doctor = array();

if ($_SERVER['REQUEST_METHOD'] == "POST" && isset($_POST['doctors'])) {
		
	$DoctorResult = "";
	if(isset($_POST['hospital_department_id'])){
		$hospital_department_id = $_POST['hospital_department_id'];
		  $DoctorResult = $db->fetch_multi_row('doctor_tbl',array("*"),array('hospital_department_id'=>$hospital_department_id));
	}else{
		  $DoctorResult = $db->fetch_all('doctor_tbl');
	}
   

  

  
    foreach ($DoctorResult as $key) {
        $doctor[] = $key;
    }
    $response['doctor'] = $doctor;
	
    $response['error'] = FALSE;
    
    
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}

echo json_encode($response);

?>