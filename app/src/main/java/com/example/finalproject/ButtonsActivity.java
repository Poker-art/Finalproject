package com.example.finalproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class ButtonsActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private LinearLayout LL;

    private Button b1;
    private TextView t1;
    private SwipeRefreshLayout SL;

    String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?start=1&limit=500";
    private String api_key = "a0554350-70fc-4876-a8ac-7f6b39238cc7";
    DatabaseHandler coinDatabase = new DatabaseHandler(ButtonsActivity.this);


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                Intent mainIntent = new Intent(this, MainActivity.class);
                startActivity(mainIntent);
                break;

            case R.id.item2:
                if(getCallingActivity() == this.getCallingActivity()){
                    Toast.makeText(getApplicationContext(), "Already In the Coins Screen", Toast.LENGTH_LONG).show();
                    break;
                }
                Intent buttonsIntent = new Intent(this, ButtonsActivity.class);
                startActivity(buttonsIntent);
                break;

            case R.id.item3:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buttons);

        Bundle bundle = getIntent().getExtras();
        String[] namesArray = bundle.getStringArray("names");
        String[] pricesArray = bundle.getStringArray("prices");


        SL = findViewById(R.id.swiperefresh);
        SL.setOnRefreshListener(ButtonsActivity.this);
        LL = findViewById(R.id.linlay1);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        for (int i = 0;i<500;i++) {

            t1 = new TextView(ButtonsActivity.this);
            b1 = new Button(ButtonsActivity.this);
            b1.setId(i);
            t1.setText(pricesArray[i]);
            t1.setElegantTextHeight(true);
            t1.setTextColor(Color.parseColor("#FFBB86FC"));
            b1.setText(namesArray[i] + "   " + t1.getText());
            b1.setGravity(Gravity.LEFT);
            b1.setTag(1);
            LL.addView(b1);
            b1.setOnClickListener(this);
        }
    }



    @Override
    public void onClick(View view) {
        Button coinButton = findViewById(view.getId());
        switch (view.getId()){
                default:

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

                    alertDialogBuilder.setTitle("Add Currency?")
                            .setMessage("Add to Portfolio/Favorites")
                            .setPositiveButton("Favorites", null)
                            .setNegativeButton("Portfolio",null)

                            .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();


                    Button favoritesButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    favoritesButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            String name = coinButton.getText().toString().replaceAll("\\d+(\\.\\d+)?","");
                            name = name.substring(0, name.length()-2);
                            coinDatabase.addCoin(name);
                            Toast.makeText(getApplicationContext(), "Added to Favorites! " + coinDatabase.getFavorites().get(0), Toast.LENGTH_SHORT).show();
                        }
                    });


                    Button portfolioButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                    portfolioButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            String name = coinButton.getText().toString().replaceAll("\\d+(\\.\\d+)?","");

                            AlertDialog.Builder walletDialogBuilder = new AlertDialog.Builder(ButtonsActivity.this);
                            Context context = getApplicationContext();

                            LinearLayout layout = new LinearLayout(context);
                            layout.setOrientation(LinearLayout.VERTICAL);

                            final EditText amountBox = new EditText(context);
                            amountBox.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
                            amountBox.setHint("Amount");
                            layout.addView(amountBox); // Notice this is an add method

                            final EditText dateBox = new EditText(context);
                            dateBox.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
                            dateBox.setHint("Date");

                            layout.addView(dateBox); // Another add method


                            final EditText priceBox = new EditText(context);
                            priceBox.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
                            priceBox.setHint("Price Paid");

                            layout.addView(priceBox); // Another add method

                            final Button save = new Button(context);
                            save.setHint("Save");
                            save.setHintTextColor(Color.CYAN);
                            save.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String amountData = amountBox.getText().toString();
                                    String dateData = dateBox.getText().toString();

                                    String priceData = priceBox.getText().toString();

                                    coinDatabase.addCoin(name,amountData,dateData,priceData);

                                    Toast.makeText(getApplicationContext(),coinDatabase.getPortfolio().get(1).getDate(), Toast.LENGTH_LONG).show();
                                }
                            });
                            layout.addView(save);


                            walletDialogBuilder.setView(layout);
                            AlertDialog walletDialog = walletDialogBuilder.create();
                            walletDialog.show();


                            Toast.makeText(getApplicationContext(), "Added to Wallet! " + name, Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
            }

    }






    @Override
    public void onRefresh() {
        LinkedList<String> prices = new LinkedList<String>();
        LinkedList<String> names = new LinkedList<String>();

        //LinkedList of names of every CryptoCoin.
        //The function implements a VolleyCallBack which is called on a successful response through the VolleyCallback interface.

        GetCoins(new MainActivity.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    Iterator x = result.keys();
                    JSONArray resultsArray = new JSONArray();

                    while (x.hasNext()) { // Iterator that turn JSONObject to JSONArray.
                        String key = (String) x.next();
                        resultsArray.put(result.get(key));
                    }

                    Toast.makeText(getApplicationContext(), "Loop worked!", Toast.LENGTH_LONG).show();

                    JSONArray coinDetails = resultsArray.getJSONArray(1); // gets the JSONArray that contains the needed coins data.

                    LinkedList<JSONObject> linkedCoins = new LinkedList<JSONObject>(); //Linked list of JSONObject coins.

                    for (int i = 0; i < coinDetails.length(); i++) {
                        linkedCoins.add(coinDetails.getJSONObject(i)); //Adding the coins to the linked list.
                    }


                    for (int i = 0; i < linkedCoins.size(); i++) {
                        names.add(linkedCoins.get(i).getString("name"));
                        prices.add(linkedCoins.get(i).getJSONObject("quote").getJSONObject("USD").getString("price"));

                    }


                    System.out.println(prices); //FIXME:A system print for debugger.

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Something went wrong" + e.toString(), Toast.LENGTH_LONG).show();
                }

                String[] arrayPrices = prices.toArray(new String[prices.size()]);
                String[] arraynames = names.toArray(new String[names.size()]);

                LL.removeAllViews();
                for (int i = 0;i < 500;i++){
                    b1 = new Button(ButtonsActivity.this);
                    String price = arrayPrices[i];
                    b1.setText(arraynames[i] + "     " + price);
                    b1.setGravity(Gravity.LEFT);
                    b1.setTag(1);
                    LL.addView(b1);
                    b1.setOnClickListener(ButtonsActivity.this);
                }

                SL.setRefreshing(false);
            }


        });

    }

    public void GetCoins(final MainActivity.VolleyCallback callback) {
        //creating a JSON object with the needed method (GET), the url (CoinMarketCap), and the listener for the response.
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
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

    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }
}








/*
runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    b1 = new Button(ButtonsActivity.this);
                                    b1.setId(i);
                                    b1.setText(namesArray[i]);
                                    b1.setGravity(Gravity.LEFT);
                                    b1.setTag(1);
                                    LL.addView(b1);
                                    b1.setOnClickListener(this);

                                    JSONObject Jresponse = new JSONObject(response.body().string());
                                    int coinPrice = (Jresponse.getJSONObject("bitcoin")).getInt("usd");
                                    System.out.println("test");

                                    //TODO: API has daily limit [REALLY FUCKING LOW :( ]. Check CoinGecko API...

                                } catch (IOException | JSONException e) {
                                    Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            }
                        });








public JSONArray getRate(String url){
        JSONArray Jarray;
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://rest.coinapi.io/v1/exchangerate/INJ/USD/")
                    .build();
            Response responses = null;

            try {
                responses = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String jsonData = responses.body().string();
            JSONObject Jobject = new JSONObject(jsonData);
            Jarray = Jobject.getJSONArray("rate");


        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return Jarray;

    }
    */