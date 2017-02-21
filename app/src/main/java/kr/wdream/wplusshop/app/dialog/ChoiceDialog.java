package kr.wdream.wplusshop.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import kr.wdream.wplusshop.R;
import kr.wdream.wplusshop.app.PayActivity;


/**
 * Created by deobeuldeulim on 2016. 10. 26..
 */

public class ChoiceDialog extends Dialog {
    private Context context;

    private Button btnPay;
    private Button btnSave;

    private String point;

    public ChoiceDialog(Context context) {
        super(context);
    }

    public ChoiceDialog(Context context, String point){
        super(context);
        this.context = context;
        this.point = point;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choise);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        initView();
    }

    private void initView(){
        btnPay = (Button)findViewById(R.id.btn_pay);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PayActivity.class);
                intent.putExtra("STATE", PayActivity.STATE_PAY);
                intent.putExtra("point", point);
                context.startActivity(intent);

                dismiss();
            }
        });
        btnSave = (Button)findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PayActivity.class);
                intent.putExtra("STATE", PayActivity.STATE_SAVE);
                intent.putExtra("point", point);
                context.startActivity(intent);

                dismiss();
            }
        });
    }
}
