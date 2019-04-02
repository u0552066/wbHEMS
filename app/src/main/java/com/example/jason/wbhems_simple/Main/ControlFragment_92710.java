package com.example.jason.wbhems_simple.Main;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jason.wbhems_simple.Control.ComfortSettingFragment;
import com.example.jason.wbhems_simple.Control.DimSettingFragment;
import com.example.jason.wbhems_simple.Control.MotionSettingFragment;
import com.example.jason.wbhems_simple.Control.activity_schedule;
import com.example.jason.wbhems_simple.R;
import com.example.jason.wbhems_simple.ScrollableSeekBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ControlFragment_92710#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ControlFragment_92710 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String url_room = "http://140.116.163.19:10107/epslab_ems/api/room.php";
    String plug_ur1 = "http://140.116.163.19:10107/epslab_ems/api/plug.php";
    String url = "http://140.116.163.19:10107/epslab_ems/api/appliance.php";
    String url_action = "http://140.116.163.19:10107/epslab_ems/action.php";
    private RequestQueue requestQueue;
    private StringRequest postRequest;
    private SharedPreferences setting;
    private SharedPreferences.Editor settingedit;
    String User,Token,IP,Port;
    //---------
    //----------
    private int acmode1 = 0, acmode2 = 0, fanmode1 = 0, exhaustmode1 = 0, plugmode1 = 0;
    private LinearLayout LLqset, LLHqset, LLlight, LLHlight, LLac, LLHac,LLfan,LLHfan,LLexhaust,LLHexhaust;
    private TextView tvCount1, tvCount2, tvCount3,tvCount4,tvCount5,tvCount6;
    private Button btn_Ac_1, btn_Fan_1, btn_Exhaust_1; // 開/關
    private Button btn_FanWind1_high, btn_FanWind1_mid, btn_FanWind1_low,btn_Turn_1; // 風扇風度and旋轉按鈕
    private Button btn_ExhaustWind1_high, btn_ExhaustWind1_mid, btn_ExhaustWind1_low; //排風扇風度 按鈕
    private TextView tvFanMode_1,tvFanWind_1; // 風扇模式and風度 文字
    private TextView tvExhaustMode_1,tvExhaustWind_1; // 排風扇模式and風度 文字
    private Button btn_Cold_1,btn_Wet_1,btn_Wind_1; //空調模式 按鈕
    private TextView tvLight_Lux1,tvLight_Lux2,tvAcMode_1,tvAcFeatures_1,tvAc_Temp1; // 空調and燈光文字資訊
    private Switch sw_Light_1, sw_Light_2;
    private ScrollableSeekBar sb_Light_1, sb_Light_2, sb_Ac_1;
    private Switch sw_Auto_Comfort,sw_Auto_Light,sw_Motion;
    private ImageView img_All_Light_On, img_All_Light_Off, img_All_Ac_On, img_All_Ac_Off, img_All_Fan_On, img_All_Fan_Off;
    private ImageView img_Auto_Comfort,img_Auto_Light,img_Motion;
    private ImageView img_Light_1, img_Light_2, img_Ac_1, img_Fan_1, img_Exhaust_1;
    //----
    private String tv_lux1,tv_lux2; // 燈光亮度
    private String tv_temp1; // 空調溫度
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ControlFragment_92710() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ControlFragment_92702.
     */
    // TODO: Rename and change types and number of parameters
    public static ControlFragment_92710 newInstance(String param1, String param2) {
        ControlFragment_92710 fragment = new ControlFragment_92710();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_control_92710, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestQueue = Volley.newRequestQueue(getActivity());
        setting = this.getActivity().getSharedPreferences("auto",0);
        User = setting.getString("User", "");
        Token = setting.getString("TOKEN","");
        IP = setting.getString("IP","");
        Port = setting.getString("Port","11000");
        settingedit = setting.edit();
        img_findViewById();
        sw_findViewById();
        light_findViewById();
        ac_findViewById();
        fan_findViewById();
        exhaust_findViewById();
        getRoom(); // 快速操作API
        getHemsApplianceByName(); //抓各電器設備API
    }

    private void getHemsApplianceByName() {
        final JSONObject body = new JSONObject();
        try {
            body.put("action", "getHemsAppliance");
            body.put("field","92710");
            body.put("name","");
            body.put("token",Token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("getHemsAppliance_body", body.toString());
        postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    Log.d("92710，getHemsAppliance", object.toString());
                    JSONObject data = object.getJSONObject("data");
                    //------------ 燈光
                    String sw_light1 = data.getJSONArray("list").getJSONObject(0).getString("switch"); // 前排
                    String sw_light2 = data.getJSONArray("list").getJSONObject(1).getString("switch"); // 後排
                    tv_lux1 = data.getJSONArray("list").getJSONObject(0).getString("setting");
                    tv_lux2 = data.getJSONArray("list").getJSONObject(1).getString("setting");
                    if(sw_light1.equals("1")){
                        img_Light_1.setImageResource(R.drawable.light_on);
                        sw_Light_1.setChecked(true);
                        tvLight_Lux1.setText(tv_lux1);
                        sb_Light_1.setProgress(Integer.parseInt(tv_lux1)-2);
                    }else{
                        img_Light_1.setImageResource(R.drawable.light_off);
                        sw_Light_1.setChecked(false);
                        tvLight_Lux1.setText(tv_lux1);
                        sb_Light_1.setProgress(Integer.parseInt(tv_lux1)-2);
                    }
                    if(sw_light2.equals("1")){
                        img_Light_2.setImageResource(R.drawable.light_on);
                        sw_Light_2.setChecked(true);
                        tvLight_Lux2.setText(tv_lux2);
                        sb_Light_2.setProgress(Integer.parseInt(tv_lux2)-2);
                    }else{
                        img_Light_2.setImageResource(R.drawable.light_off);
                        sw_Light_2.setChecked(false);
                        tvLight_Lux2.setText(tv_lux2);
                        sb_Light_2.setProgress(Integer.parseInt(tv_lux2)-2);
                    }
                    //空調
                    String sw_ac1 = data.getJSONArray("list").getJSONObject(2).getString("switch");
                    tv_temp1 = data.getJSONArray("list").getJSONObject(2).getString("setting"); // 0:送風，1:除濕，其他:冷氣溫度
                    if(sw_ac1.equals("1")){
                        acmode1 = 1;
                        tvAcMode_1.setText("電源 : 開");
                        img_Ac_1.setImageResource(R.drawable.air_conditioner_on);
                    }else{
                        acmode1 = 0;
                        tvAcMode_1.setText("電源 : 關");
                        img_Ac_1.setImageResource(R.drawable.air_conditioner_off);
                    }
                    if(tv_temp1.equals("0")){
                        tvAcFeatures_1.setText("模式 : 送風");
                        tvAc_Temp1.setText("");
                        sb_Ac_1.setVisibility(View.GONE);
                    }else if(tv_temp1.equals("1")){
                        tvAcFeatures_1.setText("模式 : 除濕");
                        tvAc_Temp1.setText("");
                        sb_Ac_1.setVisibility(View.GONE);
                    }else{
                        tvAcFeatures_1.setText("模式 : 冷氣");
                        tvAc_Temp1.setText(tv_temp1);
                        sb_Ac_1.setVisibility(View.VISIBLE);
                        sb_Ac_1.setProgress(Integer.parseInt(tv_temp1)-18);
                    }
                    //----------- 風扇
                    String sw_fan1 = data.getJSONArray("list").getJSONObject(3).getString("switch");
                    String tv_mode1 = data.getJSONArray("list").getJSONObject(3).getString("setting");
                    if(sw_fan1.equals("1")){
                        fanmode1 = 1;
                        tvFanMode_1.setText("電源 : 開");
                        img_Fan_1.setImageResource(R.drawable.fan_on);
                    }else{
                        fanmode1 = 0;
                        tvFanMode_1.setText("電源 : 關");
                        img_Fan_1.setImageResource(R.drawable.fan_off);
                    }
                    switch(tv_mode1){
                        case "0":
                            tvFanWind_1.setText("風度：弱");
                            break;
                        case "1":
                            tvFanWind_1.setText("風度：中");
                            break;
                        case "2":
                            tvFanWind_1.setText("風度：強");
                            break;
                    }
                    //----------- 排風扇
                    String sw_exhaust1 = data.getJSONArray("list").getJSONObject(4).getString("switch");
                    String tv_exhaust1 = data.getJSONArray("list").getJSONObject(4).getString("setting");
                    if(sw_exhaust1.equals("1")){
                        exhaustmode1 = 1;
                        //tvExhaustMode_1.setText("电源 : 开");
                        tvExhaustMode_1.setText("電源 : 開");
                        img_Exhaust_1.setImageResource(R.drawable.exhaust_on);
                    }else{
                        exhaustmode1 = 0;
                        //tvExhaustMode_1.setText("电源 : 关");
                        tvExhaustMode_1.setText("電源 : 關");
                        img_Exhaust_1.setImageResource(R.drawable.exhaust_off);
                    }
                    switch(tv_exhaust1){
                        case "0":
                            tvExhaustWind_1.setText("風度：弱");
                            break;
                        case "1":
                            tvExhaustWind_1.setText("風度：中");
                            break;
                        case "2":
                            tvExhaustMode_1.setText("風度：強");
                            break;
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

    private void getRoom() {
        final JSONObject body = new JSONObject();
        try {
            body.put("action", "getHemsAutoControl");
            //body.put("name","");
            body.put("field","92710");
            body.put("token",Token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        postRequest = new StringRequest(Request.Method.POST, url_room, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    Log.d("92710，getRoom", object.toString());
                    JSONObject data = object.getJSONObject("data");
                    String auto_comfort = data.getJSONArray("list").getJSONObject(0).getString("switch"); //自動舒適度
                    String auto_light = data.getJSONArray("list").getJSONObject(1).getString("switch"); //自動調光
                    String motion_detector = data.getJSONArray("list").getJSONObject(2).getString("switch"); // 門口
                    if(auto_comfort.equals("1")){
                        sw_Auto_Comfort.setChecked(true);
                    }else{
                        sw_Auto_Comfort.setChecked(false);
                    }
                    if(auto_light.equals("1")){
                        sw_Auto_Light.setChecked(true);
                    }else{
                        sw_Auto_Light.setChecked(false);
                    }
                    if (motion_detector.equals("1")){
                        sw_Motion.setChecked(true);
                    }else{
                        sw_Motion.setChecked(false);
                    }
                    //----
                    String comfort_level = data.getJSONArray("list").getJSONObject(0).getString("setting_value");
                    settingedit.putString("92710_comfort",comfort_level).commit();
                    String motion_offtime = data.getJSONArray("list").getJSONObject(2).getString("setting_value");
                    settingedit.putFloat("92710_motion_offtime", Float.parseFloat(motion_offtime)).commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError_getRoom", error.toString());
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
    // 各電器開關
    private void setApplianceByName_switch(String name, String sw) {
        final JSONObject body = new JSONObject();
        try {
            body.put("action", "setHemsAppliance");
            body.put("field","92710");
            body.put("mode","control");
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
                Log.d("setAppliance_respone", response);
                getHemsApplianceByName();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError_setSwitch", error.toString());
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
    // 設定各電器模式or值
    private void setApplianceByName_setting(String name, String setting) {
        final JSONObject body = new JSONObject();
        try {
            body.put("action", "setHemsAppliance");
            body.put("field","92710");
            body.put("mode","control");
            body.put("name",name);
            body.put("setting",setting);
            body.put("token",Token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("cmd",body.toString());
                Log.d("setAppliance_respone", response);
                getHemsApplianceByName();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError_setting", error.toString());
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
    //風扇旋轉
    private void setFan_turn(String name){
        final JSONObject body = new JSONObject();
        try {
            body.put("action", "setHemsAppliance");
            body.put("field","92710");
            body.put("mode","control");
            body.put("name",name);
            body.put("token",Token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("FanTurn_body",body.toString());
                Log.d("setFanTurn_respone", response);
                getHemsApplianceByName();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError_FanTurn", error.toString());
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
    //排風扇
    private void exhaust_findViewById() {
        img_Exhaust_1 = getView().findViewById(R.id.img_exhaust_1);
        btn_Exhaust_1 = getView().findViewById(R.id.btn_exhaust_1);
        btn_ExhaustWind1_high = getView().findViewById(R.id.btn_exhaustwind1_high);
        btn_ExhaustWind1_mid = getView().findViewById(R.id.btn_exhaustwind1_mid);
        btn_ExhaustWind1_low = getView().findViewById(R.id.btn_exhaustwind1_low);
        tvExhaustMode_1 = getView().findViewById(R.id.tv_exhaustmode_1);
        tvExhaustWind_1 = getView().findViewById(R.id.tv_exhaustwind_1);
        //排風扇開/關
        btn_Exhaust_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(exhaustmode1 == 0){
                    exhaustmode1 = 1;
                    //tvExhaustMode_1.setText("电源 : 开");
                    tvExhaustMode_1.setText("電源 : 開");
                    img_Exhaust_1.setImageResource(R.drawable.exhaust_on);
                    setApplianceByName_switch("air_ejector_fan_1","1");
                    closeAutoControl("air_ejector_fan_1-button","0","auto_comfort");
                    action("ExhaustFan","3","1");
                }
                else{
                    exhaustmode1 = 0;
                    //tvExhaustMode_1.setText("电源 : 关");
                    tvExhaustMode_1.setText("電源 : 關");
                    img_Exhaust_1.setImageResource(R.drawable.exhaust_off);
                    setApplianceByName_switch("air_ejector_fan_1","0");
                    closeAutoControl("air_ejector_fan_1-button","0","auto_comfort");
                    action("ExhaustFan","3","0");
                }
            }
        });
        //排風扇風度
        btn_ExhaustWind1_high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // tvExhaustWind_1.setText("风度：强");
                tvExhaustWind_1.setText("風度：強");
                setApplianceByName_setting("air_ejector_fan_1","2");
                closeAutoControl("air_ejector_fan_1-slider","0","auto_comfort");
                action("ExhaustFan","3","4");
            }
        });
        btn_ExhaustWind1_mid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // tvExhaustWind_1.setText("风度：中");
                tvExhaustWind_1.setText("風度：中");
                setApplianceByName_setting("air_ejector_fan_1","1");
                closeAutoControl("air_ejector_fan_1-slider","0","auto_comfort");
                action("ExhaustFan","3","3");
            }
        });
        btn_ExhaustWind1_low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvExhaustWind_1.setText("风度：弱");
                tvExhaustWind_1.setText("風度：弱");
                setApplianceByName_setting("air_ejector_fan_1","0");
                closeAutoControl("air_ejector_fan_1-slider","0","auto_comfort");
                action("ExhaustFan","3","2");
            }
        });
    }
    //風扇
    private void fan_findViewById() {
        img_Fan_1 = getView().findViewById(R.id.img_fan_1);
        btn_Fan_1 = getView().findViewById(R.id.btn_fan_1);
        btn_Turn_1 = getView().findViewById(R.id.btn_turn_1);
        btn_FanWind1_high = getView().findViewById(R.id.btn_fanwind1_high);
        btn_FanWind1_mid = getView().findViewById(R.id.btn_fanwind1_mid);
        btn_FanWind1_low = getView().findViewById(R.id.btn_fanwind1_low);
        tvFanMode_1 =getView().findViewById(R.id.tv_fanmode_1);
        tvFanWind_1 = getView().findViewById(R.id.tv_fanwind_1);
        //風扇開/關
        btn_Fan_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fanmode1 == 0){
                    fanmode1 = 1;
                    //tvFanMode_1.setText("电源 : 开");
                    tvFanMode_1.setText("電源 : 開");
                    img_Fan_1.setImageResource(R.drawable.fan_on);
                    setApplianceByName_switch("fan_1","1");
                    closeAutoControl("fan_1-button","0","auto_comfort");
                    action("Fan","6","1");
                }
                else{
                    fanmode1 = 0;
                   // tvFanMode_1.setText("电源 : 关");
                    tvFanMode_1.setText("電源 : 關");
                    img_Fan_1.setImageResource(R.drawable.fan_off);
                    setApplianceByName_switch("fan_1","0");
                    closeAutoControl("fan_1-button","0","auto_comfort");
                    action("Fan","6","0");
                }
            }
        });
        //風扇旋轉
        btn_Turn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFan_turn("fan_1");
            }
        });
        //風扇風度
        btn_FanWind1_high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvFanWind_1.setText("风度：强");
                tvFanWind_1.setText("風度：強");
                setApplianceByName_setting("fan_1","2");
                closeAutoControl("fan_1-slider","0","auto_comfort");
                action("Fan","6","4");
            }
        });
        btn_FanWind1_mid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvFanWind_1.setText("风度：中");
                tvFanWind_1.setText("風度：中");
                setApplianceByName_setting("fan_1","1");
                closeAutoControl("fan_1-slider","0","auto_comfort");
                action("Fan","6","3");
            }
        });
        btn_FanWind1_low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvFanWind_1.setText("风度：弱");
                tvFanWind_1.setText("風度：弱");
                setApplianceByName_setting("fan_1","0");
                closeAutoControl("fan_1-slider","0","auto_comfort");
                action("Fan","6","2");
            }
        });
    }
    //空調
    private void ac_findViewById() {
        tvAcMode_1 = getView().findViewById(R.id.tv_acmode_1);
        tvAc_Temp1 = getView().findViewById(R.id.tv_ac_temp1);
        tvAcFeatures_1 =getView().findViewById(R.id.tv_Features_1);
        img_Ac_1 = getView().findViewById(R.id.img_ac_1);
        btn_Ac_1 = getView().findViewById(R.id.btn_ac_1);
        btn_Cold_1 = getView().findViewById(R.id.btn_cold_1);
        btn_Wet_1 = getView().findViewById(R.id.btn_wet_1);
        btn_Wind_1 = getView().findViewById(R.id.btn_wind_1);
        sb_Ac_1 = getView().findViewById(R.id.sb_ac_1);
        //開關
        btn_Ac_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(acmode1 == 0){
                    acmode1 = 1;
                    //tvAcMode_1.setText("电源 : 开");
                    tvAcMode_1.setText("電源 : 開");
                    img_Ac_1.setImageResource(R.drawable.air_conditioner_on);
                    setApplianceByName_switch("ac_1","1");
                    closeAutoControl("ac_1-button","0","auto_comfort");
                    action("AirConditioner","3","1");
                }
                else{
                    acmode1 = 0;
                    //tvAcMode_1.setText("电源 : 关");
                    tvAcMode_1.setText("電源 : 關");
                    img_Ac_1.setImageResource(R.drawable.air_conditioner_off);
                    setApplianceByName_switch("ac_1","0");
                    closeAutoControl("ac_1-button","0","auto_comfort");
                    action("AirConditioner","3","0");
                }
            }
        });
        //空調模式
        btn_Cold_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvAcFeatures_1.setText("模式：冷气");
                tvAcFeatures_1.setText("模式：冷氣");
                setApplianceByName_setting("ac_1",tv_temp1);
                closeAutoControl("ac_1-status-button","0","auto_comfort");
                action("AirConditioner","3",tv_temp1);
            }
        });
        btn_Wet_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvAcFeatures_1.setText("模式：除湿");
                tvAcFeatures_1.setText("模式：除濕");
                setApplianceByName_setting("ac_1","1");
                closeAutoControl("ac_1-status-button","0","auto_comfort");
                action("AirConditioner","3","3");
            }
        });
        btn_Wind_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvAcFeatures_1.setText("模式：送风");
                tvAcFeatures_1.setText("模式：送風");
                setApplianceByName_setting("ac_1","0");
                closeAutoControl("ac_1-status-button","0","auto_comfort");
                action("AirConditioner","3","2");
            }
        });
        sb_Ac_1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvAc_Temp1.setText(String.valueOf(progress+18));
                tv_temp1 = String.valueOf(progress+18);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setApplianceByName_setting("ac_1",tv_temp1);
                closeAutoControl("ac_1-status-button","0","auto_comfort");
                action("AirConditioner","3",tv_temp1);
            }
        });
    }
    //燈光
    private void light_findViewById() {
        tvLight_Lux1 = getView().findViewById(R.id.tv_light_lux1);
        tvLight_Lux2 = getView().findViewById(R.id.tv_light_lux2);
        img_Light_1 = getView().findViewById(R.id.img_light_1);
        img_Light_2 = getView().findViewById(R.id.img_light_2);
        sw_Light_1 = getView().findViewById(R.id.sw_light_1);
        sw_Light_2 = getView().findViewById(R.id.sw_light_2);
        sb_Light_1 = getView().findViewById(R.id.sb_light_1);
        sb_Light_2 = getView().findViewById(R.id.sb_light_2);
        //開關
        sw_Light_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sw_Light_1.isChecked()){
                    img_Light_1.setImageResource(R.drawable.light_on);
                    tvLight_Lux1.setText(tv_lux1);
                    setApplianceByName_switch("light_1","1");
                    closeAutoControl("light_1-button","0","auto_dimming");
                    action("DimmerControl","71","1");
                }
                else {
                    img_Light_1.setImageResource(R.drawable.light_off);
                    tvLight_Lux1.setText(tv_lux1);
                    setApplianceByName_switch("light_1","0");
                    closeAutoControl("light_1-button","0","auto_dimming");
                    action("DimmerControl","71","0");
                }
            }
        });
        sw_Light_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sw_Light_2.isChecked()){
                    img_Light_2.setImageResource(R.drawable.light_on);
                    tvLight_Lux2.setText(tv_lux2);
                    setApplianceByName_switch("light_2","1");
                    closeAutoControl("light_2-button","0","auto_dimming");
                    action("DimmerControl","72","1");
                }
                else {
                    img_Light_2.setImageResource(R.drawable.light_off);
                    tvLight_Lux2.setText(tv_lux2);
                    setApplianceByName_switch("light_2","0");
                    closeAutoControl("light_2-button","0","auto_dimming");
                    action("DimmerControl","72","0");
                }
            }
        });
        //SeekBar
        sb_Light_1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvLight_Lux1.setText(String.valueOf(progress+2));
                tv_lux1 = String.valueOf(progress+2);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setApplianceByName_setting("light_1",tv_lux1);
                closeAutoControl("light_1-slider","0","auto_dimming");
                action("DimmerControl","71",tv_lux1);
            }
        });
        sb_Light_2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvLight_Lux2.setText(String.valueOf(progress+2));
                tv_lux2 = String.valueOf(progress+2);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setApplianceByName_setting("light_2",tv_lux2);
                closeAutoControl("light_2-slider","0","auto_dimming");
                action("DimmerControl","72",tv_lux2);
            }
        });
    }

    private void sw_findViewById() {
        sw_Auto_Comfort = getView().findViewById(R.id.sw_auto_comfort);
        sw_Auto_Light = getView().findViewById(R.id.sw_auto_light);
        sw_Motion = getView().findViewById(R.id.sw_motion);
        //自動舒適度
        sw_Auto_Comfort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sw_Auto_Comfort.isChecked()){
                    //Toast.makeText(getActivity(),"自动舒适开",Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(),"自動舒適開",Toast.LENGTH_SHORT).show();
                    setAutoRoomHotKey("auto_comfort","1");
                }
                else {
                    //Toast.makeText(getActivity(),"自动舒适关",Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(),"自動舒適關",Toast.LENGTH_SHORT).show();
                    setAutoRoomHotKey("auto_comfort","0");
                }
            }
        });
        sw_Auto_Light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sw_Auto_Light.isChecked()){
                    //Toast.makeText(getActivity(),"自动调光开",Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(),"自動調光開",Toast.LENGTH_SHORT).show();
                    setAutoRoomHotKey(" auto_dimming","1");
                }
                else {
                    //Toast.makeText(getActivity(),"自动调光关",Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(),"自動調光關",Toast.LENGTH_SHORT).show();
                    setAutoRoomHotKey(" auto_dimming","0");
                }
            }
        });
        sw_Motion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sw_Motion.isChecked()){
                    //Toast.makeText(getActivity(),"移动感测器开",Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(),"移動感測器開",Toast.LENGTH_SHORT).show();
                    setMotion_switch("motion_detector","1");
                    setMotion_hotkey("motion_detector","1");
                }
                else {
                    //Toast.makeText(getActivity(),"移动感测器关",Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(),"移動感測器關",Toast.LENGTH_SHORT).show();
                    setMotion_switch("motion_detector","0");
                    setMotion_hotkey("motion_detector","0");
                }
            }
        });
    }
    private void img_findViewById(){
        img_All_Light_On = getView().findViewById(R.id.img_all_light_on);
        img_All_Light_Off = getView().findViewById(R.id.img_all_light_off);
        img_All_Ac_On = getView().findViewById(R.id.img_all_ac_on);
        img_All_Ac_Off = getView().findViewById(R.id.img_all_ac_off);
        img_All_Fan_On = getView().findViewById(R.id.img_all_fan_on);
        img_All_Fan_Off = getView().findViewById(R.id.img_all_fan_off);
        img_Auto_Comfort = getView().findViewById(R.id.img_auto_comfort);
        img_Auto_Light = getView().findViewById(R.id.img_auto_light);
        img_Motion = getView().findViewById(R.id.img_motion);
        LLqset = getView().findViewById(R.id.layout_qset);
        LLHqset = getView().findViewById(R.id.layout_hide_qset);
        LLlight = getView().findViewById(R.id.layout_light);
        LLHlight = getView().findViewById(R.id.layout_hide_light);
        LLac = getView().findViewById(R.id.layout_ac);
        LLHac = getView().findViewById(R.id.layout_hide_ac);
        LLfan = getView().findViewById(R.id.layout_fan);
        LLHfan = getView().findViewById(R.id.layout_hide_fan);
        LLexhaust = getView().findViewById(R.id.layout_exhaust);
        LLHexhaust = getView().findViewById(R.id.layout_hide_exhaust);
        tvCount1 = getView().findViewById(R.id.tv_count1);
        tvCount2 = getView().findViewById(R.id.tv_count2);
        tvCount3 = getView().findViewById(R.id.tv_count3);
        tvCount4 = getView().findViewById(R.id.tv_count4);
        tvCount5 = getView().findViewById(R.id.tv_count5);
        tvCount6 = getView().findViewById(R.id.tv_count6);
        //顯示or隱藏
        LLqset.setOnClickListener(new View.OnClickListener() {
            boolean visible;
            @Override
            public void onClick(View v) {
                visible = !visible;
                LLHqset.setVisibility(visible ? View.VISIBLE : View.GONE);
                tvCount1.setText(visible ? "-" : "+");
            }
        });
        LLlight.setOnClickListener(new View.OnClickListener() {
            boolean visible;
            @Override
            public void onClick(View v) {
                visible = !visible;
                LLHlight.setVisibility(visible ? View.VISIBLE : View.GONE);
                tvCount2.setText(visible ? "-" : "+");
            }
        });
        LLac.setOnClickListener(new View.OnClickListener() {
            boolean visible;
            @Override
            public void onClick(View v) {
                visible = !visible;
                LLHac.setVisibility(visible ? View.VISIBLE : View.GONE);
                tvCount3.setText(visible ? "-" : "+");
            }
        });
        LLfan.setOnClickListener(new View.OnClickListener() {
            boolean visible;
            @Override
            public void onClick(View v) {
                visible = !visible;
                LLHfan.setVisibility(visible ? View.VISIBLE : View.GONE);
                tvCount4.setText(visible ? "-" : "+");
            }
        });
        LLexhaust.setOnClickListener(new View.OnClickListener() {
            boolean visible;
            @Override
            public void onClick(View v) {
                visible = !visible;
                LLHexhaust.setVisibility(visible ? View.VISIBLE : View.GONE);
                tvCount5.setText(visible ? "-" : "+");
            }
        });
        //-----
        img_All_Light_On.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(),"灯光全开",Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(),"燈光全開",Toast.LENGTH_SHORT).show();
                setHemsApplianceByName("light","1");
                setHemsRoomHotKey("light","1","auto_dimming");
                action("DimmerControl","71","1");
                action("DimmerControl","72","1");
            }
        });
        img_All_Light_Off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(),"灯光全关",Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(),"燈光全關",Toast.LENGTH_SHORT).show();
                setHemsApplianceByName("light","0");
                setHemsRoomHotKey("light","0","auto_dimming");
                action("DimmerControl","71","0");
                action("DimmerControl","72","0");
            }
        });
        img_All_Ac_On.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(),"空调全开",Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(),"空調全開",Toast.LENGTH_SHORT).show();
                setHemsApplianceByName("ac","1");
                setHemsRoomHotKey("ac","1","auto_comfort");
                action("AirConditioner","3","1");
            }
        });
        img_All_Ac_Off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(),"空调全关",Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(),"空調全關",Toast.LENGTH_SHORT).show();
                setHemsApplianceByName("ac","0");
                setHemsRoomHotKey("ac","0","auto_comfort");
                action("AirConditioner","3","0");
            }
        });
        img_All_Fan_On.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(),"风扇全开",Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(),"風扇全開",Toast.LENGTH_SHORT).show();
                setHemsApplianceByName("fan","1");
                setHemsRoomHotKey("fan","1","auto_comfort");
                action("Fan","6","1");
            }
        });
        img_All_Fan_Off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(),"风扇全关",Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(),"風扇全關",Toast.LENGTH_SHORT).show();
                setHemsApplianceByName("fan","0");
                setHemsRoomHotKey("fan","0","auto_comfort");
                action("Fan","6","0");
            }
        });
        // 自動舒適度設定
        img_Auto_Comfort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingedit.putString("comfort_title","92710").commit();
                FragmentTransaction comfortSetting1 = getFragmentManager().beginTransaction();
                Fragment comfort1 = getFragmentManager().findFragmentByTag("ComfortSettingFragment");
                if (comfort1 != null) {
                    comfortSetting1.remove(comfort1);
                }
                comfortSetting1.addToBackStack(null);
                // Create and show the dialog.
                DialogFragment newFragment_comfort1 = new ComfortSettingFragment();
                newFragment_comfort1.show(comfortSetting1, "ComfortSettingFragment");
            }
        });
        // 自動調光
        img_Auto_Light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingedit.putString("dim_title","92710").commit();
                FragmentTransaction dimSetting1 = getFragmentManager().beginTransaction();
                Fragment dim1 = getFragmentManager().findFragmentByTag("DimSettingFragment");
                if (dim1 != null) {
                    dimSetting1.remove(dim1);
                }
                dimSetting1.addToBackStack(null);
                // Create and show the dialog.
                DialogFragment newFragment_dom1 = new DimSettingFragment();
                newFragment_dom1.show(dimSetting1, "DimSettingFragment");
            }
        });
        // 移動感測器
        img_Motion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingedit.putString("motion_title","92710").commit();
                FragmentTransaction motionSetting = getFragmentManager().beginTransaction();
                Fragment motion = getFragmentManager().findFragmentByTag("MotionSettingFragment");
                if (motion != null) {
                    motionSetting.remove(motion);
                }
                motionSetting.addToBackStack(null);
                // Create and show the dialog.
                DialogFragment newFragment_motion = new MotionSettingFragment();
                newFragment_motion.show( motionSetting, "MotionSettingFragment");
            }
        });
    }

    // 自動調光、舒適度開關副程式
    private void setAutoRoomHotKey(String id, String sw){
        final JSONObject body = new JSONObject();
        try {
            body.put("action", "setHemsRoomHotKey");
            body.put("field","92710");
            body.put("id",id);
            body.put("switch",sw);
            body.put("token",Token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        postRequest = new StringRequest(Request.Method.POST, url_room, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RoomHotKey_autoSwitch", response);
                getHemsApplianceByName();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError_autoSwitch", error.toString());
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
    // 移動感測器開關
    private void setMotion_switch(String name, String sw){
        final JSONObject body = new JSONObject();
        try {
            body.put("action","setHemsAppliance");
            body.put("field","92710");
            body.put("mode","control");
            body.put("name",name);
            body.put("setting","");
            body.put("switch",sw);
            body.put("token",Token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("setMotion_respone", response);
                getHemsApplianceByName();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError_motion", error.toString());
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
    // 移動感測器HotKey
    private void setMotion_hotkey(String id, String sw){
        final JSONObject body = new JSONObject();
        try {
            body.put("action", "setHemsRoomHotKey");
            body.put("auto_control","");
            body.put("field","92710");
            body.put("id",id);
            body.put("mode","hotkey");
            body.put("switch",sw);
            body.put("token",Token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        postRequest = new StringRequest(Request.Method.POST, url_room, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("MotionHotKey_autoSwitch", response);
                getHemsApplianceByName();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError_autoSwitch", error.toString());
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

    // 一鍵全開or關副程式1
    private void setHemsApplianceByName(String name, String sw) {
        final JSONObject body = new JSONObject();
        try {
            body.put("field","92710");
            body.put("action", "setHemsAppliance");
            body.put("name",name);
            body.put("stting","");
            body.put("token",Token);
            body.put("switch",sw);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //JSONObject object = new JSONObject(response);
                Log.d("ApplianceByName", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError_Appliance", error.toString());
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
    // 一鍵全開or關副程式2
    private void setHemsRoomHotKey(String id, String sw, String auto_control) {
        final JSONObject body = new JSONObject();
        try {
            body.put("action", "setHemsRoomHotKey");
            body.put("field","92710");
            body.put("id",id);
            body.put("switch",sw);
            body.put("token",Token);
            body.put("mode","hotkey");
            body.put("auto_control",auto_control);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("HotKey_body",body.toString());
        postRequest = new StringRequest(Request.Method.POST, url_room, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RoomHotKey", response);
                getHemsApplianceByName();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError_HotKey", error.toString());
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

    // 控制電燈開關或是調整亮度時，關閉該區域的自動調光功能
    // 操作空調、風扇、排氣扇時，關閉該區域的自動舒適度功能
    private void closeAutoControl(String id, String sw, String auto_control) {
        final JSONObject body = new JSONObject();
        try {
            body.put("action", "setHemsRoomHotKey");
            body.put("auto_control",auto_control);
            body.put("field","92710");
            body.put("from_function","applianceControl");
            body.put("id",id);
            body.put("switch",sw);
            body.put("token",Token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("closeAutoControl_body",body.toString());
        postRequest = new StringRequest(Request.Method.POST, url_room, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("closeAutoControl", response);
                getHemsApplianceByName();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error_closeAutoControl", error.toString());
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


    private void action(final String name,final String address, final String cmd){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_action,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("action",response);
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
                params.put("name",name);
                params.put("address",address);
                params.put("command",cmd);
                params.put("ip",IP);
                params.put("port",Port);
                Log.d("POST_action",name+"，"+address+"，"+cmd+"，"+IP+"，"+Port);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
