package cn.edu.sjzc.fanyafeng.payactivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
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
import com.xj.af.gdl.Result;
import com.xj.af.util.PayUtils;
import com.xj.af.util.StrUtil;

import cn.edu.sjzc.fanyafeng.activity.SignUpActivity;

public class SignUpPayActivity extends BaseBackActivity implements View.OnClickListener {
    private Button signup_alipay, signup_bank,signup_back;
    private EditText signup_phone, signup_money;
    private String id;
    private Handler handler;
    private String orderInfo;
    private Handler alipayHandler;
    private TextView signup_result,signup_errormsg;
    private LinearLayout signup_input_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean flag = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_sign_up_pay);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
        title = "报名支付页面";
        initView();
//        initData();
        SharedPreferences sharedata = getSharedPreferences(Constant.USER_DATA, 0);
        signup_phone.setText(sharedata.getString(Constant.USER_DATA_PHONENUM, ""));

    }

    private void initView() {
        this.signup_input_layout = (LinearLayout) this.findViewById(R.id.signup_input_layout);
        this.signup_phone = (EditText) this.findViewById(R.id.signup_phone);
        this.signup_money = (EditText) this.findViewById(R.id.signup_money);
        this.signup_alipay = (Button) this.findViewById(R.id.signup_alipay);
        this.signup_alipay.setOnClickListener(this);
        this.signup_back = (Button) this.findViewById(R.id.signup_back);
        this.signup_back.setOnClickListener(this);
        this.signup_bank = (Button) this.findViewById(R.id.signup_bank);
        this.signup_bank.setOnClickListener(this);
        this.signup_result = (TextView) this.findViewById(R.id.signup_result);
        this.signup_errormsg = (TextView) this.findViewById(R.id.signup_errormsg);

    }

    private void initData() {
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
//                            submit();
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
                        PayUtils.pay(SignUpPayActivity.this,alipayHandler,orderInfo);
                        break;
                    }
                    default:
                        break;
                }
            }
        };

    }

    class MyHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bd = msg.getData();
            String emsg = bd.getString("emsg");
            String imsg = bd.getString("imsg");
            if (StrUtil.isNotBlank(emsg)) {
                signup_errormsg.setText(emsg);
                return;
            } else if (StrUtil.isNotBlank(imsg)) {
                //保存成功
                if (StrUtil.isNotBlank(imsg)) {
                    signup_result.setText(imsg);
                }
                signup_back.setVisibility(View.VISIBLE);
                signup_result.setVisibility(View.VISIBLE);
                signup_input_layout.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup_alipay:
                if (checkInPut()) {
                    String subject = "放生活动费用";
                    orderInfo = PayUtils.getMyOrderInfo(SignUpPayActivity.this, subject, subject, signup_money.getText().toString(), getUsername());
                    Log.d("--------------------------------------------------------------------------------------------------------------------","");
                    PayUtils.checkAndPay(SignUpPayActivity.this, alipayHandler);
                }else{

                }

                break;
            case R.id.signup_bank:
                break;
            default:
                break;
        }

    }

    private boolean checkInPut() {
        if (StrUtil.isMobileNO(signup_phone.getText().toString()) && StrUtil.isNumeric(signup_money.getText().toString())) {
            return true;
        } else {

            Toast.makeText(getApplicationContext(), "验证失败！",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sing_up_pay, menu);
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
