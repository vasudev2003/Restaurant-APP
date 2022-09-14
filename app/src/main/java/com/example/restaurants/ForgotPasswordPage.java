package com.example.restaurants;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class ForgotPasswordPage extends AppCompatActivity {
    EditText mobileNo,email;
    Button next_btn;
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_page);

        queue = Volley.newRequestQueue(getApplicationContext());

        mobileNo = findViewById(R.id.mobile_no);
        email = findViewById(R.id.email);
        next_btn = findViewById(R.id.next_btn);

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validation
                if(mobileNo.getText().length()==10 && !email.getText().toString().isEmpty())
                {

                    sendOTP();
//
                }
                else
                    Toast.makeText(getApplicationContext(),"Fill Details Correctly",Toast.LENGTH_SHORT).show();

            }
        });
    }
    void sendOTP()
    {
        queue.getCache().clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Links.forgotPassword,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("ForgotPassword", "onResponse: "+response.toString());

                        try {
                            if(response.getJSONObject("data").getBoolean("success")==true){
                                if(response.getJSONObject("data").getBoolean("first_try")==true)
                                    Toast.makeText(getApplicationContext(),"OTP sent",Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(getApplicationContext(),"OTP already sent",Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(ForgotPasswordPage.this, ResetPasswordPage.class);
                                i.putExtra("mobile_no",mobileNo.getText().toString());
                                startActivity(i);
//                                finish();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"No mobile number or email found",Toast.LENGTH_SHORT).show();
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
                param.put("email",email.getText().toString());
                return param;
            }
        };
        queue.add(jsonObjectRequest);
    }
}
