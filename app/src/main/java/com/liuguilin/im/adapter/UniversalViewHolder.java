package com.liuguilin.im.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.liuguilin.im.R;
import com.liuguilin.im.utils.GlideUtils;

/**
 * FileName: UniversalViewHolder
 * Founder: LiuGuiLin
 * Create Date: 2018/12/11 15:10
 * Email: lgl@szokl.com.cn
 * Profile: 万能缓冲组件
 */
public class UniversalViewHolder extends RecyclerView.ViewHolder {

    //子View集合
    private SparseArray<View> mViews;
    private View mContentView;

    /**
     * 获取当前缓冲组件
     *
     * @param parent
     * @param layoutId
     * @return
     */
    public static UniversalViewHolder getViewHolder(ViewGroup parent, int layoutId) {
        return new UniversalViewHolder((View.inflate(parent.getContext(), layoutId, null)));
    }

    /**
     * 构造函数
     *
     * @param itemView
     */
    public UniversalViewHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
        mContentView = itemView;
    }

    /**
     * 子View访问
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getSubView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mContentView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public UniversalViewHolder setText(int viewId, String text) {
        TextView tv = getSubView(viewId);
        if (!TextUtils.isEmpty(text)) {
            tv.setText(text);
        }
        return this;
    }

    public UniversalViewHolder setImageUrl(Context mContext, int viewId, String url) {
        ImageView iv = getSubView(viewId);
        if (!TextUtils.isEmpty(url)) {
            GlideUtils.loadImg(mContext, url,R.drawable.img_def_photo, iv);
        }
        return this;
    }

    public UniversalViewHolder setImageResource(int viewId, int resId) {
        ImageView iv = getSubView(viewId);
        iv.setImageResource(resId);
        return this;
    }
}
