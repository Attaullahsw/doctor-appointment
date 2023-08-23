<?php

include '.././database/config.php';

$response['error'] = TRUE;
$response['msg'] = "";

if ($_SERVER['REQUEST_METHOD'] == "POST") {
	
	if(isset($_POST['insertDeparment'])){
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
		 
		 
		 }
    
	}
   

    
    $response['error'] = FALSE;
	
	}else if($_POST['updateDeparment']){
		
		$department_id = $_POST['department_id'];
		$title = $_POST['title'];
		$image = $_POST['image'];
		$image_name = $_POST['image_name'];
		$old_image_name = $_POST['old_image_name'];
		
		
	if(strcmp($image_name,$old_image_name) != 0){
		$image_name = explode(".", $image_name);
		$extension = $image_name[sizeof($image_name) - 1];
		$image_name = $image_name[0] . rand() . "." . $extension;
	}
   
	  $c = $db->custom_query("select count(*) as c from department_tbl where department_id != '$department_id' and department_title='$title'");
            foreach ($c as $k) {

		if($k->c > 0){
			$response['insert'] = FALSE;
			$response['msg'] = "Department Already Exits.";
			
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
		 	 
			 $data = array(
			 'department_title'=>$title,
			 'department_image'=>$image_name
			 
			 );
			 
			 if($return){
				 
				  if ($db->update('department_tbl', $data,'department_id',$department_id)) {
					$response['insert'] = TRUE;
					$response['msg'] = "Department Updated Successfully.";
				} else {
					$response['insert'] = FALSE;
					$response['msg'] = "Department Not Updated Successfully.";
				}
				 
			 }else{
				   $response['insert'] = FALSE;
					$response['msg'] = "Department Not Added Successfully.";
				 
			 }
			 
			 
			 }else{
				  
				  if ($db->update('department_tbl', array('department_title'=>$title),'department_id',$department_id)) {
					$response['insert'] = TRUE;
					$response['msg'] = "Department Updated Successfully.";
				} else {
					$response['insert'] = FALSE;
					$response['msg'] = "Department Not Updated Successfully.";
				}
				 
			 }
		
		}
			}
	   

		
		$response['error'] = FALSE;
			
	}
	
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}


echo json_encode($response);


?>