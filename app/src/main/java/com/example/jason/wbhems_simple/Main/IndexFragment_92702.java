package com.example.jason.wbhems_simple.Main;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jason.wbhems_simple.Chart;
import com.example.jason.wbhems_simple.ChartDataAdapter;
import com.example.jason.wbhems_simple.Home.ElePriceFragment;
import com.example.jason.wbhems_simple.Home.PowerAcFragment;
import com.example.jason.wbhems_simple.Home.PowerExhaustFragment;
import com.example.jason.wbhems_simple.Home.PowerFanFragment;
import com.example.jason.wbhems_simple.Home.PowerLightFragment;
import com.example.jason.wbhems_simple.R;
import com.example.jason.wbhems_simple.listviewitems.BarChartItem;
import com.example.jason.wbhems_simple.listviewitems.ChartItem;
import com.example.jason.wbhems_simple.listviewitems.LineChartItem;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class IndexFragment_92702 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context context;
    private LinearLayout LLinfo, LLHinfo, LLdr, LLHdr, LLchart, LLHchart;
    private LinearLayout Lpower_ac, Lpower_all,Lprice_month;
    private TextView tvCount1, tvCount2, tvCount3;
    private TextView tvWeather,tvOutdoor_temp,tvIndoor_temp1,tvIndoor_temp2,tvIndoor_rh1,tvIndoor_rh2,tvIndoor_CO21,tvIndoor_CO22,
                    tvIndoor_lux1, tvIndoor_lux2,tvIndoor_lux3,tvIndoor_lux4,tvIndoor_lux5; //  天氣/室內資訊
    private TextView tvStart_at,tvEnd_at,tv_Exdrop,tv_Droptime; //近期節電行動資訊
    private TextView tvPrice,immediatePower_office,tvPower_all,tvPower_ac,tvPower_plug; // 即時用電量
    private Button btnNow, btnDay, btnMonth, btnYear;
    private ListView lv;
    private Chart chart;
    private RequestQueue requestQueue;
    private StringRequest postRequest;
    private String url_room = "http://140.116.163.19:10107/epslab_ems/api/room.php";
    private String url_dr = "http://140.116.163.19:10107/epslab_ems/api/dr.php";
    private String url_general = "http://140.116.163.19:10107/epslab_ems/api/general.php";
    private String url_ami = "http://140.116.163.19:10107/epslab_ems/api/ami.php";
    private String url_electricity = "http://140.116.163.19:10107/epslab_ems/api/electricity.php";
    private int mYear, mMonth, mDay;
    private EditText txtTime;
    private SharedPreferences setting;
    private SharedPreferences.Editor settingedit;
    String User,Token;
    private int colors[] = {Color.rgb(255,0,0),Color.rgb(0,0,205),
            Color.rgb(51,201,51),Color.rgb(255,215,0),Color.rgb(191,128,64)};
    private ArrayList<Entry>[] totalList_line;
    private ArrayList[] totalList_bar;
    String Chart_id[] = {"空調1","空調2","照明、風扇、排風扇","插座","總和"};
    private Timer timer;
    public IndexFragment_92702() {
        // Required empty public constructor
    }

    public static IndexFragment_92702 newInstance(String param1, String param2) {
        IndexFragment_92702 fragment = new IndexFragment_92702();
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
        return inflater.inflate(R.layout.fragment_index_92702, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestQueue = Volley.newRequestQueue(getActivity());
        setting = this.getActivity().getSharedPreferences("auto",0);
        User = setting.getString("User", "");
        Token = setting.getString("TOKEN","");
        settingedit = setting.edit();
        context = getActivity();
        tvWeather = getView().findViewById(R.id.tv_weather);
        tvOutdoor_temp = getView().findViewById(R.id.tv_outdoor_temp);
        tvIndoor_temp1 = getView().findViewById(R.id.tv_indoor_temp1);
        tvIndoor_temp2 = getView().findViewById(R.id.tv_indoor_temp2);
        tvIndoor_rh1 = getView().findViewById(R.id.tv_indoor_rh1);
        tvIndoor_rh2 = getView().findViewById(R.id.tv_indoor_rh2);
        tvIndoor_CO21 = getView().findViewById(R.id.tv_indoor_CO21);
        tvIndoor_CO22 = getView().findViewById(R.id.tv_indoor_CO22);
        tvIndoor_lux1 = getView().findViewById(R.id.tv_indoor_lux1);
        tvIndoor_lux2 = getView().findViewById(R.id.tv_indoor_lux2);
        tvIndoor_lux3 = getView().findViewById(R.id.tv_indoor_lux3);
        tvIndoor_lux4 = getView().findViewById(R.id.tv_indoor_lux4);
        tvIndoor_lux5 = getView().findViewById(R.id.tv_indoor_lux5);
        findViewById();
        sample();
        getDREvents();
        //宣告Timer
        timer =new Timer();
        MyTask task = new MyTask();
        //設定Timer(task為執行內容，0代表立刻開始,間格3秒執行一次)
        timer.schedule(task, 0,3000);
    }
    class MyTask extends TimerTask{
        @Override
        public void run(){
            // TODO Auto-generated method stub
            getSensor(); // 室內資訊
            getGeneral(); // 天氣資訊
            getElectricity(); // 累積用電and累積電費
            getPower(); // 取得及時用電資料
        }
    }

    private void getElectricity() {
        final JSONObject body = new JSONObject();
        try {
            body.put("action", "getHemsElectricityFee");
            body.put("field",User);
            body.put("token",Token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        postRequest = new StringRequest(Request.Method.POST, url_electricity, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    Log.d("92702_electricity", object.toString());
                    JSONArray data = object.getJSONArray("data");
                    int num = data.length();
                    String electricity_fee = data.getJSONObject(num-1).getString("month_fee"); // 累積電費(元)
                    String accumulate_consumption = data.getJSONObject(num-1).getString("consumption"); // 累積用電(度)
                    settingedit.putString("price_92702",accumulate_consumption).commit();
                    tvPrice.setText(String.format("%.01f", Float.valueOf(electricity_fee))+"  元");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError_electricity", error.toString());
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

    private void getGeneral() {
        final JSONObject body = new JSONObject();
        try {
            body.put("action", "getBemsHomepageInformation");
            body.put("field","NCKU");
            body.put("token",Token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        postRequest = new StringRequest(Request.Method.POST, url_general, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    Log.d("92702_general", object.toString());
                    JSONArray data = object.getJSONArray("data");
                    String temp = data.getJSONObject(0).getString("temperature");
                    String weather = data.getJSONObject(0).getString("weather_status");
                    tvOutdoor_temp.setText(String.format("%.01f", Float.valueOf(temp)));
                    switch(weather){
                        case "1":
                            tvWeather.setText("晴");
                            break;
                        case "2":
                            tvWeather.setText("陰");
                            break;
                        case "3":
                            tvWeather.setText("雨");
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError_weather", error.toString());
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

    private void getPower() {
        final JSONObject body = new JSONObject();
        try {
            body.put("action", "getHemsConsumptionDisplay");
            body.put("field","92702");
            body.put("token",Token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        postRequest = new StringRequest(Request.Method.POST, url_ami, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    Log.d("92702，Hoem_ami", object.toString());
                    JSONArray data = object.getJSONArray("data");
                    String power_ac1 = data.getJSONObject(0).getString("total_kw");
                    String power_ac2 = data.getJSONObject(1).getString("total_kw");
                    String power_all = data.getJSONObject(2).getString("total_kw");
                    String power_plug = data.getJSONObject(3).getString("total_kw");
                    String power_office = String.format("%.03f", Float.valueOf(power_ac1)+Float.valueOf(power_ac2)+Float.valueOf(power_all)+Float.valueOf(power_plug));
                    settingedit.putString("power_ac1",power_ac1).commit();
                    settingedit.putString("power_ac2",power_ac2).commit();
                    tvPower_ac.setText(String.format("%.03f", Float.valueOf(power_ac1)+Float.valueOf(power_ac2))+"  kW");
                    tvPower_all.setText(power_all+"  kW");
                    tvPower_plug.setText(power_plug+"  kW");
                    immediatePower_office.setText(power_office+"  kW");
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
        requestQueue.add(postRequest);
    }

    private void getSensor() {
        final JSONObject body = new JSONObject();
        try {
            body.put("action", "getHemsSensor");
            body.put("field","92702");
            body.put("token",Token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("92702，getSensor_body", body.toString());
        postRequest = new StringRequest(Request.Method.POST, url_room, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    Log.d("92702，getSensor", object.toString());
                    JSONObject data = object.getJSONObject("data");
                    String indoor_temp1 = data.getJSONArray("list").getJSONObject(0).getString("value");
                    String indoor_temp2 = data.getJSONArray("list").getJSONObject(1).getString("value");
                    String indoor_rh1 = data.getJSONArray("list").getJSONObject(2).getString("value");
                    String indoor_rh2 = data.getJSONArray("list").getJSONObject(3).getString("value");
                    String indoor_CO21 = data.getJSONArray("list").getJSONObject(4).getString("value");
                    String indoor_CO22 = data.getJSONArray("list").getJSONObject(5).getString("value");
                    String indoor_lux1 = data.getJSONArray("list").getJSONObject(6).getString("value"); // 照度值(窗戶中柱)
                    String indoor_lux2 = data.getJSONArray("list").getJSONObject(7).getString("value"); // 照度值(窗戶左柱)
                    String indoor_lux3 = data.getJSONArray("list").getJSONObject(8).getString("value"); // 照度值(配電盤)
                    String indoor_lux4 = data.getJSONArray("list").getJSONObject(9).getString("value"); // 照度值(門左)
                    String indoor_lux5 = data.getJSONArray("list").getJSONObject(10).getString("value"); // 照度值(門右側)
                    tvIndoor_temp1.setText(String.format("%.02f", Float.valueOf(indoor_temp1)));
                    tvIndoor_temp2.setText(String.format("%.02f", Float.valueOf(indoor_temp2)));
                    tvIndoor_rh1.setText(String.format("%.02f", Float.valueOf(indoor_rh1)));
                    tvIndoor_rh2.setText(String.format("%.02f", Float.valueOf(indoor_rh2)));
                    tvIndoor_CO21.setText(indoor_CO21);
                    tvIndoor_CO22.setText(indoor_CO22);
                    tvIndoor_lux1.setText(indoor_lux1);
                    tvIndoor_lux2.setText(indoor_lux2);
                    tvIndoor_lux3.setText(indoor_lux3);
                    tvIndoor_lux4.setText(indoor_lux4);
                    tvIndoor_lux5.setText(indoor_lux5);
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
        requestQueue.add(postRequest);
    }

    private void getDREvents() {
        tvStart_at = getView().findViewById(R.id.tv_start_at);
        tvEnd_at = getView().findViewById(R.id.tv_end_at);
        tv_Exdrop = getView().findViewById(R.id.tv_exdrop);
        tv_Droptime = getView().findViewById(R.id.tv_droptime);
        final JSONObject body = new JSONObject();
        try {
            body.put("action", "getHemsDrEvent");
            body.put("field",User);
            body.put("token",Token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        postRequest = new StringRequest(Request.Method.POST, url_dr, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("body", body.toString());
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    Log.d("Response_DrEvent", object.toString());
                    JSONObject data = object.getJSONObject("data");
                    JSONArray list = data.getJSONArray("list");
                    int num = data.getJSONArray("list").length();
                    Log.d("Event_Num",String.valueOf(num));
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    formatter.setLenient(false);
                    Date now = new Date() ; // 獲取當前時間
                    for(int i = num-1; i > -1; i--){
                        String[] start = data.getJSONArray("list").getJSONObject(i).getString("start_time").split(" ");
                        String[] end = data.getJSONArray("list").getJSONObject(i).getString("end_time").split(" ");
                        Date dr_start = formatter.parse(data.getJSONArray("list").getJSONObject(i).getString("start_time"));
                        Date dr_end = formatter.parse(data.getJSONArray("list").getJSONObject(i).getString("end_time"));
                        //取得兩個時間的Unix時間
                        Long now1 = now.getTime();
                        Long dr_start1 = dr_start.getTime();
                        Long dr_end1 = dr_end.getTime();  //相減獲得兩個時間差距的毫秒
                        Long End_Now = dr_end1 - now1;  //毫秒差 (結束時間 - 現在時間)
                        if (End_Now > 0) {  //結束時間大於現在時間
                            Long Start_Now = dr_start1 - now1;  //毫秒差
                            if (Start_Now > 0) {
                                //開始時間大於現在時間，把事件歸類未來事件
                            } else {
                                //開始時間小於現在時間，把事件歸類到當前事件
                            }
                        } else {
                            //結束時間小於現在時間，把事件歸類到歷史事件
                            HashMap<String, String> item = new HashMap<String, String>();
                            tvStart_at.setText(start[0]+" "+start[1]);
                            tvEnd_at.setText( end[0]+" "+end[1]);
                            if(list.getJSONObject(i).getString("amount").equals("null")){ // 如果為空值
                                tv_Exdrop.setText("--");
                            }else{
                                tv_Exdrop.setText(list.getJSONObject(i).getString("amount")); // 預計抑低量 (瓩)
                            }
                            if(list.getJSONObject(i).getString("duration").equals("null")){ // 如果為空值
                                tv_Droptime.setText("--");
                            }else{
                                tv_Droptime.setText(list.getJSONObject(i).getString("duration")); // 抑低時間 (分)
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError_DR", error.toString());
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

    private void findViewById(){
        LLinfo = getView().findViewById(R.id.layout_info);
        LLHinfo = getView().findViewById(R.id.layout_hide_info);
        LLdr = getView().findViewById(R.id.layout_dr);
        LLHdr = getView().findViewById(R.id.layout_hide_dr);
        LLchart = getView().findViewById(R.id.layout_chart);
        LLHchart = getView().findViewById(R.id.layout_hide_chart);
        Lpower_ac = getView().findViewById(R.id.power_ac);
        Lprice_month = getView().findViewById(R.id.price_month);
        tvPrice = getView().findViewById(R.id.tv_month_powerBill);
        immediatePower_office = getView().findViewById(R.id.tv_immediatePower_office);
        tvPower_all = getView().findViewById(R.id.tv_immediatePower_all);
        tvPower_ac = getView().findViewById(R.id.tv_immediatePower_ac);
        tvPower_plug = getView().findViewById(R.id.tv_immediatePower_plug);
        tvCount1 = getView().findViewById(R.id.tv_count);
        tvCount2 = getView().findViewById(R.id.tv_count2);
        tvCount3 = getView().findViewById(R.id.tv_count3);
        chart = new Chart(getActivity());
        lv = (ListView) getView().findViewById(R.id.listView1);
        txtTime = (EditText) getView().findViewById(R.id.tv_time);
        btnNow = (Button) getView().findViewById(R.id.btn_now);
        btnDay = (Button) getView().findViewById(R.id.btn_day);
        btnMonth = (Button) getView().findViewById(R.id.btn_month);
        btnYear = (Button) getView().findViewById(R.id.btn_year);
        LLinfo.setOnClickListener(new View.OnClickListener() {
            boolean visible;
            @Override
            public void onClick(View v) {
                visible = !visible;
                LLHinfo.setVisibility(visible ? View.VISIBLE : View.GONE);
                tvCount1.setText(visible ? "-" : "+");
            }
        });
        LLdr.setOnClickListener(new View.OnClickListener() {
            boolean visible;
            @Override
            public void onClick(View v) {
                visible = !visible;
                LLHdr.setVisibility(visible ? View.VISIBLE : View.GONE);
                tvCount2.setText(visible ? "-" : "+");
            }
        });
        LLchart.setOnClickListener(new View.OnClickListener() {
            boolean visible;
            @Override
            public void onClick(View v) {
                visible = !visible;
                LLHchart.setVisibility(visible ? View.VISIBLE : View.GONE);
                tvCount3.setText(visible ? "-" : "+");
            }
        });
        Lpower_ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction powerAC = getFragmentManager().beginTransaction();
                Fragment AC = getFragmentManager().findFragmentByTag("PowerAcFragment");
                if (AC != null) {
                    powerAC.remove(AC);
                }
                powerAC.addToBackStack(null);
                // Create and show the dialog.
                DialogFragment newFragment = new PowerAcFragment();
                newFragment.show(powerAC, "PowerAcFragment");
            }
        });
        Lprice_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction priceMonth = getFragmentManager().beginTransaction();
                Fragment Price = getFragmentManager().findFragmentByTag("ElePriceFragment");
                if (Price != null) {
                    priceMonth.remove(Price);
                }
                priceMonth.addToBackStack(null);
                // Create and show the dialog.
                DialogFragment newFragment = new ElePriceFragment();
                newFragment.show(priceMonth, "ElePriceFragment");
            }
        });
        btnNow.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date curDate = new Date(System.currentTimeMillis()) ; // 獲取當前時間
                String[] date = formatter.format(curDate).split("-");
                Log.d("now", formatter.format(curDate));
                txtTime.setVisibility(View.GONE);
                getChart("now", Integer.valueOf(date[0]), Integer.valueOf(date[1])-1, Integer.valueOf(date[2]));
            }
        });
        btnDay.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String format = setDateFormat(year,month,day);
                        txtTime.setVisibility(View.VISIBLE);
                        txtTime.setText(format);
                        getChart("day", year, month, day);
                    }

                }, mYear,mMonth, mDay).show();
            }
        });
        btnMonth.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String format = setMonthFormat(year,month);
                        txtTime.setVisibility(View.VISIBLE);
                        txtTime.setText(format);
                        getChart("month", year, month, 0);
                    }

                }, mYear,mMonth, mDay).show();
            }
        });
        btnYear.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String format = String.valueOf(year);
                        txtTime.setVisibility(View.VISIBLE);
                        txtTime.setText(format);
                        getChart("year", year, 0, 0);
                    }

                }, mYear,mMonth, mDay).show();
            }
        });
    }

    private ArrayList<String> Time() {
        ArrayList<String> m = new ArrayList<String>();
        m.add("00:00");
        m.add("00:15");
        m.add("00:30");
        m.add("00:45");
        m.add("01:00");
        m.add("01:15");
        m.add("01:30");
        m.add("01:45");
        m.add("02:00");
        m.add("02:15");
        m.add("02:30");
        m.add("02:45");
        m.add("03:00");
        m.add("03:15");
        m.add("03:30");
        m.add("03:45");
        m.add("04:00");
        m.add("04:15");
        m.add("04:30");
        m.add("04:45");
        m.add("05:00");
        m.add("05:15");
        m.add("05:30");
        m.add("05:45");
        m.add("06:00");
        m.add("06:15");
        m.add("06:30");
        m.add("06:45");
        m.add("07:00");
        m.add("07:15");
        m.add("07:30");
        m.add("07:45");
        m.add("08:00");
        m.add("08:15");
        m.add("08:30");
        m.add("08:45");
        m.add("09:00");
        m.add("09:15");
        m.add("09:30");
        m.add("09:45");
        m.add("10:00");
        m.add("10:15");
        m.add("10:30");
        m.add("10:45");
        m.add("11:00");
        m.add("11:15");
        m.add("11:30");
        m.add("11:45");
        m.add("12:00");
        m.add("12:15");
        m.add("12:30");
        m.add("12:45");
        m.add("13:00");
        m.add("13:15");
        m.add("13:30");
        m.add("13:45");
        m.add("14:00");
        m.add("14:15");
        m.add("14:30");
        m.add("14:45");
        m.add("15:00");
        m.add("15:15");
        m.add("15:30");
        m.add("15:45");
        m.add("16:00");
        m.add("16:15");
        m.add("16:30");
        m.add("16:45");
        m.add("17:00");
        m.add("17:15");
        m.add("17:30");
        m.add("17:45");
        m.add("18:00");
        m.add("18:15");
        m.add("18:30");
        m.add("18:45");
        m.add("19:00");
        m.add("19:15");
        m.add("19:30");
        m.add("19:45");
        m.add("20:00");
        m.add("20:15");
        m.add("20:30");
        m.add("20:45");
        m.add("21:00");
        m.add("21:15");
        m.add("21:30");
        m.add("21:45");
        m.add("22:00");
        m.add("22:15");
        m.add("22:30");
        m.add("22:45");
        m.add("23:00");
        m.add("23:15");
        m.add("23:30");
        m.add("23:45");
        return m;
    }

    private ArrayList<Entry> LinePower() {
        ArrayList<Entry> m = new ArrayList<Entry>();
        for (int i = 0; i < 10; i++){
            m.add(new Entry(i, Float.parseFloat("1")));
        }
        return m;
    }

    public void sample() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis()) ; // 獲取當前時間
        String[] date = formatter.format(curDate).split("-");
        Log.d("now", formatter.format(curDate));
        txtTime.setVisibility(View.GONE);
        getChart("now", Integer.valueOf(date[0]), Integer.valueOf(date[1])-1, Integer.valueOf(date[2]));
    }

    public void getChart(final String time, int year, int month, int day) {
        final JSONObject body = new JSONObject();
        try {
            body.put("action", "getHemsConsumptionHistories");
            body.put("field", User);
            body.put("time",time);
            body.put("token", Token);
            switch (time){
                case "year":
                    body.put("year", String.valueOf(year));
                    break;
                case "month":
                    body.put("year", String.valueOf(year));
                    body.put("month", String.valueOf(month+1));
                    break;
                case "day":
                    body.put("year", String.valueOf(year));
                    body.put("month", String.valueOf(month+1));
                    body.put("day", String.valueOf(day));
                    break;
            }
        } catch (JSONException e) {
            Log.d("getChart_1", String.valueOf(e));
        }

        postRequest = new StringRequest(Request.Method.POST, url_ami, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<ChartItem> list = new ArrayList<ChartItem>();
                totalList_bar = new ArrayList[5];
                totalList_line = new ArrayList[5];
                ArrayList<BarEntry> BarEntries_ac1 = new ArrayList<BarEntry>();// 空調1長條圖
                ArrayList<Entry> LineEntries_ac1 = new ArrayList<Entry>();     // 空調1折線圖
                ArrayList<BarEntry> BarEntries_ac2 = new ArrayList<BarEntry>();// 空調2長條圖
                ArrayList<Entry> LineEntries_ac2 = new ArrayList<Entry>();     // 空調2折線圖
                ArrayList<BarEntry> BarEntries_appliance = new ArrayList<BarEntry>();// 照明、風扇、排風扇長條圖
                ArrayList<Entry> LineEntries_appliance = new ArrayList<Entry>();     // 照明、風扇、排風扇折線圖
                ArrayList<BarEntry> BarEntries_plug = new ArrayList<BarEntry>();// 插座長條圖
                ArrayList<Entry> LineEntries_plug = new ArrayList<Entry>();     // 插座折線圖
                ArrayList<BarEntry> BarEntries_all = new ArrayList<BarEntry>(); // 總和長條圖
                ArrayList<Entry> LineEntries_all = new ArrayList<Entry>();      // 總和折線圖
                ArrayList<String> array = new ArrayList<String>();// 時間軸資料
                try {
                    Log.d("body", body.toString());
                    int num_date = 0;
                    JSONObject object = new JSONObject(response);
                    Log.d("getChart_response",object.toString());
                    JSONArray data = object.getJSONArray("data");
                    int num = data.length(); // 總資料筆數
                    for(int i = 0; i<num; i++){
                        if(data.getJSONObject(i).getString("name").equals("ac_1")){
                            num_date = num_date+1; // 日期筆數(x軸)
                        }
                    }
                    Log.d("Num_Date", String.valueOf(num_date));
                    for(int j = 0; j<num; j++){
                        switch (time){
                            case "year":
                                if(data.getJSONObject(j).getString("name").equals("ac_1")){
                                    String date = data.getJSONObject(j).getString("month");
                                    array.add(new String(date)+"月"); // x軸:幾月
                                }
                                break;
                            case "month":
                                if(data.getJSONObject(j).getString("name").equals("ac_1")){
                                    String date_month = data.getJSONObject(j).getString("month");
                                    String date_day= data.getJSONObject(j).getString("day");
                                    String Date = date_month+"/"+date_day;
                                    array.add(new String(Date)); // x軸:幾月/幾號
                                }
                                break;
                            case "day":
                                if(data.getJSONObject(j).getString("name").equals("ac_1")){
                                    String date_hour = data.getJSONObject(j).getString("hour");
                                    array.add(new String(date_hour)); // x軸:幾時
                                }
                                break;
                            case "now":
                                if(data.getJSONObject(j).getString("name").equals("ac_1")){
                                    String date_hour1 = data.getJSONObject(j).getString("hour");
                                    String date_min = data.getJSONObject(j).getString("minute");
                                    String time = date_hour1+":"+date_min;
                                    array.add(new String(time)); // x軸:幾時:幾分
                                }
                                break;
                        }
                    }
                    Log.d("array_date", String.valueOf(array));
                    int ac_1 = 0;
                    for (int i = 0;i < num;i++){
                        if(data.getJSONObject(i).getString("name").equals("ac_1")){
                            String power = data.getJSONObject(i).getString("total_kw");
                            BarEntries_ac1.add(new BarEntry(ac_1, Float.parseFloat(power))); // 空調1年、月、日資料
                            LineEntries_ac1.add(new Entry(ac_1, Float.parseFloat(power))); // 空調1即時資料
                            ac_1 = ac_1 + 1;
                        }
                    }
                    int ac_2 = 0;
                    for (int i = 0;i < num;i++){
                        if(data.getJSONObject(i).getString("name").equals("ac_2")){
                            String power = data.getJSONObject(i).getString("total_kw");
                            BarEntries_ac2.add(new BarEntry(ac_2, Float.parseFloat(power))); // 空調1年、月、日資料
                            LineEntries_ac2.add(new Entry(ac_2, Float.parseFloat(power))); // 空調1即時資料
                            ac_2 = ac_2 + 1;
                        }
                    }
                    int appliance = 0;
                    for (int i = 0;i < num;i++){
                        if(data.getJSONObject(i).getString("name").equals("appliance_1")){
                            String power = data.getJSONObject(i).getString("total_kw");
                            BarEntries_appliance.add(new BarEntry(appliance, Float.parseFloat(power)));
                            LineEntries_appliance.add(new Entry(appliance, Float.parseFloat(power)));
                            appliance = appliance + 1;
                        }
                    }
                    int plug = 0;
                    for (int i = 0;i < num;i++){
                        if(data.getJSONObject(i).getString("name").equals("plug_1")){
                            String power = data.getJSONObject(i).getString("total_kw");
                            BarEntries_plug.add(new BarEntry(plug, Float.parseFloat(power)));
                            LineEntries_plug.add(new Entry(plug, Float.parseFloat(power)));
                            plug = plug + 1;
                        }
                    }
                    for(int j = 0; j < num_date; j++) {
                        float p = 0;
                        for (int i = 0; i < num; i++) {
                            switch (time) {
                                case "year":
                                    if ((data.getJSONObject(i).getString("month")+"月").equals(array.get(j))) {
                                        String power = data.getJSONObject(i).getString("total_kw");
                                        p = p + Float.parseFloat(power);
                                        Log.d("PPPP", String.valueOf(p));
                                    }
                                    break;
                                case "month":
                                    if (data.getJSONObject(i).getString("day").equals(array.get(j).split("/")[1])) {
                                        String power = data.getJSONObject(i).getString("total_kw");
                                        p = p + Float.parseFloat(power);
                                        Log.d("PPPP", String.valueOf(p));
                                    }
                                    break;
                                case "day":
                                    if (data.getJSONObject(i).getString("hour").equals(array.get(j))) {
                                        String power = data.getJSONObject(i).getString("total_kw");
                                        p = p + Float.parseFloat(power);
                                        Log.d("PPPP", String.valueOf(p));
                                    }
                                    break;
                                case "now":
                                    if ((data.getJSONObject(i).getString("hour")+":"+
                                            data.getJSONObject(i).getString("minute")).equals(array.get(j))) {
                                        String power = data.getJSONObject(i).getString("total_kw");
                                        p = p + Float.parseFloat(power);
                                        Log.d("PPPP", String.valueOf(p));
                                    }
                                    break;
                            }
                        }
                        BarEntries_all.add(new BarEntry(j, p));
                        LineEntries_all.add(new Entry(j, p));
                    }
                    for(int i = 0; i < Chart_id.length; i++){
                        totalList_bar[i] = new ArrayList<BarEntry>();
                    }
                    for(int i = 0; i < Chart_id.length; i++){
                        totalList_line[i] = new ArrayList<Entry>();
                    }
                    switch (time) {
                        case "year":
                            totalList_bar[0] = BarEntries_ac1;
                            totalList_bar[1] = BarEntries_ac2;
                            totalList_bar[2] = BarEntries_appliance;
                            totalList_bar[3] = BarEntries_plug;
                            totalList_bar[4] = BarEntries_all;
                            list.add(new BarChartItem(chart.generateSixDataBar(Chart_id,totalList_bar,colors), getActivity(), "", array, "kWh", "時間(月份)"));
                            break;
                        case "month":
                            totalList_bar[0] = BarEntries_ac1;
                            totalList_bar[1] = BarEntries_ac2;
                            totalList_bar[2] = BarEntries_appliance;
                            totalList_bar[3] = BarEntries_plug;
                            totalList_bar[4] = BarEntries_all;
                            list.add(new BarChartItem(chart.generateSixDataBar(Chart_id,totalList_bar,colors), getActivity(), "", array, "kWh", "時間(日)"));
                            break;
                        case "day":
                            totalList_bar[0] = BarEntries_ac1;
                            totalList_bar[1] = BarEntries_ac2;
                            totalList_bar[2] = BarEntries_appliance;
                            totalList_bar[3] = BarEntries_plug;
                            totalList_bar[4] = BarEntries_all;
                            list.add(new BarChartItem(chart.generateSixDataBar(Chart_id,totalList_bar,colors), getActivity(), "", array, "kWh", "時間(小時)"));
                            break;
                        case "now":
                            totalList_line[0] = LineEntries_ac1;
                            totalList_line[1] = LineEntries_ac2;
                            totalList_line[2] = LineEntries_appliance;
                            totalList_line[3] = LineEntries_plug;
                            totalList_line[4] = LineEntries_all;
                            list.add(new LineChartItem(chart.generateSixDataLine(Chart_id,totalList_line,colors), getActivity(), "", array, "kW", "時間(分鐘)"));
                            break;
                    }
                    ChartDataAdapter cda = new ChartDataAdapter(getActivity(), list);
                    lv.setAdapter(cda);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Error_addData", String.valueOf(e));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError_getChart", error.toString());
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

    private String setDateFormat(int year,int monthOfYear,int dayOfMonth){
        return String.valueOf(year) + "-"
                + String.valueOf(monthOfYear + 1) + "-"
                + String.valueOf(dayOfMonth);
    }

    private String setMonthFormat(int year,int monthOfYear){
        return String.valueOf(year) + "-"
                + String.valueOf(monthOfYear + 1);
    }
}
