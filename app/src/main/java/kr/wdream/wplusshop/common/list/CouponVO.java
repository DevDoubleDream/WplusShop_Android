package kr.wdream.wplusshop.common.list;

/**
 * Created by deobeuldeulim on 2016. 10. 24..
 */

public class CouponVO {
    private String cp_nm;
    private String cp_sdt;
    private String cp_edt;
    private String cp_id;
    private String cp_no;
    private String cp_pub;
    private String cp_pub_nm;
    private String cp_photo;

    private String code;
    private String message;

    public static String totalCount;

    public String get_cp_nm(){return cp_nm;}
    public String get_cp_sdt(){return cp_sdt;}
    public String get_cp_edt(){return cp_edt;}
    public String get_cp_id(){return cp_id;}
    public String get_cp_no(){return cp_no;}
    public String get_cp_pub(){return cp_pub;}
    public String get_cp_pub_nm(){return cp_pub_nm;}
    public String get_cp_photo(){return cp_photo;}

    public String get_code(){return code;}
    public String get_message(){return message;}


    public CouponVO(String code, String message, String cp_nm,String cp_sdt, String cp_edt, String cp_id, String cp_no, String cp_pub, String cp_pub_nm, String cp_photo){
        this.cp_nm = cp_nm;
        this.cp_sdt = cp_sdt;
        this.cp_edt = cp_edt;
        this.cp_id = cp_id;
        this.cp_no = cp_no;
        this.cp_pub = cp_pub;
        this.cp_pub_nm = cp_pub_nm;
        this.cp_photo = cp_photo;

        this.code = code;
        this.message = message;
    }
}
