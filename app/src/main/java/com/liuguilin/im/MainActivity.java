package com.liuguilin.im;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liuguilin.im.base.BaseActivity;
import com.liuguilin.im.entity.Constants;
import com.liuguilin.im.event.EventManager;
import com.liuguilin.im.event.MessageEvent;
import com.liuguilin.im.fragment.FriendFragment;
import com.liuguilin.im.fragment.MeFragment;
import com.liuguilin.im.fragment.NewsFragment;
import com.liuguilin.im.fragment.SessionFragment;
import com.liuguilin.im.im.IMSDK;
import com.liuguilin.im.im.IMUser;
import com.liuguilin.im.im.NewFriend;
import com.liuguilin.im.manager.DialogManager;
import com.liuguilin.im.service.IMService;
import com.liuguilin.im.ui.QueryFriendActivity;
import com.liuguilin.im.ui.ScanActivity;
import com.liuguilin.im.ui.SettingActivity;
import com.liuguilin.im.ui.UserInfoActivity;
import com.liuguilin.im.utils.CommonUtils;
import com.liuguilin.im.utils.IMLog;
import com.liuguilin.im.utils.PermissionUtils;
import com.liuguilin.im.view.DialogView;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static final int REQUEST_CODE = 1001;

    private RelativeLayout include_title_iv_back;
    private TextView include_title_text;
    private TextView title_right_text;
    private ImageView iv_session;
    private TextView tv_session;
    private LinearLayout ll_session;
    private ImageView iv_friend;
    private TextView tv_friend;
    private LinearLayout ll_friend;
    private ImageView iv_news;
    private TextView tv_news;
    private LinearLayout ll_news;
    private ImageView iv_me;
    private TextView tv_me;
    private LinearLayout ll_me;

    private SessionFragment mSessionFragment;
    private FragmentTransaction mSessionTransaction;

    private FriendFragment mFriendFragment;
    private FragmentTransaction mFriendTransaction;

    private NewsFragment mNewsFragment;
    private FragmentTransaction mNewsTransaction;

    private MeFragment mMeFragment;
    private FragmentTransaction mMeTransaction;

    private DialogView mMenuDialog;
    private TextView tv_add_friend;
    private TextView tv_scan;
    private TextView tv_unread_size;
    private ImageView iv_new_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {

        checkPermisson();
        checkBindPhoto();
        initFragment();
        initMenuDialog();

        include_title_iv_back = (RelativeLayout) findViewById(R.id.include_title_iv_back);
        include_title_text = (TextView) findViewById(R.id.include_title_text);
        title_right_text = (TextView) findViewById(R.id.title_right_text);

        iv_session = (ImageView) findViewById(R.id.iv_session);
        tv_session = (TextView) findViewById(R.id.tv_session);
        ll_session = (LinearLayout) findViewById(R.id.ll_session);

        iv_friend = (ImageView) findViewById(R.id.iv_friend);
        tv_friend = (TextView) findViewById(R.id.tv_friend);
        ll_friend = (LinearLayout) findViewById(R.id.ll_friend);

        iv_news = (ImageView) findViewById(R.id.iv_news);
        tv_news = (TextView) findViewById(R.id.tv_news);
        ll_news = (LinearLayout) findViewById(R.id.ll_news);

        iv_me = (ImageView) findViewById(R.id.iv_me);
        tv_me = (TextView) findViewById(R.id.tv_me);
        ll_me = (LinearLayout) findViewById(R.id.ll_me);

        tv_unread_size = (TextView) findViewById(R.id.tv_unread_size);
        iv_new_msg = (ImageView) findViewById(R.id.iv_new_msg);

        title_right_text.setOnClickListener(this);
        ll_session.setOnClickListener(this);
        ll_friend.setOnClickListener(this);
        ll_news.setOnClickListener(this);
        ll_me.setOnClickListener(this);

        //逻辑部分
        include_title_iv_back.setVisibility(View.GONE);

        checkMainTab(0);

        CommonUtils.startService(this, IMService.class);

        getAllUnReadCount();
    }

    /**
     * 检查权限
     */
    private void checkPermisson() {
        PermissionUtils.requestAllPermissions(this, 100);
    }

    /**
     * 检查绑定手机
     */
    private void checkBindPhoto() {
        IMUser imUser = IMSDK.getCurrentUser();
        String phone = imUser.getMobilePhoneNumber();
        IMLog.i("phone:" + phone);
        if (TextUtils.isEmpty(phone)) {
            imUser.setMobilePhoneNumber(imUser.getUsername());
            imUser.setMobilePhoneNumberVerified(true);
            IMSDK.updateUser(imUser, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e != null) {
                        IMLog.e(e.toString());
                    }
                }
            });
        }
    }

    private void initMenuDialog() {
        mMenuDialog = DialogManager.getInstance().initView(this, R.layout.dialog_mian_menu);
        tv_add_friend = (TextView) mMenuDialog.findViewById(R.id.tv_add_friend);
        tv_scan = (TextView) mMenuDialog.findViewById(R.id.tv_scan);

        tv_add_friend.setOnClickListener(this);
        tv_scan.setOnClickListener(this);
    }

    private void initFragment() {

        if (mSessionFragment == null) {
            mSessionTransaction = getSupportFragmentManager().beginTransaction();
            mSessionFragment = new SessionFragment();
            mSessionTransaction.add(R.id.mMainLayout, mSessionFragment);
            mSessionTransaction.commit();
        }

        if (mFriendFragment == null) {
            mFriendTransaction = getSupportFragmentManager().beginTransaction();
            mFriendFragment = new FriendFragment();
            mFriendTransaction.add(R.id.mMainLayout, mFriendFragment);
            mFriendTransaction.commit();
        }

        if (mNewsFragment == null) {
            mNewsTransaction = getSupportFragmentManager().beginTransaction();
            mNewsFragment = new NewsFragment();
            mNewsTransaction.add(R.id.mMainLayout, mNewsFragment);
            mNewsTransaction.commit();
        }

        if (mMeFragment == null) {
            mMeTransaction = getSupportFragmentManager().beginTransaction();
            mMeFragment = new MeFragment();
            mMeTransaction.add(R.id.mMainLayout, mMeFragment);
            mMeTransaction.commit();
        }
    }

    /**
     * 显示
     *
     * @param fragment
     */
    private void showFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            hideAllFragment(transaction);
            transaction.show(fragment);
            transaction.commitAllowingStateLoss();
            Constants.CURRENT_FRAGMENT = fragment;
        }
    }

    /**
     * 隐藏
     *
     * @param transaction
     */
    private void hideAllFragment(FragmentTransaction transaction) {
        if (mSessionFragment != null) {
            transaction.hide(mSessionFragment);
        }
        if (mFriendFragment != null) {
            transaction.hide(mFriendFragment);
        }
        if (mNewsFragment != null) {
            transaction.hide(mNewsFragment);
        }
        if (mMeFragment != null) {
            transaction.hide(mMeFragment);
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (mSessionFragment == null && fragment instanceof SessionFragment) {
            mSessionFragment = (SessionFragment) fragment;
        }
        if (mFriendFragment == null && fragment instanceof FriendFragment) {
            mFriendFragment = (FriendFragment) fragment;
        }
        if (mNewsFragment == null && fragment instanceof NewsFragment) {
            mNewsFragment = (NewsFragment) fragment;
        }
        if (mMeFragment == null && fragment instanceof MeFragment) {
            mMeFragment = (MeFragment) fragment;
        }
    }

    /**
     * 切换主页选项卡
     *
     * @param index
     */
    private void checkMainTab(int index) {
        switch (index) {
            case 0:
                showFragment(mSessionFragment);

                iv_session.setImageResource(R.drawable.img_session_b);
                iv_friend.setImageResource(R.drawable.img_firend_a);
                iv_news.setImageResource(R.drawable.img_news_a);
                iv_me.setImageResource(R.drawable.img_me_a);

                tv_session.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_friend.setTextColor(getResources().getColor(R.color.color_black));
                tv_news.setTextColor(getResources().getColor(R.color.color_black));
                tv_me.setTextColor(getResources().getColor(R.color.color_black));

                include_title_text.setText(getString(R.string.str_main_title_session));

                title_right_text.setVisibility(View.VISIBLE);
                title_right_text.setText(getString(R.string.str_main_title_add));

                break;
            case 1:
                showFragment(mFriendFragment);

                iv_session.setImageResource(R.drawable.img_session_a);
                iv_friend.setImageResource(R.drawable.img_firend_b);
                iv_news.setImageResource(R.drawable.img_news_a);
                iv_me.setImageResource(R.drawable.img_me_a);

                tv_session.setTextColor(getResources().getColor(R.color.color_black));
                tv_friend.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_news.setTextColor(getResources().getColor(R.color.color_black));
                tv_me.setTextColor(getResources().getColor(R.color.color_black));

                include_title_text.setText(getString(R.string.str_main_title_friend));

                title_right_text.setVisibility(View.GONE);

                break;
            case 2:
                showFragment(mNewsFragment);

                iv_session.setImageResource(R.drawable.img_session_a);
                iv_friend.setImageResource(R.drawable.img_firend_a);
                iv_news.setImageResource(R.drawable.img_news_b);
                iv_me.setImageResource(R.drawable.img_me_a);

                tv_session.setTextColor(getResources().getColor(R.color.color_black));
                tv_friend.setTextColor(getResources().getColor(R.color.color_black));
                tv_news.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_me.setTextColor(getResources().getColor(R.color.color_black));

                include_title_text.setText(getString(R.string.str_main_title_news));

                title_right_text.setVisibility(View.GONE);

                break;
            case 3:
                showFragment(mMeFragment);

                iv_session.setImageResource(R.drawable.img_session_a);
                iv_friend.setImageResource(R.drawable.img_firend_a);
                iv_news.setImageResource(R.drawable.img_news_a);
                iv_me.setImageResource(R.drawable.img_me_b);

                tv_session.setTextColor(getResources().getColor(R.color.color_black));
                tv_friend.setTextColor(getResources().getColor(R.color.color_black));
                tv_news.setTextColor(getResources().getColor(R.color.color_black));
                tv_me.setTextColor(getResources().getColor(R.color.colorPrimary));

                include_title_text.setText(getString(R.string.str_main_title_me));

                title_right_text.setVisibility(View.VISIBLE);
                title_right_text.setText(getString(R.string.str_main_title_more));

                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_right_text:
                if (Constants.CURRENT_FRAGMENT instanceof SessionFragment) {
                    DialogManager.getInstance().show(mMenuDialog);
                } else if (Constants.CURRENT_FRAGMENT instanceof MeFragment) {
                    CommonUtils.startActivity(this, SettingActivity.class, false);
                }
                break;
            case R.id.tv_add_friend:
                DialogManager.getInstance().hide(mMenuDialog);
                CommonUtils.startActivity(this, QueryFriendActivity.class, false);
                break;
            case R.id.tv_scan:
                DialogManager.getInstance().hide(mMenuDialog);
                //启动二维码
                Intent intent = new Intent(this, ScanActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.ll_session:
                checkMainTab(0);
                break;
            case R.id.ll_friend:
                checkMainTab(1);
                break;
            case R.id.ll_news:
                checkMainTab(2);
                break;
            case R.id.ll_me:
                checkMainTab(3);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getType()) {
            case EventManager.EVENT_TYPE_NEW_FIREND:
                iv_new_msg.setVisibility(View.VISIBLE);
                break;
            case EventManager.EVENT_TYPE_NEW_FIREND_UN:
                iv_new_msg.setVisibility(View.GONE);
                break;
            case EventManager.EVENT_TYPE_MSG_EVENT:
            case EventManager.EVENT_TYPE_MAIN_SIZE:
                //新消息更新未读数量
                getAllUnReadCount();
                break;
            case EventManager.EVENT_TYPE_MAIN_EXIT:
                finish();
                break;
        }
    }

    /**
     * 查询全部的未读数量
     */
    private void getAllUnReadCount() {
        long size = IMSDK.getAllUnReadCount();
        IMLog.i("getAllUnReadCount:" + size);
        if (size > 0) {
            tv_unread_size.setText("" + size);
            tv_unread_size.setVisibility(View.VISIBLE);
        } else {
            tv_unread_size.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    if (!TextUtils.isEmpty(result)) {
                        //用户名
                        IMSDK.queryFriend("username", result, new FindListener<IMUser>() {
                            @Override
                            public void done(List<IMUser> list, BmobException e) {
                                if (e == null) {
                                    if (list.size() > 0) {
                                        IMUser imUser = list.get(0);
                                        IMLog.i(imUser.toString());
                                        //过滤自己
                                        if (!imUser.getUsername().equals(IMSDK.getCurrentUser().getUsername())) {
                                            Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
                                            intent.putExtra("user", imUser);
                                            startActivity(intent);
                                        } else {
                                            CommonUtils.Toast(MainActivity.this, getString(R.string.str_main_scan_you));
                                        }
                                    } else {
                                        toastScanFail();
                                    }
                                } else {
                                    IMLog.e(e.toString());
                                    toastScanFail();
                                }
                            }
                        });
                    } else {
                        toastScanFail();
                    }
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    toastScanFail();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void toastScanFail() {
        CommonUtils.Toast(this, getString(R.string.str_toast_qrcode_fail));
    }
}
