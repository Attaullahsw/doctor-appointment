package com.example.patientappointmentproject.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.patientappointmentproject.R;
import com.example.patientappointmentproject.util.CustomVolly;
import com.example.patientappointmentproject.util.Permissions;
import com.example.patientappointmentproject.util.UtilFunctions;
import com.example.patientappointmentproject.util.VolleyCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddDoctorActivity extends AppCompatActivity {

    EditText edt_doctor_name, edt_doctor_username, edt_doctor_password, edt_doctor_address, edt_doctor_contact;
    TextView txt_doctor_image_name;
    ImageView img_doctor_image;
    AutoCompleteTextView auto_doctor_select_hospital, auto_doctor_select_department;
    Button btn_add_doctor;

    RadioGroup radio_gender;
    RadioButton radio_male_female;

    String update_image_name = "";


    int hospital_id[];
    String hospital_name[];

    int department_id[];
    String department_name[];

    int doctor_id = 0;

    Context context = AddDoctorActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor);

        doctor_id = getIntent().getIntExtra("doctor_id", 0);

        /**************************VIEW INITIALLIZATION******************************************/
        edt_doctor_name = findViewById(R.id.edt_doctor_name);
        edt_doctor_username = findViewById(R.id.edt_doctor_username);
        edt_doctor_password = findViewById(R.id.edt_doctor_password);
        edt_doctor_address = findViewById(R.id.edt_doctor_address);
        edt_doctor_contact = findViewById(R.id.edt_doctor_contact);

        txt_doctor_image_name = findViewById(R.id.txt_doctor_image_name);

        auto_doctor_select_hospital = findViewById(R.id.auto_doctor_select_hospital);
        auto_doctor_select_department = findViewById(R.id.auto_doctor_select_department);

        img_doctor_image = findViewById(R.id.img_doctor_image);

        btn_add_doctor = findViewById(R.id.btn_add_doctor);

        radio_gender = findViewById(R.id.radio_gender);
        /**************************VIEW INITIALLIZATION******************************************/


        /***************************ON ImageView CLICK**************************/
        img_doctor_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Permissions.isStoragePermissionGranted(context)) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, 1);
                } else {
                    Toast.makeText(context, "First Grant The Storage Permission!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        /***************************ON ImageView CLICK**************************/

        getHospitalDepartment();

        if (doctor_id != 0) {
            edit();
        }


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


                        for (int i = 0; i < hospitals.length(); i++) {
                            JSONObject h = hospitals.getJSONObject(i);
                            hospital_id[i] = h.getInt("hospital_id");
                            hospital_name[i] = h.getString("hospital_name");
                        }


                        ArrayAdapter hospitalAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, hospital_name);
                        auto_doctor_select_hospital.setThreshold(1);
                        auto_doctor_select_hospital.setAdapter(hospitalAdapter);

                        auto_doctor_select_hospital.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                int h_id=0;
                                String selectedHospital = auto_doctor_select_hospital.getText().toString();
                                for (int c = 0; c < hospital_id.length; c++) {
                                    if (selectedHospital.equals(hospital_name[c])) {
                                        h_id = hospital_id[c];
                                        break;
                                    }
                                }
                                getDeapartment(h_id);

                            }
                        });
                        /***************************ON BUTTON CLICK**************************/
                        btn_add_doctor.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int h_id = 0;
                                String selectedHospital = auto_doctor_select_hospital.getText().toString();
                                for (int i = 0; i < hospital_id.length; i++) {
                                    if (selectedHospital.equals(hospital_name[i])) {
                                        h_id = hospital_id[i];
                                    }
                                }

                                int d_id = 0;
                                String selectedDepartment = auto_doctor_select_department.getText().toString();
                                for (int i = 0; i < department_id.length; i++) {
                                    if (selectedDepartment.equals(department_name[i])) {
                                        d_id = department_id[i];
                                    }
                                }

                                String name = edt_doctor_name.getText().toString();
                                String username = edt_doctor_username.getText().toString();
                                String password = edt_doctor_password.getText().toString();
                                String address = edt_doctor_address.getText().toString();
                                String contact = edt_doctor_contact.getText().toString();
                                final String image_name = txt_doctor_image_name.getText().toString();

                                int selectedId = radio_gender.getCheckedRadioButtonId();
                                radio_male_female = findViewById(selectedId);
                                String male_female = radio_male_female.getText().toString();


                                if (h_id == 0) {
                                    Toast.makeText(context, "Hospital May Not Be Empty!", Toast.LENGTH_SHORT).show();
                                    auto_doctor_select_hospital.requestFocus();
                                } else if (d_id == 0) {
                                    Toast.makeText(context, "Department May Not Be Empty!", Toast.LENGTH_SHORT).show();
                                    auto_doctor_select_department.requestFocus();
                                } else if (name.equals("")) {
                                    Toast.makeText(context, "Name May Not Be Empty!", Toast.LENGTH_SHORT).show();
                                    edt_doctor_username.requestFocus();
                                } else if (username.equals("")) {
                                    Toast.makeText(context, "User Name May Not Be Empty!", Toast.LENGTH_SHORT).show();
                                    edt_doctor_username.requestFocus();
                                } else if (password.equals("")) {
                                    Toast.makeText(context, "Password May Not Be Empty!", Toast.LENGTH_SHORT).show();
                                    edt_doctor_password.requestFocus();
                                } else if (address.equals("")) {
                                    Toast.makeText(context, "Address May Not Be Empty!", Toast.LENGTH_SHORT).show();
                                    edt_doctor_address.requestFocus();
                                } else if (contact.equals("")) {
                                    Toast.makeText(context, "Contact May Not Be Empty!", Toast.LENGTH_SHORT).show();
                                    edt_doctor_contact.requestFocus();
                                } else if (image_name.equals("")) {
                                    Toast.makeText(context, "Image May Not Be Empty!", Toast.LENGTH_SHORT).show();
                                    txt_doctor_image_name.requestFocus();
                                } else {
                                    if (doctor_id != 0) {
                                        update(name, username, password, address, contact, male_female, h_id, d_id, image_name);
                                    }else{
                                        insert(name, username, password, address, contact, male_female, h_id, d_id, image_name);
                                    }


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




    void insert(String name, String username, String password, String address, String contact, String male_female, int h_id,
                int d_id, String image_name) {
        String url = "admin/insertDoctor.php";
        HashMap<String, String> params = new HashMap<>();
        params.put("insertDoctor", String.valueOf(true));
        params.put("name", name);
        params.put("username", username);
        params.put("password", password);
        params.put("address", address);
        params.put("contact", contact);
        params.put("male_female", male_female);
        params.put("hospital_id", String.valueOf(h_id));
        params.put("department_id", String.valueOf(d_id));
        params.put("image_name", image_name.substring(image_name.lastIndexOf("/") + 1));
        params.put("image", UtilFunctions.convertToBase64(image_name));

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
                            UtilFunctions.successAlert(context, result.getString("msg"), "").show();
                            auto_doctor_select_department.setText("");
                            auto_doctor_select_hospital.setText("");
                            edt_doctor_name.setText("");
                            edt_doctor_username.setText("");
                            edt_doctor_password.setText("");
                            edt_doctor_address.setText("");
                            edt_doctor_contact.setText("");
                            txt_doctor_image_name.setText("");
                            img_doctor_image.setImageDrawable(null);
                            auto_doctor_select_hospital.requestFocus();
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

    void update(String name, String username, String password, String address, String contact, String male_female, int h_id,
                int d_id, String image_name) {
        String url = "admin/updateDoctor.php";
        HashMap<String, String> params = new HashMap<>();

        params.put("updateDoctor", String.valueOf(true));
        params.put("doctor_id", String.valueOf(doctor_id));
        params.put("old_image_name", String.valueOf(update_image_name));
        params.put("name", name);
        params.put("username", username);
        params.put("password", password);
        params.put("address", address);
        params.put("contact", contact);
        params.put("male_female", male_female);
        params.put("hospital_id", String.valueOf(h_id));
        params.put("department_id", String.valueOf(d_id));

        if(!image_name.equals(update_image_name)) {
            params.put("image_name", image_name.substring(image_name.lastIndexOf("/") + 1));
            params.put("image", UtilFunctions.convertToBase64(image_name));
        }else{
            params.put("image_name", update_image_name);
            params.put("image", "");
        }

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
                            UtilFunctions.successAlert(context, result.getString("msg"), "").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    finish();
                                }
                            }).show();
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

    void edit() {
        HashMap<String, String> params = new HashMap<>();
        params.put("doctor_id", String.valueOf(doctor_id));
        CustomVolly.getInsertPOSTData(context, params, "doctor/fetchDoctor.php", new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                try {
                    boolean error = result.getBoolean("error");
                    if (error) {
                        UtilFunctions.warningAlert(context, "Something went wrong!", "").show();
                    } else {
                        JSONArray doctor = result.getJSONArray("doctor");


                        update_image_name = doctor.getJSONObject(0).getString("doctor_image");

                        Picasso.get().load(context.getResources().getString(R.string.url) + "images/" +doctor.getJSONObject(0).getString("doctor_image"))
                                .into(img_doctor_image);


                        txt_doctor_image_name.setText(doctor.getJSONObject(0).getString("doctor_image"));
                        edt_doctor_name.setText(doctor.getJSONObject(0).getString("doctor_name"));
                        edt_doctor_contact.setText(doctor.getJSONObject(0).getString("doctor_contact_no"));
                        edt_doctor_address.setText(doctor.getJSONObject(0).getString("doctor_address"));
                        auto_doctor_select_hospital.setText(doctor.getJSONObject(0).getString("hospital_name"));

                        edt_doctor_username.setText(doctor.getJSONObject(0).getString("doctor_username"));
                        edt_doctor_password.setText(doctor.getJSONObject(0).getString("doctor_password"));
                        btn_add_doctor.setText("Update");
                        String selectedHospital = auto_doctor_select_hospital.getText().toString();
                        int h_id=0;
                        for (int i = 0; i < hospital_id.length; i++) {
                            if (selectedHospital.equals(hospital_name[i])) {
                                h_id = hospital_id[i];
                            }
                        }

                        getDeapartment(h_id);

                        auto_doctor_select_department.setText(doctor.getJSONObject(0).getString("department_name"));

                        if (doctor.getJSONObject(0).getString("doctor_gender").equals("Male")) {
                            RadioButton r = findViewById(R.id.radio_male2);
                            r.setChecked(true);
                        } else {
                            RadioButton r = findViewById(R.id.radio_female2);
                            r.setChecked(true);
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    void getDeapartment(int h_id){

        HashMap<String, String> params = new HashMap<>();
        params.put("hospital_id", String.valueOf(h_id));
        CustomVolly.getInsertPOSTData(context, params, "admin/fetchHospitalDepartment.php", new VolleyCallback() {
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

                        for (int i = 0; i < departments.length(); i++) {
                            JSONObject d = departments.getJSONObject(i);
                            department_id[i] = d.getInt("department_id");
                            department_name[i] = d.getString("department_title");
                        }
                        ArrayAdapter departmentAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, department_name);
                        auto_doctor_select_department.setThreshold(1);
                        auto_doctor_select_department.setAdapter(departmentAdapter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();

                String filePath = UtilFunctions.getRealPathFromURI(context, selectedImage);
                String file_extn = filePath.substring(filePath.lastIndexOf(".") + 1);
                String file_name = filePath.substring(filePath.lastIndexOf("/") + 1);

                Bitmap bm = BitmapFactory.decodeFile(filePath);
                img_doctor_image.setImageBitmap(bm);
                txt_doctor_image_name.setText(filePath);

//                if (file_extn.equals("img") || file_extn.equals("jpg") || file_extn.equals("jpeg") || file_extn.equals("gif") || file_extn.equals("png")) {
//
//                } else {
//                    Toast.makeText(RegisterUserActivity.this,"Select The image Only!",Toast.LENGTH_LONG).show();
//                }
            }
    }
}
