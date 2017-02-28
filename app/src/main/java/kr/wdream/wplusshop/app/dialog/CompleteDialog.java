package kr.wdream.wplusshop.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import kr.wdream.wplusshop.R;
import kr.wdream.wplusshop.app.PayActivity;


/**
 * Created by deobeuldeulim on 2016. 10. 26..
 */

public class CompleteDialog extends Dialog {

    private TextView txtTitle;
    private TextView txtCardNo;
    private TextView txtUseSave;
    private TextView txtUseSave2;

    private TextView txtPoint;
    private TextView remainPoint;

    String cardNo;
    String point;
    String strRemainPoint;

    int STATE;
    private double totalPoint;

    public CompleteDialog(Context context) {
        super(context);
    }

    public CompleteDialog(Context context, String cardNo, int STATE, String point, String remainPoint){
        super(context);

        Log.d("상은", "상은 point : " + point);
        this.cardNo = cardNo;
        this.point = point;
        this.strRemainPoint = remainPoint;
        this.STATE = STATE;

        if (STATE == PayActivity.STATE_PAY) {
            totalPoint = Integer.parseInt(remainPoint) - Integer.parseInt(point);
            double savePoint = Integer.parseInt(point) * 0.055;
            totalPoint = totalPoint + savePoint;
        }else{
            totalPoint = Integer.parseInt(remainPoint) + (Integer.parseInt(point) * 0.055);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_complete);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        initView();
    }

    private void initView(){
        txtTitle = (TextView)findViewById(R.id.txt_title);

        txtCardNo = (TextView)findViewById(R.id.txt_card_no);
        txtCardNo.setText(cardNo);

        txtUseSave = (TextView)findViewById(R.id.txt_use_save);
        txtUseSave2 = (TextView)findViewById(R.id.txt_use_save2);
        if(STATE == PayActivity.STATE_PAY){
            txtTitle.setText("포인트 사용");
            txtUseSave.setText("사용정상처리 되었습니다.");
            txtUseSave2.setText("사용 포인트");
        }else{
            txtTitle.setText("포인트 적립");
            txtUseSave.setText("적립정상처리 되었습니다.");
            txtUseSave2.setText("적립 포인트");
        }
        txtPoint = (TextView)findViewById(R.id.txt_point);
        txtPoint.setText(point);
        remainPoint = (TextView)findViewById(R.id.txt_remain_point);
        remainPoint.setText(String.valueOf(totalPoint));
    }
}
