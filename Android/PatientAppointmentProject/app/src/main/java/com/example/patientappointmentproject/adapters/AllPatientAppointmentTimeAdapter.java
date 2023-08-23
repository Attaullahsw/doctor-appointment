package com.example.patientappointmentproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.patientappointmentproject.R;
import com.example.patientappointmentproject.models.TimeSliceModel;
import com.example.patientappointmentproject.patient.MyAppoinmentPatientActivity;
import com.example.patientappointmentproject.util.CustomVolly;
import com.example.patientappointmentproject.util.UtilFunctions;
import com.example.patientappointmentproject.util.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AllPatientAppointmentTimeAdapter extends RecyclerView.Adapter<AllPatientAppointmentTimeAdapter.ViewHolder>{


    Context context;
    ArrayList<TimeSliceModel> items;
    int appointment_id;
    int patient_id;
    public AllPatientAppointmentTimeAdapter(Context context, ArrayList<TimeSliceModel> items,int appointment_id,int patient_id){
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



        return new AllPatientAppointmentTimeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final TimeSliceModel item = items.get(position);
        holder.txt_slice_count.setText("Appointment Time "+String.valueOf(item.getCount())+":");
        holder.txt_patient_time.setText(item.getTime());

        if(item.getStatus() == 1){
            holder.img_reserved.setVisibility(View.VISIBLE);
        }else {
            holder.lin_appointment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UtilFunctions.warningChoiceAlert(context, "Are you sure?", "Are You Want To Reserved This Time!", "Yes!", "cancel")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    insertPatinetAppointment(item.getTime(),appointment_id,patient_id,position);
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .show();
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

    public  void  insertPatinetAppointment(String time, int a_id, int p_id, final int p){
        String url = "patient/insertPatientAppointment.php";
        HashMap<String,String> params = new HashMap<>();
        params.put("patient_id", String.valueOf(p_id));
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

                        boolean insert = result.getBoolean("insert");
                        if(insert){
                            UtilFunctions.successAlert(context,result.getString("msg"),"").setConfirmClickListener(
                                    new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            Intent intent = new Intent(context, MyAppoinmentPatientActivity.class);
                                            context.startActivity(intent);
                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    }
                            ).show();
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
