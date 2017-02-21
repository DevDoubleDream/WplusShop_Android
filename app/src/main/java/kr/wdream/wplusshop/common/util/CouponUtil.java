package kr.wdream.wplusshop.common.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import kr.wdream.wplusshop.common.list.CouponVO;

/**
 * Created by deobeuldeulim on 2016. 10. 24..
 */

public class CouponUtil {

    public static ArrayList<CouponVO> getCoupon(HashMap<String,String> paramCoupon) throws Exception {
        ArrayList<CouponVO> resultCoupon = new ArrayList<CouponVO>();

        String code = "-1";
        String message = "쿠폰 정보를 가져오지 못했습니다.\n관리자에게 문의하세요.";

        String connStr = Util.getRequestStr("http://coupon.wdream.kr/api/process.php","POST", paramCoupon);

        JSONObject response = new JSONObject(connStr);

        if(response!=null){
            JSONObject result = response.getJSONObject("RESULT");

            code = result.getString("CODE");
            message = result.getString("MESSAGE");

            CouponVO.totalCount = response.getString("RECORDCOUNT");

            JSONObject data = response.getJSONObject("DATA");

            JSONArray list = data.getJSONArray("LIST");
            Log.d("adapter", "상은 : " + list.length());
            for(int i=0; i<list.length(); i++) {
                JSONObject item = list.getJSONObject(i);

                String cp_nm = item.getString("CP_NM");
                String cp_sdt = item.getString("CP_SDT");
                String cp_edt = item.getString("CP_EDT");
                String cp_id = item.getString("CP_ID");
                String cp_no = item.getString("CP_NO");
                String cp_pub = item.getString("CP_PUB");
                String cp_pub_nm = item.getString("CP_PUB_NM");
                String cp_photo = item.getString("CP_PHOTO_PATH_A_T");

                if (!"".equals(cp_nm)) {
                    resultCoupon.add(new CouponVO(code, message, cp_nm, cp_sdt, cp_edt, cp_id, cp_no, cp_pub, cp_pub_nm, cp_photo));
                    Log.d("상은", "resultCoupon.size() : " + resultCoupon.size());
                }
            }
        }else{
            resultCoupon.add(new CouponVO(code, message,"", "", "", "", "", "", "", ""));
        }

        return resultCoupon;
    }

    public static HashMap<String, String> useCoupon(HashMap<String, String> paramCoupon) throws Exception {
        String conStr = "";
        String code = "-1";
        String message = "쿠폰 사용에 실패했습니다.\n관리자에게 문의하세요.";

        HashMap<String,String> resultCoupon = new HashMap<String,String>();

        resultCoupon.put("code", code);
        resultCoupon.put("message", message);

        conStr = Util.getRequestStr("http://coupon.wdream.kr/api/process.php", "POST", paramCoupon);

        JSONObject response = new JSONObject(conStr);

        if(null != response) {
            JSONObject result = response.getJSONObject("RESULT");

            code = result.getString("CODE");
            message = result.getString("MESSAGE");

            resultCoupon.put("code", code);
            resultCoupon.put("message", message);
        }

        return resultCoupon;
    }

    public static HashMap<String,String> useCouponPoint(HashMap<String,String> paramCoupon) throws Exception {
        String conStr = "";
        String code = "-1";
        String message = "쿠폰 사용에 실패하였습니다. \n 관리자에게 문의하세요.";

        HashMap<String,String> resultCoupon = new HashMap<String,String>();

        resultCoupon.put("code", code);
        resultCoupon.put("message", message);

        conStr = Util.getRequestStr("http://www.wpoint.co.kr/commong/pay/reg_coupon_exe.asp", "POST", paramCoupon);

        JSONObject response = new JSONObject(conStr);

        if (null !=response) {
            JSONObject result = response.getJSONObject("RESULT");

            code = result.getString("CODE");

            resultCoupon.put("code", code);
        }

        return resultCoupon;
    }
}
