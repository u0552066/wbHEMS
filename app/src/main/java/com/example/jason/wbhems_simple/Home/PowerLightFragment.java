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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.jason.wbhems_simple.R;


public class PowerLightFragment extends DialogFragment {
    private LinearLayout linearLayout_92702, linearLayout_92710, linearLayout_92712;
    private Button btn_OK,btn_OK_92710,btn_OK_92712;
    private SharedPreferences setting;
    private SharedPreferences.Editor settingedit;
    private TextView tvPower_light1,tvPower_light2,tvPower_light3,tvPower_light4,tvPower_light5,tvPower_light6; // 92702
    private TextView tvPower_light1_92710,tvPower_light2_92710; // 92710
    private TextView tvPower_light1_92712,tvPower_light2_92712,tvPower_light3_92712,tvPower_light4_92712; // 92702
    String user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //隐藏title
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        /*寫在這裡沒效果，要寫在onStart裡*/
//      getDialog().getWindow().getAttributes().width=getResources().getDisplayMetrics().widthPixels-200;
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_homepower_light, container, false);
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
        linearLayout_92702 = getView().findViewById(R.id.light_92702);
        linearLayout_92710 = getView().findViewById(R.id.light_92710);
        linearLayout_92712 = getView().findViewById(R.id.light_92712);
        btn_OK = getView().findViewById(R.id.btn_OK);
        btn_OK_92710 = getView().findViewById(R.id.btn_OK_92710);
        btn_OK_92712 = getView().findViewById(R.id.btn_OK_92712);
        tvPower_light1 = getView().findViewById(R.id.fragment_powerLight1);
        tvPower_light2 = getView().findViewById(R.id.fragment_powerLight2);
        tvPower_light3 = getView().findViewById(R.id.fragment_powerLight3);
        tvPower_light4 = getView().findViewById(R.id.fragment_powerLight4);
        tvPower_light5 = getView().findViewById(R.id.fragment_powerLight5);
        tvPower_light6 = getView().findViewById(R.id.fragment_powerLight6);
        tvPower_light1_92710 = getView().findViewById(R.id.powerLight1_92710);
        tvPower_light2_92710 = getView().findViewById(R.id.powerLight2_92710);
        tvPower_light1_92712 = getView().findViewById(R.id.powerLight1_92712);
        tvPower_light2_92712 = getView().findViewById(R.id.powerLight2_92712);
        tvPower_light3_92712 = getView().findViewById(R.id.powerLight3_92712);
        tvPower_light4_92712 = getView().findViewById(R.id.powerLight4_92712);
        setting = this.getActivity().getSharedPreferences("auto", 0);
        settingedit = setting.edit();
        user = setting.getString("User", "92702"); //取出使用者帳號

        switch (user){
            case "92702":
                linearLayout_92702.setVisibility(View.VISIBLE);
                linearLayout_92710.setVisibility(View.GONE);
                linearLayout_92712.setVisibility(View.GONE);
                String light1 = setting.getString("power_light1","0");
                String light2 = setting.getString("power_light2","0");
                String light3 = setting.getString("power_light3","0");
                String light4 = setting.getString("power_light4","0");
                String light5 = setting.getString("power_light5","0");
                String light6 = setting.getString("power_light6","0");
                tvPower_light1.setText(light1);
                tvPower_light2.setText(light2);
                tvPower_light3.setText(light3);
                tvPower_light4.setText(light4);
                tvPower_light5.setText(light5);
                tvPower_light6.setText(light6);
                btn_OK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onStop();
                    }
                });
                break;
            case "92710":
                linearLayout_92702.setVisibility(View.GONE);
                linearLayout_92710.setVisibility(View.VISIBLE);
                linearLayout_92712.setVisibility(View.GONE);
                String light1_92710 = setting.getString("power_light1_92710","0");
                String light2_92710 = setting.getString("power_light2_92710","0");
                tvPower_light1_92710.setText(light1_92710);
                tvPower_light2_92710.setText(light2_92710);
                btn_OK_92710.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onStop();
                    }
                });
                break;
            case "92712":
                linearLayout_92702.setVisibility(View.GONE);
                linearLayout_92710.setVisibility(View.GONE);
                linearLayout_92712.setVisibility(View.VISIBLE);
                String light1_92712 = setting.getString("power_light1_92712","0");
                String light2_92712 = setting.getString("power_light2_92712","0");
                String light3_92712 = setting.getString("power_light3_92712","0");
                String light4_92712 = setting.getString("power_light4_92712","0");
                tvPower_light1_92712.setText(light1_92712);
                tvPower_light2_92712.setText(light2_92712);
                tvPower_light3_92712.setText(light3_92712);
                tvPower_light4_92712.setText(light4_92712);
                btn_OK_92712.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onStop();
                    }
                });
                break;
        }

    }
}
