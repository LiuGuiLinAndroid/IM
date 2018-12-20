package com.liuguilin.im.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kymjs.rxvolley.client.HttpCallback;
import com.liuguilin.im.R;
import com.liuguilin.im.base.BaseFragment;
import com.liuguilin.im.http.HttpHelper;
import com.liuguilin.im.utils.IMLog;

import java.util.logging.XMLFormatter;

/**
 * FileName: NewsFragment
 * Founder: LiuGuiLin
 * Create Date: 2018/12/11 13:21
 * Email: lgl@szokl.com.cn
 * Profile: 资讯
 */
public class NewsFragment extends BaseFragment implements ViewPager.OnPageChangeListener, TabLayout.BaseOnTabSelectedListener {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mTabLayout = (TabLayout) view.findViewById(R.id.mTabLayout);
        mViewPager = (ViewPager) view.findViewById(R.id.mViewPager);

        mViewPager.addOnPageChangeListener(this);
        mTabLayout.addOnTabSelectedListener(this);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    class NewsAdapter extends XMLFormatter{

    }
}
