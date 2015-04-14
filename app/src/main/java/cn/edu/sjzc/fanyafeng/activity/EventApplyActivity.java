package cn.edu.sjzc.fanyafeng.activity;

import android.app.Activity;
import android.content.Intent;
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
import com.xj.af.gdl.Result;
import com.xj.af.util.PayUtils;
import com.xj.af.util.StrUtil;
import com.xj.af.util.ThreadUtil;

import java.util.HashMap;
import java.util.Map;

public class EventApplyActivity extends BaseBackActivity implements View.OnClickListener {
    private TextView ea_title, eventApply_result;
    private EditText ea_name_et, ea_phone_et, ea_detail_et;
    private Button ea_submit_but, apply_backBtn;
    private String eatitle, newsId, money, event_apply_title;
    private Handler alipayHandler;
    private String orderInfo = "";
    private Handler handler;
    private LinearLayout eventapply_input_linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean flag = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_event_apply);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
        initView();
        initData();
    }

    private void initView() {
        this.ea_title = (TextView) EventApplyActivity.this.findViewById(R.id.ea_title);
        this.ea_name_et = (EditText) EventApplyActivity.this.findViewById(R.id.ea_name_et);
        this.ea_phone_et = (EditText) EventApplyActivity.this.findViewById(R.id.ea_phone_et);
        this.ea_detail_et = (EditText) EventApplyActivity.this.findViewById(R.id.ea_detail_et);
        this.ea_submit_but = (Button) EventApplyActivity.this.findViewById(R.id.ea_submit_but);

        eventapply_input_linearLayout = findLinearLayout(R.id.eventapply_input_linearLayout);
        eventApply_result = findTextView(R.id.eventApply_result);
        apply_backBtn = findButton(R.id.apply_backBtn);
        this.ea_submit_but.setOnClickListener(this);


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
                        PayUtils.pay(EventApplyActivity.this,alipayHandler,orderInfo);
                        break;
                    }
                    default:
                        break;
                }
            }
        };
    }

    private void initData() {
        Intent it = this.getIntent();
        event_apply_title = it.getStringExtra("eventapply_title");
        title = "参加" + event_apply_title;
        ///增加付款对应的佛事服务新闻id，金额
        newsId = it.getStringExtra("newsId");//新闻id
        money = it.getStringExtra("money");
        if(StrUtil.isNotBlank(money) && new Float(money).floatValue() < 0){
            ea_detail_et.setText("");
        }else{
            ea_detail_et.setText(money);
            ea_detail_et.setEnabled(false);
        }
    }

    private void submit() {
        //--------------------------------------------------------------------
        Message msg = new Message();
        Bundle bd = new Bundle();
        msg.setData(bd);
        Map map = new HashMap();
        map.put("money", ea_detail_et.getText().toString());
        map.put("alipay", PayUtils.SELLER);
        int outtradeNoIndex = orderInfo.indexOf("out_trade_no=");
        map.put("tradeNo", orderInfo.substring(outtradeNoIndex + 14, outtradeNoIndex + 29));
        map.put("newsSimple.id", newsId);
        ThreadUtil tu = new ThreadUtil(handler, getServerURL() + "/api/shopsy/accountMoney/create", map);
        tu.start();
    }

    /**
     * 提交表单后更新界面
     */
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bd = msg.getData();
            String emsg = bd.getString("emsg");
            String imsg = bd.getString("imsg");
            if (StrUtil.isNotBlank(emsg)) {
                Toast.makeText(getApplicationContext(), emsg, Toast.LENGTH_SHORT).show();
                return;
            } else if (StrUtil.isNotBlank(imsg)) {
                //保存成功
                if (StrUtil.isNotBlank(imsg)) {
                    eventApply_result.setText(imsg);
                }
                apply_backBtn.setVisibility(View.VISIBLE);
                eventApply_result.setVisibility(View.VISIBLE);
                eventapply_input_linearLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_apply, menu);
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
            case R.id.ea_submit_but:
                if (StrUtil.isNotBlank(money) && new Float(money).floatValue() != 0) {
                    //如果需要支付则创建订单，提交支付宝
                    String subject = ea_name_et.getText().toString();
                    orderInfo = PayUtils.getMyOrderInfo(EventApplyActivity.this, eatitle + "-" + subject, eatitle + "-" + subject, ea_detail_et.getText().toString(), getUsername());
                    PayUtils.checkAndPay(EventApplyActivity.this, alipayHandler);
                    break;
                }
            case R.id.apply_backBtn:
                finish();
                break;
            default:
                break;

        }
    }

}