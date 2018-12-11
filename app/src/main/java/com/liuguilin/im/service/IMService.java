package com.liuguilin.im.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.liuguilin.im.im.IMSDK;
import com.liuguilin.im.utils.IMLog;

import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.v3.exception.BmobException;

/**
 * FileName: IMService
 * Founder: LiuGuiLin
 * Create Date: 2018/12/11 14:28
 * Email: lgl@szokl.com.cn
 * Profile: IM 服务
 */
public class IMService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initService();
    }

    private void initService() {
        //连接
        IMSDK.connect(new ConnectListener() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    IMLog.i("uid:" + s);
                } else {
                    IMLog.e("connect error");
                }
            }
        });
        //监听
        IMSDK.setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
            @Override
            public void onChange(ConnectionStatus connectionStatus) {
                IMLog.e("status:" + connectionStatus.getMsg());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        IMSDK.disConnect();
    }
}
