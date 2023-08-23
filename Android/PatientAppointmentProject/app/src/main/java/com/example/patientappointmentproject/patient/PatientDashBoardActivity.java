package com.example.patientappointmentproject.patient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.patientappointmentproject.LoginActivity;
import com.example.patientappointmentproject.R;

public class PatientDashBoardActivity extends AppCompatActivity {

    Context context = PatientDashBoardActivity.this;

    SharedPreferences logPre;
    SharedPreferences.Editor preEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dash_board);
        logPre = context.getSharedPreferences("UserLogin", MODE_PRIVATE);
        preEditor = logPre.edit();


    }

    public void gotoappointment(View view) {
        Intent intent = new Intent(context,MyAppoinmentPatientActivity.class);
        startActivity(intent);
    }

    public void takeAppointmet(View view) {
        Intent intent = new Intent(context,AllHospitalPatientActivity.class);
        startActivity(intent);
    }

    public void gotoProfile(View view) {
        Intent intent = new Intent(context,PatientProfileActivity.class);
        startActivity(intent);
    }

    public void logout(View view) {
        preEditor.remove("patient_login_remenber");
        preEditor.remove("patient_user_name");
        preEditor.remove("patient_pass");
        preEditor.apply();
        preEditor.commit();
        Intent intent = new Intent(context, LoginActivity.class);
        finish();
        startActivity(intent);
    }
}
