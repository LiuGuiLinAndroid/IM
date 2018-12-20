package com.liuguilin.im.base;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.liuguilin.im.event.EventManager;
import com.liuguilin.im.event.MessageEvent;
import com.liuguilin.im.utils.LanguaueUtils;
import com.liuguilin.im.utils.SharedPreUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * FileName: BaseActivity
 * Founder: LiuGuiLin
 * Create Date: 2018/12/10 17:19
 * Email: lgl@szokl.com.cn
 * Profile: Base Activity
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initActivity();

    }

    private void initActivity() {
        EventBus.getDefault().register(this);
        LanguaueUtils.updateLanguaue(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getType()) {
            case EventManager.EVENT_TYPE_NEW_FIREND:
                LanguaueUtils.updateLanguaue(this);
                recreate();
                break;
        }
    }
}
