package com.example.patientappointmentproject.models;

public class AppointmentModel {

    int appointment_id;
    String appointment_date;
    String appointment_start_time;
    String appointment_end_time;
    int appointment_per_patient_time;
    int doctor_id;
    int hospital_department_id;
    int status;

    public AppointmentModel(int appointment_id, String appointment_date, String appointment_start_time,
                            String appointment_end_time, int appointment_per_patient_time, int doctor_id, int hospital_department_id) {
        this.appointment_id = appointment_id;
        this.appointment_date = appointment_date;
        this.appointment_start_time = appointment_start_time;
        this.appointment_end_time = appointment_end_time;
        this.appointment_per_patient_time = appointment_per_patient_time;
        this.doctor_id = doctor_id;
        this.hospital_department_id = hospital_department_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAppointment_id() {
        return appointment_id;
    }

    public String getAppointment_date() {
        return appointment_date;
    }

    public String getAppointment_start_time() {
        return appointment_start_time;
    }

    public String getAppointment_end_time() {
        return appointment_end_time;
    }

    public int getAppointment_per_patient_time() {
        return appointment_per_patient_time;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public int getHospital_department_id() {
        return hospital_department_id;
    }
}
