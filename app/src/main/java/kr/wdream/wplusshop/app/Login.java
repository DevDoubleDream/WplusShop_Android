package kr.wdream.wplusshop.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;

import kr.wdream.wplusshop.R;
import kr.wdream.wplusshop.common.CDialog;
import kr.wdream.wplusshop.common.DBManager;
import kr.wdream.wplusshop.common.LoginUtil;
import kr.wdream.wplusshop.common.util.WcardUtil;

/**
 * Created by SEO on 2015-12-17.
 */
public class Login extends Activity implements View.OnClickListener {
    public String Tag = "Login";
    public static String locationAc = "";
    public static String menuId = "";
    public boolean autoLogin = false;
    public String DBName = "WPLUSSHOP.db";
    public String tableName = "USER_SETTING";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        locationAc = "";
        Intent intent = getIntent();
        try {
            locationAc = intent.getExtras().getString("locationActivity");
            menuId = intent.getExtras().getString("menuId");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        ((TextView) findViewById(R.id.txt_head_center)).setText("로그인");
        LinearLayout before_bullet = (LinearLayout)findViewById(R.id.icon_head_left);
        before_bullet.setVisibility(View.VISIBLE);
        before_bullet.setOnClickListener(this);
        ((LinearLayout)findViewById(R.id.btn_login)).setOnClickListener(this);
        ((LinearLayout)findViewById(R.id.lyt_btn_autologin)).setOnClickListener(this);

        Main.locationHistory.add(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.icon_head_left:
                finish();
                break;
            case R.id.btn_login:
                LoginUtil.inIt(this);
                HashMap<String, String> loginParams = new HashMap<String, String>();

                String login_id = ((EditText) findViewById(R.id.login_id)).getText().toString();
                String login_pw = ((EditText) findViewById(R.id.login_pw)).getText().toString();

//                String login_id = "greatmrpark";
//                String login_pw = "KamrolY000";

//                String login_id = "arcnatone";
//                String login_pw = "rlawltjq3678!";

                loginParams.put("cmd", "login");
                loginParams.put("user_dmn", "app.wincash.co.kr");
                loginParams.put("user_grade", "M");
                loginParams.put("user_id", login_id);
                loginParams.put("user_pw", login_pw);

                HashMap<String, String> loginResult = LoginUtil.getLoginResult(loginParams);
//
                String code     = loginResult.get("code");
                String message  = loginResult.get("message");

                if (code.equals("0000")){
                    Main.userInfo.put("user_nm", loginResult.get("user_nm"));
                    Main.userInfo.put("user_id", loginResult.get("user_id"));
                    Main.userInfo.put("user_pw", loginResult.get("user_pw"));
                    Main.userInfo.put("user_no", loginResult.get("user_no"));
                    Main.userInfo.put("card_no", loginResult.get("user_cardno"));
                    Main.userInfo.put("user_grade", loginResult.get("user_grade"));
                    Main.userInfo.put("user_dmn", loginResult.get("user_dmn"));
                    Main.userInfo.put("user_cpoint", loginResult.get("user_cpoint"));
                    Main.userInfo.put("user_viz_at", loginResult.get("user_viz_at"));

                    HashMap<String, String> wcardParams = new HashMap<String, String>();
                    wcardParams.put("cmd", "info");
                    wcardParams.put("member_no", loginResult.get("user_no"));
                    wcardParams.put("member_cardno", loginResult.get("user_cardno"));

                    Log.d("상은", "Login : " + Main.userInfo.get("user_no") + "\n" + Main.userInfo.get("card_no"));
                    WcardUtil.getWcardResult(wcardParams);

                    DBManager dbManager = new DBManager(getApplicationContext(), DBName, null, 4);

                    // 자동 로그인 설정
                    HashMap<String, String> updateParams = new HashMap<String, String>();    // update columns
                    HashMap<String, String> whereParams = new HashMap<String, String>();    // where 조건

                        updateParams.put("USER_ID", "'"+ loginResult.get("user_id") +"'");
                        updateParams.put("USER_PW", "'"+ loginResult.get("user_pw") +"'");
                        updateParams.put("USER_NO", "'"+ loginResult.get("user_no") +"'");
                        updateParams.put("CARD_NO", "'"+ loginResult.get("user_cardno") +"'");
                        updateParams.put("USER_GRADE", "'"+ loginResult.get("user_grade") +"'");
                        updateParams.put("USER_DMN", "'"+ loginResult.get("user_dmn") +"'");
                        updateParams.put("USER_NM", "'"+ loginResult.get("user_nm") +"'");
                        updateParams.put("USER_CPOINT", "'"+ loginResult.get("user_cpoint") +"'");
                    updateParams.put("USER_VIZ_AT", "'" + loginResult.get("user_viz_at") +"'");
                        updateParams.put("AUTO_LOGIN_YN", autoLogin ? "'Y'" : "'N'");
                    try {
                        dbManager.update(tableName, updateParams, whereParams);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if(!locationAc.equals("")) {
                        Intent intent = null;
                        try {
                            intent = new Intent(this, Class.forName("kr.co.wdream.wplus.app."+ locationAc));
                            if(locationAc.equals("Use")){
                                intent.putExtra("menuId", menuId);
                            }
                            startActivity(intent);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }

                    }
                    finish();
                }
                else if(!code.equals("9999")){
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                }
                CDialog.showLoading(this);
                break;
            case R.id.lyt_btn_autologin:
                if(autoLogin) {
                    ((ImageView) findViewById(R.id.icon_autologin)).setImageResource(R.drawable.auto_off);
                    autoLogin = false;
                }else{
                    ((ImageView) findViewById(R.id.icon_autologin)).setImageResource(R.drawable.auto_on);
                    autoLogin = true;
                }
                break;
        }
    }

    public void prepareMessage(){
        Toast.makeText(this, "서비스 준비중 입니다.", Toast.LENGTH_SHORT).show();
    }
}
