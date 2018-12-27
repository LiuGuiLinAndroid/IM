package com.liuguilin.im.ui;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.liuguilin.im.R;
import com.liuguilin.im.base.BaseActivity;
import com.liuguilin.im.utils.IMLog;

/**
 * FileName: VideoBrowseActivity
 * Founder: LiuGuiLin
 * Create Date: 2018/12/24 14:13
 * Email: lgl@szokl.com.cn
 * Profile:
 */
public class VideoBrowseActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout include_title_iv_back;
    private TextView include_title_text;
    private VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_browse);
        initView();
    }

    private void initView() {

        String url = getIntent().getStringExtra("text");

        include_title_iv_back = (RelativeLayout) findViewById(R.id.include_title_iv_back);
        include_title_text = (TextView) findViewById(R.id.include_title_text);
        mVideoView = (VideoView) findViewById(R.id.mVideoView);

        include_title_iv_back.setOnClickListener(this);

        include_title_text.setText(getString(R.string.str_brower_video));

        if(url.startsWith("http:")){
            mVideoView.setVideoURI(Uri.parse(url));
        }else{
            mVideoView.setVideoPath(url);
        }

        IMLog.i("url:" + url);

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //铺满
                mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                mVideoView.start();
            }
        });

        MediaController mediaController = new MediaController(this);
        mVideoView.setMediaController(mediaController);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.include_title_iv_back:
                finish();
                break;
        }
    }
}
