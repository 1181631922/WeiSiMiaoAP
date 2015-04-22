package com.xj.af.gdl;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.xj.af.R;
import com.xj.af.common.BaseXjFragment;
import com.xj.af.util.StrUtil;
import com.xj.af.util.http.GetUtil;

import org.apache.http.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 功德林LIST
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class GdlItemFragment extends BaseXjFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String url;
    private String unitId;

    private UIHandler handler;
    private GridView gridView;
    SimpleAdapter saImageItems ;
    ArrayList<HashMap<String, Object>> lstImageItem;

    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types of parameters
    public static GdlItemFragment newInstance(String param1, String param2) {
        GdlItemFragment fragment = new GdlItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GdlItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            url = getArguments().getString(ARG_PARAM1);
            unitId = getArguments().getString(ARG_PARAM2);
        }
        lstImageItem = new ArrayList();
        saImageItems = new SimpleAdapter(getActivity(),lstImageItem,
                R.layout.shopsy_item,new String[] { "ItemImage", "ItemText" },
                new int[] { R.id.shopsy_ItemImage, R.id.shopsy_ItemText });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view  =  inflater.inflate(R.layout.fragment_gdl_grid,container,false);
        gridView = (GridView)view.findViewById(R.id.gdl_gridView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setBackgroundColor(Color.RED);
            }
        });
        //setListAdapter(saImageItems);
        return view;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //listView = (ListView) getActivity().findViewById(android.R.id.list);
        //listView.setAdapter(listItemAdapter);
        //((AdapterView<ListAdapter>) mListView).setAdapter(listItemAdapter);
        //mListView.setOnItemClickListener(this);
        handler = new UIHandler();
        UIThread thread = new UIThread();
        thread.start();
    }

    /*获取数据后更新界面*/
    class UIHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String emsg = bundle.getString("emsg");
            String imsg = bundle.getString("imsg");
            if(emsg!=null && !emsg.equals("")){
                Toast.makeText(getActivity(), "发生错误:" + emsg, Toast.LENGTH_SHORT).show();
            }else{
                gridView.setAdapter(saImageItems);
                gridView.setOnItemClickListener(new ItemClickListener());
                //saImageItems.notifyDataSetChanged();
            }
        }
    }


    /**获取功德林商品json数据线程*/
    class UIThread extends Thread {

        @Override
        public void run() {
            url = url + "/api/shopsy/"+unitId;
            String content = "";
            Message msg = new Message();
            Bundle bundle = new Bundle();
            msg.setData(bundle);
            try {
                content = GetUtil.get(url);
                bundle.putString("imsg", content);
            } catch (ParseException e) {
                bundle.putString("emsg", "解析错误");
                e.printStackTrace();
            } catch (IOException e) {
                bundle.putString("emsg", "网络不够给力，等会再试试吧(T_T)");
                e.printStackTrace();
            }
            try {
                if (!content.equals("")) {
                    JSONArray ja = new JSONArray(content);
                    for (int i = 0; i < ja.length(); i++) {
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        JSONObject jso = ((JSONObject) ja.opt(i));
                        String picPath = jso.getString("picPath");
                        if (!StrUtil.isBlank(picPath)) {
                            //map.put("ItemImage",BitmapHelper.getBitmap(picPath));// 添加图像资源的ID  速度太慢
                        }
                        map.put("price", jso.getString("price"));
                        map.put("ItemText", jso.getString("name"));//
                        map.put("id",jso.getString("id"));
                        map.put("picPath", picPath);
                        lstImageItem.add(map);
                    }
                                    }
            } catch (JSONException e) {
                bundle.putString("emsg", "解析错误");
                e.printStackTrace();
            }
            msg.setData(bundle);
           GdlItemFragment.this.handler.sendMessage(msg);
        }
    }

    /**单击功德林商品事件*/
    class ItemClickListener implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            HashMap<String, Object> item = (HashMap<String, Object>) arg0
                    .getItemAtPosition(arg2);
            String id = (String)item.get("id");
            String picPath = (String)item.get("picPath");
            String subject = (String)item.get("ItemText");
            String price = (String)item.get("price");
           Intent i = new Intent(getActivity(),ShopsyDetailActivity.class);

            i.putExtra("id", id);
            i.putExtra("picPath", picPath);
            i.putExtra("subject", subject);
            i.putExtra("price", price);
            startActivity(i);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String id);
    }

}
