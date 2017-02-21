package kr.wdream.wplusshop.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;

import kr.wdream.wplusshop.R;
import kr.wdream.wplusshop.common.util.AppInfoUtil;
import kr.wdream.wplusshop.common.DBManager;

/**
 * Created by SEO on 2016-01-04.
 */
public class Setting extends Activity implements View.OnClickListener {

    public String DBName = "WPLUSSHOP.db";
    public String tableName = "USER_SETTING";

    public String autoLoginYN = "OFF";
    public String messageYN = "OFF";
    public String lockYN = "OFF";
    public String shopDomain = "";

    Intent intent;

    String appVer   = "";
    String appPath  = "";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        checkAppVersion();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        ((TextView) findViewById(R.id.txt_head_center)).setText("설정");
        LinearLayout before_bullet = (LinearLayout)findViewById(R.id.icon_head_left);
        before_bullet.setVisibility(View.VISIBLE);
        before_bullet.setOnClickListener(this);

        Main.locationHistory.add(this);

        setUserSettings();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon_head_left:
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUserSettings();
    }

    public void btnClick(View v) {
        switch (v.getId()) {
            case R.id.lyt_autologin:
                if(Main.userInfo.get("user_id").equals("")) {
                    intent = new Intent(this, Login.class);
                    intent.putExtra("locationActivity", "AutoLogin");
                }
                else{
                    intent = new Intent(this, AutoLogin.class);
                    intent.putExtra("AUTO_LOGIN_YN", autoLoginYN);
                }
                break;
            case R.id.lyt_msg:
                intent = new Intent(this, SetMessage.class);
                intent.putExtra("MSG_YN", messageYN);
                break;
            case R.id.lyt_pass:
                intent = new Intent(this, SetPassLock.class);
                intent.putExtra("LOCK_YN", lockYN);
                break;
            case R.id.lyt_domain:
                intent = new Intent(this, UserSetting.class);
                break;
            case R.id.lyt_notice:
                intent = new Intent(this, NoticeList.class);
                break;
        }
        startActivity(intent);
    }

    public void setUserSettings(){
        DBManager dbManager = new DBManager(getApplicationContext(), DBName, null, 4);
        String[] selectParams = {"AUTO_LOGIN_YN", "MSG_YN", "LOCK_YN", "LOCATION_DOMAIN"};    // select columns
        HashMap<String, String> whereParams = new HashMap<String, String>();    // where 조건
        HashMap<String, String> userSetRst = new HashMap<String, String>();
        try {
            userSetRst = dbManager.select(tableName, selectParams, whereParams);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("test_wplus", "Setting userSetRst.size() : " + userSetRst.size());

        if (userSetRst.size() > 0) {
            autoLoginYN = userSetRst.get("AUTO_LOGIN_YN").equals("Y") ? "ON" : "OFF";
            messageYN   = userSetRst.get("MSG_YN").equals("Y") ? "ON" : "OFF";
            lockYN      = userSetRst.get("LOCK_YN").equals("Y") ? "ON" : "OFF";
            shopDomain  = userSetRst.get("LOCATION_DOMAIN");
        }

        TextView txtAutoLogin = (TextView) findViewById(R.id.txt_autologin);
        txtAutoLogin.setText(autoLoginYN);
        txtAutoLogin.setTextColor(autoLoginYN.equals("ON") ? Color.parseColor("#f34950") : Color.parseColor("#c5c5c5"));

        TextView txtMessage = (TextView) findViewById(R.id.txt_msg);
        txtMessage.setText(messageYN);
        txtMessage.setTextColor(messageYN.equals("ON") ? Color.parseColor("#f34950") : Color.parseColor("#c5c5c5"));

        TextView txtPass = (TextView) findViewById(R.id.txt_pass);
        txtPass.setText(lockYN);
        txtPass.setTextColor(lockYN.equals("ON") ? Color.parseColor("#f34950") : Color.parseColor("#c5c5c5"));

        ((TextView) findViewById(R.id.txt_domain)).setText(shopDomain);
    }

    public void getAppInfo(){
        HashMap<String, String> appInfoParams = new HashMap<String, String>();
        appInfoParams.put("cmd", "appview");
        appInfoParams.put("app_id", "wplus");

        HashMap<String, String> appInfoResult = AppInfoUtil.getAppInfoResult(appInfoParams);

        String code     = appInfoResult.get("code");
        String message  = appInfoResult.get("message");

        if (code.equals("0000")) {
            appVer   = appInfoResult.get("app_ver");
            appPath  = appInfoResult.get("app_path");
        }
    }

    public void intentAct(){
        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.update_path)));
//        intent = new Intent(this, PushWebView.class);
//        intent.putExtra("LOCATION_URL", getString(R.string.update_path));
        startActivity(intent);
    }
    public void checkAppVersion(){
        getAppInfo();
        if(!appVer.equals("") && !appVer.equals(getString(R.string.app_version))){
            ((ImageView) findViewById(R.id.img_version_bullet)).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.txt_version_info)).setText("새로운 버전을 업데이트 해주세요.");
            ((LinearLayout) findViewById(R.id.lyt_btn_version)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intentAct();
                }
            });
        }
        else{
            ((ImageView) findViewById(R.id.img_version_bullet)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.txt_version_info)).setText("현재 최신 버전입니다.");

        }
    }
}
