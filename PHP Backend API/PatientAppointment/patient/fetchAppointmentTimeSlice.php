
<?php

include '../database/config.php';

function SplitTime($StartTime, $EndTime, $Duration="60"){
    $ReturnArray = array ();// Define output
    $StartTime    = strtotime ($StartTime); //Get Timestamp
    $EndTime      = strtotime ($EndTime); //Get Timestamp

    $AddMins  = $Duration * 60;

    while ($StartTime <= $EndTime) //Run loop
    {
        $ReturnArray[] = date ("G:i", $StartTime);
        $StartTime += $AddMins; //Endtime check
    }
    return $ReturnArray;
}

$response['error'] = FALSE;
$response['msg'] = "";

$timeStatus = array();

if ($_SERVER['REQUEST_METHOD'] == "POST" && isset($_POST['start_time'])) {
	
	$appointment_id = $_POST['appointment_id'];
	$start_time = $_POST['start_time'];
	$end_time = $_POST['end_time'];
	$slice = $_POST['slice'];

   
    $response['appointment'] =  SplitTime($start_time, $end_time, $slice);
	foreach($response['appointment'] as $key){
		if($db->check_exist('patient_appointment_tbl',array('appointment_time'=>$key,'appointment_id'=>$appointment_id))){
			$timeStatus[] = 1;
		}else{
			$timeStatus[] = 0;
		}
	}		
	
    $response['timeStatus'] = $timeStatus;
	
    $response['error'] = FALSE;
    
    
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}

echo json_encode($response);

?>