package com.example.restaurants.navigations;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.restaurants.Links;
import com.example.restaurants.LoginPage;
import com.example.restaurants.R;
import com.example.restaurants.RestaurantAdapter;
import com.example.restaurants.RestaurantDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    JSONArray data ;
    RequestQueue queue;
    ListView listView;
    EditText searchBar;
    ImageButton sortBtn;
    RestaurantAdapter adapter;
    View sortDailogView;
    AlertDialog sortDailog;
    public Home() {


    }


    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        sortDailogView = getSortDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        listView = view.findViewById(R.id.restaurant_list);

        searchBar = view.findViewById(R.id.search_bar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("Search Bar", "beforeTextChanged: " + s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().matches("[A-Za-z0-9]"))
                {
                    Log.d("Search Bar", "onTextChanged: " + searchBar.getText().toString());
                    adapter = new RestaurantAdapter(getActivity(), data, searchBar.getText().toString());
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("Search Bar", "afterTextChanged: " + s.toString());
                if(s.toString().matches("[A-Za-z0-9]"))
                {
                    Log.d("Search Bar", "onTextChanged: " + searchBar.getText().toString());
                    adapter = new RestaurantAdapter(getActivity(), data, searchBar.getText().toString());
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

            }
        });




        sortBtn = view.findViewById(R.id.sort_btn);
        sortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show dialog box
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(getSortDialog());
                sortDailog = builder.create();
                sortDailog.show();
            }
        });


        searchBar = view.findViewById(R.id.search_bar);

        return view;
    }
    View getSortDialog()
    {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.sort_dialog,null,false);
        RadioGroup rg = view.findViewById(R.id.radio_group);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                sortDailog.dismiss();
                Log.d("Sort Option", "onCheckedChanged: "+checkedId);
                try {
                    switch (checkedId)
                    {
                        case R.id.cost_l_h:   data=sortJson(data,"cost_for_one",false);  break;
                        case R.id.cost_h_l:   data=sortJson(data,"cost_for_one",true);  break;
                        case R.id.rating_l_h: data=sortJson(data,"rating",false); break;
                        case R.id.rating_h_l: data=sortJson(data,"rating",true); break;
                    }
                    // Set to Adapter

                    adapter = new RestaurantAdapter(getActivity(),data);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(getContext(),"Error while sorting",Toast.LENGTH_SHORT).show();
                }



            }
        });

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        sendRequestForData();
    }

    void sendRequestForData()
    {
        // Required empty public constructors
        queue = Volley.newRequestQueue(getContext());
//        queue.getCache().clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Links.restaurants,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        Log.d("All Restaurants", "onResponse: "+response.toString());

                        try {
                            if(response.getJSONObject("data").getBoolean("success")==true){
                                data = response.getJSONObject("data").getJSONArray("data");

                                // Display Restaurants: in ListView
                                adapter = new RestaurantAdapter(getActivity(),data);
                                listView.setAdapter(adapter);

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Intent i = new Intent(getContext(), RestaurantDetails.class);
                                        try {
                                            JSONObject object = (JSONObject)data.get(position);
                                            i.putExtra("rest_name",object.getString("name"));
                                            i.putExtra("rest_id",object.getString("id"));
                                            startActivity(i);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
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

    JSONArray sortJson(JSONArray array,String type,boolean reverse_) throws JSONException {
        ArrayList<JSONObject> arrayList = new ArrayList<>();
        final String key = type;
        final boolean reverse = reverse_;



        for(int i=0;i<array.length();i++)
            arrayList.add((JSONObject) array.get(i));

        Collections.sort(arrayList, new  Comparator <JSONObject>(){

            @Override
            public int compare(JSONObject o1, JSONObject o2) {
                try {
                    Double a = Double.parseDouble(o1.getString(key));
                    Double b = Double.parseDouble(o2.getString(key));
                    int res = a > b ? 1 : a==b ? 0 : -1 ;
                    if(reverse)
                        res*=-1;
                    return res;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });
        JSONArray jsonArray = new JSONArray();

        for(int i=0;i<arrayList.size();i++)
            jsonArray.put(arrayList.get(i));

        return jsonArray;



    }

}
