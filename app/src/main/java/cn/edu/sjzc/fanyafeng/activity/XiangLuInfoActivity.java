package cn.edu.sjzc.fanyafeng.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xj.af.R;
import com.xj.af.common.BaseBackActivity;

public class XiangLuInfoActivity extends BaseBackActivity implements View.OnClickListener {
    private ImageView xianglu_info_img;
    private TextView xianglu_info_name, xianglu_info_time, xianglu_info_detail;
    private Button xianglu_info_submit_but;
    private String xiangluinfo_img, xiangluinfo_name, xiangluinfo_detail, xiangluinfo_time, xiangluinfo_api, newsId, money;
    private ProgressDialog mDialog;
    private ProgressBar xianglu_info_progress;
    private boolean isUse = true;
    private WebView wv;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean flag = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_xiang_lu_info);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
        initView();
        initData();
        loadurl(wv, xiangluinfo_api);
    }

    private void initView() {

        Intent it = this.getIntent();
        xiangluinfo_name = it.getStringExtra("xiangluName");
        title = xiangluinfo_name;
        xiangluinfo_api = it.getStringExtra("xiangluApi");
        this.xianglu_info_progress = (ProgressBar) XiangLuInfoActivity.this.findViewById(R.id.xianglu_info_progress);
        this.xianglu_info_submit_but = (Button) XiangLuInfoActivity.this.findViewById(R.id.xianglu_info_submit_but);
        this.xianglu_info_submit_but.setOnClickListener(this);
    }

    public void initData() {
        handler = new Handler() {
            public void handleMessage(Message msg) {
                if (!Thread.currentThread().isInterrupted()) {
                    switch (msg.what) {
                        case 0:
                            xianglu_info_progress.showContextMenu();
                            break;
                        case 1:
                            xianglu_info_progress.setVisibility(View.GONE);
                            break;
                    }
                }
                super.handleMessage(msg);
            }
        };
        wv = (WebView) findViewById(R.id.xianglu_info_webview);
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
        AlertDialog.Builder ad = new AlertDialog.Builder(XiangLuInfoActivity.this);
        ad.setTitle("退出");
        ad.setMessage("是否返回到主页?");
        ad.setPositiveButton("是", new DialogInterface.OnClickListener() {// 退出按钮
            @Override
            public void onClick(DialogInterface dialog, int i) {
                XiangLuInfoActivity.this.finish();// 关闭activity

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

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_xiang_lu_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
