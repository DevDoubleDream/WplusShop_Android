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
 * Created by SEO on 2015-12-17.
 */
public class LoginUtil {
    private static Context mMain;

    public static void inIt(Context main){
        mMain = (Context) main;
    }

    public static HashMap<String, String> getLoginResult(HashMap<String, String> loginParams){
        HashMap<String, String> loginResult = new HashMap<String, String>();
        if(checkValid(loginParams)){
            String getConnStr = null;
            try {
                getConnStr = Util.getRequestStr("http://api.wincash.co.kr/login/loginprocess.asp", "GET", loginParams);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Log.d("dev_test", "getConnStr : " + getConnStr);

            try {
                JSONObject responseJSON = new JSONObject(getConnStr);

                String sessionid        = (String) responseJSON.get("SESSIONSESSIONID");
                String cmd              = (String) responseJSON.get("CMD");
                String mode             = (String) responseJSON.get("MODE");

//                int recordcount         = Integer.valueOf((String) responseJSON.get("RECORDCOUNT"));

                JSONObject sscommon     = responseJSON.getJSONObject("SSCOMMON");

                JSONObject result       = responseJSON.getJSONObject("RESULT");
                String code             = (String) result.get("CODE");
                String message          = (String) result.get("MESSAGE");

                if (code.equals("0000")) {
                    loginResult.put("user_no", (String) sscommon.get("SS_USER_NO"));
                    loginResult.put("user_id", (String) sscommon.get("SS_USER_ID"));
                    loginResult.put("user_pw", (String) sscommon.get("SS_USER_PWA"));
                    loginResult.put("user_nm", (String) sscommon.get("SS_USER_NAME"));
                    loginResult.put("user_cardno", (String) sscommon.get("SS_USER_CARDNO"));
                    loginResult.put("user_dmn", (String) sscommon.get("SS_USER_DMN"));
                    loginResult.put("user_cpoint", (String) sscommon.get("SS_USER_CPOINT"));
                    loginResult.put("user_grade", (String) sscommon.get("SS_USER_GRADE"));
                    loginResult.put("user_viz_at", sscommon.getString("SS_VIZ_AT"));
                }
                loginResult.put("code", code);
                loginResult.put("message", message);

            } catch (Exception e) {
                loginResult.put("code", "9998");
                loginResult.put("message", "로그인 처리 중 오류가 발생되었습니다.");
                e.printStackTrace();
            }
        }
        else{
            loginResult.put("code", "9999");
            loginResult.put("message", "유효성 체크 오류.");
        }
        return loginResult;
    }

    public static boolean checkValid(HashMap<String, String> loginParams){
        boolean state = true;
        String login_id = loginParams.get("user_id");
        String login_pw = loginParams.get("user_pw");

        if(login_id.length() < 4 || login_id.length() > 12){
            Toast.makeText(mMain, "아이디를 4~12자에 맞게 입력해 주세요.", Toast.LENGTH_SHORT).show();
            ((EditText) ((Activity) mMain).findViewById(R.id.login_id)).requestFocus();
            state = false;
        }
        else if(login_pw.length() < 8 || login_pw.length() > 15){
            Toast.makeText(mMain, "비밀번호를 8~15자에 맞게 입력해 주세요.", Toast.LENGTH_SHORT).show();
            ((EditText) ((Activity) mMain).findViewById(R.id.login_pw)).requestFocus();
            state = false;
        }
        return state;
    }
}
