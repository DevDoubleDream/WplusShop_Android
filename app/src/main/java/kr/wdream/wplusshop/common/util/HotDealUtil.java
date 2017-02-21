package kr.wdream.wplusshop.common.util;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import kr.wdream.wplusshop.common.list.HotDealVO;

/**
 * Created by SEO on 2015-12-22.
 */
public class HotDealUtil {

    private static Context mMain;

    public static void inIt(Context main){
        mMain = main;
    }

    // 강의 목록 조회
    public static ArrayList<HotDealVO> getHotDealList(HashMap<String, String> hotDealParams){

        ArrayList<HotDealVO> hotDealList = null;

        String getConnStr = null;
        try {
            getConnStr = Util.getRequestStr("http://api.wincash.co.kr/shop/shopprocess.asp", "GET", hotDealParams);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Log.d("dev_test", "getConnStr : " + getConnStr);

        hotDealList = new ArrayList<HotDealVO>();
        try {
            JSONObject responseJSON = new JSONObject(getConnStr);

            JSONObject result       = responseJSON.getJSONObject("RESULT");
            String code             = result.getString("CODE");
            String message          = result.getString("MESSAGE");
            if(code.equals("0000")){
                JSONObject data     = responseJSON.getJSONObject("DATA");

                JSONArray list     = data.getJSONArray("LIST");
                for ( int i = 0; i < list.length(); i++){
                    JSONObject listInfo = list.getJSONObject(i);

                    String prdt_nm          = listInfo.getString("PRDT_NM");
                    int cty_mny             = listInfo.getInt("CTY_MNY");
                    int sll_mny             = listInfo.getInt("SLL_MNY");
                    String sml_img_path     = listInfo.getString("SML_IMG_PATH");
                    String mdl_img_path     = listInfo.getString("MDL_IMG_PATH");
                    String big_img_path     = listInfo.getString("BIG_IMG_PATH");
                    String prdt_path        = listInfo.getString("PRDT_PATH");

                    hotDealList.add(new HotDealVO(prdt_nm, cty_mny, sll_mny, sml_img_path, mdl_img_path, big_img_path, prdt_path));
                }

            }

        } catch (Exception e) {
            Log.d("Login", "Exception");
            e.printStackTrace();
        }
        return hotDealList;
    }
}
