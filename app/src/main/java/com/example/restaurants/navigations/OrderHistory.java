package com.example.restaurants.navigations;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.restaurants.Links;
import com.example.restaurants.R;
import com.example.restaurants.RestaurantAdapter;
import com.example.restaurants.RestaurantDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderHistory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderHistory extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    LinearLayout orders_lay;
    RequestQueue queue;
    JSONArray order_data;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrderHistory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderHistory.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderHistory newInstance(String param1, String param2) {
        OrderHistory fragment = new OrderHistory();
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
    public void onStart() {
        super.onStart();
        // Send Request
        getOrderHistory();



    }
    void createOrderList()
    {
        // Start Creating View
        View orderInfo = null;
        JSONArray orderedItems;
        for(int i=0;i<order_data.length();i++)
        {
            try {
                JSONObject rest = (JSONObject) order_data.get(i);
                orderInfo = getActivity().getLayoutInflater().inflate(R.layout.order_history_item,null);
                ((TextView)orderInfo.findViewById(R.id.rest_name)).setText(rest.getString("restaurant_name"));
                ((TextView)orderInfo.findViewById(R.id.order_date)).setText(rest.getString("order_placed_at").split(" ")[0]);
                ((TextView)orderInfo.findViewById(R.id.total)).setText(rest.getString("total_cost"));

                orderedItems = rest.getJSONArray("food_items");
                LinearLayout items_lay = orderInfo.findViewById(R.id.order_items_lay);
                // Create List
                for(int j=0;j<orderedItems.length();j++)
                {
                    JSONObject item = (JSONObject)orderedItems.get(j);
                    View orderItem = getActivity().getLayoutInflater().inflate(R.layout.cart_item,null);
                    ((TextView)orderItem.findViewById(R.id.food_name)).setText(item.getString("name"));
                    ((TextView)orderItem.findViewById(R.id.cost)).setText(item.getString("cost"));
                    items_lay.addView(orderItem);
                }




            } catch (JSONException e) {
                e.printStackTrace();
            }

            orders_lay.addView(orderInfo);

        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        queue = Volley.newRequestQueue(getContext());
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);
        orders_lay = view.findViewById(R.id.orders_lay);
        return view;


    }

    void getOrderHistory()
    {
        // Required empty public constructors
        queue = Volley.newRequestQueue(getContext());
        queue.getCache().clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Links.orderHistory,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("All Restaurants", "onResponse: "+response.toString());

                        try {
                            if(response.getJSONObject("data").getBoolean("success")==true){
                                    order_data = response.getJSONObject("data").getJSONArray("data");
                                    createOrderList();
                            }
                            else
                            {
                                Toast.makeText(getContext(),"Fetching Error",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Login", "onErrorResponse: "+error);
                        Toast.makeText(getContext(),"Check the internet connection",Toast.LENGTH_SHORT).show();
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
