<?php

include '.././database/config.php';

$response['error'] = TRUE;
$response['msg'] = "";

if ($_SERVER['REQUEST_METHOD'] == "POST" && isset($_POST['insertDoctor'])) {

    $name = $_POST['name'];
    $male_female = $_POST['male_female'];
    $username = $_POST['username'];
    $password = $_POST['password'];
    $hospital_id = $_POST['hospital_id'];
    $department_id = $_POST['department_id'];
    $address = $_POST['address'];
    $contact = $_POST['contact'];
	$image = $_POST['image'];
    $image_name = $_POST['image_name'];
   
    $image_name = explode(".", $image_name);
    $extension = $image_name[sizeof($image_name) - 1];
    $image_name = $image_name[0] . rand() . "." . $extension;
	
	if($db->check_exist('doctor_tbl',array('doctor_username'=>$username))){
		$response['insert'] = FALSE;
		$response['msg'] = "User Email Already Exits.";
		
	}else{


	
	 if ((strcmp($image, "") != 0)) {
		 $decodedImage = base64_decode("$image");
		 $return = file_put_contents("../images/" . $image_name, $decodedImage);
		 
		 $hospitalDepartmentResult = $db->fetch_multi_row('hospital_department_tbl',array("*"),
		 array('hospital_id'=>$hospital_id,'department_id'=>$department_id));
		
		$data = "";
  
		foreach ($hospitalDepartmentResult as $key) {
			 $data = array(
			 'doctor_name'=>$name,
			 'doctor_address'=>$address,
			 'doctor_image'=>$image_name,
			 'doctor_gender'=>$male_female,
			 'doctor_contact_no'=>$contact,
			 'doctor_username'=>$username,
			 'doctor_password'=>$password,
			 'hospital_department_id'=>$key->hospital_assign_id
		 
		 );
		}
		 
		
		 
		 if($return){
			 
			  if ($db->insert('doctor_tbl', $data)) {
                $response['insert'] = TRUE;
                $response['msg'] = "Doctor Added Successfully.";
            } else {
                $response['insert'] = FALSE;
                $response['msg'] = "Doctor Not Added Successfully.";
            }
			 
		 }else{
			   $response['insert'] = FALSE;
                $response['msg'] = "Doctor Not Added Successfully.";
			 
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