package kr.wdream.wplusshop.common;

/**
 * Created by user on 2016-02-22.
 */
import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;

import kr.wdream.wplusshop.R;


public class CDialog {
    private static Dialog m_loadingDialog = null;

    public static void hideLoading() {
        if (m_loadingDialog != null) {
            m_loadingDialog.dismiss();
            m_loadingDialog = null;
        }
    }

    public static boolean showLoading(Context context) {
        if (m_loadingDialog == null) {
            m_loadingDialog = new Dialog(context, R.style.TransDialog);
            ProgressBar pb = new ProgressBar(context);
            LayoutParams params = new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            m_loadingDialog.addContentView(pb, params);
            m_loadingDialog.setCancelable(false);
        }

        m_loadingDialog.show();
        return true;
    }
}


