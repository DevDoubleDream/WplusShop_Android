package kr.wdream.wplusshop.common.util;

import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by deobeuldeulim on 2017. 2. 28..
 */

public class LoginUtil {
    public static HashMap<String,String> setCardLogin(HashMap<String,String> param) throws Exception {
        HashMap<String,String> resultCardLogin = new HashMap<String,String>();

        String code = "-1";
        String msg  = "관리자에게 문의하세요.";

        resultCardLogin.put("code", code);
        resultCardLogin.put("msg" , msg);

        String strConnect = Util.getRequestStr("http://api.wincash.co.kr/card/cardprocess.asp", "POST", param);

        if (strConnect != null) {
            JSONObject response = new JSONObject(strConnect);

            JSONObject result = response.getJSONObject("RESULT");

            code = result.getString("CODE");
            msg  = result.getString("MESSAGE");

            resultCardLogin.put("code", code);
            resultCardLogin.put("msg", msg);

            if("0000".equals(code)){

                JSONObject data = response.getJSONObject("DATA");

                String cardNo = data.getString("CARDNO");
                String cardPw = data.getString("CARDPW");
                String vacctno = data.getString("VACCTNO");
                String cardStatCd = data.getString("CARDSTATCD");
                String cardStatNm = data.getString("CARDSTATNM");
                String cPoint = data.getString("CPOINT");

                resultCardLogin.put("cardNo", cardNo);
                resultCardLogin.put("cardPw", cardPw);
                resultCardLogin.put("vacctno", vacctno);
                resultCardLogin.put("cardStatCd", cardStatCd);
                resultCardLogin.put("cardStatNm", cardStatNm);
                resultCardLogin.put("cPoint", cPoint);

            }

        }
        return resultCardLogin;
    }
}
