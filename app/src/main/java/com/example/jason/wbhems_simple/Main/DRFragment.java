package com.example.jason.wbhems_simple.Main;


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
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.example.jason.wbhems_simple.DR.DrHistoryAdapter;
import com.example.jason.wbhems_simple.DR.PriceSettingFragment;
import com.example.jason.wbhems_simple.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DRFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DRFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1,mParam2;
    private String TOKEN,User;
    private int num = 0;
    private SharedPreferences setting;
    private SharedPreferences.Editor settingedit;
    private RequestQueue requestQueue;
    private StringRequest postRequest;
    //private String url = "http://140.116.163.19:10107/epslab_ems_api/appliance.php";
    private String url = "http://140.116.163.19:10107/epslab_ems/api/dr.php";
    //private String url = "http://140.116.163.19:10107/epslab_ems_api/dr.php";
    private ListView lv_History;
    private LinearLayout LLnow, LLHnow, LLfuture, LLHfuture, LLhistory, LLHhistory;
    private TextView tvCount1, tvCount2, tvCount3, tvCount4;
    private TextView  tv_now_start, tv_now_stop, tv_now_p1, tv_now_time,  tv_now_cost ,
                      tv_fu_start, tv_fu_stop, tv_fu_p1, tv_fu_time, tv_fu_cost;
    private ArrayList<HashMap<String, String>> datas = new ArrayList<HashMap<String,String>>();

    public DRFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DRFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DRFragment newInstance(String param1, String param2) {
        DRFragment fragment = new DRFragment();
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
        return inflater.inflate(R.layout.fragment_dr, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestQueue = Volley.newRequestQueue(getActivity());
        setting = this.getActivity().getSharedPreferences("auto",0);
        settingedit = setting.edit();
        User = setting.getString("User", "");
        TOKEN = setting.getString("TOKEN","");
        findViewById();
        getDREvents();
    }
    public void getDREvents(){
        final JSONObject body = new JSONObject();
        try {
            body.put("action", "getHemsDrEvent");
            body.put("field",User);
            body.put("token",TOKEN);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("body", body.toString());
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    Log.d("Response_DrEvent", object.toString());
                    JSONObject data = object.getJSONObject("data");
                    JSONArray list = data.getJSONArray("list");
                    num = data.getJSONArray("list").length();
                    Log.d("Event_Num",String.valueOf(num));
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    formatter.setLenient(false);
                    Date now = new Date() ; // 獲取當前時間
                    for(int i = 0; i < num; i++){
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
                            if (Start_Now > 0) {  //開始時間大於現在時間，把事件歸類未來事件
                                tv_fu_start.setText(start[0]+" "+start[1]);
                                tv_fu_stop.setText(end[0]+" "+end[1]);
                                tv_fu_p1.setText(data.getJSONArray("list").getJSONObject(i).getString("amount"));
                                tv_fu_time.setText(data.getJSONArray("list").getJSONObject(i).getString("duration"));
                                tv_fu_cost.setText(data.getJSONArray("list").getJSONObject(i).getString("expected_reward"));
                            } else {  //開始時間小於現在時間，把事件歸類到當前事件
                                tv_now_start.setText(start[0]+" "+start[1]);
                                tv_now_stop.setText(end[0]+" "+end[1]);
                                tv_now_p1.setText(data.getJSONArray("list").getJSONObject(i).getString("amount"));
                                tv_now_time.setText(data.getJSONArray("list").getJSONObject(i).getString("duration"));
                                tv_now_cost.setText(data.getJSONArray("list").getJSONObject(i).getString("expected_reward"));
                            }
                        } else {  //結束時間小於現在時間，把事件歸類到歷史事件
                            HashMap<String, String> item = new HashMap<String, String>();
                            item.put("history_date", start[0]);
                            item.put("history_start", start[0]+" "+start[1]);
                            item.put("history_stop", end[0]+" "+end[1]);
                            if(list.getJSONObject(i).getString("amount").equals("null")){ // 如果為空值
                                item.put("history_p1", "--");
                            }else{
                                item.put("history_p1", list.getJSONObject(i).getString("amount")); // 預計抑低量 (瓩)
                            }
                            if(list.getJSONObject(i).getString("duration").equals("null")){ // 如果為空值
                                item.put("history_time", "--");
                            }else{
                                item.put("history_time",list.getJSONObject(i).getString("duration")); // 抑低時間 (分)
                            }
                            if(list.getJSONObject(i).getString("actual_load_sheeding").equals("null")){
                                item.put("history_p2", "--");
                            }else{
                                item.put("history_p2", list.getJSONObject(i).getString("actual_load_sheeding")); // 實際抑低量 (瓩)
                            }
                            if(list.getJSONObject(i).getString("achieving_rate").equals("null")){
                                item.put("history_result","失敗"); // 結果
                            }else if(Float.parseFloat(list.getJSONObject(i).getString("achieving_rate")) > 0){
                                item.put("history_result","成功");
                            }else{
                                item.put("history_result","失敗");
                            }
                            if(list.getJSONObject(i).getString("expected_reward").equals("null")){
                                item.put("history_cost", "--");
                            }else{
                                item.put("history_cost", list.getJSONObject(i).getString("expected_reward")); // 預估補償金額 (元)
                            }
                            if(list.getJSONObject(i).getString("actual_reward").equals("null")){
                                item.put("history_cost2", "--"); // 實際補償金額 (元)
                            }else{
                                item.put("history_cost2", list.getJSONObject(i).getString("actual_reward")); // 實際補償金額 (元)
                            }
                            if(list.getJSONObject(i).getString("baseline").equals("null")){
                                item.put("history_p3", "--"); // 當日負載基準線 (瓩)
                            }else{
                                item.put("history_p3", list.getJSONObject(i).getString("baseline")); // 當日負載基準線 (瓩)
                            }
                            datas.add(item);
                            DrHistoryAdapter adapter = new DrHistoryAdapter(getActivity(), datas);
                            lv_History.setAdapter(adapter);

                        }
                       // DrHistoryAdapter adapter = new DrHistoryAdapter(getActivity(), datas);
                        //lv_History.setAdapter(adapter);
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
        LLnow = getView().findViewById(R.id.layout_event_now);
        LLHnow = getView().findViewById(R.id.layout_hide_event_now);
        LLfuture = getView().findViewById(R.id.layout_event_future);
        LLHfuture = getView().findViewById(R.id.layout_hide_event_future);
        LLhistory = getView().findViewById(R.id.layout_event_history);
        LLHhistory = getView().findViewById(R.id.layout_hide_event_history);
        tvCount1 = getView().findViewById(R.id.tv_count1);
        tvCount2 = getView().findViewById(R.id.tv_count2);
        tvCount3 = getView().findViewById(R.id.tv_count3);
        tvCount4 = getView().findViewById(R.id.tv_count4);
        lv_History = getView().findViewById(R.id.lv_history);
        //當前事件
        tv_now_start = getView().findViewById(R.id.tv_start_at);
        tv_now_stop = getView().findViewById(R.id.tv_end_at);
        tv_now_p1 = getView().findViewById(R.id.tv_exdrop);
        tv_now_time = getView().findViewById(R.id.tv_droptime);
        tv_now_cost = getView().findViewById(R.id.tv_exmakeup);
        //未來事件
        tv_fu_start = getView().findViewById(R.id.tv_futurestart);
        tv_fu_stop = getView().findViewById(R.id.tv_futureend);
        tv_fu_p1 = getView().findViewById(R.id.tv_futureexdrop);
        tv_fu_time = getView().findViewById(R.id.tv_futuredroptime);
        tv_fu_cost = getView().findViewById(R.id.tv_futureexmakeup);
        //顯示or隱藏
        LLnow.setOnClickListener(new View.OnClickListener() {
            boolean visible;
            @Override
            public void onClick(View v) {
                visible = !visible;
                LLHnow.setVisibility(visible ? View.VISIBLE : View.GONE);
                tvCount2.setText(visible ? "-" : "+");
            }
        });
        LLfuture.setOnClickListener(new View.OnClickListener() {
            boolean visible;
            @Override
            public void onClick(View v) {
                visible = !visible;
                LLHfuture.setVisibility(visible ? View.VISIBLE : View.GONE);
                tvCount3.setText(visible ? "-" : "+");
            }
        });
        LLhistory.setOnClickListener(new View.OnClickListener() {
            boolean visible;
            @Override
            public void onClick(View v) {
                visible = !visible;
                LLHhistory.setVisibility(visible ? View.VISIBLE : View.GONE);
                tvCount4.setText(visible ? "-" : "+");
            }
        });
    }
}
