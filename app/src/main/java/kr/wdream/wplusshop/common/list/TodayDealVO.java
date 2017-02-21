package kr.wdream.wplusshop.common.list;

import android.graphics.Bitmap;

/**
 * Created by SEO on 2016-01-11.
 */
public class TodayDealVO {

    private static int _totalCNT;

    private String _prdt_nm;
    private String _sml_img_path;
    private Bitmap _mdl_img_path;
    private String _big_img_path;
    private String _prdt_path;


    public TodayDealVO(String prdt_nm, String sml_img_path, Bitmap mdl_img_path, String big_img_path, String prdt_path){
        _prdt_nm = prdt_nm;
        _sml_img_path = sml_img_path;
        _mdl_img_path = mdl_img_path;
        _big_img_path = big_img_path;
        _prdt_path = prdt_path;
    }

    public static int get_totalCNT() {
        return _totalCNT;
    }

    public static void set_totalCNT(int _totalCNT) {
        TodayDealVO._totalCNT = _totalCNT;
    }

    public String get_prdt_nm() {
        return _prdt_nm;
    }

    public String get_sml_img_path() {
        return _sml_img_path;
    }

    public Bitmap get_mdl_img_path() {
        return _mdl_img_path;
    }

    public String get_big_img_path() {
        return _big_img_path;
    }

    public String get_prdt_path() {
        return _prdt_path;
    }


}
