package com.xj.af.index;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.xj.af.R;
import com.xj.af.common.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;

import cn.edu.sjzc.fanyafeng.activity.EventsActivity;
import cn.edu.sjzc.fanyafeng.fragment.MainTabActivity;

/**
 * 首页
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IndexFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IndexFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IndexFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private GridView gridView;
    private ImageView iv ;
    private  SimpleAdapter saImageItems;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    public Animation loadAnimation() {
        return AnimationUtils.loadAnimation(getActivity(), R.anim.translate);
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IndexFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IndexFragment newInstance(String param1, String param2) {
        IndexFragment fragment = new IndexFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public IndexFragment() {
        // Required empty public constructor
    }

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);
        setContentView(R.layout.fragment_index);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d("xj","===================onViewCreated()");
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d("xj","===================onActivityCreated()");
        super.onActivityCreated(savedInstanceState);
        gridView = (GridView)getActivity().findViewById(R.id.index_gridView);
        Log.d("xj","=========gridView="+gridView);
        gridView.setAdapter(saImageItems);
        gridView.setOnItemClickListener(new ItemClickListener());



        Log.d("xj", this + " finsh");

    }
    class  ItemClickListener implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            // 在本例中arg2=arg3
            HashMap<String, Object> item = (HashMap<String, Object>) arg0
                    .getItemAtPosition(arg2);
            // 显示所选Item的ItemText
            String txt = (String) item.get("ItemText");
            if("寺庙".equals(txt)){
               // Intent i = new Intent(getActivity(),SiMiaoActivity.class);
                Intent i = new Intent(getActivity(),TabActivity.class);
                i.putExtra(TabActivity.key, TabActivity.SiMiao);
                startActivity(i);
            }else if("佛事".equals(txt)){
               // Intent i = new Intent(getActivity(),FoShiActivity.class);
                Intent i = new Intent(getActivity(),TabFixActivity.class);
                i.putExtra(TabActivity.key, TabFixActivity.FoShi);
                startActivity(i);
                //overridePendingTransition(R.anim.left_start, R.anim.left_end);
            }else if("讲堂".equals(txt)){
                Intent i = new Intent(getActivity(),TabActivity.class);
                i.putExtra(TabActivity.key, TabActivity.JiangTang);
                startActivity(i);
            }else if("发现".equals(txt)){
               // Intent i = new Intent(getActivity(),SiMiaoActivity.class);
               // i.putExtra("enName","jingDianJieDu");
               // i.putExtra("title","经典解读" );
               // startActivity(i);
                Intent i = new Intent(getActivity(), MainTabActivity.class);
                startActivity(i);
//                Toast.makeText(getActivity(), "马上开通，敬请期待", Toast.LENGTH_SHORT).show();
            }
            else if("活动".equals(txt)){
               // Intent i = new Intent(getActivity(),SiMiaoActivity.class);
               // i.putExtra("url", getServerURL()+"/m/news/newsDetail/enName/dangJiaZhuChi/"+getUnitId());
               // i.putExtra("title", "当家主持");
               // startActivity(i);

                Intent i=new Intent(getActivity(), EventsActivity.class);
                startActivity(i);

            }else if("结缘".equals(txt)){
                Intent i = new Intent(getActivity(),TabFixActivity.class);
                i.putExtra(TabActivity.key, TabFixActivity.JieYuan);
                startActivity(i);
            }
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



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String str) {
        if (mListener != null) {
            mListener.onFragmentInteraction(str);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        Log.d("xj","======================onAttach()");
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
        String wenzi[] = {"寺庙","佛事","讲堂","发现","活动","结缘"};
        int pics[]={R.drawable.index_ico_simiao,R.drawable.index_ico1,R.drawable.index_ico9,R.drawable.index_ico4,R.drawable.index_ico7,R.drawable.index_ico3,R.drawable.index_ico7,R.drawable.index_ico8,R.drawable.index_ico9};
        for(int i=0;i<6;i++)  {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImage", pics[i]);//添加图像资源的ID
            map.put("ItemText", wenzi[i]);//
            lstImageItem.add(map);
        }
        saImageItems = new SimpleAdapter(getActivity(),
                lstImageItem,
                R.layout.index_item,
                new String[] {"ItemImage","ItemText"},
                new int[] {R.id.index_ItemImage,R.id.index_ItemText}
        );
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
        public void onFragmentInteraction(String str);
    }

}
