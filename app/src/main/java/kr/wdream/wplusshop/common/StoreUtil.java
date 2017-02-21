package kr.wdream.wplusshop.common;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import kr.wdream.wplusshop.common.util.Util;

/**
 * Created by SEO on 2015-12-17.
 */
public class StoreUtil {


    public static HashMap<String, String> getStoreResult(HashMap<String, String> storeParams){

        HashMap<String, String> storeResult = new HashMap<String, String>();
        String getConnStr = null;
        try {
            getConnStr = Util.getRequestStr("http://api.wincash.co.kr/store/storeprocess.asp", "GET", storeParams);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            JSONObject responseJSON = new JSONObject(getConnStr);


            JSONObject result       = responseJSON.getJSONObject("RESULT");
            String code             = result.getString("CODE");
            String message          = result.getString("MESSAGE");

            storeResult.put("code", code);
            storeResult.put("message", message);

            Log.d("Wcard", "code : " + code);

            if (code.equals("0000")){
                JSONObject data       = responseJSON.getJSONObject("DATA");
                JSONArray list     = data.getJSONArray("LIST");
                for ( int i = 0; i < list.length(); i++){
                    JSONObject listInfo = list.getJSONObject(i);
                    String region       = listInfo.getString("REGION");
                    String region_cnt   = listInfo.getString("REGION_CNT");
                    storeResult.put(region, region_cnt);
                }
            }

        } catch (Exception e) {
            storeResult.put("code", "9998");
            storeResult.put("message", "가맹점정보 조회 중 오류가 발생되었습니다.");
            e.printStackTrace();
        }

        return storeResult;
    }
}
