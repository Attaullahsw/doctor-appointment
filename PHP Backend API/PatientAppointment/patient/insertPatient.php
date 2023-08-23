<?php

include '../database/config.php';

$response['error'] = TRUE;
$response['msg'] = "";

if ($_SERVER['REQUEST_METHOD'] == "POST" && isset($_POST['insertPatient'])) {

    $name = $_POST['name'];
    $male_female = $_POST['male_female'];
    $username = $_POST['username'];
    $password = $_POST['password'];
    $address = $_POST['address'];
    $contact = $_POST['contact'];
	
	if($db->check_exist('patient_tbl',array('patient_username'=>$username))){
		$response['insert'] = FALSE;
		$response['msg'] = "User Email Already Exits.";
		
	}else{
			 $data = array(
			 'patient_name'=>$name,
			 'patient_address'=>$address,
			 'patient_gender'=>$male_female,
			 'patient_contact_no'=>$contact,
			 'patient_username'=>$username,
			 'patient_password'=>$password
		 );
			 
			  if ($db->insert('patient_tbl', $data)) {
                $response['insert'] = TRUE;
                $response['msg'] = "User Added Successfully.";
            } else {
                $response['insert'] = FALSE;
                $response['msg'] = "User Not Added Successfully.";
            } 
		 }
	
    

   

    
    $response['error'] = FALSE;
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}


echo json_encode($response);


?>