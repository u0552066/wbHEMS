package com.example.jason.wbhems_simple;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChangePWDActivity extends Activity {
    private EditText oldPWD,newPWD,checkPWD;
    private Button pwdReturn,pwdConfirm;
    private RequestQueue requestQueue;
    private StringRequest postRequest;
    private SharedPreferences setting,pref;
    private SharedPreferences.Editor settingedit,editor;
    private String url = "http://140.116.163.19:10107/epslab_ems/api/login.php";
    String User,Token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        oldPWD = (EditText)findViewById(R.id.et_oldpwd);
        newPWD = (EditText)findViewById(R.id.et_newpwd);
        checkPWD = (EditText)findViewById(R.id.et_checkpwd);
        pwdConfirm = (Button)findViewById(R.id.btn_cpwd_confirm);
        pwdReturn = (Button)findViewById(R.id.btn_cpwd_return);
        requestQueue = Volley.newRequestQueue(this);
        setting = this.getSharedPreferences("auto",0);
        User = setting.getString("User", "");
        Token = setting.getString("TOKEN","");
        settingedit = setting.edit();
        pref = this.getSharedPreferences("LoginInfo",0);
        editor = pref.edit();
        pwdReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reit = new Intent();
                reit.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                reit.setClass(ChangePWDActivity.this,MainActivity.class);
                startActivity(reit);
                ChangePWDActivity.this.finish();
            }
        });

        pwdConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final JSONObject body = new JSONObject();
                try {
                    body.put("action", "updateUserPassword");
                    body.put("address",User);
                    body.put("chk",checkPWD.getText().toString());
                    body.put("new",newPWD.getText().toString());
                    body.put("old",oldPWD.getText().toString());
                    body.put("token",Token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            Log.d("change_pwd", object.toString());
                            if(object.getBoolean("status") == true){
                                Toast.makeText(getApplicationContext(), "密碼更改成功", Toast.LENGTH_SHORT).show();
                                editor.putString("UserName","").commit();
                                editor.putString("Password","").commit();
                                Intent reit = new Intent();
                                reit.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                reit.setClass(ChangePWDActivity.this,LoginActivity.class);
                                startActivity(reit);
                                ChangePWDActivity.this.finish();
                            }else {
                                Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                                checkPWD.setText("");
                                newPWD.setText("");
                                oldPWD.setText("");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VolleyError_pwd", error.toString());
                    }
                }) {
                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        return body.toString().getBytes();
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json";
                    }
                };
                requestQueue.add(postRequest);
            }
        });

    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent reit = new Intent();
        reit.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        reit.setClass(ChangePWDActivity.this,MainActivity.class);
        startActivity(reit);
        ChangePWDActivity.this.finish();
        return true;
    }
}
