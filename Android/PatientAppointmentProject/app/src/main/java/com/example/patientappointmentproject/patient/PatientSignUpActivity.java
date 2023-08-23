package com.example.patientappointmentproject.patient;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.patientappointmentproject.R;
import com.example.patientappointmentproject.util.CustomVolly;
import com.example.patientappointmentproject.util.UtilFunctions;
import com.example.patientappointmentproject.util.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PatientSignUpActivity extends AppCompatActivity {


    EditText edt_patient_name, edt_patient_username, edt_patient_password, edt_patient_address, edt_patient_contact;
    TextView txt_sign_up;
    RadioGroup radio_gender;
    RadioButton radio_male_female;

    Button btn_add_patient;

    SharedPreferences logPre;
    SharedPreferences.Editor preEditor;



    Context context = PatientSignUpActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_sign_up);

        logPre = getSharedPreferences("UserLogin", MODE_PRIVATE);
        preEditor = logPre.edit();

        /**************************VIEW INITIALLIZATION******************************************/
        txt_sign_up = findViewById(R.id.txt_sign_up);
        edt_patient_name = findViewById(R.id.edt_patient_name);
        edt_patient_username = findViewById(R.id.edt_patient_username);
        edt_patient_password = findViewById(R.id.edt_patient_password);
        edt_patient_address = findViewById(R.id.edt_patient_address);
        edt_patient_contact = findViewById(R.id.edt_patient_contact);


        radio_gender = findViewById(R.id.radio_gender);

        btn_add_patient = findViewById(R.id.btn_add_patient);
        /**************************VIEW INITIALLIZATION******************************************/


        if (getIntent().getBooleanExtra("update", false)) {
            txt_sign_up.setText("Update Profile");
            edt_patient_name.setText(getIntent().getStringExtra("patient_name"));
            edt_patient_username.setText(getIntent().getStringExtra("patient_username"));
            edt_patient_password.setText(getIntent().getStringExtra("patient_password"));
            edt_patient_address.setText(getIntent().getStringExtra("patient_address"));
            edt_patient_contact.setText(getIntent().getStringExtra("patient_contact_no"));
            if (getIntent().getStringExtra("patient_gender").equals("Male")) {
                RadioButton r = findViewById(R.id.radio_male);
                r.setChecked(true);
            } else {
                RadioButton r = findViewById(R.id.radio_female);
                r.setChecked(true);
            }


            btn_add_patient.setText("Update");
        }

        edt_patient_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                   checkusername(edt_patient_username.getText().toString());

                }
            }
        });


        /***************************ON BUTTON CLICK**************************/
        btn_add_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = edt_patient_name.getText().toString();
                String username = edt_patient_username.getText().toString();
                String password = edt_patient_password.getText().toString();
                String address = edt_patient_address.getText().toString();
                String contact = edt_patient_contact.getText().toString();

                int selectedId = radio_gender.getCheckedRadioButtonId();
                radio_male_female = findViewById(selectedId);
                String male_female = radio_male_female.getText().toString();


                if (name.equals("")) {
                    Toast.makeText(context, "Name May Not Be Empty!", Toast.LENGTH_SHORT).show();
                    edt_patient_name.requestFocus();
                } else if (username.equals("")  ||  username.length() < 5) {
                    if(username.equals("")){
                        Toast.makeText(context, "User Name May Not Be Empty!", Toast.LENGTH_SHORT).show();
                    }else{
                        edt_patient_username.setError("User Name must be at least 5 characters!");
                    }
                    edt_patient_username.requestFocus();
                } else if (password.equals("")  ||  password.length() < 5) {
                    if(password.equals("")) {
                        Toast.makeText(context, "Password May Not Be Empty!", Toast.LENGTH_SHORT).show();
                    }else{
                        edt_patient_password.setError("Password Must be at least 5 characters!");
                    }
                    edt_patient_password.requestFocus();
                }else if (address.equals("")) {
                    Toast.makeText(context, "Address May Not Be Empty!", Toast.LENGTH_SHORT).show();
                    edt_patient_address.requestFocus();
                } else if (contact.equals("") || contact.length() < 10) {
                        if(contact.equals("")){
                            Toast.makeText(context, "Contact May Not Be Empty!", Toast.LENGTH_SHORT).show();
                            edt_patient_contact.requestFocus();
                        }else{
                            edt_patient_contact.requestFocus();
                            edt_patient_contact.setError("Contact must be at least 10 characters!");
                        }

                } else {


                    if (getIntent().getBooleanExtra("update", false)) {
                        confirmPassword(name,password,username,address,contact,male_female);
                    }else{
                        insert(name,password,username,address,contact,male_female);
                    }

                }

            }
        });
        /***************************ON BUTTON CLICK**************************/


    }


    public void confirmPassword(final String name, final String password, final String username, final String address, final String contact, final String male_female){
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.confirm_update_profile_dialog, null);
        dialogBuilder.setView(dialogView);

        TextView btn_confirm_password_cancel = dialogView.findViewById(R.id.btn_confirm_password_cancel);
        final EditText edt_confirm_password = dialogView.findViewById(R.id.edt_confirm_password);

        Button btn_confirm_update = dialogView.findViewById(R.id.btn_confirm_update);
        btn_confirm_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = edt_confirm_password.getText().toString();
                if(pass.equals("")){
                    Toast.makeText(context, "Passeord May Not Be Empty!", Toast.LENGTH_SHORT).show();
                    edt_confirm_password.requestFocus();
                }else if(pass.equals(getIntent().getStringExtra("patient_password"))){
                    update(name,password,username,address,contact,male_female);
                }else{
                    UtilFunctions.warningAlert(context,"Warning!","Password Does not Match!").show();
                }
            }
        });

        final AlertDialog alertDialog = dialogBuilder.create();
        edt_confirm_password.requestFocus();
        alertDialog.show();
        btn_confirm_password_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    void insert(String name,String password,String username,String address,String contact,String male_female){

        String url = "patient/insertPatient.php";
        HashMap<String, String> params = new HashMap<>();
        params.put("insertPatient", String.valueOf(true));
        params.put("name", name);
        params.put("username", username);
        params.put("password", password);
        params.put("address", address);
        params.put("contact", contact);
        params.put("male_female", male_female);

        CustomVolly.getInsertPOSTData(context, params, url, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    boolean error = result.getBoolean("error");
                    if (error) {
                        UtilFunctions.warningAlert(context, "Something went wrong!", "").show();
                    } else {

                        boolean insert = result.getBoolean("insert");
                        if (insert) {
                            UtilFunctions.successAlert(context, result.getString("msg"), "")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                            finish();
                                        }
                                    })
                                    .show();

                        } else {
                            UtilFunctions.warningAlert(context, result.getString("msg"), "").show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void update(String name,String password,String username,String address,String contact,String male_female){

        String url = "patient/updatePatient.php";
        HashMap<String, String> params = new HashMap<>();
        params.put("updatePatient", String.valueOf(true));
        params.put("patient_id", String.valueOf(logPre.getInt("patient_user_id", 0)));
        params.put("name", name);
        params.put("username", username);
        params.put("password", password);
        params.put("address", address);
        params.put("contact", contact);
        params.put("male_female", male_female);

        CustomVolly.getInsertPOSTData(context, params, url, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    boolean error = result.getBoolean("error");
                    if (error) {
                        UtilFunctions.warningAlert(context, "Something went wrong!", "").show();
                    } else {

                        boolean insert = result.getBoolean("update");
                        if (insert) {
                            UtilFunctions.successAlert(context, result.getString("msg"), "")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                            finish();
                                        }
                                    })
                                    .show();

                        } else {
                            UtilFunctions.warningAlert(context, result.getString("msg"), "").show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void checkusername(String username){

        String url = "patient/checkusername.php";
        HashMap<String, String> params = new HashMap<>();
        params.put("username", username);
        CustomVolly.getInsertPOSTData(context, params, url, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    boolean error = result.getBoolean("error");
                    if (error) {
                        UtilFunctions.warningAlert(context, "Something went wrong!", "").show();
                    } else {

                        boolean check = result.getBoolean("check");
                        if (check) {
                          edt_patient_username.setError("User name already Taken!");
                            edt_patient_username.requestFocus();

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
