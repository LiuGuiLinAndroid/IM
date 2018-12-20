package com.liuguilin.im.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.liuguilin.im.event.EventManager;
import com.liuguilin.im.event.MessageEvent;
import com.liuguilin.im.utils.LanguaueUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * FileName: BaseFragment
 * Founder: LiuGuiLin
 * Create Date: 2018/12/11 13:07
 * Email: lgl@szokl.com.cn
 * Profile: Base Fragment
 */

public class BaseFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initFragment();
    }

    private void initFragment() {
        EventBus.getDefault().register(this);
        LanguaueUtils.updateLanguaue(getActivity());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getType()) {
            case EventManager.EVENT_TYPE_UPDATE_LANGUAUE:
                LanguaueUtils.updateLanguaue(getActivity());
                getActivity().recreate();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}