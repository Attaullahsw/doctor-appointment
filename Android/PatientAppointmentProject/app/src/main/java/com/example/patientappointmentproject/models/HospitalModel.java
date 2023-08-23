package com.example.patientappointmentproject.models;

public class HospitalModel {

    int hospital_id;
    String hospital_name;
    String hospital_address;
    String hospital_contact_no;
    String hospital_image;

    public HospitalModel(int hospital_id, String hospital_name, String hospital_address, String hospital_contact_no, String hospital_image) {
        this.hospital_id = hospital_id;
        this.hospital_name = hospital_name;
        this.hospital_address = hospital_address;
        this.hospital_contact_no = hospital_contact_no;
        this.hospital_image = hospital_image;
    }

    public int getHospital_id() {
        return hospital_id;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public String getHospital_address() {
        return hospital_address;
    }

    public String getHospital_contact_no() {
        return hospital_contact_no;
    }

    public String getHospital_image() {
        return hospital_image;
    }
}
