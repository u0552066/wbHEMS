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
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.jason.wbhems_simple.R;

import org.json.JSONException;
import org.json.JSONObject;

public class MotionSettingFragment extends DialogFragment {
    private Button cancel, confirm;
    private EditText delaysec;
    private SharedPreferences setting;
    private SharedPreferences.Editor settingedit;
    private TextView title;
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
        return inflater.inflate(R.layout.fragment_motion_setting, container, false);
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
        cancel = getView().findViewById(R.id.btn_cancel_motion);
        confirm = getView().findViewById(R.id.btn_confirm_motion);
        delaysec = getView().findViewById(R.id.et_delaysec);
        title = getView().findViewById(R.id.textView7);
        setting = this.getActivity().getSharedPreferences("auto", 0);
        settingedit = setting.edit();
        User = setting.getString("User", "");
        Token = setting.getString("TOKEN","");
        String Title = setting.getString("motion_title","");
        Float motion_offtime_1 = setting.getFloat("92702_motion_offtime_1", 0);
        Float motion_offtime_2 = setting.getFloat("92702_motion_offtime_2", 0);
        Float motion_offtime_3 = setting.getFloat("92702_motion_offtime_3", 0);
        Float motion_offtime_4 = setting.getFloat("92702_motion_offtime_4", 0);
        Float motion_offtime_92710 = setting.getFloat("92710_motion_offtime",0);
        Float motion_offtime_92712 = setting.getFloat("92712_motion_offtime",0);
        switch (Title){
            case "92702_1":
                title.setText("移動延遲參數設定(左區)");
                delaysec.setText(String.valueOf(motion_offtime_1));
                name = "motion_detector_1";
                break;
            case "92702_2":
                title.setText("移動延遲參數設定(門口)");
                delaysec.setText(String.valueOf(motion_offtime_2));
                name = "motion_detector_2";
                break;
            case "92702_3":
                title.setText("移動延遲參數設定(右區)");
                delaysec.setText(String.valueOf(motion_offtime_3));
                name = "motion_detector_3";
                break;
            case "92702_4":
                title.setText("移動延遲參數設定(中區)");
                delaysec.setText(String.valueOf(motion_offtime_4));
                name = "motion_detector_4";
                break;
            case "92710":
                title.setText("移動延遲參數設定");
                delaysec.setText(String.valueOf(motion_offtime_92710));
                name = "motion_detector";
                break;
            case "92712":
                title.setText("移動延遲參數設定");
                delaysec.setText(String.valueOf(motion_offtime_92712));
                name = "motion_detector";
                break;
        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMotion(delaysec.getText().toString());
                onStop();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStop();
            }
        });
    }

    private void setMotion(String value){
        final JSONObject body = new JSONObject();
        try {
            body.put("action", "setHemsControlPanel");
            body.put("field",User);
            body.put("name",name);
            body.put("setMode",name);
            body.put("token",Token);
            body.put("value",value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("setMotion_body",body.toString());
        postRequest = new StringRequest(Request.Method.POST, url_room, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("setMotion_response", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError_Motion", error.toString());
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
