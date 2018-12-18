package com.liuguilin.im.ui;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liuguilin.im.MainActivity;
import com.liuguilin.im.R;
import com.liuguilin.im.base.BaseActivity;
import com.liuguilin.im.utils.IMLog;
import com.liuguilin.im.utils.PictureUtils;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.File;

/**
 * FileName: ScanActivity
 * Founder: LiuGuiLin
 * Create Date: 2018/12/18 10:09
 * Email: lgl@szokl.com.cn
 * Profile: 扫一扫
 */
public class ScanActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQUEST_IMAGE = 1002;

    private CaptureFragment captureFragment;
    private RelativeLayout include_title_iv_back;
    private TextView include_title_text;
    private TextView title_right_text;
    private ImageView iv_light;

    private static boolean isOff = false;

    private Uri uri;
    private String path = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        initScan();
        initView();
    }

    //初始化二维码
    private void initScan() {
        captureFragment = new CaptureFragment();
        CodeUtils.setFragmentArgs(captureFragment, R.layout.layout_camera);
        captureFragment.setAnalyzeCallback(new CodeUtils.AnalyzeCallback() {
            @Override
            public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                putResult(result, CodeUtils.RESULT_SUCCESS);
            }

            @Override
            public void onAnalyzeFailed() {
                putResult("", CodeUtils.RESULT_FAILED);
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
    }

    /**
     * 结果回调
     *
     * @param result
     * @param values
     */
    private void putResult(String result, int values) {
        IMLog.i("result:" + result + "values:" + values);
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt(CodeUtils.RESULT_TYPE, values);
        bundle.putString(CodeUtils.RESULT_STRING, result);
        resultIntent.putExtras(bundle);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void initView() {
        include_title_iv_back = (RelativeLayout) findViewById(R.id.include_title_iv_back);
        include_title_text = (TextView) findViewById(R.id.include_title_text);
        title_right_text = (TextView) findViewById(R.id.title_right_text);
        iv_light = (ImageView) findViewById(R.id.iv_light);

        include_title_iv_back.setOnClickListener(this);
        title_right_text.setOnClickListener(this);
        iv_light.setOnClickListener(this);

        include_title_text.setText(getString(R.string.str_qrcode_scan_text));
        title_right_text.setText(getString(R.string.str_chat_more_album));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.include_title_iv_back:
                finish();
                break;
            case R.id.title_right_text:
                openAlbum();
                break;
            case R.id.iv_light:
                isOff = !isOff;
                iv_light.setBackgroundResource(isOff ? R.drawable.img_light_on : R.drawable.img_light_off);
                CodeUtils.isLightEnable(isOff);
                break;
        }
    }

    /**
     * 打开相册
     */
    private void openAlbum() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (data != null) {
                uri = data.getData();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    path = this.uri.getPath();
                    path = PictureUtils.getPath_above19(this, uri);
                } else {
                    path = PictureUtils.getFilePath_below19(this, this.uri);
                }
                if (!TextUtils.isEmpty(path)) {
                    //解析
                    CodeUtils.analyzeBitmap("", new CodeUtils.AnalyzeCallback() {

                        @Override
                        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                            putResult(result, CodeUtils.RESULT_SUCCESS);
                        }

                        @Override
                        public void onAnalyzeFailed() {
                            putResult("", CodeUtils.RESULT_FAILED);
                        }
                    });
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
