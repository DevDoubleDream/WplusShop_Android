package kr.wdream.wplusshop.common;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.HashMap;

import kr.wdream.wplusshop.common.util.StoreAuthUtil;

/**
 * Created by deobeuldeulim on 2017. 2. 1..
 */

public class AuthTask extends AsyncTask {
    public static final int DIALOG_START = 100;
    public static final int DIALOG_STOP  = 101;
    public static final int TASK_SUCCESS = 200;
    public static final int TASK_FAILED  = 201;

    private Handler handler;

    private String authKey;
    private String stCode;
    private String code;
    private String msg;

    private HashMap<String,String> paramAuth  = new HashMap<String, String>();
    private HashMap<String,String> resultAuth = new HashMap<String, String>();

    public AuthTask(Handler handler, String authKey, String stCode){
        this.handler = handler;
        this.authKey = authKey;
        this.stCode  = stCode;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        handler.sendEmptyMessage(DIALOG_START);

        paramAuth.put("cmd", "crtfctn");
        paramAuth.put("auth_key", authKey);
        paramAuth.put("stcd", stCode);


    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            resultAuth = StoreAuthUtil.getStoreAuth(paramAuth);

            stCode = resultAuth.get("stCode");
            code = resultAuth.get("code");

            Bundle bundle = new Bundle();
            bundle.putString("stCode", stCode);
            bundle.putString("code", code);
            bundle.putString("msg", msg);

            Message msg = new Message();
            msg.what = TASK_SUCCESS;
            msg.setData(bundle);

            handler.sendMessage(msg);

        } catch (Exception e) {
            e.printStackTrace();
            handler.sendEmptyMessage(TASK_FAILED);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        handler.sendEmptyMessage(DIALOG_STOP);
    }
}
