package kr.wdream.wplusshop.common;

import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

import kr.wdream.wplusshop.common.util.Util;

/**
 * Created by SEO on 2015-12-17.
 */
public class PointUtil {

    public static HashMap<String, String> getPointResult(HashMap<String, String> pointParams){

        HashMap<String, String> pointResult = new HashMap<String, String>();
        String getConnStr = null;
        try {
            getConnStr = Util.getRequestStr("http://api.wincash.co.kr/point/pointprocess.asp", "GET", pointParams);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            JSONObject responseJSON = new JSONObject(getConnStr);


            JSONObject result       = responseJSON.getJSONObject("RESULT");
            String code             = result.getString("CODE");
            String message          = result.getString("MESSAGE");

            pointResult.put("code", code);
            pointResult.put("message", message);

            Log.d("Wcard", "code : " + code);

            if (code.equals("0000")){
                JSONObject data     = responseJSON.getJSONObject("DATA");
//                pointResult.put("CPOINT", data.getString("CPOINT"));
                JSONObject point    = data.getJSONObject("POINT");
                pointResult.put("CP", point.getString("CPOINT"));
                pointResult.put("WP", point.getString("WPOINT"));
                pointResult.put("HP", point.getString("HPOINT"));
                pointResult.put("IP", point.getString("IPOINT"));
                pointResult.put("TP", point.getString("TPOINT"));
                pointResult.put("PP", point.getString("PPOINT"));
                pointResult.put("FP", point.getString("FPOINT"));
            }

        } catch (Exception e) {
            pointResult.put("code", "9998");
            pointResult.put("message", "포인트 조회 중 오류가 발생되었습니다.");
            e.printStackTrace();
        }

        return pointResult;
    }
}

