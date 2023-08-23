package com.example.patientappointmentproject.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.patientappointmentproject.R;
import com.example.patientappointmentproject.adapters.AllDepartmentAdapter;
import com.example.patientappointmentproject.models.DepartmentModel;
import com.example.patientappointmentproject.util.CustomVolly;
import com.example.patientappointmentproject.util.UtilFunctions;
import com.example.patientappointmentproject.util.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AllDepartmentActivity extends AppCompatActivity {

    AllDepartmentAdapter adapter;
    RecyclerView list_all_department;

    Context context = AllDepartmentActivity.this;

    ArrayList<DepartmentModel> items = new ArrayList<>();

    int hospital_id = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_department);

        list_all_department = findViewById(R.id.list_all_department);


//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
//        list_all_department.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(list_all_department.getContext(),
//                linearLayoutManager.getOrientation());
//        list_all_department.addItemDecoration(dividerItemDecoration);

        hospital_id = getIntent().getIntExtra("hospital_id",0);
        list_all_department.setHasFixedSize(true);
        list_all_department.setItemViewCacheSize(20);
        list_all_department.setDrawingCacheEnabled(true);
        list_all_department.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        if(hospital_id == 0) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),1);
            list_all_department.setLayoutManager(gridLayoutManager);

            adapter = new AllDepartmentAdapter(context, items);
            list_all_department.setAdapter(adapter);

            //getDepartment();
        }else{

            if(getIntent().getBooleanExtra("admin",false)){
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),1);
                list_all_department.setLayoutManager(gridLayoutManager);
                adapter = new AllDepartmentAdapter(context, items);
            }else {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
                list_all_department.setLayoutManager(gridLayoutManager);
                adapter = new AllDepartmentAdapter(context, items, false);
            }
            list_all_department.setAdapter(adapter);
            //getDepartment(hospital_id);
        }


    }

    void getDepartment() {

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
                        JSONArray departments = result.getJSONArray("department");
                        for(int i=0; i<departments.length(); i++){
                            JSONObject d = departments.getJSONObject(i);
                            items.add(new DepartmentModel(d.getInt("department_id"),d.getString("department_title"),d.getString("department_image")));
                        }
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    void getDepartment(int hospital_id) {

        String url = "admin/fetchHospitalDepartment.php";
        HashMap<String, String> params = new HashMap<>();
        params.put("hospital_id", String.valueOf(hospital_id));
        CustomVolly.getInsertPOSTData(context, params, url, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    boolean error = result.getBoolean("error");
                    if (error) {
                        UtilFunctions.warningAlert(context, "Something went wrong!", "").show();
                    } else {
                        JSONArray departments = result.getJSONArray("department");
                        for(int i=0; i<departments.length(); i++){
                            JSONObject d = departments.getJSONObject(i);
                            items.add(new DepartmentModel(d.getInt("department_id"),d.getString("department_title"),d.getString("department_image")
                            ,d.getInt("hospital_assign_id")));
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
        if(hospital_id == 0) {
            getDepartment();
        }else{
            getDepartment(hospital_id);
        }

    }
}
