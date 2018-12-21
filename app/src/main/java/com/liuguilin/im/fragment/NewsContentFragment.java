package com.liuguilin.im.fragment;

import android.arch.lifecycle.ViewModelStoreOwner;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kymjs.rxvolley.client.HttpCallback;
import com.liuguilin.im.R;
import com.liuguilin.im.adapter.UniversalAdapter;
import com.liuguilin.im.adapter.UniversalViewHolder;
import com.liuguilin.im.base.BaseFragment;
import com.liuguilin.im.bean.NewsBean;
import com.liuguilin.im.http.HttpHelper;
import com.liuguilin.im.model.NewsModel;
import com.liuguilin.im.ui.NewsContentActivity;
import com.liuguilin.im.utils.CommonUtils;
import com.liuguilin.im.utils.IMLog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * FileName: NewsContentFragment
 * Founder: LiuGuiLin
 * Create Date: 2018/12/20 17:18
 * Email: lgl@szokl.com.cn
 * Profile:新闻详情
 */
public class NewsContentFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mNewsRyView;
    private String title;
    private SwipeRefreshLayout mSwLayout;

    private UniversalAdapter<NewsBean> mAdapter;
    private List<NewsBean> mList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_content, null);
        Bundle bundle = getArguments();
        title = bundle.getString("title");
        IMLog.i("title:" + title);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mNewsRyView = (RecyclerView) view.findViewById(R.id.mNewsRyView);
        mSwLayout = (SwipeRefreshLayout) view.findViewById(R.id.mSwLayout);
        mSwLayout.setOnRefreshListener(this);

        mNewsRyView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new UniversalAdapter<>(mList, new UniversalAdapter.OnBindDataInterface<NewsBean>() {
            @Override
            public void onBindData(NewsBean model, UniversalViewHolder hodler, int type, final int position) {
                hodler.setText(R.id.tv_type, model.getType());
                hodler.setText(R.id.tv_title, model.getTitle());
                hodler.setText(R.id.tv_author, model.getAuthor());
                hodler.setText(R.id.tv_time, model.getTime());

                String url1 = model.getUrl1();
                if (!TextUtils.isEmpty(url1)) {
                    hodler.setVisibility(R.id.iv_img1, View.VISIBLE);
                    hodler.setImageUrl(getActivity(), R.id.iv_img1, R.drawable.img_load_img, url1);
                } else {
                    hodler.setVisibility(R.id.iv_img1, View.INVISIBLE);
                }

                String url2 = model.getUrl2();
                if (!TextUtils.isEmpty(url2)) {
                    hodler.setVisibility(R.id.iv_img2, View.VISIBLE);
                    hodler.setImageUrl(getActivity(), R.id.iv_img2, R.drawable.img_load_img, url2);
                } else {
                    hodler.setVisibility(R.id.iv_img2, View.INVISIBLE);
                }

                String url3 = model.getUrl3();
                if (!TextUtils.isEmpty(url3)) {
                    hodler.setVisibility(R.id.iv_img3, View.VISIBLE);
                    hodler.setImageUrl(getActivity(), R.id.iv_img3, R.drawable.img_load_img, url3);
                } else {
                    hodler.setVisibility(R.id.iv_img3, View.INVISIBLE);
                }

                hodler.getSubView(R.id.ll_news).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getActivity(), NewsContentActivity.class);
                        intent.putExtra("url", mList.get(position).getUrl());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public int getItemLayoutId(int viewType) {
                return R.layout.view_list_new_content;
            }
        });
        mNewsRyView.setAdapter(mAdapter);

        getNews();
    }

    private void getNews() {
        mSwLayout.setRefreshing(true);
        HttpHelper.requestNews(title, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                mSwLayout.setRefreshing(false);
                parsingJson(t);
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                mSwLayout.setRefreshing(false);
                IMLog.e("errorNo:" + errorNo + "strMsg: " + strMsg);
            }
        });
    }

    private void parsingJson(String t) {
        if (TextUtils.isEmpty(t)) {
            return;
        }
        IMLog.i("json:" + t);

        if (mList.size() > 0) {
            mList.clear();
        }

        Gson gson = new Gson();
        NewsModel model = gson.fromJson(t, NewsModel.class);

        NewsModel.ResultBean resultBean = model.getResult();

        if (resultBean == null) {
            return;
        }

        List<NewsModel.ResultBean.DataBean> mDataList = resultBean.getData();
        for (int i = 0; i < mDataList.size(); i++) {
            NewsModel.ResultBean.DataBean b = mDataList.get(i);
            NewsBean bean = new NewsBean();
            bean.setType(b.getCategory());
            bean.setTitle(b.getTitle());
            bean.setTime(b.getDate());
            bean.setAuthor(b.getAuthor_name());
            bean.setUrl(b.getUrl());
            bean.setUrl1(b.getThumbnail_pic_s());
            bean.setUrl2(b.getThumbnail_pic_s02());
            bean.setUrl3(b.getThumbnail_pic_s03());
            mList.add(bean);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        getNews();
    }
}
