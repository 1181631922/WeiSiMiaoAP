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

import cn.edu.sjzc.fanyafeng.adapter.SiMiaoAdapter;
import cn.edu.sjzc.fanyafeng.adapter.XiangLuAdapter;
import cn.edu.sjzc.fanyafeng.bean.SiMiaoBean;
import cn.edu.sjzc.fanyafeng.bean.XiangLuBean;

public class SiMiaoActivity extends BaseBackActivity {
    private ImageView simiao_img;
    private TextView simiao_name, simiao_detail, simiao_phone;
    private ListView simiao_listview;
    private ProgressBar simiao_progress;
    private List<Map<String, Object>> simiaoList = new ArrayList<Map<String, Object>>();
    private boolean isNet;
    private static List<SiMiaoBean> siMiaoBeans;
    private SiMiaoAdapter siMiaoAdapter;
    private String SIYUANHUODOND = "/api/newssort/page/findByEnName/global_siMiaoJianZhu";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean flag = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_si_miao2);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
        title = "寺庙/建筑";
        initView();
        simiao_progress.showContextMenu();
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
        this.simiao_img = (ImageView) SiMiaoActivity.this.findViewById(R.id.simiao_img);
        this.simiao_name = (TextView) SiMiaoActivity.this.findViewById(R.id.simiao2_name);
        this.simiao_detail = (TextView) SiMiaoActivity.this.findViewById(R.id.simiao_detail);
        this.simiao_phone = (TextView) SiMiaoActivity.this.findViewById(R.id.simiao_phone);
        this.simiao_listview = (ListView) SiMiaoActivity.this.findViewById(R.id.simiao2_listview);
        this.simiao_progress = (ProgressBar) SiMiaoActivity.this.findViewById(R.id.simiao_progress);
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
                        siMiaoBeans = new ArrayList<SiMiaoBean>();
                        for (int i = 0; i < eveArray.length(); i++) {
                            JSONObject eveobj = eveArray.getJSONObject(i);
                            String id = eveobj.getString("id");
                            String SiMiaoInfoAPI = getServerURL() + "/m/news/newsDetail/" + id;
                            String title = eveobj.getString("title");
                            String phone = eveobj.getString("money");
                            String smallPic = getServerURL() + eveobj.getString("smallPic");
                            String des = eveobj.getString("des");

                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("simiaoName", title);
                            map.put("simiaoApi", SiMiaoInfoAPI);
                            simiaoList.add(map);
                            siMiaoBeans.add(new SiMiaoBean(smallPic, title,des, phone));

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
                    simiao_progress.setVisibility(View.GONE);
                    siMiaoAdapter = new SiMiaoAdapter(SiMiaoActivity.this, siMiaoBeans);
                    simiao_listview.setAdapter(siMiaoAdapter);

                    simiao_listview.setOnItemClickListener(new simiaoInfoOnItemClickListener());
                    break;
                case 1:
                    break;
            }
        }
    };

    protected class simiaoInfoOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent it_simiao_info = new Intent(SiMiaoActivity.this, SiMiaoInfoActivity.class);
            for (int i = 0; i <= position; i++) {
                if (position == i) {
                    Map map = (Map) simiaoList.get(i);
                    it_simiao_info.putExtra("simiaoName", (String) map.get("simiaoName"));
                    it_simiao_info.putExtra("simiaoApi", (String) map.get("simiaoApi"));
                }
            }
            startActivity(it_simiao_info);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_si_miao, menu);
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
