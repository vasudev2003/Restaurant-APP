package com.example.restaurants;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.restaurants.navigations.Home;

public class OrderPlaced extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed);
    }
    public void goToHome(View v)
    {
        startActivity(new Intent(OrderPlaced.this, MainScreen.class));
        finish();
    }
}
