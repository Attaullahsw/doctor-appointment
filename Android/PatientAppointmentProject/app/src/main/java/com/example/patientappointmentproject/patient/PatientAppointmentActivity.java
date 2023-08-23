package com.example.patientappointmentproject.patient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.patientappointmentproject.R;
import com.example.patientappointmentproject.adapters.AllDoctorPatientAdapter;
import com.example.patientappointmentproject.adapters.AllPatientAppointmentAdapter;
import com.example.patientappointmentproject.models.AppointmentModel;
import com.example.patientappointmentproject.models.DoctorModel;
import com.example.patientappointmentproject.models.TimeSliceModel;
import com.example.patientappointmentproject.util.CustomVolly;
import com.example.patientappointmentproject.util.UtilFunctions;
import com.example.patientappointmentproject.util.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PatientAppointmentActivity extends AppCompatActivity {

    RecyclerView list_all_appointment;
    ImageView img_empty_list;

    ArrayList<AppointmentModel> items = new ArrayList<>();
    AllPatientAppointmentAdapter adapter;

    Context context = PatientAppointmentActivity.this;


    int doctor_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_appointment);


        doctor_id = getIntent().getIntExtra("doctor_id", 0);

        if (doctor_id == 0) {
            Toast.makeText(context, "Some thing went wrong!", Toast.LENGTH_SHORT).show();
            finish();
        }
        list_all_appointment = findViewById(R.id.list_all_appointment);
        img_empty_list = findViewById(R.id.img_empty_list3);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        list_all_appointment.setLayoutManager(gridLayoutManager);
        list_all_appointment.setHasFixedSize(true);
        list_all_appointment.setItemViewCacheSize(20);
        list_all_appointment.setDrawingCacheEnabled(true);
        list_all_appointment.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        if (getIntent().getBooleanExtra("admin", false)) {
            adapter = new AllPatientAppointmentAdapter(context, items);
        } else {
            adapter = new AllPatientAppointmentAdapter(context, items, false);
        }

        list_all_appointment.setAdapter(adapter);


        getDoctor(doctor_id);
    }

    void getDoctor(int doctor_id) {
        String url = "";
        if (getIntent().getBooleanExtra("old_appointment", false)) {
            url = "doctor/fetchOldAppointment.php";
        } else {
            url = "patient/fetchAppointment.php";
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("doctor_id", String.valueOf(doctor_id));
        CustomVolly.getInsertPOSTData(context, params, url, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    boolean error = result.getBoolean("error");
                    if (error) {
                        UtilFunctions.warningAlert(context, "Something went wrong!", "").show();
                    } else {
                        JSONArray appointment = result.getJSONArray("appointment");
                        for (int i = 0; i < appointment.length(); i++) {
                            JSONObject a = appointment.getJSONObject(i);
                            items.add(new AppointmentModel(
                                    a.getInt("appointment_id"),
                                    a.getString("appointment_date"),
                                    a.getString("appointment_start_time"),
                                    a.getString("appointment_end_time"),
                                    a.getInt("appointment_per_patient_time"),
                                    a.getInt("doctor_id"),
                                    a.getInt("hospital_department_id")
                            ));

//                            checkSeats(a.getInt("appointment_id"),a.getString("appointment_start_time"),a.getString("appointment_end_time"),
//                                    a.getInt("appointment_per_patient_time"),i);
//                            Toast.makeText(context,String.valueOf(items.get(i).getStatus()),Toast.LENGTH_SHORT).show();
                        }
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}