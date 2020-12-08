package com.h5stuty;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class H5CallAndroidActivity extends AppCompatActivity {
    private EditText etName;
    private EditText etPassword;
    private Button btnLogin;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h5_call_android);

        this.btnLogin = (Button) findViewById(R.id.btnLogin);
        this.etPassword = (EditText) findViewById(R.id.etPassword);
        this.etName = (EditText) findViewById(R.id.etName);
        //初始化WebView

        webView = (WebView)findViewById(R.id.wv_webview);
        loadWeb();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(password)) {//输入框判空
                    Toast.makeText(H5CallAndroidActivity.this,
                            "用户或密码不能为空!", Toast.LENGTH_SHORT).show();
                } else {
                    loginSuccess(name);
                }
            }
        });
    }
    /**
     * 登录成功后调用
     * @param name
     */
    private void loginSuccess(String name) {
        /**
         * 关键代码：
         * 我们要调用H5页面的Js代码(其实就是调用JS中的方法)
         * 就是通过这种形式：javascript:Js文件中的方法名(参数)
         * 下面的javaCallJs();必须是JS代码中方法
         * 如果参数是变量,就使用拼接字符串方式传参:如下↓↓↓
         */
        webView.loadUrl("javascript:javaCallJs('" + name + "')");
        //登录成功后将H5页面加载到本界面
        setContentView(webView);
    }
    @SuppressLint("JavascriptInterface")
    public void loadWeb(){

        String url =  "file:///android_asset/dj_index.html";
        webView = new WebView(this);
        WebSettings settings = webView.getSettings();
        //设置支持javaScript脚步语言
        settings.setJavaScriptEnabled(true);
        //支持双击-前提是页面要支持才显示
        settings.setUseWideViewPort(true);
        //支持缩放按钮-前提是页面要支持才显示
        settings.setBuiltInZoomControls(true);
        //此方法可以在webview中打开链接而不会跳转到外部浏览器
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);

        /*
          打开js接口，参数1为本地类名；参数2为别名
         */
        webView.addJavascriptInterface(new JsInteration(),"android");

        /**
         * Js中调用Android原生代码核心代码:
         * 该方法传入两个参数:1.被调用的类;2.JS中被调用类的实例对象名字
         * 对象名可以随意取,但是必须和H5页面中onclick的名字对应
         */
        webView.addJavascriptInterface(new AndroidAndJsInterface(), "Android");
    }




    public  class  JsInteration  {
        @JavascriptInterface//一定要写，不然h5调不到这个方法
        public  String  back() {
            return "hello world";
        }
    }

    /**
     * Js中调用的类
     */
    private class AndroidAndJsInterface {
        /**
         * Js中调用的方法
         */
        @JavascriptInterface
        public void showToast() {
            Toast.makeText(H5CallAndroidActivity.this,
                    "我是原生代码!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //重写onKeyDown，当浏览网页，WebView可以后退时执行后退操作。
        if(keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()){
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
