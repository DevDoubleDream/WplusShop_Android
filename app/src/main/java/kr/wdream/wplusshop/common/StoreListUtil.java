package kr.wdream.wplusshop.common;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import kr.wdream.wplusshop.R;
import kr.wdream.wplusshop.common.list.StoreListVO;
import kr.wdream.wplusshop.common.util.Util;

/**
 * Created by SEO on 2015-12-17.
 */
public class StoreListUtil {

    private static Context mMain;

    public static void inIt(Context main){
        mMain = main;
    }

    // 강의 목록 조회
    public static ArrayList<StoreListVO> getStoreList(int pageNum, HashMap<String, String> storeParams){

        ArrayList<StoreListVO> storeList = null;

        String getConnStr = null;
        try {
            getConnStr = Util.getRequestStr("http://api.wincash.co.kr/store/storeprocess.asp", "GET", storeParams);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Log.d("dev_test", "getConnStr : " + getConnStr);

        storeList = new ArrayList<StoreListVO>();
        try {
            JSONObject responseJSON = new JSONObject(getConnStr);

            JSONObject result       = responseJSON.getJSONObject("RESULT");
            String code             = result.getString("CODE");
            String message          = result.getString("MESSAGE");
            if(code.equals("0000")){
                JSONObject data     = responseJSON.getJSONObject("DATA");
                StoreListVO.set_totalCNT(Integer.valueOf((Integer) data.get("TOTAL_COUNT")));
                StoreListVO.set_pageSize(Integer.parseInt(storeParams.get("pagesize")));
                StoreListVO.set_pageNo(Integer.parseInt(storeParams.get("page")));

                JSONArray list     = data.getJSONArray("LIST");
                for ( int i = 0; i < list.length(); i++){
                    JSONObject listInfo = list.getJSONObject(i);
                    String stbiztype       = listInfo.getString("STBIZTYPE");
                    String stbizitem   = listInfo.getString("STBIZITEM");
                    String stnm      = listInfo.getString("STNM");
                    String stphone      = listInfo.getString("STPHONE");
                    String staddr1      = listInfo.getString("STADDR1");
                    String staddr2      = listInfo.getString("STADDR2");
                    String point_xx      = listInfo.getString("XX");
                    String point_yy      = listInfo.getString("YY");

                    storeList.add(new StoreListVO(stbiztype, stbizitem, stnm, stphone, staddr1, staddr2, point_xx, point_yy));
                }

            }

        } catch (Exception e) {
            Log.d("Login", "Exception");
            e.printStackTrace();
        }
        setPointDisplay(pageNum);
        return storeList;
    }

    public static void setPointDisplay(int pageNum){
        int pageSize    = 0;
        int totCNT      = 0;
        totCNT = StoreListVO.get_totalCNT();
        pageSize = StoreListVO.get_pageSize();
        if (pageNum * pageSize >= totCNT)   // 더보기 버튼 비노출
            ((Button) ((Activity) mMain).findViewById(R.id.btn_next)).setVisibility(View.GONE);
        else
            ((Button) ((Activity) mMain).findViewById(R.id.btn_next)).setText("더보기 ∨ ("+ (totCNT - (pageNum * pageSize)) +")");
//
//        // 총 강의 갯수 노출
//        ((TextView) ((Activity) mMain).findViewById(R.id.tot_cnt)).setText(Integer.toString(totCNT));
    }
}

