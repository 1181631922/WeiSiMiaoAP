package com.xj.af.index.jieyuan;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xj.af.R;
import com.xj.af.common.BaseBackActivity;
import com.xj.af.common.Constant;
import com.xj.af.gdl.PayActivity;
import com.xj.af.gdl.Result;
import com.xj.af.util.PayUtils;
import com.xj.af.util.StrUtil;
import com.xj.af.util.ThreadUtil;

import java.util.HashMap;
import java.util.Map;

public class ZhuyinDetailActivity extends BaseBackActivity implements View.OnClickListener{
    private EditText jieyuan_detail_huixiang;
    private EditText jieyuan_detail_phonenumber;
    private EditText jieyuan_detail_money;
    private Button jieyuan_submit;
    private TextView jieyuan_errormsg ;
    private TextView jieyuan_result;
    private TextView jieyuan_des;
    private LinearLayout jieyuan_input_layout;
    private Button jieyuan_back_btn;
    private Handler handler;
    private long jingShuId;//id
    private String name;//经书
    private String picPath;//图片路径
    private String des;//经书描述
    private Handler alipayHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean flag = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_zhuyin_detail);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
        title = "助印";
        init();
        SharedPreferences sharedata = getSharedPreferences(Constant.USER_DATA, 0);
        jieyuan_detail_phonenumber.setText(sharedata.getString(Constant.USER_DATA_PHONENUM,""));
    }
    private String orderInfo = "";
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.jieyuan_back_btn){
            this.finish();
        }else {
            String str = checkInput();
            if(StrUtil.isBlank(str)) {
                String subject = "经书助印";
                orderInfo = PayUtils.getMyOrderInfo(ZhuyinDetailActivity.this, subject, subject, jieyuan_detail_money.getText().toString(), getUsername());
                PayUtils.checkAndPay(ZhuyinDetailActivity.this, alipayHandler);
            }else{
                jieyuan_errormsg.setText(str);
            }
        }
    }

    class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bd = msg.getData();
            String emsg = bd.getString("emsg");
            String imsg = bd.getString("imsg");
            if (StrUtil.isNotBlank(emsg)) {
                jieyuan_errormsg.setText(emsg);
                return;
            } else if (StrUtil.isNotBlank(imsg)) {
                //保存成功
                if (StrUtil.isNotBlank(imsg)) {
                    jieyuan_result.setText(imsg);
                }
                jieyuan_back_btn.setVisibility(View.VISIBLE);
                jieyuan_result.setVisibility(View.VISIBLE);
                jieyuan_input_layout.setVisibility(View.GONE);
            }
        }
    }


    private String checkInput(){
        String flag = "";
        if(!StrUtil.isMobileNO(jieyuan_detail_phonenumber.getText().toString())){
            flag = ("电话填写错误");
        }else
        if((!StrUtil.isNumeric(jieyuan_detail_money.getText().toString())) || StrUtil.isBlank(jieyuan_detail_money.getText().toString())){
            flag = ("金额填写错误");
        }
        return flag;
    }

    private void submit(){
        //--------------------------------------------------------------------
        Message msg = new Message();
        Bundle bd = new Bundle();
        msg.setData(bd);
        SharedPreferences.Editor sharedata = getSharedPreferences(Constant.USER_DATA, 0).edit();
        sharedata.putString(Constant.USER_DATA_PHONENUM, jieyuan_detail_phonenumber.getText().toString());
        sharedata.commit();
        Map map = new HashMap();
        map.put("money", jieyuan_detail_money.getText().toString());
        map.put("phoneNum", jieyuan_detail_phonenumber.getText().toString());
        map.put("huiXiang", jieyuan_detail_huixiang.getText().toString());
        map.put("jieYuanJingShu.id", String.valueOf(jingShuId));
        map.put("type", "2");
        ThreadUtil tu = new ThreadUtil(handler,getServerURL() + "/api/jieyuan/add/zydingdan", map);
        tu.start();
    }

    private void init(){
        jieyuan_detail_huixiang = (EditText) findViewById(R.id.jieyuan_detail_huixiang);
        jieyuan_detail_phonenumber =  (EditText)findViewById(R.id.jieyuan_detail_phonenumber);
        jieyuan_detail_money =  (EditText)findViewById(R.id.jieyuan_detail_money);
        jieyuan_submit = (Button)findViewById(R.id.zhuyin_alpayBtn);
        jieyuan_errormsg = (TextView)findViewById(R.id.jieyuan_errormsg);
        jieyuan_result = (TextView)findViewById(R.id.jieyuan_result);
        jieyuan_input_layout = (LinearLayout)findViewById(R.id.jieyuan_input_layout);
        jieyuan_des = (TextView)findViewById(R.id.jieyuan_des);
        jieyuan_back_btn = findButton(R.id.jieyuan_back_btn);

        Intent it = this.getIntent();
        jingShuId = it.getLongExtra("id",0l);
        name = it.getStringExtra("title");
        des = it.getStringExtra("des");
        picPath = it.getStringExtra("picPath");

        handler = new MyHandler();

        jieyuan_des.setText(name+":"+des);
        jieyuan_submit.setOnClickListener(this);
        jieyuan_back_btn.setOnClickListener(this);

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
                           submit();
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
                        PayUtils.pay(ZhuyinDetailActivity.this,alipayHandler,orderInfo);
                        break;
                    }
                    default:
                        break;
                }
            }
        };

    }
}
