package com.example.jason.wbhems_simple.Control;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
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
import com.example.jason.wbhems_simple.LoginActivity;
import com.example.jason.wbhems_simple.MainActivity;
import com.example.jason.wbhems_simple.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleviewHolder> {

    private Context mContext;
    private ArrayList<ExampleItem> mExampleList;
    private RequestQueue requestQueue;
    private StringRequest request;
    String server_ur1 = "http://163.18.57.43/sumeeko_api/plug.php";
    public ExampleAdapter(Context context , ArrayList<ExampleItem> exampleList){
        mContext = context;
        mExampleList = exampleList;
    }

    @Override
    public ExampleviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item2_schedule, parent, false);
        return new ExampleviewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ExampleviewHolder holder, final int position) {
        ExampleItem currentItem = mExampleList.get(position);
        final String tv_time = currentItem.getmTv_time();
        final String tv_name = currentItem.getmTv_name();
        final String tv_date_week = currentItem.getmTv_date_week();
        final String tv_id = currentItem.getmTv_id();
        final String tv_action = currentItem.getmTv_action();
        boolean sw = currentItem.getSw_boolean();


        holder.mTv_time.setText(tv_time);
        holder.mTv_name.setText(tv_name);
        //holder. mTv_date_week.setText(tv_date_week);
        holder.mTv_action.setText(tv_action);
        /*holder.mSwitch.setChecked(sw);
        if( sw == true){
            holder.mTv_time.setTextColor(0xff000000);
            holder.mTv_name.setTextColor(0xff000000);
        }*/
        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                final PopupMenu popup = new PopupMenu(mContext, holder.mButton);
                //inflating menu from xml resource
                popup.inflate(R.menu.schedult_set);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item1:
                                delete_schedule(tv_id, position);
                                //handle menu1 click
                                return true;
                            case R.id.item2:
                                Intent intent = new Intent(mContext, activity_edit_schedule.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("id",tv_id);
                                bundle.putString("time",tv_time);
                                bundle.putString("action",tv_action);
                                intent.putExtras(bundle);
                                mContext.startActivity(intent);
                                //handle menu2 click
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();

            }
        });
        /*holder.mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(holder.mSwitch.isChecked()){
                    holder.mTv_time.setTextColor(0xff000000);
                    holder.mTv_name.setTextColor(0xff000000);
                    Log.d("ID",tv_id);
                    switch_enable("schedule_switch_enable",tv_id,"1");
                    Toast.makeText(mContext,"ON",Toast.LENGTH_SHORT).show();
                }
                else {
                    holder.mTv_time.setTextColor(0xff747373);
                    holder.mTv_name.setTextColor(0xff747373);
                    switch_enable("schedule_switch_enable",tv_id,"0");
                    Toast.makeText(mContext,"OFF",Toast.LENGTH_SHORT).show();
                }
            }
        });*/
    }


    private void delete_schedule( final String id,final Integer position) {
        new AlertDialog.Builder(mContext)
                .setTitle("刪除排程")
                .setMessage("是否要刪除此排程")
                .setPositiveButton("確定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                RemoveData(position);
                                final JSONObject body = new JSONObject();
                                try {
                                    body.put("action","deletePlugSchedules");
                                    body.put("id",id);;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                request = new StringRequest(Request.Method.POST, server_ur1, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.d("delete_schedule", response);
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
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                            }
                        }).show();
    }

    public void switch_enable(final String cmd, final String id, final String select) {
       /* StringRequest stringRequest = new StringRequest(Request.Method.POST, server_ur1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error",error.toString());
            }
        })
        {
            protected Map<String,String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("cmd",cmd);
                params.put("schedule_id",id);
                params.put("enable_select",select);
                return params;
            }
        };
        requestQueue.add(stringRequest);*/
    }

    public void RemoveData(int position){
        mExampleList.remove(position);
        //通知适配器item内容删除
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public class ExampleviewHolder extends RecyclerView.ViewHolder{
        private TextView mTv_time,mTv_name,mTv_date_week,mTv_id,mTv_action;
        private Switch mSwitch;
        private Button mButton;
        public ExampleviewHolder(View itemView) {
            super(itemView);
            requestQueue = Volley.newRequestQueue(mContext);
            mTv_time = itemView.findViewById(R.id.tv_time);
            mTv_name = itemView.findViewById(R.id.tv_name);
            //mTv_date_week = itemView.findViewById(R.id.tv_date_week);
            mTv_id = itemView.findViewById(R.id.tv_id);
            mTv_action = itemView.findViewById(R.id.tv_action);
            //mSwitch = itemView.findViewById(R.id.switch_on_off);
            mButton = itemView.findViewById(R.id.btn_set);

            // 點擊項目時
            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext,"onClick",Toast.LENGTH_SHORT).show();
                }
            });*/


        }
    }
}

