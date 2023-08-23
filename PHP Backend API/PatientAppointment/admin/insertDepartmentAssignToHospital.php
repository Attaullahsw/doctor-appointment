<?php

include '.././database/config.php';

$response['error'] = TRUE;
$response['msg'] = "";

if ($_SERVER['REQUEST_METHOD'] == "POST" && isset($_POST['insertAssignDeparment'])) {

    $hospital_id = $_POST['hospital_id'];
    $department_id = $_POST['department_id'];
  
	
	 $data = array(
		 'hospital_id'=>$hospital_id,
		 'department_id'=>$department_id
		 );
		 
		 
			if ($db->insert('hospital_department_tbl', $data)) {
                $response['insert'] = TRUE;
                $response['msg'] = "Department Added Successfully.";
            } else {
                $response['insert'] = FALSE;
                $response['msg'] = "Department Not Added Successfully.";
            }
			 
		
   

    
    $response['error'] = FALSE;
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}


echo json_encode($response);


?>