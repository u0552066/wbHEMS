package com.example.jason.wbhems_simple.DR;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jason.wbhems_simple.R;

//import net.ddns.b505.wbhems.R;

import java.util.ArrayList;
import java.util.HashMap;


public class DrHistoryAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<HashMap<String, String>> list;
    private int currentItem = -1; //用于记录点击的 Item 的 position。是控制 item 展开的核心

    public DrHistoryAdapter(Context context,
                            ArrayList<HashMap<String, String>> list) {
        super();
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_dr_history, parent, false);
            holder = new ViewHolder();
            holder.showArea = (LinearLayout) convertView.findViewById(R.id.layout_showArea);
            holder.tv_history_date = (TextView) convertView
                    .findViewById(R.id.tv_history_date);
            holder.tv_history_start = (TextView) convertView
                    .findViewById(R.id.tv_history_start);
            holder.tv_history_stop = (TextView) convertView
                    .findViewById(R.id.tv_history_stop);
            holder.tv_history_p1 = (TextView) convertView
                    .findViewById(R.id.tv_history_p);
            holder.tv_history_time = (TextView) convertView
                    .findViewById(R.id.tv_history_time);
            holder.tv_history_p2 = (TextView) convertView
                    .findViewById(R.id.tv_history_p2);
            holder.tv_history_result = (TextView) convertView
                    .findViewById(R.id.tv_history_result);
            holder.tv_history_cost = (TextView) convertView
                    .findViewById(R.id.tv_history_cost);
            holder.tv_history_cost2 = (TextView) convertView
                    .findViewById(R.id.tv_history_cost2);
            holder.tv_history_p3 = (TextView) convertView
                    .findViewById(R.id.tv_history_p3);
            holder.hideArea = (LinearLayout) convertView.findViewById(R.id.layout_hideArea);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HashMap<String, String> item = list.get(position);

        // 注意：我们在此给响应点击事件的区域（我的样例里是 showArea 的线性布局）加入Tag。为了记录点击的 position。我们正好用 position 设置 Tag
        holder.showArea.setTag(position);
        holder.tv_history_date.setText(item.get("history_date"));
        holder.tv_history_start.setText(item.get("history_start"));
        holder.tv_history_stop.setText(item.get("history_stop"));
        holder.tv_history_p1.setText(item.get("history_p1"));
        holder.tv_history_time.setText(item.get("history_time"));
        holder.tv_history_p2.setText(item.get("history_p2"));
        holder.tv_history_result.setText(item.get("history_result"));
        holder.tv_history_cost.setText(item.get("history_cost"));
        holder.tv_history_cost2.setText(item.get("history_cost2"));
        holder.tv_history_p3.setText(item.get("history_p3"));

        //依据 currentItem 记录的点击位置来设置"相应Item"的可见性（在list依次载入列表数据时，每载入一个时都看一下是不是需改变可见性的那一条）
        if (currentItem == position) {
            holder.hideArea.setVisibility(View.VISIBLE);
        } else {
            holder.hideArea.setVisibility(View.GONE);
        }

        holder.showArea.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //用 currentItem 记录点击位置
                int tag = (Integer) view.getTag();
                if (tag == currentItem) { //再次点击
                    currentItem = -1; //给 currentItem 一个无效值
                } else {
                    currentItem = tag;
                }
                //通知adapter数据改变须要又一次载入
                notifyDataSetChanged(); //必须有的一步
            }
        });
        /*holder.tv_history_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(context, "hehe", Toast.LENGTH_SHORT).show();
            }
        });*/
        return convertView;
    }

    private static class ViewHolder {
        private LinearLayout showArea;

        private TextView tvPhoneType;
        private TextView tvDiscount;
        private TextView tvPrice;
        private TextView tvTime;
        private TextView tvNum;
        private TextView
                tv_history_date, tv_history_start, tv_history_stop, tv_history_p1, tv_history_time, tv_history_cost,
                tv_history_p2, tv_history_p3, tv_history_result, tv_history_cost2;
        private Button btnBuy;

        private LinearLayout hideArea;
    }
}
