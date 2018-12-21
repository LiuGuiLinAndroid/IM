package com.liuguilin.im.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTitleStrip;
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
public class NewsFragment extends BaseFragment {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private NewsAdapter mNewsAdapter;

    private String[] mStrTitle;
    private String[] mStrTitleEn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, null);
        initView(view);
        return view;
    }

    private void initView(View view) {

        mStrTitle = getResources().getStringArray(R.array.news_title);
        mStrTitleEn = getResources().getStringArray(R.array.news_title_en);

        mTabLayout = (TabLayout) view.findViewById(R.id.mTabLayout);
        mViewPager = (ViewPager) view.findViewById(R.id.mViewPager);

        for (int i = 0; i < mStrTitle.length; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(mStrTitle[i]));
        }

        mViewPager.setOffscreenPageLimit(mStrTitle.length);
        mNewsAdapter = new NewsAdapter(getFragmentManager());
        mViewPager.setAdapter(mNewsAdapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    class NewsAdapter extends FragmentPagerAdapter {

        public NewsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mStrTitle[position];
        }

        @Override
        public Fragment getItem(int i) {
            NewsContentFragment contentFragment = new NewsContentFragment();
            Bundle bundle = new Bundle();
            bundle.putString("title", mStrTitleEn[i]);
            contentFragment.setArguments(bundle);
            return contentFragment;
        }

        @Override
        public int getCount() {
            return mStrTitle.length;
        }
    }
}
