package com.liuguilin.im.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

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
     * @param parent
     * @param layoutId
     * @return
     */
    public static UniversalViewHolder getViewHolder(ViewGroup parent, int layoutId) {
        return new UniversalViewHolder((View.inflate(parent.getContext(), layoutId, null)));
    }

    /**
     * 构造函数
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
}
