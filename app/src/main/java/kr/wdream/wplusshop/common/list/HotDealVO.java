package kr.wdream.wplusshop.common.list;

/**
 * Created by SEO on 2015-12-22.
 */
public class HotDealVO {

    private String _prdt_nm;
    private int _cty_mny;
    private int _sll_mny;
    private String _sml_img_path;
    private String _mdl_img_path;
    private String _big_img_path;
    private String _prdt_path;


    public HotDealVO(String prdt_nm, int cty_mny, int sll_mny, String sml_img_path, String mdl_img_path, String big_img_path, String prdt_path){
        _prdt_nm = prdt_nm;
        _cty_mny = cty_mny;
        _sll_mny = sll_mny;
        _sml_img_path = sml_img_path;
        _mdl_img_path = mdl_img_path;
        _big_img_path = big_img_path;
        _prdt_path = prdt_path;
    }

    public String get_prdt_nm() {
        return _prdt_nm;
    }

    public int get_cty_mny() {
        return _cty_mny;
    }

    public int get_sll_mny() {
        return _sll_mny;
    }

    public String get_sml_img_path() {
        return _sml_img_path;
    }

    public String get_mdl_img_path() {
        return _mdl_img_path;
    }

    public String get_big_img_path() {
        return _big_img_path;
    }

    public String get_prdt_path() {
        return _prdt_path;
    }
}
