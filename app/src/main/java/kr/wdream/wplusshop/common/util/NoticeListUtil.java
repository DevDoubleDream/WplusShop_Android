package kr.wdream.wplusshop.common.util;

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
import kr.wdream.wplusshop.common.list.NoticeListVO;

/**
 * Created by SEO on 2016-04-25.
 */
public class NoticeListUtil {

    private static Context mMain;

    public static void inIt(Context main){
        mMain = main;
    }

    // 강의 목록 조회
    public static ArrayList<NoticeListVO> getNoticeList(int pageNum, HashMap<String, String> noticeParams){

        ArrayList<NoticeListVO> noticeList = null;

        String getConnStr = null;
        try {
            getConnStr = Util.getRequestStr("http://api.wincash.co.kr/app/appprocess.asp", "GET", noticeParams);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Log.d("dev_test", "getConnStr : " + getConnStr);

        noticeList = new ArrayList<NoticeListVO>();
        try {
            JSONObject responseJSON = new JSONObject(getConnStr);

            JSONObject result       = responseJSON.getJSONObject("RESULT");
            String code             = result.getString("CODE");
            String message          = result.getString("MESSAGE");
            if(code.equals("1")){
                JSONObject data     = responseJSON.getJSONObject("DATA");
                NoticeListVO.set_totalCNT(Integer.valueOf((Integer) data.get("TOTAL_COUNT")));
                NoticeListVO.set_pageSize(Integer.parseInt(noticeParams.get("pagesize")));
                NoticeListVO.set_pageNo(Integer.parseInt(noticeParams.get("page")));

                JSONArray list     = data.getJSONArray("LIST");
                for ( int i = 0; i < list.length(); i++){
                    JSONObject listInfo = list.getJSONObject(i);
                    String subject      = listInfo.getString("SUBJECT");
                    String content      = listInfo.getString("CONTENT");
                    String create_dt    = listInfo.getString("CREATE_DT");

                    Log.d("dev_test", "subject : " + subject);
                    Log.d("dev_test", "content : " + content);
                    Log.d("dev_test", "create_dt : " + create_dt);
                    noticeList.add(new NoticeListVO(subject, content, create_dt));
                }

            }

        } catch (Exception e) {
            Log.d("Login", "Exception");
            e.printStackTrace();
        }
        setMoreDisplay(pageNum);
        return noticeList;
    }

    public static void setMoreDisplay(int pageNum){
        int pageSize    = 0;
        int totCNT      = 0;
        totCNT = NoticeListVO.get_totalCNT();
        pageSize = NoticeListVO.get_pageSize();
        if (pageNum * pageSize >= totCNT)   // 더보기 버튼 비노출
            ((Button) ((Activity) mMain).findViewById(R.id.btn_next)).setVisibility(View.GONE);
        else
            ((Button) ((Activity) mMain).findViewById(R.id.btn_next)).setText("더보기 ∨ ("+ (totCNT - (pageNum * pageSize)) +")");
//
//        // 총 강의 갯수 노출
//        ((TextView) ((Activity) mMain).findViewById(R.id.tot_cnt)).setText(Integer.toString(totCNT));
    }
}
