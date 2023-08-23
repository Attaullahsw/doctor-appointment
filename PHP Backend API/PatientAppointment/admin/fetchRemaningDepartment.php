<?php

include '../database/config.php';

$response['error'] = FALSE;
$response['msg'] = "";
$hospital = array();
$department = array();

if ($_SERVER['REQUEST_METHOD'] == "POST" && isset($_POST['hospital_id'])) {

	$hospital_id = $_POST['hospital_id'];

  

    $fetchDepartment = $db->fetch_all_order('department_tbl','department_title');

    foreach ($fetchDepartment as $key) {
		if(!$db->check_exist('hospital_department_tbl',array('hospital_id'=>$hospital_id,'department_id'
		=>$key->department_id
		))){
        $department[] = $key;
		}
    }
    $response['department'] = $department;
	
    $response['error'] = FALSE;
    
    
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}

echo json_encode($response);

?>