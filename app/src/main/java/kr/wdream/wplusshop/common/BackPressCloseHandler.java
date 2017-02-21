package kr.wdream.wplusshop.common;


import android.app.Activity;
import android.widget.Toast;

import kr.wdream.wplusshop.app.Main;

/**
 * Created by SEO on 2015-12-17.
 */

public class BackPressCloseHandler {
    private long backKeyPressedTime = 0;
    private Toast toast;

    private Activity activity;


    public BackPressCloseHandler(Activity context) {
        this.activity = context;
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            for (int i = 0; i < Main.locationHistory.size(); i++) {
                Main.locationHistory.get(i).finish();  //List가 Static 이므로, Class명.변수명.get으로 접근
            }
            toast.cancel();
        }
    }

    private void showGuide() {
        toast = Toast.makeText(activity, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }

}
