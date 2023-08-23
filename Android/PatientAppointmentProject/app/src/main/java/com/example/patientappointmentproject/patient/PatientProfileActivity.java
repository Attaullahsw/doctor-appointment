package com.example.patientappointmentproject.patient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.patientappointmentproject.R;
import com.example.patientappointmentproject.util.CustomVolly;
import com.example.patientappointmentproject.util.UtilFunctions;
import com.example.patientappointmentproject.util.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class PatientProfileActivity extends AppCompatActivity {

    TextView txt_patient_name, txt_patient_contact_no, txt_patient_gender;
    TextView txt_patient_username, txt_patient_address;
    ImageButton btn_patient_update;

    SharedPreferences logPre;
    SharedPreferences.Editor preEditor;

    Context context = PatientProfileActivity.this;

    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);

        txt_patient_name = findViewById(R.id.txt_patient_name);
        txt_patient_contact_no = findViewById(R.id.txt_patient_contact_no);
        txt_patient_gender = findViewById(R.id.txt_patient_gender);
        txt_patient_username = findViewById(R.id.txt_patient_username);
        txt_patient_address = findViewById(R.id.txt_patient_address);

        btn_patient_update = findViewById(R.id.btn_patient_update);

        logPre = getSharedPreferences("UserLogin", MODE_PRIVATE);
        preEditor = logPre.edit();


        String url = "patient/fetchPatient.php";
        HashMap<String, String> params = new HashMap<>();
        params.put("patient_id", String.valueOf(logPre.getInt("patient_user_id", 0)));
        CustomVolly.getInsertPOSTData(context, params, url, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    boolean error = result.getBoolean("error");

                    if (error) {
                        UtilFunctions.warningAlert(context, "Something went wrong!", "").show();
                    } else {

                        JSONArray patient = result.getJSONArray("patient");


                        password = patient.getJSONObject(0).getString("patient_password");
                        txt_patient_name.setText(patient.getJSONObject(0).getString("patient_name"));
                        txt_patient_contact_no.setText(patient.getJSONObject(0).getString("patient_contact_no"));
                        txt_patient_gender.setText(patient.getJSONObject(0).getString("patient_gender"));
                        txt_patient_username.setText(patient.getJSONObject(0).getString("patient_username"));
                        txt_patient_address.setText(patient.getJSONObject(0).getString("patient_address"));

                        btn_patient_update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(context, PatientSignUpActivity.class);
                                intent.putExtra("update",true);
                                intent.putExtra("patient_name",txt_patient_name.getText().toString());
                                intent.putExtra("patient_contact_no",txt_patient_contact_no.getText().toString());
                                intent.putExtra("patient_gender",txt_patient_gender.getText().toString());
                                intent.putExtra("patient_username",txt_patient_username.getText().toString());
                                intent.putExtra("patient_address",txt_patient_address.getText().toString());
                                intent.putExtra("patient_password",password);
                                context.startActivity(intent);
                            }
                        });


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
