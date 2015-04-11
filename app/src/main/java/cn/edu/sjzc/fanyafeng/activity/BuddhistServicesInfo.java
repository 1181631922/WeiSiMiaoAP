package cn.edu.sjzc.fanyafeng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.xj.af.R;
import com.xj.af.common.BaseBackActivity;

public class BuddhistServicesInfo extends BaseBackActivity implements View.OnClickListener {

    private TextView bsi_title, bsi_detail, bsi_first, bsi_second, bsi_third;
    private Button bsi_but;
    private String info_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean flag = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_buddhist_services_info);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);

        initView();
        initData();
    }

    private void initView() {
        this.bsi_title = (TextView) BuddhistServicesInfo.this.findViewById(R.id.bsi_title);
        this.bsi_detail = (TextView) BuddhistServicesInfo.this.findViewById(R.id.bsi_detail);
        this.bsi_first = (TextView) BuddhistServicesInfo.this.findViewById(R.id.bsi_first);
        this.bsi_second = (TextView) BuddhistServicesInfo.this.findViewById(R.id.bsi_second);
        this.bsi_third = (TextView) BuddhistServicesInfo.this.findViewById(R.id.bsi_third);
        this.bsi_but = (Button) BuddhistServicesInfo.this.findViewById(R.id.bsi_but);
        this.bsi_but.setOnClickListener(this);
    }

    private void initData() {
        Intent it = this.getIntent();
        info_title = it.getStringExtra("btitle_info");
        this.bsi_title.setText(info_title);
        title = info_title;
        String info_detail = it.getStringExtra("bdetail_info");
        String info_first = it.getStringExtra("bfirst_info");
        String info_second = it.getStringExtra("bsecond_info");
        String info_third = it.getStringExtra("bthird_info");
        this.bsi_detail.setText(info_detail);
        this.bsi_first.setText(info_first);
        this.bsi_second.setText(info_second);
        this.bsi_third.setText(info_third);


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
                it_sign_up.putExtra("su_title",info_title);
                startActivity(it_sign_up);
                break;
            default:
                break;
        }

    }
}
