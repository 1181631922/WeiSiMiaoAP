package cn.edu.sjzc.fanyafeng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xj.af.R;
import com.xj.af.common.BaseBackActivity;
import com.xj.af.gdl.Result;
import com.xj.af.util.PayUtils;
import com.xj.af.util.StrUtil;
import com.xj.af.util.ThreadUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * 佛事报名界面
 */
public class SignUpActivity extends BaseBackActivity implements View.OnClickListener {

    private TextView su_title,signUp_result;
    private EditText su_name_et, su_phone_et, su_detail_et;
    private Button su_submit_but,signUp_backBtn;
    private String sutitle,newsId,money;
    private Handler alipayHandler;
    private String orderInfo = "";
    private Handler handler;
    private LinearLayout signup_input_linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean flag = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_sign_up);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
        initView();
        initDate();
    }

    private void initView() {
        this.su_title = (TextView) SignUpActivity.this.findViewById(R.id.su_title);
        this.su_name_et = (EditText) SignUpActivity.this.findViewById(R.id.su_name_et);
        this.su_phone_et = (EditText) SignUpActivity.this.findViewById(R.id.su_phone_et);
        this.su_detail_et = (EditText) SignUpActivity.this.findViewById(R.id.su_detail_et);
        this.su_submit_but = (Button) SignUpActivity.this.findViewById(R.id.su_submit_but);
        //////布局增加返回按钮，成功提示
        signup_input_linearLayout = findLinearLayout(R.id.signup_input_linearLayout);
        signUp_result = findTextView(R.id.signUp_result);
        signUp_backBtn = findButton(R.id.signUp_backBtn);
        this.su_submit_but.setOnClickListener(this);

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
                            Toast.makeText(getApplicationContext(), "支付成功",Toast.LENGTH_SHORT).show();
                            //支付成功后提交表单
                            submit();
                        } else {
                            // 判断resultStatus 为非“9000”则代表可能支付失败
                            // “8000” 代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                            if (TextUtils.equals(resultStatus, "8000")) {
                                Toast.makeText(getApplicationContext(), "支付结果确认中",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "支付失败",Toast.LENGTH_SHORT).show();
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
                        PayUtils.pay(SignUpActivity.this,alipayHandler,orderInfo);
                        break;
                    }
                    default:
                        break;
                }
            }
        };
    }

    private void initDate() {
        Intent it = this.getIntent();
        sutitle = it.getStringExtra("su_title");
        title = "报名" + sutitle;
        this.su_title.setText(sutitle);
        ///增加付款对应的佛事服务新闻id，金额
        newsId = it.getStringExtra("newsId");//新闻id
        money = it.getStringExtra("money");
        if(StrUtil.isNotBlank(money) && new Float(money).floatValue() < 0){
            su_detail_et.setText("");
        }else{
            su_detail_et.setText(money);
            su_detail_et.setEnabled(false);
        }
    }

    /**
     * 不需要支付或支付成功后提交表单
     */
    private void submit(){
        //--------------------------------------------------------------------
        Message msg = new Message();
        Bundle bd = new Bundle();
        msg.setData(bd);
        Map map = new HashMap();
        map.put("money",su_detail_et.getText().toString());
        map.put("alipay", PayUtils.SELLER);
        int outtradeNoIndex = orderInfo.indexOf("out_trade_no=");
        map.put("tradeNo",orderInfo.substring(outtradeNoIndex+14,outtradeNoIndex+29));
        map.put("newsSimple.id", newsId);
        ThreadUtil tu = new ThreadUtil(handler,getServerURL() + "/api/shopsy/accountMoney/create", map);
        tu.start();
    }

    /**
     * 提交表单后更新界面
     */
    class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bd = msg.getData();
            String emsg = bd.getString("emsg");
            String imsg = bd.getString("imsg");
            if (StrUtil.isNotBlank(emsg)) {
                Toast.makeText(getApplicationContext(),emsg,Toast.LENGTH_SHORT).show();
                return;
            } else if (StrUtil.isNotBlank(imsg)) {
                //保存成功
                if (StrUtil.isNotBlank(imsg)) {
                    signUp_result.setText(imsg);
                }
                signUp_backBtn.setVisibility(View.VISIBLE);
                signUp_result.setVisibility(View.VISIBLE);
                signup_input_linearLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.su_submit_but:
                //TODO:此处需要判断是否登录
                if(StrUtil.isNotBlank(money) && new Float(money).floatValue()!=0) {
                    //如果需要支付则创建订单，提交支付宝
                    String subject = su_name_et.getText().toString();
                    orderInfo = PayUtils.getMyOrderInfo(SignUpActivity.this, sutitle+"-"+subject, sutitle+"-"+subject, su_detail_et.getText().toString(), getUsername());
                    PayUtils.checkAndPay(SignUpActivity.this, alipayHandler);
                }else {
                    //不需要支付，直接提交报名信息
                    submit();
                }
                break;
            case R.id.signUp_backBtn:
                finish();
            default:
                break;
        }
    }
}
