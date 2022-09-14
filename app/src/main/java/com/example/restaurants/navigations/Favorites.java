package com.example.restaurants.navigations;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.restaurants.Links;
import com.example.restaurants.R;
import com.example.restaurants.RestaurantAdapter;
import com.example.restaurants.RestaurantDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Favorites#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Favorites extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ListView listView;
    JSONArray jsonArray;
    public Favorites() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Favorites.
     */
    // TODO: Rename and change types and number of parameters
    public static Favorites newInstance(String param1, String param2) {
        Favorites fragment = new Favorites();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        listView = view.findViewById(R.id.restaurant_list);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onResume() {
        getFavorites();
        super.onResume();
    }

    void getFavorites()
    {

        // Prepare Favourite Data
        SharedPreferences preferences = getActivity().getSharedPreferences(Links.perfFav, Context.MODE_PRIVATE);
        StringBuffer jsonString = new StringBuffer();
        Map favourites = preferences.getAll();

        jsonString.append("[");
        for(Object jsonObj:favourites.values())
        {
            jsonString.append(((String)jsonObj)+",");
        }
        jsonString.append("]");

        try {
            jsonArray = new JSONArray(jsonString.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        // Display Restaurants: in ListView
        RestaurantAdapter adapter = new RestaurantAdapter(getActivity(),jsonArray);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getContext(), RestaurantDetails.class);
                try {
                    JSONObject object = (JSONObject)jsonArray.get(position);
                    i.putExtra("rest_name",object.getString("name"));
                    i.putExtra("rest_id",object.getString("id"));
                    startActivity(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
