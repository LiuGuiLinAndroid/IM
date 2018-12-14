package com.liuguilin.im.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * FileName: NullReceiverView
 * Founder: LiuGuiLin
 * Create Date: 2018/12/14 13:14
 * Email: lgl@szokl.com.cn
 * Profile: 空布局的RyView
 */
public class NullReceiverView extends RecyclerView {

    private View mEmptyView;

    public NullReceiverView(Context context) {
        super(context);
    }

    public NullReceiverView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NullReceiverView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setEmptyView(View mEmptyView) {
        this.mEmptyView = mEmptyView;
    }

    private AdapterDataObserver emptyObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            Adapter<?> adapter = getAdapter();
            if (adapter != null && mEmptyView != null) {
                if (adapter.getItemCount() == 0) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    NullReceiverView.this.setVisibility(View.GONE);
                } else {
                    mEmptyView.setVisibility(View.GONE);
                    NullReceiverView.this.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(emptyObserver);
        }
        emptyObserver.onChanged();
    }
}
