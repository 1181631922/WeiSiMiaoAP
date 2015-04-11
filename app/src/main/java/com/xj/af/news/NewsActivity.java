package com.xj.af.news;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xj.af.R;
import com.xj.af.common.BaseBackFragmentActivity;
import com.xj.af.util.StrUtil;

import cn.sharesdk.framework.ShareSDK;


/**
 * 显示新闻，需要title，id，url
 */
public class NewsActivity extends BaseBackFragmentActivity implements NewsFragment.OnFragmentInteractionListener{

    private WebView webView;
    private String url;
    private long id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean flag = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_news);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
        isShowShare = false;
        String StrTitle = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
        if(StrUtil.isNotBlank(StrTitle)){
            title = StrTitle;
        }else{
            title = "";
        }
        webView = (WebView) this.findViewById(R.id.webView_news);
        //isShowShare = true;
        Intent intent = this.getIntent();
        long id = intent.getLongExtra("id",0);
        url = id==0?url:(getServerURL()+ "/m/news/newsDetail/"+id+"?userName="+getUsername());

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.addJavascriptInterface(new Object(){
            @JavascriptInterface
            public void click(final int i,final int shopId,final String picPath,final long newsId) {
                Intent it = new Intent();
                it.putExtra("id", shopId+"");
                it.putExtra("picPath", picPath);
                it.putExtra("newsId", newsId+"");
                Log.d("xj", "NewsActivity:id:" + shopId);
                startActivity(it);
            }
        }, "android");
        webView.loadUrl(url);
    }

    @Override
    public void onFragmentInteraction(String str) {
       //url = str;
    }


}
