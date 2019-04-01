package com.example.jason.wbhems_simple.DR;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
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
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by AndyShi on 2018/1/12.
 */

public class DrDragListAdapter extends BaseAdapter {
    private String url = "http://140.116.163.19:10107/epslab_ems/api/appliance.php";
    private ArrayList<String> mDatas;
    private ArrayList<String> powers;
    private ArrayList<String> dollars;
    private ArrayList<Boolean> states;
    private ArrayList<String> id;
    String User,Token,Name,Price,name;
    private SharedPreferences setting;
    private SharedPreferences.Editor settingedit;
    private Context context;
    private Button lowest;
    private Switch equipSwitch;
    private TextView tv_power,tv_equipment;
    private RequestQueue requestQueue;
    private StringRequest postRequest;

    public DrDragListAdapter(Context context, ArrayList<String> arrayTitles, ArrayList<String> arrayPowers, ArrayList<String> arrayDollars, ArrayList<Boolean> arrayStates, ArrayList<String> arrayID) {
        this.context = context;
        this.mDatas = arrayTitles;
        this.powers = arrayPowers;
        this.dollars = arrayDollars;
        this.states = arrayStates;
        this.id = arrayID;

    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View view;
        /***
         * 在這裏盡可能每次都進行實例化新的，這樣在拖拽ListView的時候不會出現錯亂.
         * 具體原因不明，不過這樣經過測試，目前沒有發現錯亂。雖說效率不高，但是做拖拽LisView足夠了。
         */
        view = LayoutInflater.from(context).inflate(
                R.layout.item_dr_drag_list, null);
        setting = this.context.getSharedPreferences("auto",0);
        settingedit = setting.edit();
        User = setting.getString("User", "");
        Token = setting.getString("TOKEN","");
        equipSwitch = (Switch)view.findViewById(R.id.sw_equipment);
        tv_equipment = (TextView)view.findViewById(R.id.tv_equipment);
        tv_power = (TextView)view.findViewById(R.id.tv_equipment_pw);
        lowest = (Button)view.findViewById(R.id.btn_lowestpay);
        requestQueue = Volley.newRequestQueue(context);
       if(mDatas.get(position).equals("ac")){
           Name = "空調";
       }else if(mDatas.get(position).equals("light")){
           Name = "電燈";
       }
       if(dollars.get(position).equals("0")){
           lowest.setText("不參與");
       }else{
           lowest.setText(dollars.get(position));
       }
        tv_equipment.setText(Name);
        tv_power.setText(powers.get(position));
        equipSwitch.setChecked(states.get(position));
        equipSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    setApplianceRecover(mDatas.get(position),"1");
                }else {
                    setApplianceRecover(mDatas.get(position),"0");
                }
            }
        });
        lowest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /////
                settingedit.putString("Price",dollars.get(position)).commit();
                settingedit.putString("equip_name",mDatas.get(position)).commit();
                FragmentTransaction priceSetting = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
                Fragment price = ((FragmentActivity)context).getSupportFragmentManager().findFragmentByTag("PriceSettingFragment");
                if (price != null) {
                    priceSetting.remove(price);
                }
                priceSetting.addToBackStack(null);
                // Create and show the dialog.
                DialogFragment newFragment_price_ac = new PriceSettingFragment();
                newFragment_price_ac.show(priceSetting, "PriceSettingFragment");
            }
        });
        return view;
    }

    /***
     * 動態修改ListVIiw的方位.（數據移位）
     *
     * @param start 點擊移動的position
     * @param end   松開時候的position
     */
    public void update(int start, int end) {
        String data = mDatas.get(start);
        String pw = powers.get(start);
        String drs = dollars.get(start);
        Boolean sta = states.get(start);
        String num = id.get(start);
        mDatas.remove(start);  // 刪除該項
        mDatas.add(end, data); // 添加刪除項
        powers.remove(start);
        powers.add(end,pw);
        dollars.remove(start);
        dollars.add(end,drs);
        states.remove(start);
        states.add(end,sta);
        id.remove(start);
        id.add(end,num);
        notifyDataSetChanged();// 刷新ListView
        //Toast.makeText(context,"優先順序變動："+mDatas.get(end)+"從"+String.valueOf(start)+"改為"+String.valueOf(end),Toast.LENGTH_LONG).show();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return id.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public AlertDialog getDollarsDialog(final String[] items, final int position){
        Builder builder = new Builder(context);
        //設定對話框內的項目
        builder.setItems(items, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //當使用者點選對話框時，顯示使用者所點選的項目
                dollars.remove(position);
                dollars.add(position,items[which]);
                if(items[which].equals("不參與")){
                    setApplianceShedding(position,"0");
                }else {
                    setApplianceShedding(position,items[which]);
                }

                notifyDataSetChanged();
            }
        });
        return builder.create();
        }

    private void setApplianceRecover(String name, String recover){
        final JSONObject body = new JSONObject();
        try {
            body.put("action", "setHemsAppliance");
            body.put("field",User);
            body.put("mode","drRecovery");
            body.put("name",name);
            body.put("recover",recover);
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
                Log.e("VolleyError_Recover", error.toString());
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

    private void setApplianceShedding(int getid, String value){
       /* String[] theid = id.get(getid).split(",");
        JsonStr mJsonStr = new JsonStr();
        mJsonStr.setAction("setApplianceShedding");
        mJsonStr.setId(theid[1]);
        mJsonStr.setValue(value);
        mJsonStr.setToken(pref.getString("TOKEN",""));
        Gson gson = new Gson();
        String json = gson.toJson(mJsonStr);
        request = new JsonObjectRequest(Request.Method.POST, "http://59.125.180.237/xlii_hems_api/appliance.php", json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    Log.d("dim", response.toString());
                    int num = 0;
                    try {
                        Toast.makeText(context,response.getString("result"), Toast.LENGTH_LONG).show();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }catch (Exception e) {
                    e.printStackTrace();
                    Log.e("dim err",e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("dim reponse err",error.getMessage());
            }
        });
        requestQueue.add(request);*/
    }
}
