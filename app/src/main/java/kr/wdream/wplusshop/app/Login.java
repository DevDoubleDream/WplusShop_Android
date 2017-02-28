package kr.wdream.wplusshop.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;

import kr.wdream.wplusshop.R;
import kr.wdream.wplusshop.common.DBManager;
import kr.wdream.wplusshop.common.LoginUtil;
import kr.wdream.wplusshop.common.Metrics;
import kr.wdream.wplusshop.common.util.CardLoginTask;
import kr.wdream.wplusshop.common.util.WcardUtil;

import static android.view.View.GONE;

/**
 * Created by SEO on 2015-12-17.
 */
public class Login extends Activity implements View.OnClickListener {

    public static String locationAc = "";
    public static String menuId = "";
    public boolean autoLogin = false;
    public String DBName = "WPLUSSHOP.db";
    public String tableName = "USER_SETTING";

    private Button btnLogin;
    private Button btnCardLogin;

    private boolean methodLogin = true;

    private EditText edtId;
    private EditText edtPw;

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


        if (!intent.getExtras().getBoolean("FROM")) {
            ((LinearLayout)findViewById(R.id.lytTop)).setVisibility(GONE);
            Log.d("상은", "locationAC try : " + locationAc);
            locationAc = "Main";
        }


        ((TextView) findViewById(R.id.txt_head_center)).setText("로그인");
        LinearLayout before_bullet = (LinearLayout)findViewById(R.id.icon_head_left);
        before_bullet.setVisibility(View.VISIBLE);
        before_bullet.setOnClickListener(this);
        ((LinearLayout)findViewById(R.id.btn_login)).setOnClickListener(this);
        ((LinearLayout)findViewById(R.id.lyt_btn_autologin)).setOnClickListener(this);

        Main.locationHistory.add(this);

        edtId = (EditText)findViewById(R.id.login_id);
        edtPw = (EditText)findViewById(R.id.login_pw);

        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        btnCardLogin = (Button)findViewById(R.id.btnCardLogin);
        btnCardLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.icon_head_left:
                finish();
                break;
            case R.id.btn_login:
                if (methodLogin) {
                    LoginUtil.inIt(this);
                    HashMap<String, String> loginParams = new HashMap<String, String>();

                    Log.d("상은", "LoginStart");
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

                    if (code.equals("0000")) {
                        Main.userInfo = new HashMap<String, String>();
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

                        updateParams.put("USER_ID", "'" + loginResult.get("user_id") + "'");
                        updateParams.put("USER_PW", "'" + loginResult.get("user_pw") + "'");
                        updateParams.put("USER_NO", "'" + loginResult.get("user_no") + "'");
                        updateParams.put("CARD_NO", "'" + loginResult.get("user_cardno") + "'");
                        updateParams.put("USER_GRADE", "'" + loginResult.get("user_grade") + "'");
                        updateParams.put("USER_DMN", "'" + loginResult.get("user_dmn") + "'");
                        updateParams.put("USER_NM", "'" + loginResult.get("user_nm") + "'");
                        updateParams.put("USER_CPOINT", "'" + loginResult.get("user_cpoint") + "'");
                        updateParams.put("USER_VIZ_AT", "'" + loginResult.get("user_viz_at") + "'");
                        updateParams.put("AUTO_LOGIN_YN", autoLogin ? "'Y'" : "'N'");
                        try {
                            dbManager.update(tableName, updateParams, whereParams);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (locationAc != null){
                            if (!locationAc.equals("")) {
                                Intent intent = null;
                                try {
                                    intent = new Intent(this, Class.forName("kr.wdream.wplusshop.app." + locationAc));
                                    intent.putExtra("testArray", loginResult);
                                    if (locationAc.equals("Use")) {
                                        intent.putExtra("menuId", menuId);
                                    }
                                    startActivity(intent);
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        finish();
                    }
                    else if(!code.equals("9999")){
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String id = edtId.getText().toString();
                    String pw = edtPw.getText().toString();

                    if ("".equals(id) || id.length() < 16) {
                        Toast.makeText(this, "카드번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                    } else if ("".equals(pw)) {
                        Toast.makeText(this, "카드 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                    } else {
                        new CardLoginTask(id, pw, handler).execute();
                    }
                }

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

            case R.id.btnLogin:
                methodLogin = true;
                ((EditText)findViewById(R.id.login_id)).setHint("아이디를 입력해주세요.");
                ((EditText)findViewById(R.id.login_pw)).setHint("비밀번호를 입력해주세요.");

                btnLogin.setTextColor(Color.parseColor("#F34950"));
                btnCardLogin.setTextColor(Color.parseColor("#919191"));

                ((ImageView)findViewById(R.id.icon_autologin)).setImageResource(R.drawable.auto_off);
                ((LinearLayout)findViewById(R.id.lyt_btn_autologin)).setVisibility(View.VISIBLE);
                ((LinearLayout)findViewById(R.id.lytFind)).setVisibility(View.VISIBLE);
                autoLogin = false;


                ((TextView)findViewById(R.id.textView2)).setText("로그인");

                break;

            case R.id.btnCardLogin:
                methodLogin = false;
                ((EditText)findViewById(R.id.login_id)).setHint("카드번호를 입력해주세요.");
                ((EditText)findViewById(R.id.login_pw)).setHint("카드 비밀번호를 입력해주세요.");

                btnLogin.setTextColor(Color.parseColor("#919191"));
                btnCardLogin.setTextColor(Color.parseColor("#F34950"));

                ((ImageView)findViewById(R.id.icon_autologin)).setImageResource(R.drawable.auto_off);
                ((LinearLayout)findViewById(R.id.lyt_btn_autologin)).setVisibility(GONE);
                ((LinearLayout)findViewById(R.id.lytFind)).setVisibility(View.GONE);
                autoLogin = false;

                ((TextView)findViewById(R.id.textView2)).setText("카드 로그인");

                break;
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            HashMap<String,String> resultLogin = (HashMap<String,String>) bundle.getSerializable("resultLogin");
            Metrics metrics = new Metrics(Login.this);
            switch (msg.what){
                case Metrics.CARD_LOGIN_SUCCESS:


                    String cardNo = resultLogin.get("cardNo");
                    String cardPw = resultLogin.get("cardPw");
                    String cardPoint = resultLogin.get("cPoint");
                    String cardState = resultLogin.get("cardStatCd");

                    metrics.setCardLoginNo(cardNo);
                    metrics.setCardLoginPw(cardPw);
                    metrics.setCardPoint(cardPoint);
                    metrics.setCardState(cardState);

                    Intent intent = new Intent(Login.this, CardLoginMain.class);
                    startActivity(intent);
                    finish();
                    if(Main.main!=null)
                        Main.main.finish();
                    break;

                case Metrics.CARD_LOGIN_FAILED:
                    Toast.makeText(Login.this, resultLogin.get("msg"), Toast.LENGTH_SHORT).show();
                    break;

                case Metrics.CARD_ISSUE_WAIT:
                    cardNo = resultLogin.get("cardNo");
                    cardPw = resultLogin.get("cardPw");
                    cardPoint = resultLogin.get("0");
                    cardState = resultLogin.get("cardStatCd");
                    Log.d("상은", "cardState : " + cardState);

                    metrics.setCardLoginNo(cardNo);
                    metrics.setCardLoginPw(cardPw);
                    metrics.setCardPoint(cardPoint);
                    metrics.setCardState(cardState);

                    intent = new Intent(Login.this, CardLoginMain.class);
                    startActivity(intent);
                    finish();
                    if(Main.main!=null)
                        Main.main.finish();
                    break;
            }
        }
    };

    public void prepareMessage(){
        Toast.makeText(this, "서비스 준비중 입니다.", Toast.LENGTH_SHORT).show();
    }
}
