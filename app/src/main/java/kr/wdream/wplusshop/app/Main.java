package kr.wdream.wplusshop.app;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import kr.wdream.wplusshop.R;
import kr.wdream.wplusshop.common.BackPressCloseHandler;
import kr.wdream.wplusshop.common.DBManager;
import kr.wdream.wplusshop.common.LoginUtil;
import kr.wdream.wplusshop.common.QuickstartPreferences;
import kr.wdream.wplusshop.common.util.RegistDeviceUtil;
import kr.wdream.wplusshop.common.RegistrationIntentService;
import kr.wdream.wplusshop.common.util.WcardUtil;

public class Main extends Activity implements View.OnClickListener{

    public static Main main;

    public static ArrayList<Activity> locationHistory = new ArrayList<Activity>();

    private static int MY_PERMISSION_REQUEST_STORAGE = 100;
    private BackPressCloseHandler backPressCloseHandler;
    public static HashMap<String, String> userInfo = new HashMap<String, String>();

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "Main";

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private String APP_ID = "wplus";

    DecimalFormat df = new DecimalFormat("#,##0");

    Animation anim;
    LinearLayout lytLMenuBar;
    LinearLayout lytRMenuBar;
    TextView txtLMenu;
    TextView txtRMenu;
    boolean menuPosition = true;
    String ssoURL = "";

    private DrawerLayout drawerLayout;
    private View drawerView;

    String MallDomain = "";
    Intent intent;

    HashMap<String, String> loginInfo = null;

    String user_id = "";

    String appVer   = "";
    String appPath  = "";

    private LinearLayout lyt_viz;
    private LinearLayout lyt_center;

    private TelephonyManager telephony;
    private HashMap<String, String> registDeviceParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        main = this;

        if(CardLoginMain.cardLoginMain != null)
            CardLoginMain.cardLoginMain.finish();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        if (Build.VERSION.SDK_INT >= 23) {
            checkPermission();
        }else{
            registBroadcastReceiver();
        }

        new GetMallDomain().execute();

        loginInfo = (HashMap<String, String>) getIntent().getSerializableExtra("LOGININFO");

        if(loginInfo != null) {
            if (loginInfo.size() > 0) {

                Log.d(TAG, "loginInfo1  : " + loginInfo);
                userInfo.put("user_id", loginInfo.get("USER_ID"));
                userInfo.put("user_pw", loginInfo.get("USER_PW"));
                userInfo.put("user_no", loginInfo.get("USER_NO"));
                userInfo.put("card_no", loginInfo.get("CARD_NO"));
                userInfo.put("user_grade", loginInfo.get("USER_GRADE"));
                userInfo.put("user_dmn", loginInfo.get("USER_DMN"));
                userInfo.put("user_nm", loginInfo.get("USER_NM"));
                userInfo.put("user_cpoint", loginInfo.get("USER_CPOINT"));
                userInfo.put("user_viz_at", loginInfo.get("USER_VIZ_AT"));

                new mainLoadingTask().execute();

            } else {
                Log.d(TAG, "loginInfo2  : " + loginInfo);
                userInfo.put("user_id", "");
                userInfo.put("user_pw", "");
                userInfo.put("user_no", "");
                userInfo.put("card_no", "");
                userInfo.put("user_grade", "");
                userInfo.put("user_dmn", "");
                userInfo.put("user_nm", "");
                userInfo.put("user_cpoint", "");
                userInfo.put("user_viz_at","");
            }
        }
        else {
            Log.d(TAG, "loginInfo3  : " + loginInfo);
            userInfo.put("user_id", "");
            userInfo.put("user_pw", "");
            userInfo.put("user_no", "");
            userInfo.put("card_no", "");
            userInfo.put("user_grade", "");
            userInfo.put("user_dmn", "");
            userInfo.put("user_nm", "");
            userInfo.put("user_cpoint", "");
            userInfo.put("user_viz_at", "");
        }

        locationHistory.add(this);
        backPressCloseHandler = new BackPressCloseHandler(this);

        lytLMenuBar = (LinearLayout) findViewById(R.id.lyt_left_menubar);
        lytRMenuBar = (LinearLayout) findViewById(R.id.lyt_right_menubar);
        txtLMenu    = (TextView) findViewById(R.id.txt_use);
        txtRMenu    = (TextView) findViewById(R.id.txt_wzone);

        getInstanceIdToken();

        lyt_center = (LinearLayout)findViewById(R.id.lyt_btn_viz);
        lyt_viz = (LinearLayout)findViewById(R.id.lyt_btn_vision);

        ((ImageView) findViewById(R.id.icon_head_left)).setOnClickListener(this);

        ((LinearLayout) findViewById(R.id.btn_close)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.icon_head_right)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.lyt_btn_menu_login)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.lyt_btn_main_login)).setOnClickListener(this);

        // 메뉴 버튼
        ((LinearLayout) findViewById(R.id.btn_setting)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.lyt_btn_menu_pointsearch)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.lyt_btn_menu_charge)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.btn_menu_store)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.btn_menu_payment)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.btn_menu_gift)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.btn_menu_donation)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.btn_menu_today)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.btn_menu_wmart)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.btn_menu_viz)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.btn_menu_vision)).setOnClickListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.drawer);

        drawerView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                return true;
            }
        });
    }

    private void checkPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_SMS)) {

                }
                requestPermissions(new String[]{Manifest.permission.READ_SMS}, MY_PERMISSION_REQUEST_STORAGE);
            }else{
                registBroadcastReceiver();
            }
        }
    }

    public void setLogin(){
        if (!userInfo.get("user_id").equals("")){
            LoginUtil.inIt(this);
            HashMap<String, String> loginParams = new HashMap<String, String>();
            String login_id = userInfo.get("user_id");
            String login_pw = userInfo.get("user_pw");

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
                Log.d(TAG, "loginResult  : " + loginResult);
                userInfo.put("user_nm", loginResult.get("user_nm"));
                userInfo.put("user_id", loginResult.get("user_id"));
                userInfo.put("user_pw", loginResult.get("user_pw"));
                userInfo.put("user_no", loginResult.get("user_no"));
                userInfo.put("card_no", loginResult.get("user_cardno"));
                userInfo.put("user_grade", loginResult.get("user_grade"));
                userInfo.put("user_dmn", loginResult.get("user_dmn"));
                userInfo.put("user_cpoint", loginResult.get("user_cpoint"));
                userInfo.put("user_viz_at", loginResult.get("user_viz_at"));
            }

            StringBuffer sb = new StringBuffer(Main.userInfo.get("card_no"));
            sb.insert(4, "-").insert(9, "-").insert(14, "-");
            String card_no = sb.toString();

//            메뉴 로그인 layout 변경
            ((LinearLayout) findViewById(R.id.lyt_menu_login)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.lyt_menu_logout)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.txt_menu_nm)).setText(userInfo.get("user_nm"));
            ((TextView) findViewById(R.id.txt_menu_cardno)).setText(card_no);

//            메인 로그인 layout 변경
            ((LinearLayout) findViewById(R.id.lyt_main_login)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.lyt_main_logout)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.txt_main_cpoint)).setText(df.format(Integer.parseInt(userInfo.get("user_cpoint"))));
            if(Integer.parseInt(userInfo.get("user_cpoint")) >= 100000) {
                ((TextView) findViewById(R.id.txt_main_cpoint)).setTextSize(50);
                ((TextView) findViewById(R.id.txt_main_cp)).setTextSize(30);
            }
            ((TextView) findViewById(R.id.txt_main_cardno)).setText(card_no);


        }
        else{
//            메뉴 로그인 layout 변경
            ((LinearLayout) findViewById(R.id.lyt_menu_login)).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.lyt_menu_logout)).setVisibility(View.VISIBLE);
//            메인 로그인 layout 변경
            ((LinearLayout) findViewById(R.id.lyt_main_login)).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.lyt_main_logout)).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.icon_head_left:
                drawerLayout.openDrawer(drawerView);
                break;
            case R.id.btn_close:
                drawerLayout.closeDrawers();
                break;

            case R.id.lyt_btn_menu_login:
            case R.id.lyt_btn_main_login:
            case R.id.icon_head_right:
                if (!Main.userInfo.get("user_no").equals("")) {
                    Main.userInfo.put("user_id", "");
                    Main.userInfo.put("user_pw", "");
                    Main.userInfo.put("user_no", "");
                    Main.userInfo.put("card_no", "");
                    Main.userInfo.put("user_grade", "");
                    Main.userInfo.put("user_dmn", "");
                    Main.userInfo.put("user_nm", "");
                    Main.userInfo.put("user_viz_at", "");

                    setLogin();
                    String DBName = "WPLUSSHOP.db";
                    String tableName = "USER_SETTING";
                    DBManager dbManager = new DBManager(getApplicationContext(), DBName, null, 4);
                    HashMap<String, String> updateParams = new HashMap<String, String>();    // update columns
                    HashMap<String, String> whereParams = new HashMap<String, String>();    // where 조건
                    updateParams.put("AUTO_LOGIN_YN", "'N'");

                    lyt_center.setVisibility(View.GONE);
                    lyt_viz.setVisibility(View.GONE);
                    try {
                        dbManager.update(tableName, updateParams, whereParams);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    intent = new Intent(this, Login.class);
                    intent.putExtra("FROM", true);
                    startActivity(intent);

                }
                break;


            // ======================메인 버튼 시작===========================


            // ======================메인 버튼 끝===========================



            // ======================메뉴 버튼 시작===========================
            case R.id.btn_setting: // 쇼핑몰 설정
                intent = new Intent(this, Setting.class);
                startActivity(intent);
                drawerLayout.closeDrawers();
                break;
            case R.id.lyt_btn_menu_pointsearch: // 포인트 조회
                if(userInfo.get("user_no").equals("")){
                    intent = new Intent(this, Login.class);
                    intent.putExtra("locationActivity", "Point");
                    intent.putExtra("FROM", true);
                }else{
                    intent = new Intent(this, Point.class);
                    startActivity(intent);
                }
                break;
            case R.id.lyt_btn_menu_charge: // 충전하기
                prepareMessage();
                break;
            case R.id.btn_menu_store:   // 가맹점
                intent = new Intent(this, Use.class);
                intent.putExtra("menuId", "0");
                startActivity(intent);
                drawerLayout.closeDrawers();
                break;
            case R.id.btn_menu_payment:
                if(userInfo.get("user_no").equals("")){
                    intent = new Intent(this, Login.class);
                    intent.putExtra("locationActivity", "Use");
                    intent.putExtra("FROM", true);
                }else
                    intent = new Intent(this, Use.class);

                intent.putExtra("menuId", "1");
                startActivity(intent);
                drawerLayout.closeDrawers();
                break;
            case R.id.btn_menu_gift:
                if(userInfo.get("user_no").equals("")){
                    intent = new Intent(this, Login.class);
                    intent.putExtra("locationActivity", "Use");
                    intent.putExtra("FROM", true);
                }else
                    intent = new Intent(this, Use.class);

                intent.putExtra("menuId", "2");
                startActivity(intent);
                drawerLayout.closeDrawers();
                break;
            case R.id.btn_menu_donation:
                if(userInfo.get("user_no").equals("")){
                    intent = new Intent(this, Login.class);
                    intent.putExtra("FROM", true);
                    intent.putExtra("locationActivity", "Use");
                }else
                    intent = new Intent(this, Use.class);

                intent.putExtra("menuId", "3");
                startActivity(intent);
                drawerLayout.closeDrawers();
                break;
            case R.id.btn_menu_today:
                intent = new Intent(this, TodayDeal.class);
                startActivity(intent);
                drawerLayout.closeDrawers();
                break;
            case R.id.btn_menu_wmart:
                intent = new Intent(this, PushWebView.class);
                intent.putExtra("LOCATION_URL", MallDomain);
                intent.putExtra("TITLE", "가족의 일상에서 필요로 하는 제품을 판매하는 쇼핑몰.");
                intent.putExtra("SUMMARY", "");
                intent.putExtra("PICTURE", "");
                startActivity(intent);

                break;
            case R.id.btn_menu_viz:
                if(Main.userInfo.get("user_id").toString().equals("")){
                    ssoURL = "http://viz.wincash.co.kr";
                }else{
                    ssoURL = "http://api.wincash.co.kr/login/loginprocess.asp?cmd=sso&user_id="+ Main.userInfo.get("user_id") +"&user_pw="+ Main.userInfo.get("user_pw") +"&user_grade="+ Main.userInfo.get("user_grade") +"&user_dmn=viz.wincash.co.kr";
                }
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ssoURL));
                startActivity(intent);
                break;
            case R.id.btn_menu_vision:
                if(Main.userInfo.get("user_id").toString().equals("")){
                    ssoURL = "http://www.wvision.co.kr";
                }else{
                    ssoURL = "http://api.wincash.co.kr/login/loginprocess.asp?cmd=sso&user_id="+ Main.userInfo.get("user_id") +"&user_pw="+ Main.userInfo.get("user_pw") +"&user_grade="+ Main.userInfo.get("user_grade") +"&user_dmn=m.wvision.co.kr";
                }
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ssoURL));
                startActivity(intent);
                break;
            // ======================메뉴 버튼 끝===========================
        }
        Log.d("wplus_test", "onClick : " + v.getId());
    }


    public void btnClick(View v){
        switch (v.getId()){
            case R.id.lyt_use:
                if(!menuPosition) {
                    anim = AnimationUtils.loadAnimation(this, R.anim.translate_layer_left);
                    anim.setFillAfter(false);
                    lytRMenuBar.startAnimation(anim);

                    menuPosition = true;
                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            lytLMenuBar.setVisibility(View.VISIBLE);
                            lytRMenuBar.setVisibility(View.INVISIBLE);
                            txtLMenu.setTextColor(Color.parseColor("#f34950"));
                            txtRMenu.setTextColor(Color.parseColor("#949494"));
                            ((HorizontalScrollView) findViewById(R.id.lyt_menu_use)).setVisibility(View.VISIBLE);
                            ((HorizontalScrollView) findViewById(R.id.lyt_menu_wzone)).setVisibility(View.GONE);
                        }
                    }, 300);
                }
                break;
            case R.id.lyt_wzone:
                if(menuPosition) {
                    anim = AnimationUtils.loadAnimation(this, R.anim.translate_layer_right);
                    anim.setFillAfter(false);
                    lytLMenuBar.startAnimation(anim);

                    menuPosition = false;
                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            lytRMenuBar.setVisibility(View.VISIBLE);
                            lytLMenuBar.setVisibility(View.INVISIBLE);
                            txtLMenu.setTextColor(Color.parseColor("#949494"));
                            txtRMenu.setTextColor(Color.parseColor("#f34950"));
                            ((HorizontalScrollView) findViewById(R.id.lyt_menu_wzone)).setVisibility(View.VISIBLE);
                            ((HorizontalScrollView) findViewById(R.id.lyt_menu_use)).setVisibility(View.GONE);
                        }
                    }, 300);
                }
                break;

            case R.id.lyt_btn_store:
                intent = new Intent(this, Use.class);
                intent.putExtra("menuId", "0");
                startActivity(intent);
                break;
            case R.id.lyt_btn_payment:
                if(userInfo.get("user_no").equals("")){
                    intent = new Intent(this, Login.class);
                    intent.putExtra("locationActivity", "Use");
                    intent.putExtra("FROM", true);
                }else
                    intent = new Intent(this, Use.class);

                intent.putExtra("menuId", "1");
                startActivity(intent);
                break;
            case R.id.lyt_btn_gift:
                if(userInfo.get("user_no").equals("")){
                    intent = new Intent(this, Login.class);
                    intent.putExtra("locationActivity", "Use");
                    intent.putExtra("FROM", true);
                }else
                    intent = new Intent(this, Use.class);

                intent.putExtra("menuId", "2");
                startActivity(intent);
                break;
            case R.id.lyt_btn_donation:
                if(userInfo.get("user_no").equals("")){
                    intent = new Intent(this, Login.class);
                    intent.putExtra("locationActivity", "Use");
                    intent.putExtra("FROM", true);
                }else
                    intent = new Intent(this, Use.class);

                intent.putExtra("menuId", "3");
                startActivity(intent);
                break;
            case R.id.lyt_btn_setting:
                intent = new Intent(this, UserSetting.class);
                startActivity(intent);
                break;
            case R.id.btn_txt_pointsearch:
                intent = new Intent(this, Point.class);
                startActivity(intent);
                break;
            case R.id.btn_txt_charge:
                prepareMessage();
                break;
            case R.id.lyt_btn_today:
                intent = new Intent(this, TodayDeal.class);
                startActivity(intent);
                break;
            case R.id.lyt_btn_wmart:
                intent = new Intent(this, PushWebView.class);
                intent.putExtra("LOCATION_URL", MallDomain);
                intent.putExtra("TITLE", "");
                intent.putExtra("SUMMARY", "");
                intent.putExtra("PICTURE", "");
                startActivity(intent);
                break;
            case R.id.lyt_btn_viz:
                if(Main.userInfo.get("user_id").toString().equals("")){
                    ssoURL = "http://viz.wincash.co.kr";
                }else{
                    ssoURL = "http://api.wincash.co.kr/login/loginprocess.asp?cmd=sso&user_id="+ Main.userInfo.get("user_id") +"&user_pw="+ Main.userInfo.get("user_pw") +"&user_grade="+ Main.userInfo.get("user_grade") +"&user_dmn=viz.wincash.co.kr";
                }
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ssoURL));
                startActivity(intent);
                break;
            case R.id.lyt_btn_vision:
                if(Main.userInfo.get("user_id").toString().equals("")){
                    ssoURL = "http://www.wvision.co.kr";
                }else{
                    ssoURL = "http://api.wincash.co.kr/login/loginprocess.asp?cmd=sso&user_id="+ Main.userInfo.get("user_id") +"&user_pw="+ Main.userInfo.get("user_pw") +"&user_grade="+ Main.userInfo.get("user_grade") +"&user_dmn=m.wvision.co.kr";
                }
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ssoURL));
                startActivity(intent);
                break;
        }
        Log.d("wplus_test", "btnClick : "+ v.getId());
    }

    public void prepareMessage(){
        Toast.makeText(this, "서비스 준비중 입니다.", Toast.LENGTH_SHORT).show();
    }
    /**
     * Instance ID를 이용하여 디바이스 토큰을 가져오는 RegistrationIntentService를 실행한다.
     */
    public void getInstanceIdToken() {
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    /**
     * LocalBroadcast 리시버를 정의한다. 토큰을 획득하기 위한 READY, GENERATING, COMPLETE 액션에 따라 UI에 변화를 준다.
     */
    public void registBroadcastReceiver(){
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if(action.equals(QuickstartPreferences.REGISTRATION_COMPLETE)){
                    // 액션이 COMPLETE일 경우
                    String token = intent.getStringExtra("token");
                    Log.d("dev_test", "token : "+ token);
                    // 포인트 내역 조회
                    registDeviceParams = new HashMap<String, String>();


                            Log.d(TAG,"상은2");
                            telephony=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

                            registDeviceParams.put("cmd"            , "device");	//명령코드
                            registDeviceParams.put("device_os"      , Build.DEVICE);	//단말OS
                            registDeviceParams.put("device_id"      , Build.ID);	//단말아이디
                            registDeviceParams.put("device_name"    , Build.MODEL);	//단말이름
                            registDeviceParams.put("registrationid" , token);	// 등록아이디
                            registDeviceParams.put("service_no"     , telephony.getLine1Number());	// 서비스번호
                            registDeviceParams.put("app_id"         , APP_ID);	// APP 아이디
                            registDeviceParams.put("app_os"         , "A");	// APP OS
                            registDeviceParams.put("app_ver"        , "2.0");	// APP 버전
                            registDeviceParams.put("push_at"        , "Y");	// 알림여부
                            registDeviceParams.put("member_no"      , "0");	// 회원번호
                            registDeviceParams.put("member_id"      , "");	// 회원아이디
                            registDeviceParams.put("member_name"    , "");	// 회원이름
                            HashMap<String, String> pointList = RegistDeviceUtil.getRegistDeviceResult(registDeviceParams);


                }

            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        if("Y".equals(userInfo.get("user_viz_at"))){
            lyt_center.setVisibility(View.VISIBLE);
            lyt_viz.setVisibility(View.VISIBLE);
        }

        Intent getIntent = getIntent();
        if (getIntent.getSerializableExtra("testArray") != null) {
            HashMap<String,String> loginResult = (HashMap<String,String>) getIntent.getSerializableExtra("testArray");
            Main.userInfo.put("user_nm", loginResult.get("user_nm"));
            Main.userInfo.put("user_id", loginResult.get("user_id"));
            Main.userInfo.put("user_pw", loginResult.get("user_pw"));
            Main.userInfo.put("user_no", loginResult.get("user_no"));
            Main.userInfo.put("card_no", loginResult.get("user_cardno"));
            Main.userInfo.put("user_grade", loginResult.get("user_grade"));
            Main.userInfo.put("user_dmn", loginResult.get("user_dmn"));
            Main.userInfo.put("user_cpoint", loginResult.get("user_cpoint"));
            Main.userInfo.put("user_viz_at", loginResult.get("user_viz_at"));
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_READY));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_GENERATING));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));

        // 로그인 설정
        setLogin();

        // 도메인 설정
        new GetMallDomain().execute();


    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    /**
     * Google Play Service를 사용할 수 있는 환경이지를 체크한다.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        backPressCloseHandler.onBackPressed();
    }

    public String getMallDomain(){
        String returnDomain;
        String DBName = "WPLUSSHOP.db";
        String tableName = "USER_SETTING";

        DBManager dbManager = new DBManager(getApplicationContext(), DBName, null, 4);
        String[] selectParams = {"LOCATION_DOMAIN"};    // select columns
        HashMap<String, String> whereParams = new HashMap<String, String>();    // where 조건
        HashMap<String, String> userSetRst = null;
        try {
            userSetRst = dbManager.select(tableName, selectParams, whereParams);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (userSetRst.isEmpty())   returnDomain = "http://www.wpoint.co.kr";
        else                        returnDomain = userSetRst.get("LOCATION_DOMAIN").equals("") ? "http://www.wpoint.co.kr" : "http://"+ userSetRst.get("LOCATION_DOMAIN");

        return returnDomain;
    }

    private class mainLoadingTask extends AsyncTask {
        // 실제 화면에 보여지는 장소
        // 다이얼로그 생성
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        // Background로 실행 될 실제 코드
        @Override
        protected Object doInBackground(Object[] objects) {
            // ViewPager 전부 로딩

            HashMap<String, String> wcardParams = new HashMap<String, String>();
            wcardParams.put("cmd", "info");
            wcardParams.put("member_no", Main.userInfo.get("user_no"));
            wcardParams.put("member_cardno", Main.userInfo.get("card_no"));

            Log.d(TAG, "Login : " + Main.userInfo.get("user_no") + "\n" + Main.userInfo.get("card_no"));
            WcardUtil.getWcardResult(wcardParams);

            return null;
        }

        //Background 작업 완료
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }
    }

    private class GetMallDomain extends AsyncTask{
        private String returnDomain;
        private String DBName = "WPLUSSHOP.db";
        private String tableName = "USER_SETTING";

        @Override
        protected Object doInBackground(Object[] params) {
            DBManager dbManager = new DBManager(getApplicationContext(), DBName, null, 4);
            String[] selectParams = {"LOCATION_DOMAIN"};    // select columns
            HashMap<String, String> whereParams = new HashMap<String, String>();    // where 조건
            HashMap<String, String> userSetRst = null;
            try {
                userSetRst = dbManager.select(tableName, selectParams, whereParams);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (userSetRst.isEmpty())   returnDomain = "http://www.wpoint.co.kr";
            else                        returnDomain = userSetRst.get("LOCATION_DOMAIN").equals("") ? "http://www.wpoint.co.kr" : "http://"+ userSetRst.get("LOCATION_DOMAIN");

            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("returnDomain", returnDomain);
            msg.setData(bundle);
            msg.what = 100;
            handler.sendMessage(msg);

            return null;
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 100:
                    Bundle bundle = new Bundle();
                    bundle = msg.getData();

                    MallDomain = bundle.getString("returnDomain");
                    break;
            }
        }
    };
}
