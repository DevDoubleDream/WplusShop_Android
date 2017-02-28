package kr.wdream.wplusshop.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.HashMap;

import kr.wdream.wplusshop.R;
import kr.wdream.wplusshop.app.dialog.ChoiceDialog;
import kr.wdream.wplusshop.common.LoginUtil;
import kr.wdream.wplusshop.common.PointSendUtil;
import kr.wdream.wplusshop.common.StoreUtil;

/**
 * Created by SEO on 2015-12-14.
 */
public class Use extends Activity implements View.OnClickListener{
    public static String menuId;
    public Intent intent;
    public static String card_no = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gift);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        ((LinearLayout) findViewById(R.id.icon_head_left)).setOnClickListener(this);


        intent = getIntent();
        if(intent.getExtras().getString("menuId") != null) menuId = intent.getExtras().getString("menuId");
        setMenuLayout(menuId);

        Main.locationHistory.add(this);
    }

    public void setMenuLayout(String rtnMenuId){
        menuId = rtnMenuId;
        ((LinearLayout) findViewById(R.id.lyt_gift_content)).setVisibility(View.GONE);
        ((LinearLayout) findViewById(R.id.lyt_payment_content)).setVisibility(View.GONE);
        ((LinearLayout) findViewById(R.id.lyt_store_content)).setVisibility(View.GONE);

        ((LinearLayout) findViewById(R.id.lyt_menubar_01)).setVisibility(View.INVISIBLE);
        ((LinearLayout) findViewById(R.id.lyt_menubar_02)).setVisibility(View.INVISIBLE);
        ((LinearLayout) findViewById(R.id.lyt_menubar_03)).setVisibility(View.INVISIBLE);
        ((LinearLayout) findViewById(R.id.lyt_menubar_04)).setVisibility(View.INVISIBLE);

        ((TextView) findViewById(R.id.txt_menubar_01)).setTextColor(Color.parseColor("#949494"));
        ((TextView) findViewById(R.id.txt_menubar_02)).setTextColor(Color.parseColor("#949494"));
        ((TextView) findViewById(R.id.txt_menubar_03)).setTextColor(Color.parseColor("#949494"));
        ((TextView) findViewById(R.id.txt_menubar_04)).setTextColor(Color.parseColor("#949494"));

        ((EditText) findViewById(R.id.txt_cardno_target)).setText("");
        ((EditText) findViewById(R.id.txt_point)).setText("");
        switch (rtnMenuId){
            case "0":
                ((LinearLayout) findViewById(R.id.lyt_store_content)).setVisibility(View.VISIBLE);

                ((TextView) findViewById(R.id.txt_menubar_01)).setTextColor(Color.parseColor("#22bbc0"));
                ((LinearLayout) findViewById(R.id.lyt_menubar_01)).setBackgroundColor(Color.rgb(34, 187, 192));
                ((LinearLayout) findViewById(R.id.lyt_menubar_01)).setVisibility(View.VISIBLE);
                ((LinearLayout) findViewById(R.id.bg_menu)).setBackgroundResource(R.drawable.bg_pre);

                ((ImageView) findViewById(R.id.icon_title)).setImageResource(R.drawable.franchise_icon);
                ((TextView) findViewById(R.id.txt_title)).setText("가맹점");
                ((TextView) findViewById(R.id.txt_comment)).setText("W+ 의 가맹점을 확인할 수 있습니다");
                setStoreData();
                break;
            case "1":
                ((LinearLayout) findViewById(R.id.lyt_payment_content)).setVisibility(View.VISIBLE);

                ((TextView) findViewById(R.id.txt_menubar_02)).setTextColor(Color.parseColor("#f34950"));
                ((LinearLayout) findViewById(R.id.lyt_menubar_02)).setBackgroundColor(Color.rgb(243, 73, 80));
                ((LinearLayout) findViewById(R.id.lyt_menubar_02)).setVisibility(View.VISIBLE);
                ((LinearLayout) findViewById(R.id.bg_menu)).setBackgroundResource(R.drawable.bg_dona);

                ((ImageView) findViewById(R.id.icon_title)).setImageResource(R.drawable.pay_icon);
                ((TextView) findViewById(R.id.txt_title)).setText("결제하기");
                ((TextView) findViewById(R.id.txt_comment)).setText("W+ 의 가맹점을 확인할 수 있습니다");

                StringBuffer sb = new StringBuffer(Main.userInfo.get("card_no"));
                sb.insert(4, "-").insert(9, "-").insert(14, "-");
                String card_no = sb.toString();
                ((TextView) findViewById(R.id.txt_cardno)).setText(card_no);

                break;
            case "2":
                ((LinearLayout) findViewById(R.id.lyt_gift_content)).setVisibility(View.VISIBLE);

                ((TextView) findViewById(R.id.txt_menubar_03)).setTextColor(Color.parseColor("#525abe"));
                ((LinearLayout) findViewById(R.id.lyt_menubar_03)).setBackgroundColor(Color.rgb(82, 90, 190));
                ((LinearLayout) findViewById(R.id.lyt_menubar_03)).setVisibility(View.VISIBLE);
                ((LinearLayout) findViewById(R.id.bg_menu)).setBackgroundResource(R.drawable.bg_pre);

                ((ImageView) findViewById(R.id.icon_title)).setImageResource(R.drawable.present_icon);
                ((TextView) findViewById(R.id.txt_title)).setText("선물하기");
                ((TextView) findViewById(R.id.txt_comment)).setText("자신의 포인트를 선물 할 수 있습니다");

                ((ImageView) findViewById(R.id.icon_pre)).setImageResource(R.drawable.point_icon_pre);
                ((TextView) findViewById(R.id.txt_pre)).setText("선물할 포인트");
                ((TextView) findViewById(R.id.txt_pre)).setTextColor(Color.parseColor("#525abe"));

                ((ImageView) findViewById(R.id.icon_getter)).setImageResource(R.drawable.person_icon_pre);
                ((TextView) findViewById(R.id.txt_getter)).setTextColor(Color.parseColor("#525abe"));

                ((ImageView) findViewById(R.id.img_btn_pre)).setImageResource(R.drawable.send_icon_pre);
                ((TextView) findViewById(R.id.txt_btn_pre)).setTextColor(Color.parseColor("#525abe"));

                ((TextView) findViewById(R.id.txt_send_pw)).setText("선물하기");
                break;
            case "3":
                ((LinearLayout) findViewById(R.id.lyt_gift_content)).setVisibility(View.VISIBLE);

                ((TextView) findViewById(R.id.txt_menubar_04)).setTextColor(Color.parseColor("#08916e"));
                ((LinearLayout) findViewById(R.id.lyt_menubar_04)).setBackgroundColor(Color.rgb(8, 145, 110));
                ((LinearLayout) findViewById(R.id.lyt_menubar_04)).setVisibility(View.VISIBLE);
                ((LinearLayout) findViewById(R.id.bg_menu)).setBackgroundResource(R.drawable.bg_dona);

                ((ImageView) findViewById(R.id.icon_title)).setImageResource(R.drawable.donation_icon);
                ((TextView) findViewById(R.id.txt_title)).setText("기부하기");
                ((TextView) findViewById(R.id.txt_comment)).setText("자신의 포인트를 기부 할 수 있습니다");

                ((ImageView) findViewById(R.id.icon_pre)).setImageResource(R.drawable.point_icon_dona);
                ((TextView) findViewById(R.id.txt_pre)).setText("기부할 포인트");
                ((TextView) findViewById(R.id.txt_pre)).setTextColor(Color.parseColor("#08916e"));

                ((ImageView) findViewById(R.id.icon_getter)).setImageResource(R.drawable.person_icon_dona);
                ((TextView) findViewById(R.id.txt_getter)).setTextColor(Color.parseColor("#08916e"));

                ((ImageView) findViewById(R.id.img_btn_pre)).setImageResource(R.drawable.send_icon_dona);
                ((TextView) findViewById(R.id.txt_btn_pre)).setTextColor(Color.parseColor("#08916e"));

                ((TextView) findViewById(R.id.txt_send_pw)).setText("기부하기");
                break;
        }

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.icon_head_left:
                finish();
                break;
        }
    }

    public void checkPoint(EditText cardno, EditText point, boolean proc_type){
        int snedPoint = 0;
        int userPoint = 0;
        if(!point.getText().toString().equals("")) snedPoint = Integer.parseInt(point.getText().toString());

        if(snedPoint < 100){
            Toast.makeText(this, "포인트를 100 point 이상 입력해주세요.", Toast.LENGTH_SHORT).show();
            point.requestFocus();
        }
        else if(cardno.getText().toString().length() < 16){
            Toast.makeText(this, "카드번호를 정확히 입력해 주세요.", Toast.LENGTH_SHORT).show();
            cardno.requestFocus();
        }
        else {
            LoginUtil.inIt(this);
            HashMap<String, String> loginParams = new HashMap<String, String>();

            String login_id = Main.userInfo.get("user_id");
            String login_pw = Main.userInfo.get("user_pw");

            loginParams.put("cmd", "login");
            loginParams.put("user_dmn", "app.wincash.co.kr");
            loginParams.put("user_grade", "M");
            loginParams.put("user_id", login_id);
            loginParams.put("user_pw", login_pw);

            HashMap<String, String> loginResult = LoginUtil.getLoginResult(loginParams);
//
            String code     = loginResult.get("code");
            String message  = loginResult.get("message");

            if (code.equals("0000"))    userPoint = Integer.parseInt(loginResult.get("user_cpoint"));

            if(userPoint < 20000)           Toast.makeText(this, "20,000 포인트 이상 적립하셔야 이용 가능합니다.", Toast.LENGTH_SHORT).show();
            else if(snedPoint > userPoint)  Toast.makeText(this, "포인트가 부족합니다.", Toast.LENGTH_SHORT).show();
            else {
                if(proc_type) {
                    ((LinearLayout) findViewById(R.id.lyt_pw)).setVisibility(View.VISIBLE);
                    ((EditText) findViewById(R.id.txt_member_cardpw)).setText("");
                }
            }
        }
    }
    public void btnClick(View v) {
        String point;
        EditText txt_cardno_target = (EditText) findViewById(R.id.txt_cardno_target);
        EditText txt_point = (EditText) findViewById(R.id.txt_point);
        switch (v.getId()) {
            case R.id.lyt_ok:
                checkPoint(txt_cardno_target, txt_point, true);
                break;
            case R.id.button_txt_cancel:
                lyt_hidden();
                break;
            case R.id.button_txt_ok:
                Log.d("wplus_test", "menuId : " + menuId);
                checkPoint(txt_cardno_target, txt_point, false);
                EditText txt_member_cardpw = (EditText)findViewById(R.id.txt_member_cardpw);
//                Log.d("dev_test", "txt_member_cardpw : " + txt_member_cardpw.getText().toString());
//                Log.d("dev_test", "txt_member_cardpw : " + ((EditText) findViewById(R.id.txt_member_cardpw)).getText().toString());
                if(txt_member_cardpw.getText().toString().length() < 4){
                    Toast.makeText(this, "카드 비밀번호를 정확히 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    txt_member_cardpw.requestFocus();
                }else {

                    Log.d("dev_test", "button_txt_ok");
                    checkPoint(txt_cardno_target, txt_point, false);
                    PointSendUtil.inIt(this);
                    HashMap<String, String> pointSendParams = new HashMap<String, String>();
                    pointSendParams.put("cmd", "payment");
                    if(menuId.equals("2")) {
                        pointSendParams.put("mode", "gift");
                        pointSendParams.put("pointmsg", "포인트선물");
                        pointSendParams.put("trsubgbncd", "KD");
                        pointSendParams.put("trsubgbnnm", "포인트선물");
                    }
                    else {
                        pointSendParams.put("mode", "donate");
                        pointSendParams.put("pointmsg", "포인트기부");
                        pointSendParams.put("trsubgbncd", "KE");
                        pointSendParams.put("trsubgbnnm", "포인트기부");
                    }
                    pointSendParams.put("member_no", Main.userInfo.get("user_no"));
                    pointSendParams.put("member_cardno", Main.userInfo.get("card_no"));
//                    pointSendParams.put("member_no", "26273");
//                    pointSendParams.put("member_cardno", "1599171820044748");

                    pointSendParams.put("member_cardpw", ((EditText) findViewById(R.id.txt_member_cardpw)).getText().toString());
                    //                pointSendParams.put("member_dmn", Main.userInfo.get("user_dmn"));
                    pointSendParams.put("member_dmn", "app.wincash.co.kr");
                    pointSendParams.put("point", ((EditText) findViewById(R.id.txt_point)).getText().toString());

                    pointSendParams.put("trgbncd", "K");
                    pointSendParams.put("trgbnnm", "유료컨텐츠");

                    pointSendParams.put("cardno_target", ((EditText) findViewById(R.id.txt_cardno_target)).getText().toString());


                    HashMap<String, String> pointSendResult = PointSendUtil.putPointSendResult(pointSendParams);

                    String code = pointSendResult.get("code");
                    String message = pointSendResult.get("message");

                    Log.d("dev_test", "code : " + code);
                    Log.d("dev_test", "message : " + message);

                    if (code.equals("0000")) {
                        //                    Toast.makeText(this, "정상적으로 처리 되었습니다.", Toast.LENGTH_SHORT).show();
                        lyt_hidden();
                        ((EditText) findViewById(R.id.txt_cardno_target)).setText("");
                        ((EditText) findViewById(R.id.txt_point)).setText("");
                        ((LinearLayout) findViewById(R.id.lyt_success)).setVisibility(View.VISIBLE);
                        ((ImageView) findViewById(R.id.icon_result)).setImageResource(R.drawable.check_icon_big);
                        ((TextView) findViewById(R.id.txt_result)).setText("정상 처리되었습니다.");
                    } else {
                        lyt_hidden();
                        ((LinearLayout) findViewById(R.id.lyt_success)).setVisibility(View.VISIBLE);
                        ((ImageView) findViewById(R.id.icon_result)).setImageResource(R.drawable.check_icon_big);
                        ((TextView) findViewById(R.id.txt_result)).setText(message);
                    }
                }
                break;
//            case R.id.button_txt_cancel2:
            case R.id.button_txt_ok2:
            case R.id.lyt_success:
                ((LinearLayout)findViewById(R.id.lyt_success)).setVisibility(View.GONE);
                break;
            case R.id.btn_price_100:
                point = txt_point.getText().toString().equals("") ? "100" : Integer.toString(Integer.parseInt(String.valueOf(txt_point.getText())) + 100);
                ((EditText) findViewById(R.id.txt_point)).setText(point);
                break;
            case R.id.btn_price_1000:
                point = txt_point.getText().toString().equals("") ? "1000" : Integer.toString(Integer.parseInt(String.valueOf(txt_point.getText())) + 1000);
                ((EditText) findViewById(R.id.txt_point)).setText(point);
                break;
            case R.id.btn_price_10000:
                point = txt_point.getText().toString().equals("") ? "10000" : Integer.toString(Integer.parseInt(String.valueOf(txt_point.getText())) + 10000);
                ((EditText) findViewById(R.id.txt_point)).setText(point);
                break;
            case R.id.lyt_store:
                setMenuLayout("0");
                break;
            case R.id.lyt_payment:
                if(Main.userInfo.get("user_no").equals("")){
                    intent = new Intent(this, Login.class);
                    startActivity(intent);
                }
                else
                    setMenuLayout("1");
                break;
            case R.id.lyt_gift:
                if(Main.userInfo.get("user_no").equals("")){
                    intent = new Intent(this, Login.class);
                    startActivity(intent);
                }
                else
                    setMenuLayout("2");
                break;
            case R.id.lyt_donation:
                if(Main.userInfo.get("user_no").equals("")){
                    intent = new Intent(this, Login.class);
                    startActivity(intent);
                }
                else
                    setMenuLayout("3");
                break;
            case R.id.lyt_payment_ok:
                ChoiceDialog dialog = new ChoiceDialog(Use.this, Main.userInfo.get("user_cpoint"));
                dialog.show();
                break;

            case R.id.lyt_coupon_ok:
                Toast.makeText(this, "쿠폰서비스는 준비중입니다", Toast.LENGTH_SHORT).show();
//                Intent couponIntent = new Intent(Use.this, CouponActivity.class);
//                startActivity(couponIntent);

                break;
            case R.id.btn_user_regist:
                intent = new Intent(this, UserRegist.class);
                startActivity(intent);
                break;
            case R.id.btn_user_list:
                intent = new Intent(this, UserList.class);
                startActivity(intent);
                break;

        }
    }

    public void lyt_hidden(){
        ((LinearLayout)findViewById(R.id.lyt_pw)).setVisibility(View.GONE);
        EditText editText = (EditText) findViewById(R.id.txt_member_cardpw);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public void setStoreData(){
        DecimalFormat df = new DecimalFormat("#,##0");
        HashMap<String, String> storeParams = new HashMap<String, String>();
        storeParams.put("cmd", "region");
        storeParams.put("page", "1");
        storeParams.put("pagesize", "10");
        HashMap<String, String> storeResult = StoreUtil.getStoreResult(storeParams);

        ((TextView)findViewById(R.id.txt_서울)).setText(storeResult.get("서울"));
        ((TextView)findViewById(R.id.txt_강원)).setText(storeResult.get("강원"));
        ((TextView)findViewById(R.id.txt_경북)).setText(storeResult.get("경북"));
        ((TextView)findViewById(R.id.txt_경남)).setText(storeResult.get("경남"));
        ((TextView)findViewById(R.id.txt_대구)).setText(storeResult.get("대구"));
        ((TextView)findViewById(R.id.txt_충북)).setText(storeResult.get("충북"));
        ((TextView)findViewById(R.id.txt_충남)).setText(storeResult.get("충남"));

        ((TextView)findViewById(R.id.txt_경기)).setText(storeResult.get("경기"));
        ((TextView)findViewById(R.id.txt_제주)).setText(storeResult.get("제주"));
        ((TextView)findViewById(R.id.txt_인천)).setText(storeResult.get("인천"));
        ((TextView)findViewById(R.id.txt_울산)).setText(storeResult.get("울산"));

        ((TextView)findViewById(R.id.txt_전북)).setText(storeResult.get("전북"));
        ((TextView)findViewById(R.id.txt_부산)).setText(storeResult.get("부산"));

        ((TextView)findViewById(R.id.txt_전남)).setText(storeResult.get("전남"));
        ((TextView)findViewById(R.id.txt_광주)).setText(storeResult.get("광주"));
        ((TextView)findViewById(R.id.txt_대전)).setText(storeResult.get("대전"));
    }
    public void areaClick(View v){
        String cutStr =  v.toString().substring(v.toString().length() - 3, v.toString().length());
        cutStr = cutStr.substring(0, 2);

        Intent intent = new Intent(this, StoreList.class);
        intent.putExtra("storeNM", cutStr);
        startActivity(intent);
    }

    public void prepareMessage(){
        Toast.makeText(this, "서비스 준비중 입니다.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        card_no = getIntent().getExtras().getString("CARD_NO");
        if(card_no != null) ((EditText) findViewById(R.id.txt_cardno_target)).setText(card_no);
        card_no = "";


    }
}
