package com.example.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    String[] namesArray = null;
    String[] pricesArray = null;

    String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?start=1&limit=500";
    private String api_key = "a0554350-70fc-4876-a8ac-7f6b39238cc7";
    DatabaseHandler coinDatabase = new DatabaseHandler(ButtonsActivity.this);

    SharedPreferences prefs = null;



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.btnPortfolio:
                Intent portfolioIntent = new Intent(this, PortfolioActivity.class);
                startActivity(portfolioIntent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;

            case R.id.btnCoinMenu:
                    Toast.makeText(getApplicationContext(), "Already In the Coins Screen", Toast.LENGTH_LONG).show();
                break;

            case R.id.btnFavorites:
                Intent favoritesIntent = new Intent(this, FavoritesActivity.class);
                startActivity(favoritesIntent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
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

        LinkedList<String> names = new LinkedList<String>();
        LinkedList<String> prices = new LinkedList<String>();
        //LinkedList of names of every CryptoCoin.
        //The function implements a VolleyCallBack which is called on a successful response through the VolleyCallback interface.

        GetCoins(new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    Iterator x = result.keys();
                    JSONArray resultsArray = new JSONArray();

                    while (x.hasNext()) { // Iterator that turn JSONObject to JSONArray.
                        String key = (String) x.next();
                        resultsArray.put(result.get(key));
                    }

                    Toast.makeText(getApplicationContext(), "Welcome!", Toast.LENGTH_LONG).show();

                    JSONArray coinDetails = resultsArray.getJSONArray(1); // gets the JSONArray that contains the needed coins data.

                    LinkedList<JSONObject> linkedCoins = new LinkedList<JSONObject>(); //Linked list of JSONObject coins.

                    for (int i = 0; i < coinDetails.length(); i++) {
                        linkedCoins.add(coinDetails.getJSONObject(i)); //Adding the coins to the linked list.
                    }


                    for (int i = 0; i < linkedCoins.size(); i++) {
                        names.add(linkedCoins.get(i).getString("name")); //Adding all the names to the LinkedList of coin names.
                        prices.add(linkedCoins.get(i).getJSONObject("quote").getJSONObject("USD").getString("price"));
                    }

                    SL = findViewById(R.id.swiperefresh);
                    SL.setOnRefreshListener(ButtonsActivity.this);
                    LL = findViewById(R.id.linlay1);

                    namesArray = names.toArray(new String[names.size()]);
                    pricesArray = prices.toArray(new String[prices.size()]);
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
                        b1.setOnClickListener(ButtonsActivity.this);
                    }

                    prefs = getSharedPreferences("shared preferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();

                    StringBuilder sbNames = new StringBuilder();
                    for (int i = 0;i< namesArray.length;i++){
                        sbNames.append(namesArray[i]).append(",");
                    }

                    editor.putString("names",sbNames.toString());

                    StringBuilder sbPrices = new StringBuilder();
                    for (int i = 0;i< pricesArray.length;i++){
                        sbPrices.append(pricesArray[i]).append(",");
                    }

                    editor.putString("prices",sbPrices.toString());
                    //editor.apply();
                    System.out.println(names); //FIXME:A system print for debugger.

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Something went wrong" + e.toString(), Toast.LENGTH_LONG).show();
                }

            }




        });


        //coinDatabase.deleteTablePortfolio();
        //coinDatabase.deleteTableFavorites();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }



    @Override
    public void onClick(View view) {
        Button coinButton = findViewById(view.getId());
        switch (view.getId()){
                default:

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

                    alertDialogBuilder.setTitle("Add Currency?")
                            .setMessage("Add to Portfolio/Favorites")
                            .setPositiveButton("Favorites", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String name = coinButton.getText().toString().replaceAll("\\d+(\\.\\d+)?", "");
                                    name = name.substring(0, name.length() - 2);
                                    boolean isUpdated = coinDatabase.updateData(name);
                                    if (isUpdated) {
                                        Toast.makeText(getApplicationContext(), "Coin is already in the Favorites Directory!", Toast.LENGTH_LONG).show();
                                        return;
                                    } else {
                                        coinDatabase.addCoin(name);
                                        Toast.makeText(getApplicationContext(), "Added to Favorites! " + coinDatabase.getFavorites().get(0), Toast.LENGTH_SHORT).show();

                                    }
                                }
                            })
                            .setNegativeButton("Portfolio",null)

                            .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();




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

                                    DateValidator validator = new DateValidatorUsingDateFormat("dd/MM/YYYY");
                                    if(amountData.length() == 0 || dateData.length() == 0 || priceData.length() == 0) {
                                        Toast.makeText(getApplicationContext(), "Please enter a valid data arguments for all text inputs, with a date format of: DD/MM/YYYY", Toast.LENGTH_LONG).show();
                                        return;
                                    } else if (Integer.parseInt(amountData) <= 0 || !validator.isValid(dateData) || Integer.parseInt(priceData) <= 0) {
                                        Toast.makeText(getApplicationContext(), "Please enter a valid data arguments for all text inputs, with a date format of: DD/MM/YYYY", Toast.LENGTH_LONG).show();
                                        return;
                                    }

                                    boolean overAllGainLoss = coinDatabase.updateData(name,amountData,dateData,priceData);
                                    if(overAllGainLoss){
                                        Toast.makeText(getApplicationContext(),"Updated amount bought of " + name, Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        coinDatabase.addCoin(name,amountData,dateData,priceData);
                                        Toast.makeText(getApplicationContext(),"Added "+coinDatabase.getPortfolio().get(0).getName(), Toast.LENGTH_LONG).show();

                                    }

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

        GetCoins(new VolleyCallback() {
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
                String[] arrayNames = names.toArray(new String[names.size()]);

                LL.removeAllViews();
                for (int i = 0;i < 500;i++){
                    b1 = new Button(ButtonsActivity.this);
                    String price = arrayPrices[i];
                    b1.setText(arrayNames[i] + "   " + price);
                    b1.setGravity(Gravity.LEFT);
                    b1.setTag(1);
                    b1.setId(i);
                    LL.addView(b1);
                    b1.setOnClickListener(ButtonsActivity.this);
                }

                prefs = getSharedPreferences("shared preferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                StringBuilder sbNames = new StringBuilder();
                for (int i = 0;i< arrayNames.length;i++){
                    sbNames.append(arrayNames[i]).append(",");
                }

                editor.putString("names",sbNames.toString());

                StringBuilder sbPrices = new StringBuilder();
                for (int i = 0;i< arrayPrices.length;i++){
                    sbPrices.append(arrayPrices[i]).append(",");
                }

                editor.putString("prices",sbPrices.toString());
                //editor.apply();

                SL.setRefreshing(false);
            }


        });

    }

    //interface for receiving the Volley successful response
    public interface VolleyCallback{
        void onSuccess(JSONObject result);
    }

    public void GetCoins(final VolleyCallback callback) {
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



    public interface DateValidator {
        boolean isValid(String dateStr);
    }

    public class DateValidatorUsingDateFormat implements DateValidator {
        private String dateFormat;

        public DateValidatorUsingDateFormat(String dateFormat) {
            this.dateFormat = dateFormat;
        }

        @Override
        public boolean isValid(String dateStr) {
            DateFormat sdf = new SimpleDateFormat(this.dateFormat);
            sdf.setLenient(false);
            try {
                sdf.parse(dateStr);
            } catch (ParseException e) {
                return false;
            }
            return true;
        }
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