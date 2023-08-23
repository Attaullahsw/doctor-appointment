<?php

include '.././database/config.php';

$response['error'] = TRUE;
$response['msg'] = "";

if ($_SERVER['REQUEST_METHOD'] == "POST" && isset($_POST['updateDoctor'])) {

    $doctor_id = $_POST['doctor_id'];
    $old_image_name = $_POST['old_image_name'];
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
	
	if(strcmp($image_name,$old_image_name) != 0){
		$image_name = explode(".", $image_name);
		$extension = $image_name[sizeof($image_name) - 1];
		$image_name = $image_name[0] . rand() . "." . $extension;
	}
   
   
     $c = $db->custom_query("select count(*) as c from doctor_tbl where doctor_id != '$doctor_id' and doctor_username='$username'");
            foreach ($c as $k) {

	
	if($k->c > 0){
		$response['insert'] = FALSE;
		$response['msg'] = "User Email Already Exits.";
		
	}else{


	
	 if ((strcmp($image, "") != 0)) {
		 $decodedImage = base64_decode("$image");
		 $return=true;
		 if(strcmp($image_name,$old_image_name) == 0){
		$return = true;
		}else{
			if (file_exists("../images/" .$old_image_name)) {
                            unlink("../images/" .$old_image_name);
							}
							$return = file_put_contents("../images/" . $image_name, $decodedImage);
		}
		 
		 
		 
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
			 
			 if ($db->update('doctor_tbl', $data,'doctor_id',$doctor_id)) {
                $response['insert'] = TRUE;
                $response['msg'] = "Doctor Updated Successfully.";
            } else {
                $response['insert'] = FALSE;
                $response['msg'] = "Doctor Not updated Successfully.";
            }
			 
		 }else{
			   $response['insert'] = FALSE;
                $response['msg'] = "Doctor Not updated Successfully.";
			 
		 }
		 
		 
		 }else{
			 
			  $hospitalDepartmentResult = $db->fetch_multi_row('hospital_department_tbl',array("*"),
		 array('hospital_id'=>$hospital_id,'department_id'=>$department_id));
		
		$data = "";
  
		foreach ($hospitalDepartmentResult as $key) {
			 $data = array(
			 'doctor_name'=>$name,
			 'doctor_address'=>$address,
			 'doctor_gender'=>$male_female,
			 'doctor_contact_no'=>$contact,
			 'doctor_username'=>$username,
			 'doctor_password'=>$password,
			 'hospital_department_id'=>$key->hospital_assign_id
		 
		 );
		}
			  
			 if ($db->update('doctor_tbl', $data,'doctor_id',$doctor_id)) {
                $response['insert'] = TRUE;
                $response['msg'] = "Doctor Updated Successfully.";
            } else {
                $response['insert'] = FALSE;
                $response['msg'] = "Doctor Not updated Successfully.";
            }
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