package com.example.jason.wbhems_simple.Control;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jason.wbhems_simple.MainActivity;
import com.example.jason.wbhems_simple.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class activity_schedule extends AppCompatActivity {
    private RequestQueue requestQueue;
    private StringRequest request;
    private LinearLayout hide_linearLayout;
    String server_ur1 = "http://163.18.57.43/sumeeko_api/plug.php";
    private RecyclerView recyclerView;
    private ExampleAdapter exampleAdapter;
    private ArrayList<ExampleItem> exampleList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        hide_linearLayout = findViewById(R.id.hide_layout);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(exampleAdapter);
        exampleList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        getPlugSchedule();
    }

    public void  button_back(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void button_insert (View view){
        Intent intent = new Intent(this, activity_insert_schedule.class);
        startActivity(intent);
    }

    private void getPlugSchedule() {
        final JSONObject body = new JSONObject();
        try {
            body.put("action", "getPlugSchedule");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request = new StringRequest(Request.Method.POST, server_ur1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray object = new JSONArray(response);
                    int num = object.length();
                    Log.d("getPlugSchedule", object.toString());
                    if(num == 0){
                        hide_linearLayout.setVisibility(View.VISIBLE);
                    }
                    for(int i = 0 ;i < num ;i++ ){
                        String id = object.getJSONObject(i).getString("id");
                        String name = "排程"+id;
                        String[] start = object.getJSONObject(i).getString("time").split(" ");
                        String[] start_time = start[1].split(":"); // 時間分割
                        String[] startDay = start[0].split("-");  //日期分割
                        String [] month = startDay[1].split("");  //月 分割
                        String start_day = month[2]+"月"+startDay[2]+"日";
                        String motion = object.getJSONObject(i).getString("motion");
                        String action;
                        if(motion.equals("1")){
                            action = "開";
                        }else {
                            action = "關";
                        }
                        exampleList.add(new ExampleItem(name,start[0]+" "+start_time[0]+":"+start_time[1],start_day , id, action , true));
                        //exampleList.add(new ExampleItem(name,start_time[0]+":"+start_time[1],start_day , id, action , true));
                        exampleAdapter = new ExampleAdapter(activity_schedule.this,exampleList);
                        recyclerView.setAdapter(exampleAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
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
        requestQueue.add(request);
    }
}
