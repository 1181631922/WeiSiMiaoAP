package cn.edu.sjzc.fanyafeng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xj.af.R;
import com.xj.af.common.BaseBackActivity;

public class SignUpActivity extends BaseBackActivity implements View.OnClickListener{

    private TextView su_title;
    private EditText su_name_et, su_phone_et, su_detail_et;
    private Button su_submit_but;
    private String sutitle;

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
        this.su_detail_et =(EditText) SignUpActivity.this.findViewById(R.id.su_detail_et);
        this.su_submit_but = (Button) SignUpActivity.this.findViewById(R.id.su_submit_but);
        this.su_submit_but.setOnClickListener(this);
    }

    private void initDate(){
        Intent it = this.getIntent();
        sutitle = it.getStringExtra("su_title");
        title = "报名"+sutitle;
        this.su_title.setText(sutitle);

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

    }
}
