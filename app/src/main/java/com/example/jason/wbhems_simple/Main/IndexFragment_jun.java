package com.example.jason.wbhems_simple.Main;


import android.app.DatePickerDialog;
import android.content.Context;
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

public class IndexFragment_jun extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context context;
    private LinearLayout LLinfo, LLHinfo, LLdr, LLHdr, LLchart, LLHchart;
    private LinearLayout Lpower_ac, Lpower_all, Lpower_plug,Lprice_month;
    private TextView tvCount1, tvCount2, tvCount3;
    private TextView tvWeather,tvOutdoor_temp,tvIndoor_temp,tvIndoor_rh,tvIndoor_CO2, tvIndoor_lux1, tvIndoor_lux2; //  天氣/室內資訊
    private TextView tvStart_at,tvEnd_at,tv_Exdrop,tv_Droptime; //近期節電行動資訊
    private TextView tvPrice,immediatePower_office,tvPower_ac,tvPower_all,tvPower_plug; // 即時用電量
    private Button btnNow, btnDay, btnMonth, btnYear;
    private ListView lv;
    private Chart chart;
    private RequestQueue requestQueue;
    private StringRequest postRequest;
    private String url_room = "http://140.116.163.19:10107/epslab_ems/api/room.php";
    private String url_dr = "http://140.116.163.19:10107/epslab_ems/api/dr.php";
    private String url_general = "http://140.116.163.19:10107/epslab_ems/api/general.php";
    private String url_ami = "http://140.116.163.19:10107/epslab_ems/api/ami.php";
    private int mYear, mMonth, mDay;
    private EditText txtTime;
    private SharedPreferences setting;
    private SharedPreferences.Editor settingedit;
    String User,Token;
    private Timer timer;
    public IndexFragment_jun() {
        // Required empty public constructor
    }

    public static IndexFragment_jun newInstance(String param1, String param2) {
        IndexFragment_jun fragment = new IndexFragment_jun();
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
        return inflater.inflate(R.layout.fragment_index_jun, container, false);
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
        tvIndoor_temp = getView().findViewById(R.id.tv_indoor_temp);
        tvIndoor_rh = getView().findViewById(R.id.tv_indoor_rh);
        tvIndoor_CO2 = getView().findViewById(R.id.tv_indoor_CO2);
        tvIndoor_lux1 = getView().findViewById(R.id.tv_indoor_lux1);
        tvIndoor_lux2 = getView().findViewById(R.id.tv_indoor_lux2);
        findViewById();
        sample();
        getDREvents();
        //宣告Timer
        timer =new Timer();
        //設定Timer(task為執行內容，0代表立刻開始,間格5秒執行一次)
        timer.schedule(task, 0,500000);
    }
    private TimerTask task = new TimerTask(){
        @Override
        public void run() {
            // TODO Auto-generated method stub
            getSensor(); // 室內資訊
            getGeneral(); // 天氣資訊and電費
            getPower(); // 取得及時用電資料
        }

    };
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
                    Log.d("92712_general", object.toString());
                    JSONArray data = object.getJSONArray("data");
                    String temp = data.getJSONObject(0).getString("temperature");
                    String weather = data.getJSONObject(0).getString("weather_status");
                    String electricity_fee = data.getJSONObject(0).getString("electricity_fee"); // 累積電費(元)
                    String accumulate_consumption = data.getJSONObject(0).getString("accumulate_consumption"); // 累積用電(度)
                    settingedit.putString("price",accumulate_consumption).commit();
                    tvOutdoor_temp.setText(String.format("%.01f", Float.valueOf(temp)));
                    tvPrice.setText(String.format("%.01f", Float.valueOf(electricity_fee))+"  元");
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
            body.put("field","92712");
            body.put("token",Token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        postRequest = new StringRequest(Request.Method.POST, url_ami, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    Log.d("92712，Hoem_ami", object.toString());
                    JSONArray data = object.getJSONArray("data");
                    String power_ac1 = data.getJSONObject(0).getString("total_kw");
                    String power_all = data.getJSONObject(1).getString("total_kw");
                    String power_plug = data.getJSONObject(2).getString("total_kw");
                    String power_office = String.format("%.03f", Float.valueOf(power_ac1)+Float.valueOf(power_all)+Float.valueOf(power_plug));
                    tvPower_ac.setText(String.format("%.03f", Float.valueOf(power_ac1))+"  kW");
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
            body.put("field","92712");
            body.put("token",Token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("92712，getSensor_body", body.toString());
        postRequest = new StringRequest(Request.Method.POST, url_room, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    Log.d("92712，getSensor", object.toString());
                    JSONObject data = object.getJSONObject("data");
                    String indoor_temp = data.getJSONArray("list").getJSONObject(0).getString("value");
                    String indoor_rh = data.getJSONArray("list").getJSONObject(1).getString("value");
                    String indoor_CO2 = data.getJSONArray("list").getJSONObject(2).getString("value");
                    String indoor_lux1 = data.getJSONArray("list").getJSONObject(3).getString("value");
                    String indoor_lux2 = data.getJSONArray("list").getJSONObject(4).getString("value");
                    tvIndoor_temp.setText(String.format("%.02f", Float.valueOf(indoor_temp)));
                    tvIndoor_rh.setText(String.format("%.02f", Float.valueOf(indoor_rh)));
                    tvIndoor_CO2.setText(indoor_CO2);
                    tvIndoor_lux1.setText(indoor_lux1);
                    tvIndoor_lux2.setText(indoor_lux2);
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
        Lpower_all = getView().findViewById(R.id.power_all);
        Lpower_plug = getView().findViewById(R.id.power_plug);
        Lprice_month = getView().findViewById(R.id.price_month);
        tvPrice = getView().findViewById(R.id.tv_month_powerBill);
        immediatePower_office = getView().findViewById(R.id.tv_immediatePower_office);
        tvPower_ac = getView().findViewById(R.id.tv_immediatePower_ac);
        tvPower_all = getView().findViewById(R.id.tv_immediatePower_all);
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
                getChart("getHistoryInNow", Integer.valueOf(date[0]), Integer.valueOf(date[1])-1, Integer.valueOf(date[2]));
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
                        getChart("getHistoryInDay", year, month, day);
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
                        getChart("getHistoryInMonth", year, month, 0);
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
                        getChart("getHistoryInYear", year, 0, 0);
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
        ArrayList<ChartItem> list = new ArrayList<ChartItem>();
        ArrayList<Entry> LinePower = new ArrayList<Entry>();
        ArrayList<String> array = new ArrayList<String>();
        String[] Chart_id = {"Energy"};
        String xUnit ="時間";
        try {
            for (int i = 0;i < 10;i++){
                LinePower.add(new Entry(i, Float.parseFloat("1")));
                array.add(new String("1"));
            }
            list.add(new LineChartItem(chart.generateDataLine(Chart_id, LinePower()), getActivity(), "用電量", Time(), "kWh", xUnit));

            ChartDataAdapter cda = new ChartDataAdapter(getActivity(), list);
            lv.setAdapter(cda);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getChart(final String button, int year, int month, int day) {
        /*final JSONObject body = new JSONObject();
        try {
            body.put("action", button);
            body.put("field", "xinglong2");
            body.put("cluster", "1");
            body.put("token", "mDSbpZrRXACEsBE8WR34");
            switch (button){
                case "getHistoryInYear":
                    body.put("year", String.valueOf(year));
                    break;
                case "getHistoryInMonth":
                    body.put("year", String.valueOf(year));
                    body.put("month", String.valueOf(month+1));
                    break;
                case "getHistoryInDay":
                    body.put("year", String.valueOf(year));
                    body.put("month", String.valueOf(month+1));
                    body.put("day", String.valueOf(day));
                    break;
                case "getHistoryInNow":
                    body.put("year", String.valueOf(year));
                    body.put("month", String.valueOf(month+1));
                    body.put("day", String.valueOf(day));
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        postRequest = new StringRequest(Request.Method.POST, url_room, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // response
                ArrayList<ChartItem> list = new ArrayList<ChartItem>();
                ArrayList<BarEntry> BarEntries = new ArrayList<BarEntry>();
                ArrayList<Entry> LineEntries = new ArrayList<Entry>();
                ArrayList<String> array = new ArrayList<String>();
                String[] Chart_id = {"Power"};
                try {
                    Log.d("body", body.toString());
                    //JSONArray array = new JSONArray(response);
                    JSONObject object = new JSONObject(response);
                    Log.d("Login", object.toString());
                    int num = object.getJSONArray("label").length();
                    Log.d("num", String.valueOf(num));
                    for (int i = 0;i < num;i++){
                        String label = object.getJSONArray("label").getString(i);
                        String power = object.getJSONArray("power").getString(i);
                        BarEntries.add(new BarEntry(i, Float.parseFloat(power)));
                        LineEntries.add(new Entry(i, Float.parseFloat(power)));
                        array.add(new String(label));
                    }
                    switch (button) {
                        case "getHistoryInYear":
                            list.add(new BarChartItem(chart.generateDataBar(Chart_id, BarEntries), getActivity(), "用電量 (度)", array, "kWh", "時間(月份)"));
                            break;
                        case "getHistoryInMonth":
                            list.add(new BarChartItem(chart.generateDataBar(Chart_id, BarEntries), getActivity(), "用電量 (度)", array, "kWh", "時間(日)"));
                            break;
                        case "getHistoryInDay":
                            list.add(new BarChartItem(chart.generateDataBar(Chart_id, BarEntries), getActivity(), "用電量 (度)", array, "kWh", "時間(小時)"));
                            break;
                        case "getHistoryInNow":
                            list.add(new LineChartItem(chart.generateDataLine(Chart_id, LineEntries), getActivity(), "實功 (瓩)", array, "kW", "時間(分鐘)"));
                            break;
                    }
                    ChartDataAdapter cda = new ChartDataAdapter(getActivity(), list);
                    lv.setAdapter(cda);
                } catch (JSONException e) {
                    e.printStackTrace();
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
        requestQueue.add(postRequest);*/
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
