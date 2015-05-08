package cn.edu.sjzc.fanyafeng.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.net.URI;
import java.net.URISyntaxException;

import cn.edu.sjzc.fanyafeng.activity.SengFuActivity;
import cn.edu.sjzc.fanyafeng.activity.XiangLuActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SengFuFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SengFuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SengFuFragment extends BaseFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String SIYUANHUODOND = "/api/newssort/page/findByEnName/global_gongZhuo";
    private ImageView sengfu_frag_one_ib, sengfu_frag_two_ib, sengfu_frag_three_ib, sengfu_frag_four_ib;
    private TextView sengfu_frag_one_text, sengfu_frag_two_text, sengfu_frag_three_text, sengfu_frag_four_text;
    String url = "http://img3.douban.com/spic/s27078194.jpg";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SengFuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SengFuFragment newInstance(String param1, String param2) {
        SengFuFragment fragment = new SengFuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SengFuFragment() {
        // Required empty public constructor
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
        this.sengfu_frag_one_ib = (ImageView) getActivity().findViewById(R.id.sengfu_frag_one_ib);
        this.sengfu_frag_one_ib.setOnClickListener(this);
        this.sengfu_frag_two_ib = (ImageView) getActivity().findViewById(R.id.sengfu_frag_two_ib);
        this.sengfu_frag_two_ib.setOnClickListener(this);
        this.sengfu_frag_three_ib = (ImageView) getActivity().findViewById(R.id.sengfu_frag_three_ib);
        this.sengfu_frag_three_ib.setOnClickListener(this);
        this.sengfu_frag_four_ib = (ImageView) getActivity().findViewById(R.id.sengfu_frag_four_ib);
        this.sengfu_frag_four_ib.setOnClickListener(this);
        this.sengfu_frag_one_text = (TextView) getActivity().findViewById(R.id.sengfu_frag_one_text);
        this.sengfu_frag_two_text = (TextView) getActivity().findViewById(R.id.sengfu_frag_two_text);
        this.sengfu_frag_three_text = (TextView) getActivity().findViewById(R.id.sengfu_frag_three_text);
        this.sengfu_frag_four_text = (TextView) getActivity().findViewById(R.id.sengfu_frag_four_text);
    }
    private void initData() {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet request;
        try {
            String SIYUANHUODOND_API_OLD = getServerURL() + SIYUANHUODOND;
            Log.d("------------------------------------", SIYUANHUODOND_API_OLD);
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
                                        this.sengfu_frag_one_ib);
                                imageTask.execute("http://a1.qpic.cn/psb?/V12cYG6y0mgzhv/feRw83MoTv.kTBH4TYdXYNXGkvZK2Sln2yD7I3wYBCo!/m/dFgAAAAAAAAAnull&bo=AAQAAgAAAAAFByI!&rf=photolist&t=5");
                            }
                            if (i == 1) {
                                SingleImageTaskUtil imageTask = new SingleImageTaskUtil(
                                        this.sengfu_frag_two_ib);
                                imageTask.execute("http://a3.qpic.cn/psb?/V12cYG6y0mgzhv/F0xmiFqWMXaDvdISQPop8LdUxWtoz91vfxcU4q984Ks!/m/dB4AAAAAAAAAnull&bo=AAQAAgAAAAAFByI!&rf=photolist&t=5");
                            }
                            if (i == 2) {
                                SingleImageTaskUtil imageTask = new SingleImageTaskUtil(
                                        this.sengfu_frag_three_ib);
                                imageTask.execute("http://a1.qpic.cn/psb?/V12cYG6y0mgzhv/l8YuRSK*iQ.LK79UrHSuUC.y8b8FFQLxkCZTqCWwJPU!/m/dFgAAAAAAAAAnull&bo=AAQAAgAAAAAFByI!&rf=photolist&t=5");
                            }
                            if (i == 3) {
                                SingleImageTaskUtil imageTask = new SingleImageTaskUtil(
                                        this.sengfu_frag_four_ib);
                                imageTask.execute("http://a3.qpic.cn/psb?/V12cYG6y0mgzhv/36YhO2cUYOmNB7UmounGVakQ1M3D8LwGD0iVKi6DERw!/m/dAoAAAAAAAAAnull&bo=AAQAAgAAAAAFByI!&rf=photolist&t=5");
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
            case R.id.sengfu_frag_one_ib:
                Intent it_gongzhuo = new Intent(getActivity(), SengFuActivity.class);
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seng_fu, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
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
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        public void onFragmentInteraction(Uri uri);
//    }

}
