package com.example.restaurants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MenuAdapter extends BaseAdapter {

    Context context;
    JSONArray data;
    ArrayList<JSONObject> cart;
    LayoutInflater layoutInflater;
    ViewHolder vH;
    MenuAdapter(Context context, JSONArray data, ArrayList<JSONObject> cart)
    {
     this.context = context;
     this.data = data;
        Log.d("Menu", "MenuAdapter: "+data.toString());
     this.cart = cart;
        Log.d("CARt", "MenuAdapter: "+cart.toString());
     layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return data.length();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        vH = new ViewHolder();



        if(view==null) {
            view = layoutInflater.inflate(R.layout.menu_item, null);
        }
//        else{
//            vH = (ViewHolder)convertView.getTag();
//        }

        try {
            JSONObject  jsonObject = (JSONObject) data.get(position);

            vH.name = view.findViewById(R.id.name);
            vH.cost = view.findViewById(R.id.cost);
            vH.addToCartBtn = view.findViewById(R.id.cartBtn);
            vH.addToCartBtn.setContentDescription(""+position);
            vH.addToCartBtn.setTag(jsonObject);

            // Set Data
            vH.name.setText(jsonObject.getString("name"));
            vH.cost.setText("Rs. "+jsonObject.getString("cost_for_one"));



            // Set Toggle  Add / Remove
            vH.addToCartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button btn = (Button)v;
                    JSONObject json = (JSONObject) btn.getTag();
                    Log.d("Menu:"+((Button) v).getText().toString(), "onClick: "+json.toString());
//                    int position_ = Integer.parseInt(btn.getContentDescription().toString());
//                    Log.d("Menu", "onClickMenu: "+position_+" Name : "+btn.getText().toString());
                   if(btn.getText().toString().equals("ADD"))
                   {
                       try {
                           cart.add(json);
                           btn.setBackgroundColor(Color.RED);
                           btn.setText("REMOVE");
                       } catch (Exception e) {
                           e.printStackTrace();
                       }
                   }
                   else
                   {

                       cart.remove(json);
                       btn.setBackgroundColor(Color.rgb(35,150,243));
                       btn.setText("ADD");
                   }

                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }





        return view;
    }

    class ViewHolder {
        TextView name,cost;
        Button addToCartBtn;
    }
}
