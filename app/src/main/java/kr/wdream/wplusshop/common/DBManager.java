package kr.wdream.wplusshop.common;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by SEO on 2015-12-18.
 */
public class DBManager extends SQLiteOpenHelper {

    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //테이블 생성 (이미 생성된 경우 생성되지 않음)
//        db.execSQL("CREATE TABLE USER_SETTING (" +
//                "IDX INTEGER PRIMARY KEY, " +       // 시퀀스 번호
//                "DEVICE_SN VARCHAR(30) UNIQUE," +   // 단말기 일련번호
//                "MEMBER_DMN VARCHAR(30)," +         // 회원가입 도메인
//                "MEMBER_NO VARCHAR(20)," +          // 회원 일련번호
//                "MEMBER_ID VARCHAR(20)," +          // 회원 아이디
//                "MEMBER_NAME VARCHAR(20)," +        // 회원 성명
//                "MEMBER_CARDNO VARCHAR(20), " +     // 회원 카드번호
//                "MEMBER_CPOINT VARCHAR(20), " +     // 회원 잔여 포인트
//                "LOCK_YN VARCHAR(1) DEFAULT 'N'," +             // 암호 잠금 설정
//                "LOCK_PW VARCHAR(4)," +             // 암호 잠금 비밀번호
//                "MSG_YN VARCHAR(1) DEFAULT 'N'," +              // PUSH MSG 수신 설정
//                "AUTO_LOGIN_YN VARCHAR(1) DEFAULT 'N');");       // 자동 로그인 설정

        db.execSQL("CREATE TABLE USER_SETTING (" +
                "IDX INTEGER PRIMARY KEY, " +       // 시퀀스 번호
                "USER_ID VARCHAR(20), " +
                "USER_PW VARCHAR(20), " +
                "USER_NO VARCHAR(20), " +
                "CARD_NO VARCHAR(20), " +
                "USER_GRADE VARCHAR(20), " +
                "USER_DMN VARCHAR(30), " +
                "USER_NM VARCHAR(20), " +
                "USER_CPOINT VARCHAR(20), " +
                "USER_VIZ_AT VARCHAR(1), " +
                "LOCK_YN VARCHAR(1) DEFAULT 'N'," +             // 암호 잠금 설정
                "LOCK_PW VARCHAR(4)," +                         // 암호 잠금 비밀번호
                "MSG_YN VARCHAR(1) DEFAULT 'Y'," +              // PUSH MSG 수신 설정
                "AUTO_LOGIN_YN VARCHAR(1) DEFAULT 'N'," +       // 자동 로그인 설정
                "LOCATION_DOMAIN VARCHAR(50));");               // 쇼핑몰 도메인 설정

        db.execSQL("CREATE TABLE USER_LIST (" +
                "IDX INTEGER PRIMARY KEY, " +       // 시퀀스 번호
                "USER_NAME VARCHAR(20), " +
                "USER_PHONE VARCHAR(20), " +
                "USER_PHONE1 VARCHAR(3), " +
                "USER_PHONE2 VARCHAR(4), " +
                "USER_PHONE3 VARCHAR(4), " +
                "USER_SEX VARCHAR(1), " +
                "USER_CARDNO VARCHAR(16));");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        Log.d("TAG", "TAG : " + oldVersion + ", "+ newVersion);
        String sql = "drop table if exists USER_SETTING";
        db.execSQL(sql);

        sql = "drop table if exists USER_LIST";
        db.execSQL(sql);

        onCreate(db);
    }


    public void insert(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    public String[][] selectQuery(String Query){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);

        String[][] returnData = null;

        if (cursor != null && cursor.getCount() != 0){
            cursor.moveToFirst();
            returnData = new String[cursor.getCount()][cursor.getColumnCount()];

            for (int i = 0 ; i < cursor.getCount() ; i++) {
                for (int j = 0 ; j < cursor.getColumnCount() ; j++) {
                    returnData[i][j] = cursor.getString(j);
                }
                cursor.moveToNext();
            }
        }


        return returnData;
    }

    public HashMap<String, String> select(String fromParam, String[] selectParams, HashMap<String, String> whereParams) throws IOException {
        SQLiteDatabase db = getReadableDatabase();
        HashMap<String, String> rtnSelect = new HashMap<String, String>();

        String rstSelect    = castSelectParams(selectParams);
        String rstWhere     = castWhereParams(whereParams);

        String sql = "SELECT "+ rstSelect +" FROM "+ fromParam + rstWhere;

//        Log.d("dev_test", "sql : "+ sql);

        Cursor cursor = db.rawQuery(sql, null);
        while(cursor.moveToNext()) {
            for (int i = 0; i < selectParams.length ; i++)
                rtnSelect.put(selectParams[i], cursor.getString(i));
        }
        return rtnSelect;
    }

    public void update(String fromParam, HashMap<String, String> updateParams, HashMap<String, String> whereParams) throws IOException {
        SQLiteDatabase db = getReadableDatabase();

        String rstUpdate = castUpdateParams(updateParams);
        String rstWhere = castWhereParams(whereParams);

        String sql = "update "+ fromParam +" set "+ rstUpdate + rstWhere;

        db.execSQL(sql);
        db.close();
    }

    public void delete(String fromParam, HashMap<String, String> whereParams) throws IOException {
        SQLiteDatabase db = getReadableDatabase();

        String rstWhere = castWhereParams(whereParams);

        String sql = "delete from "+ fromParam + rstWhere;
        Log.d("dev_test", "sql : "+ sql);
        db.execSQL(sql);
        db.close();
    }

    public static String castSelectParams(String[] params){
        StringBuilder sb = new StringBuilder() ;

        for(int i = 0;i < params.length; i++){
            if(i > 0 )
                sb.append(",");
            sb.append(" "+ params[i]);
        }
        return sb.toString();
    }

    public static String castUpdateParams(HashMap<String, String> params) throws UnsupportedEncodingException {
        if( params == null )
        {
            return "" ;
        }
        StringBuilder sb = new StringBuilder() ;

        for( Iterator<String> i = params.keySet().iterator()   ; i.hasNext()  ;  )
        {
            String key = (String) i.next();
            sb.append(key);
            sb.append(" = ");
//            sb.append(URLEncoder.encode(String.valueOf(params.get(key)), "UTF-8"));
            sb.append(String.valueOf(params.get(key)));
            if (i.hasNext())
                sb.append(" , ");
        }
        return sb.toString();
    }
    public static String castWhereParams(HashMap<String, String> params) throws IOException
    {
        if( params == null )
        {
            return "" ;
        }
        StringBuilder sb = new StringBuilder() ;

        if(params.keySet().iterator().hasNext())
            sb.append(" where ");

        for( Iterator<String> i = params.keySet().iterator()   ; i.hasNext()  ;  )
        {
            String key = (String) i.next();
            sb.append(key);
            sb.append(" = ");
            sb.append("'"+ URLEncoder.encode(String.valueOf(params.get(key)), "UTF-8") +"'");
            if (i.hasNext())
                sb.append(" and ");
        }
        return sb.toString();
    }
}
