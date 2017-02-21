package kr.wdream.wplusshop.common.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by SEO on 2015-12-17.
 */
public class StorePointUtil {

    public static HashMap<String, String> getStoreResult(HashMap<String, String> storePointParams){

        HashMap<String, String> storePointResult = new HashMap<String, String>();
        String getConnStr = null;
        try {
            getConnStr = Util.getRequestStr("http://api.wincash.co.kr/store/storeprocess.asp", "GET", storePointParams);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            JSONObject responseJSON = new JSONObject(getConnStr);


            JSONObject result       = responseJSON.getJSONObject("RESULT");
            String code             = result.getString("CODE");
            String message          = result.getString("MESSAGE");

            storePointResult.put("code", code);
            storePointResult.put("message", message);

            if (code.equals("0000")){
                JSONObject data       = responseJSON.getJSONObject("DATA");
                JSONArray list     = data.getJSONArray("LIST");
                for ( int i = 0; i < list.length(); i++){
                    JSONObject listInfo = list.getJSONObject(i);
                    String stnm     = listInfo.getString("STNM");
                    String stphone  = listInfo.getString("STPHONE");
                    String point_xx  = listInfo.getString("XX");
                    String point_yy  = listInfo.getString("YY");
                    storePointResult.put("stnm", stnm);
                    storePointResult.put("stphone", stphone);
                    storePointResult.put("point_xx", point_xx);
                    storePointResult.put("point_yy", point_yy);
                }
            }

        } catch (Exception e) {
            storePointResult.put("code", "9998");
            storePointResult.put("message", "가맹점정보 조회 중 오류가 발생되었습니다.");
            e.printStackTrace();
        }

        return storePointResult;
    }


    public static String[][] getOtherStoreResult(HashMap<String, String> otherStoreParams){
        String[][] otherStoreResult = new String[0][0];
        String getConnStr = null;
        try {
            getConnStr = Util.getRequestStr("http://api.wincash.co.kr/store/storeprocess.asp", "GET", otherStoreParams);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            JSONObject responseJSON = new JSONObject(getConnStr);


            Integer recordcount     = responseJSON.getInt("RECORDCOUNT");
            JSONObject result       = responseJSON.getJSONObject("RESULT");
            String code             = result.getString("CODE");
            String message          = result.getString("MESSAGE");

            Log.d("dev_test", "recordcount : " + recordcount);

            otherStoreResult = new String[recordcount][4];
            if (code.equals("0000")){
                JSONObject data       = responseJSON.getJSONObject("DATA");
                JSONArray list     = data.getJSONArray("LIST");
                for ( int i = 0; i < list.length(); i++){
                    JSONObject listInfo = list.getJSONObject(i);
                    otherStoreResult[i][0] = listInfo.getString("STNM");
                    otherStoreResult[i][1] = listInfo.getString("STPHONE");
                    otherStoreResult[i][2] = listInfo.getString("XX");
                    otherStoreResult[i][3] = listInfo.getString("YY");

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return otherStoreResult;
    }
}

