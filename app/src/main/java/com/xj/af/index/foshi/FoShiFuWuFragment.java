package com.xj.af.index.foshi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xj.af.R;
import com.xj.af.common.BaseFragment;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
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
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.sjzc.fanyafeng.activity.BuddhistServicesInfo;
import cn.edu.sjzc.fanyafeng.activity.SignUpActivity;
import cn.edu.sjzc.fanyafeng.adapter.BuddhistServicesAdapter;
import cn.edu.sjzc.fanyafeng.bean.BuddhistServicesBean;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FoShiFuWuFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FoShiFuWuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoShiFuWuFragment extends BaseFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView buddhistservices_listview;
    private BuddhistServicesAdapter buddhistServicesAdapter;
    private List<BuddhistServicesBean> buddhistServicesBeans;
    private List<Map<String, Object>> buddhistServicesList = new ArrayList<Map<String, Object>>();
    private String bsTitle, bsDetail, bsFirst, bsSecond, bsThird;
    private String FOSHIFUWU_API = "/api/newssort/page/foShiFuWu/";
    private ProgressDialog mDialog;
    private boolean isNet;
    private String startDate,endDate,FOSHIFUWU_API_new;
    private List<String> data = new ArrayList<String>();

    private OnFragmentInteractionListener mListener;
    private ProgressBar bs_progress;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FoShiFuWuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FoShiFuWuFragment newInstance(String param1, String param2) {
        FoShiFuWuFragment fragment = new FoShiFuWuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FoShiFuWuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fo_shi_fu_wu, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        FOSHIFUWU_API_new =getServerURL()+FOSHIFUWU_API+getUnitId();
        String unitId = getUnitId();
        bs_progress.showContextMenu();
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
        buddhistservices_listview = (ListView) getActivity().findViewById(R.id.buddhistservices_listview);
        this.bs_progress = (ProgressBar)getActivity().findViewById(R.id.bs_progress);

    }


    private void initData() {
        HttpClient httpClient = new DefaultHttpClient();

        HttpGet request;
        try {
            request = new HttpGet(new URI(FOSHIFUWU_API_new));
            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String out = EntityUtils.toString(entity, "UTF-8");
                    try {
                        JSONObject jsonObject = new JSONObject(out);
                        JSONArray bsArray = jsonObject.getJSONArray("content");
                        buddhistServicesBeans = new ArrayList<BuddhistServicesBean>();
                        for (int i = 0; i < bsArray.length(); i++) {
                            JSONObject bsobj = bsArray.getJSONObject(i);
                            String id = bsobj.getString("id");
                            String bsInfoAPI = getServerURL() + "/m/news/newsDetail/" + id;
                            String title = bsobj.getString("title");
                            String des = bsobj.getString("des");
                            String money = "每人所需金额:"+bsobj.getString("money");
                            String startTime = bsobj.getString("createTime");
                            if (!startTime.equals("null")) {
                                startDate ="开始时间："+ getMilliToDate(startTime);
                            } else {
                                startDate = null;
                            }
                            String endTime = bsobj.getString("endTime");
                            if (!endTime.equals("null")) {
                                endDate ="结束时间："+ getMilliToDate(endTime);
                            } else {
                                endDate = null;
                            }
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("bsInfoApi", bsInfoAPI);
                            map.put("newsId",id);
                            map.put("bsTitle", title);
                            map.put("money",bsobj.getString("money"));
                            buddhistServicesList.add(map);
                            buddhistServicesBeans.add(new BuddhistServicesBean(title, des, money, startDate, endDate));

                        }
                        isNet = true;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                isNet = false;
            }

        } catch (URISyntaxException e) {
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
                    buddhistServicesAdapter = new BuddhistServicesAdapter(getActivity(), buddhistServicesBeans);
                    buddhistservices_listview.setAdapter(buddhistServicesAdapter);
                    bs_progress.setVisibility(View.GONE);
                    buddhistservices_listview.setOnItemClickListener(new bsInfoOnItemClickListener());
                    break;
                case 1:
                    break;
            }
        }
    };


    protected class bsInfoOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent it_bs_info = new Intent(getActivity(), BuddhistServicesInfo.class);
            for (int i = 0; i <= position; i++) {
                if (position == i) {
                    Map map = (Map) buddhistServicesList.get(i);
                    String btitle = (String) map.get("bsTitle");
                    String bapi = (String) map.get("bsInfoApi");
                    it_bs_info.putExtra("newsId",(String)map.get("newsId"));
                    it_bs_info.putExtra("btitle_info", btitle);
                    it_bs_info.putExtra("bapi_info", bapi);
                    it_bs_info.putExtra("money",(String)map.get("money"));

                }
            }

            startActivity(it_bs_info);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            default:
                break;
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mDialog != null) {
            mDialog.cancel();
        }
    }
}
