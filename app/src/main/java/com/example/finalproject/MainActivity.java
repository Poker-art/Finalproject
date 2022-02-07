package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnProfile, btnCamera;
    HttpHandler myHandler = new HttpHandler(this);
    String url = "https://sandbox-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?start=1&limit=500&convert=USD";;
    private String api_key = "a0554350-70fc-4876-a8ac-7f6b39238cc7";








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnProfile = findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(this);
        btnCamera = findViewById(R.id.btnCamera);
        btnCamera.setOnClickListener(this);




    }

    @Override
    public void onClick(View view) {
        if(view == btnProfile){
            Intent profileIntent = new Intent(this,ProfileActivity.class);
            startActivity(profileIntent);
        }

        if(view == btnCamera){

            //The function implements a VolleyCallBack which is called on a successful response through the VolleyCallback interface.
            GetCoins(new VolleyCallback(){
                @Override
                public void onSuccess(JSONObject result){
                    Toast.makeText(getApplicationContext(), "Response: "+ result, Toast.LENGTH_LONG).show();

              //do stuff here
                }
            });

        }
    }

    //interface for receiving the Volley successful response
    public interface VolleyCallback{
        void onSuccess(JSONObject result);
    }




     public void GetCoins(final VolleyCallback callback) {
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
                callback.onSuccess(response);
//                Toast.makeText(getApplicationContext(), "Response: "+ result, Toast.LENGTH_LONG).show();
                try {
                    Log.d("JSON", String.valueOf(response));
                    String Error = response.getString("httpStatus");
                    if (Error.equals("OK")) {

                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", "Error: " + error.getMessage());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("accept", "application-json");
                headers.put("X-CMC_PRO_API_KEY", api_key);
                return headers;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(req);


    }






}


