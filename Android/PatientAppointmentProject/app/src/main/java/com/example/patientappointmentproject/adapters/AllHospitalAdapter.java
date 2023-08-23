package com.example.patientappointmentproject.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.patientappointmentproject.R;
import com.example.patientappointmentproject.admin.AddDepartmentActivity;
import com.example.patientappointmentproject.admin.AddDoctorActivity;
import com.example.patientappointmentproject.admin.AddHospitalActivity;
import com.example.patientappointmentproject.admin.AllDepartmentActivity;
import com.example.patientappointmentproject.models.DepartmentModel;
import com.example.patientappointmentproject.models.HospitalModel;
import com.example.patientappointmentproject.util.CustomVolly;
import com.example.patientappointmentproject.util.UtilFunctions;
import com.example.patientappointmentproject.util.VolleyCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AllHospitalAdapter extends RecyclerView.Adapter<AllHospitalAdapter.ViewHolder>{


    Context context;
    ArrayList<HospitalModel> items;

    boolean check_admin = true;


    public AllHospitalAdapter(Context context, ArrayList<HospitalModel> items){
        this.context = context;
        this.items = items;
    }

    public AllHospitalAdapter(Context context, ArrayList<HospitalModel> items,boolean admin){
        this.context = context;
        this.items = items;
        this.check_admin = admin;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_all_hospital, parent, false);

        return new AllHospitalAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final HospitalModel item = items.get(position);
        holder.txt_hospital_name.setText(item.getHospital_name());
        holder.txt_address.setText(item.getHospital_address());
        holder.txt_contact.setText(item.getHospital_contact_no());
        holder.btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + item.getHospital_contact_no()));
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 101);
                } else {
                    context.startActivity(callIntent);
                }
            }
        });

        Picasso.get().load(context.getResources().getString(R.string.url) + "images/" + item.getHospital_image())
                .placeholder(R.drawable.loading_image)
                .error(R.drawable.loading_image)
                .into(holder.img_hospital);

        if(check_admin) {

            holder.card_hospital.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AllDepartmentActivity.class);
                    intent.putExtra("admin", true);
                    intent.putExtra("hospital_id", item.getHospital_id());
                    context.startActivity(intent);
                }
            });

            holder.btn_hospital_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UtilFunctions.warningChoiceAlert(context, "Are you sure?", "This may affect other files!", "Delete It", "cancel")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    deleteHospital(item.getHospital_id(), position);
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                }
            });

            holder.btn_hospital_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                Intent intent = new Intent(context, AddHospitalActivity.class);
                intent.putExtra("hospital_update",true);
                intent.putExtra("hospital_id",item.getHospital_id());
                intent.putExtra("hospital_name",item.getHospital_name());
                intent.putExtra("hospital_contact",item.getHospital_contact_no());
                intent.putExtra("hospital_address",item.getHospital_address());
                intent.putExtra("hospital_image",item.getHospital_image());

                context.startActivity(intent);
                }
            });
        }else{
            holder.btn_hospital_delete.setVisibility(View.GONE);
            holder.btn_hospital_update.setVisibility(View.GONE);
            holder.card_hospital.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AllDepartmentActivity.class);
                    intent.putExtra("admin", false);
                    intent.putExtra("hospital_id", item.getHospital_id());
                    context.startActivity(intent);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView  txt_hospital_name,txt_contact,txt_address;
        ImageView img_hospital;
        ImageButton btn_hospital_update,btn_hospital_delete,btn_call;
        CardView card_hospital;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_hospital_name =  itemView.findViewById(R.id.txt_hospital_name);
            txt_contact =  itemView.findViewById(R.id.txt_contact);
            txt_address =  itemView.findViewById(R.id.txt_address);

            img_hospital =  itemView.findViewById(R.id.img_hospital);

            card_hospital =  itemView.findViewById(R.id.card_hospital);

            btn_hospital_update =  itemView.findViewById(R.id.btn_hospital_update);
            btn_hospital_delete =  itemView.findViewById(R.id.btn_hospital_delete);
            btn_call =  itemView.findViewById(R.id.btn_call);

        }
    }


    void deleteHospital(final int id,final int p){
        String url = "admin/deleteHospital.php";
        HashMap<String,String> params = new HashMap<>();
        params.put("deleteHospital", String.valueOf(true));
        params.put("hospital_id", String.valueOf(id));
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
