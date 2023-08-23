package com.example.patientappointmentproject.doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.patientappointmentproject.LoginActivity;
import com.example.patientappointmentproject.R;
import com.example.patientappointmentproject.patient.PatientAppointmentActivity;

public class DoctorDashBoradActivity extends AppCompatActivity {

    Context context = DoctorDashBoradActivity.this;

    SharedPreferences logPre;
    SharedPreferences.Editor preEditor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dash_borad);

        logPre = context.getSharedPreferences("UserLogin", MODE_PRIVATE);
        preEditor = logPre.edit();


    }

    public void startAppointmet(View view) {
        Intent intent = new Intent(context,StartAppointmentActivity.class);
        startActivity(intent);
    }

    public void appointmentList(View view) {
        Intent intent = new Intent(context,PatientAppointmentActivity.class);
        intent.putExtra("admin",true);
        intent.putExtra("doctor_id",logPre.getInt("temp_user_id",0));
        startActivity(intent);

    }

    public void gotoProfile(View view) {
        Intent intent = new Intent(context,DoctorProfile.class);
        startActivity(intent);
    }

    public void logout(View view) {
        preEditor.remove("user_name");
        preEditor.remove("user_pass");
        preEditor.apply();
        preEditor.commit();
        Intent intent = new Intent(context, LoginActivity.class);
        finish();
        startActivity(intent);
    }

    public void oldAppointment(View view) {
        Intent intent = new Intent(context,PatientAppointmentActivity.class);
        intent.putExtra("old_appointment",true);
        intent.putExtra("admin",true);
        intent.putExtra("doctor_id",logPre.getInt("temp_user_id",0));
        startActivity(intent);

    }
}
