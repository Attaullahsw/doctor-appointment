package com.example.patientappointmentproject.fragments;

import android.content.Context;
import android.content.Intent;
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

import com.example.patientappointmentproject.LoginActivity;
import com.example.patientappointmentproject.R;
import com.example.patientappointmentproject.admin.AdminDashBoardActivity;
import com.example.patientappointmentproject.util.CustomVolly;
import com.example.patientappointmentproject.util.UtilFunctions;
import com.example.patientappointmentproject.util.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class AdminLoginFragment extends Fragment {

    EditText edt_admin_login_user_name,edt_admin_login_password;
    CheckBox chb_admin_login_remember;
    TextView txt_admin_login_forgot_password;
    Button btn_admin_login;

    Context context;


    public AdminLoginFragment(Context context){
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_admin_login, container, false);


        /********************VIEW INITIALLIZATION****************************/
        edt_admin_login_user_name = view.findViewById(R.id.edt_admin_login_user_name);
        edt_admin_login_password = view.findViewById(R.id.edt_admin_login_password);



        btn_admin_login = view.findViewById(R.id.btn_admin_login);
        /********************VIEW INITIALLIZATION****************************/

        /***************************ON BUTTON CLICK**************************/
        btn_admin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_name = edt_admin_login_user_name.getText().toString();
                String user_pass = edt_admin_login_password.getText().toString();

                if(user_name.equals("")){
                    Toast.makeText(context, "User Name May Not Be Empty!", Toast.LENGTH_SHORT).show();
                    edt_admin_login_user_name.requestFocus();
                }else if(user_pass.equals("")){
                    Toast.makeText(context, "Password May Not Be Empty!", Toast.LENGTH_SHORT).show();
                    edt_admin_login_password.requestFocus();
                }else {
                    String url = "admin/checkAdmin.php";
                    HashMap<String,String> params = new HashMap<>();
                    params.put("checkAdmin", String.valueOf(true));
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
                                        Intent intent = new Intent(context, AdminDashBoardActivity.class);
                                        startActivity(intent);
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
        });


        /***************************ON BUTTON CLICK**************************/



        return view;
    }

    public static AdminLoginFragment newInstance(Context context) {

        AdminLoginFragment f = new AdminLoginFragment(context);

        return f;
    }
}
