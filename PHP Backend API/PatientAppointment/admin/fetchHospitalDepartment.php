<?php

include '../database/config.php';

$response['error'] = FALSE;
$response['msg'] = "";
$hospital = array();
$department = array();

if ($_SERVER['REQUEST_METHOD'] == "POST" && isset($_POST['hospital_id'])) {
	
	$hospital_id = $_POST['hospital_id'];

    $departmentResult = $db->fetch_multi_row('hospital_department_tbl',array("*"),array('hospital_id'=>$hospital_id));

  
    foreach ($departmentResult as $key) {
		$result = $db->fetch_single_row('department_tbl','department_id',$key->department_id);
		$result->hospital_assign_id = $key->hospital_assign_id;
        $department[] = $result;
    }
    $response['department'] = $department;
	
    $response['error'] = FALSE;
    
    
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}

echo json_encode($response);

?>