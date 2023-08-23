package com.example.patientappointmentproject.models;

public class MyAppointmentModel {

    String appointment_id;
    String date;
    String time;
    String doctor_name;
    String doctor_contact;

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public String getDoctor_contact() {
        return doctor_contact;
    }

    public MyAppointmentModel(String appointment_id,String date, String time, String doctor_name, String doctor_contact) {
        this.date = date;
        this.time = time;
        this.doctor_name = doctor_name;
        this.doctor_contact = doctor_contact;
        this.appointment_id = appointment_id;
    }

    public String getAppointment_id() {
        return appointment_id;
    }
}
