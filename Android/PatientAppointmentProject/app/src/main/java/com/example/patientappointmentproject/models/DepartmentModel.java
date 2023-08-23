package com.example.patientappointmentproject.models;

public class DepartmentModel {

    int department_id;
    String deparment_name;
    String department_image;

    int hospital_assign_id;

    public DepartmentModel(int department_id, String deparment_name, String department_image) {
        this.department_id = department_id;
        this.deparment_name = deparment_name;
        this.department_image = department_image;
    }

    public DepartmentModel(int department_id, String deparment_name, String department_image,int hospital_assign_id) {
        this.department_id = department_id;
        this.deparment_name = deparment_name;
        this.department_image = department_image;
        this.hospital_assign_id = hospital_assign_id;
    }


    public int getDepartment_id() {
        return department_id;
    }

    public String getDeparment_name() {
        return deparment_name;
    }

    public String getDepartment_image() {
        return department_image;
    }

    public int getHospital_assign_id() {
        return hospital_assign_id;
    }
}
