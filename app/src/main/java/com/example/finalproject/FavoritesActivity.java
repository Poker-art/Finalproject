package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity implements  View.OnClickListener {
    List<Coin> coins = null;
    Button b1;
    LinearLayout LL;


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.btnPortfolio:
                Intent portfolioIntent = new Intent(this, PortfolioActivity.class);
                startActivity(portfolioIntent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;

            case R.id.btnCoinMenu:
                Intent coinsIntent = new Intent(this, ButtonsActivity.class);
                startActivity(coinsIntent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;

            case R.id.btnFavorites:
                Toast.makeText(getApplicationContext(), "Already In the Favorites Screen", Toast.LENGTH_LONG).show();
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
        setContentView(R.layout.activity_favorite);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        coins = dbHandler.getFavorites();
        LL = findViewById(R.id.linlay1);

        if(coins != null){

            Coin[] favoriteCoins =coins.toArray(new Coin[coins.size()]);

            LinkedList<String> linkedFavoriteNames = new LinkedList<String>();
            for(int i = 0;i< favoriteCoins.length;i++){
                linkedFavoriteNames.add(favoriteCoins[i].getName());
            }

            String[] favoriteNames =linkedFavoriteNames.toArray(new String[linkedFavoriteNames.size()]);


            for (int i = 0;i < favoriteNames.length;i++){
                b1 = new Button(FavoritesActivity.this);
                String name = favoriteNames[i];
                b1.setText(favoriteNames[i]);
                b1.setGravity(Gravity.LEFT);
                b1.setTag(1);
                LL.addView(b1);
                b1.setOnClickListener(FavoritesActivity.this);
            }
        }
    }


    @Override
    public void onClick(View view) {

    }
}