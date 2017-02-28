package kr.wdream.wplusshop.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by deobeuldeulim on 2017. 2. 28..
 */

public class Metrics {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public static final int CARD_LOGIN_SUCCESS = 100;
    public static final int CARD_LOGIN_FAILED  = 101;
    public static final int CARD_POINT_SUCCESS = 102;
    public static final int CARD_POINT_FAILED  = 103;
    public static final int CARD_ISSUE_ACCEPT  = 104;
    public static final int CARD_ISSUE_WAIT    = 105;

    public static final String STRING_CARD_WAIT = "A";
    public static final String STRING_CARD_ACCEPT = "C";

    public Metrics(Context context){
        sp = context.getSharedPreferences("CARDLOGIN", Context.MODE_PRIVATE);
        editor = sp.edit();
    }
    public void setCardPoint(String cardPoint) {
        Log.d("상은", "cardPoint : " + cardPoint);
        editor.putString("cardLoginPoint", cardPoint);
        editor.commit();
        Log.d("상은", "after commit cardPoint : " + sp.getString("cardLoginPoint", ""));
    }

    public void setCardLoginNo(String cardLoginNo) {
        editor.putString("cardLoginNo", cardLoginNo);
        editor.commit();
    }

    public void setCardLoginPw(String cardLoginPw) {
        editor.putString("cardLoginPw", cardLoginPw);
        editor.commit();
    }

    public void setCardState(String cardState){
        editor.putString("cardState", cardState);
        editor.commit();
    }

    public String getCardPoint() {
        return sp.getString("cardLoginPoint","");
    }

    public String getCardLoginPw() {
        return sp.getString("cardLoginPw","");
    }

    public String getCardLoginNo() {
        return sp.getString("cardLoginNo", "");
    }

    public String getCardState() {
        return sp.getString("cardState", "");
    }

    public void clearLoginData() {
        editor.clear();
        editor.commit();
    }
}
