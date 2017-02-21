package kr.wdream.wplusshop.common.util;

import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by SEO on 2016-01-26.
 */
public class AppInfoUtil {

    public static HashMap<String, String> getAppInfoResult(HashMap<String, String> appInfoParams) {
        HashMap<String, String> appInfoResult = new HashMap<String, String>();

        String getConnStr = null;
        try {
            getConnStr = Util.getRequestStr("http://api.wincash.co.kr/app/appprocess.asp", "GET", appInfoParams);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Log.d("dev_test", "getConnStr : " + getConnStr);

        try {
            JSONObject responseJSON = new JSONObject(getConnStr);

            String sessionid = (String) responseJSON.get("SESSIONSESSIONID");
            String cmd = (String) responseJSON.get("CMD");
            String mode = (String) responseJSON.get("MODE");

//                int recordcount         = Integer.valueOf((String) responseJSON.get("RECORDCOUNT"));

            JSONObject appData = responseJSON.getJSONObject("DATA");

            JSONObject result = responseJSON.getJSONObject("RESULT");
            String code = (String) result.get("CODE");
            String message = (String) result.get("MESSAGE");

            if (code.equals("0000")) {
                appInfoResult.put("app_ver", (String) appData.get("APP_VER"));
                appInfoResult.put("app_path", (String) appData.get("APP_PATH"));
            }
            appInfoResult.put("code", code);
            appInfoResult.put("message", message);

        } catch (Exception e) {
            appInfoResult.put("code", "9998");
            appInfoResult.put("message", "APP 정보 조회 중 오류가 발생되었습니다.");
            e.printStackTrace();
        }

        return appInfoResult;
    }
}
