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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.patientappointmentproject.R;
import com.example.patientappointmentproject.util.CustomVolly;
import com.example.patientappointmentproject.util.Permissions;
import com.example.patientappointmentproject.util.UtilFunctions;
import com.example.patientappointmentproject.util.VolleyCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddHospitalActivity extends AppCompatActivity {

    EditText edt_hospital_name,edt_hospital_address,edt_hospital_contact;
    Button btn_add_hospital;
    ImageView img_hospital_image;
    TextView txt_hospital_image_name;

    Context context;

    boolean check_update = false;
    int hospital_id = 0;
    String old_image_name = "";


    public AddHospitalActivity(){
        context = AddHospitalActivity.this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hospital);


        /********************VIEW INITIALLIZATION****************************/


        edt_hospital_name = findViewById(R.id.edt_hospital_name);
        edt_hospital_contact = findViewById(R.id.edt_hospital_contact);
        edt_hospital_address = findViewById(R.id.edt_hospital_address);

        img_hospital_image = findViewById(R.id.img_hospital_image);

        txt_hospital_image_name = findViewById(R.id.txt_hospital_image_name);

        btn_add_hospital = findViewById(R.id.btn_add_hospital);


        /********************VIEW INITIALLIZATION****************************/
        if(getIntent().getBooleanExtra("hospital_update",false)){
            check_update = true;

            hospital_id =  getIntent().getIntExtra("hospital_id",0);
            edt_hospital_name.setText(getIntent().getStringExtra("hospital_name"));
            edt_hospital_contact.setText(getIntent().getStringExtra("hospital_contact"));
            edt_hospital_address.setText(getIntent().getStringExtra("hospital_address"));
            txt_hospital_image_name.setText(getIntent().getStringExtra("hospital_image"));
            old_image_name = getIntent().getStringExtra("hospital_image");
            Picasso.get().load(context.getResources().getString(R.string.url) + "images/" + getIntent().getStringExtra("hospital_image"))
                    .placeholder(R.drawable.loading_image)
                    .error(R.drawable.loading_image)
                    .into(img_hospital_image);
            btn_add_hospital.setText("Update");

        }



        /***************************ON ImageView CLICK**************************/
        img_hospital_image.setOnClickListener(new View.OnClickListener() {
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

        /***************************ON BUTTON CLICK**************************/

        btn_add_hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edt_hospital_name.getText().toString();
                String address = edt_hospital_address.getText().toString();
                String contact = edt_hospital_contact.getText().toString();
                final String image_name = txt_hospital_image_name.getText().toString();

                if(name.equals("")){
                    Toast.makeText(context, "Name May Not Be Empty!", Toast.LENGTH_SHORT).show();
                }else if(address.equals("")){
                    Toast.makeText(context, "Address May Not Be Empty!", Toast.LENGTH_SHORT).show();
                }else if(contact.equals("")){
                    Toast.makeText(context, "Contact May Not Be Empty!", Toast.LENGTH_SHORT).show();
                }else if(image_name.equals("")){
                    Toast.makeText(context, "Image May Not Be Empty!", Toast.LENGTH_SHORT).show();
                }else{
                    if(check_update){
                        updateHospital(name,address,contact,image_name);
                    }else {
                        insertHospital(name,address,contact,image_name);
                    }

                }

            }
        });

        /***************************ON BUTTON CLICK**************************/


    }


    public void insertHospital(String name,String address,String contact,String image_name){
        String url = "admin/insertHospital.php";
        HashMap<String,String> params = new HashMap<>();
        params.put("insertHospital", String.valueOf(true));
        params.put("name",name);
        params.put("address",address);
        params.put("contact",contact);
        params.put("image_name", image_name.substring(image_name.lastIndexOf("/") + 1));
        params.put("image", UtilFunctions.convertToBase64(image_name));

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
                            edt_hospital_name.setText("");
                            img_hospital_image.setImageDrawable(null);
                            edt_hospital_address.setText("");
                            edt_hospital_contact.setText("");
                            txt_hospital_image_name.setText("");
                            edt_hospital_name.requestFocus();
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

    public void updateHospital(String name,String address,String contact,String image_name){
        String url = "admin/updateHospital.php";
        HashMap<String,String> params = new HashMap<>();
        params.put("updateHospital", String.valueOf(true));
        params.put("name",name);
        params.put("hospital_id", String.valueOf(hospital_id));
        params.put("address",address);
        params.put("old_image_name",old_image_name);
        params.put("contact",contact);

        if(!image_name.equals(old_image_name)) {
            params.put("image_name", image_name.substring(image_name.lastIndexOf("/") + 1));
            params.put("image", UtilFunctions.convertToBase64(image_name));
        }else{
            params.put("image_name", old_image_name);
            params.put("image", "");
        }
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
                            UtilFunctions.successAlert(context,result.getString("msg"),"").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    finish();
                                }
                            }).show();

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();

                String filePath = UtilFunctions.getRealPathFromURI(context,selectedImage);
                String file_extn = filePath.substring(filePath.lastIndexOf(".") + 1);
                String file_name = filePath.substring(filePath.lastIndexOf("/") + 1);

                Bitmap bm = BitmapFactory.decodeFile(filePath);
                img_hospital_image.setImageBitmap(bm);
                txt_hospital_image_name.setText(filePath);

//                if (file_extn.equals("img") || file_extn.equals("jpg") || file_extn.equals("jpeg") || file_extn.equals("gif") || file_extn.equals("png")) {
//
//                } else {
//                    Toast.makeText(RegisterUserActivity.this,"Select The image Only!",Toast.LENGTH_LONG).show();
//                }
            }
    }
}
