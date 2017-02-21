package kr.wdream.wplusshop.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;

import kr.wdream.wplusshop.R;
import kr.wdream.wplusshop.common.DBManager;

/**
 * Created by SEO on 2016-01-05.
 */
public class AutoLogin extends Activity implements View.OnClickListener{
    public String autoLoginCheck = "OFF";
    public String autoLoginYN = "";
    public ImageButton autoLogin;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auto_login);

        ((TextView) findViewById(R.id.txt_head_center)).setText("자동 로그인");
        LinearLayout before_bullet = (LinearLayout)findViewById(R.id.icon_head_left);
        before_bullet.setVisibility(View.VISIBLE);
        before_bullet.setOnClickListener(this);

        Main.locationHistory.add(this);

        try {
            autoLoginCheck = getIntent().getExtras().getString("AUTO_LOGIN_YN");
        }catch (Exception e) {
        }

        autoLogin = (ImageButton) findViewById(R.id.img_btn_autologin);
        autoLogin.setImageResource(autoLoginCheck.equals("ON") ? R.drawable.setting_on : R.drawable.setting_off);
        autoLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String DBName = "WPLUSSHOP.db";
        String tableName = "USER_SETTING";

        switch (v.getId()) {
            case R.id.icon_head_left:   // 이전
                finish();
                break;
            case R.id.img_btn_autologin:    // 자동로그인 선택 버튼
                autoLoginCheck  = autoLoginCheck.equals("ON") ? "OFF" : "ON";
                autoLoginYN     = autoLoginCheck.equals("ON") ? "Y" : "N";
                autoLogin.setImageResource(autoLoginCheck.equals("ON") ? R.drawable.setting_on : R.drawable.setting_off);

                DBManager dbManager = new DBManager(getApplicationContext(), DBName, null, 4);
                HashMap<String, String> updateParams = new HashMap<String, String>();    // update columns
                HashMap<String, String> whereParams = new HashMap<String, String>();    // where 조건
                updateParams.put("AUTO_LOGIN_YN", "'"+ autoLoginYN +"'");
                try {
                    dbManager.update(tableName, updateParams, whereParams);
                    Toast.makeText(this, "자동 로그인 설정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
