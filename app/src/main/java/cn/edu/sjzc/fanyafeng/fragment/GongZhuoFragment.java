package cn.edu.sjzc.fanyafeng.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xj.af.R;
import com.xj.af.common.BaseFragment;
import com.xj.af.util.SingleImageTaskUtil;

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
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.edu.sjzc.fanyafeng.activity.GongZhuoActivity;
import cn.edu.sjzc.fanyafeng.bean.GongZhuoBean;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GongZhuoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GongZhuoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GongZhuoFragment extends BaseFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

//    private String SIYUANHUODOND = "/api/newssort/page/siYuanHuoDong/";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String SIYUANHUODOND = "/api/newssort/page/findByEnName/global_gongZhuo";
    private ImageView gongzhuo_frag_one_ib, gongzhuo_frag_two_ib, gongzhuo_frag_three_ib, gongzhuo_frag_four_ib;
    private TextView gongzhuo_frag_one_text, gongzhuo_frag_two_text, gongzhuo_frag_three_text, gongzhuo_frag_four_text;
    String url = "http://img3.douban.com/spic/s27078194.jpg";
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GongZhuoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GongZhuoFragment newInstance(String param1, String param2) {
        GongZhuoFragment fragment = new GongZhuoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public GongZhuoFragment() {
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
        View view = inflater.inflate(R.layout.fragment_gong_zhuo, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
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
        this.gongzhuo_frag_one_ib = (ImageView) getActivity().findViewById(R.id.gongzhuo_frag_one_ib);
        this.gongzhuo_frag_one_ib.setOnClickListener(this);
        this.gongzhuo_frag_two_ib = (ImageView) getActivity().findViewById(R.id.gongzhuo_frag_two_ib);
        this.gongzhuo_frag_two_ib.setOnClickListener(this);
        this.gongzhuo_frag_three_ib = (ImageView) getActivity().findViewById(R.id.gongzhuo_frag_three_ib);
        this.gongzhuo_frag_three_ib.setOnClickListener(this);
        this.gongzhuo_frag_four_ib = (ImageView) getActivity().findViewById(R.id.gongzhuo_frag_four_ib);
        this.gongzhuo_frag_four_ib.setOnClickListener(this);
        this.gongzhuo_frag_one_text = (TextView) getActivity().findViewById(R.id.gongzhuo_frag_one_text);
        this.gongzhuo_frag_two_text = (TextView) getActivity().findViewById(R.id.gongzhuo_frag_two_text);
        this.gongzhuo_frag_three_text = (TextView) getActivity().findViewById(R.id.gongzhuo_frag_three_text);
        this.gongzhuo_frag_four_text = (TextView) getActivity().findViewById(R.id.gongzhuo_frag_four_text);
    }

    private void initData() {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet request;
        try {
            String SIYUANHUODOND_API_OLD = getServerURL() + SIYUANHUODOND;
            Log.d("------------------------------------",SIYUANHUODOND_API_OLD);
            request = new HttpGet(new URI(SIYUANHUODOND_API_OLD));
            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String out = EntityUtils.toString(entity, "UTF-8");
                    try {
                        JSONObject jsonObject = new JSONObject(out);
                        JSONArray eveArray = jsonObject.getJSONArray("content");
                        Log.d("--------------------------", jsonObject.toString());
                        for (int i = 0; i < 4; i++) {
//                            JSONObject eveobj = eveArray.getJSONObject(i);
//                            String smallPic = getServerURL() + eveobj.getString("smallPic");
//                            Log.d("----------------------------------smallPic", smallPic);
//                            String title = eveobj.getString("title");
                            if (i == 0) {
                                SingleImageTaskUtil imageTask = new SingleImageTaskUtil(
                                        this.gongzhuo_frag_one_ib);
                                imageTask.execute(url);
                            }
                            if (i == 1) {
                                SingleImageTaskUtil imageTask = new SingleImageTaskUtil(
                                        this.gongzhuo_frag_two_ib);
                                imageTask.execute(url);
                            }
                            if (i == 2) {
                                SingleImageTaskUtil imageTask = new SingleImageTaskUtil(
                                        this.gongzhuo_frag_three_ib);
                                imageTask.execute(url);
                            }
                            if (i == 3) {
                                SingleImageTaskUtil imageTask = new SingleImageTaskUtil(
                                        this.gongzhuo_frag_four_ib);
                                imageTask.execute(url);
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gongzhuo_frag_one_ib:
                Intent it_gongzhuo = new Intent(getActivity(), GongZhuoActivity.class);
                startActivity(it_gongzhuo);
                break;
//            case R.id.gongzhuo_frag_one_ib:
//                Intent it_gongzhuo = new Intent(getActivity(), GongZhuoActivity.class);
//                startActivity(it_gongzhuo);
//                break;
//            case R.id.gongzhuo_frag_one_ib:
//                Intent it_gongzhuo = new Intent(getActivity(), GongZhuoActivity.class);
//                startActivity(it_gongzhuo);
//                break;
//            case R.id.gongzhuo_frag_one_ib:
//                Intent it_gongzhuo = new Intent(getActivity(), GongZhuoActivity.class);
//                startActivity(it_gongzhuo);
//                break;
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
    /*
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.*/
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }
}
