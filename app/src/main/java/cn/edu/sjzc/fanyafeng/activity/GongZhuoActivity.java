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

import cn.edu.sjzc.fanyafeng.adapter.EventAdapter;
import cn.edu.sjzc.fanyafeng.adapter.GongZhuoAdapter;
import cn.edu.sjzc.fanyafeng.bean.EventBean;
import cn.edu.sjzc.fanyafeng.bean.GongZhuoBean;
import cn.edu.sjzc.fanyafeng.util.RefreshableView;

public class GongZhuoActivity extends BaseBackActivity {
    private ImageView gongzhuo_img;
    private TextView gongzhuo_name, gongzhuo_detail, gongzhuo_phone;
    private ListView gongzhuo_listview;
    private ProgressBar gongzhuo_progress;
    private List<Map<String, Object>> gongzhuoList = new ArrayList<Map<String, Object>>();
    private boolean isNet;
    private static List<GongZhuoBean> gongZhuoBeans;
    private GongZhuoAdapter gongZhuoAdapter;
    private String SIYUANHUODOND = "/api/newssort/page/findByEnName/global_gongZhuo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean flag = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_gong_zhuo);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
        title = "供桌/佛龛";
        initView();
        gongzhuo_progress.showContextMenu();
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
        this.gongzhuo_img = (ImageView) GongZhuoActivity.this.findViewById(R.id.gongzhuo_img);
        this.gongzhuo_name = (TextView) GongZhuoActivity.this.findViewById(R.id.gongzhuo_name);
        this.gongzhuo_detail = (TextView) GongZhuoActivity.this.findViewById(R.id.gongzhuo_detail);
        this.gongzhuo_phone = (TextView) GongZhuoActivity.this.findViewById(R.id.gongzhuo_phone);
        this.gongzhuo_listview = (ListView) GongZhuoActivity.this.findViewById(R.id.gongzhuo_listview);
        this.gongzhuo_progress = (ProgressBar) GongZhuoActivity.this.findViewById(R.id.gongzhuo_progress);
    }

    private void initData() {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet request;
        try {
            String SIYUANHUODOND_API_OLD = getServerURL() + SIYUANHUODOND;
//            String SIYUANHUODOND_API = SIYUANHUODOND_API_OLD + k;
            request = new HttpGet(new URI(SIYUANHUODOND_API_OLD));
            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String out = EntityUtils.toString(entity, "UTF-8");
                    Log.d("------------------------------------------------------------------------------------------->",out);
                    try {
                        JSONObject jsonObject = new JSONObject(out);
                        JSONArray eveArray = jsonObject.getJSONArray("content");
//                        lastPage = jsonObject.getString("last");
                        gongZhuoBeans = new ArrayList<GongZhuoBean>();
                        for (int i = 0; i < eveArray.length(); i++) {
                            JSONObject eveobj = eveArray.getJSONObject(i);
                            String id = eveobj.getString("id");
                            String GongZhuoInfoAPI = getServerURL() + "/m/news/newsDetail/" + id;
                            String title = eveobj.getString("title");
                            String phone = eveobj.getString("money");
                            String smallPic = getServerURL() + eveobj.getString("smallPic");
                            String des = eveobj.getString("des");

                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("gongzhuoName", title);
                            map.put("gongzhuoApi", GongZhuoInfoAPI);
                            gongzhuoList.add(map);
                            gongZhuoBeans.add(new GongZhuoBean(smallPic, title,des, phone));

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
                    gongzhuo_progress.setVisibility(View.GONE);
                    gongZhuoAdapter = new GongZhuoAdapter(GongZhuoActivity.this, gongZhuoBeans);
                    gongzhuo_listview.setAdapter(gongZhuoAdapter);

                    gongzhuo_listview.setOnItemClickListener(new gongzhuoInfoOnItemClickListener());


                    break;
                case 1:
                    break;
            }
        }
    };

    protected class gongzhuoInfoOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent it_event_info = new Intent(GongZhuoActivity.this, GongZhuoInfoActivity.class);
            for (int i = 0; i <= position; i++) {
                if (position == i) {
                    Map map = (Map) gongzhuoList.get(i);
                    it_event_info.putExtra("gongzhuoName", (String) map.get("gongzhuoName"));
                    it_event_info.putExtra("gongzhuoApi", (String) map.get("gongzhuoApi"));
                }
            }
            startActivity(it_event_info);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gong_zhuo, menu);
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
