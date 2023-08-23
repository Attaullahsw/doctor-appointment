<?php

include '.././database/config.php';

$response['error'] = TRUE;
$response['msg'] = "";

if ($_SERVER['REQUEST_METHOD'] == "POST" && isset($_POST['deleteDeparment'])) {

    $department_id = $_POST['department_id'];

	if($db->delete('department_tbl','department_id',$department_id)){
		$response['delete'] = TRUE;
		$response['msg'] = "Department Deleted Successfully.";
		
	}else{
                $response['delete'] = FALSE;
                $response['msg'] = "Department Not Deleted Successfully.";
		 }
		 
		
   

    
    $response['error'] = FALSE;
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}


echo json_encode($response);


?>