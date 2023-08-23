<?php

include '.././database/config.php';

$response['error'] = TRUE;
$response['msg'] = "";

if ($_SERVER['REQUEST_METHOD'] == "POST" && isset($_POST['insertHospital'])) {

    $name = $_POST['name'];
    $address = $_POST['address'];
    $contact = $_POST['contact'];
	$image = $_POST['image'];
    $image_name = $_POST['image_name'];
   
    $image_name = explode(".", $image_name);
    $extension = $image_name[sizeof($image_name) - 1];
    $image_name = $image_name[0] . rand() . "." . $extension;


	
	 if ((strcmp($image, "") != 0)) {
		 $decodedImage = base64_decode("$image");
		 $return = file_put_contents("../images/" . $image_name, $decodedImage);
		 
		 $data = array(
		 'hospital_name'=>$name,
		 'hospital_address'=>$address,
		 'hospital_contact_no'=>$contact,
		 'hospital_image'=>$image_name
		 
		 );
		 
		 if($return){
			 
			  if ($db->insert('Hospital_tbl', $data)) {
                $response['insert'] = TRUE;
                $response['msg'] = "Hospital Added Successfully.";
            } else {
                $response['insert'] = FALSE;
                $response['msg'] = "Hospital Not Added Successfully.";
            }
			 
		 }else{
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