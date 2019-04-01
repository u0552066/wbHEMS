package com.example.jason.wbhems_simple.Control;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jason.wbhems_simple.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DimSettingFragment extends DialogFragment {
    private LinearLayout linearLayout;
    private EditText et_number;
    private Button confirm, cancel;
    private Button btn_work,btn_rest,btn_present,btn_preset;
    private TextView tv_preset,tv_title;
    private SharedPreferences setting;
    private SharedPreferences.Editor settingedit;
    private RequestQueue requestQueue;
    private StringRequest postRequest;
    String url_room = "http://140.116.163.19:10107/epslab_ems/api/room.php";
    String User,Token,name,auto_control_id;
    String Mode;
    String Preset,current_value;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //隐藏title
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        /*寫在這裡沒效果，要寫在onStart裡*/
//      getDialog().getWindow().getAttributes().width=getResources().getDisplayMetrics().widthPixels-200;
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dim_setting, container, false);
    }
    @Override
    public void onStart() {
        /*設置對話框的寬高*/
        getDialog().getWindow().getAttributes().width=getResources().getDisplayMetrics().widthPixels;
        /*下面的方式設置也行*/
//      getDialog().getWindow().setLayout(getResources().getDisplayMetrics().widthPixels-200, getDialog().getWindow().getAttributes().height);
        super.onStart();
    }
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        linearLayout = getView().findViewById(R.id.layout_present);
        btn_work = getView().findViewById(R.id.btn_dimWork); // 工作
        btn_rest = getView().findViewById(R.id.btn_dimBreak); // 休息
        btn_present = getView().findViewById(R.id.btn_dimPresent); // 簡報
        btn_preset = getView().findViewById(R.id.btn_preset); // 預設
        confirm = getView().findViewById(R.id.btn_confirm_dim);
        cancel = getView().findViewById(R.id.btn_cancel_dim);
        et_number = getView().findViewById(R.id.et_number);
        tv_title = getView().findViewById(R.id.dim_title);
        tv_preset = getView().findViewById(R.id.tv_preset);
        requestQueue = Volley.newRequestQueue(getContext());
        setting = this.getActivity().getSharedPreferences("auto", 0);
        settingedit = setting.edit();
        User = setting.getString("User", "");
        Token = setting.getString("TOKEN","");
        String Title = setting.getString("dim_title","");
        switch (Title){
            case "92702_1":
                linearLayout.setVisibility(View.GONE); // 隱藏簡報模式
                tv_title.setText("區域一自動調光");
                name = "auto_dimming_1";
                auto_control_id = "3";
                break;
            case "92702_2":
                linearLayout.setVisibility(View.GONE); // 隱藏簡報模式
                tv_title.setText("區域二自動調光");
                name = "auto_dimming_2";
                auto_control_id = "9";
                break;
            case "92710":
                linearLayout.setVisibility(View.GONE); // 隱藏簡報模式
                tv_title.setText("自動調光");
                name = "auto_dimming";
                auto_control_id = "15";
                break;
            case "92712":
                linearLayout.setVisibility(View.VISIBLE); // 顯示簡報模式
                tv_title.setText("自動調光");
                name = "auto_dimming";
                auto_control_id = "18";
                break;
        }
        getDimmingMode(name); // 取得模式 present:簡報模式 work:工作模式 rest:休息模式
        getDimmingValue(auto_control_id); // 取得各模式預設值and目前值
        btn_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mode = "work";
                setMode();
                setButton();
            }
        });
        btn_rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mode = "rest";
                setMode();
                setButton();
            }
        });
        btn_present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mode = "present";
                setMode();
                setButton();
            }
        });
        btn_preset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_preset.setText(Preset);
                et_number.setText(Preset);
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(),"工作模式",Toast.LENGTH_LONG).show();
                setValue(et_number.getText().toString());
                onStop();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStop();
            }
        });
    }

    private void getDimmingMode(String name) {
        final JSONObject body = new JSONObject();
        try {
            body.put("action", "getHemsAutoControl");
            body.put("field",User);
            body.put("name",name);
            body.put("token",Token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        postRequest = new StringRequest(Request.Method.POST, url_room, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DimmingMode_response", response);
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject data = object.getJSONObject("data");
                    JSONArray list = data.getJSONArray("list");
                    Mode = list.getJSONObject(0).getString("setting_value");
                    setButton();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError_DimmingMode", error.toString());
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

    private void getDimmingValue(String id){
        final JSONObject body = new JSONObject();
        try {
            body.put("action", "getHemsDimmingMode");
            body.put("auto_control_id",id);
            body.put("token",Token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        postRequest = new StringRequest(Request.Method.POST, url_room, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DimmingValue_response", response);
                Log.d("Mode",Mode);
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject data = object.getJSONObject("data");
                    JSONArray list = data.getJSONArray("list");
                    switch (Mode){
                        case "work":
                            Preset = list.getJSONObject(0).getString("dimming_default_value"); // 預設值
                            current_value = list.getJSONObject(0).getString("dimming_value");
                            tv_preset.setText(Preset);
                            et_number.setText(String.valueOf(current_value));
                            break;
                        case "rest":
                            Preset = list.getJSONObject(1).getString("dimming_default_value"); // 預設值
                            current_value = list.getJSONObject(1).getString("dimming_value");
                            tv_preset.setText(Preset);
                            et_number.setText(String.valueOf(current_value));
                            break;
                        case "present":
                            Preset = list.getJSONObject(2).getString("dimming_default_value"); // 預設值
                            current_value = list.getJSONObject(2).getString("dimming_value");
                            tv_preset.setText(Preset);
                            et_number.setText(String.valueOf(current_value));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error_DimmingValue", error.toString());
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

    @SuppressLint({"ResourceType", "NewApi"})
    private void setButton() {
        switch (Mode){
            case "work":
                btn_work.setBackground(getResources().getDrawable(R.drawable.button_shape5)); //藍底
                btn_work.setTextColor(Color.rgb(255,255,255)); // 白字
                //----
                btn_rest.setBackground(getResources().getDrawable(R.drawable.button_shape4)); // 白底
                btn_rest.setTextColor(Color.rgb(2,153,255)); // 藍字
                //----
                btn_present.setBackground(getResources().getDrawable(R.drawable.button_shape4)); // 白底
                btn_present.setTextColor(Color.rgb(2,153,255)); // 藍字
            break;
            case "rest":
                btn_work.setBackground(getResources().getDrawable(R.drawable.button_shape4)); // 白底
                btn_work.setTextColor(Color.rgb(2,153,255)); // 藍字
                //----
                btn_rest.setBackground(getResources().getDrawable(R.drawable.button_shape5)); //藍底
                btn_rest.setTextColor(Color.rgb(255,255,255)); // 白字
                //----
                btn_present.setBackground(getResources().getDrawable(R.drawable.button_shape4)); // 白底
                btn_present.setTextColor(Color.rgb(2,153,255)); // 藍字
                break;
            case "present":
                btn_work.setBackground(getResources().getDrawable(R.drawable.button_shape4)); // 白底
                btn_work.setTextColor(Color.rgb(2,153,255)); // 藍字
                //----
                btn_rest.setBackground(getResources().getDrawable(R.drawable.button_shape4)); // 白底
                btn_rest.setTextColor(Color.rgb(2,153,255)); // 藍字
                //----
                btn_present.setBackground(getResources().getDrawable(R.drawable.button_shape5)); //藍底
                btn_present.setTextColor(Color.rgb(255,255,255)); // 白字
                break;
        }
        getDimmingValue(auto_control_id);

    }

    private void setMode(){
        final JSONObject body = new JSONObject();
        try {
            body.put("action", "setHemsControlPanel");
            body.put("field",User);
            body.put("name",name);
            body.put("setMode","lux");
            body.put("token",Token);
            body.put("value",Mode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("setMode_body",body.toString());
        postRequest = new StringRequest(Request.Method.POST, url_room, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("setMode_response", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError_Mode", error.toString());
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

    private void setValue(String value){
        final JSONObject body = new JSONObject();
        try {
            body.put("action", "setHemsControlPanel");
            body.put("auto_control_id",auto_control_id);
            body.put("field",User);
            body.put("luxMode",Mode);
            body.put("setMode","luxValue");
            body.put("token",Token);
            body.put("value",value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("setLuxValue_body",body.toString());
        postRequest = new StringRequest(Request.Method.POST, url_room, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("setLuxValue_response", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError_LuxValue", error.toString());
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

}
