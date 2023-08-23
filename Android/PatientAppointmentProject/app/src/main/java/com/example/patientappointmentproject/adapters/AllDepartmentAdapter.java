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
import com.example.patientappointmentproject.admin.AllDoctorActivity;
import com.example.patientappointmentproject.models.DepartmentModel;
import com.example.patientappointmentproject.patient.AllDoctorPatientActivity;
import com.example.patientappointmentproject.util.CustomVolly;
import com.example.patientappointmentproject.util.UtilFunctions;
import com.example.patientappointmentproject.util.VolleyCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AllDepartmentAdapter extends RecyclerView.Adapter<AllDepartmentAdapter.ViewHolder>{


    Context context;
    ArrayList<DepartmentModel> items;

    boolean check_admin = true;


    public AllDepartmentAdapter(Context context, ArrayList<DepartmentModel> items){
        this.context = context;
        this.items = items;
    }

    public AllDepartmentAdapter(Context context, ArrayList<DepartmentModel> items,boolean check_admin){
        this.context = context;
        this.items = items;
        this.check_admin = check_admin;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_all_department2, parent, false);



        return new AllDepartmentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final DepartmentModel item = items.get(position);
        holder.txt_name.setText(item.getDeparment_name());

        Picasso.get().load(context.getResources().getString(R.string.url) + "images/" + item.getDepartment_image())
                .placeholder(R.drawable.loading_image)
                .error(R.drawable.loading_image)
                .into(holder.img_department);

        if(check_admin) {

            holder.btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UtilFunctions.warningChoiceAlert(context, "Are you sure?", "This may affect other files!", "Delete It", "cancel")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    deleteDepartmenr(item.getDepartment_id(), position);
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                }
            });

            holder.btn_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AddDepartmentActivity.class);
                    intent.putExtra("department_update", true);
                    intent.putExtra("department_id", item.getDepartment_id());
                    intent.putExtra("department_name", item.getDeparment_name());
                    intent.putExtra("department_image", item.getDepartment_image());
                    context.startActivity(intent);
                }
            });

            holder.card_department.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AllDoctorActivity.class);
                    intent.putExtra("hospital_assign_id",item.getHospital_assign_id());
                    context.startActivity(intent);
                }
            });

        }else{
            holder.btn_update.setVisibility(View.GONE);
            holder.btn_delete.setVisibility(View.GONE);
            holder.card_department.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AllDoctorPatientActivity.class);
                    intent.putExtra("hospital_assign_id",item.getHospital_assign_id());
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
        TextView  txt_name;
        ImageView img_department;
        ImageButton btn_update,btn_delete;
        CardView card_department;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_name =  itemView.findViewById(R.id.txt_name);
            img_department =  itemView.findViewById(R.id.img_department);

            btn_update =  itemView.findViewById(R.id.btn_update);
            btn_delete =  itemView.findViewById(R.id.btn_delete);

            card_department =  itemView.findViewById(R.id.card_department);

        }
    }


    void deleteDepartmenr(final int id,final int p){
        String url = "admin/deleteDepartment.php";
        HashMap<String,String> params = new HashMap<>();
        params.put("deleteDeparment", String.valueOf(true));
        params.put("department_id", String.valueOf(id));
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
