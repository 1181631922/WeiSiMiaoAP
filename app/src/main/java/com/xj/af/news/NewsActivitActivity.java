package com.xj.af.news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xj.af.R;
import com.xj.af.common.BaseActivity;
import com.xj.af.common.BaseBackActivity;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;


public class NewsActivitActivity extends BaseBackActivity {

    private WebView webView;
    private String url;
    private long id;
    private String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean flag = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_news_activit);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
        isShowShare = true;
        Intent intent = this.getIntent();
        id = intent.getLongExtra("id",0);
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");

        url = id==0?url:(getServerURL()+ "/m/news/newsDetail/"+id+"?userName="+getUsername());
        webView = (WebView)findViewById(R.id.webView_news);
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
    protected void onResume() {
        isShowShare = true;
        super.onResume();
    }

    public void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字
        oks.setNotification(R.drawable.ico, getString(R.string.app_name2));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(title);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        //oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name2));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(getServerURL());
        // 启动分享GUI
        oks.show(this);
    }
}
