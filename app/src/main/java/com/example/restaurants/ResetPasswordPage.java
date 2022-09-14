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

public class ResetPasswordPage extends AppCompatActivity {
    EditText otp,password,confPassword;
    Button submitBtn;
    RequestQueue queue;
    String mobileNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_page);

        queue = Volley.newRequestQueue(getApplicationContext());

        otp = findViewById(R.id.otp);
        password = findViewById(R.id.password);
        confPassword = findViewById(R.id.conf_password);
        submitBtn = findViewById(R.id.submit_btn);
        mobileNo = getIntent().getStringExtra("mobile_no");

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validation
                if(otp.getText().toString().length()==4)
                    if(password.getText().toString().length()>=4 &&
                    password.getText().toString().equals(confPassword.getText().toString()))
                    {
                        sumbit();
                    }
                    else
                        Toast.makeText(getApplicationContext(),"Invalid Password",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(),"Invalid OTP",Toast.LENGTH_SHORT).show();
            }
        });

    }
    void sumbit()
    {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Links.restPassword,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("ResetPassword", "onResponse: "+response.toString());

                        try {
                            if(response.getJSONObject("data").getBoolean("success")==true){
                                Toast.makeText(getApplicationContext(),"Password has successfully changed.",Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(ResetPasswordPage.this,LoginPage.class);
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
                param.put("mobile_number",mobileNo);
                param.put("password",password.getText().toString());
                param.put("otp",otp.getText().toString());
                return param;
            }
        };
        queue.add(jsonObjectRequest);
    }
}
