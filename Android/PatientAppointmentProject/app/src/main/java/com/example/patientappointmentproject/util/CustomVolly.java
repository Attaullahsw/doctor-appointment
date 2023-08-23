package com.example.patientappointmentproject.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.patientappointmentproject.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class CustomVolly {

    /**
     * Request to the server for data
     */
    public static void getInsertGETData(final Context context, final Map<String, String> parameters, String url,final VolleyCallback callback) {

        final ProgressDialog progressDialog = new ProgressDialog(context);

        progressDialog.setTitle("Loading!");
        progressDialog.setMessage("Please wait a while...");
        progressDialog.setCancelable(false);

        String strings = context.getResources().getString(R.string.url)+url;
        StringRequest request = new StringRequest(Request.Method.GET, strings, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    progressDialog.dismiss();
                    callback.onSuccess(jsonObject);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
                //Toast.makeText(RegisterUserActivity.this, "Connection Problem", Toast.LENGTH_LONG).show();
                //InternetWarningDialog.showCustomDialog(RegisterUserActivity.this);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = parameters;
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
        progressDialog.show();

    }

    /**
     * Request to the server for data
     */
    public static void getInsertPOSTData(final Context context, final Map<String, String> parameters, String url,final VolleyCallback callback) {

        final ProgressDialog progressDialog = new ProgressDialog(context);

        progressDialog.setTitle("Loading!");
        progressDialog.setMessage("Please wait a while...");
       // progressDialog.setCancelable(false);

        String strings = context.getResources().getString(R.string.url)+url;
        StringRequest request = new StringRequest(Request.Method.POST, strings, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    progressDialog.dismiss();
                    callback.onSuccess(jsonObject);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                //InternetWarningDialog.showCustomDialog(RegisterUserActivity.this);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = parameters;
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
        progressDialog.show();

    }


}
