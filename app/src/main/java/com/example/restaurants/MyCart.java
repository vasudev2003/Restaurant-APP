package com.example.restaurants;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyCart extends AppCompatActivity {

    LinearLayout cartItemsLay;
    TextView rest_name;
    Button orderBtn;
    ArrayList<JSONObject> cart;
    String id;
    double total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);

        cartItemsLay = findViewById(R.id.cart_items);
        rest_name = findViewById(R.id.rest_name);
        orderBtn = findViewById(R.id.orderBtn);



        rest_name.setText("Ordering From : "+getIntent().getStringExtra("rest_name"));
        id = getIntent().getStringExtra("rest_id");

        cart  = Links.cart;

        Log.d("Cart","Items:"+cart.toString());


        TextView name,cost;
        for(int i=0;i<cart.size();i++)
        {
            JSONObject json = cart.get(i);
            View item = getLayoutInflater().inflate(R.layout.cart_item,null);
            try {
                name = (TextView)item.findViewById(R.id.food_name);
                name.setText(json.getString("name"));
                total += Double.parseDouble(json.getString("cost_for_one"));
                cost = (TextView)item.findViewById(R.id.cost);
                cost.setText("Rs."+json.getString("cost_for_one"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            cartItemsLay.addView(item);
        }

        orderBtn.setText("Place Order(Total: Rs."+total+" )");




        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    sendOrder();
            }
        });




    }

    void sendOrder()
    {
        startActivity(new Intent(MyCart.this,OrderPlaced.class));
        finish();
    }
}
