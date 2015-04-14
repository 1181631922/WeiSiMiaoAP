package com.xj.af.gdl;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.xj.af.R;
import com.xj.af.common.BaseActivity;
import com.xj.af.common.BaseBackActivity;
import com.xj.af.persion.PersionActivity;
import com.xj.af.util.BitmapHelper;
import com.xj.af.util.PayUtils;
import com.xj.af.util.StrUtil;

public class ShopsyDetailActivity extends BaseBackActivity {
    private String id ;
    private Handler handler;
    private WebView webView;
    private ImageView imageView;
    private String picPath;
    private String newsId;
    private Bitmap bm;
    private Button buttonPayAliOnline;
    private Button buttonPayAliForward;
    private Button buttonPayBank;
    PopupWindow pop;
    private boolean logined = false;

    private Handler alipayHandler;
    String orderInfo = "";
    String subject;
    String price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean flag = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_shopsy_detail);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);

        logined = getLogined();
        webView = (WebView)findViewById(R.id.shopsyDetail_webView1);
        imageView=(ImageView)findViewById(R.id.shopsyDetail_imageView1);
        buttonPayAliOnline = (Button)findViewById(R.id.shopsy_pay_alipay_onlien);
        buttonPayAliForward = (Button)findViewById(R.id.shopsy_pay_alipay_foward);
        buttonPayBank = (Button)findViewById(R.id.shopsy_pay_bank);

        Intent intent = this.getIntent();
        //
        id = intent.getStringExtra("id");
        //
        picPath = intent.getStringExtra("picPath");
        newsId = intent.getStringExtra("newsId");
        subject = intent.getStringExtra("subject");
        //金额
        price = intent.getStringExtra("price");
        title = subject;
        if(!getAlipayEnable().equals("true")){
            buttonPayAliForward.setVisibility(View.GONE);
        }
        //子线程获取图片
        Runnable r = new LodeImgRunable();
        Thread t = new Thread(r);
        t.start();
        //web控件显示详细
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                view.getSettings().setBlockNetworkImage(false);
                return true;
            }
        });
        webView.loadUrl(getUrl("/m/shopsy/detail/")+id);
        //更新图片
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what == 1){
                    imageView.setImageBitmap(bm);
                }
            }
        };
        //支付
        alipayHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case PayUtils.SDK_PAY_FLAG: {
                        Result resultObj = new Result((String) msg.obj);
                        String resultStatus = resultObj.resultStatus;
                        // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                        if (TextUtils.equals(resultStatus, "9000")) {
                            Toast.makeText(getApplicationContext(), "支付成功",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // 判断resultStatus 为非“9000”则代表可能支付失败
                            // “8000” 代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                            if (TextUtils.equals(resultStatus, "8000")) {
                                Toast.makeText(getApplicationContext(), "支付结果确认中",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "支付失败",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                    }
                    case PayUtils.SDK_CHECK_FLAG: {
                        boolean b = (Boolean)msg.obj;
                        final String paymethod = "expressGateway";//启用银行卡支付，如果checkAccountIfExist返回false可使用次参数
                        if(!b){
                            orderInfo += "&paymethod="+paymethod;
                        }
                        PayUtils.pay(ShopsyDetailActivity.this,alipayHandler,orderInfo);
                        break;
                    }
                    default:
                        break;
                }
            }
        };

        //支付宝直接支付，需要登陆
        buttonPayAliOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(!logined){
                    Toast.makeText(ShopsyDetailActivity.this, "请登陆", Toast.LENGTH_LONG).show();
                    Intent it = new Intent(ShopsyDetailActivity.this,PersionActivity.class);
                    startActivity(it);
                    return;
                }
                orderInfo = PayUtils.getOrderInfo(ShopsyDetailActivity.this,"测试商品","测试商品详细","0.01",getUsername());
                PayUtils.checkAndPay(ShopsyDetailActivity.this,alipayHandler);
            }
        });
        //支付宝转账，需要登陆并输入支付宝账号
        buttonPayAliForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(logined){
                    Intent it = new Intent(ShopsyDetailActivity.this,PayActivity.class);
                    it.putExtra("type", "alipay");
                    it.putExtra("id", id);
                    it.putExtra("newsId", newsId);
                    it.putExtra("subject", subject);
                    it.putExtra("price", price);
                    startActivity(it);
                }else{
                    Intent it = new Intent(ShopsyDetailActivity.this,PersionActivity.class);
                    startActivity(it);
                }
            }
        });
        //银行转账，需要登陆并输入打款人账号
        buttonPayBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(logined){
                    Intent it = new Intent(ShopsyDetailActivity.this,PayActivity.class);
                    it.putExtra("type", "bank");
                    it.putExtra("id", id);
                    it.putExtra("newsId", newsId);
                    it.putExtra("subject", subject);
                    it.putExtra("price", price);
                    startActivity(it);
                }else{
                    Intent it = new Intent(ShopsyDetailActivity.this,PersionActivity.class);
                    startActivity(it);
                }
            }
        });

    }


    class LodeImgRunable implements Runnable{
        @Override
        public void run() {
            if(StrUtil.isNotBlank(picPath)){
                Message message = new Message();
                if(!picPath.startsWith("http")){
                    picPath = getServerURL()+picPath;
                }
                bm = BitmapHelper.getBitmap(picPath);
                message.what = 1;
                handler.sendMessage(message);
            }
        }
    }

}
