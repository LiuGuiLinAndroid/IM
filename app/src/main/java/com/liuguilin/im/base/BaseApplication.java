package com.liuguilin.im.base;

import android.app.Application;

import com.liuguilin.im.receiver.IMHandlerReceiver;
import com.liuguilin.im.utils.SharedPreUtils;

import org.litepal.LitePalApplication;
import org.litepal.util.SharedUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import cn.bmob.newim.BmobIM;

/**
 * FileName: BaseApplication
 * Founder: LiuGuiLin
 * Create Date: 2018/12/10 17:05
 * Email: lgl@szokl.com.cn
 * Profile: Base App
 */
public class BaseApplication extends LitePalApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        initApp();
    }

    private void initApp() {
        //init Bmob
        if (getApplicationInfo().packageName.equals(getMyProcessName())){
            BmobIM.init(this);
            BmobIM.registerDefaultMessageHandler(new IMHandlerReceiver(this));
            SharedPreUtils.getInstance().init(this);
        }
    }

    /**
     * 获取当前运行的进程名
     *
     * @return
     */
    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
