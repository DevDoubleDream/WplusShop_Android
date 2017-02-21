package kr.wdream.wplusshop.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import kr.wdream.wplusshop.R;
import kr.wdream.wplusshop.common.util.Util;
import lee.whdghks913.WebViewAllCapture.WebViewAllCapture;

/**
 * Created by SEO on 2015-12-18.
 */
public class PushWebView extends Activity implements View.OnClickListener, Animation.AnimationListener {
    Boolean STOP = false;

    Boolean result;         // 캡쳐버튼 더블 클릭 방지
    Boolean flag = true;
    LinearLayout capture; // Button Disable을 위한 선언

    Intent intent;
    String ssoURL = "";

    static boolean wMenuCheck = true;
    static boolean menuCheck = true;

    String locationURL = "";
    String title = "";
    String summary = "";
    String picture = "";
    String mFilePath;
    String mScreenShotName;

    HashMap<String, String> bandParams = new HashMap<String, String>();
    HashMap<String, String> kakaoParams = new HashMap<String, String>();

    WebView webView = null;
    String nUrl = "";
    boolean pageState = true;
    String kakaoText = "";
    String bandText = "";

    static Context mMain;
    private static final String INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";

    static String targetURL = "";
    static URL homeURL = null;
    static String homeDomain = "";

    public ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);

        progress = (ProgressBar) findViewById(R.id.web_progress);

        mMain = this;
        startAnimation();
        capture  = (LinearLayout)findViewById(R.id.navi_menu_01);

        Intent intent = getIntent();
        try {
            locationURL = intent.getExtras().getString("LOCATION_URL");
        }catch (Exception e) {
        }
        try {
            title       = intent.getExtras().getString("TITLE");
        }catch (Exception e) {
        }
        try {
            summary     = intent.getExtras().getString("SUMMARY");
        }catch (Exception e) {
        }
        try {
            picture     = intent.getExtras().getString("PICTURE");
        }catch (Exception e) {
        }
        targetURL = locationURL;
        Log.d("MyGcmListenerService", "locationURL : " + locationURL);
//        String url = "http://www.naver.com";
        webView = (WebView) findViewById(R.id.webView);
        WebSettings set = webView.getSettings();
        set.setLoadWithOverviewMode(true);
        set.setUseWideViewPort(true);
        set.setBuiltInZoomControls(true);
        set.setSupportZoom(true);
        set.setDisplayZoomControls(false);
        set.setJavaScriptEnabled(true);
//        set.setSupportMultipleWindows(true);
//
//        webView.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg) {
//                WebView newWebView = new WebView(PushWebView.this);
//
//                WebView.WebViewTransport transport = (WebView.WebViewTransport)resultMsg.obj;
//                transport.setWebView(newWebView);
//                resultMsg.sendToTarget();
//
//                return true;
//            }
//        });

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                Log.d("test_wplus", "shouldOverrideUrlLoading : " + nUrl);
                startAnimation();
                pageState = false;
                return true;
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progress.setVisibility(View.VISIBLE);
                Log.d("test_wplus", "onPageStarted");
                startAnimation();
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                Log.d("test_wplus", "onLoadResource");
                nUrl = url;

            }

            public void onPageFinished(WebView view, String url) {
                locationURL = url;
                Log.d("test_wplus", "onPageFinished : " + nUrl);
                pageState = true;
                stopAnimation();
                targetURL = webView.getUrl();
                try {
                    homeURL = new URL(targetURL);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                homeDomain = homeURL.getAuthority();
                progress.setVisibility(View.GONE);
            }

        });
        webView.loadUrl(locationURL);
        Main.locationHistory.add(this);

        }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.icon_head_left:
                intent = new Intent(this, Main.class);
                startActivity(intent);
                finish();
                break;
            case R.id.icon_head_right:
                if (!pageState)
                    Toast.makeText(this, "페이지 로딩 중입니다.", Toast.LENGTH_SHORT).show();
                else {
                    kakaoText = "";
                    // 카카오 parameter
//                    kakaoText = title;
//                    if(!summary.equals("")) kakaoText = "\n\n"+ summary;
//                    if(!locationURL.equals("")) kakaoText += "\n"+ locationURL;
                    kakaoText = locationURL;
                    kakaoParams.put("TEXT", kakaoText);
//                    kakaoParams.put("PICTURE", picture);
                    kakaoParams.put("PICTURE", "");

//                    Util.goKakao(this, kakaoParams);
                }
                break;

            case R.id.icon_head_right2:
                // 네이버 밴드 parameter
                bandText = "";
                bandParams.put("SUBJECT", title);
                if (!summary.equals("")) bandText = "\n" + summary;
                if (!locationURL.equals("")) bandText += "\n\n" + locationURL;
                bandParams.put("TEXT", bandText);

                Util.goBand(this, bandParams);
                break;

        }
    }
    public void btnClick(View v) {
        switch (v.getId()) {
            case R.id.img_btn_wv_home: // 홈으로
                setMenuView("", false);
                webView.loadUrl("http://"+ homeDomain);
                break;

            case R.id.img_btn_wv_prev: // 이전으로
                setMenuView("", false);
                webView.goBack();
                break;

            case R.id.img_btn_wv_next: // 다음으로
                setMenuView("", false);
                webView.goForward();
                break;

            case R.id.img_btn_wv_wmenu: // W 메뉴
                setMenuView("WMENU", wMenuCheck);
                break;

            case R.id.img_btn_wv_refresh: // 새로고침
                setMenuView("", false);
                startAnimation();
                webView.reload();
                break;

            case R.id.img_btn_wv_menu: // 메뉴
                setMenuView("MENU", menuCheck);
                break;

            case R.id.img_btn_wv_exit: // 나가기
                if(Main.locationHistory.size() < 2) {
                    intent = new Intent(this, Main.class);
                    startActivity(intent);
                }
                finish();
                break;

            case R.id.lyt_menu:
                setMenuView("", false);
                break;

            // W 메뉴
            case R.id.navi_wmenu_01: // 화면 캡쳐
                setMenuView("", false);
                if(flag) {
                    takePicture();
                }else{
                    capture.setClickable(false);
                }
                capture.setEnabled(false);
                Handler handler = new Handler();
                handler.postDelayed(new splashhandler(),2000);
                //Toast.makeText(this, "화면 캡쳐", Toast.LENGTH_SHORT).show();
                break;

            case R.id.navi_wmenu_02: // url 복사
                setMenuView("", false);
                String URL = null;
                URL = webView.getUrl();
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setText(URL);
                Toast.makeText(this, "url 복사되었습니다.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.navi_wmenu_03: // 홈 화면 추가
                setMenuView("", false);
                final String[] name = new String[1];
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(dialogView);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        EditText edt_dlg_name = (EditText) dialogView.findViewById(R.id.edt_dlg_url);
                        String name = edt_dlg_name.getText().toString();
                        addShortcut(mMain, name, targetURL);
                    }
                });

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
//                addShortcut(this);
                break;

            case R.id.navi_wmenu_04: // 다른 브라우저
                setMenuView("", false);
                Intent i = new Intent(Intent.ACTION_VIEW);
                Uri u = Uri.parse(targetURL);
                i.setData(u);
                startActivity(i);
                //Toast.makeText(this, "다른 브라우저", Toast.LENGTH_SHORT).show();
                break;

            // 메뉴
            case R.id.navi_menu_01: // 가맹점
                setMenuView("", false);
                intent = new Intent(this, Use.class);
                intent.putExtra("menuId", "0");
                startActivity(intent);
//                Toast.makeText(this, "가맹점", Toast.LENGTH_SHORT).show();
                break;


            case R.id.navi_menu_02: // 결제하기
                setMenuView("", false);
                if (Main.userInfo.get("user_no").equals("")) {
                    intent = new Intent(this, Login.class);
                    intent.putExtra("locationActivity", "Use");
                } else
                    intent = new Intent(this, Use.class);
                intent.putExtra("menuId", "1");
                startActivity(intent);
//                Toast.makeText(this, "결제하기", Toast.LENGTH_SHORT).show();
                break;


            case R.id.navi_menu_03: // 선물하기
                setMenuView("", false);
                if (Main.userInfo.get("user_no").equals("")) {
                    intent = new Intent(this, Login.class);
                    intent.putExtra("locationActivity", "Use");
                } else
                    intent = new Intent(this, Use.class);

                intent.putExtra("menuId", "2");
                startActivity(intent);
//                Toast.makeText(this, "선물하기", Toast.LENGTH_SHORT).show();
                break;

            case R.id.navi_menu_04: // 기부하기
                setMenuView("", false);
                if (Main.userInfo.get("user_no").equals("")) {
                    intent = new Intent(this, Login.class);
                    intent.putExtra("locationActivity", "Use");
                } else
                    intent = new Intent(this, Use.class);

                intent.putExtra("menuId", "3");
                startActivity(intent);
//                Toast.makeText(this, "기부하기", Toast.LENGTH_SHORT).show();
                break;

            case R.id.navi_menu_05: // 투데이딜
                setMenuView("", false);
                intent = new Intent(this, TodayDeal.class);
                startActivity(intent);
//                Toast.makeText(this, "투데이딜", Toast.LENGTH_SHORT).show();
                break;

            case R.id.navi_menu_06: // W 마트
                setMenuView("", false);
                ssoURL = "http://www.wpoint.co.kr";
                intent = new Intent(Intent.ACTION_VIEW,Uri.parse(ssoURL));
                startActivity(intent);
//                startActivity(intent);
//                Toast.makeText(this, "W 마트", Toast.LENGTH_SHORT).show();
                break;

            case R.id.navi_menu_07: // 비지니스 센터
                setMenuView("", false);
                if (Main.userInfo.get("user_id").toString().equals("")) {
                    ssoURL = "http://viz.wincash.co.kr";
                } else {
                    ssoURL = "http://api.wincash.co.kr/login/loginprocess.asp?cmd=sso&user_id=" + Main.userInfo.get("user_id") + "&user_pw=" + Main.userInfo.get("user_pw") + "&user_grade=" + Main.userInfo.get("user_grade") + "&user_dmn=viz.wincash.co.kr";
                }
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ssoURL));
                startActivity(intent);
//                Toast.makeText(this, "비지니스 센터", Toast.LENGTH_SHORT).show();
                break;

            case R.id.navi_menu_08: // 더블비젼
                setMenuView("", false);
                if (Main.userInfo.get("user_id").toString().equals("")) {
                    ssoURL = "http://www.wvision.co.kr";
                } else {
                    ssoURL = "http://api.wincash.co.kr/login/loginprocess.asp?cmd=sso&user_id=" + Main.userInfo.get("user_id") + "&user_pw=" + Main.userInfo.get("user_pw") + "&user_grade=" + Main.userInfo.get("user_grade") + "&user_dmn=m.wvision.co.kr";
                }
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ssoURL));
                startActivity(intent);
//                Toast.makeText(this, "더블비젼", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public void takePicture(){
        flag=false;
        mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/Pictures/Wplus/";
        mScreenShotName = "Wplus_" + System.currentTimeMillis() + ".jpeg";
        WebViewAllCapture mAllCapture = new WebViewAllCapture();
        result = mAllCapture.onWebViewAllCapture(webView, mFilePath, mScreenShotName);
        if (result) {
       //     Toast.makeText(this, "저장 되었습니다. (" + mFilePath
         //           + mScreenShotName + ")", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("화면 캡쳐 저장이 완료되었습니다.");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    flag=true;
                }
            });
            AlertDialog dlg = builder.create();
            dlg.show();
        }
        refresh();

    }

    public void refresh(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mFilePath + mScreenShotName);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    public class splashhandler implements Runnable{
        public void run(){
            capture.setEnabled(true);
        }
    }
    public void setMenuView(String menuStr, boolean menuState){
        if(menuState){
            ((ImageView) findViewById(R.id.img_arrow_1)).setVisibility(View.GONE);
            ((ImageView) findViewById(R.id.img_arrow_2)).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.lyt_navi_wmenu)).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.lyt_navi_menu)).setVisibility(View.GONE);

            ((LinearLayout) findViewById(R.id.lyt_menu)).setVisibility(View.VISIBLE);
            if(menuStr.equals("WMENU")) {
                ((LinearLayout) findViewById(R.id.lyt_navi_wmenu)).setVisibility(View.VISIBLE);
                ((ImageView) findViewById(R.id.img_arrow_1)).setVisibility(View.VISIBLE);
                wMenuCheck = wMenuCheck ? false : true;
                menuCheck = true;
            }else {
                ((LinearLayout) findViewById(R.id.lyt_navi_menu)).setVisibility(View.VISIBLE);
                ((ImageView) findViewById(R.id.img_arrow_2)).setVisibility(View.VISIBLE);
                menuCheck = menuCheck ? false : true;
                wMenuCheck = true;
            }
        }
        else{
            ((LinearLayout) findViewById(R.id.lyt_menu)).setVisibility(View.GONE);
            wMenuCheck  = true;
            menuCheck   = true;
        }
    }

    public void startAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate_refresh);
        animation.setFillAfter(true);
//        animation.setAnimationListener(this);
        ImageView animatedView = (ImageView) findViewById(R.id.img_wv_refresh);
        animatedView.startAnimation(animation);
    }

    public void stopAnimation(){
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.rotate_refresh);
        animation.setFillAfter(true);
//        animation.setAnimationListener(this);
        ImageView animatedView = (ImageView) findViewById(R.id.img_wv_refresh);
        animatedView.clearAnimation();
        STOP = true;
    }

    private void addShortcut(Context context, String title, String uri) {
        Intent shortcutIntent = new Intent(mMain,Intro.class);
        shortcutIntent.putExtra("locationURI", uri);
        shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//        shortcutIntent.setClassName(context, getClass().getName());
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Wplus :: "+ title);
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(context,
                        R.drawable.icon_webview_01));
        intent.putExtra("duplicate", false);
        intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");

        sendBroadcast(intent);

    }
}
