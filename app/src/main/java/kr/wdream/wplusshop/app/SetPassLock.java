package kr.wdream.wplusshop.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;

import kr.wdream.wplusshop.R;
import kr.wdream.wplusshop.common.DBManager;

/**
 * Created by SEO on 2016-01-06.
 */
public class SetPassLock extends Activity implements View.OnClickListener{
    public String passLockCheck = "";
    public String passLockYN = "";
    public ImageButton passLock;
    public String DBName = "WPLUSSHOP.db";
    public String tableName = "USER_SETTING";
    public DBManager dbManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_pass_lock);

//        img_btn_pass_lock
        ((TextView) findViewById(R.id.txt_head_center)).setText("암호 잠금");
        LinearLayout before_bullet = (LinearLayout)findViewById(R.id.icon_head_left);
        before_bullet.setVisibility(View.VISIBLE);
        before_bullet.setOnClickListener(this);

        Main.locationHistory.add(this);

        passLockCheck = getIntent().getExtras().getString("LOCK_YN");
        passLock = (ImageButton) findViewById(R.id.img_btn_pass_lock);

        passLock.setOnClickListener(this);

        dbManager = new DBManager(getApplicationContext(), DBName, null, 4);

        setPassLock(passLockCheck);
//        #E0E0E0
    }

    @Override
    public void onResume() {
        super.onResume();
        setPassLock("");
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.icon_head_left:   // 이전
                finish();
                break;
            case R.id.img_btn_pass_lock:    // 알림 설정 선택 버튼
                if (passLockCheck.equals("OFF")){
                    intent = new Intent(this, PassLockNumber.class);
                    intent.putExtra("proc_type", "lock");
                    startActivity(intent);
                } else {
                    HashMap<String, String> updateParams = new HashMap<String, String>();    // update columns
                    updateParams.put("LOCK_YN", "'N'");
                    updateParams.put("LOCK_PW", "null");
                    HashMap<String, String> whereParams = new HashMap<String, String>();    // where 조건
                    try {
                        dbManager.update(tableName, updateParams, whereParams);
                        Toast.makeText(this, "암호 잠금 설정이 해지되었습니다.", Toast.LENGTH_SHORT).show();
                        setPassLock("");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.lyt_passlock:    // 암호 변경하기 버튼
                if (passLockCheck.equals("ON")){
                    intent = new Intent(this, PassLockNumber.class);
                    intent.putExtra("proc_type", "lock");
                    startActivity(intent);
                }
                break;
        }
    }

    public void setPassLock(String strPassLock){
        if(strPassLock.equals("")){
            String[] selectParams = {"LOCK_YN"};    // select columns
            HashMap<String, String> whereParams = new HashMap<String, String>();    // where 조건
            HashMap<String, String> userSetRst = new HashMap<String, String>();
            try {
                userSetRst = dbManager.select(tableName, selectParams, whereParams);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (userSetRst.size() > 0)  strPassLock = userSetRst.get("LOCK_YN").equals("Y") ? "ON" : "OFF";
        }
        passLock.setImageResource(strPassLock.equals("ON") ? R.drawable.setting_on : R.drawable.setting_off);
        ((TextView) findViewById(R.id.txt_passlock)).setTextColor(strPassLock.equals("ON") ? Color.parseColor("#666666") : Color.parseColor("#E0E0E0"));
        ((ImageView) findViewById(R.id.img_passlock_bullet)).setVisibility(strPassLock.equals("ON") ? View.VISIBLE : View.GONE);
        ((LinearLayout) findViewById(R.id.lyt_passlock)).setBackgroundResource(strPassLock.equals("ON") ? R.drawable.btn_color_selector : R.drawable.btn_color_white);
        passLockCheck = strPassLock;
    }
}
