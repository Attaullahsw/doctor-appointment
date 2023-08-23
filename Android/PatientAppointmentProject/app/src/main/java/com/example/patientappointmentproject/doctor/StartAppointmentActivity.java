package com.example.patientappointmentproject.doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.patientappointmentproject.R;
import com.example.patientappointmentproject.util.CustomVolly;
import com.example.patientappointmentproject.util.UtilFunctions;
import com.example.patientappointmentproject.util.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class StartAppointmentActivity extends AppCompatActivity {

    Button  btn_start_time,btn_date,btn_end_time,btn_add_appointment;
    EditText edt_time_slice;

    Calendar myCalendar;

    Context context = StartAppointmentActivity.this;

    SharedPreferences logPre;
    SharedPreferences.Editor preEditor;

    String doctor_id;
    String hospital_department_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_appointment);



        myCalendar = Calendar.getInstance();
        logPre = context.getSharedPreferences("UserLogin", MODE_PRIVATE);
        preEditor = logPre.edit();

        doctor_id = String.valueOf(logPre.getInt("temp_user_id", 0));
        hospital_department_id = logPre.getString("temp_hospital_id", "");

        if(doctor_id.equals("0") || hospital_department_id.equals("")){
            Toast.makeText(context, "Some thing went wrong!", Toast.LENGTH_SHORT).show();
        }


        /**************************VIEW INITALLIZATIN***********************************/
        btn_start_time = findViewById(R.id.btn_start_time);
        btn_date = findViewById(R.id.btn_date);
        btn_end_time = findViewById(R.id.btn_end_time);
        btn_add_appointment = findViewById(R.id.btn_add_appointment);


        edt_time_slice = findViewById(R.id.edt_time_slice);
        /**************************VIEW INITALLIZATIN***********************************/

        /**************************On Date Selection***********************************/
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        /**************************On Date Selection***********************************/

        /**************************On Date Button CLik***********************************/
        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        /**************************On Date Button CLik***********************************/


        /**************************On start time Button CLik***********************************/
        btn_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if(selectedMinute==0){
                            btn_start_time.setText( selectedHour + ":0" + selectedMinute);
                        }else{
                            btn_start_time.setText( selectedHour + ":" + selectedMinute);
                        }
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
        /**************************On start time Button CLik***********************************/
        /**************************On end time Button CLik***********************************/
        btn_end_time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if(selectedMinute==0){
                            btn_end_time.setText( selectedHour + ":0" + selectedMinute);
                        }else{
                            btn_end_time.setText( selectedHour + ":" + selectedMinute);
                        }

                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
        /**************************On end time Button CLik***********************************/


        btn_add_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = btn_date.getText().toString();
                String start_time = btn_start_time.getText().toString();
                String end_time = btn_end_time.getText().toString();
                String patient_time_slice = edt_time_slice.getText().toString();

                if(date.equals("")){
                    Toast.makeText(context, "Date May Not Be Empty!", Toast.LENGTH_SHORT).show();
                }else if(start_time.equals("")){
                    Toast.makeText(context, "Start Time May Not Be Empty!", Toast.LENGTH_SHORT).show();
                }else if(end_time.equals("")){
                    Toast.makeText(context, "End Time May Not Be Empty!", Toast.LENGTH_SHORT).show();
                }else if(patient_time_slice.equals("")){
                    Toast.makeText(context, "Patient Time Slice May Not Be Empty!", Toast.LENGTH_SHORT).show();
                }else{
                    String url = "doctor/insertAppointment.php";
                    HashMap<String,String> params = new HashMap<>();
                    params.put("insertAppointment", String.valueOf(true));
                    params.put("date",date);
                    params.put("start_time",start_time);
                    params.put("end_time",end_time);
                    params.put("time_slice",patient_time_slice);
                    params.put("doctor_id",doctor_id);
                    params.put("hospital_department_id",hospital_department_id);

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
                                        btn_date.setText("");
                                        btn_start_time.setText("");
                                        btn_end_time.setText("");
                                        edt_time_slice.setText("");
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




    }

    /**
     * set Date to Edit Text
     */
    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        btn_date.setText(sdf.format(myCalendar.getTime()));
    }

}
