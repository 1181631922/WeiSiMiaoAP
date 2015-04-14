package cn.edu.sjzc.fanyafeng.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.view.KeyEvent;
import android.app.AlertDialog;

import com.xj.af.R;
import com.xj.af.common.BaseBackActivity;
import com.xj.af.util.SingleImageTaskUtil;

import java.util.logging.LogRecord;

public class EventInfoActivity extends BaseBackActivity implements View.OnClickListener {
    private ImageView event_info_img;
    private TextView event_info_name, event_info_time, event_info_detail;
    private Button event_info_submit_but;
    private String eventinfo_img, eventinfo_name, eventinfo_detail, eventinfo_time,eventinfo_api,newsId,money;
    private ProgressDialog mDialog;
    private ProgressBar event_info_progress;
    private boolean isUse = true;
    private WebView wv;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean flag = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_event_info);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);


        initView();
        initData();
        loadurl(wv, eventinfo_api);
    }


    private void initView() {

        Intent it = this.getIntent();
        eventinfo_name = it.getStringExtra("event_title");
        title = eventinfo_name;
        eventinfo_api = it.getStringExtra("event_api");
        newsId = it.getStringExtra("event_newsId");
        money = it.getStringExtra("event_money");
        this.event_info_progress = (ProgressBar)EventInfoActivity.this.findViewById(R.id.event_info_progress);
        this.event_info_submit_but = (Button) EventInfoActivity.this.findViewById(R.id.event_info_submit_but);
        this.event_info_submit_but.setOnClickListener(this);


    }

    public void initData() {
        handler = new Handler() {
            public void handleMessage(Message msg) {
                if (!Thread.currentThread().isInterrupted()) {
                    switch (msg.what) {
                        case 0:
                            event_info_progress.showContextMenu();;
                            break;
                        case 1:
                            event_info_progress.setVisibility(View.GONE);
                            break;
                    }
                }
                super.handleMessage(msg);
            }
        };
        wv = (WebView) findViewById(R.id.event_info_webview);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setScrollBarStyle(0);
        wv.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(final WebView view,
                                                    final String url) {
                loadurl(view, url);
                return true;
            }

        });

        wv.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {// 载入进度改变而触发
                if (progress == 100) {
                    handler.sendEmptyMessage(1);// 如果全部载入,隐藏进度对话框
                }
                super.onProgressChanged(view, progress);
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {// 捕捉返回键
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wv.canGoBack()) {
            wv.goBack();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            ConfirmExit();// 按了返回键，但已经不能返回，则执行退出确认
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void ConfirmExit() {// 退出确认
        AlertDialog.Builder ad = new AlertDialog.Builder(EventInfoActivity.this);
        ad.setTitle("退出");
        ad.setMessage("是否返回到主页?");
        ad.setPositiveButton("是", new DialogInterface.OnClickListener() {// 退出按钮
            @Override
            public void onClick(DialogInterface dialog, int i) {
                EventInfoActivity.this.finish();// 关闭activity

            }
        });
        ad.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                // 不退出不用执行任何操作
            }
        });
        ad.show();// 显示对话框
    }

    public void loadurl(final WebView view, final String url) {
                handler.sendEmptyMessage(0);
                view.loadUrl(url);// 载入网页
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.event_info_submit_but:
                Intent it_event_apply = new Intent(EventInfoActivity.this,EventApplyActivity.class);
                it_event_apply.putExtra("eventapply_title",eventinfo_name);
                it_event_apply.putExtra("newsId",newsId);
                it_event_apply.putExtra("money",money);
                startActivity(it_event_apply);
                break;
            default:
                break;
        }

    }

}
