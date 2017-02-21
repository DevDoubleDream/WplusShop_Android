package kr.wdream.wplusshop.common.list;

/**
 * Created by SEO on 2015-12-17.
 */
public class StoreListVO {

    private static int _totalCNT;
    private static int _pageNo;
    private static int _pageSize;

    private String _stbiztype;
    private String _stbizitem;
    private String _stnm;
    private String _stphone;
    private String _staddr1;
    private String _staddr2;
    private String _point_xx;
    private String _point_yy;


    public static int get_totalCNT() {
        return _totalCNT;
    }

    public static void set_totalCNT(int _totalCNT) {
        StoreListVO._totalCNT = _totalCNT;
    }

    public static int get_pageNo() {
        return _pageNo;
    }

    public static void set_pageNo(int _pageSize) {
        StoreListVO._pageNo = _pageSize;
    }

    public static int get_pageSize() {
        return _pageSize;
    }

    public static void set_pageSize(int _pageSize) {
        StoreListVO._pageSize = _pageSize;
    }

    public String get_stbiztype() {
        return _stbiztype;
    }

    public String get_stbizitem() {
        return _stbizitem;
    }

    public String get_stnm() {
        return _stnm;
    }

    public String get_stphone() {
        return _stphone;
    }

    public String get_staddr1() {
        return _staddr1;
    }

    public String get_staddr2() {
        return _staddr2;
    }

    public StoreListVO(String stbiztype, String stbizitem, String stnm, String stphone, String staddr1, String staddr2, String point_xx, String point_yy){
        _stbiztype = stbiztype;
        _stbizitem = stbizitem;
        _stnm = stnm;
        _stphone = stphone;
        _staddr1 = staddr1;
        _staddr2 = staddr2;
        _point_xx = point_xx;
        _point_yy = point_yy;
    }

    public String get_point_xx() {
        return _point_xx;
    }

    public String get_point_yy() {
        return _point_yy;
    }
}

