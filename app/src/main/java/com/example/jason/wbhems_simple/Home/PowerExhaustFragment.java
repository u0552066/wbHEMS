package com.example.jason.wbhems_simple.Home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.jason.wbhems_simple.R;


public class PowerExhaustFragment extends DialogFragment {
    private Button btn_OK;
    private TextView tvPower_exhaust1,tvPower_exhaust2;
    private SharedPreferences setting;
    private SharedPreferences.Editor settingedit;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //隐藏title
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        /*寫在這裡沒效果，要寫在onStart裡*/
//      getDialog().getWindow().getAttributes().width=getResources().getDisplayMetrics().widthPixels-200;
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_homepower_exhaust, container, false);
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
        tvPower_exhaust1 = getView().findViewById(R.id.fragment_powerExhaust1);
        tvPower_exhaust2 = getView().findViewById(R.id.fragment_powerExhaust2);
        setting = this.getActivity().getSharedPreferences("auto", 0);
        settingedit = setting.edit();
        String exhaust1 = setting.getString("power_exhaust1","0");
        String exhaust2 = setting.getString("power_exhaust2","0");
        tvPower_exhaust1.setText(exhaust1);
        tvPower_exhaust2.setText(exhaust2);
        btn_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStop();
            }
        });

    }
}
