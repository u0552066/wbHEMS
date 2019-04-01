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


public class PowerFanFragment extends DialogFragment {
    private Button btn_OK;
    private SharedPreferences setting;
    private SharedPreferences.Editor settingedit;
    private TextView tvPower_fan1,tvPower_fan2,tvPower_fan3,tvPower_fan4,tvPower_fan5;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //隐藏title
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        /*寫在這裡沒效果，要寫在onStart裡*/
//      getDialog().getWindow().getAttributes().width=getResources().getDisplayMetrics().widthPixels-200;
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_homepower_fan, container, false);
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
        tvPower_fan1 = getView().findViewById(R.id.fragment_powerFan1);
        tvPower_fan2 = getView().findViewById(R.id.fragment_powerFan2);
        tvPower_fan3 = getView().findViewById(R.id.fragment_powerFan3);
        tvPower_fan4 = getView().findViewById(R.id.fragment_powerFan4);
        tvPower_fan5 = getView().findViewById(R.id.fragment_powerFan5);
        setting = this.getActivity().getSharedPreferences("auto", 0);
        settingedit = setting.edit();
        String fan1 = setting.getString("power_fan1","0");
        String fan2 = setting.getString("power_fan2","0");
        String fan3 = setting.getString("power_fan3","0");
        String fan4 = setting.getString("power_fan4","0");
        String fan5 = setting.getString("power_fan5","0");
        tvPower_fan1.setText(fan1);
        tvPower_fan2.setText(fan2);
        tvPower_fan3.setText(fan3);
        tvPower_fan4.setText(fan4);
        tvPower_fan5.setText(fan5);
        btn_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStop();
            }
        });


    }
}
