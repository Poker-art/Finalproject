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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnCamera;
    FloatingActionButton addButton;
    String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?start=1&limit=500";
    private String api_key = "a0554350-70fc-4876-a8ac-7f6b39238cc7";







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCamera = findViewById(R.id.btnCamera);
        btnCamera.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        if(view == btnCamera){
            Intent ButtonsIntent = new Intent(this,ButtonsActivity.class);
            startActivity(ButtonsIntent);
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);


        }
    }

    public void startButtons(LinkedList<String> names, LinkedList<String> prices){

        String[] arraynames = names.toArray(new String[names.size()]);
        String[] arrayPrices = prices.toArray(new String[prices.size()]);

        Intent ButtonsIntent = new Intent(this,ButtonsActivity.class);

        ButtonsIntent.putExtra("names",arraynames);
        ButtonsIntent.putExtra("prices",arrayPrices);

        startActivity(ButtonsIntent);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

    }


}


