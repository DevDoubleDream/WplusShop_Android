package kr.wdream.wplusshop.common.util;

import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by SEO on 2015-12-11.
 */
public class RegistDeviceUtil {

    public static HashMap<String, String> getRegistDeviceResult(HashMap<String, String> registDeviceParams){

        HashMap<String, String> registDeviceResult = new HashMap<String, String>();
        String getConnStr = null;
        try {
            getConnStr = Util.getRequestStr("http://api.wincash.co.kr/app/appprocess.asp", "GET", registDeviceParams);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            JSONObject responseJSON = new JSONObject(getConnStr);


            JSONObject result       = responseJSON.getJSONObject("RESULT");
            String code             = result.getString("CODE");
            String message          = result.getString("MESSAGE");

            registDeviceResult.put("code", code);
            registDeviceResult.put("message", message);

            Log.d("Wcard", "code : " + code);

            if (code.equals("0000")){
                JSONObject data     = responseJSON.getJSONObject("DATA");
//                pointResult.put("CPOINT", data.getString("CPOINT"));
//                JSONObject point    = data.getJSONObject("POINT");
//                registDeviceResult.put("CP", point.getString("CPOINT"));
//                registDeviceResult.put("WP", point.getString("WPOINT"));
//                registDeviceResult.put("HP", point.getString("HPOINT"));
//                registDeviceResult.put("IP", point.getString("IPOINT"));
//                registDeviceResult.put("TP", point.getString("TPOINT"));
//                registDeviceResult.put("PP", point.getString("PPOINT"));
//                registDeviceResult.put("FP", point.getString("FPOINT"));
            }

        } catch (Exception e) {
            registDeviceResult.put("code", "9998");
            registDeviceResult.put("message", "포인트 조회 중 오류가 발생되었습니다.");
            e.printStackTrace();
        }

        return registDeviceResult;
    }
}
