package cn.edu.sjzc.fanyafeng.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xj.af.R;
import com.xj.af.common.BaseBackActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.sjzc.fanyafeng.adapter.FoXiangAdapter;
import cn.edu.sjzc.fanyafeng.adapter.ZhuangShiAdapter;
import cn.edu.sjzc.fanyafeng.bean.FoXiangBean;
import cn.edu.sjzc.fanyafeng.bean.ZhuangShiBean;

public class FoXiangDingZhiActivity extends BaseBackActivity {
    private ImageView foxiang_img;
    private TextView foxiang_name, foxiang_detail, foxiang_phone;
    private ListView foxiang_listview;
    private ProgressBar foxiang_progress;
    private List<Map<String, Object>> foxiangList = new ArrayList<Map<String, Object>>();
    private boolean isNet;
    private static List<FoXiangBean> foxiangBeans;
    private FoXiangAdapter foxiangAdapter;
    private String SIYUANHUODOND = "/api/newssort/page/findByEnName/global_foXiangDingZhi";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean flag = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_fo_xiang_ding_zhi);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
        title = "佛像定制";
        initView();
        foxiang_progress.showContextMenu();
        Thread loadThread = new Thread(new LoadThread());
        loadThread.start();
    }
    class LoadThread implements Runnable {
        @Override
        public void run() {
            initData();
        }
    }
    private void initView() {
        this.foxiang_img = (ImageView) FoXiangDingZhiActivity.this.findViewById(R.id.foxiang_img);
        this.foxiang_name = (TextView) FoXiangDingZhiActivity.this.findViewById(R.id.foxiang_name);
        this.foxiang_detail = (TextView) FoXiangDingZhiActivity.this.findViewById(R.id.foxiang_detail);
        this.foxiang_phone = (TextView) FoXiangDingZhiActivity.this.findViewById(R.id.foxiang_phone);
        this.foxiang_listview = (ListView) FoXiangDingZhiActivity.this.findViewById(R.id.foxiang_listview);
        this.foxiang_progress = (ProgressBar) FoXiangDingZhiActivity.this.findViewById(R.id.foxiang_progress);
    }
    private void initData() {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet request;
        try {
            String SIYUANHUODOND_API_OLD = getServerURL() + SIYUANHUODOND;
            request = new HttpGet(new URI(SIYUANHUODOND_API_OLD));
            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String out = EntityUtils.toString(entity, "UTF-8");
                    Log.d("------------------------------------------------------------------------------------------->", out);
                    try {
                        JSONObject jsonObject = new JSONObject(out);
                        JSONArray eveArray = jsonObject.getJSONArray("content");
                        foxiangBeans = new ArrayList<FoXiangBean>();
                        for (int i = 0; i < eveArray.length(); i++) {
                            JSONObject eveobj = eveArray.getJSONObject(i);
                            String id = eveobj.getString("id");
                            String foxiangInfoAPI = getServerURL() + "/m/news/newsDetail/" + id;
                            String title = eveobj.getString("title");
                            String phone = eveobj.getString("money");
                            String smallPic = getServerURL() + eveobj.getString("smallPic");
                            String des = eveobj.getString("des");

                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("foxiangName", title);
                            map.put("foxiangApi", foxiangInfoAPI);
                            foxiangList.add(map);
                            foxiangBeans.add(new FoXiangBean(smallPic, title,des, phone));

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
                isNet = true;
            } else {
                isNet = false;
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Message msg = Message.obtain();
        if (isNet) {
            msg.what = 0;
            handler.sendMessage(msg);
        } else {
            msg.what = 1;
            handler.sendMessage(msg);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    foxiang_progress.setVisibility(View.GONE);
                    foxiangAdapter = new FoXiangAdapter(FoXiangDingZhiActivity.this, foxiangBeans);
                    foxiang_listview.setAdapter(foxiangAdapter);

                    foxiang_listview.setOnItemClickListener(new foxiangInfoOnItemClickListener());
                    break;
                case 1:
                    break;
            }
        }
    };

    protected class foxiangInfoOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent it_foxiang_info = new Intent(FoXiangDingZhiActivity.this, FoXiangInfoActivity.class);
            for (int i = 0; i <= position; i++) {
                if (position == i) {
                    Map map = (Map) foxiangList.get(i);
                    it_foxiang_info.putExtra("foxiangName", (String) map.get("foxiangName"));
                    it_foxiang_info.putExtra("foxiangApi", (String) map.get("foxiangApi"));
                }
            }
            startActivity(it_foxiang_info);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fo_xiang_ding_zhi, menu);
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
