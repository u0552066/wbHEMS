package com.example.jason.wbhems_simple.Home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.jason.wbhems_simple.R;


public class ElePriceFragment extends DialogFragment {
    private Button btn_OK;
    private SharedPreferences setting;
    private SharedPreferences.Editor settingedit;
    private TextView tvPrice;
    String User,Token;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //隐藏title
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        /*寫在這裡沒效果，要寫在onStart裡*/
//      getDialog().getWindow().getAttributes().width=getResources().getDisplayMetrics().widthPixels-200;
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_homepower_ele, container, false);
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
        btn_OK = getView().findViewById(R.id.btn_OK);
        tvPrice = getView().findViewById(R.id.fragment_elePrice);
        setting = this.getActivity().getSharedPreferences("auto", 0);
        User = setting.getString("User", "");
        settingedit = setting.edit();
        switch (User){
            case "92702":
                String price1 = setting.getString("price_92702","");
                tvPrice.setText(price1);
                break;
            case "92710":
                String price2 = setting.getString("price_92710","");
                tvPrice.setText(price2);
                break;
            case "92712":
                String price3 = setting.getString("price_92712","");
                tvPrice.setText(price3);
                break;
        }
        btn_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStop();
            }
        });

    }
}
