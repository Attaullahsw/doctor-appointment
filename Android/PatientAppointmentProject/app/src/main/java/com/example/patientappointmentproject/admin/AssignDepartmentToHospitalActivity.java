package com.example.patientappointmentproject.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.patientappointmentproject.R;
import com.example.patientappointmentproject.util.CustomVolly;
import com.example.patientappointmentproject.util.UtilFunctions;
import com.example.patientappointmentproject.util.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.HashMap;

public class AssignDepartmentToHospitalActivity extends AppCompatActivity {

    AutoCompleteTextView auto_Hospital_name, auto_department_name;
    Button btn_add_department_to_hospital;

    int hospital_id[];
    String hospital_name[];

    int department_id[];
    String department_name[];

    Context context;

    public AssignDepartmentToHospitalActivity() {
        context = AssignDepartmentToHospitalActivity.this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_department_to_hospital);


        /********************VIEW INITIALLIZATION****************************/
        auto_Hospital_name = findViewById(R.id.auto_Hospital_name);
        auto_department_name = findViewById(R.id.auto_department_name);
        btn_add_department_to_hospital = findViewById(R.id.btn_add_department_to_hospital);
        /********************VIEW INITIALLIZATION****************************/

        getHospitalDepartment();


    }


    void getHospitalDepartment() {

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
                        final JSONArray hospitals = result.getJSONArray("hospital");
                        hospital_id = new int[hospitals.length()];
                        hospital_name = new String[hospitals.length()];

                        JSONArray departments = result.getJSONArray("department");
                        department_id = new int[departments.length()];
                        department_name = new String[departments.length()];

                        for(int i=0; i<hospitals.length(); i++){
                            JSONObject h = hospitals.getJSONObject(i);
                            hospital_id[i] = h.getInt("hospital_id");
                            hospital_name[i] = h.getString("hospital_name");
                        }

                        for(int i=0; i<departments.length(); i++){
                            JSONObject d = departments.getJSONObject(i);
                            department_id[i] = d.getInt("department_id");
                            department_name[i] = d.getString("department_title");
                        }

                        ArrayAdapter hospitalAdapter = new ArrayAdapter(context,android.R.layout.simple_spinner_dropdown_item,hospital_name);
//                        ArrayAdapter departmentAdapter = new ArrayAdapter(context,android.R.layout.simple_spinner_dropdown_item,department_name);
                        auto_Hospital_name.setThreshold(1);
//                        auto_department_name.setThreshold(1);
//                        auto_department_name.setAdapter(departmentAdapter);
                        auto_Hospital_name.setAdapter(hospitalAdapter);

                        auto_Hospital_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                int h_id = 0;
                                String selectedHospital = auto_Hospital_name.getText().toString();
                                for (int count = 0; count < hospital_id.length; count++) {
                                    if (selectedHospital.equals(hospital_name[count])) {
                                        h_id = hospital_id[count];
                                        break;
                                    }
                                }
                                getDepartment(h_id);
                            }
                        });

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    void getDepartment(int temp_hospital_id) {


        String url = "admin/fetchRemaningDepartment.php";
        HashMap<String, String> params = new HashMap<>();
        params.put("hospital_id", String.valueOf(temp_hospital_id));
        CustomVolly.getInsertPOSTData(context, params, url, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    boolean error = result.getBoolean("error");
                    if (error) {
                        UtilFunctions.warningAlert(context, "Something went wrong!", "").show();
                    } else {

                        JSONArray departments = result.getJSONArray("department");
                        department_id = new int[departments.length()];
                        department_name = new String[departments.length()];


                        for(int i=0; i<departments.length(); i++){
                            JSONObject d = departments.getJSONObject(i);
                            department_id[i] = d.getInt("department_id");
                            department_name[i] = d.getString("department_title");
                        }


                        ArrayAdapter departmentAdapter = new ArrayAdapter(context,android.R.layout.simple_spinner_dropdown_item,department_name);
                        auto_department_name.setThreshold(1);
                        auto_department_name.setAdapter(departmentAdapter);

                        /***************************ON BUTTON CLICK**************************/
                        btn_add_department_to_hospital.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int h_id = 0;
                                String selectedHospital = auto_Hospital_name.getText().toString();
                                for (int i = 0; i < hospital_id.length; i++) {
                                    if (selectedHospital.equals(hospital_name[i])) {
                                        h_id = hospital_id[i];
                                    }
                                }

                                int d_id = 0;
                                String selectedDepartment = auto_department_name.getText().toString();
                                for (int i = 0; i < department_id.length; i++) {
                                    if (selectedDepartment.equals(department_name[i])) {
                                        d_id = department_id[i];
                                    }
                                }

                                if(h_id==0){
                                    Toast.makeText(context, "Hospital May Not Be Empty!", Toast.LENGTH_SHORT).show();
                                    auto_Hospital_name.requestFocus();
                                }else if(d_id==0){
                                    Toast.makeText(context, "Department May Not Be Empty!", Toast.LENGTH_SHORT).show();
                                    auto_department_name.requestFocus();
                                }else{
                                    insertAssignDepartment(h_id,d_id);
                                }

                            }
                        });
                        /***************************ON BUTTON CLICK**************************/

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }



    void insertAssignDepartment(int h_id,int d_id){
        String url = "admin/insertDepartmentAssignToHospital.php";
        HashMap<String,String> params = new HashMap<>();
        params.put("insertAssignDeparment", String.valueOf(true));
        params.put("hospital_id", String.valueOf(h_id));
        params.put("department_id", String.valueOf(d_id));

        CustomVolly.getInsertPOSTData(context, params, url, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {

                    boolean error = result.getBoolean("error");
                    if(error){
                        UtilFunctions.warningAlert(context,"Something went wrong!","").show();
                    }else{

                        boolean insert = result.getBoolean("insert");
                        if(insert){
                            UtilFunctions.successAlert(context,result.getString("msg"),"").show();
                            auto_department_name.setText("");
                            auto_Hospital_name.setText("");
                            auto_Hospital_name.requestFocus();
                        }else{
                            UtilFunctions.warningAlert(context,result.getString("msg"),"").show();
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }

}
