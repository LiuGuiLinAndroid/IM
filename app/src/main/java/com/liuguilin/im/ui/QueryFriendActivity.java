package com.liuguilin.im.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liuguilin.im.R;
import com.liuguilin.im.adapter.UniversalAdapter;
import com.liuguilin.im.adapter.UniversalViewHolder;
import com.liuguilin.im.base.BaseActivity;
import com.liuguilin.im.im.IMSDK;
import com.liuguilin.im.im.IMUser;
import com.liuguilin.im.utils.CommonUtils;
import com.liuguilin.im.utils.IMLog;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * FileName: QueryFriendActivity
 * Founder: LiuGuiLin
 * Create Date: 2018/12/12 14:28
 * Email: lgl@szokl.com.cn
 * Profile: 查询好友
 */
public class QueryFriendActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout include_title_iv_back;
    private TextView include_title_text;
    private EditText et_account;
    private Button btn_search;
    private RecyclerView mAddFriendRyView;
    private List<IMUser> mList = new ArrayList<>();
    private UniversalAdapter<IMUser> mAdapter;

    public static IMUser mClickimUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_friend);
        initView();
    }

    private void initView() {
        include_title_iv_back = (RelativeLayout) findViewById(R.id.include_title_iv_back);
        include_title_text = (TextView) findViewById(R.id.include_title_text);
        et_account = (EditText) findViewById(R.id.et_account);
        btn_search = (Button) findViewById(R.id.btn_search);
        mAddFriendRyView = (RecyclerView) findViewById(R.id.mAddFriendRyView);

        include_title_iv_back.setOnClickListener(this);
        btn_search.setOnClickListener(this);

        //l逻辑
        include_title_text.setText(getString(R.string.str_add_friend_title_text));

        mAddFriendRyView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new UniversalAdapter<>(mList, new UniversalAdapter.OnBindDataInterface<IMUser>() {
            @Override
            public void onBindData(IMUser model, UniversalViewHolder hodler, int type, final int position) {
                hodler.setImageResource(R.id.iv_sex, model.isSex() ? R.drawable.img_boy : R.drawable.img_girl);
                hodler.setText(R.id.tv_account, model.getUsername());
                String nickName = model.getNickname();
                if (!TextUtils.isEmpty(nickName)) {
                    hodler.setText(R.id.tv_niname, nickName);
                }
                BmobFile bmobFile = model.getAvatar();
                if (bmobFile != null) {
                    String fileUrl = bmobFile.getFileUrl();
                    if (!TextUtils.isEmpty(fileUrl)) {
                        hodler.setImageUrl(QueryFriendActivity.this, R.id.iv_user, fileUrl);
                    } else {
                        hodler.setImageResource(R.id.iv_user, R.drawable.img_def_photo);
                    }
                } else {
                    hodler.setImageResource(R.id.iv_user, R.drawable.img_def_photo);
                }

                //点击事件
                hodler.getSubView(R.id.ll_user).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mClickimUser = mList.get(position);
                        CommonUtils.startActivity(QueryFriendActivity.this,UserInfoActivity.class,false);
                    }
                });
            }

            @Override
            public int getItemLayoutId(int viewType) {
                return R.layout.view_list_add_friend;
            }
        });
        mAddFriendRyView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.include_title_iv_back:
                finish();
                break;
            case R.id.btn_search:
                final String account = et_account.getText().toString().trim();
                if (TextUtils.isEmpty(account)) {
                    CommonUtils.Toast(this, getString(R.string.str_toast_account_null));
                    return;
                }
                btn_search.setEnabled(false);
                if (mList != null) {
                    if (mList.size() > 0) {
                        mList.clear();
                        mAdapter.notifyDataSetChanged();
                    }
                }
                IMLog.i("account:" + account);
                IMSDK.queryFriend("username",account, new FindListener<IMUser>() {
                    @Override
                    public void done(List<IMUser> list, BmobException e) {
                        IMLog.i("list:" + list.size());
                        btn_search.setEnabled(true);
                        if (e == null) {
                            if (list.size() > 0) {
                                for (int i = 0; i < list.size(); i++) {
                                    IMUser imUser = list.get(i);
                                    IMLog.i(imUser.toString());
                                    //过滤自己
                                    if(!imUser.getUsername().equals(IMSDK.getCurrentUser().getUsername())){
                                        mList.add(imUser);
                                    }
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        } else {
                            IMLog.e(e.toString());
                        }
                    }
                });
                break;
        }
    }
}
