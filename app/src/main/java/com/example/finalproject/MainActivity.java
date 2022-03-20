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
        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(this);
        btnCamera = findViewById(R.id.btnCamera);
        btnCamera.setOnClickListener(this);




    }

    @Override
    public void onClick(View view) {
        if(view == addButton){
            Intent ButtonsIntent = new Intent(this,ButtonsActivity.class);
            startActivity(ButtonsIntent);
        }

        if(view == btnCamera){

            //The function implements a VolleyCallBack which is called on a successful response through the VolleyCallback interface.
            GetCoins(new VolleyCallback(){
                @Override
                public void onSuccess(JSONObject result){
                    try {
                        Iterator x = result.keys();
                        JSONArray resultsArray = new JSONArray();

                        while(x.hasNext()){ // Iterator that turn JSONObject to JSONArray.
                            String key = (String) x.next();
                            resultsArray.put(result.get(key));
                        }

                        Toast.makeText(getApplicationContext(), "Loop worked!" , Toast.LENGTH_LONG).show();

                        JSONArray coinDetails = resultsArray.getJSONArray(1); // gets the JSONArray that contains the needed coins data.
                        LinkedList<JSONObject> linkedCoins = new LinkedList<JSONObject>(); //Linked list of JSONObject coins.

                        for(int i = 0;i < coinDetails.length();i++){
                            linkedCoins.add(coinDetails.getJSONObject(i)); //Adding the coins to the linked list.
                        }

                        LinkedList<String> names = new LinkedList<String>(); //LinkedList of names of every CryptoCoin.

                        for(int i = 0;i<linkedCoins.size();i++){
                            names.add(linkedCoins.get(i).getString("name")); //Adding all the names to the LinkedList of coin names.
                        }

                        System.out.println(names); //FIXME:A system print for debugger.

                    } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Something went wrong" + e.toString(), Toast.LENGTH_LONG).show();
                }
                }
            });

        }
    }

    //interface for receiving the Volley successful response
    public interface VolleyCallback{
        void onSuccess(JSONObject result);
    }



    //this function get JSON information from the given URL (line 33)
     public void GetCoins(final VolleyCallback callback) {
        //creating a JSON object with the needed method (GET), the url (CoinMarketCap), and the listener for the response.
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            //when a response is given:
            public void onResponse(JSONObject response) {
                //Log the response to screen
                Log.d("Response", response.toString());
                //If the response is "successful" (GET request worked), execute the onSuccess() function - which takes the Json object returned from the request.
                callback.onSuccess(response);
//                Toast.makeText(getApplicationContext(), "Response: "+ result, Toast.LENGTH_LONG).show();
               //try to parse the value of the json response, TODO: i have some questions about this function as it seems unneeded
                try {
                    Log.d("JSON", String.valueOf(response));
                    String Error = response.getString("httpStatus");
                 //possibly an unneeded function (if/else are empty)
                    if (Error.equals("OK")) {

                    } else {

                    }
                    //Catches ONLY parsing errors.
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


