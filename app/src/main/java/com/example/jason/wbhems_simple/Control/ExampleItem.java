package com.example.jason.wbhems_simple.Control;

public class ExampleItem {
    private String mTv_time;
    private String mTv_name;
    private String mTv_date_week;
    private String mTv_id;
    private String mTv_action;
    private boolean mSwitch;

    public ExampleItem(String tv_name, String tv_time, String tv_date_week, String tv_id , String tv_action, boolean sw){
        mTv_name = tv_name; // 排程28
        mTv_time = tv_time;  // 2019-02-15 07:00
        mTv_date_week = tv_date_week; // 目前沒用到
        mTv_id = tv_id; // 排程的ID
        mTv_action = tv_action; // 排程設定的動作
        mSwitch = sw; // 目前沒用到
    }

    public String getmTv_time() {
        return mTv_time;
    }

    public String getmTv_name() {
        return mTv_name;
    }

    public String getmTv_date_week() {
        return mTv_date_week;
    }

    public String getmTv_action(){
        return  mTv_action;
    }

    public String getmTv_id(){
        return  mTv_id;
    }

    public boolean getSw_boolean(){
        return mSwitch;
    }
}
