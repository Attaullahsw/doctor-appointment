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
import com.example.patientappointmentproject.doctor.DoctorDashBoradActivity;
import com.example.patientappointmentproject.util.CustomVolly;
import com.example.patientappointmentproject.util.UtilFunctions;
import com.example.patientappointmentproject.util.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;


public class DoctorLoginFragment extends Fragment {

    EditText edt_doctor_login_user_name,edt_doctor_login_password;
    CheckBox chb_doctor_login_remember;
    TextView txt_doctor_login_forgot_password;
    Button btn_doctor_login;

    Context context;

    SharedPreferences logPre;
    SharedPreferences.Editor preEditor;



    public DoctorLoginFragment(Context context){
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctor_login, container, false);


        logPre = context.getSharedPreferences("UserLogin", MODE_PRIVATE);
        preEditor = logPre.edit();



        preEditor.remove("temp_login");
        preEditor.remove("temp_user_id");
        preEditor.remove("temp_pass");
        preEditor.apply();
        preEditor.commit();



        /********************VIEW INITIALLIZATION****************************/
        edt_doctor_login_user_name = view.findViewById(R.id.edt_doctor_login_user_name);
        edt_doctor_login_password = view.findViewById(R.id.edt_doctor_login_password);

        chb_doctor_login_remember = view.findViewById(R.id.chb_doctor_login_remember);


        btn_doctor_login = view.findViewById(R.id.btn_doctor_login);
        /********************VIEW INITIALLIZATION****************************/

        rememberLogin();

        /***************************ON BUTTON CLICK**************************/
        btn_doctor_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_name = edt_doctor_login_user_name.getText().toString();
                String user_pass = edt_doctor_login_password.getText().toString();

                if(user_name.equals("")){
                    Toast.makeText(context, "User Name May Not Be Empty!", Toast.LENGTH_SHORT).show();
                    edt_doctor_login_user_name.requestFocus();
                }else if(user_pass.equals("")){
                    Toast.makeText(context, "Password May Not Be Empty!", Toast.LENGTH_SHORT).show();
                    edt_doctor_login_password.requestFocus();
                }else {
                    checkLogin(user_name,user_pass);
                }

            }
        });


        /***************************ON BUTTON CLICK**************************/



        return view;
    }

    public void checkLogin(final String user_name, final String user_pass){
        String url = "doctor/checkDoctor.php";
        HashMap<String,String> params = new HashMap<>();
        params.put("checkDoctor", String.valueOf(true));
        params.put("username",user_name);
        params.put("password", user_pass);
        CustomVolly.getInsertPOSTData(context, params, url, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    boolean error = result.getBoolean("error");
                    if(error){
                        UtilFunctions.warningAlert(context,"Something went wrong!","").show();
                    }else{

                        boolean insert = result.getBoolean("check");
                        if(insert){

                            JSONObject doctor_detail_object = result.getJSONObject("doctor");

                            if(chb_doctor_login_remember.isChecked()){
                                preEditor.putBoolean("login_remenber",true);
                                preEditor.putString("user_name", user_name);
                                preEditor.putString("user_pass", user_pass);
                                preEditor.apply();
                                preEditor.commit();
                            }

                            preEditor.putBoolean("temp_login",true);
                            preEditor.putInt("temp_user_id", doctor_detail_object.getInt("doctor_id"));
                            preEditor.putString("temp_hospital_id", doctor_detail_object.getString("hospital_department_id"));
                            preEditor.apply();
                            preEditor.commit();
                            Intent intent = new Intent(context, DoctorDashBoradActivity.class);
                            startActivity(intent);
                        }else{
                            preEditor.remove("user_name");
                            preEditor.remove("user_pass");
                            preEditor.apply();
                            preEditor.commit();

                            UtilFunctions.warningAlert(context,result.getString("msg"),"").show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Remeber user and skip get login data
    public void rememberLogin() {
        String tempUser = logPre.getString("user_name", "");
        String tempPass = logPre.getString("user_pass", "");
        if (!tempUser.isEmpty() && !tempPass.isEmpty()) {
            checkLogin(tempUser, tempPass);
        }
    }

    public static DoctorLoginFragment newInstance(Context context) {

        DoctorLoginFragment f = new DoctorLoginFragment(context);

        return f;
    }
}
