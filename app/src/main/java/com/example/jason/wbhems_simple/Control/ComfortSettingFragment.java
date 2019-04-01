package com.example.jason.wbhems_simple.Control;

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
import com.example.jason.wbhems_simple.R;

import org.json.JSONException;
import org.json.JSONObject;


public class ComfortSettingFragment extends DialogFragment {
    private SeekBar seekBar;
    private TextView tv_comfort,tv_title;
    private Button comfirm, cancel;
    private SharedPreferences setting;
    private SharedPreferences.Editor settingedit;
    private RequestQueue requestQueue;
    private StringRequest postRequest;
    String url_room = "http://140.116.163.19:10107/epslab_ems/api/room.php";
    String User,Token,IP,Port,name;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //隐藏title
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        /*寫在這裡沒效果，要寫在onStart裡*/
//      getDialog().getWindow().getAttributes().width=getResources().getDisplayMetrics().widthPixels-200;
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comfort_setting, container, false);
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
        requestQueue = Volley.newRequestQueue(getActivity());
        seekBar = getView().findViewById(R.id.sb_comfortset);
        tv_comfort = getView().findViewById(R.id.tv_comfort);
        tv_title = getView().findViewById(R.id.textView7);
        comfirm = getView().findViewById(R.id.btn_confirm_comfort);
        cancel = getView().findViewById(R.id.btn_cancel_comfort);
        setting = this.getActivity().getSharedPreferences("auto", 0);
        settingedit = setting.edit();
        User = setting.getString("User", "");
        Token = setting.getString("TOKEN","");
        String Comfort1_92702 = setting.getString("92702_comfort1", "1");
        String Comfort2_92702 = setting.getString("92702_comfort2", "1");
        String Comfort_92710 = setting.getString("92710_comfort","1");
        String Comfort_92712 = setting.getString("92712_comfort","1");
        String Title = setting.getString("comfort_title","");
        tv_title.setText(Title);
        switch (Title){
            case "92702_1":
                tv_title.setText("區域一舒適度設定");
                tv_comfort.setText(Comfort1_92702);
                seekBar.setProgress(Integer.parseInt(Comfort1_92702)-1);
                name = "auto_comfort_1";
                break;
            case "92702_2":
                tv_title.setText("區域二舒適度設定");
                tv_comfort.setText(Comfort2_92702);
                seekBar.setProgress(Integer.parseInt(Comfort2_92702)-1);
                name = "auto_comfort_2";
                break;
            case "92710":
                tv_title.setText("舒適度設定");
                tv_comfort.setText(Comfort_92710);
                seekBar.setProgress(Integer.parseInt(Comfort_92710)-1);
                name = "auto_comfort";
                break;
            case "92712":
                tv_title.setText("舒適度設定");
                tv_comfort.setText(Comfort_92712);
                seekBar.setProgress(Integer.parseInt(Comfort_92712)-1);
                name = "auto_comfort";
                break;
        }
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_comfort.setText(String.valueOf(progress+1));
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
                setComfort(String.valueOf(tv_comfort.getText().toString()));
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
    private void setComfort(String value){
        final JSONObject body = new JSONObject();
        try {
            body.put("action", "setHemsControlPanel");
            body.put("field",User);
            body.put("name",name);
            body.put("setMode","comfort");
            body.put("token",Token);
            body.put("value",value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("setComfort_body",body.toString());
        postRequest = new StringRequest(Request.Method.POST, url_room, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("setComfort_response", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError_Comfort", error.toString());
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
