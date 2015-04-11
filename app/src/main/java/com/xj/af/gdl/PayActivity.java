package com.xj.af.gdl;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xj.af.R;
import com.xj.af.common.BaseActivity;

import com.xj.af.common.BaseBackActivity;
import com.xj.af.entity.Unit;
import com.xj.af.util.JsonUtil;
import com.xj.af.util.PayUtils;
import com.xj.af.util.StrUtil;
import com.xj.af.util.ThreadUtil;
import com.xj.af.util.http.PostUtil;


import java.util.HashMap;
import java.util.Map;

public class PayActivity extends BaseBackActivity{
    private String id;
    private Handler handler;
    private	TextView tv;
    private	TextView tv2;
    private TextView tv3;
    private EditText huixiang_editText;
    private Button  submitBtn;
    private EditText account;
    private EditText num;
    private EditText money;
    private String type;
    private String newsId;
    private Handler postHandler;

    private Handler alipayHandler;
    String orderInfo = "";
    String subject = "";
    String price = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_pay);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.title_bar);
        tv = (TextView)this.findViewById(R.id.pay_textView1);
        tv2 = (TextView)this.findViewById(R.id.pay_textView2);
        tv3 = (TextView)this.findViewById(R.id.pay_result_textView3);
        huixiang_editText = (EditText)findViewById(R.id.huixiang_editText);
        submitBtn = (Button)findViewById(R.id.pay_submit_button1);
        account = (EditText)findViewById(R.id.pay_account_editText);
        num = (EditText)findViewById(R.id.pay_num_editText);
        money = (EditText)findViewById(R.id.pay_money_editText);
        Intent it = this.getIntent();
        type = it.getStringExtra("type");//alipay   bank
        id = it.getStringExtra("id");
        newsId = it.getStringExtra("newsId");
        subject = (String)it.getStringExtra("subject");
        price = (String)it.getStringExtra("price");
        title = subject+"-支付";
        if(StrUtil.isBlank(type))
            type = "alipay";
        if(type.equals("alipay")){
            account.setVisibility(View.GONE);
            num.setVisibility(View.GONE);
        }else{
            account.setVisibility(View.VISIBLE);
            num.setVisibility(View.VISIBLE);
        }
        handler =  new MyHandly();
        postHandler = new PostHandler();
        String url = getServerURL()+"/api/unit/"+getUnitId();
        ThreadUtil tu = new ThreadUtil(handler, url);
        if(!type.equals("alipay")){
            tu.start();
        }else{
            tv.setText("支付宝支付");
            String s = "\n商品名称："+subject+"\n";
            float f = new Float(price);
            if(f!=0){
                money.setText(price);
                money.setEnabled(false);
            }
            tv2.setText(s);
        }
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
                            PostThread pt = new PostThread();
                            pt.start();
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
                        PayUtils.pay(PayActivity.this,alipayHandler,orderInfo);
                        break;
                    }
                    default:
                        break;
                }
            }
        };

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(type.equals("alipay")){

                }else{
                    if(StrUtil.xiaoYu(account.getText().toString(),2)){
                        Toast.makeText(PayActivity.this, "开户名输入错误", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(StrUtil.xiaoYu(num.getText().toString(),8) || !StrUtil.isNumeric(num.getText().toString())){
                        Toast.makeText(PayActivity.this, "账户输入错误", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                if(StrUtil.isBlank(money.getText().toString()) || !StrUtil.isNumeric(money.getText().toString())){
                    Toast.makeText(PayActivity.this, "金额入错误", Toast.LENGTH_LONG).show();
                    return;
                }
                if(StrUtil.daYu(huixiang_editText.getText().toString(), 100)){
                    Toast.makeText(PayActivity.this, "功德回向不能超过100个字", Toast.LENGTH_SHORT).show();
                    return;
                }
                //
                if(type.equals("alipay")){
                    orderInfo = PayUtils.getOrderInfo(PayActivity.this,subject,subject,money.getText().toString(),getUsername());
                    PayUtils.checkAndPay(PayActivity.this,alipayHandler);
                }else{
                    PostThread pt = new PostThread();
                    pt.start();
                }
            }
        });
    }


    class PostHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            Bundle bd = msg.getData();
            String imsg = bd.getString("imsg");
            String emsg = bd.getString("emsg");
            if(StrUtil.isNotBlank(imsg) && imsg.startsWith("success")){
                account.setVisibility(View.GONE);
                num.setVisibility(View.GONE);
                huixiang_editText.setVisibility(View.GONE);
                money.setVisibility(View.GONE);
                tv3.setVisibility(View.VISIBLE);
                tv3.setText("数据提交成功");
                submitBtn.setText("返回");
                submitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        finish();
                    }
                });
            }else{
                Toast.makeText(PayActivity.this, "数据提交失败:"+emsg, Toast.LENGTH_LONG).show();
            }
        }

    }
    class PostThread extends Thread{
        @Override
        public void run() {
            String address = getServerURL()+"/api/shopsy/accountMoney/create";
            Message msg = new Message();
            Bundle data = new Bundle();
            Map<String,String> map = new HashMap<String,String>();
            try {
                if(type.equals("alipay")){
                    map.put("alipay", PayUtils.SELLER);
                    int outtradeNoIndex = orderInfo.indexOf("out_trade_no=");
                    map.put("tradeNo", orderInfo.substring(outtradeNoIndex+14,outtradeNoIndex+29));
                    map.put("money", money.getText().toString());
                }else{
                    map.put("bankAccount", account.getText().toString());
                    map.put("bankNum", account.getText().toString());
                    map.put("money", money.getText().toString());
                }
                map.put("huixiang",huixiang_editText.getText().toString() );
                map.put("shopingsyId", id);
                map.put("newsSimple.id", newsId);
                String str = PostUtil.postData(address, map);
                data.putString("imsg", str);
            }catch(Exception e){
                e.printStackTrace();
                data.putString("emsg", e.getMessage());
            }
            msg.setData(data);
            postHandler.sendMessage(msg);
        }
    }

    class MyHandly extends Handler{
        @Override
        public void handleMessage(Message msg) {
            Bundle bd = msg.getData();
            String emsg = bd.getString("emsg");
            String imsg = bd.getString("imsg");
            if(StrUtil.isNotBlank(emsg)){
                Toast.makeText(PayActivity.this, "错误："+emsg, Toast.LENGTH_LONG).show();
            }
            if(StrUtil.isNotBlank(imsg)){
                Unit unit = JsonUtil.getEntity(imsg, Unit.class);
                String str = "您选择了银行转账，我们的银行信息如下：\n开户行："+unit.getBank()+"\n开户行名称："+unit.getBlankName()
                        +"\n银行账号："+unit.getBlankNum();
                tv2.setText("\n您可以到柜台或通过网银进行转账，请把您的银行信息填写到下面的表单，以便让我们核对您的信息。");
                tv.setText(str);
            }
        }

    }

}
