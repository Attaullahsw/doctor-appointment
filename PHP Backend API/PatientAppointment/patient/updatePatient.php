<?php

include '../database/config.php';

$response['error'] = TRUE;
$response['msg'] = "";

if ($_SERVER['REQUEST_METHOD'] == "POST" && isset($_POST['updatePatient'])) {

    $patient_id = $_POST['patient_id'];
    $name = $_POST['name'];
    $male_female = $_POST['male_female'];
    $username = $_POST['username'];
    $password = $_POST['password'];
    $address = $_POST['address'];
    $contact = $_POST['contact'];
	
	 $c = $db->custom_query("select count(*) as c from patient_tbl where patient_id != '$patient_id' and patient_username='$username'");
            foreach ($c as $k) {
	if($k->c > 0){
		$response['update'] = FALSE;
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
			 
			  if ($db->update('patient_tbl', $data,'patient_id',$patient_id)) {
                $response['update'] = TRUE;
                $response['msg'] = "User Updated Successfully.";
            } else {
                $response['update'] = FALSE;
                $response['msg'] = "User Not Updated Successfully.";
            } 
		 }
			}
	
    

   

    
    $response['error'] = FALSE;
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}


echo json_encode($response);


?>