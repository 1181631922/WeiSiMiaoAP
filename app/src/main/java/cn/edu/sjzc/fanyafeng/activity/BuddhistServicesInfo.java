package cn.edu.sjzc.fanyafeng.activity;

import android.app.AlertDialog;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xj.af.R;
import com.xj.af.common.BaseBackActivity;

public class BuddhistServicesInfo extends BaseBackActivity implements View.OnClickListener {

    private TextView bsi_title, bsi_detail, bsi_first, bsi_second, bsi_third;
    private Button bsi_but;
    private String info_title, bs_api,newsId,money;
    private WebView wv;
    private Handler handler;
    private ProgressBar bs_info_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean flag = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_buddhist_services_info);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);

        initView();
        initData();
        loadurl(wv, bs_api);
    }

    private void initView() {
        this.wv = (WebView) BuddhistServicesInfo.this.findViewById(R.id.bs_info_webview);
        this.bs_info_progress = (ProgressBar) BuddhistServicesInfo.this.findViewById(R.id.bs_info_progress);
        this.bsi_but = (Button) BuddhistServicesInfo.this.findViewById(R.id.bsi_but);
        this.bsi_but.setOnClickListener(this);
        Intent it = this.getIntent();
        info_title = it.getStringExtra("btitle_info");
        title = info_title;
        bs_api = it.getStringExtra("bapi_info");
        newsId = it.getStringExtra("newsId");
        money = it.getStringExtra("money");
    }

    public void initData() {
        handler = new Handler() {
            public void handleMessage(Message msg) {
                if (!Thread.currentThread().isInterrupted()) {
                    switch (msg.what) {
                        case 0:
                            bs_info_progress.showContextMenu();
                            break;
                        case 1:
                            bs_info_progress.setVisibility(View.GONE);
                            break;
                    }
                }
                super.handleMessage(msg);
            }
        };
        wv = (WebView) findViewById(R.id.bs_info_webview);
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
        AlertDialog.Builder ad = new AlertDialog.Builder(BuddhistServicesInfo.this);
        ad.setTitle("退出");
        ad.setMessage("是否返回到主页?");
        ad.setPositiveButton("是", new DialogInterface.OnClickListener() {// 退出按钮
            @Override
            public void onClick(DialogInterface dialog, int i) {
                // TODO Auto-generated method stub
                BuddhistServicesInfo.this.finish();// 关闭activity

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_listed_on, menu);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bsi_but:
                Intent it_sign_up = new Intent(BuddhistServicesInfo.this, SignUpActivity.class);
                it_sign_up.putExtra("su_title", info_title);
                it_sign_up.putExtra("newsId",newsId);
                it_sign_up.putExtra("money",money);
                startActivity(it_sign_up);
                break;
            default:
                break;
        }

    }
}
