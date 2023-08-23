package com.example.patientappointmentproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.patientappointmentproject.R;
import com.example.patientappointmentproject.models.AppointmentModel;
import com.example.patientappointmentproject.patient.AppointTimeActivity;
import com.example.patientappointmentproject.util.CustomVolly;
import com.example.patientappointmentproject.util.UtilFunctions;
import com.example.patientappointmentproject.util.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AllPatientAppointmentAdapter extends RecyclerView.Adapter<AllPatientAppointmentAdapter.ViewHolder> {


    Context context;
    ArrayList<AppointmentModel> items;

    boolean check_admin = true;

    public AllPatientAppointmentAdapter(Context context, ArrayList<AppointmentModel> items) {
        this.context = context;
        this.items = items;
        check_admin = true;
    }

    public AllPatientAppointmentAdapter(Context context, ArrayList<AppointmentModel> items, boolean admin) {
        this.context = context;
        this.items = items;
        check_admin = admin;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_appointment_patient, parent, false);


        return new AllPatientAppointmentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final AppointmentModel item = items.get(position);
        holder.txt_patient_appointment_date.setText("Appointment Date: " + item.getAppointment_date());
        holder.txt_patient_start_date.setText(item.getAppointment_start_time());
        holder.txt_patient_end_date.setText(item.getAppointment_end_time());


        if(!check_admin) {

            String url = "patient/fetchAppointmentTimeSlice.php";
            HashMap<String, String> params = new HashMap<>();
            params.put("appointment_id", String.valueOf(item.getAppointment_id()));
            params.put("start_time", item.getAppointment_start_time());
            params.put("end_time", item.getAppointment_end_time());
            params.put("slice", String.valueOf(item.getAppointment_per_patient_time()));
            CustomVolly.getInsertPOSTData(context, params, url, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject result) {
                    try {
                        boolean error = result.getBoolean("error");
                        if (error) {
                            UtilFunctions.warningAlert(context, "Something went wrong!", "").show();
                        } else {
                            JSONArray times = result.getJSONArray("appointment");
                            JSONArray timeStatus = result.getJSONArray("timeStatus");
                            boolean c = true;
                            for (int i = 0; i < times.length(); i++) {
                                if (timeStatus.getInt(i) == 0) {
                                    c = false;
                                    break;
                                }
                            }
                            if (c) {
                                holder.txt_reservation.setVisibility(View.VISIBLE);
                                //  Toast.makeText(context,String.valueOf(items.get(p).getStatus()),Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

//
//        if (item.getStatus() == 1) {
//            holder.txt_reservation.setVisibility(View.VISIBLE);
//        }


        holder.lin_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AppointTimeActivity.class);
                if (check_admin) {
                    intent.putExtra("admin", true);
                }
                intent.putExtra("appointment_id", item.getAppointment_id());
                intent.putExtra("start_time", item.getAppointment_start_time());
                intent.putExtra("end_time", item.getAppointment_end_time());
                intent.putExtra("slice", item.getAppointment_per_patient_time());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_patient_appointment_date, txt_patient_start_date, txt_patient_end_date, txt_reservation;
        LinearLayout lin_appointment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_reservation = itemView.findViewById(R.id.txt_reservation);
            txt_patient_appointment_date = itemView.findViewById(R.id.txt_patient_appointment_date);
            txt_patient_start_date = itemView.findViewById(R.id.txt_patient_start_date);
            txt_patient_end_date = itemView.findViewById(R.id.txt_patient_time);
            lin_appointment = itemView.findViewById(R.id.lin_appointment2);

        }
    }


}

