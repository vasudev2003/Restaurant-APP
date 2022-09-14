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
import android.widget.TextView;
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

public class LoginPage extends AppCompatActivity {
    TextView forgotPassword,signUp;
    EditText mobileNo,password;
    Button loginBtn;

    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

            SharedPreferences preferences = getSharedPreferences(Links.perfName, Context.MODE_PRIVATE);
                if(preferences.contains("logged"))
                    if(preferences.getString("logged","-").equals("true"))
                    {
                        Intent i = new Intent(LoginPage.this,MainScreen.class);
                        startActivity(i);
                    }

        queue = Volley.newRequestQueue(getApplicationContext());

        forgotPassword = findViewById(R.id.forgot_password);
        signUp = findViewById(R.id.sign_up);
        mobileNo = findViewById(R.id.mobile_no);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.login_btn);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(LoginPage.this,ForgotPasswordPage.class);
                startActivity(i);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(LoginPage.this,RegistrationPage.class);
                startActivity(i);

            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validation
                if(mobileNo.getText().toString().length()==10 && password.getText().toString().length()>4)
                {
                    login();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Invalid Mobile No",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    void login()
    {
        queue.getCache().clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Links.login,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Login", "onResponse: "+response.toString());

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

                                // Fake
                                SharedPreferences preferences = getSharedPreferences(Links.perfName, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("user_id","123");
                                editor.putString("name","Vasudev");
                                editor.putString("email","VS@gmail.com");
                                editor.putString("mobile_no","7894561236");
                                editor.putString("address","Solapur");
                                editor.putString("logged","true");

//                                SharedPreferences preferences = getSharedPreferences(Links.perfName, Context.MODE_PRIVATE);
//                                SharedPreferences.Editor editor = preferences.edit();
//                                editor.putString("logged","true");

                                editor.commit();
                                // Go to Home Page
                                Intent i = new Intent(LoginPage.this,MainScreen.class);
                                startActivity(i);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Invalid Login",Toast.LENGTH_SHORT).show();
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
                param.put("mobile_no",mobileNo.getText().toString());
                param.put("password",password.getText().toString());
                return param;
            }
        };
        queue.add(jsonObjectRequest);
    }
}
