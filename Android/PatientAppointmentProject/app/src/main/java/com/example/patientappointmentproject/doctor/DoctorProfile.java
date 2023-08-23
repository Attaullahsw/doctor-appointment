package com.example.patientappointmentproject.doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.patientappointmentproject.R;
import com.example.patientappointmentproject.util.CustomVolly;
import com.example.patientappointmentproject.util.UtilFunctions;
import com.example.patientappointmentproject.util.VolleyCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class DoctorProfile extends AppCompatActivity {

    TextView txt_doctor_name2;
    TextView txt_doctor_contact_no2;
    TextView txt_doctor_address2;
    TextView txt_doctor_gender2;
    ImageView img_doctor;

    Context context = DoctorProfile.this;

    SharedPreferences logPre;
    SharedPreferences.Editor preEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_all_doctor_patient);

        logPre = context.getSharedPreferences("UserLogin", MODE_PRIVATE);
        preEditor = logPre.edit();


        txt_doctor_name2 = findViewById(R.id.txt_doctor_name2);
        txt_doctor_contact_no2 = findViewById(R.id.txt_doctor_contact_no2);
        txt_doctor_address2 = findViewById(R.id.txt_doctor_address2);
        txt_doctor_gender2 = findViewById(R.id.txt_doctor_gender2);
        img_doctor = findViewById(R.id.img_doctor2);

        String url = "doctor/fetchDoctor.php";
        HashMap<String, String> params = new HashMap<>();
        params.put("doctor_id", String.valueOf(logPre.getInt("temp_user_id", 0)));
        CustomVolly.getInsertPOSTData(context, params, url, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    boolean error = result.getBoolean("error");

                    if (error) {
                        UtilFunctions.warningAlert(context, "Something went wrong!", "").show();
                    } else {

                        JSONArray doctor = result.getJSONArray("doctor");


                        txt_doctor_name2.setText(doctor.getJSONObject(0).getString("doctor_name"));
                        txt_doctor_contact_no2.setText(doctor.getJSONObject(0).getString("doctor_contact_no"));
                        txt_doctor_address2.setText(doctor.getJSONObject(0).getString("doctor_address"));
                        txt_doctor_gender2.setText(doctor.getJSONObject(0).getString("doctor_gender"));
                        Picasso.get().load(context.getResources().getString(R.string.url) + "images/" + doctor.getJSONObject(0).getString("doctor_image"))
                                .placeholder(R.drawable.loading_image)
                                .error(R.drawable.loading_image)
                                .into(img_doctor);



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
