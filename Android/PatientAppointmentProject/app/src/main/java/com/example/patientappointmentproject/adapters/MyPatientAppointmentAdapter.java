package com.example.patientappointmentproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.patientappointmentproject.R;
import com.example.patientappointmentproject.models.AppointmentModel;
import com.example.patientappointmentproject.models.MyAppointmentModel;
import com.example.patientappointmentproject.patient.AppointTimeActivity;
import com.example.patientappointmentproject.util.CustomVolly;
import com.example.patientappointmentproject.util.UtilFunctions;
import com.example.patientappointmentproject.util.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MyPatientAppointmentAdapter extends RecyclerView.Adapter<MyPatientAppointmentAdapter.ViewHolder> {


    Context context;
    ArrayList<MyAppointmentModel> items;

    public MyPatientAppointmentAdapter(Context context, ArrayList<MyAppointmentModel> items) {
        this.context = context;
        this.items = items;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_my_appointment, parent, false);


        return new MyPatientAppointmentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final MyAppointmentModel item = items.get(position);
        holder.txt_appoint_time_date.setText("Your Appointment is on: " + item.getDate());
        holder.txt_patient_time.setText(item.getTime());
        holder.txt_patient_doctor_name.setText(item.getDoctor_name());
        holder.txt_patient_doctor_contact_no.setText(item.getDoctor_contact());
        holder.img_btn_cancel_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilFunctions.warningChoiceAlert(context, "Are you sure?", "Are you sure to cancel Appointment!", "Delete It", "cancel")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                deleteAppointment(item.getAppointment_id(), position);
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_appoint_time_date, txt_patient_time, txt_patient_doctor_name;
        TextView txt_patient_doctor_contact_no, txt_patient_hospital;
        ImageButton img_btn_cancel_appointment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_appoint_time_date = itemView.findViewById(R.id.txt_appoint_time_date);
            txt_patient_time = itemView.findViewById(R.id.txt_patient_time);
            txt_patient_doctor_name = itemView.findViewById(R.id.txt_patient_doctor_name);
            txt_patient_doctor_contact_no = itemView.findViewById(R.id.txt_patient_doctor_contact_no);
            img_btn_cancel_appointment = itemView.findViewById(R.id.img_btn_cancel_appointment);

        }
    }

    void deleteAppointment(final String id, final int p) {
        String url = "patient/deleteAppointment.php";
        HashMap<String, String> params = new HashMap<>();
        params.put("deleteAppointment", String.valueOf(true));
        params.put("appointment_id", id);
        CustomVolly.getInsertPOSTData(context, params, url, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    boolean error = result.getBoolean("error");
                    if (error) {
                        UtilFunctions.warningAlert(context, "Something went wrong!", "").show();
                    } else {

                        boolean insert = result.getBoolean("delete");
                        if (insert) {
                            UtilFunctions.successAlert(context, result.getString("msg"), "").show();
                            items.remove(p);
                            notifyItemRemoved(p);
                            notifyItemRangeChanged(p, items.size());
                            notifyDataSetChanged();
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

}
