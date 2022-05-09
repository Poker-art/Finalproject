package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Color;
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

public class PortfolioActivity extends AppCompatActivity implements View.OnClickListener {
    List<Coin> coins = null;
    Button b1;
    LinearLayout LL;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.btnPortfolio:
                Toast.makeText(getApplicationContext(), "Already In the Portfolio Screen", Toast.LENGTH_LONG).show();
                break;

            case R.id.btnCoinMenu:
                Intent coinsIntent = new Intent(this, ButtonsActivity.class);
                startActivity(coinsIntent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
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
        setContentView(R.layout.activity_portfolio);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        coins = dbHandler.getPortfolio();
        LL = findViewById(R.id.linlay1);

        if(coins != null){

            Coin[] portfolioCoins =coins.toArray(new Coin[coins.size()]);

            LinkedList<String> linkedPortfolioNames = new LinkedList<String>();
            LinkedList<String> linkedPortfolioGainLoss = new LinkedList<String>();
            LinkedList<String> linkedPortfolioDate = new LinkedList<String>();

            for(int i = 0;i< portfolioCoins.length;i++){
                linkedPortfolioNames.add(portfolioCoins[i].getName());
                linkedPortfolioGainLoss.add(portfolioCoins[i].getProfit());
                linkedPortfolioDate.add(portfolioCoins[i].getDate());
            }

            String[] portfolioNames =linkedPortfolioNames.toArray(new String[linkedPortfolioNames.size()]);
            String[] portfolioGainLoss = linkedPortfolioGainLoss.toArray(new String[linkedPortfolioGainLoss.size()]);
            String[] portfolioDate = linkedPortfolioDate.toArray(new String[linkedPortfolioDate.size()]);


            for (int i = 0;i < portfolioNames.length;i++){
                b1 = new Button(PortfolioActivity.this);
                b1.setText(portfolioNames[i]+ "  "+portfolioGainLoss[i]+"%  At: " + portfolioDate[i]);
                if(Double.parseDouble(portfolioGainLoss[i]) < 0){
                    b1.setTextColor(Color.RED);
                }else if(Double.parseDouble(portfolioGainLoss[i]) > 0){
                    b1.setTextColor(Color.GREEN);

                }else{
                    b1.setTextColor(Color.BLACK);

                }
                b1.setGravity(Gravity.LEFT);
                b1.setTag(1);
                LL.addView(b1);
                b1.setOnClickListener(PortfolioActivity.this);
            }
        }
    }

    @Override
    public void onClick(View view) {

    }
}