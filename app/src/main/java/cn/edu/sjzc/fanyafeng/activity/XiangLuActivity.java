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
import cn.edu.sjzc.fanyafeng.bean.GongZhuoBean;
import cn.edu.sjzc.fanyafeng.bean.XiangLuBean;

public class XiangLuActivity extends BaseBackActivity {
    private ImageView xianglu_img;
    private TextView xianglu_name, xianglu_detail, xianglu_phone;
    private ListView xianglu_listview;
    private ProgressBar xianglu_progress;
    private List<Map<String, Object>> xiangluList = new ArrayList<Map<String, Object>>();
    private boolean isNet;
    private static List<XiangLuBean> xiangLuBeans;
    private XiangLuAdapter xiangLuAdapter;
    private String SIYUANHUODOND = "/api/newssort/page/findByEnName/global_xiangLu";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean flag = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_xiang_lu);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
        title = "香炉/大鼎";
        initView();
        xianglu_progress.showContextMenu();
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
        this.xianglu_img = (ImageView) XiangLuActivity.this.findViewById(R.id.xianglu_img);
        this.xianglu_name = (TextView) XiangLuActivity.this.findViewById(R.id.xianglu_name);
        this.xianglu_detail = (TextView) XiangLuActivity.this.findViewById(R.id.xianglu_detail);
        this.xianglu_phone = (TextView) XiangLuActivity.this.findViewById(R.id.xianglu_phone);
        this.xianglu_listview = (ListView) XiangLuActivity.this.findViewById(R.id.xianglu_listview);
        this.xianglu_progress = (ProgressBar) XiangLuActivity.this.findViewById(R.id.xianglu_progress);
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
                        xiangLuBeans = new ArrayList<XiangLuBean>();
                        for (int i = 0; i < eveArray.length(); i++) {
                            JSONObject eveobj = eveArray.getJSONObject(i);
                            String id = eveobj.getString("id");
                            String XiangLuInfoAPI = getServerURL() + "/m/news/newsDetail/" + id;
                            String title = eveobj.getString("title");
                            String phone = eveobj.getString("money");
                            String smallPic = getServerURL() + eveobj.getString("smallPic");
                            String des = eveobj.getString("des");

                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("xiangluName", title);
                            map.put("xiangluApi", XiangLuInfoAPI);
                            xiangluList.add(map);
                            xiangLuBeans.add(new XiangLuBean(smallPic, title,des, phone));

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
                    xianglu_progress.setVisibility(View.GONE);
                    xiangLuAdapter = new XiangLuAdapter(XiangLuActivity.this, xiangLuBeans);
                    xianglu_listview.setAdapter(xiangLuAdapter);

                    xianglu_listview.setOnItemClickListener(new xiangluInfoOnItemClickListener());
                    break;
                case 1:
                    break;
            }
        }
    };

    protected class xiangluInfoOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent it_xianglu_info = new Intent(XiangLuActivity.this, XiangLuInfoActivity.class);
            for (int i = 0; i <= position; i++) {
                if (position == i) {
                    Map map = (Map) xiangluList.get(i);
                    it_xianglu_info.putExtra("xiangluName", (String) map.get("xiangluName"));
                    it_xianglu_info.putExtra("xiangluApi", (String) map.get("xiangluApi"));
                }
            }
            startActivity(it_xianglu_info);
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_xiang_lu, menu);
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
