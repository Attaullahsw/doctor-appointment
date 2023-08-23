package com.example.patientappointmentproject.patient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.patientappointmentproject.R;
import com.example.patientappointmentproject.adapters.AllDepartmentAdapter;
import com.example.patientappointmentproject.adapters.MyPatientAppointmentAdapter;
import com.example.patientappointmentproject.models.MyAppointmentModel;
import com.example.patientappointmentproject.util.CustomVolly;
import com.example.patientappointmentproject.util.UtilFunctions;
import com.example.patientappointmentproject.util.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MyAppoinmentPatientActivity extends AppCompatActivity {

    TextView txt_appoint_time_date,txt_patient_time,txt_patient_doctor_name;
    TextView txt_patient_doctor_contact_no,txt_patient_hospital;

    SharedPreferences logPre;
    SharedPreferences.Editor preEditor;

    MyPatientAppointmentAdapter adapter;
    ArrayList<MyAppointmentModel> items = new ArrayList<>();

    RecyclerView list_all_patient_appointment;

    Context context = MyAppoinmentPatientActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appoinment_patient);

        logPre = getSharedPreferences("UserLogin", MODE_PRIVATE);
        preEditor = logPre.edit();


        list_all_patient_appointment = findViewById(R.id.list_all_patient_appointment);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),1);
        list_all_patient_appointment.setLayoutManager(gridLayoutManager);
        list_all_patient_appointment.setHasFixedSize(true);
        list_all_patient_appointment.setItemViewCacheSize(20);
        list_all_patient_appointment.setDrawingCacheEnabled(true);
        list_all_patient_appointment.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        adapter = new MyPatientAppointmentAdapter(context, items);
        list_all_patient_appointment.setAdapter(adapter);



        String url = "patient/myAppointment.php";
        HashMap<String, String> params = new HashMap<>();
        params.put("patient_id", String.valueOf(logPre.getInt("patient_user_id",0)));
        CustomVolly.getInsertPOSTData(context, params, url, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    boolean error = result.getBoolean("error");

                    if (error) {
                        UtilFunctions.warningAlert(context, "Something went wrong!", "").show();
                    } else {

                        JSONArray doctor = result.getJSONArray("appointment");


                        for(int i=0; i<doctor.length(); i++){
                            JSONObject object = doctor.getJSONObject(i);
                            items.add(new MyAppointmentModel(
                               object.getString("patient_appointment_id"),
                               object.getString("appointment_date"),
                               object.getString("appointment_time"),
                               object.getString("doctor_name"),
                               object.getString("doctor_contact_no")
                            ));
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
