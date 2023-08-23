<?php

include '../database/config.php';

$response['error'] = FALSE;
$response['msg'] = "";
$hospital = array();
$department = array();

if ($_SERVER['REQUEST_METHOD'] == "POST") {

    $hospitalResult = $db->fetch_all('Hospital_tbl');

    foreach ($hospitalResult as $key) {
        $hospital[] = $key;
    }
    $response['hospital'] = $hospital;

    $fetchDepartment = $db->fetch_all_order('department_tbl','department_title');

    foreach ($fetchDepartment as $key) {
        $department[] = $key;
    }
    $response['department'] = $department;
	
    $response['error'] = FALSE;
    
    
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}

echo json_encode($response);

?>