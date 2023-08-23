<?php

include '.././database/config.php';

$response['error'] = TRUE;
$response['msg'] = "";

if ($_SERVER['REQUEST_METHOD'] == "POST" && isset($_POST['insertAppointment'])) {

    $date = $_POST['date'];
    $start_time = $_POST['start_time'];
    $end_time = $_POST['end_time'];
    $time_slice = $_POST['time_slice'];
    $doctor_id = $_POST['doctor_id'];
    $hospital_department_id = $_POST['hospital_department_id'];
   
   
				$data = array(
				'appointment_date'=>$date,
				'appointment_start_time'=>$start_time,
				'doctor_id'=>$doctor_id,
				'hospital_department_id'=>$hospital_department_id,
				);
				
				if($db->check_exist('appointment_tbl',$data)){
					 $response['insert'] = FALSE;
                $response['msg'] = "Appointment Already Start.";
				}else{
				$data = array(
				'appointment_date'=>$date,
				'appointment_start_time'=>$start_time,
				'appointment_end_time'=>$end_time,
				'appointment_per_patient_time'=>$time_slice,
				'doctor_id'=>$doctor_id,
				'hospital_department_id'=>$hospital_department_id,
				);
		
			  if ($db->insert('appointment_tbl', $data)) {
                $response['insert'] = TRUE;
                $response['msg'] = "Appointment Started Successfully.";
            } else {
                $response['insert'] = FALSE;
                $response['msg'] = "Appointment Not Started Successfully.";
            }
				}
    
    $response['error'] = FALSE;
} else {
    $response['msg'] = "Invalid Request";
    $response['error'] = true;
}


echo json_encode($response);


?>