package com.example.jason.wbhems_simple.DR;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jason.wbhems_simple.Control.ControlQuestionFragment;
import com.example.jason.wbhems_simple.MainActivity;
import com.example.jason.wbhems_simple.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class dr_equipmentActivity extends AppCompatActivity {
    private DrDragListView DRdragListView;
    private ArrayList<String> mDatas;
    private ArrayList<String> powers;
    private ArrayList<String> dollars;
    private ArrayList<Boolean> states;
    private ArrayList<String> id;
    private Button confirm,question;
    private SharedPreferences setting;
    private SharedPreferences.Editor settingedit;
    private RequestQueue requestQueue;
    private StringRequest postRequest;
    int ACisCreated = 0;
    private String TOKEN,User;
    private String url = "http://140.116.163.19:10107/epslab_ems/api/appliance.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dr_equipment);
        DRdragListView = (DrDragListView)findViewById(R.id.lv_equips);
        confirm = (Button)findViewById(R.id.btn_drequip_confirm);
        question = (Button)findViewById(R.id.btn_drequip_question);
        mDatas = new ArrayList<>();
        powers = new ArrayList<>();
        dollars = new ArrayList<>();
        states = new ArrayList<>();
        id = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        setting = getSharedPreferences("auto",0);
        settingedit = setting.edit();
        User = setting.getString("User", "");
        TOKEN = setting.getString("TOKEN","");
        getAppliance();

        DRdragListView.setMyDragListener(new DrDragListView.MyDragListener() {
            @Override
            public void onDragFinish(int srcPositon, int finalPosition,String[] arrayPrio) {
                Log.d("Tag", String.valueOf(arrayPrio.length));
                setDrPriority(arrayPrio[srcPositon].split("_")[0],srcPositon+1);
                setDrPriority(arrayPrio[finalPosition].split("_")[0],finalPosition+1);
               //Log.d("@@@@@@@@", String.valueOf(srcPositon)+"   "+finalPosition+"   "+arrayPrio[0]);
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reit = new Intent();
                reit.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                reit.setClass(dr_equipmentActivity.this,MainActivity.class);
                startActivity(reit);
                dr_equipmentActivity.this.finish();
            }
        });
        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction DrQuestion = getSupportFragmentManager().beginTransaction();
                Fragment Question = getSupportFragmentManager().findFragmentByTag("DRequipQuestionFragment");
                if (Question != null) {
                    DrQuestion.remove(Question);
                }
                DrQuestion.addToBackStack(null);
                // Create and show the dialog.
                DialogFragment newFragment = new DRequipQuestionFragment();
                newFragment.show(DrQuestion, "DRequipQuestionFragment");
            }
        });
    }

    private void getAppliance() {
        final JSONObject body = new JSONObject();
        try {
            body.put("action", "getHemsAppliance");
            body.put("field",User);
            body.put("order","dr_priority");
            body.put("token",TOKEN);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DR_body", body.toString());
                JSONObject object = null;
                try{
                    object = new JSONObject(response);
                    Log.d("Equip:", object.toString());
                    JSONArray list = object.getJSONObject("data").getJSONArray("list");
                    int num = list.length();
                    int priority;
                    double acPower = 0.0;
                    Log.d("length", String.valueOf(num));   //顯示回傳值的資料數
                    for (int i = 0; i<num; i++){
                        Log.d("i", String.valueOf(i));
                        switch (list.getJSONObject(i).getString("name")){ // .split("_")[0]
                            case "light_1":
                                Log.d("if", String.valueOf(i));
                                priority = Integer.parseInt(list.getJSONObject(i).getString("dr_priority"));
                                if(!String.valueOf(priority).equals("0")) {
                                    mDatas.add(priority - 1, list.getJSONObject(i).getString("name").split("_")[0]);
                                    if (list.getJSONObject(i).getString("average_power").equals("null")) {
                                        powers.add(priority - 1, "0.000");
                                    } else {
                                        powers.add(priority - 1, list.getJSONObject(i).getString("average_power"));
                                    }
                                    if (list.getJSONObject(i).getString("sheeding_willing").equals("null")) {
                                        dollars.add(priority - 1, "0");
                                    } else {
                                        dollars.add(priority - 1, list.getJSONObject(i).getString("sheeding_willing"));
                                    }
                                    id.add(priority - 1, list.getJSONObject(i).getString("name"));
                                    if (list.getJSONObject(i).getString("dr_recover").equals("1")) {
                                        states.add(priority - 1, true);
                                    } else {
                                        states.add(priority - 1, false);
                                    }
                                    Log.d("mdatas", String.valueOf(mDatas));
                                }
                                break;
                            case "ac_1":
                                Log.d("if", String.valueOf(i));
                                priority = Integer.parseInt(list.getJSONObject(i).getString("dr_priority"));
                                if(!String.valueOf(priority).equals("0")) {
                                    mDatas.add(priority - 1, list.getJSONObject(i).getString("name").split("_")[0]);

                                    if (list.getJSONObject(i).getString("average_power").equals("null")) {
                                        powers.add(priority - 1, "0.000");
                                    } else {
                                        powers.add(priority - 1, list.getJSONObject(i).getString("average_power"));
                                    }
                                    if (list.getJSONObject(i).getString("sheeding_willing").equals("0")) {
                                        dollars.add(priority - 1, "不參與");
                                    } else {
                                        dollars.add(priority - 1, list.getJSONObject(i).getString("sheeding_willing"));
                                    }
                                    id.add(priority - 1, list.getJSONObject(i).getString("name"));
                                    if (list.getJSONObject(i).getString("dr_recover").equals("1")) {
                                        states.add(priority - 1, true);
                                    } else {
                                        states.add(priority - 1, false);
                                    }
                                    Log.d("mdatas", String.valueOf(mDatas));
                                }
                                break;
                            default:
                                break;
                        }
                    }
                    DRdragListView.setAdapter(new DrDragListAdapter(dr_equipmentActivity.this, mDatas,powers,dollars,states,id));
                }catch (Exception  e) {
                    e.printStackTrace();
                    //Log.d("Control err:", "catch");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError_DRequipment", error.toString());
            }
        }){
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

    private void setDrPriority(String name,int priority){
        final JSONObject body = new JSONObject();
        try {
            body.put("action","setHemsAppliance");
            body.put("field",User);
            body.put("mode","drPriority");
            body.put("name",name);
            body.put("priority",priority);
            body.put("token",TOKEN);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DrPriority_respone", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error_DrPriority", error.toString());
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent reit = new Intent();
        reit.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        reit.setClass(dr_equipmentActivity.this, MainActivity.class);
        startActivity(reit);
        dr_equipmentActivity.this.finish();
        return true;
    }

}
