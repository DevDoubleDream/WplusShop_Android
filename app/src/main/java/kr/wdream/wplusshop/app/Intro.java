package kr.wdream.wplusshop.app;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.io.IOException;
import java.util.HashMap;

import kr.wdream.wplusshop.R;
import kr.wdream.wplusshop.common.DBManager;

/**
 * Created by SEO on 2015-12-04.
 */
public class Intro extends Activity{

    Animation anim;
    public String DBName = "WPLUSSHOP.db";
    public String tableName = "USER_SETTING";
    public HashMap<String, String> loginInfo = new HashMap<String, String>();
    public String lockCheck = "N";
    public Intent intent;
    public String locationURI = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);

        intent = getIntent();
        try {
            locationURI = intent.getExtras().getString("locationURI");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Log.d("test_wplus", "locationURI : "+ locationURI);

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        pref.getString("check", "");
        if(pref.getString("check", "").isEmpty()){

            Log.d("test_wplus", "this : "+ this);
            Log.d("test_wplus", "getApplicationContext() : "+ getApplicationContext());
            Log.d("test_wplus", "getClass().getName() : "+ getClass().getName());

            Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
            shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            shortcutIntent.setClassName(this, getClass().getName());
            shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|
                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            intent = new Intent();

            intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
            intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getResources().getString(R.string.app_name));
            intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                    Intent.ShortcutIconResource.fromContext(this, R.drawable.icon));
            intent.putExtra("duplicate", false);
            intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");

            sendBroadcast(intent);
        }
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("check", "exist");
        editor.commit();

        DBManager dbManager = new DBManager(getApplicationContext(), DBName, null, 4);
        String[] selectParams = {"AUTO_LOGIN_YN", "LOCK_YN", "USER_ID", "USER_PW","USER_VIZ_AT", "USER_NO", "CARD_NO", "USER_GRADE", "USER_DMN", "USER_NM", "USER_CPOINT"};    // select columns
        HashMap<String, String> whereParams = new HashMap<String, String>();    // where 조건
        HashMap<String, String> userSetRst = null;
        try {
            userSetRst = dbManager.select(tableName, selectParams, whereParams);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("test_wplus", "userSetRst : "+ userSetRst);
        Log.d("test_wplus", "userSetRstSize : " + userSetRst.size());

        if (userSetRst.size() > 0) {
            lockCheck = userSetRst.get("LOCK_YN");
            if (userSetRst.get("AUTO_LOGIN_YN").equals("Y")) {
                loginInfo.put("USER_ID", userSetRst.get("USER_ID"));
                loginInfo.put("USER_PW", userSetRst.get("USER_PW"));
                loginInfo.put("USER_NO", userSetRst.get("USER_NO"));
                loginInfo.put("CARD_NO", userSetRst.get("CARD_NO"));
                loginInfo.put("USER_DMN", userSetRst.get("USER_DMN"));
                loginInfo.put("USER_NM", userSetRst.get("USER_NM"));
                loginInfo.put("USER_CPOINT", userSetRst.get("USER_CPOINT"));
                loginInfo.put("USER_VIZ_AT", userSetRst.get("USER_VIZ_AT"));
            }
        } else {
            String sql;
            sql =   "insert into USER_SETTING('USER_ID', 'USER_PW', 'USER_NO','USER_VIZ_AT', 'CARD_NO', 'USER_GRADE', 'USER_DMN', 'USER_NM', 'USER_CPOINT', 'LOCATION_DOMAIN') " +
                    "values('', '', '', '', '', '', '', '', '', '');";
            dbManager.insert(sql);
        }

        Log.d("test_wplus", "AUTO_LOGIN_YN : " + userSetRst.get("AUTO_LOGIN_YN"));
        Log.d("test_wplus", "USER_ID : " + loginInfo.get("USER_ID"));



        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ImageView icon_plus = (ImageView) findViewById(R.id.icon_plus);
                anim = AnimationUtils.loadAnimation(Intro.this, R.anim.translate);
                anim.setFillAfter(true);
                icon_plus.startAnimation(anim);
            }
        }, 800);

//        Handler handler2 = new Handler();
//        handler2.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ImageView icon_circle = (ImageView) findViewById(R.id.icon_circle);
//                icon_circle.setVisibility(View.VISIBLE);
//                anim = AnimationUtils.loadAnimation(Intro.this, R.anim.scale);
//                anim.setFillAfter(true);
//                icon_circle.startAnimation(anim);
//            }
//        }, 4000);

        Handler handler3 = new Handler();
        handler3.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(locationURI != null && !locationURI.equals("")){
                    intent = new Intent(Intro.this, PushWebView.class);
                    intent.putExtra("LOCATION_URL", locationURI);
                }
                else if (lockCheck.equals("Y")){
                    intent = new Intent(Intro.this, PassLockNumber.class);
                    intent.putExtra("proc_type", "check");
                }else
                    intent = new Intent(Intro.this, Main.class);

                intent.putExtra("LOGININFO", loginInfo);
                startActivity(intent);
                overridePendingTransition(R.anim.intro_fade_in, R.anim.intro_fade_out);
                finish();
            }
        }, 4800);
    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
    }
}
