package kr.wdream.wplusshop.common.list;

/**
 * Created by SEO on 2016-04-21.
 */
public class NoticeListVO {

    private static int _totalCNT;
    private static int _pageNo;
    private static int _pageSize;

    private String _subject;
    private String _content;
    private String _create_dt;


    public static int get_totalCNT() {
        return _totalCNT;
    }

    public static void set_totalCNT(int _totalCNT) {
        NoticeListVO._totalCNT = _totalCNT;
    }

    public static int get_pageNo() {
        return _pageNo;
    }

    public static void set_pageNo(int _pageSize) {
        NoticeListVO._pageNo = _pageSize;
    }

    public static int get_pageSize() {
        return _pageSize;
    }

    public static void set_pageSize(int _pageSize) {
        NoticeListVO._pageSize = _pageSize;
    }



    public NoticeListVO(String subject, String content, String create_dt){
        _subject = subject;
        _content = content;
        _create_dt = create_dt;
    }

    public String get_subject() {
        return _subject;
    }

    public String get_content() {
        return _content;
    }

    public String get_create_dt() {
        return _create_dt;
    }
}
