package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btnMain = findViewById(R.id.btnMain);
        btnMain.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == btnMain){
            Intent mainIntent = new Intent(this,MainActivity.class);
            startActivity(mainIntent);
        }
    }
}


