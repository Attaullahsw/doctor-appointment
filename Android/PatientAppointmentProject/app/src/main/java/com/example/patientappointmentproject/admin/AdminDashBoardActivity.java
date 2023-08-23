package com.example.patientappointmentproject.admin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.patientappointmentproject.R;

import android.view.View;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;

public class AdminDashBoardActivity extends AppCompatActivity {

    Context context = AdminDashBoardActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dash_board);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_dash_board, menu);
        return true;
    }

    public void addDepartment(View view) {
        Intent intent = new Intent(context,AddDepartmentActivity.class);
        startActivity(intent);
    }

    public void addHospital(View view) {
        Intent intent = new Intent(context,AddHospitalActivity.class);
        startActivity(intent);
    }

    public void AllHospital(View view) {
        Intent intent = new Intent(context,AllHospitalAdminActivity.class);
        startActivity(intent);
    }

    public void AllDeparment(View view) {
        Intent intent = new Intent(context,AllDepartmentActivity.class);
        startActivity(intent);
    }

    public void addDoctor(View view) {
        Intent intent = new Intent(context,AddDoctorActivity.class);
        startActivity(intent);
    }

    public void AllDoctor(View view) {
        Intent intent = new Intent(context,AllDoctorActivity.class);
        startActivity(intent);
    }

    public void assignDepartment(View view) {
        Intent intent = new Intent(context,AssignDepartmentToHospitalActivity.class);
        startActivity(intent);
    }
}
