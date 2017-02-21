package kr.wdream.wplusshop.common.util;

/**
 * Created by SEO on 2015-12-17.
 */

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import kr.wdream.wplusshop.common.CardInfo;

/**
 * Created by SEO on 2015-11-12.
 */
public class WcardUtil {

    public static HashMap<String, String> getWcardResult(HashMap<String, String> paramCard){

        String connStr = null;
        String code = "-1";
        String message = "관리자에게 문의하세요.";

        HashMap<String,String> resultCard = new HashMap<String, String>();

        try {
            connStr = Util.getRequestStr("http://api.wincash.co.kr/card/cardprocess.asp", "POST", paramCard);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            JSONObject response = new JSONObject(connStr);

            JSONObject result = response.getJSONObject("RESULT");

            String result_code = result.getString("CODE");
            String result_message =result.getString("MESSAGE");

            if(result_code.equals("0000")){
                JSONObject data = response.getJSONObject("DATA");

                resultCard.put("code", result_code);

                resultCard.put("user_no", data.getString("MEM_NO"));
                resultCard.put("CPOINT", String.valueOf(data.getInt("CPOINT")));
                resultCard.put("user_sex", data.getString("SEX"));
                resultCard.put("user_card", data.getString("CARDNO"));
                resultCard.put("user_card_pw", data.getString("CARDPW"));
                resultCard.put("code", result_code);
                resultCard.put("message", result_message);

                CardInfo.putCardNo(data.getString("CARDNO"));
                CardInfo.putCardPw(data.getString("CARDPW"));
                CardInfo.putPoint(data.getString("CPOINT"));
                CardInfo.putUserNo(data.getString("MEM_NO"));
                CardInfo.putUserSex(data.getString("SEX"));

            }else{
                resultCard.put("code", result_code);
                resultCard.put("message", result_message);
            }

        } catch (JSONException e) {
            e.printStackTrace();

            resultCard.put("code", code);
            resultCard.put("message", message);

            return resultCard;
        }

        return resultCard;
    }
}

