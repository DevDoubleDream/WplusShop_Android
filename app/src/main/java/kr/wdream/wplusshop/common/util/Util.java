package kr.wdream.wplusshop.common.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


;

/**
 * Created by SEO on 2015-12-11.
 */
public class Util {

    public static String getRequestStr(String url, String method, HashMap<String, String> params) throws Exception  {
        // 문자를 저장할 StringBuilder.
        StringBuilder sb = new StringBuilder();
        // 인터넷을 연결할 커넥션.
        HttpURLConnection conn = null;

        // 읽어들일 Buffer 선언.
        BufferedReader br = null;
        String rstGetdata = "";
        InputStream inSt = null;
        OutputStream outSt = null ;

        String paramstr = buildParameters( params );
        if(paramstr != "")
            url += "?"+ paramstr;

        URL targetURL = new URL(url);

        Log.d("dev_test", "targetURL : " + targetURL);
        try {
            conn = (HttpURLConnection) targetURL.openConnection();

            // 연결하지 않을 때까지 반복.
            if (conn != null) {

                // 10초 시간 주기. ( 그때까지 연결하라는거. )
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setConnectTimeout(10000);
                conn.setUseCaches(false);
                conn.setRequestMethod(method);

                if (method.equals("POST"))
                {
                    // 데이터를 주소와 별개로 전송한다.
                    conn.setDoOutput(true);       /// 아웃풋 스트림 쓰기위에 아웃풋을 true로 켬
                    outSt = conn.getOutputStream();    /// 아웃풋 스트림 생성
                    outSt.write( paramstr.getBytes("UTF-8")) ;  /// UTF-8포멧으로 변경해서 변수를 쓴다.
                    outSt.flush() ;         /// 플러쉬~
                    outSt.close() ;         /// 스트림 닫기
                }

                // 연결이 만약 되었다면.
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    // BufferedReader에 입력 스트림 할당.
                    br = new BufferedReader(new InputStreamReader(
                            conn.getInputStream()));
                    // URL에 해당되는 문자열이 끝날때까지 반복.
                    for (;;) {
                        String line = br.readLine();
                        if (line == null)
                            break;
                        sb.append(line);
                    }
                    rstGetdata = sb.toString();
                    br.close();
                }
                conn.disconnect();
            }

            // URL 에러가 뜰 경우.
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

            // close는 반드시 finally로 묶어서 해준다. ( close를 하지 않으면 메모리 낭비 )
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
            if (conn != null) {
                try {
                    conn.disconnect();
                } catch (Exception e) {
                }
            }

        }// end try~catch~finally
        return rstGetdata;
    }

    /// 파라메터 받은 값을  "변수명=변수값&" 형식의 텍스트로 변환해주는 함수
    public static String buildParameters(HashMap<String, String> params) throws IOException
    {
        if( params == null )
        {
            return "" ;
        }
        StringBuilder sb = new StringBuilder( ) ;

        for( Iterator<String> i = params.keySet( ).iterator( )  ; i.hasNext( )  ;  )
        {
            String key = (String) i.next();
            sb.append(key);
            sb.append("=");
            sb.append(URLEncoder.encode(String.valueOf(params.get(key)), "UTF-8"));
            if (i.hasNext())
                sb.append("&");
        }
        return sb.toString();
    }

    public static void goBand(Context context, HashMap<String, String> params){
        String subject = "";
        String content = "";
        boolean packageCheck = false;

        subject = params.get("SUBJECT");
        content = params.get("TEXT");

        PackageManager manager = context.getPackageManager();
        Intent intent = null;

        List<ApplicationInfo> mInstalledPkgList = manager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        for( android.content.pm.ApplicationInfo a : mInstalledPkgList ){
            if( a.packageName.equals("com.nhn.android.band")){
                packageCheck = true;
            }
        }

//        subject = "subject_test";
//        content = "content_test";
        if(packageCheck) {
            intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
//            intent.setType("image/*");
//            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File("http://www.wpoint.co.kr/images/dist_shop/piain/wpointlogo.png")));
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, content);
            intent.setPackage("com.nhn.android.band");
            context.startActivity(intent);
        }else{
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.nhn.android.band"));
            context.startActivity(intent);
        }

    }

   /* public static void goKakao(Context context, HashMap<String, String> params){
        String title ="";
        String picture = "";
        title = params.get("TEXT");
        picture = params.get("PICTURE");

        KakaoLink mKakaoLink = null;
        KakaoTalkLinkMessageBuilder mKakaoTalkLinkMessageBuilder = null;

        try{
            mKakaoLink = KakaoLink.getKakaoLink(context);
            mKakaoTalkLinkMessageBuilder = mKakaoLink.createKakaoTalkLinkMessageBuilder();
        }catch(KakaoParameterException e){
            e.printStackTrace();
        }
        try{
            mKakaoTalkLinkMessageBuilder.addText(title);
            if(!picture.equals("")) {
                mKakaoTalkLinkMessageBuilder.addImage(picture, 128, 128);
            }
//                    mKakaoTalkLinkMessageBuilder.addText("http://www.wpoint.co.kr");
//                    mKakaoTalkLinkMessageBuilder.addWebLink("홈 페이지 이동", "http://www.wpoint.co.kr");
//                    mKakaoTalkLinkMessageBuilder.addAppButton("테스트 앱 열기", new AppActionBuilder()
//                            .setAndroidExecuteURLParam("target=main")
//                            .setIOSExecuteURLParam("target=main", AppActionBuilder.DEVICE_TYPE.PHONE).build());
            mKakaoLink.sendMessage(mKakaoTalkLinkMessageBuilder.build(), context);
        }catch(KakaoParameterException e){
            e.printStackTrace();
        }
    }*/


}
