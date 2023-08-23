package com.example.patientappointmentproject.patient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.patientappointmentproject.R;
import com.example.patientappointmentproject.adapters.AllDoctorAppointmentTimeAdapter;
import com.example.patientappointmentproject.adapters.AllDoctorPatientAdapter;
import com.example.patientappointmentproject.adapters.AllPatientAppointmentAdapter;
import com.example.patientappointmentproject.adapters.AllPatientAppointmentTimeAdapter;
import com.example.patientappointmentproject.models.AppointmentModel;
import com.example.patientappointmentproject.models.TimeSliceModel;
import com.example.patientappointmentproject.util.CustomVolly;
import com.example.patientappointmentproject.util.UtilFunctions;
import com.example.patientappointmentproject.util.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AppointTimeActivity extends AppCompatActivity {

    RecyclerView list_all_appointment;
    ImageView img_empty_list;

    ArrayList<TimeSliceModel> items = new ArrayList<>();
    AllPatientAppointmentTimeAdapter adapter1;
    AllDoctorAppointmentTimeAdapter adapter2;

    Context context = AppointTimeActivity.this;


    int appointment_id;

    SharedPreferences logPre;
    SharedPreferences.Editor preEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_appointment);

        logPre = getSharedPreferences("UserLogin", MODE_PRIVATE);
        preEditor = logPre.edit();


        appointment_id = getIntent().getIntExtra("appointment_id", 0);

        if (appointment_id == 0) {
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
            adapter2 = new AllDoctorAppointmentTimeAdapter(context, items, appointment_id, logPre.getInt("patient_user_id", 0));
            list_all_appointment.setAdapter(adapter2);
        } else {
            adapter1 = new AllPatientAppointmentTimeAdapter(context, items, appointment_id, logPre.getInt("patient_user_id", 0));
            list_all_appointment.setAdapter(adapter1);
        }
    }

    void getAppointmentTime(int appointment_id) {

        String url = "patient/fetchAppointmentTimeSlice.php";
        HashMap<String, String> params = new HashMap<>();
        params.put("appointment_id", String.valueOf(appointment_id));
        params.put("start_time", getIntent().getStringExtra("start_time"));
        params.put("end_time", getIntent().getStringExtra("end_time"));
        params.put("slice", String.valueOf(getIntent().getIntExtra("slice", 0)));
        CustomVolly.getInsertPOSTData(context, params, url, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    boolean error = result.getBoolean("error");
                    if (error) {
                        UtilFunctions.warningAlert(context, "Something went wrong!", "").show();
                    } else {
                        JSONArray times = result.getJSONArray("appointment");
                        JSONArray timeStatus = result.getJSONArray("timeStatus");

                        for (int i = 0; i < times.length(); i++) {
                            items.add(new TimeSliceModel(
                                    (i + 1), times.get(i).toString(), timeStatus.getInt(i)
                            ));
                        }


                        if (getIntent().getBooleanExtra("admin", false)) {
                            adapter2.notifyDataSetChanged();
                        } else {
                           // Toast.makeText(context,times.toString(),Toast.LENGTH_LONG).show();
                            adapter1.notifyDataSetChanged();
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        items.clear();
        getAppointmentTime(appointment_id);
    }
}
