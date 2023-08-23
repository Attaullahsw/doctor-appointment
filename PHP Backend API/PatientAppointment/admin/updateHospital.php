<?php

include '.././database/config.php';

$response['error'] = TRUE;
$response['msg'] = "";

if ($_SERVER['REQUEST_METHOD'] == "POST" && isset($_POST['updateHospital'])) {

    $hospital_id = $_POST['hospital_id'];
    $old_image_name = $_POST['old_image_name'];
    $name = $_POST['name'];
    $address = $_POST['address'];
    $contact = $_POST['contact'];
	$image = $_POST['image'];
    $image_name = $_POST['image_name'];
   
if(strcmp($image_name,$old_image_name) != 0){
		$image_name = explode(".", $image_name);
		$extension = $image_name[sizeof($image_name) - 1];
		$image_name = $image_name[0] . rand() . "." . $extension;
	}
   

	
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
		 
		 
		 $data = array(
		 'hospital_name'=>$name,
		 'hospital_address'=>$address,
		 'hospital_contact_no'=>$contact,
		 'hospital_image'=>$image_name
		 
		 );
		 
		 if($return){
			 
			  if ($db->update('Hospital_tbl', $data,'hospital_id',$hospital_id)) {
                $response['insert'] = TRUE;
                $response['msg'] = "Hospital Updated Successfully.";
            } else {
                $response['insert'] = FALSE;
                $response['msg'] = "Hospital Not Updated Successfully.";
            }
			 
		 }else{
			   $response['insert'] = FALSE;
                $response['msg'] = "Hospital Not Updated Successfully.";
			 
		 }
		 
		 
		 }else{
			 
			  $data = array(
		 'hospital_name'=>$name,
		 'hospital_address'=>$address,
		 'hospital_contact_no'=>$contact
		 );
			   if ($db->update('Hospital_tbl', $data,'hospital_id',$hospital_id)) {
                $response['insert'] = TRUE;
                $response['msg'] = "Hospital Updated Successfully.";
            } else {
                $response['insert'] = FALSE;
                $response['msg'] = "Hospital Not Updated Successfully.";
            }
			 
		 }
    

   

    
    $response['error'] = FALSE;
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}


echo json_encode($response);


?>