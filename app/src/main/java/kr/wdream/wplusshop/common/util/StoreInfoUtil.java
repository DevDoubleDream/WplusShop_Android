package kr.wdream.wplusshop.common.util;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by deobeuldeulim on 2016. 10. 26..
 */
public class StoreInfoUtil {
    public static HashMap<String, String> getStore(HashMap<String, String> paramStore) throws Exception {
            String connStr = "";
            String code = "-1";
            String message = "매장 정보를 가져오기 못했습니다.\n 관리자에게 문의하세요.";

            HashMap<String, String> resultStore = new HashMap<String, String>();
            resultStore.put("code", code);
            resultStore.put("message", message);

            connStr = Util.getRequestStr("http://coupon.wdream.kr/coupon/process.php", "GET", paramStore);

            JSONObject response = new JSONObject(connStr);

            if (null != response) {
                JSONObject result = response.getJSONObject("RESULT");

                code = result.getString("CODE");
                message = result.getString("MESSAGE");

                JSONObject data = response.getJSONObject("DATA");

                String photo = data.getString("CP_PUB_PHOTO_STORE");

                resultStore.put("photo", photo);
                resultStore.put("code", code);
                resultStore.put("message", message);
            }
            return resultStore;
    }
}
