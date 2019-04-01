package com.example.jason.wbhems_simple;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class ChangeAC extends Activity {
    private Spinner spinner_1,spinner_2;
    private Button  btn_return,btn_confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_ac);
        spinner_1 = findViewById(R.id.spinner1);
        spinner_2 = findViewById(R.id.spinner2);
        btn_return = findViewById(R.id.btn_cpwd_return);
        btn_confirm = findViewById(R.id.btn_cpwd_confirm);
        setCenterSpinner();
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reit = new Intent();
                reit.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                reit.setClass(ChangeAC.this,MainActivity.class);
                startActivity(reit);
                ChangeAC.this.finish();
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "更新成功！", Toast.LENGTH_SHORT).show();
                Intent reit = new Intent();
                reit.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                reit.setClass(ChangeAC.this,MainActivity.class);
                startActivity(reit);
                ChangeAC.this.finish();
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent reit = new Intent();
        reit.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        reit.setClass(ChangeAC.this,MainActivity.class);
        startActivity(reit);
        ChangeAC.this.finish();
        return true;
    }

    private void setCenterSpinner(){
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                this, R.array.Ac_position,R.layout.spinner_center_item);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                this, R.array.Ac_Label,R.layout.spinner_center_item);
        // set whatever dropdown resource you want
        adapter1.setDropDownViewResource(R.layout.spinner_center_item);
        adapter2.setDropDownViewResource(R.layout.spinner_center_item);

        spinner_1.setAdapter(adapter1);
        spinner_2.setAdapter(adapter2);
        //spinner1.setOnItemSelectedListener(new MyOnItemSelectedListener());
    }
}
