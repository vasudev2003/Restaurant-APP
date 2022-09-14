package com.example.restaurants;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RestaurantDetails extends AppCompatActivity {
    ListView listView;
    String rest_id;
    String rest_name;
    RequestQueue queue ;
    JSONArray data;
    ArrayList<JSONObject> myCart;
    Button cartBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        queue = Volley.newRequestQueue(getApplicationContext());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rest_name = getIntent().getStringExtra("rest_name");
        rest_id  = getIntent().getStringExtra("rest_id");
        toolbar.setTitle(rest_name);



        listView = findViewById(R.id.listView);
        cartBtn = findViewById(R.id.cartBtn);
        myCart = new ArrayList<JSONObject>();


        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RestaurantDetails.this,MyCart.class);

                if(myCart.size()!=0)
                {
                    Links.cart = myCart;
                    i.putExtra("rest_name",rest_name);
                    i.putExtra("rest_id",rest_id);
                    startActivity(i);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please select some items", Toast.LENGTH_SHORT).show();
                }

            }
        });



        sendRequestForData();
    }

    @Override
    public void onBackPressed() {
        if(myCart.size()!=0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirmation")
                    .setMessage("Are you sure you want to clear the cart?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Links.cart.clear();
                            RestaurantDetails.super.onBackPressed();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            AlertDialog dialog = builder.create();

            dialog.show();
        }
        else
            super.onBackPressed();
    }

    void sendRequestForData()
    {

//        queue.getCache().clear();
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Links.restaurants,null,
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Links.menu,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Restaurants Menu", "onResponse: "+response.toString());

                        try {
                            if(response.getJSONObject("data").getBoolean("success")==true){
                                data = response.getJSONObject("data").getJSONArray("data");
                                MenuAdapter menuAdapter = new MenuAdapter(getApplicationContext(),data,myCart);
                                // Display Restaurants: in ListView
                                listView.setAdapter(menuAdapter);

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                                    }
                                });
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Fetching Error",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(),"Check the internet connection",Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String,String>();
                return param;
            }
        };
        queue.add(jsonObjectRequest);
    }
}
