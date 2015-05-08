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
import android.widget.Button;
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

import cn.edu.sjzc.fanyafeng.activity.AboutCompanyActivity;
import cn.edu.sjzc.fanyafeng.activity.GongZhuoActivity;
import cn.edu.sjzc.fanyafeng.activity.GuZhongActivity;
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
    private Button gongzhuo_but;
    private String gongzhuo_one_api,gongzhuo_one_title,gongzhuo_two_api,gongzhuo_two_title,gongzhuo_three_api,gongzhuo_three_title,gongzhuo_four_api,gongzhuo_four_title;

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
        this.gongzhuo_frag_one_text.setText("广东省潮州市乐古陶瓷");
        this.gongzhuo_frag_two_text = (TextView) getActivity().findViewById(R.id.gongzhuo_frag_two_text);
        this.gongzhuo_frag_two_text.setText("吾心堂");
        this.gongzhuo_frag_three_text = (TextView) getActivity().findViewById(R.id.gongzhuo_frag_three_text);
        this.gongzhuo_frag_three_text.setText("铜心缘");
        this.gongzhuo_frag_four_text = (TextView) getActivity().findViewById(R.id.gongzhuo_frag_four_text);
        this.gongzhuo_frag_four_text.setText("元谱服饰文化");
        this.gongzhuo_but=(Button)getActivity().findViewById(R.id.gongzhuo_but);
        this.gongzhuo_but.setOnClickListener(this);
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
                    try {
                        JSONObject jsonObject = new JSONObject(out);
                        JSONArray eveArray = jsonObject.getJSONArray("content");
                        for (int i = 0; i < 4; i++) {
//                            JSONObject eveobj = eveArray.getJSONObject(i);
//                            String smallPic = getServerURL() + eveobj.getString("smallPic");
//                            Log.d("----------------------------------smallPic", smallPic);
//                            String title = eveobj.getString("title");
                            if (i == 0) {
                                SingleImageTaskUtil imageTask = new SingleImageTaskUtil(
                                        this.gongzhuo_frag_one_ib);
                                imageTask.execute("http://a1.qpic.cn/psb?/V12cYG6y0mgzhv/MsPX.Bqvg3KwrcmfgnCunJXzM2u2wW9*cJyV3K*gR*k!/m/dFgAAAAAAAAAnull&bo=3AHuAAAAAAAFBxc!&rf=photolist&t=5");
                                gongzhuo_one_api="http://weixin.asiabt.com/api/9034ea376f/web/?m=9034ea376f/web&wxref=mp.weixin.qq.com";
                                gongzhuo_one_title="广东省潮州市乐古陶瓷";
//                                this.gongzhuo_frag_one_text.setText(gongzhuo_one_title);
                            }
                            if (i == 1) {
                                SingleImageTaskUtil imageTask = new SingleImageTaskUtil(
                                        this.gongzhuo_frag_two_ib);
                                imageTask.execute("http://a3.qpic.cn/psb?/V12cYG6y0mgzhv/xOZJSPiiEB1cg0*UDYErPOtcDNqpnG0KH3lIKfngCGA!/m/dB4AAAAAAAAAnull&bo=WAIsAQAAAAAFB1M!&rf=photolist&t=5");
                                gongzhuo_two_api="http://xlf.55zhe.net/wz.php?mod=wzfenlei&wzid=24337&openid=fromuserid&aw=wx.qq.com";
                                gongzhuo_two_title="吾心堂";
                            }
                            if (i == 2) {
                                SingleImageTaskUtil imageTask = new SingleImageTaskUtil(
                                        this.gongzhuo_frag_three_ib);
                                imageTask.execute("http://a1.qpic.cn/psb?/V12cYG6y0mgzhv/IDu0OUxuyPVhjPXyS5Z7XTRn9rWwBkTL9qSN8ijBWNk!/m/dFQAAAAAAAAAnull&bo=uwHdAAAAAAADB0U!&rf=photolist&t=5");
                                gongzhuo_three_api="http://www.txyxm.com/";
                                gongzhuo_three_title="铜心缘";
                            }
                            if (i == 3) {
                                SingleImageTaskUtil imageTask = new SingleImageTaskUtil(
                                        this.gongzhuo_frag_four_ib);
                                imageTask.execute("http://a1.qpic.cn/psb?/V12cYG6y0mgzhv/nG6pcCjd1UGa9GshQ5yjm75AqNK90HZY6ua2ImIZ9WU!/m/dFQAAAAAAAAAnull&bo=gAJAAQAAAAAFB.c!&rf=photolist&t=5");
                                gongzhuo_four_api="http://299976.m.weimob.com/weisite/home?_tj_twtype=16&_tj_pid=299976&_tt=1&_tj_graphicid=6350943&_tj_title=%E6%AC%A2%E8%BF%8E%E5%85%B3%E6%B3%A8%E5%85%83%E8%B0%B1%E6%9C%8D%E9%A5%B0&_tj_keywords=%E5%BE%AE%E5%AE%98%E7%BD%91&pid=299976&bid=453364&wechatid=o6KP7t9HCz8a_LFDLRBiPwBLlEu0&from=1&wxref=mp.weixin.qq.com&channel=menu%255E%2523%255E5b6u5a6Y572R";
                                gongzhuo_four_title="元谱服饰文化";
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
            case R.id.gongzhuo_but:
                Intent it_gongzhuo_list =new Intent(getActivity(), GuZhongActivity.class);
                startActivity(it_gongzhuo_list);
                break;
            case R.id.gongzhuo_frag_one_ib:
                Intent it_gongzhuo_one = new Intent(getActivity(), AboutCompanyActivity.class);
                it_gongzhuo_one.putExtra("about_company_title",gongzhuo_one_title);
                it_gongzhuo_one.putExtra("about_company_api",gongzhuo_one_api);
                startActivity(it_gongzhuo_one);
                break;
            case R.id.gongzhuo_frag_two_ib:
                Intent it_gongzhuo_two = new Intent(getActivity(), AboutCompanyActivity.class);
                it_gongzhuo_two.putExtra("about_company_title",gongzhuo_two_title);
                it_gongzhuo_two.putExtra("about_company_api",gongzhuo_two_api);
                startActivity(it_gongzhuo_two);
                break;
            case R.id.gongzhuo_frag_three_ib:
                Intent it_gongzhuo_three = new Intent(getActivity(), AboutCompanyActivity.class);
                it_gongzhuo_three.putExtra("about_company_title",gongzhuo_three_title);
                it_gongzhuo_three.putExtra("about_company_api",gongzhuo_three_api);
                startActivity(it_gongzhuo_three);
                break;
            case R.id.gongzhuo_frag_four_ib:
                Intent it_gongzhuo_four = new Intent(getActivity(), AboutCompanyActivity.class);
                it_gongzhuo_four.putExtra("about_company_title",gongzhuo_four_title);
                it_gongzhuo_four.putExtra("about_company_api",gongzhuo_four_api);
                startActivity(it_gongzhuo_four);
                break;
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
