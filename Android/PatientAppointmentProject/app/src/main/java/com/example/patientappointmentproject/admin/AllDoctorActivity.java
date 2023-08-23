package com.example.patientappointmentproject.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.patientappointmentproject.R;
import com.example.patientappointmentproject.adapters.AllDoctorAdapter;
import com.example.patientappointmentproject.adapters.AllHospitalAdapter;
import com.example.patientappointmentproject.models.DoctorModel;
import com.example.patientappointmentproject.models.HospitalModel;
import com.example.patientappointmentproject.util.CustomVolly;
import com.example.patientappointmentproject.util.UtilFunctions;
import com.example.patientappointmentproject.util.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AllDoctorActivity extends AppCompatActivity {

    RecyclerView list_all_doctor;
    ImageView img_empty_list;

    ArrayList<DoctorModel> items = new ArrayList<>();
    AllDoctorAdapter adapter;

    Context context = AllDoctorActivity.this;

    int hospital_assign_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_doctor);


        hospital_assign_id = getIntent().getIntExtra("hospital_assign_id",0);



        list_all_doctor = findViewById(R.id.list_all_doctor);
        img_empty_list = findViewById(R.id.img_empty_list2);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),1);
        list_all_doctor.setLayoutManager(gridLayoutManager);
        list_all_doctor.setHasFixedSize(true);
        list_all_doctor.setItemViewCacheSize(20);
        list_all_doctor.setDrawingCacheEnabled(true);
        list_all_doctor.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        adapter = new AllDoctorAdapter(context,items);
        list_all_doctor.setAdapter(adapter);



    }

    void getDoctor() {

        String url = "admin/fetchDoctor.php";
        HashMap<String, String> params = new HashMap<>();
        params.put("doctors", String.valueOf(true));
        if(hospital_assign_id != 0){
            params.put("hospital_department_id", String.valueOf(hospital_assign_id));

        }

        CustomVolly.getInsertPOSTData(context, params, url, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    boolean error = result.getBoolean("error");
                    if (error) {
                        UtilFunctions.warningAlert(context, "Something went wrong!", "").show();
                    } else {
                        JSONArray doctor = result.getJSONArray("doctor");
                        for(int i=0; i<doctor.length(); i++){
                            JSONObject d = doctor.getJSONObject(i);
                            items.add(new DoctorModel(
                                    d.getInt("doctor_id"),
                                    d.getString("doctor_name"),
                                    d.getString("doctor_address"),
                                    d.getString("doctor_image"),
                                    d.getString("doctor_gender"),
                                    d.getString("doctor_contact_no"),
                                    d.getString("doctor_username"),
                                    d.getString("doctor_password"),
                                    d.getString("hospital_department_id")

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

    @Override
    protected void onPostResume() {
        super.onPostResume();
        items.clear();
        getDoctor();

    }
}
