package kr.wdream.wplusshop.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import kr.wdream.wplusshop.R;
import kr.wdream.wplusshop.app.CouponActivity;
import kr.wdream.wplusshop.app.PayActivity;

/**
 * Created by deobeuldeulim on 2017. 1. 17..
 */

public class NFCDialog extends Dialog {
    private String point;
    private int state;
    private boolean isNFC;

    private Button btnNFC;
    private Button btnQR;

    private OnDismissListener listener;

    public NFCDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_choise);

        btnNFC = (Button)findViewById(R.id.btn_pay);
        btnNFC.setText("NFC");
        btnNFC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNFC = true;
                listener.onDismiss(NFCDialog.this);
            }
        });

        btnQR = (Button)findViewById(R.id.btn_save);
        btnQR.setText("QR코드");

        btnQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNFC = false;
                listener.onDismiss(NFCDialog.this);
            }
        });
    }

    public void setOnDismissListener(OnDismissListener listener){
        this.listener = listener;
    }

    public boolean returnValue(){
        Log.d("상은", "상은 is NFC : " + isNFC);
        return isNFC;
    }
}
