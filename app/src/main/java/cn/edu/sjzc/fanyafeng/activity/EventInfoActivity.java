package cn.edu.sjzc.fanyafeng.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xj.af.R;
import com.xj.af.common.BaseBackActivity;
import com.xj.af.util.SingleImageTaskUtil;

import java.util.logging.LogRecord;

public class EventInfoActivity extends BaseBackActivity implements View.OnClickListener {
    private ImageView event_info_img;
    private TextView event_info_name, event_info_time, event_info_detail;
    private Button event_info_submit_but;
    private String eventinfo_img, eventinfo_name, eventinfo_detail, eventinfo_time;
    private ProgressDialog mDialog;
    private boolean isUse = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean flag = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_event_info);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
        initView();
        initData();
    }


    private void initView() {
        this.event_info_img = (ImageView) EventInfoActivity.this.findViewById(R.id.event_info_img);
        this.event_info_name = (TextView) EventInfoActivity.this.findViewById(R.id.event_info_name);
        this.event_info_detail = (TextView) EventInfoActivity.this.findViewById(R.id.event_info_detail);
        this.event_info_time = (TextView) EventInfoActivity.this.findViewById(R.id.event_info_time);

        this.event_info_submit_but = (Button) EventInfoActivity.this.findViewById(R.id.event_info_submit_but);
        this.event_info_submit_but.setOnClickListener(this);
    }

    private void initData() {
        Intent it = this.getIntent();
        eventinfo_img = it.getStringExtra("eventimg_info");
        SingleImageTaskUtil imageTask = new SingleImageTaskUtil(this.event_info_img);
        imageTask.execute(eventinfo_img);
        eventinfo_name = it.getStringExtra("eventname_info");
        title = eventinfo_name;
        this.event_info_name.setText(eventinfo_name);
        eventinfo_time = it.getStringExtra("eventtime_info");
        this.event_info_time.setText(eventinfo_time);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.event_info_submit_but:
                mDialog = new ProgressDialog(EventInfoActivity.this);
                mDialog.setTitle("加载");
                mDialog.setMessage("正在获取网络数据。。。");
                mDialog.show();
                Thread loadDataThread = new Thread(new LoadDataThread());
                loadDataThread.start();


                break;
            default:
                break;
        }

    }

    class LoadDataThread implements Runnable {

        @Override
        public void run() {
            Message msg = handler.obtainMessage();
            if (isUse) {
                msg.what = 0;
                handler.sendMessage(msg);
            } else {
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mDialog.cancel();
                    Toast.makeText(getApplicationContext(), "加载成功！",Toast.LENGTH_SHORT).show();
                    Intent it_event_signup = new Intent(EventInfoActivity.this, EventSignUpActivity.class);
                    startActivity(it_event_signup);
                    break;
                case 1:
                    break;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_info, menu);
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
