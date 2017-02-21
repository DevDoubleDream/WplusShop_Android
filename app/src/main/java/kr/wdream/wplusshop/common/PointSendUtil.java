package kr.wdream.wplusshop.common;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

import kr.wdream.wplusshop.R;
import kr.wdream.wplusshop.common.util.Util;

/**
 * Created by SEO on 2015-12-16.
 */
public class PointSendUtil {

    private static Context mMain;

    public static void inIt(Context main){
        mMain = (Context) main;
    }

    public static HashMap<String, String> putPointSendResult(HashMap<String, String> pointSendParams) {
        HashMap<String, String> pointSendResult = new HashMap<String, String>();


        String getConnStr = null;
        try {
            getConnStr = Util.getRequestStr("http://api.wincash.co.kr/point/pointprocess.asp", "GET", pointSendParams);
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

//                JSONObject sscommon = responseJSON.getJSONObject("SSCOMMON");

            JSONObject result = responseJSON.getJSONObject("RESULT");
            String code = (String) result.get("CODE");
            String message = (String) result.get("MESSAGE");

//                if (code.equals("0000")) {
//                    pointSendResult.put("user_no", (String) sscommon.get("SS_USER_NO"));
//                    pointSendResult.put("user_id", (String) sscommon.get("SS_USER_ID"));
//                    pointSendResult.put("user_pw", (String) sscommon.get("SS_USER_PWA"));
//                    pointSendResult.put("user_nm", (String) sscommon.get("SS_USER_NAME"));
//                    pointSendResult.put("user_cardno", (String) sscommon.get("SS_USER_CARDNO"));
//                    pointSendResult.put("user_dmn", (String) sscommon.get("SS_USER_DMN"));
//                    pointSendResult.put("user_cpoint", (String) sscommon.get("SS_USER_CPOINT"));
//                    pointSendResult.put("user_grade", (String) sscommon.get("SS_USER_GRADE"));
//                }
            pointSendResult.put("code", code);
            pointSendResult.put("message", message);

        } catch (Exception e) {
            pointSendResult.put("code", "9998");
            pointSendResult.put("message", "선물/기부하기 처리 중 오류가 발생되었습니다.");
            e.printStackTrace();
        }

        return pointSendResult;
    }

    public static boolean checkValid(HashMap<String, String> pointSendParams){
        boolean state = true;
        String member_cardpw = pointSendParams.get("member_cardpw");
        Log.d("dev_test", "member_cardpw : "+ member_cardpw);
        if(member_cardpw.length() < 4){
            Toast.makeText(mMain, "카드 비밀번호를 정확히 입력해 주세요.", Toast.LENGTH_SHORT).show();
            ((EditText) ((Activity) mMain).findViewById(R.id.txt_member_cardpw)).requestFocus();
            state = false;
        }
        Log.d("dev_test", "state : "+ state);
        return state;
    }

}
