package kr.wdream.wplusshop.common.util;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.HashMap;

import kr.wdream.wplusshop.common.Metrics;

/**
 * Created by deobeuldeulim on 2017. 2. 28..
 */

public class GetPointTask extends AsyncTask {
    private HashMap<String,String> paramPoint = new HashMap<String,String>();
    private HashMap<String,String> resultPoint = new HashMap<String,String>();

    private Handler handler;
    private String cardNo;

    private Bundle bundle = new Bundle();
    private Message msg = new Message();

    public GetPointTask(String cardNo, Handler handler){
        this.cardNo = cardNo;
        this.handler = handler;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        paramPoint.put("cmd", "info");
        paramPoint.put("member_cardno", cardNo);
    }

    @Override
    protected Object doInBackground(Object[] params) {
        resultPoint = PointUtil.getPointResult(paramPoint);
        bundle.putSerializable("resultPoint", resultPoint);

        msg.what = Metrics.CARD_POINT_FAILED;

        if ("0000".equals(resultPoint.get("code"))) {
            msg.what = Metrics.CARD_POINT_SUCCESS;
        }
        msg.setData(bundle);

        handler.sendMessage(msg);


        return null;
    }
}
