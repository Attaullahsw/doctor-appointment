package com.example.patientappointmentproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.patientappointmentproject.R;
import com.example.patientappointmentproject.admin.AddDepartmentActivity;
import com.example.patientappointmentproject.admin.AllDepartmentActivity;
import com.example.patientappointmentproject.models.DoctorModel;
import com.example.patientappointmentproject.patient.PatientAppointmentActivity;
import com.example.patientappointmentproject.util.CustomVolly;
import com.example.patientappointmentproject.util.UtilFunctions;
import com.example.patientappointmentproject.util.VolleyCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AllDoctorPatientAdapter extends RecyclerView.Adapter<AllDoctorPatientAdapter.ViewHolder>{


    Context context;
    ArrayList<DoctorModel> items;


    public AllDoctorPatientAdapter(Context context, ArrayList<DoctorModel> items){
        this.context = context;
        this.items = items;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_all_doctor_patient, parent, false);

        return new AllDoctorPatientAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final DoctorModel item = items.get(position);

        holder.txt_doctor_name.setText(item.getDoctor_name());
        holder.txt_doctor_contact_no.setText(item.getDoctor_contact_no());
        holder.txt_doctor_address.setText(item.getDoctor_address());
        holder.txt_doctor_gender.setText(item.getDoctor_gender());

        Picasso.get().load(context.getResources().getString(R.string.url) + "images/" + item.getDoctor_image())
                .placeholder(R.drawable.loading_image)
                .error(R.drawable.loading_image)
                .into(holder.img_doctor);



            holder.card_doctor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PatientAppointmentActivity.class);
                    intent.putExtra("admin",false);
                    intent.putExtra("doctor_id",item.getDoctor_id());
                    context.startActivity(intent);
                }
            });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView  txt_doctor_name,txt_doctor_contact_no,txt_doctor_address;
        TextView  txt_doctor_gender;
        CardView card_doctor;
        ImageView img_doctor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_doctor_name =  itemView.findViewById(R.id.txt_doctor_name2);
            txt_doctor_contact_no =  itemView.findViewById(R.id.txt_doctor_contact_no2);
            txt_doctor_address =  itemView.findViewById(R.id.txt_doctor_address2);
            txt_doctor_gender =  itemView.findViewById(R.id.txt_doctor_gender2);
            img_doctor =  itemView.findViewById(R.id.img_doctor2);


            card_doctor =  itemView.findViewById(R.id.card_doctor2);

        }
    }




}
