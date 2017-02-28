package kr.wdream.wplusshop.common.util;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.HashMap;

import kr.wdream.wplusshop.common.Metrics;

/**
 * Created by deobeuldeulim on 2017. 2. 28..
 */

public class CardLoginTask extends AsyncTask {

    private HashMap<String,String> paramLogin = new HashMap<String,String>();
    private HashMap<String,String> resultLogin = new HashMap<String,String>();
    private String memberCardNo;
    private String memberCardPw;
    private Handler handler;

    private Bundle bundle;
    private Message msg;

    public CardLoginTask(String cardNo, String cardPw, Handler handler){
        this.memberCardNo = cardNo;
        this.memberCardPw = cardPw;
        this.handler = handler;

        bundle = new Bundle();
        msg = new Message();

        resultLogin.put("code", "-1");
        resultLogin.put("msg", "관리자에게 문의하세요");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        paramLogin.put("cmd", "login");
        paramLogin.put("member_cardno", memberCardNo);
        paramLogin.put("member_cardpw", memberCardPw);
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            resultLogin = LoginUtil.setCardLogin(paramLogin);
            bundle.putSerializable("resultLogin", resultLogin);

            if ("0000".equals(resultLogin.get("code"))) {
                if (Metrics.STRING_CARD_ACCEPT.equals(resultLogin.get("cardStatCd"))) {
                    msg.what = Metrics.CARD_LOGIN_SUCCESS;
                } else if (Metrics.STRING_CARD_WAIT.equals(resultLogin.get("cardStatCd"))) {
                    msg.what = Metrics.CARD_ISSUE_WAIT;
                }
            } else {
                msg.what = Metrics.CARD_LOGIN_FAILED;
            }
            msg.setData(bundle);

            handler.sendMessage(msg);

        } catch (Exception e) {
            e.printStackTrace();

            bundle.putSerializable("resultLogin", resultLogin);

            msg.what = Metrics.CARD_LOGIN_FAILED;
            msg.setData(bundle);

            handler.sendMessage(msg);

        }
        return null;
    }
}
