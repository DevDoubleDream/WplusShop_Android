package kr.wdream.wplusshop.common;

/**
 * Created by deobeuldeulim on 2016. 10. 21..
 */

public class CardInfo {
    public static String cardNo;
    public static String cardPw;
    public static String point;
    public static String userNo;
    public static String userSex;


    public static void putCardNo(String putCardNo){cardNo = putCardNo;}
    public static void putCardPw(String putCardPw){cardPw = putCardPw;}
    public static void putPoint(String putPoint){point = putPoint;}
    public static void putUserNo(String putUserNo){userNo = putUserNo;}
    public static void putUserSex(String putUserSex){userSex = putUserSex;}

    public static String getCardNo(){ return cardNo;}
    public static String getCardPw(){ return cardPw;}
    public static String getPoint(){ return point;}
    public static String getUserNo(){ return userNo;}
    public static String getUserSex(){ return userSex;}

}

