package kr.wdream.wplusshop.common.util;

import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

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

    public static HashMap<String,String> selectPay (HashMap<String,String> paramPay) throws Exception {
        String connStr = "";
        String code = "-1";
        String message = "결제가 이루어지지 않았습니다\n관리자에게 문의하세요.";

        HashMap<String,String> resultPay = new HashMap<String,String>();

        resultPay.put("code", code);
        resultPay.put("message", message);

        connStr = Util.getRequestStr("http://store.wincash.co.kr/St_Sub/quick/payment/point_transact_insert_exec_van.asp", "POST", paramPay);

        JSONObject response = new JSONObject(connStr);

        if (response != null) {
            JSONObject result = response.getJSONObject("RESULT");

            code = result.getString("CODE");
            message = result.getString("MESSAGE");

            resultPay.put("code", code);
            resultPay.put("message", message);

        }

        return resultPay;
    }

    public static HashMap<String,String> selectSave(HashMap<String,String> paramSave) throws Exception {
        String connStr = "";
        String code = "-1";
        String message = "결제가 이루어지지 않았습니다\n관리자에게 문의하세요.";

        HashMap<String,String> resultSave = new HashMap<String,String>();

        resultSave.put("code", code);
        resultSave.put("message", message);

        connStr = Util.getRequestStr("http://store.wincash.co.kr/St_Sub/quick/payment/point_transact_insert_exec_van.asp", "POST", paramSave);

        JSONObject response = new JSONObject(connStr);

        if (response != null) {
            JSONObject result = response.getJSONObject("RESULT");

            code = result.getString("CODE");
            message = result.getString("MESSAGE");

            resultSave.put("code", code);
            resultSave.put("message", message);

        }

        return resultSave;
    }
}

