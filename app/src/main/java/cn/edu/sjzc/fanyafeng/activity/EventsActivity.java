package cn.edu.sjzc.fanyafeng.activity;

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
import cn.edu.sjzc.fanyafeng.bean.EventBean;
import cn.edu.sjzc.fanyafeng.util.RefreshableView;

import java.text.SimpleDateFormat;

public class EventsActivity extends BaseBackActivity {

    private ImageView event_img;
    private ProgressBar eventProgressBar;
    private TextView event_name, eventstart_time, eventend_time, event_money;
    private ListView event_listview;
    private EventAdapter eventAdapter;
    private static List<EventBean> eventBeans;

    private List<Map<String, Object>> eventsList = new ArrayList<Map<String, Object>>();
    private String eventImg, eventName, eventStartTime, eventEndTime, smallPic, createTime, endTime, des, money, lastPage;
    private EventBean[] eventArray;

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
    private boolean mIsStart = true;
    private int mCurIndex = 0;
    private static final int mLoadDataCount = 100;
    private boolean isNet;
    private int k = 1;
    private RefreshableView refreshableView;
    private String SIYUANHUODOND = "/api/newssort/page/siYuanHuoDong/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean flag = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

        setContentView(R.layout.activity_events);

        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);

        title = "活动";
        initView();
        eventProgressBar.showContextMenu();
        Thread loadThread = new Thread(new LoadThread());
        loadThread.start();
    }

    class LoadThread implements Runnable {
        @Override
        public void run() {
            initData(k);
        }
    }

    private void initView() {
        this.eventProgressBar = (ProgressBar) EventsActivity.this.findViewById(R.id.event_progress);
        this.event_name = (TextView) EventsActivity.this.findViewById(R.id.event_name);
        this.eventstart_time = (TextView) EventsActivity.this.findViewById(R.id.eventstart_time);
        this.eventend_time = (TextView) EventsActivity.this.findViewById(R.id.eventend_time);
        this.event_img = (ImageView) EventsActivity.this.findViewById(R.id.event_img);
        this.event_money = (TextView) EventsActivity.this.findViewById(R.id.event_money);
        refreshableView = (RefreshableView) findViewById(R.id.events_refreshable);
        event_listview = (ListView) EventsActivity.this.findViewById(R.id.event_listview);

    }

    private int initData(int k) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet request;
        try {
            String SIYUANHUODOND_API_OLD = getServerURL() + SIYUANHUODOND + getUnitId() + "?page=";
            String SIYUANHUODOND_API = SIYUANHUODOND_API_OLD + k;
            request = new HttpGet(new URI(SIYUANHUODOND_API));
            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String out = EntityUtils.toString(entity, "UTF-8");
                    try {
                        JSONObject jsonObject = new JSONObject(out);

                        JSONArray eveArray = jsonObject.getJSONArray("content");

                        lastPage = jsonObject.getString("last");

                        eventBeans = new ArrayList<EventBean>();
                        for (int i = 0; i < eveArray.length(); i++) {
                            JSONObject eveobj = eveArray.getJSONObject(i);
                            String id = eveobj.getString("id");
                            String EventInfoAPI = getServerURL() + "/m/news/newsDetail/" + id;
                            String title = eveobj.getString("title");
                            String createTime = eveobj.getString("createTime");
                            if (!createTime.equals("null")) {
                                createTime = "开始时间：" + getMilliToDate(createTime);
                            } else {
                                createTime = null;
                            }
                            String endTime = eveobj.getString("endTime");
                            if (!endTime.equals("null")) {
                                endTime = "结束时间：" + getMilliToDate(endTime);
                            } else {
                                endTime = null;
                            }
                            String money = "报名金额：" + eveobj.getString("money");
                            String smallPic = getServerURL() + eveobj.getString("smallPic");
                            String des = eveobj.getString("des");

                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("eveName", title);
                            map.put("eveApi", EventInfoAPI);
                            map.put("eveId", eveobj.getString("id"));
                            map.put("eveMoney", eveobj.getString("money"));
                            eventsList.add(map);
                            eventBeans.add(new EventBean(smallPic, title, createTime, endTime, des, money));

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
        return k;

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    eventProgressBar.setVisibility(View.GONE);
                    eventAdapter = new EventAdapter(EventsActivity.this, eventBeans);
                    event_listview.setAdapter(eventAdapter);

                    event_listview.setOnItemClickListener(new eventInfoOnItemClickListener());

                    refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
                        @Override
                        public void onRefresh() {
                            if (lastPage.equalsIgnoreCase("true")) {
                                k = 1;
                            } else {
                                k++;
                            }
                            Log.d("-----------------------------------------------",""+k);
                            initData(k);
                            eventProgressBar.setVisibility(View.GONE);
                            eventAdapter = new EventAdapter(EventsActivity.this, eventBeans);
                            event_listview.setAdapter(eventAdapter);

                            event_listview.setOnItemClickListener(new eventInfoOnItemClickListener());
                            refreshableView.finishRefreshing();
                        }
                    }, 0);
                    break;
                case 1:
                    break;
            }
        }
    };

    protected class eventInfoOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent it_event_info = new Intent(EventsActivity.this, EventInfoActivity.class);
            for (int i = 0; i <= position; i++) {
                if (position == i) {
                    Map map = (Map) eventsList.get(i);
                    String etitle = (String) map.get("eveName");
                    String eapi = (String) map.get("eveApi");
                    String newsId = (String) map.get("eveId");
                    it_event_info.putExtra("event_title", etitle);
                    it_event_info.putExtra("event_api", eapi);
                    it_event_info.putExtra("event_newsId", newsId);
                    it_event_info.putExtra("event_money", (String) map.get("eveMoney"));
                }
            }
            startActivity(it_event_info);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_events, menu);
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
