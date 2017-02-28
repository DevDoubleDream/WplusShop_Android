package kr.wdream.wplusshop.common;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by deobeuldeulim on 2017. 2. 28..
 */

public class PxToDp {
    private static Context c;

    public PxToDp(Context context){
        c = context;
    }
    public static int pxToDp(Context context, int pixel){
        c = context;
        Resources resources = c.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();

        int dp = (int)(pixel / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));

        return dp;
    }

    public static int dpToPx(Context context, int dp){
        c = context;
        float scale = c.getResources().getDisplayMetrics().density;
        int px = (int) (dp * scale + 0.5f);

        return px;
    }
}
