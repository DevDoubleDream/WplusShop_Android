package kr.wdream.wplusshop.app.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import kr.wdream.wplusshop.R;

/**
 * Created by deobeuldeulim on 2017. 2. 21..
 */

public class NFCCheckDialog extends AlertDialog {

    //View 선언
    private LinearLayout lytNotice;
    private RelativeLayout lytMain;
    private Button btnSubmit;

    //Animation
    private Animation ani;

    //Animation을 위한 Handler
    private Handler handler = new Handler();

    //생성자
    public NFCCheckDialog(Context context) {
        super(context);
    }

    //onCreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_check_nfc);

        initView();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ani = AnimationUtils.loadAnimation(getContext(), R.anim.intro_fade_out);
                ani.setFillAfter(true);

                lytNotice.startAnimation(ani);
            }
        }, 1000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ani = AnimationUtils.loadAnimation(getContext(), R.anim.intro_fade_in);
                ani.setFillAfter(true);

                lytMain.setVisibility(View.VISIBLE);
                lytMain.startAnimation(ani);
            }
        }, 1000);
    }

    private void initView(){
        lytNotice = (LinearLayout)findViewById(R.id.lyt_notice);
        lytMain   = (RelativeLayout) findViewById(R.id.lyt_main);

        btnSubmit = (Button)findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
