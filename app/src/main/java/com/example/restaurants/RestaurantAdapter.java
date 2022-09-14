package com.example.restaurants;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class RestaurantAdapter extends BaseAdapter {

    Activity activity;
    JSONArray data;
    LayoutInflater layoutInflater = null;
    SharedPreferences preferences;
    ViewHolder vH;
    String search="";
    public RestaurantAdapter(Activity activity, JSONArray data){
        this.activity=activity;
        this.data = data;
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        preferences = activity.getSharedPreferences(Links.perfFav,Context.MODE_PRIVATE);
    }
    public RestaurantAdapter(Activity activity, JSONArray data,String search){
        this.search=search;
        this.activity=activity;
        this.data = data;
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        preferences = activity.getSharedPreferences(Links.perfFav,Context.MODE_PRIVATE);
    }
    @Override
    public int getCount() {
        return data.length()-1;
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
    public View getView(int position, View convertView, ViewGroup parent) {

        JSONObject  jsonObject = null;
        try {
            jsonObject = (JSONObject) data.get(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        View view = convertView;
         vH = new ViewHolder();



        if(view==null) {
            view = layoutInflater.inflate(R.layout.resturants_card, null);
        }
//        else{
//            vH = (ViewHolder)convertView.getTag();
//        }

        try {

            vH.image =  view.findViewById(R.id.image);
            vH.name = view.findViewById(R.id.name);
            vH.cost = view.findViewById(R.id.cost);
            vH.rating = view.findViewById(R.id.ratings);
            vH.favorite = view.findViewById(R.id.favorite);

            vH.favorite.setContentDescription(jsonObject.getString("id"));
            vH.favorite.setTag(jsonObject);

            // Set Data
            vH.name.setText(jsonObject.getString("name"));
            vH.rating.setText(jsonObject.getString("rating"));
            vH.cost.setText(jsonObject.getString("cost_for_one")+"/person");

            // Set Image by URl
//            if(vH.image!=null)
                Glide.with(view).load(jsonObject.getString("image_url")).into(vH.image);

            // Is Favorite
            if(preferences.contains("ID_"+jsonObject.getString("id")))
                vH.favorite.setColorFilter(0xFFF44336);//(Red COlor)
            else
                vH.favorite.setColorFilter(0xDFD1D0);//(Gray Color)

            // Set Toggle  favorite
            vH.favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView imageView = (ImageView)v;
                    String id = imageView.getContentDescription().toString();
                    JSONObject json = (JSONObject)imageView.getTag();
                    SharedPreferences.Editor editor = preferences.edit();
                    if(preferences.contains("ID_"+id))
                    {
                        // Remove
                        editor.remove("ID_"+id);
                        editor.commit();
                        imageView.setColorFilter(0xDFD1D0);//(Gray Color)
                    }
                    else
                    {

                        // Add to Favorite("ID_12" -> "12")
                        editor.putString("ID_"+id,json.toString());
                        editor.commit();
                        imageView.setColorFilter(0xFFF44336);//(Red COlor)

                    }
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }


        if(view==null)
            view.setTag(vH);

        if(!search.equals("")){
            try {
                boolean res = ((JSONObject)data.get(position)).getString("name").toLowerCase().startsWith(search.toLowerCase());
                Log.d("Adapter", "getView: "+res);
                if(res)
                {
                    Log.d("Search", "getView: "+jsonObject.getString("name"));
                    return  view;

                }
                else
                {
                    view = new LinearLayout(activity);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return view;
    }

    class ViewHolder {
        TextView name,cost,rating;
        ImageView image,favorite;
    }
}
