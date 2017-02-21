package kr.wdream.wplusshop.common;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by SEO on 2015-12-28.
 */
public class OAuthWebClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        // WebView의 페이지 로드가 완료되면 콜백의 형태로 이 메쏘드가 호출됩니다.. 좀 더 정확하게는 WebView가 이벤트 발생하는 경우 WebViewClient의 선언된 메쏘드들을 호출하고, 요 형태는 전형적인 옵저버 패턴의 모습입니다.
    }
}