package com.example.patientappointmentproject.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.patientappointmentproject.R;
import com.example.patientappointmentproject.adapters.AllDepartmentAdapter;
import com.example.patientappointmentproject.adapters.AllHospitalAdapter;
import com.example.patientappointmentproject.models.DepartmentModel;
import com.example.patientappointmentproject.models.HospitalModel;
import com.example.patientappointmentproject.util.CustomVolly;
import com.example.patientappointmentproject.util.UtilFunctions;
import com.example.patientappointmentproject.util.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AllHospitalAdminActivity extends AppCompatActivity {

    RecyclerView list_all_hospital;
    ImageView img_empty_list;

    ArrayList<HospitalModel> items = new ArrayList<>();
    AllHospitalAdapter adapter;

    Context context = AllHospitalAdminActivity.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_hospital_admin);

        list_all_hospital = findViewById(R.id.list_all_hospital);
        img_empty_list = findViewById(R.id.img_empty_list);

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
//        list_all_department.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),1);
        list_all_hospital.setLayoutManager(gridLayoutManager);
        list_all_hospital.setHasFixedSize(true);
        list_all_hospital.setItemViewCacheSize(20);
        list_all_hospital.setDrawingCacheEnabled(true);
        list_all_hospital.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(list_all_department.getContext(),
//                linearLayoutManager.getOrientation());
//        list_all_department.addItemDecoration(dividerItemDecoration);
        adapter = new AllHospitalAdapter(context,items);
        list_all_hospital.setAdapter(adapter);


    }

    void getHospital() {

        String url = "admin/fetchHospitalAndDepartment.php";
        HashMap<String, String> params = new HashMap<>();
        CustomVolly.getInsertPOSTData(context, params, url, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    boolean error = result.getBoolean("error");
                    if (error) {
                        UtilFunctions.warningAlert(context, "Something went wrong!", "").show();
                    } else {
                        JSONArray hospitals = result.getJSONArray("hospital");
                        for(int i=0; i<hospitals.length(); i++){
                            JSONObject d = hospitals.getJSONObject(i);
                            items.add(new HospitalModel(d.getInt("hospital_id")
                                    ,d.getString("hospital_name"),d.getString("hospital_address")
                                    ,d.getString("hospital_contact_no"),d.getString("hospital_image")));
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
        getHospital();
    }
}
