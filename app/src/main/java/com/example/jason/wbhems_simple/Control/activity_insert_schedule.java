package com.example.jason.wbhems_simple.Control;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jason.wbhems_simple.CompareDateTime;
import com.example.jason.wbhems_simple.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class activity_insert_schedule extends AppCompatActivity {

    String server_ur1 = "http://163.18.57.43/sumeeko_api/plug.php";
    String Str_Date,Time,radiobutton_status = null;
    private RequestQueue requestQueue;
    private StringRequest request;
    TimePicker timePicker;
    private TextView tv_date;
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_schedule);
        requestQueue = Volley.newRequestQueue(this);
        tv_date = findViewById(R.id.tv_date);
        timePicker = findViewById(R.id.timepicker);
        timePicker.setIs24HourView(true);
        ////////////////////////////////////
        String Week = "";
        Calendar current = Calendar.getInstance();
        int year = current.get(Calendar.YEAR);
        int month = current.get(Calendar.MONTH);
        int day =current.get(Calendar.DAY_OF_MONTH);
        Str_Date = year+"-"+(month + 1) + "-" + (day);
        int day_week = current.get(Calendar.DAY_OF_WEEK);
        Log.d("DAY_week", String.valueOf(day_week));
        if(day_week == 7){Week = "日";}
        if(day_week == 1){Week = "一";}
        if(day_week == 2){Week = "二";}
        if(day_week == 3){Week = "三";}
        if(day_week == 4){Week = "四";}
        if(day_week == 5){Week = "五";}
        if(day_week == 6){Week = "六";}
        tv_date.setText((month + 1) + "月" + (day)+"日"+","+"週"+Week);
        ///////////////////////////////////////////
    }

    public void btn_cancel (View view){
        Intent intent = new Intent(this, activity_schedule.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void btn_save (View view){
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();
        Time = hour+":"+minute+":"+"00";
        String time = hour+":"+minute;
        final String start_week = tv_date.getText().toString();
        final String start = Str_Date+" "+Time;
        boolean now_start =  CompareDateTime.isDateOneBigger(start);
        if(now_start == false )
        {
            setPlugSchedule(radiobutton_status,start,"setPlugSchedules","1","006");
            Intent intent = new Intent(this, activity_schedule.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }else if(now_start == true)
        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(activity_insert_schedule.this);
            dialog.setTitle("提示訊息");
            dialog.setMessage("無法設定過去時間的排程");
            dialog.setPositiveButton("確定",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                }
            });
            dialog.show();
        }

    }

    private void setPlugSchedule(String radiobutton_status, String start_week, String schedule_save, String enable,String name) {
        final JSONObject body = new JSONObject();
        try {
            body.put("action", schedule_save);
            body.put("name",name);
            body.put("time",start_week);
            body.put("motion",radiobutton_status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request = new StringRequest(Request.Method.POST, server_ur1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    Log.d("setPlugSchedules", object.toString());

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
        requestQueue.add(request);
    }

    public void RadioButton_on (View view){
        radiobutton_status = "1";
    }
    public void RadioButton_off (View view){
        radiobutton_status = "0";
    }

    public void date_onclick (View view){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                Str_Date = year+"-"+(month + 1) + "-" + day;
                Log.d("DAY",Str_Date);
                String Week = "";
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Calendar c = Calendar.getInstance();
                try {
                    c.setTime(format.parse(year+"-"+(month+1)+"-"+day));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int day_Week = c.get(Calendar.DAY_OF_WEEK);
                Log.d("DAY", String.valueOf(day_Week));
                if(day_Week == 1){Week = "日";}
                if(day_Week == 2){Week = "一";}
                if(day_Week == 3){Week = "二";}
                if(day_Week == 4){Week = "三";}
                if(day_Week == 5){Week = "四";}
                if(day_Week == 6){Week = "五";}
                if(day_Week == 7){Week = "六";}

                tv_date.setText((month + 1) + "月" + day+"日"+","+"週"+Week);
            }

        }, mYear,mMonth, mDay).show();
    }



}
