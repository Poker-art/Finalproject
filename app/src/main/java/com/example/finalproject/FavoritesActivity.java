package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FavoritesActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    List<Coin> coins = null;
    Button b1;
    LinearLayout LL;
    BottomNavigationView bottomNavigationView;
    SwipeRefreshLayout SL;

    DatabaseHandler dbHandler = new DatabaseHandler(FavoritesActivity.this);


    String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?start=1&limit=500";
    private String api_key = "a0554350-70fc-4876-a8ac-7f6b39238cc7";



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        SL = findViewById(R.id.swiperefresh);
        SL.setOnRefreshListener(FavoritesActivity.this);


        bottomNavigationView = findViewById(R.id.bottomNavigator);
        bottomNavigationView.setSelectedItemId(R.id.btnFavorites);
        bottomNavigationView.setClipToOutline(true);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.btnPortfolio:
                        Intent portfolioIntent = new Intent(getApplicationContext(), PortfolioActivity.class);
                        startActivity(portfolioIntent);
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        break;

                    case R.id.btnCoinMenu:
                        Intent coinsIntent = new Intent(getApplicationContext(), ButtonsActivity.class);
                        startActivity(coinsIntent);
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        break;

                    case R.id.btnFavorites:
                        Toast.makeText(getApplicationContext(), "Already In the Favorites Screen", Toast.LENGTH_LONG).show();
                        break;
                }
                return false;
            }
        });


        coins = dbHandler.getFavorites();
        LL = findViewById(R.id.linlay1);



        if(coins != null){

            Coin[] favoriteCoins =coins.toArray(new Coin[coins.size()]);

            LinkedList<String> linkedFavoriteNames = new LinkedList<String>();
            LinkedList<String> linkedFavoritePrices = new LinkedList<String>();

            for(int i = 0;i< favoriteCoins.length;i++){
                linkedFavoriteNames.add(favoriteCoins[i].getName());
                linkedFavoritePrices.add(favoriteCoins[i].getPrice());
            }

            String[] favoriteNames =linkedFavoriteNames.toArray(new String[linkedFavoriteNames.size()]);
            String[] favoritePrices =linkedFavoritePrices.toArray(new String[linkedFavoritePrices.size()]);


            for (int i = 0;i < favoriteNames.length;i++){
                b1 = new Button(FavoritesActivity.this);
                Drawable icon = getApplicationContext().getDrawable(R.drawable.ic_baseline_star_24);
                icon.setBounds(0,0,60,60);
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) LL.getLayoutParams();
                params.setMargins(15,15,15,15);
                b1.setText(favoriteNames[i] + "  " + favoritePrices[i]);
                b1.setGravity(Gravity.LEFT);
                b1.setTag(1);
                b1.setId(i);
                b1.setAllCaps(false);
                b1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                b1.setTextColor(Color.WHITE);
                b1.setBackgroundResource(R.drawable.custom_button);
                b1.setCompoundDrawables(null,null,icon,null);
                LL.addView(b1,params);
                b1.setOnClickListener(FavoritesActivity.this);
            }
        }
    }


    @Override
    public void onClick(View view) {
        Button coinButton = findViewById(view.getId());
        switch (view.getId()) {
            default:
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(FavoritesActivity.this);
                builder.setTitle("Remove Coin");
                builder.setMessage("Are you sure want to remove the Coin from favorites?");
                builder.setIcon(R.drawable.ic_baseline_remove_from_queue_24);
                builder.setBackground(getDrawable(R.drawable.alert_dialog_remove));
                builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dbHandler.deleteData(coinButton.getText().toString().replaceAll("\\d+(\\.\\d+)?", ""));
                        Toast.makeText(getApplicationContext(),"Removed coin.", Toast.LENGTH_LONG).show();
                        onRefresh();
                    }
                });
                builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });


                builder.show();
        }
    }

    @Override
    public void onRefresh() {
        LinkedList<String> prices = new LinkedList<String>();
        LinkedList<String> names = new LinkedList<String>();

        //LinkedList of names of every CryptoCoin.
        //The function implements a VolleyCallBack which is called on a successful response through the VolleyCallback interface.

        GetCoins(new ButtonsActivity.VolleyCallback() {
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
                    boolean inFavorites = dbHandler.updateData(arrayNames[i]);
                    if(inFavorites) {
                        b1 = new Button(FavoritesActivity.this);
                        Drawable icon = getApplicationContext().getDrawable(R.drawable.ic_baseline_star_24);
                        icon.setBounds(0, 0, 60, 60);
                        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) LL.getLayoutParams();
                        params.setMargins(15, 15, 15, 15);
                        b1.setText(arrayNames[i] + "  " + arrayPrices[i]);
                        b1.setGravity(Gravity.LEFT);
                        b1.setTag(1);
                        b1.setId(i);
                        b1.setAllCaps(false);
                        b1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        b1.setTextColor(Color.WHITE);
                        b1.setBackgroundResource(R.drawable.custom_button);
                        b1.setCompoundDrawables(null, null, icon, null);
                        LL.addView(b1, params);
                        b1.setOnClickListener(FavoritesActivity.this);
                    }

                }



                SL.setRefreshing(false);
            }


        });
    }

    public interface VolleyCallback{
        void onSuccess(JSONObject result);
    }

    public void GetCoins(final ButtonsActivity.VolleyCallback callback) {
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
                headers.put("X-CMC_PRO_API_KEY",api_key );
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