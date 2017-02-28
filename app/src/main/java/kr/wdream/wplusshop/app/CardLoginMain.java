package kr.wdream.wplusshop.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import kr.wdream.wplusshop.R;
import kr.wdream.wplusshop.common.Metrics;
import kr.wdream.wplusshop.common.PxToDp;
import kr.wdream.wplusshop.common.util.CardLoginTask;
import kr.wdream.wplusshop.common.util.GetPointTask;

/**
 * Created by deobeuldeulim on 2017. 2. 28..
 */

public class CardLoginMain extends Activity implements View.OnClickListener{

    public static CardLoginMain cardLoginMain;

    //Properties
    private String cardNo = "";
    private String cardPw = "";
    private String cardPoint = "";

    private Metrics metrics;

    private String cardState = "";

    //UI
    private LinearLayout lytRoot;

    private RelativeLayout lytTopView;

    private RelativeLayout lytNavigationBar;
    private LinearLayout lytTitleBack;
    private TextView txtTitle;
    private ImageView btnLogout;

    private RelativeLayout lytInfoView;
    private Button btnLogin;
    private TextView txtPoint;
    private TextView txtCardNo;

    private TextView txtGuide;
    private TextView txtGuide2;

    private LinearLayout lytSave;
    private ImageButton btnSave;
    private TextView txtSave;

    private TextView txtTip;

    public static class Constants{
        // dp
        public static final int lytTopViewHeight = 400;

        public static final int lytNavigationBarHeight = 50;

        public static final int btnLogoutWidth  = 26;
        public static final int btnLogoutHeight = 26;
        public static final int btnLogoutRight  = 16;

        public static final int lytInfoLeft = 20;
        public static final int lytInfoRight = 20;
        public static final int lytinfoHeight = 180;

        public static final int btnLoginLeft = 10;
        public static final int btnLoginTop = 10;

        public static final int txtGuideLeft = 10;
        public static final int txtGuideTop = 10;
        public static final int txtGuideBottom = 10;
        public static final int txtGuideRight = 10;
        public static final int txtGuideBetween = 5;

        public static final int lytSaveHeight = 140;

        public static final int txtSaveLeft = 10;

        public static final int txtTipBackground = Color.parseColor("#F4F4F4");
    }

    public static class Font {
        public static final int txtTitleSize = 20;
        public static final int txtPointSize = 15;
        public static final int txtCardNoSize = 5;
        public static final int txtGuideSize  = 5;
        public static final int txtSaveSize   = 5;
        public static final int txtTipSize    = 5;

        public static final int btnLoginTextColor = Color.WHITE;
        public static final int txtGuideFontColor = Color.parseColor("#555555");
        public static final int txtTipFontColor = Color.parseColor("#32B6C0");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cardLoginMain = this;

        initView();

        setContentView(lytRoot);
    }

    private void initView(){
        lytRoot = new LinearLayout(this);
        lytRoot.setOrientation(LinearLayout.VERTICAL);
        lytRoot.setBackgroundColor(Color.WHITE);

        //lytTopView
        lytTopView = new RelativeLayout(this);
        lytTopView.setBackgroundResource(R.drawable.bg02);

        //NavigationBar Init
        lytNavigationBar = new RelativeLayout(this);

        lytTitleBack = new LinearLayout(this);
        RelativeLayout.LayoutParams paramNaviTitle = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramNaviTitle.addRule(RelativeLayout.CENTER_IN_PARENT);
        lytTitleBack.setLayoutParams(paramNaviTitle);

        txtTitle = new TextView(this);
        txtTitle.setText("WPLUS");
        txtTitle.setTextSize(Font.txtTitleSize);
        txtTitle.setTextColor(Color.WHITE);

        btnLogout = new ImageView(this);
        btnLogout.setImageResource(R.drawable.login_icon);
        btnLogout.setBackgroundColor(Color.TRANSPARENT);
        btnLogout.setScaleType(ImageView.ScaleType.FIT_XY);
        RelativeLayout.LayoutParams paramLogout = new RelativeLayout.LayoutParams(PxToDp.dpToPx(this, Constants.btnLogoutWidth), PxToDp.dpToPx(this, Constants.btnLogoutHeight));
        paramLogout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT );
        paramLogout.addRule(RelativeLayout.CENTER_VERTICAL);
        paramLogout.setMargins(0, 0, PxToDp.dpToPx(this, Constants.btnLogoutRight), 0);
        btnLogout.setLayoutParams(paramLogout);
        btnLogout.setOnClickListener(this);

        lytTitleBack.addView(txtTitle, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lytTitleBack.setBackgroundResource(R.drawable.roundcorner_stroke);

        lytNavigationBar.addView(lytTitleBack);
        lytNavigationBar.addView(btnLogout);

        //Point Init
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = PxToDp.pxToDp(this, dm.widthPixels);
        int height = dm.heightPixels;

        lytInfoView = new RelativeLayout(this);
        RelativeLayout.LayoutParams paramLytInfoView = new RelativeLayout.LayoutParams(PxToDp.dpToPx(this, width - Constants.lytInfoLeft - Constants.lytInfoRight), PxToDp.dpToPx(this, Constants.lytinfoHeight));
        paramLytInfoView.addRule(RelativeLayout.CENTER_IN_PARENT);
        lytInfoView.setLayoutParams(paramLytInfoView);

        btnLogin = new Button(this);
        RelativeLayout.LayoutParams paramBtnLogin = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramBtnLogin.setMargins(PxToDp.dpToPx(this, Constants.btnLoginLeft), PxToDp.dpToPx(this, Constants.btnLoginTop), 0, 0);
        btnLogin.setLayoutParams(paramBtnLogin);
        btnLogin.setText("아이디 로그인");
        btnLogin.setTextColor(Font.btnLoginTextColor);
        btnLogin.setBackgroundColor(Color.TRANSPARENT);
        btnLogin.setOnClickListener(this);

        txtPoint = new TextView(this);
        txtPoint.setTextSize(PxToDp.dpToPx(this, Font.txtPointSize));
        txtPoint.setTextColor(Color.WHITE);
        txtPoint.setId(R.id.txtPoint);
        RelativeLayout.LayoutParams paramPoint = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramPoint.addRule(RelativeLayout.CENTER_IN_PARENT);
        txtPoint.setLayoutParams(paramPoint);
        txtPoint.setGravity(Gravity.CENTER);

        txtCardNo = new TextView(this);
        txtCardNo.setTextSize(PxToDp.dpToPx(this, Font.txtCardNoSize));
        txtCardNo.setTextColor(Color.WHITE);
        RelativeLayout.LayoutParams paramCardNo = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramCardNo.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        txtCardNo.setLayoutParams(paramCardNo);
        txtCardNo.setGravity(Gravity.RIGHT);

        lytInfoView.addView(btnLogin);
        lytInfoView.addView(txtPoint);
        lytInfoView.addView(txtCardNo);
        lytInfoView.setBackgroundResource(R.drawable.roundcorner_gray);

        lytTopView.addView(lytNavigationBar, ViewGroup.LayoutParams.MATCH_PARENT, PxToDp.dpToPx(this, Constants.lytNavigationBarHeight));
        lytTopView.addView(lytInfoView);

        txtGuide = new TextView(this);
        LinearLayout.LayoutParams paramTxtGuide = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramTxtGuide.setMargins(
                PxToDp.dpToPx(this, Constants.txtGuideLeft),
                PxToDp.dpToPx(this, Constants.txtGuideTop),
                PxToDp.dpToPx(this, Constants.txtGuideRight),
                PxToDp.dpToPx(this, Constants.txtGuideBetween));
        txtGuide.setLayoutParams(paramTxtGuide);
        txtGuide.setText("카드로 로그인 하실 경우");
        txtGuide.setTextSize(PxToDp.dpToPx(this, Font.txtGuideSize));
        txtGuide.setTextColor(Font.txtGuideFontColor);

        txtGuide2 = new TextView(this);
        LinearLayout.LayoutParams paramTxtGuide2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramTxtGuide2.setMargins(
                PxToDp.dpToPx(this, Constants.txtGuideLeft),
                0,
                PxToDp.dpToPx(this, Constants.txtGuideRight),
                PxToDp.dpToPx(this, Constants.txtGuideBottom));
        txtGuide2.setLayoutParams(paramTxtGuide2);
        txtGuide2.setText("포인트 적립만 가능합니다.");
        txtGuide2.setTextSize(PxToDp.dpToPx(this, Font.txtGuideSize));
        txtGuide2.setTextColor(Font.txtGuideFontColor);

        lytSave = new LinearLayout(this);
        lytSave.setOrientation(LinearLayout.HORIZONTAL);
        lytSave.setBackgroundColor(Color.parseColor("#F0F0F0"));
        lytSave.setGravity(Gravity.CENTER_VERTICAL);
        lytSave.setOnClickListener(this);

        btnSave = new ImageButton(this);
        LinearLayout.LayoutParams paramBtnSave = new LinearLayout.LayoutParams(PxToDp.dpToPx(this, 90), PxToDp.dpToPx(this, 90));
        btnSave.setLayoutParams(paramBtnSave);
        btnSave.setImageResource(R.drawable.tab01_02);
        btnSave.setScaleType(ImageView.ScaleType.FIT_XY);
        btnSave.setBackgroundColor(Color.TRANSPARENT);

        txtSave = new TextView(this);
        LinearLayout.LayoutParams paramTxtSave = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramTxtSave.setMargins(PxToDp.dpToPx(this, Constants.txtSaveLeft), 0, 0, 0);
        txtSave.setText("포인트 적립하기");
        txtSave.setTextSize(PxToDp.dpToPx(this, Font.txtSaveSize));
        txtSave.setTextColor(Color.BLACK);

        lytSave.addView(btnSave);
        lytSave.addView(txtSave);

        txtTip = new TextView(this);
        txtTip.setText("블루투스 켜고 근처에서 더 많은 포인트 적립을 받으세요!");
        txtTip.setTextSize(PxToDp.dpToPx(this, Font.txtTipSize));
        txtTip.setTextColor(Font.txtTipFontColor);
        txtTip.setBackgroundColor(Constants.txtTipBackground);
        txtTip.setGravity(Gravity.CENTER);

        //Add CreatedView to RootView
        lytRoot.addView(lytTopView, ViewGroup.LayoutParams.MATCH_PARENT, PxToDp.dpToPx(this, Constants.lytTopViewHeight));
        lytRoot.addView(txtGuide);
        lytRoot.addView(txtGuide2);
        lytRoot.addView(lytSave, ViewGroup.LayoutParams.MATCH_PARENT, PxToDp.dpToPx(this, Constants.lytSaveHeight));
        lytRoot.addView(txtTip, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            HashMap<String,String> resultPoint = (HashMap<String,String>) bundle.getSerializable("resultPoint");
            switch (msg.what){
                case Metrics.CARD_POINT_SUCCESS:

                    cardPoint = resultPoint.get("CP");

                    if(cardPoint.length() == 0 )
                        cardPoint = "0";

                    txtPoint.setText(cardPoint + "CP");
                    break;

                case Metrics.CARD_POINT_FAILED:
                    Log.d("상은", "카드 정보 가져오지 못함");
                    txtPoint.setText("승인 대기");
                    break;

            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        metrics = new Metrics(CardLoginMain.this);

        cardState = metrics.getCardState();
        Log.d("상은", "cardState : " + metrics.getCardState());

        cardNo = metrics.getCardLoginNo();
        cardPw = metrics.getCardLoginPw();

        txtCardNo.setText(cardNo);

        new GetPointTask(cardNo, handler).execute();
    }

    @Override
    public void onClick(View v) {
        if (v == btnLogout) {
            metrics.clearLoginData();
            Intent intent = new Intent(CardLoginMain.this, Main.class);
            startActivity(intent);
            finish();
        }

        if (v == btnLogin) {
            Intent intent = new Intent(CardLoginMain.this, Login.class);
            intent.putExtra("FROM", false);
            startActivity(intent);
        }

        if (v == lytSave) {
            if ("C".equals(cardState)) {
                Intent intent = new Intent(CardLoginMain.this, PayActivity.class);
                Log.d("상은", "cardNo : " + cardNo);
                Log.d("상은", "cardPw : " + cardPw);
                Log.d("상은", "cardPoint : " + cardPoint);
                intent.putExtra("STATE", PayActivity.STATE_CARD_SAVE);
                intent.putExtra("cardNo", cardNo);
                intent.putExtra("cardPw", cardPw);
                intent.putExtra("point", cardPoint);
                startActivity(intent);
            } else if ("A".equals(cardState)){
                AlertDialog.Builder dialog = new AlertDialog.Builder(CardLoginMain.this);
                dialog.setTitle("발급 대기");
                dialog.setMessage("승인 대기 중인 카드입니다.\n고객센터에 문의해주세요.");
                dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        metrics.clearLoginData();
    }
}
