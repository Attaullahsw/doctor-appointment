<?php

include '.././database/config.php';

$response['error'] = TRUE;
$response['msg'] = "";

if ($_SERVER['REQUEST_METHOD'] == "POST" && isset($_POST['updateDeparment'])) {

    $title = $_POST['title'];
    $image = $_POST['image'];
    $image_name = $_POST['image_name'];
   
    $image_name = explode(".", $image_name);
    $extension = $image_name[sizeof($image_name) - 1];
    $image_name = $image_name[0] . rand() . "." . $extension;


	if($db->check_exist('department_tbl',array('department_title'=>$title))){
		$response['insert'] = FALSE;
		$response['msg'] = "Department Already Exits.";
		
	}else{


	
	 if ((strcmp($image, "") != 0)) {
		 $decodedImage = base64_decode("$image");
		 $return = file_put_contents("../images/" . $image_name, $decodedImage);
		 
		 $data = array(
		 'department_title'=>$title,
		 'department_image'=>$image_name
		 
		 );
		 
		 if($return){
			 
			  if ($db->insert('department_tbl', $data)) {
                $response['insert'] = TRUE;
                $response['msg'] = "Department Added Successfully.";
            } else {
                $response['insert'] = FALSE;
                $response['msg'] = "Department Not Added Successfully.";
            }
			 
		 }else{
			   $response['insert'] = FALSE;
                $response['msg'] = "User Not Added Successfully.";
			 
		 }
		 
		 
		 }else{
			 
			 
			 
		 }
    
	}
   

    
    $response['error'] = FALSE;
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}


echo json_encode($response);


?>