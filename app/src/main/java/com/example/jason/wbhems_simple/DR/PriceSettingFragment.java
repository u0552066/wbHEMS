package com.example.jason.wbhems_simple.DR;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jason.wbhems_simple.ChangeAC;
import com.example.jason.wbhems_simple.MainActivity;
import com.example.jason.wbhems_simple.R;

import org.json.JSONException;
import org.json.JSONObject;


public class PriceSettingFragment extends DialogFragment {
    private String url = "http://140.116.163.19:10107/epslab_ems/api/appliance.php";
    private SeekBar seekBar;
    private TextView tv_price;
    private Button comfirm, cancel;
    private RequestQueue requestQueue;
    private StringRequest postRequest;
    private SharedPreferences setting;
    private SharedPreferences.Editor settingedit;
    String User,Token,Price,Name;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //隐藏title
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        /*寫在這裡沒效果，要寫在onStart裡*/
//      getDialog().getWindow().getAttributes().width=getResources().getDisplayMetrics().widthPixels-200;
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_price_setting, container, false);
    }
    @Override
    public void onStart() {
        /*設置對話框的寬高*/
        getDialog().getWindow().getAttributes().width = getResources().getDisplayMetrics().widthPixels;
        /*下面的方式設置也行*/
//      getDialog().getWindow().setLayout(getResources().getDisplayMetrics().widthPixels-200, getDialog().getWindow().getAttributes().height);
        super.onStart();
    }

    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        requestQueue = Volley.newRequestQueue(getActivity());
        seekBar = getView().findViewById(R.id.sb_pricetset);
        tv_price = getView().findViewById(R.id.tv_price);
        comfirm = getView().findViewById(R.id.btn_confirm_price);
        cancel = getView().findViewById(R.id.btn_cancel_price);
        setting = this.getActivity().getSharedPreferences("auto", 0);
        settingedit = setting.edit();
        User = setting.getString("User", "");
        Token = setting.getString("TOKEN","");
        Price = setting.getString("Price","0");
        Name = setting.getString("equip_name","");
        int X = Integer.parseInt(Price)/2;
        seekBar.setProgress(X);
        tv_price.setText(Price);
        Log.d("Equipment",Name+"，"+Price+"元");
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_price.setText(String.valueOf(progress*2));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setApplianceByName(Name,tv_price.getText().toString());
                Intent reit2 = new Intent();
                reit2.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                reit2.setClass(getActivity(), dr_equipmentActivity.class);
                startActivity(reit2);
                getActivity().finish();
                //onStop();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStop();
            }
        });
    }

    private void setApplianceByName(String name, String value) {
        final JSONObject body = new JSONObject();
        try {
            body.put("action", "setHemsAppliance");
            body.put("field",User);
            body.put("mode","drShedding");
            body.put("name",name);
            body.put("value",value);
            body.put("token",Token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("cmd",body.toString());
                Log.d("setAppliance_respone", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError_drShedding", error.toString());
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
