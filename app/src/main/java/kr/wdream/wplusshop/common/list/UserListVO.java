package kr.wdream.wplusshop.common.list;

/**
 * Created by SEO on 2015-12-28.
 */
public class UserListVO {

    private static int _totalCNT;
    private static int _pageNo;
    private static int _pageSize
            ;
    private String _member_idx;
    private String _member_name;
    private String _member_hphone;
    private String _member_hphone1;
    private String _member_hphone2;
    private String _member_hphone3;
    private String _member_cardno;
    private String _member_sex;


    public static int get_totalCNT() {
        return _totalCNT;
    }

    public static void set_totalCNT(int _totalCNT) {
        UserListVO._totalCNT = _totalCNT;
    }

    public static int get_pageNo() {
        return _pageNo;
    }

    public static void set_pageNo(int _pageNo) {
        UserListVO._pageNo = _pageNo;
    }

    public static int get_pageSize() {
        return _pageSize;
    }

    public static void set_pageSize(int _pageSize) {
        UserListVO._pageSize = _pageSize;
    }

    public String get_member_name() {
        return _member_name;
    }

    public String get_member_hphone1() {
        return _member_hphone1;
    }

    public String get_member_hphone2() {
        return _member_hphone2;
    }

    public String get_member_hphone3() {
        return _member_hphone3;
    }

    public String get_member_cardno() {
        return _member_cardno;
    }

    public String get_member_sex() {
        return _member_sex;
    }

    public String get_member_hphone() {
        return _member_hphone;
    }

    public String get_member_idx() {
        return _member_idx;
    }

    public UserListVO(String member_idx, String member_name, String member_hphone1, String member_hphone2, String member_hphone3, String member_cardno, String member_sex){
        _member_idx = member_idx;
        _member_name = member_name;
        _member_hphone1 = member_hphone1;
        _member_hphone2 = member_hphone2;
        _member_hphone3 = member_hphone3;
        _member_hphone  = member_hphone1 +"-"+ member_hphone2 +"-"+ member_hphone3;
        _member_cardno = member_cardno;
        _member_sex = member_sex;
    }


}

