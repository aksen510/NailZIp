package com.example.nailzip.member;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nailzip.R;

public class SearchAddressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_address);

        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new BridgeInterface(), "Android");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url){
                // 안드로이드 -> 자바스크립트 호출
                webView.loadUrl("javascript:sample2_execDaumPostcode();");
            }
        });

        // 최초의 웹뷰 로드
        webView.loadUrl("https://nailzip-21eea.web.app/");
    }

    private class BridgeInterface {
        @JavascriptInterface
        public void processDATA(String data){
            //카카오 주소 검색 API결과 간의 브릿지 통로를 통해 전달(from javascript)
            Intent intent=new Intent();
            intent.putExtra("data",data);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}