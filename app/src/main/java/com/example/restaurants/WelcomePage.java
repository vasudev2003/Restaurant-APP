package com.example.restaurants;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class WelcomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                SharedPreferences preferences = getSharedPreferences(Links.perfName, Context.MODE_PRIVATE);
//                if(preferences.contains("logged"))
//                    if(preferences.getString("logged","-").equals("true"))
//                    {
//                        Intent i = new Intent(WelcomePage.this,MainScreen.class);
//                        startActivity(i);
//                    }
//                else {
                        Intent i = new Intent(WelcomePage.this, LoginPage.class);
                        startActivity(i);
//                    }
                //invoke the SecondActivity.
                finish();
                //the current activity will get finished.
            }
        }, 2000);
    }
}
