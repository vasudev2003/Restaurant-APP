package com.example.restaurants;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrationPage extends AppCompatActivity {
    EditText name,email,mobileNo,address,password,confPassword;
    Button regBtn;
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        queue = Volley.newRequestQueue(getApplicationContext());

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        mobileNo = findViewById(R.id.mobile_no);
        address = findViewById(R.id.delivery);
        password = findViewById(R.id.password);
        confPassword = findViewById(R.id.conf_password);
        regBtn  = findViewById(R.id.reg_btn);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validation
                if (name.getText().toString().length()>=3)
                    if(!email.getText().toString().isEmpty())
                        if (mobileNo.getText().toString().length()==10)
                            if(!address.getText().toString().isEmpty())
                                if(password.getText().toString().equals(confPassword.getText().toString())
                                        && password.getText().toString().length()>=4)
                                {

                                        register();
                                }
                                else
                                    Toast.makeText(getApplicationContext(),"Password and Confirm Password doesn't match",Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getApplicationContext(),"Delivery Address cannot be empty",Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getApplicationContext(),"Mobile Number should be 10 digits",Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(),"Invalid Email",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(),"Name should contain atleast 3 characters",Toast.LENGTH_SHORT).show();

            }
        });

    }
    void register()
    {
        queue.getCache().clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Links.registration,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RegistrationPage", "onResponse: "+response.toString());

                        try {
                            if(response.getJSONObject("data").getBoolean("success")!=true){
                                // Set Preferences : Save Data
//                                JSONObject data =  response.getJSONObject("data").getJSONObject("data");
//                                SharedPreferences preferences = getSharedPreferences(Links.perfName, Context.MODE_PRIVATE);
//                                SharedPreferences.Editor editor = preferences.edit();
//                                editor.putString("user_id",data.getString("user_id"));
//                                editor.putString("name",data.getString("name"));
//                                editor.putString("email",data.getString("email"));
//                                editor.putString("mobile_no",data.getString("mobile_no"));
//                                editor.putString("address",data.getString("address"));
//                                editor.putString("logged","true");

                                SharedPreferences preferences = getSharedPreferences(Links.perfName, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("logged","true");

                                editor.commit();
                                // Go to Home Page
                                Intent i = new Intent(RegistrationPage.this,MainScreen.class);
                                startActivity(i);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Registration Error",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getApplicationContext(),"Check the internet connection",Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String,String>();

                param.put("name",name.getText().toString());
                param.put("mobile_no",mobileNo.getText().toString());
                param.put("address",address.getText().toString());
                param.put("password",password.getText().toString());
                param.put("email",email.getText().toString());
                return param;
            }
        };
        queue.add(jsonObjectRequest);
    }
}
