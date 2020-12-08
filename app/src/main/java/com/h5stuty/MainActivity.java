package com.h5stuty;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    WebView webView;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button)findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //调用H5无参无返回值方法
                webView.loadUrl("javascript:show()");
                //调用H5有参方法
                webView.loadUrl("javascript:alertMsg('哈哈')");
                String content = "9880";
                webView.loadUrl("javascript:alertMsg(\""+content+"\")");
                //调用H5有返回值方法
                webView.evaluateJavascript("sum(1,2)",new ValueCallback() {
                    @Override
                    public void onReceiveValue(Object value) {
                        Toast.makeText(MainActivity.this,"js返回结果为=" + value, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


        webView = (WebView)findViewById(R.id.wv_webview);
        loadWeb();
    }

    @SuppressLint("JavascriptInterface")
    public void loadWeb(){

        String url =  "file:///android_asset/index.html";
        //String url =  "https://www.baidu.com/";
        //此方法可以在webview中打开链接而不会跳转到外部浏览器
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);

        //支持弹窗，也就是支持html网页弹框
        webView.setWebChromeClient(new WebChromeClient(){
            public boolean onJsAlert(WebView view,String url,String message,JsResult result ){
                return super.onJsAlert(view,url,message,result);
            }
        });

        //支持html中javascript解析，不管是不是js和android交互，只要网页中含有js都要
        webView.getSettings().setJavaScriptEnabled(true);
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //重写onKeyDown，当浏览网页，WebView可以后退时执行后退操作。
        if(keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()){
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        //重写onKeyDown，当浏览网页，WebView可以后退时执行后退操作。
        if(keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()){
            webView.goBack();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}
