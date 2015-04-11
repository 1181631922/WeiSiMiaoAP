package com.xj.af.wenda;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.xj.af.R;
import com.xj.af.common.BaseActivity;
import com.xj.af.common.Constant;


public class QuestionAddActivity extends BaseActivity {
    private WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean flag =  requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_question_add);

        mWebView = (WebView) findViewById(R.id.questionAdd_webView);
       // title = "答疑解惑";
        String url = getServerURL()+ "/m/wenda/";
        Intent it = this.getIntent();
        String username = it.getStringExtra("username");
        String action = it.getStringExtra("action");
        String query = "";
        if(username!=null && !"".equals(username)){
            query = "?search_EQ_loginName="+username;
            if("question".equals(action)){
                query += "&search_EQ_unitId="+getUnitId();
                url += getUnitId()+query;
            }else if("huifu".equals(action)){
                url += "answer"+query;
            }
        }else{
            url += getUnitId();
        }
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.addJavascriptInterface(new Object(){
            @JavascriptInterface
            public void login(String username,String password){
                SharedPreferences.Editor sharedata = getSharedPreferences(Constant.LOGIN_DATA, 0).edit();
                sharedata.putString(Constant.LOGIN_DATA_USERNAME,username);
                sharedata.putString(Constant.LOGIN_DATA_PASSWORD,password);
                sharedata.commit();
            }
        }, "android");

        mWebView.loadUrl(url);
        Log.d("xj",url);

    }

    /**
     * 判断详细页面单击返回键回退到列表而不是关掉activity
     */
    @Override
    public void onBackPressed() {
        String url = mWebView.getUrl();
        if(url == null){
            super.onBackPressed();
            return;
        }
        if(url.indexOf("m_logined/wenda/detail")!=-1 || url.indexOf("/login")!=-1)
            mWebView.loadUrl("javascript:history.back(-1);");
        else
            super.onBackPressed();
    }

    private class UIHandler extends Handler{

    }
}
