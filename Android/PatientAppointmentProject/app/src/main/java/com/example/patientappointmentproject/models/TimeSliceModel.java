package com.example.patientappointmentproject.models;

public class TimeSliceModel {
    int count;
    String time;
    int status;

    public TimeSliceModel(int count,String time, int status) {
        this.time = time;
        this.status = status;
        this.count = count;
    }

    public String getTime() {
        return time;
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status =  status;
    }

    public int getCount() {
        return count;
    }

}
