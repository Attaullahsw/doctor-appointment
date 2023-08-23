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
import com.example.patientappointmentproject.admin.AddDoctorActivity;
import com.example.patientappointmentproject.admin.AllDepartmentActivity;
import com.example.patientappointmentproject.models.DepartmentModel;
import com.example.patientappointmentproject.models.DoctorModel;
import com.example.patientappointmentproject.util.CustomVolly;
import com.example.patientappointmentproject.util.UtilFunctions;
import com.example.patientappointmentproject.util.VolleyCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AllDoctorAdapter extends RecyclerView.Adapter<AllDoctorAdapter.ViewHolder>{


    Context context;
    ArrayList<DoctorModel> items;

    public AllDoctorAdapter(Context context, ArrayList<DoctorModel> items){
        this.context = context;
        this.items = items;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_all_doctor, parent, false);

        return new AllDoctorAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final DoctorModel item = items.get(position);

        holder.txt_doctor_name.setText(item.getDoctor_name());
        holder.txt_doctor_contact_no.setText(item.getDoctor_contact_no());
        holder.txt_doctor_address.setText(item.getDoctor_address());
        holder.txt_doctor_gender.setText(item.getDoctor_gender());
        holder.txt_doctor_username.setText(item.getDoctor_username());
        holder.txt_doctor_password.setText(item.getDoctor_password());
        Picasso.get().load(context.getResources().getString(R.string.url) + "images/" + item.getDoctor_image())
                .placeholder(R.drawable.loading_image)
                .error(R.drawable.loading_image)
                .into(holder.img_doctor);
            holder.btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UtilFunctions.warningChoiceAlert(context, "Are you sure?", "This may affect other files!", "Delete It", "cancel")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    deleteDepartmenr(item.getDoctor_id(), position);
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                }
            });

            holder.btn_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AddDoctorActivity.class);
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
        TextView  txt_doctor_gender,txt_doctor_username,txt_doctor_password;

        ImageView img_doctor;

        ImageButton btn_update,btn_delete;
        CardView card_doctor;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_doctor_name =  itemView.findViewById(R.id.txt_doctor_name);
            txt_doctor_contact_no =  itemView.findViewById(R.id.txt_doctor_contact_no);
            txt_doctor_address =  itemView.findViewById(R.id.txt_doctor_address);
            txt_doctor_gender =  itemView.findViewById(R.id.txt_doctor_gender);
            txt_doctor_username =  itemView.findViewById(R.id.txt_doctor_username);
            txt_doctor_password =  itemView.findViewById(R.id.txt_doctor_password);

            img_doctor =  itemView.findViewById(R.id.img_doctor);

            btn_update =  itemView.findViewById(R.id.btn_doctor_update);
            btn_delete =  itemView.findViewById(R.id.btn_doctor_delete);

            card_doctor =  itemView.findViewById(R.id.card_doctor);

        }
    }


    void deleteDepartmenr(final int id,final int p){
        String url = "admin/deleteDoctor.php";
        HashMap<String,String> params = new HashMap<>();
        params.put("deleteDoctor", String.valueOf(true));
        params.put("doctor_id", String.valueOf(id));
        CustomVolly.getInsertPOSTData(context, params, url, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    boolean error = result.getBoolean("error");
                    if(error){
                        UtilFunctions.warningAlert(context,"Something went wrong!","").show();
                    }else{

                        boolean insert = result.getBoolean("delete");
                        if(insert){
                            UtilFunctions.successAlert(context,result.getString("msg"),"").show();
                            items.remove(p);
                            notifyItemRemoved(p);
                            notifyItemRangeChanged(p, items.size());
                            notifyDataSetChanged();
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
