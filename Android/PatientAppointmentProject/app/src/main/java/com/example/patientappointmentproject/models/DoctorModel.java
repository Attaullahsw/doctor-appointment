package com.example.patientappointmentproject.models;

public class DoctorModel {

    int doctor_id;
    String doctor_name;
    String doctor_address;
    String doctor_gender;
    String doctor_contact_no;
    String doctor_username;
    String doctor_password;
    String doctor_hospital_department_id;
    String doctor_image;


    public DoctorModel(int doctor_id, String doctor_name, String doctor_address,String doctor_image,
                       String doctor_gender, String doctor_contact_no, String doctor_username, String doctor_password, String doctor_hospital_department_id) {
        this.doctor_id = doctor_id;
        this.doctor_name = doctor_name;
        this.doctor_address = doctor_address;
        this.doctor_gender = doctor_gender;
        this.doctor_contact_no = doctor_contact_no;
        this.doctor_username = doctor_username;
        this.doctor_password = doctor_password;
        this.doctor_hospital_department_id = doctor_hospital_department_id;
        this.doctor_image = doctor_image;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public String getDoctor_address() {
        return doctor_address;
    }

    public String getDoctor_gender() {
        return doctor_gender;
    }

    public String getDoctor_contact_no() {
        return doctor_contact_no;
    }

    public String getDoctor_username() {
        return doctor_username;
    }

    public String getDoctor_password() {
        return doctor_password;
    }

    public String getDoctor_hospital_department_id() {
        return doctor_hospital_department_id;
    }

    public String getDoctor_image() {
        return doctor_image;
    }
}
