package com.example.patientappointmentproject.admin;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.patientappointmentproject.R;
import com.example.patientappointmentproject.models.DepartmentModel;
import com.example.patientappointmentproject.util.CustomVolly;
import com.example.patientappointmentproject.util.Permissions;
import com.example.patientappointmentproject.util.UtilFunctions;
import com.example.patientappointmentproject.util.VolleyCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddDepartmentActivity extends AppCompatActivity {

    EditText edt_department_name;
    Button btn_add_department;
    ImageView img_department_image;
    TextView txt_department_image_name;

    Context context;

    boolean checkUpdate = false;
    int department_ID;
    String old_image_name = "";



    public AddDepartmentActivity(){
        context = AddDepartmentActivity.this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_department);



        /********************VIEW INITIALLIZATION****************************/
        edt_department_name = findViewById(R.id.edt_department_name);
        img_department_image = findViewById(R.id.img_department_image);
        btn_add_department = findViewById(R.id.btn_add_department);
        txt_department_image_name = findViewById(R.id.txt_department_image_name);
        /********************VIEW INITIALLIZATION****************************/

        /**************************UPDATE*****************************************/
        if(getIntent().getBooleanExtra("department_update",false)){
            checkUpdate = true;
            department_ID =  getIntent().getIntExtra("department_id",0);
            edt_department_name.setText(getIntent().getStringExtra("department_name"));
            old_image_name = getIntent().getStringExtra("department_image");
            Picasso.get().load(context.getResources().getString(R.string.url) + "images/" + getIntent().getStringExtra("department_image"))
                    .placeholder(R.drawable.loading_image)
                    .error(R.drawable.loading_image)
                    .into(img_department_image);
            btn_add_department.setText("Update");
        }
        /**************************UPDATE*****************************************/

        /***************************ON ImageView CLICK**************************/
        img_department_image.setOnClickListener(new View.OnClickListener() {
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

        btn_add_department.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edt_department_name.getText().toString();
                final String image_name = txt_department_image_name.getText().toString();

                if(name.equals("")){
                    Toast.makeText(context, "Name May Not Be Empty!", Toast.LENGTH_SHORT).show();
                }else if(image_name.equals("") && !checkUpdate){
                    Toast.makeText(context, "Image May Not Be Empty!", Toast.LENGTH_SHORT).show();
                }else{

                    String url = "admin/insertDepartment.php";
                    HashMap<String,String> params = new HashMap<>();

                    params.put("title",name);
                    if(!image_name.equals("")) {
                        params.put("image_name", image_name.substring(image_name.lastIndexOf("/") + 1));
                        params.put("image", UtilFunctions.convertToBase64(image_name));
                    }else{
                        params.put("image_name", old_image_name);
                        params.put("image","");
                    }
                    if(checkUpdate){
                        params.put("department_id", String.valueOf(department_ID));
                        params.put("old_image_name", old_image_name);
                        params.put("updateDeparment", String.valueOf(true));
                    }else{
                        params.put("insertDeparment", String.valueOf(true));
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
                                                if(checkUpdate){
                                                    finish();
                                                }
                                            }
                                        }).show();
                                        edt_department_name.setText("");
                                        img_department_image.setImageDrawable(null);
                                        txt_department_image_name.setText("");
                                        edt_department_name.requestFocus();
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


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_dash_board, menu);
        return true;
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
                img_department_image.setImageBitmap(bm);
                txt_department_image_name.setText(filePath);

//                if (file_extn.equals("img") || file_extn.equals("jpg") || file_extn.equals("jpeg") || file_extn.equals("gif") || file_extn.equals("png")) {
//
//                } else {
//                    Toast.makeText(RegisterUserActivity.this,"Select The image Only!",Toast.LENGTH_LONG).show();
//                }
            }
    }
    
}
