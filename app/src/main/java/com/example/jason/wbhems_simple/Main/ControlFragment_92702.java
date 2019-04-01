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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ControlFragment_92702#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ControlFragment_92702 extends Fragment {
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
    private int acmode1 = 0, acmode2 = 0, acmodeEL = 0, fanmode1 = 0, fanmode2 = 0, fanmode3 = 0, fanmode4 = 0, fanmode5 = 0, fanmode6 = 0, exhaustmode1 = 0, exhaustmode2 = 0, plugmode1 = 0;
    private LinearLayout LLqset, LLHqset, LLlight, LLHlight, LLac, LLHac,LLfan,LLHfan,LLexhaust,LLHexhaust,LLplug,LLHplug;
    private TextView tvCount1, tvCount2, tvCount3,tvCount4,tvCount5,tvCount6;
    private Button btn_Ac_1,btn_Ac_2,btn_Ac_EL,btn_Fan_1,btn_Fan_2,btn_Fan_3,btn_Fan_4,btn_Fan_5,btn_Fan_6,btn_Exhaust_1,btn_Exhaust_2,btn_Plug_1; // 開/關
    private Button btn_FanWind1_high,btn_FanWind1_mid,btn_FanWind1_low,btn_FanWind2_high,btn_FanWind2_mid,btn_FanWind2_low,btn_FanWind3_high,btn_FanWind3_mid,btn_FanWind3_low,
                   btn_FanWind4_high,btn_FanWind4_mid,btn_FanWind4_low,btn_FanWind5_high,btn_FanWind5_mid,btn_FanWind5_low,btn_FanWind6_high,btn_FanWind6_mid,btn_FanWind6_low; // 風扇風度 按鈕
    private Button btn_Turn_1,btn_Turn_2,btn_Turn_3,btn_Turn_4,btn_Turn_5,btn_Turn_6; // 風扇旋轉按鈕
    private Button btn_ExhaustWind1_high, btn_ExhaustWind1_mid, btn_ExhaustWind1_low, btn_ExhaustWind2_high, btn_ExhaustWind2_mid,btn_ExhaustWind2_low; //排風扇風度 按鈕
    private TextView tvFanMode_1,tvFanMode_2,tvFanMode_3,tvFanMode_4,tvFanMode_5,tvFanMode_6,tvFanWind_1,tvFanWind_2,tvFanWind_3,tvFanWind_4,tvFanWind_5,tvFanWind_6; // 風扇模式and風度 文字
    private TextView tvExhaustMode_1,tvExhaustMode_2,tvExhaustWind_1,tvExhaustWind_2; // 排風扇模式and風度 文字
    private Button btn_Cold_1,btn_Wet_1,btn_Wind_1,btn_Cold_2,btn_Wet_2,btn_Wind_2,btn_Cold_EL,btn_Wet_EL,btn_Wind_EL; //空調模式 按鈕
    private TextView tvLight_Lux1,tvLight_Lux2,tvLight_Lux3,tvLight_Lux4,tvLight_Lux5,tvLight_Lux6,tvAcMode_1,tvAcMode_2,tvAcMode_EL,
            tvAcFeatures_1,tvAcFeatures_2,tvAcFeatures_EL,tvAc_Temp1,tvAc_Temp2,tvAc_TempEL; // 空調and燈光文字資訊
    private Button btn_Plugtime_1; //智慧插座定時按鈕
    private TextView tvPlugMode_1,tvPlugPower_1; // 智慧插座文字資訊
    private Switch sw_Light_1, sw_Light_2, sw_Light_3,sw_Light_4,sw_Light_5,sw_Light_6; // 燈光開關
    private ScrollableSeekBar sb_Light_1, sb_Light_2, sb_Light_3,sb_Light_4,sb_Light_5, sb_Light_6,sb_Ac_1, sb_Ac_2, sb_Ac_EL;
    private Switch  sw_Auto_Comfort1,sw_Auto_Comfort2, sw_Auto_Light1, sw_Auto_Light2, sw_Motion;
    private ImageView img_All_Light_On, img_All_Light_Off, img_All_Ac_On, img_All_Ac_Off, img_All_Fan_On, img_All_Fan_Off;
    private ImageView img_Auto_Comfort1,img_Auto_Comfort2, img_Auto_Light1,img_Auto_Light2,img_Motion_1, img_Motion_2, img_Motion_3, img_Motion_4;
    private ImageView img_Light_1, img_Light_2, img_Light_3,img_Light_4,img_Light_5,img_Light_6, img_Ac_1, img_Ac_2, img_Ac_EL, img_Fan_1, img_Fan_2, img_Fan_3, img_Fan_4, img_Fan_5, img_Fan_6, img_Exhaust_1, img_Exhaust_2,img_Plug_1;
    //---------
    private String tv_lux1,tv_lux2,tv_lux3,tv_lux4,tv_lux5,tv_lux6; // 燈光亮度
    private String tv_temp1,tv_temp2,tv_tempEL; // 空調溫度
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ControlFragment_92702() {
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
    public static ControlFragment_92702 newInstance(String param1, String param2) {
        ControlFragment_92702 fragment = new ControlFragment_92702();
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
        return inflater.inflate(R.layout.fragment_control_92702, container, false);
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
        getPlug(); //抓插座API
        getHemsApplianceByName(); //抓各電器設備API
        //智慧插座
        tvPlugMode_1 = getView().findViewById(R.id.tv_plugmode_1);
        tvPlugPower_1 = getView().findViewById(R.id.tv_plugpower_1);
        btn_Plug_1 = getView().findViewById(R.id.btn_plug_1);
        btn_Plugtime_1 = getView().findViewById(R.id.btn_plugtime_1);
        img_Plug_1 = getView().findViewById(R.id.img_plug_1);
        btn_Plug_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(plugmode1 == 0){
                    plugmode1 = 1;
                    //tvPlugMode_1.setText("电源 : 开");
                    tvPlugMode_1.setText("電源 : 開");
                    img_Plug_1.setImageResource(R.drawable.plug_on);
                    setPlugStatus(plugmode1,"006");
                    getPlug();
                }
                else{
                    plugmode1 = 0;
                    //tvPlugMode_1.setText("电源 : 关");
                    tvPlugMode_1.setText("電源 : 關");
                    img_Plug_1.setImageResource(R.drawable.plug_off);
                    setPlugStatus(plugmode1,"006");
                    getPlug();
                }
            }
        });
        btn_Plugtime_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(getActivity(), activity_schedule.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        
    }

    private void getHemsApplianceByName() {
        final JSONObject body = new JSONObject();
        try {
            body.put("action", "getHemsAppliance");
            body.put("field","92702");
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
                    Log.d("92702，getHemsAppliance", object.toString());
                    JSONObject data = object.getJSONObject("data");
                    //------------ 燈光
                    String sw_light1 = data.getJSONArray("list").getJSONObject(0).getString("switch");
                    String sw_light2 = data.getJSONArray("list").getJSONObject(1).getString("switch");
                    String sw_light3 = data.getJSONArray("list").getJSONObject(2).getString("switch");
                    String sw_light4 = data.getJSONArray("list").getJSONObject(3).getString("switch");
                    String sw_light5 = data.getJSONArray("list").getJSONObject(4).getString("switch");
                    String sw_light6 = data.getJSONArray("list").getJSONObject(5).getString("switch");
                    tv_lux1 = data.getJSONArray("list").getJSONObject(0).getString("setting");
                    tv_lux2 = data.getJSONArray("list").getJSONObject(1).getString("setting");
                    tv_lux3 = data.getJSONArray("list").getJSONObject(2).getString("setting");
                    tv_lux4 = data.getJSONArray("list").getJSONObject(3).getString("setting");
                    tv_lux5 = data.getJSONArray("list").getJSONObject(4).getString("setting");
                    tv_lux6 = data.getJSONArray("list").getJSONObject(5).getString("setting");
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
                    if(sw_light3.equals("1")){
                        img_Light_3.setImageResource(R.drawable.light_on);
                        sw_Light_3.setChecked(true);
                        tvLight_Lux3.setText(tv_lux3);
                        sb_Light_3.setProgress(Integer.parseInt(tv_lux3)-2); // tv_lux3:2~100  MAX:98 所以-2
                    }else{
                        img_Light_3.setImageResource(R.drawable.light_off);
                        sw_Light_3.setChecked(false);
                        tvLight_Lux3.setText(tv_lux3);
                        sb_Light_3.setProgress(Integer.parseInt(tv_lux3)-2);
                    }
                    if(sw_light4.equals("1")){
                        img_Light_4.setImageResource(R.drawable.light_on);
                        sw_Light_4.setChecked(true);
                        tvLight_Lux4.setText(tv_lux4);
                        sb_Light_4.setProgress(Integer.parseInt(tv_lux4)-2);
                    }else{
                        img_Light_4.setImageResource(R.drawable.light_off);
                        sw_Light_4.setChecked(false);
                        tvLight_Lux4.setText(tv_lux4);
                        sb_Light_4.setProgress(Integer.parseInt(tv_lux4)-2);
                    }
                    if(sw_light5.equals("1")){
                        img_Light_5.setImageResource(R.drawable.light_on);
                        sw_Light_5.setChecked(true);
                        tvLight_Lux5.setText(tv_lux5);
                        sb_Light_5.setProgress(Integer.parseInt(tv_lux5)-2);
                    }else{
                        img_Light_5.setImageResource(R.drawable.light_off);
                        sw_Light_5.setChecked(false);
                        tvLight_Lux5.setText(tv_lux5);
                        sb_Light_5.setProgress(Integer.parseInt(tv_lux5)-2);
                    }
                    if(sw_light6.equals("1")){
                        img_Light_6.setImageResource(R.drawable.light_on);
                        sw_Light_6.setChecked(true);
                        tvLight_Lux6.setText(tv_lux6);
                        sb_Light_6.setProgress(Integer.parseInt(tv_lux6)-2);
                    }else{
                        img_Light_6.setImageResource(R.drawable.light_off);
                        sw_Light_6.setChecked(false);
                        tvLight_Lux6.setText(tv_lux6);
                        sb_Light_6.setProgress(Integer.parseInt(tv_lux6)-2);
                    }
                    //------------ 空調
                    String sw_ac1 = data.getJSONArray("list").getJSONObject(6).getString("switch");
                    String sw_ac2 = data.getJSONArray("list").getJSONObject(7).getString("switch");
                    String sw_acEL = data.getJSONArray("list").getJSONObject(17).getString("switch"); // Echonet Lite AC
                    tv_temp1 = data.getJSONArray("list").getJSONObject(6).getString("setting");
                    tv_temp2 = data.getJSONArray("list").getJSONObject(7).getString("setting");
                    tv_tempEL = data.getJSONArray("list").getJSONObject(17).getString("setting");
                    if(sw_ac1.equals("1")){
                        acmode1 = 1;
                        tvAcMode_1.setText("電源 : 開");
                        img_Ac_1.setImageResource(R.drawable.air_conditioner_on);
                    }else{
                        acmode1 = 0;
                        tvAcMode_1.setText("電源 : 關");
                        img_Ac_1.setImageResource(R.drawable.air_conditioner_off);
                    }
                    if(sw_ac2.equals("1")){
                        acmode2 = 1;
                        tvAcMode_2.setText("電源 : 開");
                        img_Ac_2.setImageResource(R.drawable.air_conditioner_on);
                    }else{
                        acmode2 = 0;
                        tvAcMode_2.setText("電源 : 關");
                        img_Ac_2.setImageResource(R.drawable.air_conditioner_off);
                    }
                    if(sw_acEL.equals("1")){
                        acmodeEL = 1;
                        tvAcMode_EL.setText("電源 : 開");
                        img_Ac_EL.setImageResource(R.drawable.air_conditioner_on);
                    }else{
                        acmodeEL = 0;
                        tvAcMode_EL.setText("電源 : 關");
                        img_Ac_EL.setImageResource(R.drawable.air_conditioner_off);
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
                        sb_Ac_1.setProgress(Integer.parseInt(tv_temp1)-18);
                        sb_Ac_1.setVisibility(View.VISIBLE);
                    }
                    if(tv_temp2.equals("0")){
                        tvAcFeatures_2.setText("模式 : 送風");
                        tvAc_Temp2.setText("");
                        sb_Ac_2.setVisibility(View.GONE);
                    }else if(tv_temp2.equals("1")){
                        tvAcFeatures_2.setText("模式 : 除濕");
                        tvAc_Temp2.setText("");
                        sb_Ac_2.setVisibility(View.GONE);
                    }else{
                        tvAcFeatures_2.setText("模式 : 冷氣");
                        tvAc_Temp2.setText(tv_temp2);
                        sb_Ac_2.setProgress(Integer.parseInt(tv_temp2)-18);
                        sb_Ac_2.setVisibility(View.VISIBLE);
                    }
                    if(tv_tempEL.equals("0")){
                        tvAcFeatures_EL.setText("模式 : 送風");
                        tvAc_TempEL.setText("");
                        sb_Ac_EL.setVisibility(View.GONE);
                    }else if(tv_tempEL.equals("1")){
                        tvAcFeatures_EL.setText("模式 : 除濕");
                        tvAc_TempEL.setText("");
                        sb_Ac_EL.setVisibility(View.GONE);
                    }else{
                        tvAcFeatures_EL.setText("模式 : 冷氣");
                        tvAc_TempEL.setText(tv_tempEL);
                        sb_Ac_EL.setProgress(Integer.parseInt(tv_tempEL)-18);
                        sb_Ac_EL.setVisibility(View.VISIBLE);
                    }
                    //----------- 風扇
                    String sw_fan1 = data.getJSONArray("list").getJSONObject(8).getString("switch");
                    String sw_fan2 = data.getJSONArray("list").getJSONObject(9).getString("switch");
                    String sw_fan3 = data.getJSONArray("list").getJSONObject(10).getString("switch");
                    String sw_fan4 = data.getJSONArray("list").getJSONObject(11).getString("switch");
                    String sw_fan5 = data.getJSONArray("list").getJSONObject(12).getString("switch");
                    String sw_fan6 = data.getJSONArray("list").getJSONObject(15).getString("switch");
                    String tv_mode1 = data.getJSONArray("list").getJSONObject(8).getString("setting");
                    String tv_mode2 = data.getJSONArray("list").getJSONObject(9).getString("setting");
                    String tv_mode3 = data.getJSONArray("list").getJSONObject(10).getString("setting");
                    String tv_mode4 = data.getJSONArray("list").getJSONObject(11).getString("setting");
                    String tv_mode5 = data.getJSONArray("list").getJSONObject(12).getString("setting");
                    String tv_mode6 = data.getJSONArray("list").getJSONObject(15).getString("setting");
                    if(sw_fan1.equals("1")){
                        fanmode1 = 1;
                        tvFanMode_1.setText("電源 : 開");
                        img_Fan_1.setImageResource(R.drawable.fan_on);
                    }else{
                        fanmode1 = 0;
                        tvFanMode_1.setText("電源 : 關");
                        img_Fan_1.setImageResource(R.drawable.fan_off);
                    }
                    if(sw_fan2.equals("1")){
                        fanmode2 = 1;
                        tvFanMode_2.setText("電源 : 開");
                        img_Fan_2.setImageResource(R.drawable.fan_on);
                    }else{
                        fanmode2 = 0;
                        tvFanMode_2.setText("電源 : 關");
                        img_Fan_2.setImageResource(R.drawable.fan_off);
                    }
                    if(sw_fan3.equals("1")){
                        fanmode3 = 1;
                        tvFanMode_3.setText("電源 : 開");
                        img_Fan_3.setImageResource(R.drawable.fan_on);
                    }else{
                        fanmode3 = 0;
                        tvFanMode_3.setText("電源 : 關");
                        img_Fan_3.setImageResource(R.drawable.fan_off);
                    }
                    if(sw_fan4.equals("1")){
                        fanmode4 = 1;
                        tvFanMode_4.setText("電源 : 開");
                        img_Fan_4.setImageResource(R.drawable.fan_on);
                    }else{
                        fanmode4 = 0;
                        tvFanMode_4.setText("電源 : 關");
                        img_Fan_4.setImageResource(R.drawable.fan_off);
                    }
                    if(sw_fan5.equals("1")){
                        fanmode5 = 1;
                        tvFanMode_5.setText("電源 : 開");
                        img_Fan_5.setImageResource(R.drawable.fan_on);
                    }else{
                        fanmode5 = 0;
                        tvFanMode_5.setText("電源 : 關");
                        img_Fan_5.setImageResource(R.drawable.fan_off);
                    }
                    if(sw_fan6.equals("1")){
                        fanmode6 = 1;
                        tvFanMode_6.setText("電源 : 開");
                        img_Fan_6.setImageResource(R.drawable.fan_on);
                    }else{
                        fanmode6 = 0;
                        tvFanMode_6.setText("電源 : 關");
                        img_Fan_6.setImageResource(R.drawable.fan_off);
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
                    switch(tv_mode2){
                        case "0":
                            tvFanWind_2.setText("風度：弱");
                            break;
                        case "1":
                            tvFanWind_2.setText("風度：中");
                            break;
                        case "2":
                            tvFanWind_2.setText("風度：強");
                            break;
                    }
                    switch(tv_mode3){
                        case "0":
                            tvFanWind_3.setText("風度：弱");
                            break;
                        case "1":
                            tvFanWind_3.setText("風度：中");
                            break;
                        case "2":
                            tvFanWind_3.setText("風度：強");
                            break;
                    }
                    switch(tv_mode4){
                        case "0":
                            tvFanWind_4.setText("風度：弱");
                            break;
                        case "1":
                            tvFanWind_4.setText("風度：中");
                            break;
                        case "2":
                            tvFanWind_4.setText("風度：強");
                            break;
                    }
                    switch(tv_mode5){
                        case "0":
                            tvFanWind_5.setText("風度：弱");
                            break;
                        case "1":
                            tvFanWind_5.setText("風度：中");
                            break;
                        case "2":
                            tvFanWind_5.setText("風度：強");
                            break;
                    }
                    switch(tv_mode6){
                        case "0":
                            tvFanWind_6.setText("風度：弱");
                            break;
                        case "1":
                            tvFanWind_6.setText("風度：中");
                            break;
                        case "2":
                            tvFanWind_6.setText("風度：強");
                            break;
                    }
                    //-----------
                    //----------- 排風扇
                    String sw_exhaust1 = data.getJSONArray("list").getJSONObject(13).getString("switch");
                    String sw_exhaust2 = data.getJSONArray("list").getJSONObject(14).getString("switch");
                    String tv_exhaust1 = data.getJSONArray("list").getJSONObject(13).getString("setting");
                    String tv_exhaust2 = data.getJSONArray("list").getJSONObject(14).getString("setting");
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
                    if(sw_exhaust2.equals("1")){
                        exhaustmode2 = 1;
                        //tvExhaustMode_2.setText("电源 : 开");
                        tvExhaustMode_2.setText("電源 : 開");
                        img_Exhaust_2.setImageResource(R.drawable.exhaust_on);
                    }else{
                        exhaustmode2 = 0;
                        //tvExhaustMode_2.setText("电源 : 关");
                        tvExhaustMode_2.setText("電源 : 關");
                        img_Exhaust_2.setImageResource(R.drawable.exhaust_off);
                    }
                    switch(tv_exhaust1){
                        case "0":
                            tvExhaustWind_1.setText("風度：弱");
                            break;
                        case "1":
                            tvExhaustWind_1.setText("風度：強");
                            break;
                    }
                    switch(tv_exhaust2){
                        case "0":
                            tvExhaustWind_2.setText("風度：弱");
                            break;
                        case "1":
                            tvExhaustWind_2.setText("風度：強");
                            break;
                    }
                    //-----------
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

    private void setPlugStatus(int plugmode1, String name) {
        final JSONObject body = new JSONObject();
        try {
            body.put("action", "setPlugStatus");
            body.put("status",plugmode1);
            body.put("name",name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        postRequest = new StringRequest(Request.Method.POST, plug_ur1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    Log.d("setPlugStatus", object.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError_Plug", error.toString());
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

    private void getPlug() {
        final JSONObject body = new JSONObject();
        try {
            body.put("action", "getPlug");
            body.put("name","006");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        postRequest = new StringRequest(Request.Method.POST, plug_ur1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray object = new JSONArray(response);
                    Log.d("getPlug", object.toString());
                    String power = object.getJSONObject(0).getString("power");
                    String status = object.getJSONObject(0).getString("status");
                    tvPlugPower_1.setText(power);
                    if(status.equals("1")){
                        plugmode1 = 1;
                        //tvPlugMode_1.setText("电源 : 开");
                        tvPlugMode_1.setText("電源 : 開");
                        img_Plug_1.setImageResource(R.drawable.plug_on);
                    }else{
                        plugmode1 = 0;
                        //tvPlugMode_1.setText("电源 : 关");
                        tvPlugMode_1.setText("電源 : 關");
                        img_Plug_1.setImageResource(R.drawable.plug_off);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError_getPlug", error.toString());
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
            body.put("field","92702");
            body.put("token",Token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        postRequest = new StringRequest(Request.Method.POST, url_room, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    Log.d("92702，getRoom", object.toString());
                    JSONObject data = object.getJSONObject("data");
                    String auto_comfort1 = data.getJSONArray("list").getJSONObject(0).getString("switch"); //自動舒適度-區域一
                    String auto_comfort2 = data.getJSONArray("list").getJSONObject(1).getString("switch"); //自動舒適度-區域二
                    //String auto_ac = data.getJSONArray("list").getJSONObject(0).getString("switch"); //自動空調
                    String auto_light1 = data.getJSONArray("list").getJSONObject(2).getString("switch"); //自動調光-區域一
                    String auto_light2 = data.getJSONArray("list").getJSONObject(3).getString("switch"); //自動調光-區域二
                    //---------移動感測器
                    String motion_detector_1 = data.getJSONArray("list").getJSONObject(4).getString("switch"); // 左區
                    String motion_detector_2 = data.getJSONArray("list").getJSONObject(5).getString("switch"); // 門口
                    String motion_detector_3 = data.getJSONArray("list").getJSONObject(6).getString("switch"); // 右區
                    String motion_detector_4 = data.getJSONArray("list").getJSONObject(7).getString("switch"); // 中區
                    if(motion_detector_1.equals("1") || motion_detector_2.equals("1") || motion_detector_3.equals("1") || motion_detector_4.equals("1")){
                        sw_Motion.setChecked(true);
                    }
                   //
                    String motion_offtime_1 = data.getJSONArray("list").getJSONObject(4).getString("setting_value"); // 左區
                    String motion_offtime_2 = data.getJSONArray("list").getJSONObject(5).getString("setting_value"); // 門口
                    String motion_offtime_3 = data.getJSONArray("list").getJSONObject(6).getString("setting_value"); // 右區
                    String motion_offtime_4 = data.getJSONArray("list").getJSONObject(7).getString("setting_value"); // 中區
                    settingedit.putFloat("92702_motion_offtime_1", Float.parseFloat(motion_offtime_1)).commit();
                    settingedit.putFloat("92702_motion_offtime_2", Float.parseFloat(motion_offtime_2)).commit();
                    settingedit.putFloat("92702_motion_offtime_3", Float.parseFloat(motion_offtime_3)).commit();
                    settingedit.putFloat("92702_motion_offtime_4", Float.parseFloat(motion_offtime_4)).commit();
                    String comfort1_level = data.getJSONArray("list").getJSONObject(0).getString("setting_value");
                    String comfort2_level = data.getJSONArray("list").getJSONObject(1).getString("setting_value");
                    settingedit.putString("92702_comfort1",comfort1_level).commit();
                    settingedit.putString("92702_comfort2",comfort2_level).commit();
                    if(auto_comfort1.equals("0")){
                        sw_Auto_Comfort1.setChecked(false);
                    }else{
                        sw_Auto_Comfort1.setChecked(true);
                    }
                    if(auto_comfort2.equals("0")){
                        sw_Auto_Comfort2.setChecked(false);
                    }else{
                        sw_Auto_Comfort2.setChecked(true);
                    }
                    if(auto_light1.equals("0")){
                        sw_Auto_Light1.setChecked(false);
                    }else{
                        sw_Auto_Light1.setChecked(true);
                    }
                    if(auto_light2.equals("0")){
                        sw_Auto_Light2.setChecked(false);
                    }else{
                        sw_Auto_Light2.setChecked(true);
                    }
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

    //排風扇
    private void exhaust_findViewById() {
        img_Exhaust_1 = getView().findViewById(R.id.img_exhaust_1);
        img_Exhaust_2 = getView().findViewById(R.id.img_exhaust_2);
        btn_Exhaust_1 = getView().findViewById(R.id.btn_exhaust_1);
        btn_Exhaust_2 = getView().findViewById(R.id.btn_exhaust_2);
        btn_ExhaustWind1_high = getView().findViewById(R.id.btn_exhaustwind1_high);
        btn_ExhaustWind2_high = getView().findViewById(R.id.btn_exhaustwind2_high);
        btn_ExhaustWind1_mid = getView().findViewById(R.id.btn_exhaustwind1_mid);
        btn_ExhaustWind2_mid = getView().findViewById(R.id.btn_exhaustwind2_mid);
        btn_ExhaustWind1_low = getView().findViewById(R.id.btn_exhaustwind1_low);
        btn_ExhaustWind2_low = getView().findViewById(R.id.btn_exhaustwind2_low);
        tvExhaustMode_1 = getView().findViewById(R.id.tv_exhaustmode_1);
        tvExhaustMode_2 = getView().findViewById(R.id.tv_exhaustmode_2);
        tvExhaustWind_1 = getView().findViewById(R.id.tv_exhaustwind_1);
        tvExhaustWind_2 = getView().findViewById(R.id.tv_exhaustwind_2);
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
                    closeAutoControl("air_ejector_fan_1-button","0","auto_comfort_1");
                    action("ExhaustFan","1","1");
                }
                else{
                    exhaustmode1 = 0;
                    //tvExhaustMode_1.setText("电源 : 关");
                    tvExhaustMode_1.setText("電源 : 關");
                    img_Exhaust_1.setImageResource(R.drawable.exhaust_off);
                    setApplianceByName_switch("air_ejector_fan_1","0");
                    closeAutoControl("air_ejector_fan_1-button","0","auto_comfort_1");
                    action("ExhaustFan","1","0");
                }
            }
        });
        btn_Exhaust_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(exhaustmode2 == 0){
                    exhaustmode2 = 1;
                    //tvExhaustMode_2.setText("电源 : 开");
                    tvExhaustMode_2.setText("電源 : 開");
                    img_Exhaust_2.setImageResource(R.drawable.exhaust_on);
                    setApplianceByName_switch("air_ejector_fan_2","1");
                    closeAutoControl("air_ejector_fan_2-button","0","auto_comfort_2");
                    action("ExhaustFan","2","1");
                }
                else{
                    exhaustmode2 = 0;
                    //tvExhaustMode_2.setText("电源 : 关");
                    tvExhaustMode_2.setText("電源 : 關");
                    img_Exhaust_2.setImageResource(R.drawable.exhaust_off);
                    setApplianceByName_switch("air_ejector_fan_2","0");
                    closeAutoControl("air_ejector_fan_2-button","0","auto_comfort_2");
                    action("ExhaustFan","2","1");
                }
            }
        });
        //排風扇風度
        btn_ExhaustWind1_high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvExhaustWind_1.setText("风度：强");
                tvExhaustWind_1.setText("風度：強");
                setApplianceByName_setting("air_ejector_fan_1","2");
                closeAutoControl("air_ejector_fan_1-slider","0","auto_comfort_1");
                action("ExhaustFan","1","4");
            }
        });
        btn_ExhaustWind1_mid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvExhaustWind_1.setText("风度：中");
                tvExhaustWind_1.setText("風度：中");
                setApplianceByName_setting("air_ejector_fan_1","1");
                closeAutoControl("air_ejector_fan_1-slider","0","auto_comfort_1");
                action("ExhaustFan","1","3");
            }
        });
        btn_ExhaustWind1_low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvExhaustWind_1.setText("风度：弱");
                tvExhaustWind_1.setText("風度：弱");
                setApplianceByName_setting("air_ejector_fan_1","0");
                closeAutoControl("air_ejector_fan_1-slider","0","auto_comfort_1");
                action("ExhaustFan","1","2");
            }
        });
        btn_ExhaustWind2_high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvExhaustWind_2.setText("风度：强");
                tvExhaustWind_2.setText("風度：強");
                setApplianceByName_setting("air_ejector_fan_2","2");
                closeAutoControl("air_ejector_fan_2-slider","0","auto_comfort_2");
                action("ExhaustFan","2","4");
            }
        });
        btn_ExhaustWind2_mid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvExhaustWind_2.setText("风度：中");
                tvExhaustWind_2.setText("風度：中");
                setApplianceByName_setting("air_ejector_fan_2","1");
                closeAutoControl("air_ejector_fan_2-slider","0","auto_comfort_2");
                action("ExhaustFan","2","3");
            }
        });
        btn_ExhaustWind2_low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvExhaustWind_2.setText("风度：弱");
                tvExhaustWind_2.setText("風度：弱");
                setApplianceByName_setting("air_ejector_fan_2","0");
                closeAutoControl("air_ejector_fan_2-slider","0","auto_comfort_2");
                action("ExhaustFan","2","2");
            }
        });

    }
    //風扇
    private void fan_findViewById() {
        img_Fan_1 = getView().findViewById(R.id.img_fan_1);
        img_Fan_2 = getView().findViewById(R.id.img_fan_2);
        img_Fan_3 = getView().findViewById(R.id.img_fan_3);
        img_Fan_4 = getView().findViewById(R.id.img_fan_4);
        img_Fan_5 = getView().findViewById(R.id.img_fan_5);
        img_Fan_6 = getView().findViewById(R.id.img_fan_6);
        btn_Fan_1 = getView().findViewById(R.id.btn_fan_1);
        btn_Fan_2 = getView().findViewById(R.id.btn_fan_2);
        btn_Fan_3 = getView().findViewById(R.id.btn_fan_3);
        btn_Fan_4 = getView().findViewById(R.id.btn_fan_4);
        btn_Fan_5 = getView().findViewById(R.id.btn_fan_5);
        btn_Fan_6 = getView().findViewById(R.id.btn_fan_6);
        btn_Turn_1 = getView().findViewById(R.id.btn_turn_1);
        btn_Turn_2 = getView().findViewById(R.id.btn_turn_2);
        btn_Turn_3 = getView().findViewById(R.id.btn_turn_3);
        btn_Turn_4 = getView().findViewById(R.id.btn_turn_4);
        btn_Turn_5 = getView().findViewById(R.id.btn_turn_5);
        btn_Turn_6 = getView().findViewById(R.id.btn_turn_6);
        btn_FanWind1_high = getView().findViewById(R.id.btn_fanwind1_high);
        btn_FanWind1_mid = getView().findViewById(R.id.btn_fanwind1_mid);
        btn_FanWind1_low = getView().findViewById(R.id.btn_fanwind1_low);
        btn_FanWind2_high = getView().findViewById(R.id.btn_fanwind2_high);
        btn_FanWind2_mid = getView().findViewById(R.id.btn_fanwind2_mid);
        btn_FanWind2_low = getView().findViewById(R.id.btn_fanwind2_low);
        btn_FanWind3_high = getView().findViewById(R.id.btn_fanwind3_high);
        btn_FanWind3_mid = getView().findViewById(R.id.btn_fanwind3_mid);
        btn_FanWind3_low = getView().findViewById(R.id.btn_fanwind3_low);
        btn_FanWind4_high = getView().findViewById(R.id.btn_fanwind4_high);
        btn_FanWind4_mid = getView().findViewById(R.id.btn_fanwind4_mid);
        btn_FanWind4_low = getView().findViewById(R.id.btn_fanwind4_low);
        btn_FanWind5_high = getView().findViewById(R.id.btn_fanwind5_high);
        btn_FanWind5_mid = getView().findViewById(R.id.btn_fanwind5_mid);
        btn_FanWind5_low = getView().findViewById(R.id.btn_fanwind5_low);
        btn_FanWind6_high = getView().findViewById(R.id.btn_fanwind6_high);
        btn_FanWind6_mid = getView().findViewById(R.id.btn_fanwind6_mid);
        btn_FanWind6_low = getView().findViewById(R.id.btn_fanwind6_low);
        tvFanMode_1 =getView().findViewById(R.id.tv_fanmode_1);
        tvFanMode_2 =getView().findViewById(R.id.tv_fanmode_2);
        tvFanMode_3 =getView().findViewById(R.id.tv_fanmode_3);
        tvFanMode_4 =getView().findViewById(R.id.tv_fanmode_4);
        tvFanMode_5 =getView().findViewById(R.id.tv_fanmode_5);
        tvFanMode_6 =getView().findViewById(R.id.tv_fanmode_6);
        tvFanWind_1 = getView().findViewById(R.id.tv_fanwind_1);
        tvFanWind_2 = getView().findViewById(R.id.tv_fanwind_2);
        tvFanWind_3 = getView().findViewById(R.id.tv_fanwind_3);
        tvFanWind_4 = getView().findViewById(R.id.tv_fanwind_4);
        tvFanWind_5 = getView().findViewById(R.id.tv_fanwind_5);
        tvFanWind_6 = getView().findViewById(R.id.tv_fanwind_6);
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
                    closeAutoControl("fan_1-button","0","auto_comfort_2");
                    action("Fan","1","1");
                }
                else{
                    fanmode1 = 0;
                    //tvFanMode_1.setText("电源 : 关");
                    tvFanMode_1.setText("電源 : 關");
                    img_Fan_1.setImageResource(R.drawable.fan_off);
                    setApplianceByName_switch("fan_1","0");
                    closeAutoControl("fan_1-button","0","auto_comfort_2");
                    action("Fan","1","0");
                }
            }
        });
        btn_Fan_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fanmode2 == 0){
                    fanmode2 = 1;
                    //tvFanMode_2.setText("电源 : 开");
                    tvFanMode_2.setText("電源 : 開");
                    img_Fan_2.setImageResource(R.drawable.fan_on);
                    setApplianceByName_switch("fan_2","1");
                    closeAutoControl("fan_2-button","0","auto_comfort_2");
                    action("Fan","2","1");
                }
                else{
                    fanmode2 = 0;
                    //tvFanMode_2.setText("电源 : 关");
                    tvFanMode_2.setText("電源 : 關");
                    img_Fan_2.setImageResource(R.drawable.fan_off);
                    setApplianceByName_switch("fan_2","0");
                    closeAutoControl("fan_2-button","0","auto_comfort_2");
                    action("Fan","2","0");
                }
            }
        });
        btn_Fan_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fanmode3 == 0){
                    fanmode3 = 1;
                    //tvFanMode_3.setText("电源 : 开");
                    tvFanMode_3.setText("電源 : 開");
                    img_Fan_3.setImageResource(R.drawable.fan_on);
                    setApplianceByName_switch("fan_3","1");
                    closeAutoControl("fan_3-button","0","auto_comfort_2");
                    action("Fan","3","1");
                }
                else{
                    fanmode3 = 0;
                    //tvFanMode_3.setText("电源 : 关");
                    tvFanMode_3.setText("電源 : 關");
                    img_Fan_3.setImageResource(R.drawable.fan_off);
                    setApplianceByName_switch("fan_3","0");
                    closeAutoControl("fan_3-button","0","auto_comfort_2");
                    action("Fan","3","0");
                }
            }
        });
        btn_Fan_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fanmode4 == 0){
                    fanmode4 = 1;
                    //tvFanMode_4.setText("电源 : 开");
                    tvFanMode_4.setText("電源 : 開");
                    img_Fan_4.setImageResource(R.drawable.fan_on);
                    setApplianceByName_switch("fan_4","1");
                    closeAutoControl("fan_4-button","0","auto_comfort_2");
                    action("Fan","4","1");
                }
                else{
                    fanmode4 = 0;
                    //tvFanMode_4.setText("电源 : 关");
                    tvFanMode_4.setText("電源 : 關");
                    img_Fan_4.setImageResource(R.drawable.fan_off);
                    setApplianceByName_switch("fan_4","0");
                    closeAutoControl("fan_4-button","0","auto_comfort_2");
                    action("Fan","4","0");
                }
            }
        });
        btn_Fan_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fanmode5 == 0){
                    fanmode5 = 1;
                    //tvFanMode_5.setText("电源 : 开");
                    tvFanMode_5.setText("電源 : 開");
                    img_Fan_5.setImageResource(R.drawable.fan_on);
                    setApplianceByName_switch("fan_5","1");
                    closeAutoControl("fan_5-button","0","auto_comfort_1");
                    action("Fan","5","1");
                }
                else{
                    fanmode5 = 0;
                    //tvFanMode_5.setText("电源 : 关");
                    tvFanMode_5.setText("電源 : 關");
                    img_Fan_5.setImageResource(R.drawable.fan_off);
                    setApplianceByName_switch("fan_5","0");
                    closeAutoControl("fan_5-button","0","auto_comfort_1");
                    action("Fan","5","0");
                }
            }
        });
        btn_Fan_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fanmode6 == 0){
                    fanmode6 = 1;
                    //tvFanMode_6.setText("电源 : 开");
                    tvFanMode_6.setText("電源 : 開");
                    img_Fan_6.setImageResource(R.drawable.fan_on);
                    setApplianceByName_switch("fan_6","1");
                    closeAutoControl("fan_6-button","0","auto_comfort_2");
                    action("Fan","10","1");
                }
                else{
                    fanmode6 = 0;
                    //tvFanMode_6.setText("电源 : 关");
                    tvFanMode_6.setText("電源 : 關");
                    img_Fan_6.setImageResource(R.drawable.fan_off);
                    setApplianceByName_switch("fan_6","0");
                    closeAutoControl("fan_6-button","0","auto_comfort_2");
                    action("Fan","10","0");
                }
            }
        });
        //風扇旋轉
        btn_Turn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_Turn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_Turn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_Turn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_Turn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_Turn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //風扇風度
        btn_FanWind1_high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvFanWind_1.setText("风度：强");
                tvFanWind_1.setText("風度：強");
                setApplianceByName_setting("fan_1","2");
                closeAutoControl("fan_1-slider","0","auto_comfort_2");
                action("Fan","1","4");
            }
        });
        btn_FanWind1_mid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvFanWind_1.setText("风度：中");
                tvFanWind_1.setText("風度：中");
                setApplianceByName_setting("fan_1","1");
                closeAutoControl("fan_1-slider","0","auto_comfort_2");
                action("Fan","1","3");
            }
        });
        btn_FanWind1_low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvFanWind_1.setText("风度：弱");
                tvFanWind_1.setText("風度：弱");
                setApplianceByName_setting("fan_1","0");
                closeAutoControl("fan_1-slider","0","auto_comfort_2");
                action("Fan","1","2");
            }
        });
        btn_FanWind2_high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvFanWind_2.setText("风度：强");
                tvFanWind_2.setText("風度：強");
                setApplianceByName_setting("fan_2","2");
                closeAutoControl("fan_2-slider","0","auto_comfort_2");
                action("Fan","2","4");
            }
        });
        btn_FanWind2_mid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvFanWind_2.setText("风度：中");
                tvFanWind_2.setText("風度：中");
                setApplianceByName_setting("fan_2","1");
                closeAutoControl("fan_2-slider","0","auto_comfort_2");
                action("Fan","2","3");
            }
        });
        btn_FanWind2_low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvFanWind_2.setText("风度：弱");
                tvFanWind_2.setText("風度：弱");
                setApplianceByName_setting("fan_2","0");
                closeAutoControl("fan_2-slider","0","auto_comfort_2");
                action("Fan","2","2");
            }
        });
        btn_FanWind3_high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvFanWind_3.setText("风度：强");
                tvFanWind_3.setText("風度：強");
                setApplianceByName_setting("fan_3","2");
                closeAutoControl("fan_3-slider","0","auto_comfort_2");
                action("Fan","3","4");
            }
        });
        btn_FanWind3_mid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvFanWind_3.setText("风度：中");
                tvFanWind_3.setText("風度：中");
                setApplianceByName_setting("fan_3","1");
                closeAutoControl("fan_3-slider","0","auto_comfort_2");
                action("Fan","3","3");
            }
        });
        btn_FanWind3_low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvFanWind_3.setText("风度：弱");
                tvFanWind_3.setText("風度：弱");
                setApplianceByName_setting("fan_3","0");
                closeAutoControl("fan_3-slider","0","auto_comfort_2");
                action("Fan","3","2");
            }
        });
        btn_FanWind4_high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvFanWind_4.setText("风度：强");
                tvFanWind_4.setText("風度：強");
                setApplianceByName_setting("fan_4","2");
                closeAutoControl("fan_4-slider","0","auto_comfort_2");
                action("Fan","4","4");
            }
        });
        btn_FanWind4_mid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvFanWind_4.setText("风度：中");
                tvFanWind_4.setText("風度：中");
                setApplianceByName_setting("fan_4","1");
                closeAutoControl("fan_4-slider","0","auto_comfort_2");
                action("Fan","4","3");
            }
        });
        btn_FanWind4_low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvFanWind_4.setText("风度：弱");
                tvFanWind_4.setText("風度：弱");
                setApplianceByName_setting("fan_4","0");
                closeAutoControl("fan_4-slider","0","auto_comfort_2");
                action("Fan","4","2");
            }
        });
        btn_FanWind5_high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvFanWind_5.setText("风度：强");
                tvFanWind_5.setText("風度：強");
                setApplianceByName_setting("fan_5","2");
                closeAutoControl("fan_5-slider","0","auto_comfort_1");
                action("Fan","5","4");
            }
        });
        btn_FanWind5_mid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvFanWind_5.setText("风度：中");
                tvFanWind_5.setText("風度：中");
                setApplianceByName_setting("fan_5","1");
                closeAutoControl("fan_5-slider","0","auto_comfort_1");
                action("Fan","5","3");
            }
        });
        btn_FanWind5_low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvFanWind_5.setText("风度：弱");
                tvFanWind_5.setText("風度：弱");
                setApplianceByName_setting("fan_5","0");
                closeAutoControl("fan_5-slider","0","auto_comfort_1");
                action("Fan","5","2");
            }
        });
        btn_FanWind6_high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvFanWind_6.setText("风度：强");
                tvFanWind_6.setText("風度：強");
                setApplianceByName_setting("fan_6","2");
                closeAutoControl("fan_6-slider","0","auto_comfort_2");
                action("Fan","10","4");
            }
        });
        btn_FanWind6_mid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvFanWind_6.setText("风度：中");
                tvFanWind_6.setText("風度：中");
                setApplianceByName_setting("fan_6","1");
                closeAutoControl("fan_6-slider","0","auto_comfort_2");
                action("Fan","10","3");
            }
        });
        btn_FanWind6_low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvFanWind_6.setText("风度：弱");
                tvFanWind_6.setText("風度：弱");
                setApplianceByName_setting("fan_6","0");
                closeAutoControl("fan_6-slider","0","auto_comfort_2");
                action("Fan","10","2");
            }
        });

    }
    //空調
    private void ac_findViewById() {
        tvAcMode_1 = getView().findViewById(R.id.tv_acmode_1);
        tvAcMode_2 = getView().findViewById(R.id.tv_acmode_2);
        tvAcMode_EL = getView().findViewById(R.id.tv_acmode_EL);
        tvAc_Temp1 = getView().findViewById(R.id.tv_ac_temp1);
        tvAc_Temp2 = getView().findViewById(R.id.tv_ac_temp2);
        tvAc_TempEL = getView().findViewById(R.id.tv_ac_tempEL);
        tvAcFeatures_1 =getView().findViewById(R.id.tv_Features_1);
        tvAcFeatures_2 = getView().findViewById(R.id.tv_Features_2);
        tvAcFeatures_EL = getView().findViewById(R.id.tv_Features_EL);
        img_Ac_1 = getView().findViewById(R.id.img_ac_1);
        img_Ac_2 = getView().findViewById(R.id.img_ac_2);
        img_Ac_EL = getView().findViewById(R.id.img_ac_EL);
        btn_Ac_1 = getView().findViewById(R.id.btn_ac_1);
        btn_Ac_2 = getView().findViewById(R.id.btn_ac_2);
        btn_Ac_EL = getView().findViewById(R.id.btn_ac_EL);
        btn_Cold_1 = getView().findViewById(R.id.btn_cold_1);
        btn_Wet_1 = getView().findViewById(R.id.btn_wet_1);
        btn_Wind_1 = getView().findViewById(R.id.btn_wind_1);
        btn_Cold_2 = getView().findViewById(R.id.btn_cold_2);
        btn_Wet_2 = getView().findViewById(R.id.btn_wet_2);
        btn_Wind_2 = getView().findViewById(R.id.btn_wind_2);
        btn_Cold_EL = getView().findViewById(R.id.btn_cold_EL);
        btn_Wet_EL = getView().findViewById(R.id.btn_wet_EL);
        btn_Wind_EL = getView().findViewById(R.id.btn_wind_EL);
        sb_Ac_1 = getView().findViewById(R.id.sb_ac_1);
        sb_Ac_2 = getView().findViewById(R.id.sb_ac_2);
        sb_Ac_EL = getView().findViewById(R.id.sb_ac_EL);
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
                    closeAutoControl("ac_1-button","0","auto_comfort_1");
                    action("AirConditioner","1","1");
                }
                else{
                    acmode1 = 0;
                    //tvAcMode_1.setText("电源 : 关");
                    tvAcMode_1.setText("電源 : 關");
                    img_Ac_1.setImageResource(R.drawable.air_conditioner_off);
                    setApplianceByName_switch("ac_1","0");
                    closeAutoControl("ac_1-button","0","auto_comfort_1");
                    action("AirConditioner","1","0");
                }
            }
        });
        btn_Ac_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(acmode2 == 0){
                    acmode2 = 1;
                    //tvAcMode_2.setText("电源 : 开");
                    tvAcMode_2.setText("電源 : 開");
                    img_Ac_2.setImageResource(R.drawable.air_conditioner_on);
                    setApplianceByName_switch("ac_2","1");
                    closeAutoControl("ac_2-button","0","auto_comfort_2");
                    action("AirConditioner","2","1");
                }
                else{
                    acmode2 = 0;
                    //tvAcMode_2.setText("电源 : 关");
                    tvAcMode_2.setText("電源 : 關");
                    img_Ac_2.setImageResource(R.drawable.air_conditioner_off);
                    setApplianceByName_switch("ac_2","0");
                    closeAutoControl("ac_2-button","0","auto_comfort_2");
                    action("AirConditioner","2","0");
                }
            }
        });
        btn_Ac_EL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(acmodeEL == 0){
                    acmodeEL = 1;
                    //tvAcMode_EL.setText("电源 : 开");
                    tvAcMode_EL.setText("電源 : 開");
                    img_Ac_EL.setImageResource(R.drawable.air_conditioner_on);
                    setApplianceByName_switch("ac_3","1");
                    closeAutoControl("ac_3-button","0","auto_comfort_3");
                    action("","","1"); // EL　ＡC
                }
                else{
                    acmodeEL = 0;
                    //tvAcMode_EL.setText("电源 : 关");
                    tvAcMode_EL.setText("電源 : 關");
                    img_Ac_EL.setImageResource(R.drawable.air_conditioner_off);
                    setApplianceByName_switch("ac_3","0");
                    closeAutoControl("ac_3-button","0","auto_comfort_3");
                    action("","","0"); // EL　ＡC
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
                closeAutoControl("ac_1-status-button","0","auto_comfort_1");
                action("AirConditioner","1",tv_temp1);
            }
        });
        btn_Wet_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvAcFeatures_1.setText("模式：除湿");
                tvAcFeatures_1.setText("模式：除溼");
                setApplianceByName_setting("ac_1","1");
                closeAutoControl("ac_1-status-button","0","auto_comfort_1");
                action("AirConditioner","1","3");
            }
        });
        btn_Wind_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvAcFeatures_1.setText("模式：送风");
                tvAcFeatures_1.setText("模式：送風");
                setApplianceByName_setting("ac_1","0");
                closeAutoControl("ac_1-status-button","0","auto_comfort_1");
                action("AirConditioner","1","2");
            }
        });
        btn_Cold_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvAcFeatures_2.setText("模式：冷气");
                tvAcFeatures_2.setText("模式：冷氣");
                setApplianceByName_setting("ac_2",tv_temp2);
                closeAutoControl("ac_2-status-button","0","auto_comfort_2");
                action("AirConditioner","2",tv_temp2);
            }
        });
        btn_Wet_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvAcFeatures_2.setText("模式：除湿");
                tvAcFeatures_2.setText("模式：除溼");
                setApplianceByName_setting("ac_2","1");
                closeAutoControl("ac_2-status-button","0","auto_comfort_2");
                action("AirConditioner","2","3");
            }
        });
        btn_Wind_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvAcFeatures_2.setText("模式：送风");
                tvAcFeatures_2.setText("模式：送風");
                setApplianceByName_setting("ac_2","0");
                closeAutoControl("ac_2-status-button","0","auto_comfort_2");
                action("AirConditioner","2","2");
            }
        });
        btn_Cold_EL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvAcFeatures_EL.setText("模式：冷气");
                tvAcFeatures_EL.setText("模式：冷氣");
                setApplianceByName_setting("ac_3",tv_tempEL);
                closeAutoControl("ac_3-status-button","0","auto_comfort_3");
                action("","",tv_tempEL); // EL　ＡC
            }
        });
        btn_Wet_EL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvAcFeatures_EL.setText("模式：除湿");
                tvAcFeatures_EL.setText("模式：除溼");
                setApplianceByName_setting("ac_3","1");
                closeAutoControl("ac_3-status-button","0","auto_comfort_3");
                action("","","3"); // EL　ＡC
            }
        });
        btn_Wind_EL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvAcFeatures_EL.setText("模式：送风");
                tvAcFeatures_EL.setText("模式：送風");
                setApplianceByName_setting("ac_3","0");
                closeAutoControl("ac_3-status-button","0","auto_comfort_3");
                action("","","2"); // EL　ＡC
            }
        });
        //-----
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
                closeAutoControl("ac_1-slider","0","auto_comfort_2");
                action("AirConditioner","1",tv_temp1);
            }
        });
        sb_Ac_2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvAc_Temp2.setText(String.valueOf(progress+18));
                tv_temp2 = String.valueOf(progress+18);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setApplianceByName_setting("ac_2",tv_temp2);
                closeAutoControl("ac_2-slider","0","auto_comfort_2");
                action("AirConditioner","2",tv_temp2);
            }
        });
        sb_Ac_EL.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvAc_TempEL.setText(String.valueOf(progress+18));
                tv_tempEL = String.valueOf(progress+18);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setApplianceByName_setting("ac_3",tv_tempEL);
                closeAutoControl("ac_3-slider","0","auto_comfort_3");
                action("","",tv_tempEL); // EL　ＡC
            }
        });
    }
    //燈光
    private void light_findViewById() {
        tvLight_Lux1 = getView().findViewById(R.id.tv_light_lux1);
        tvLight_Lux2 = getView().findViewById(R.id.tv_light_lux2);
        tvLight_Lux3 = getView().findViewById(R.id.tv_light_lux3);
        tvLight_Lux4 = getView().findViewById(R.id.tv_light_lux4);
        tvLight_Lux5 = getView().findViewById(R.id.tv_light_lux5);
        tvLight_Lux6 = getView().findViewById(R.id.tv_light_lux6);
        img_Light_1 = getView().findViewById(R.id.img_light_1);
        img_Light_2 = getView().findViewById(R.id.img_light_2);
        img_Light_3 = getView().findViewById(R.id.img_light_3);
        img_Light_4 = getView().findViewById(R.id.img_light_4);
        img_Light_5 = getView().findViewById(R.id.img_light_5);
        img_Light_6 = getView().findViewById(R.id.img_light_6);
        sw_Light_1 = getView().findViewById(R.id.sw_light_1);
        sw_Light_2 = getView().findViewById(R.id.sw_light_2);
        sw_Light_3 = getView().findViewById(R.id.sw_light_3);
        sw_Light_4 = getView().findViewById(R.id.sw_light_4);
        sw_Light_5 = getView().findViewById(R.id.sw_light_5);
        sw_Light_6 = getView().findViewById(R.id.sw_light_6);
        sb_Light_1 = getView().findViewById(R.id.sb_light_1);
        sb_Light_2 = getView().findViewById(R.id.sb_light_2);
        sb_Light_3 = getView().findViewById(R.id.sb_light_3);
        sb_Light_4 = getView().findViewById(R.id.sb_light_4);
        sb_Light_5 = getView().findViewById(R.id.sb_light_5);
        sb_Light_6 = getView().findViewById(R.id.sb_light_6);
        //開關
        sw_Light_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sw_Light_1.isChecked()){
                    img_Light_1.setImageResource(R.drawable.light_on);
                    tvLight_Lux1.setText(tv_lux1);
                    setApplianceByName_switch("light_1","1");
                    closeAutoControl("light_1-button","0","auto_dimming_2");
                    action("DimmerControl","65","1");
                }
                else {
                    img_Light_1.setImageResource(R.drawable.light_off);
                    tvLight_Lux1.setText(tv_lux1);
                    setApplianceByName_switch("light_1","0");
                    closeAutoControl("light_1-button","0","auto_dimming_2");
                    action("DimmerControl","65","0");
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
                    closeAutoControl("light_2-button","0","auto_dimming_2");
                    action("DimmerControl","66","1");
                }
                else {
                    img_Light_2.setImageResource(R.drawable.light_off);
                    tvLight_Lux2.setText(tv_lux2);
                    setApplianceByName_switch("light_2","0");
                    closeAutoControl("light_2-button","0","auto_dimming_2");
                    action("DimmerControl","66","0");
                }
            }
        });
        sw_Light_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sw_Light_3.isChecked()){
                    img_Light_3.setImageResource(R.drawable.light_on);
                    tvLight_Lux3.setText(tv_lux3);
                    setApplianceByName_switch("light_3","1");
                    closeAutoControl("light_3-button","0","auto_dimming_1");
                    action("DimmerControl","67","1");
                }
                else {
                    img_Light_3.setImageResource(R.drawable.light_off);
                    tvLight_Lux3.setText(tv_lux3);
                    setApplianceByName_switch("light_3","0");
                    closeAutoControl("light_3-button","0","auto_dimming_1");
                    action("DimmerControl","67","0");
                }
            }
        });
        sw_Light_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sw_Light_4.isChecked()){
                    img_Light_4.setImageResource(R.drawable.light_on);
                    tvLight_Lux4.setText(tv_lux4);
                    setApplianceByName_switch("light_4","1");
                    closeAutoControl("light_4-button","0","auto_dimming_1");
                    action("DimmerControl","68","1");
                }
                else {
                    img_Light_4.setImageResource(R.drawable.light_off);
                    tvLight_Lux4.setText(tv_lux4);
                    setApplianceByName_switch("light_4","0");
                    closeAutoControl("light_4-button","0","auto_dimming_1");
                    action("DimmerControl","68","0");
                }
            }
        });
        sw_Light_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sw_Light_5.isChecked()){
                    img_Light_5.setImageResource(R.drawable.light_on);
                    tvLight_Lux5.setText(tv_lux5);
                    setApplianceByName_switch("light_5","1");
                    closeAutoControl("light_5-button","0","auto_dimming_1");
                    action("DimmerControl","69","1");
                }
                else {
                    img_Light_5.setImageResource(R.drawable.light_off);
                    tvLight_Lux5.setText(tv_lux5);
                    setApplianceByName_switch("light_5","0");
                    closeAutoControl("light_5-button","0","auto_dimming_1");
                    action("DimmerControl","69","0");
                }
            }
        });
        sw_Light_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sw_Light_6.isChecked()){
                    img_Light_6.setImageResource(R.drawable.light_on);
                    tvLight_Lux6.setText(tv_lux6);
                    setApplianceByName_switch("light_6","1");
                    closeAutoControl("light_6-button","0","auto_dimming_1");
                    action("DimmerControl","70","1");
                }
                else {
                    img_Light_6.setImageResource(R.drawable.light_off);
                    tvLight_Lux6.setText(tv_lux6);
                    setApplianceByName_switch("light_6","0");
                    closeAutoControl("light_6-button","0","auto_dimming_1");
                    action("DimmerControl","70","0");
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
                closeAutoControl("light_1-slider","0","auto_dimming_2");
                action("DimmerControl","65",tv_lux1);
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
                closeAutoControl("light_2-slider","0","auto_dimming_2");
                action("DimmerControl","66",tv_lux2);
            }
        });
        sb_Light_3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvLight_Lux3.setText(String.valueOf(progress+2));
                tv_lux3 = String.valueOf(progress+2);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setApplianceByName_setting("light_3",tv_lux3);
                closeAutoControl("light_3-slider","0","auto_dimming_1");
                action("DimmerControl","67",tv_lux3);
            }
        });
        sb_Light_4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvLight_Lux4.setText(String.valueOf(progress+2));
                tv_lux4 = String.valueOf(progress+2);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setApplianceByName_setting("light_4",tv_lux4);
                closeAutoControl("light_4-slider","0","auto_dimming_1");
                action("DimmerControl","68",tv_lux4);
            }
        });
        sb_Light_5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvLight_Lux5.setText(String.valueOf(progress+2));
                tv_lux5 = String.valueOf(progress+2);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setApplianceByName_setting("light_5",tv_lux5);
                closeAutoControl("light_5-slider","0","auto_dimming_1");
                action("DimmerControl","69",tv_lux5);
            }
        });
        sb_Light_6.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvLight_Lux6.setText(String.valueOf(progress+2));
                tv_lux6 = String.valueOf(progress+2);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setApplianceByName_setting("light_6",tv_lux6);
                closeAutoControl("light_6-slider","0","auto_dimming_1");
                action("DimmerControl","70",tv_lux6);
            }
        });
    }

    // 各電器開關
    private void setApplianceByName_switch(String name, String sw) {
        final JSONObject body = new JSONObject();
        try {
            body.put("action", "setHemsAppliance");
            body.put("field","92702");
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
            body.put("field","92702");
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

    private void sw_findViewById() {
        sw_Auto_Comfort1 = getView().findViewById(R.id.sw_auto_comfort1);
        sw_Auto_Comfort2 = getView().findViewById(R.id.sw_auto_comfort2);
        sw_Auto_Light1 = getView().findViewById(R.id.sw_auto_light1);
        sw_Auto_Light2 = getView().findViewById(R.id.sw_auto_light2);
        sw_Motion = getView().findViewById(R.id.sw_motion);
        //自動舒適度
        sw_Auto_Comfort1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sw_Auto_Comfort1.isChecked()){
                    //Toast.makeText(getActivity(),"自动舒适开",Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(),"自動舒適-區域一開",Toast.LENGTH_SHORT).show();
                    setAutoRoomHotKey("auto_comfort_1","1");
                }
                else {
                    //Toast.makeText(getActivity(),"自动舒适关",Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(),"自動舒適-區域一關",Toast.LENGTH_SHORT).show();
                    setAutoRoomHotKey("auto_comfort_1","0");
                }
            }
        });
        sw_Auto_Comfort2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sw_Auto_Comfort2.isChecked()){
                    //Toast.makeText(getActivity(),"自动舒适开",Toast.LENGTH_SHORT).show();
                    setAutoRoomHotKey("auto_comfort_2","1");
                    Toast.makeText(getActivity(),"自動舒適-區域二開",Toast.LENGTH_SHORT).show();
                }
                else {
                    //Toast.makeText(getActivity(),"自动舒适关",Toast.LENGTH_SHORT).show();
                    setAutoRoomHotKey("auto_comfort_2","0");
                    Toast.makeText(getActivity(),"自動舒適-區域二關",Toast.LENGTH_SHORT).show();
                }
            }
        });
       sw_Auto_Light1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(sw_Auto_Light1.isChecked()){
                   //Toast.makeText(getActivity(),"自动调光开",Toast.LENGTH_SHORT).show();
                   Toast.makeText(getActivity(),"自動調光-區域一開",Toast.LENGTH_SHORT).show();
                   setAutoRoomHotKey("auto_dimming_1","1");
               }
               else {
                   //Toast.makeText(getActivity(),"自动调光关",Toast.LENGTH_SHORT).show();
                   Toast.makeText(getActivity(),"自動調光-區域一關",Toast.LENGTH_SHORT).show();
                   setAutoRoomHotKey("auto_dimming_1","0");
               }
           }
       });
       sw_Auto_Light2.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(sw_Auto_Light2.isChecked()){
                   //Toast.makeText(getActivity(),"自动调光开",Toast.LENGTH_SHORT).show();
                   Toast.makeText(getActivity(),"自動調光-區域二開",Toast.LENGTH_SHORT).show();
                   setAutoRoomHotKey("auto_dimming_2","1");
               }
               else {
                   //Toast.makeText(getActivity(),"自动调光关",Toast.LENGTH_SHORT).show();
                   Toast.makeText(getActivity(),"自動調光-區域二關",Toast.LENGTH_SHORT).show();
                   setAutoRoomHotKey("auto_dimming_2","0");
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
                   // Toast.makeText(getActivity(),"移动感测器关",Toast.LENGTH_SHORT).show();
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
        img_Auto_Comfort1 = getView().findViewById(R.id.img_auto_comfort1);
        img_Auto_Comfort2 = getView().findViewById(R.id.img_auto_comfort2);
        img_Auto_Light1 = getView().findViewById(R.id.img_auto_light1);
        img_Auto_Light2 = getView().findViewById(R.id.img_auto_light2);
        img_Motion_1 = getView().findViewById(R.id.img_motion_1); // 左
        img_Motion_2 = getView().findViewById(R.id.img_motion_2); // 門
        img_Motion_3 = getView().findViewById(R.id.img_motion_3); // 右
        img_Motion_4 = getView().findViewById(R.id.img_motion_4); // 中
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
        LLplug = getView().findViewById(R.id.layout_plug);
        LLHplug = getView().findViewById(R.id.layout_hide_plug);
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
        LLplug.setOnClickListener(new View.OnClickListener() {
            boolean visible;
            @Override
            public void onClick(View v) {
                visible = !visible;
                LLHplug.setVisibility(visible ? View.VISIBLE : View.GONE);
                tvCount6.setText(visible ? "-" : "+");
            }
        });
        //一鍵全開or關
        img_All_Light_On.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(),"灯光全开",Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(),"燈光全開",Toast.LENGTH_SHORT).show();
                setHemsApplianceByName("light","1");
                setHemsRoomHotKey("light","1","auto_dimming");
                action("DimmerControl","65","1");
                action("DimmerControl","66","1");
                action("DimmerControl","67","1");
                action("DimmerControl","68","1");
                action("DimmerControl","69","1");
                action("DimmerControl","70","1");
            }
        });
        img_All_Light_Off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getActivity(),"灯光全关",Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(),"燈光全關",Toast.LENGTH_SHORT).show();
                setHemsApplianceByName("light","0");
                setHemsRoomHotKey("light","0","auto_dimming");
                action("DimmerControl","65","0");
                action("DimmerControl","66","0");
                action("DimmerControl","67","0");
                action("DimmerControl","68","0");
                action("DimmerControl","69","0");
                action("DimmerControl","70","0");
            }
        });
        img_All_Ac_On.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getActivity(),"空调全开",Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(),"空調全開",Toast.LENGTH_SHORT).show();
                setHemsApplianceByName("ac","1");
                setHemsRoomHotKey("ac","1","auto_comfort");
                action("AirConditioner","1","1");
                action("AirConditioner","2","1");
                action("","","1"); // EL　ＡC
            }
        });
        img_All_Ac_Off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getActivity(),"空调全关",Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(),"空調全關",Toast.LENGTH_SHORT).show();
                setHemsApplianceByName("ac","0");
                setHemsRoomHotKey("ac","0","auto_comfort");
                action("AirConditioner","1","0");
                action("AirConditioner","2","0");
                action("","","0"); // EL　ＡC

            }
        });
        img_All_Fan_On.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(),"风扇全开",Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(),"風扇全開",Toast.LENGTH_SHORT).show();
                setHemsApplianceByName("fan","1");
                setHemsRoomHotKey("fan","1","auto_comfort");
                action("Fan","1","1");
                action("Fan","2","1");
                action("Fan","3","1");
                action("Fan","4","1");
                action("Fan","5","1");
                action("Fan","10","1");
            }
        });
        img_All_Fan_Off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getActivity(),"风扇全关",Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(),"風扇全關",Toast.LENGTH_SHORT).show();
                setHemsApplianceByName("fan","0");
                setHemsRoomHotKey("fan","0","auto_comfort");
                action("Fan","1","0");
                action("Fan","2","0");
                action("Fan","3","0");
                action("Fan","4","0");
                action("Fan","5","0");
                action("Fan","10","0");
            }
        });
        //舒適度設定
        img_Auto_Comfort1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingedit.putString("comfort_title","92702_1").commit();
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
        img_Auto_Comfort2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingedit.putString("comfort_title","92702_2").commit();
                FragmentTransaction comfortSetting2 = getFragmentManager().beginTransaction();
                Fragment comfort2 = getFragmentManager().findFragmentByTag("ComfortSettingFragment");
                if (comfort2 != null) {
                    comfortSetting2.remove(comfort2);
                }
                comfortSetting2.addToBackStack(null);
                // Create and show the dialog.
                DialogFragment newFragment_comfort2 = new ComfortSettingFragment();
                newFragment_comfort2.show(comfortSetting2, "ComfortSettingFragment");
            }
        });
        //自動調光
        img_Auto_Light1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingedit.putString("dim_title","92702_1").commit();
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
        img_Auto_Light2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingedit.putString("dim_title","92702_2").commit();
                FragmentTransaction dimSetting2 = getFragmentManager().beginTransaction();
                Fragment dim2 = getFragmentManager().findFragmentByTag("DimSettingFragment");
                if (dim2 != null) {
                    dimSetting2.remove(dim2);
                }
                dimSetting2.addToBackStack(null);
                // Create and show the dialog.
                DialogFragment newFragment_dom2 = new DimSettingFragment();
                newFragment_dom2.show(dimSetting2, "DimSettingFragment");
            }
        });
        //移動感測器
        img_Motion_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingedit.putString("motion_title","92702_1").commit();
                FragmentTransaction motionSetting_1 = getFragmentManager().beginTransaction();
                Fragment motion_1 = getFragmentManager().findFragmentByTag("MotionSettingFragment");
                if (motion_1 != null) {
                    motionSetting_1.remove(motion_1);
                }
                motionSetting_1.addToBackStack(null);
                // Create and show the dialog.
                DialogFragment newFragment_motion = new MotionSettingFragment();
                newFragment_motion.show( motionSetting_1, "MotionSettingFragment");
            }
        });
        img_Motion_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingedit.putString("motion_title","92702_2").commit();
                FragmentTransaction motionSetting_2 = getFragmentManager().beginTransaction();
                Fragment motion_2 = getFragmentManager().findFragmentByTag("MotionSettingFragment");
                if (motion_2 != null) {
                    motionSetting_2.remove(motion_2);
                }
                motionSetting_2.addToBackStack(null);
                // Create and show the dialog.
                DialogFragment newFragment_motion = new MotionSettingFragment();
                newFragment_motion.show(motionSetting_2, "MotionSettingFragment");
            }
        });
        img_Motion_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingedit.putString("motion_title","92702_3").commit();
                FragmentTransaction motionSetting_3 = getFragmentManager().beginTransaction();
                Fragment motion_3 = getFragmentManager().findFragmentByTag("MotionSettingFragment");
                if (motion_3 != null) {
                    motionSetting_3.remove(motion_3);
                }
                motionSetting_3.addToBackStack(null);
                // Create and show the dialog.
                DialogFragment newFragment_motion = new MotionSettingFragment();
                newFragment_motion.show(motionSetting_3, "MotionSettingFragment");
            }
        });
        img_Motion_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingedit.putString("motion_title","92702_4").commit();
                FragmentTransaction motionSetting_4 = getFragmentManager().beginTransaction();
                Fragment motion_4 = getFragmentManager().findFragmentByTag("MotionSettingFragment");
                if (motion_4 != null) {
                    motionSetting_4.remove(motion_4);
                }
                motionSetting_4.addToBackStack(null);
                // Create and show the dialog.
                DialogFragment newFragment_motion = new MotionSettingFragment();
                newFragment_motion.show(motionSetting_4, "MotionSettingFragment");
            }
        });

    }

    // 自動調光、舒適度開關副程式
    private void setAutoRoomHotKey(String id, String sw) {
        final JSONObject body = new JSONObject();
        try {
            body.put("action", "setHemsRoomHotKey");
            body.put("field","92702");
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
            body.put("field","92702");
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
            body.put("field","92702");
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
            body.put("field","92702");
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
            body.put("field","92702");
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
            body.put("field","92702");
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
