package kr.wdream.wplusshop.common.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import kr.wdream.wplusshop.common.list.TodayDealVO;

/**
 * Created by SEO on 2016-01-11.
 */
public class TodayDealUtil {

    private static Context mMain;

    public static void inIt(Context main){
        mMain = main;
    }

    // 강의 목록 조회
    public static ArrayList<TodayDealVO> getTodayDealList(HashMap<String, String> todayDealParams){

        ArrayList<TodayDealVO> todayDealList = null;

        String getConnStr = null;
        try {
            getConnStr = Util.getRequestStr("http://api.wincash.co.kr/shop/shopprocess.asp", "GET", todayDealParams);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Log.d("dev_test", "getConnStr : " + getConnStr);

        todayDealList = new ArrayList<TodayDealVO>();
        try {
            JSONObject responseJSON = new JSONObject(getConnStr);

            int recordCount         = responseJSON.getInt("RECORDCOUNT");
            JSONObject result       = responseJSON.getJSONObject("RESULT");
            String code             = result.getString("CODE");
            String message          = result.getString("MESSAGE");
            if(code.equals("0000")){
                TodayDealVO.set_totalCNT(recordCount);
                JSONObject data = responseJSON.getJSONObject("DATA");

                JSONArray list     = data.getJSONArray("LIST");
                for ( int i = 0; i < list.length(); i++){
                    JSONObject listInfo = list.getJSONObject(i);

                    String prdt_nm          = listInfo.getString("PRDT_NM");
                    String sml_img_path     = listInfo.getString("SML_IMG_PATH");
//                    String mdl_img_path     = listInfo.getString("MDL_IMG_PATH");
                    String big_img_path     = listInfo.getString("BIG_IMG_PATH");
                    String prdt_path        = listInfo.getString("PRDT_PATH");
                    URL url = null;
                    try {
                        url = new URL(listInfo.getString("MDL_IMG_PATH"));

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

//                 입력스트림으로 해당 URL의 사진정보 가져오기 -> try⁄catch
                    InputStream is = null;
                    try {
                        is = url.openStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    final Bitmap mdl_img_path = BitmapFactory.decodeStream(is);

                    todayDealList.add(new TodayDealVO(prdt_nm, sml_img_path, mdl_img_path, big_img_path, prdt_path));
                }

            }

        } catch (Exception e) {
            Log.d("Login", "Exception");
            e.printStackTrace();
        }
        return todayDealList;
    }
}
