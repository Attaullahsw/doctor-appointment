package com.example.patientappointmentproject.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.patientappointmentproject.R;
import com.example.patientappointmentproject.models.DepartmentModel;
import com.example.patientappointmentproject.models.TimeSliceModel;
import com.example.patientappointmentproject.util.CustomVolly;
import com.example.patientappointmentproject.util.UtilFunctions;
import com.example.patientappointmentproject.util.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AllDoctorAppointmentTimeAdapter extends RecyclerView.Adapter<AllDoctorAppointmentTimeAdapter.ViewHolder>{


    Context context;
    ArrayList<TimeSliceModel> items;
    int appointment_id;
    int patient_id;
    public AllDoctorAppointmentTimeAdapter(Context context, ArrayList<TimeSliceModel> items, int appointment_id, int patient_id){
        this.context = context;
        this.items = items;
        this.appointment_id = appointment_id;
        this.patient_id =  patient_id;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_appointment_time_patient, parent, false);



        return new AllDoctorAppointmentTimeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final TimeSliceModel item = items.get(position);
        holder.txt_slice_count.setText("Appointment Time "+String.valueOf(item.getCount())+":");
        holder.txt_patient_time.setText(item.getTime());

        if(item.getStatus() == 1){
            holder.img_reserved.setVisibility(View.VISIBLE);
            holder.lin_appointment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getPatinetAppointment(item.getTime(),appointment_id,position);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView  txt_slice_count,txt_patient_time;
        ImageView img_reserved;
        LinearLayout lin_appointment;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_slice_count =  itemView.findViewById(R.id.txt_slice_count);
            txt_patient_time =  itemView.findViewById(R.id.txt_patient_time);
            img_reserved =  itemView.findViewById(R.id.img_reserved);
            lin_appointment =  itemView.findViewById(R.id.lin_appointment2);


        }
    }

    public  void  getPatinetAppointment(final String time, final int a_id, final int p){
        String url = "doctor/fetchappointmentPatient.php";
        HashMap<String,String> params = new HashMap<>();
        params.put("AppointmentPatient", String.valueOf(true));
        params.put("appointment_id", String.valueOf(a_id));
        params.put("appointment_time", time);
        CustomVolly.getInsertPOSTData(context, params, url, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    boolean error = result.getBoolean("error");
                    if(error){
                        UtilFunctions.warningAlert(context,"Something went wrong!","").show();
                    }else{

                        JSONArray patient = result.getJSONArray("patient");


                        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View dialogView = inflater.inflate(R.layout.item_all_doctor_patient, null);
                        dialogBuilder.setView(dialogView);

                        TextView txt_doctor_name2 = dialogView.findViewById(R.id.txt_doctor_name2);
                        TextView txt_doctor_contact_no2 = dialogView.findViewById(R.id.txt_doctor_contact_no2);
                        TextView txt_doctor_address2 = dialogView.findViewById(R.id.txt_doctor_address2);
                        TextView txt_doctor_gender2 = dialogView.findViewById(R.id.txt_doctor_gender2);
                        TextView txt_cancel =  dialogView.findViewById(R.id.txt_cancel);
                        txt_cancel.setVisibility(View.VISIBLE);
//
                        txt_doctor_name2.setText(patient.getJSONObject(0).getString("patient_name"));
                        txt_doctor_contact_no2.setText(patient.getJSONObject(0).getString("patient_contact_no"));
                        txt_doctor_address2.setText(patient.getJSONObject(0).getString("patient_address"));
                        txt_doctor_gender2.setText(patient.getJSONObject(0).getString("patient_gender"));
                        final AlertDialog alertDialog = dialogBuilder.create();
                        alertDialog.show();
                        txt_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                UtilFunctions.warningChoiceAlert(context, "Are you sure?", "Are you sure to cancel appointment!", "Delete It", "cancel")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                String url = "doctor/deleteAppointment.php";
                                                HashMap<String, String> params = new HashMap<>();
                                                params.put("deleteAppointment", String.valueOf(true));
                                                params.put("appointment_id", String.valueOf(a_id));
                                                params.put("appointment_time", time);
                                                CustomVolly.getInsertPOSTData(context, params, url, new VolleyCallback() {
                                                    @Override
                                                    public void onSuccess(JSONObject result) {
                                                        try {
                                                            boolean error = result.getBoolean("error");
                                                            if(error){
                                                                UtilFunctions.warningAlert(context,"Something went wrong!","").show();
                                                            }else {

                                                                boolean insert = result.getBoolean("delete");
                                                                if (insert) {
                                                                    UtilFunctions.successAlert(context, result.getString("msg"), "").show();
                                                                    items.get(p).setStatus(0);
                                                                    notifyDataSetChanged();
                                                                    alertDialog.dismiss();
                                                                } else {
                                                                    UtilFunctions.warningAlert(context, result.getString("msg"), "").show();
                                                                }
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                                sweetAlertDialog.dismissWithAnimation();
                                            }
                                        })
                                        .show();


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
