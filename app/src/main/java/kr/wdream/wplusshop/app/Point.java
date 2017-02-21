package kr.wdream.wplusshop.app;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.HashMap;

import kr.wdream.wplusshop.R;
import kr.wdream.wplusshop.common.PointUtil;
import kr.wdream.wplusshop.common.util.WcardUtil;

/**
 * Created by SEO on 2015-12-17.
 */
public class Point extends Activity implements View.OnClickListener{
    public String Tag = "Point";
    private int pageNum = 1;     // 페이지 번호
    private int pageSize = 5;
    DecimalFormat df = new DecimalFormat("#,##0");

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.point);

        Main.locationHistory.add(this);

        ((LinearLayout) findViewById(R.id.icon_head_left)).setOnClickListener(this);
//        ((Button)findViewById(R.id.btn_next)).setOnClickListener(this);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        // 회원 카드번호 및 포인트
        String cpoint = "";
        String card_no = "";
        HashMap<String, String> wcardParams = new HashMap<String, String>();
        wcardParams.put("cmd", "info");
        wcardParams.put("member_no", Main.userInfo.get("user_no"));
        wcardParams.put("member_cardno", Main.userInfo.get("card_no"));
        HashMap<String, String> wcardResult = WcardUtil.getWcardResult(wcardParams);

        if(wcardResult.get("code").equals("0000"))
            cpoint = wcardResult.get("CPOINT");

        StringBuffer sb = new StringBuffer(Main.userInfo.get("card_no"));
        sb.insert(4, " ").insert(9, " ").insert(14," ");
        card_no = sb.toString();
//        ((TextView)findViewById(R.id.txt_cardpoint)).setText(df.format(Integer.parseInt(cpoint)));

        // 포인트 내역 조회
        HashMap<String, String> pointParams = new HashMap<String, String>();
        pointParams.put("cmd", "info");
        pointParams.put("member_no", Main.userInfo.get("user_no"));
        pointParams.put("member_cardno", Main.userInfo.get("card_no"));
        HashMap<String, String> pointList = PointUtil.getPointResult(pointParams);

        ((TextView)findViewById(R.id.txt_cpoint)).setText(df.format(Integer.parseInt(pointList.get("CP"))));
        ((TextView)findViewById(R.id.txt_wpoint)).setText(df.format(Integer.parseInt(pointList.get("WP"))));
        ((TextView)findViewById(R.id.txt_hpoint)).setText(df.format(Integer.parseInt(pointList.get("HP"))));
        ((TextView)findViewById(R.id.txt_ipoint)).setText(df.format(Integer.parseInt(pointList.get("IP"))));
        ((TextView)findViewById(R.id.txt_tpoint)).setText(df.format(Integer.parseInt(pointList.get("TP"))));
        ((TextView)findViewById(R.id.txt_ppoint)).setText(df.format(Integer.parseInt(pointList.get("PP"))));
        ((TextView)findViewById(R.id.txt_fpoint)).setText(df.format(Integer.parseInt(pointList.get("FP"))));
//        ButtonCommon.setClick(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.icon_head_left:
                finish();
                break;
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
//        ButtonCommon.setClickLogin(this);
    }
}
