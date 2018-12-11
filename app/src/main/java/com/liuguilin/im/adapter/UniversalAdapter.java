package com.liuguilin.im.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * FileName: UniversalAdapter
 * Founder: LiuGuiLin
 * Create Date: 2018/12/11 15:09
 * Email: lgl@szokl.com.cn
 * Profile: 万能适配器
 */
public class UniversalAdapter<T> extends RecyclerView.Adapter<UniversalViewHolder> {

    private List<T> mData;

    private OnBindDataInterface mOnBindDataInterface;
    private OnMultiTypeBindDataInterface<T> mOnMultiTypeBindDataInterface;

    public UniversalAdapter(List<T> mData, OnBindDataInterface mOnBindDataInterface) {
        this.mData = mData;
        this.mOnBindDataInterface = mOnBindDataInterface;
    }

    public UniversalAdapter(List<T> mData, OnMultiTypeBindDataInterface<T> mOnMultiTypeBindDataInterface) {
        this.mData = mData;
        this.mOnBindDataInterface = mOnMultiTypeBindDataInterface;
        this.mOnMultiTypeBindDataInterface = mOnMultiTypeBindDataInterface;
    }

    @Override
    public UniversalViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        int layoutId = mOnBindDataInterface.getItemLayoutId(i);
        UniversalViewHolder viewHolder = UniversalViewHolder.getViewHolder(viewGroup, layoutId);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UniversalViewHolder universalViewHolder, int i) {
        mOnBindDataInterface.onBindData(mData.get(i), universalViewHolder, getItemViewType(i), i);
    }

    @Override
    public int getItemViewType(int position) {
        if (mOnMultiTypeBindDataInterface != null) {
            return mOnMultiTypeBindDataInterface.getItemViewType(position);
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public interface OnBindDataInterface<T> {

        void onBindData(T model, UniversalViewHolder hodler, int type, int position);

        int getItemLayoutId(int viewType);
    }

    /**
     * 多类型支持
     *
     * @param <T>
     */
    public interface OnMultiTypeBindDataInterface<T> extends OnBindDataInterface<T> {

        int getItemViewType(int postion);
    }
}
