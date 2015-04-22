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

import cn.edu.sjzc.fanyafeng.adapter.XiangLuAdapter;
import cn.edu.sjzc.fanyafeng.adapter.ZhuangShiAdapter;
import cn.edu.sjzc.fanyafeng.bean.XiangLuBean;
import cn.edu.sjzc.fanyafeng.bean.ZhuangShiBean;

public class ZhuangShiActivity extends BaseBackActivity {
    private ImageView zhuangshi_img;
    private TextView zhuangshi_name, zhuangshi_detail, zhuangshi_phone;
    private ListView zhuangshi_listview;
    private ProgressBar zhuangshi_progress;
    private List<Map<String, Object>> zhuangshiList = new ArrayList<Map<String, Object>>();
    private boolean isNet;
    private static List<ZhuangShiBean> zhuangshiBeans;
    private ZhuangShiAdapter zhuangshiAdapter;
    private String SIYUANHUODOND = "/api/newssort/page/findByEnName/global_siMIaoZhuangShi";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean flag = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_zhuang_shi);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
        title = "寺院装饰";
        initView();
        zhuangshi_progress.showContextMenu();
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
        this.zhuangshi_img = (ImageView) ZhuangShiActivity.this.findViewById(R.id.zhuangshi_img);
        this.zhuangshi_name = (TextView) ZhuangShiActivity.this.findViewById(R.id.zhuangshi_name);
        this.zhuangshi_detail = (TextView) ZhuangShiActivity.this.findViewById(R.id.zhuangshi_detail);
        this.zhuangshi_phone = (TextView) ZhuangShiActivity.this.findViewById(R.id.zhuangshi_phone);
        this.zhuangshi_listview = (ListView) ZhuangShiActivity.this.findViewById(R.id.zhuangshi_listview);
        this.zhuangshi_progress = (ProgressBar) ZhuangShiActivity.this.findViewById(R.id.zhuangshi_progress);
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
                        zhuangshiBeans = new ArrayList<ZhuangShiBean>();
                        for (int i = 0; i < eveArray.length(); i++) {
                            JSONObject eveobj = eveArray.getJSONObject(i);
                            String id = eveobj.getString("id");
                            String ZhuangShiInfoAPI = getServerURL() + "/m/news/newsDetail/" + id;
                            String title = eveobj.getString("title");
                            String phone = eveobj.getString("money");
                            String smallPic = getServerURL() + eveobj.getString("smallPic");
                            String des = eveobj.getString("des");

                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("zhuangshiName", title);
                            map.put("zhuangshiApi", ZhuangShiInfoAPI);
                            zhuangshiList.add(map);
                            zhuangshiBeans.add(new ZhuangShiBean(smallPic, title,des, phone));

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
                    zhuangshi_progress.setVisibility(View.GONE);
                    zhuangshiAdapter = new ZhuangShiAdapter(ZhuangShiActivity.this, zhuangshiBeans);
                    zhuangshi_listview.setAdapter(zhuangshiAdapter);

                    zhuangshi_listview.setOnItemClickListener(new zhuangshiInfoOnItemClickListener());
                    break;
                case 1:
                    break;
            }
        }
    };

    protected class zhuangshiInfoOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent it_zhuangshi_info = new Intent(ZhuangShiActivity.this, ZhuangShiInfoActivity.class);
            for (int i = 0; i <= position; i++) {
                if (position == i) {
                    Map map = (Map) zhuangshiList.get(i);
                    it_zhuangshi_info.putExtra("zhuangshiName", (String) map.get("zhuangshiName"));
                    it_zhuangshi_info.putExtra("zhuangshiApi", (String) map.get("zhuangshiApi"));
                }
            }
            startActivity(it_zhuangshi_info);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_zhuang_shi, menu);
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
