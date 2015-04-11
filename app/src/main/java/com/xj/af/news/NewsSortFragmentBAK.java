package com.xj.af.news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.xj.af.R;
import com.xj.af.common.BaseXjFragment;
import com.xj.af.util.http.GetUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 *
 * 显示新闻列表
 */
public class NewsSortFragmentBAK extends BaseXjFragment  {

   private static final String ARG_URL = "param1";
    private static final String ARG_TITLE = "param2";
    private static final String ARG_ID = "id";
    private static final String ARG_ENNAME = "enName";
    private String url;
    private String title;
    private long id;
    private String enName;
    ListView listView;
    private UIHandler UIhandler;
    ArrayList<HashMap<String, Object>> listItem;
    private OnFragmentInteractionListener mListener;
    private SimpleAdapter listItemAdapter;

    public static NewsSortFragmentBAK newInstance(String url, String title,long id,String enName) {
        NewsSortFragmentBAK fragment = new NewsSortFragmentBAK();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        args.putString(ARG_TITLE, title);
        args.putLong(ARG_ID, id);
        args.putString(ARG_ENNAME,enName);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NewsSortFragmentBAK() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("xj","this is onCreate()");
        if (getArguments() != null) {
            url = getArguments().getString(ARG_URL);
            title = getArguments().getString(ARG_TITLE);
            id = getArguments().getLong(ARG_ID);
            enName = getArguments().getString(ARG_ENNAME);
        }

        listItem = new ArrayList();
        listItemAdapter = new SimpleAdapter(getActivity(), listItem,// 数据源
                R.layout.news_sort_list,
                new String[] {  "ItemTitle" },
                new int[] {  R.id.newsSortList_ItemTitle });
       // setListAdapter(listItemAdapter);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("xj","this is onCreateView()");
        View view  =  inflater.inflate(R.layout.fragment_newssort,container,false);
        listView = (ListView)view.findViewById(android.R.id.list);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("xj","this is onActivityCreated()");
        listView.setAdapter(listItemAdapter);
        UIhandler = new UIHandler();
        UIThread thread = new UIThread();
        thread.start();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("xj","this is onAttach()");
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("xj","this is onDetach()");
        mListener = null;
    }

    private class UIThread extends Thread {
        @Override
        public void run() {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            msg.setData(bundle);
            String str = "";
            String urlStr = "";
            urlStr = url ;
            try {
                str = GetUtil.get(urlStr);
                bundle.putString("msg", str);
            } catch (IOException e) {
                e.printStackTrace();
                bundle.putString("emsg", "网络不够给力，等会再试试吧(T_T)");
            }
            NewsSortFragmentBAK.this.UIhandler.sendMessage(msg);
        }
    }

    private class UIHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String emsg = bundle.getString("emsg");
            String message = bundle.getString("msg");
            if(message==null)return;
            try {
                JSONArray jsonObjs  = new JSONArray( message);
                for (int i = 0; i < jsonObjs.length(); i++) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    JSONObject jsonObj = ((JSONObject) jsonObjs.opt(i));
                    long id = jsonObj.getLong("id");
                    String name = jsonObj.getString("title");

                    String kaishi = jsonObj.getString("createTime");
                    DateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

                    if("jinRiShuoFa".equals(enName) || (url!=null && url.indexOf("jinRiShuoFa") != -1)){
                        if(kaishi!=null && !"".equals(kaishi))
                        {
                            String reTime = sdf.format(Long.parseLong(kaishi));
                            map.put("ItemTitle", reTime+"开示");
                        }
                    }
                    else
                        map.put("ItemTitle", name);
                    map.put("id", id);
                    listItem.add(map);
                }
                listItemAdapter.notifyDataSetChanged();

            }catch (JSONException e) {
                e.printStackTrace();
            }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    ListView listv = (ListView) arg0;
                    SimpleAdapter sa = (SimpleAdapter) listv.getAdapter();
                    Map map = (Map) sa.getItem(arg2);
                    Intent i = new Intent();
                    Object obj = map.get("ItemTitle");
                    if(obj!=null){
                        String title = (String)obj;
                        i.putExtra("title", title);
                    }
                    i.putExtra("id", (Long) map.get("id"));
                    //i.putExtra("url",url);
                    i.setClass(getActivity(), NewsActivitActivity.class);
                    startActivity(i);
                   // overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                }
            });
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
