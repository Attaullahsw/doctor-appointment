package com.example.patientappointmentproject.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.patientappointmentproject.R;
import com.example.patientappointmentproject.admin.AdminDashBoardActivity;
import com.example.patientappointmentproject.patient.PatientDashBoardActivity;
import com.example.patientappointmentproject.patient.PatientSignUpActivity;
import com.example.patientappointmentproject.util.CustomVolly;
import com.example.patientappointmentproject.util.UtilFunctions;
import com.example.patientappointmentproject.util.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;


public class PaitentLoginFragment extends Fragment {

    TextView txt_patient_login_register;

    EditText edt_patient_login_user_name, edt_patient_login_password;
    CheckBox chb_patient_login_remember;
    TextView txt_patient_login_forgot_password;
    Button btn_patient_login;

    SharedPreferences logPre;
    SharedPreferences.Editor preEditor;



    Context context;


    public PaitentLoginFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_paitent_login, container, false);


        logPre = context.getSharedPreferences("UserLogin", MODE_PRIVATE);
        preEditor = logPre.edit();

        preEditor.remove("temp_patient_login");
        preEditor.remove("patient_user_id");
        preEditor.apply();
        preEditor.commit();


        /********************VIEW INITIALLIZATION****************************/
        txt_patient_login_register = view.findViewById(R.id.txt_patient_login_register);

        edt_patient_login_user_name = view.findViewById(R.id.edt_patient_login_user_name);
        edt_patient_login_password = view.findViewById(R.id.edt_patient_login_password);

        chb_patient_login_remember = view.findViewById(R.id.chb_patient_login_remember);
        txt_patient_login_forgot_password = view.findViewById(R.id.txt_patient_login_forgot_password);

        btn_patient_login = view.findViewById(R.id.btn_patient_login);
        /********************VIEW INITIALLIZATION****************************/

        txt_patient_login_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PatientSignUpActivity.class);
                startActivity(intent);
            }
        });

        rememberLogin();

        /***************************ON BUTTON CLICK**************************/
        btn_patient_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String user_name = edt_patient_login_user_name.getText().toString();
                final String user_pass = edt_patient_login_password.getText().toString();


                if (user_name.equals("")) {
                    Toast.makeText(context, "User Name May Not Be Empty!", Toast.LENGTH_SHORT).show();
                    edt_patient_login_user_name.requestFocus();
                } else if (user_pass.equals("")) {
                    Toast.makeText(context, "Password May Not Be Empty!", Toast.LENGTH_SHORT).show();
                    edt_patient_login_password.requestFocus();
                } else {
                    checkLogin(user_name, user_pass);
                }
            }
        });
        /***************************ON BUTTON CLICK**************************/

        return view;
    }

    public void rememberLogin() {
        String tempUser = logPre.getString("patient_user_name", "");
        String tempPass = logPre.getString("patient_pass", "");
        if (!tempUser.isEmpty() && !tempPass.isEmpty()) {
            checkLogin(tempUser, tempPass);
        }
    }

    public void checkLogin(final String user_name, final String user_pass){
        String url = "patient/checkPatient.php";
        HashMap<String, String> params = new HashMap<>();
        params.put("checkPatient", String.valueOf(true));
        params.put("username", user_name);
        params.put("password", user_pass);
        CustomVolly.getInsertPOSTData(context, params, url, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    boolean error = result.getBoolean("error");
                    if (error) {
                        UtilFunctions.warningAlert(context, "Something went wrong!", "").show();
                    } else {


                        boolean insert = result.getBoolean("check");
                        if (insert) {
                            JSONObject patient_detail_object = result.getJSONObject("patient");
                            if(chb_patient_login_remember.isChecked()){
                                preEditor.putBoolean("patient_login_remenber",true);
                                preEditor.putString("patient_user_name", user_name);
                                preEditor.putString("patient_pass", user_pass);
                                preEditor.apply();
                                preEditor.commit();
                            }

                            preEditor.putBoolean("temp_patient_login",true);
                            preEditor.putInt("patient_user_id", patient_detail_object.getInt("patient_id"));
                            preEditor.apply();
                            preEditor.commit();
                            Intent intent = new Intent(context, PatientDashBoardActivity.class);
                            startActivity(intent);
                        } else {
                            UtilFunctions.warningAlert(context, result.getString("msg"), "if you don't have any account,Kindly create aaccount.").show();
                            preEditor.remove("patient_login_remenber");
                            preEditor.remove("patient_user_name");
                            preEditor.remove("patient_pass");
                            preEditor.apply();
                            preEditor.commit();

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static PaitentLoginFragment newInstance(Context context) {

        PaitentLoginFragment f = new PaitentLoginFragment(context);

        return f;
    }

}
