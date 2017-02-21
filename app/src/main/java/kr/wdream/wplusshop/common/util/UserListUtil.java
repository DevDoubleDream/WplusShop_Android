package kr.wdream.wplusshop.common.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;

import kr.wdream.wplusshop.R;
import kr.wdream.wplusshop.common.DBManager;
import kr.wdream.wplusshop.common.list.StoreListVO;
import kr.wdream.wplusshop.common.list.UserListVO;

/**
 * Created by SEO on 2015-12-28.
 */
public class UserListUtil {

    private static Context mMain;

    public static String DBName = "WPLUSSHOP.db";
    public static String tableName = "USER_LIST";

    public static void inIt(Context main){
        mMain = main;
    }

    // 강의 목록 조회
    public static ArrayList<UserListVO> getUserList(int pageNum, HashMap<String, String> userParams){

        ArrayList<UserListVO> userList = null;
        userList = new ArrayList<UserListVO>();
        DBManager dbManager = new DBManager(mMain.getApplicationContext(), DBName, null, 4);
        String[] selectParams = {"IDX", "USER_NAME", "USER_PHONE", "USER_PHONE1", "USER_PHONE2", "USER_PHONE3", "USER_SEX", "USER_CARDNO"};    // select columns
        HashMap<String, String> whereParams = new HashMap<String, String>();    // where 조건
//        HashMap<String, String> userSetRst = null;
        String sql = "SELECT IDX, USER_NAME, USER_PHONE, USER_PHONE1, USER_PHONE2, USER_PHONE3, USER_SEX, USER_CARDNO FROM "+ tableName;
//        try {
//            userSetRst = dbManager.selectQuery(tableName, selectParams, whereParams);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        String[][] userSetRst = dbManager.selectQuery(sql);

        if(userSetRst != null) {

            StoreListVO.set_totalCNT(userSetRst.length);
            StoreListVO.set_pageSize(Integer.parseInt(userParams.get("pagesize")));
            StoreListVO.set_pageNo(Integer.parseInt(userParams.get("page")));

            for(int i = 0; i < userSetRst.length; i++){
                String user_idx     = userSetRst[i][0];
                String user_name    = userSetRst[i][1];
                String user_phone   = userSetRst[i][2];
                String user_phone1  = userSetRst[i][3];
                String user_phone2  = userSetRst[i][4];
                String user_phone3  = userSetRst[i][5];
                String user_sex     = userSetRst[i][6];
                String user_cardno  = userSetRst[i][7];

                Log.d("test_wplus", "user_name : "+ user_name);
                Log.d("test_wplus", "user_phone : "+ user_phone);
                Log.d("test_wplus", "user_phone1 : "+ user_phone1);
                Log.d("test_wplus", "user_phone2 : "+ user_phone2);
                Log.d("test_wplus", "user_phone3 : "+ user_phone3);
                Log.d("test_wplus", "user_sex : " + user_sex);
                Log.d("test_wplus", "user_cardno : " + user_cardno);

                userList.add(new UserListVO(user_idx, user_name, user_phone1, user_phone2, user_phone3, user_cardno, user_sex));

            }

        }
        setPointDisplay(pageNum);
        return userList;
    }

    public static void setPointDisplay(int pageNum){
        int pageSize    = 0;
        int totCNT      = 0;
        totCNT = UserListVO.get_totalCNT();
        pageSize = UserListVO.get_pageSize();
        if (pageNum * pageSize >= totCNT)   // 더보기 버튼 비노출
            ((Button) ((Activity) mMain).findViewById(R.id.btn_next)).setVisibility(View.GONE);
        else
            ((Button) ((Activity) mMain).findViewById(R.id.btn_next)).setText("더보기 ∨ ("+ (totCNT - (pageNum * pageSize)) +")");
//
//        // 총 강의 갯수 노출
//        ((TextView) ((Activity) mMain).findViewById(R.id.tot_cnt)).setText(Integer.toString(totCNT));
    }
}
