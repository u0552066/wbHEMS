package com.example.jason.wbhems_simple;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


import com.example.jason.wbhems_simple.Control.ControlQuestionFragment;
import com.example.jason.wbhems_simple.DR.dr_equipmentActivity;
import com.example.jason.wbhems_simple.Main.ControlFragment_92702;
import com.example.jason.wbhems_simple.Main.ControlFragment_92710;
import com.example.jason.wbhems_simple.Main.ControlFragment_jun;
import com.example.jason.wbhems_simple.Main.DRFragment;
import com.example.jason.wbhems_simple.Main.IndexFragment_92702;
import com.example.jason.wbhems_simple.Main.IndexFragment_92710;
import com.example.jason.wbhems_simple.Main.IndexFragment_jun;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;
    MenuItem prevMenuItem;
    private SharedPreferences pref,setting;             //使用SharedPreferences進行讀取
    private SharedPreferences.Editor editor,settingedit; //使用SharedPreferences.Editor進行儲存
    private Toolbar toolbar;
    private TextView title;
    String user;
    private int[] TollBarTitle = {R.string.title_home,R.string.title_control,R.string.title_dr};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = this.getSharedPreferences("LoginInfo",0);
        editor = pref.edit();
        ///////////
        setting = this.getSharedPreferences("auto",0);
        settingedit = setting.edit();
        user = setting.getString("User", "92702"); //取出使用者帳號
        Log.d("User","使用者: "+user);
        /////////
        toolbar = (Toolbar) findViewById(R.id.ToolBar);
        setSupportActionBar(toolbar);
        setTitle(null); //取消原本的標題
        title = findViewById(R.id.toolbarTitle);
        title.setText(TollBarTitle[0]);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        //toolbar.getMenu().clear();
                        switch (item.getItemId()) {
                            //點擊首頁
                            case R.id.navigation_home:
                                viewPager.setCurrentItem(0);
                                //toolbar.inflateMenu(R.menu.menu_home);
                                title.setText(TollBarTitle[0]);
                                break;
                            //點擊電器控制
                            case R.id.navigation_control:
                                viewPager.setCurrentItem(1);
                                //toolbar.inflateMenu(R.menu.menu_control);
                                title.setText(TollBarTitle[1]);
                                break;
                            //點擊需量反應
                            case R.id.navigation_dr:
                                viewPager.setCurrentItem(2);
                                //toolbar.inflateMenu(R.menu.menu_dr);
                                title.setText(TollBarTitle[2]);
                                break;
                        }
                        return false;
                    }
                });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
                toolbar.getMenu().clear();
                switch (position) {
                    case 0:
                        toolbar.inflateMenu(R.menu.menu_home);
                        title.setText(TollBarTitle[0]);
                        break;
                    case 1:
                        toolbar.inflateMenu(R.menu.menu_control);
                        title.setText(TollBarTitle[1]);
                        break;
                    case 2:
                        toolbar.inflateMenu(R.menu.menu_dr);
                        title.setText(TollBarTitle[2]);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // 如果想禁止滑動，可以把下面的代碼取消註解
     /* viewPager.setOnTouchListener(new View.OnTouchListener() {
           @Override
          public boolean onTouch(View v, MotionEvent event) {
               return true;
          }
        });*/

        //設定fragment分頁頁數
        setViewPager();
    }


    //設置Activity初始的Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }
    //toolbar按鈕事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();
        switch (item_id){
            case R.id.ItemControlabout: //快速操作說明
                FragmentTransaction ControlQuestionFragmentTransaction = getSupportFragmentManager().beginTransaction();
                Fragment ControlQuestion = getSupportFragmentManager().findFragmentByTag("ControlQuestionFragment");
                if (ControlQuestion != null) {
                    ControlQuestionFragmentTransaction.remove(ControlQuestion);
                }
                ControlQuestionFragmentTransaction.addToBackStack(null);
                // Create and show the dialog.
                DialogFragment newFragment_ctrl = new ControlQuestionFragment();
                newFragment_ctrl.show(ControlQuestionFragmentTransaction, "ControlQuestionFragment");
                break;
            case R.id.changePWD: //修改密碼
                Intent reit1 = new Intent();
                reit1.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                reit1.setClass(MainActivity.this,ChangePWDActivity.class);
                startActivity(reit1);
                MainActivity.this.finish();
                break;
            case R.id.changeAC: //變更冷氣設定
                Intent reit2 = new Intent();
                reit2.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                reit2.setClass(MainActivity.this,ChangeAC.class);
                startActivity(reit2);
                MainActivity.this.finish();
                break;
            case R.id.Dr_equipment: //節電行動設備
                Intent reit3 = new Intent();
                reit3.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                reit3.setClass(MainActivity.this, dr_equipmentActivity.class);
                startActivity(reit3);
                MainActivity.this.finish();
                break;
            case R.id.ItemLogout: //登出
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("確認視窗")
                        .setMessage("確定要登出嗎?")
                        .setPositiveButton("確定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        //File file = new File("/data/data/net.ddns.b505.xlbems/shared_prefs","auto.xml");
                                        //file.delete();
                                        editor.putBoolean("auto_check",false).commit();
                                        Intent reit = new Intent();
                                        reit.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                        reit.setClass(MainActivity.this,LoginActivity.class);
                                        startActivity(reit);
                                        MainActivity.this.finish();
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
                break;

            default: return false;
        }
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("確認視窗")
                    .setMessage("確定要結束應用程式嗎?")
                    .setPositiveButton("確定",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    finish();
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
        return true;
    }

    private void setViewPager(){
        // 92702室
        IndexFragment_92702 Home_92702 = new IndexFragment_92702();
        ControlFragment_92702 Control_92702 = new ControlFragment_92702();
        DRFragment Reaction_92702 = new DRFragment();
        // 92710室
        IndexFragment_92710 Home_92710 = new IndexFragment_92710();
        ControlFragment_92710 Control_92710 = new ControlFragment_92710();
        // 92712室(俊銘講堂)
        IndexFragment_jun Home_jun = new IndexFragment_jun();
        ControlFragment_jun Control_jun = new ControlFragment_jun();

        List<Fragment> fragmentList = new ArrayList<Fragment>();
        switch (user) { // 判斷使用者
            case "92702":
                fragmentList.add(Home_92702);
                fragmentList.add(Control_92702);
                fragmentList.add(Reaction_92702);
                break;
            case "92710":
                fragmentList.add(Home_92710);
                fragmentList.add(Control_92710);
                fragmentList.add(Reaction_92702);
                break;
            case "92712": // 俊銘
                fragmentList.add(Home_jun);
                fragmentList.add(Control_jun);
                fragmentList.add(Reaction_92702);
                break;
        }
        ViewPagerFragmentAdapter myFragmentAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(myFragmentAdapter);
    }
}
