package kr.wdream.wplusshop.common.util;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by deobeuldeulim on 2017. 1. 25..
 */

public class StoreAuthUtil {
    public static HashMap<String,String> getStoreAuth(HashMap<String,String> paramStoreAuth) throws Exception {
        HashMap<String,String> resultStoreAuth = new HashMap<String,String>();
        String code = "-1";
        String msg  = "가맹점 인증 중 오류가 발생했습니다.\n 관리자에게 문의하세요.";

        resultStoreAuth.put("code", code);
        resultStoreAuth.put("msg", msg);

        String strConnect = Util.getRequestStr("http://api.wincash.co.kr/store/storeprocess.asp", "POST", paramStoreAuth);

        JSONObject response = new JSONObject(strConnect);

        if (response != null) {
            JSONObject result = response.getJSONObject("RESULT");
            JSONObject data = response.getJSONObject("DATA");

            code = result.getString("CODE");
            msg  = result.getString("MESSAGE");

            if ("0000".equals(code)) {
                String stCode = data.getString("STCD");
                resultStoreAuth.put("stCode", stCode);
            }

            resultStoreAuth.put("code", code);
            resultStoreAuth.put("msg", msg);
        }

        return resultStoreAuth;
    }
}
