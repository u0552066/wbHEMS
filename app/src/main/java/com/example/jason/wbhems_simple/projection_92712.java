package com.example.jason.wbhems_simple;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class projection_92712 extends Activity {

    String url = "http://140.116.163.19:10107/epslab_ems/api/appliance.php";
    String url_action = "http://140.116.163.19:10107/epslab_ems/action.php";
    private RequestQueue requestQueue;
    private StringRequest postRequest;
    private LinearLayout linearLayout_back;
    private TextView tv_projection;
    private Button btn_On, btn_Ready, btn_Leave, btn_Source, btn_Up, btn_Down;
    private SharedPreferences setting;
    private SharedPreferences.Editor settingedit;
    final static int COUNTS = 2;// 点击次数
    final static long DURATION = 500;// 规定有效时间
    long[] mHits = new long[COUNTS];
    String User,Token,IP,Port;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projection_92712);
        requestQueue = Volley.newRequestQueue(this);
        linearLayout_back = findViewById(R.id.projection_back);
        tv_projection = findViewById(R.id.projection_staust);
        btn_On = findViewById(R.id.btn_on);
        btn_Ready = findViewById(R.id.btn_ready);
        btn_Leave = findViewById(R.id.btn_leave);
        btn_Source = findViewById(R.id.btn_source);
        btn_Up = findViewById(R.id.btn_up);
        btn_Down = findViewById(R.id.btn_down);
        setting = this.getSharedPreferences("auto",0);
        User = setting.getString("User", "");
        Token = setting.getString("TOKEN","");
        IP = setting.getString("IP","");
        Port = setting.getString("Port","");
        getHemsApplianceByName();
        linearLayout_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reit = new Intent();
                reit.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                reit.setClass(projection_92712.this,MainActivity.class);
                startActivity(reit);
                projection_92712.this.finish();
            }
        });
        btn_On.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    setProjection("projection","1");
                    actionProjection("1","1");
                    getHemsApplianceByName();
            }
        });
        btn_Ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continuousClick(COUNTS, DURATION);
            }
        });
        btn_Leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionProjection("1","6");
            }
        });
        btn_Source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionProjection("1","5");
            }
        });
        btn_Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionProjection("1","3");
            }
        });
        btn_Down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionProjection("1","4");
            }
        });
    }
    private void getHemsApplianceByName() {
        final JSONObject body = new JSONObject();
        try {
            body.put("action", "getHemsAppliance");
            body.put("field","92712");
            body.put("name","projector_1");
            body.put("token",Token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("projector_body", body.toString());
        postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    Log.d("92712，projector", object.toString());
                    JSONObject data = object.getJSONObject("data");
                    //投影機
                    String sw_projection = data.getJSONArray("list").getJSONObject(0).getString("switch");
                    if(sw_projection.equals("1")){
                        tv_projection.setText("目前為開啟狀態");
                    } else{
                        tv_projection.setText("目前為關閉狀態");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error_getAppliance", error.toString());
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
    private void actionProjection(final String address, final String cmd){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_action,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("actionProjection",response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("Tag", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name","Projector");
                params.put("address",address);
                params.put("command",cmd);
                params.put("ip",IP);
                params.put("port",Port);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    private void setProjection(String name, String sw) {
        final JSONObject body = new JSONObject();
        try {
            body.put("action", "setHemsAppliance");
            body.put("field",User);
            body.put("name",name);
            body.put("switch",sw);
            body.put("token",Token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("cmd",body.toString());
                Log.d("setProjection_respone", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError_Projection", error.toString());
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
    private void continuousClick(int count, long time) {
        //每次点击时，数组向前移动一位
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
        //为数组最后一位赋值
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();
        if (mHits[0] >= (SystemClock.uptimeMillis() - DURATION)) {
            mHits = new long[COUNTS];//重新初始化数组
            //Toast.makeText(this, "連點了2次", Toast.LENGTH_SHORT).show();
            setProjection("projection","0");
            actionProjection("1","0");
            getHemsApplianceByName();
        }else {
            setProjection("projection","0");
            getHemsApplianceByName();
        }
    }

}
