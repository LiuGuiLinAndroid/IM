package com.liuguilin.im.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liuguilin.im.R;
import com.liuguilin.im.base.BaseActivity;
import com.liuguilin.im.im.IMSDK;
import com.liuguilin.im.im.IMUser;
import com.liuguilin.im.manager.DialogManager;
import com.liuguilin.im.utils.CommonUtils;
import com.liuguilin.im.utils.GlideUtils;
import com.liuguilin.im.utils.IMLog;
import com.liuguilin.im.utils.PictureUtils;
import com.liuguilin.im.view.DialogView;
import com.liuguilin.im.view.LodingView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * FileName: UserEditActivity
 * Founder: LiuGuiLin
 * Create Date: 2018/12/11 16:28
 * Email: lgl@szokl.com.cn
 * Profile: 编辑信息
 */
public class UserEditActivity extends BaseActivity implements View.OnClickListener {

    private static int TAKEPHOTO = 1000;
    private static int TAKEALBUM = 1001;

    private RelativeLayout include_title_iv_back;
    private TextView include_title_text;
    private TextView title_right_text;
    private CircleImageView iv_photo;
    private LinearLayout ll_select_photo;
    private EditText et_nickname;
    private TextView tv_sex;
    private EditText et_age;
    private TextView tv_birthday;
    private EditText et_phone;
    private EditText et_desc;
    private TextView tv_city;

    private LinearLayout ll_birthday;
    private LinearLayout ll_city;
    private LinearLayout ll_sex;

    private DialogView mPhotoDialog;
    private TextView tv_camera;
    private TextView tv_ablum;
    private TextView tv_cancel;

    private File tempFile;
    private Uri imageUri;
    private File uploadPhotoFile;

    private Uri uri;
    private String path = "";

    private DialogView mSexDialog;
    private TextView tv_boy;
    private TextView tv_girl;
    private TextView tv_sex_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);

        initView();
    }

    private void initView() {

        initPhotoDialog();
        initSexDialog();
        LodingView.getInstance().initView(this);

        include_title_iv_back = (RelativeLayout) findViewById(R.id.include_title_iv_back);
        include_title_text = (TextView) findViewById(R.id.include_title_text);
        title_right_text = (TextView) findViewById(R.id.title_right_text);

        iv_photo = (CircleImageView) findViewById(R.id.iv_photo);
        ll_select_photo = (LinearLayout) findViewById(R.id.ll_select_photo);

        et_nickname = (EditText) findViewById(R.id.et_nickname);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        et_age = (EditText) findViewById(R.id.et_age);
        tv_birthday = (TextView) findViewById(R.id.tv_birthday);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_desc = (EditText) findViewById(R.id.et_desc);
        tv_city = (TextView) findViewById(R.id.tv_city);

        ll_birthday = (LinearLayout) findViewById(R.id.ll_birthday);
        ll_city = (LinearLayout) findViewById(R.id.ll_city);
        ll_sex = (LinearLayout) findViewById(R.id.ll_sex);

        include_title_iv_back.setOnClickListener(this);
        ll_select_photo.setOnClickListener(this);
        title_right_text.setOnClickListener(this);
        ll_sex.setOnClickListener(this);
        ll_birthday.setOnClickListener(this);
        ll_city.setOnClickListener(this);

        //逻辑部分
        include_title_text.setText(getString(R.string.str_user_edit_user));
        title_right_text.setText(getString(R.string.str_common_save));

        //更新信息
        updateUser();
    }

    private void updateUser() {
        IMUser imUser = IMSDK.getCurrentUser();
        if (imUser != null) {
            BmobFile bmobFile = imUser.getAvatar();
            if (bmobFile != null) {
                String fileUrl = bmobFile.getFileUrl();
                if (!TextUtils.isEmpty(fileUrl)) {
                    GlideUtils.loadImg(this, fileUrl,R.drawable.img_def_photo, iv_photo);
                }
            }
            String nickName = imUser.getNickname();
            if (!TextUtils.isEmpty(nickName)) {
                et_nickname.setText(nickName);
            }
            boolean sex = imUser.isSex();
            tv_sex.setText(sex ? getString(R.string.str_common_boy) : getString(R.string.str_common_girl));
            int age = imUser.getAge();
            et_age.setText(String.valueOf(age));
            String birthday = imUser.getBirthday();
            if (!TextUtils.isEmpty(birthday)) {
                tv_birthday.setText(birthday);
            }
            String phone = imUser.getPhone();
            if (!TextUtils.isEmpty(phone)) {
                tv_birthday.setText(phone);
            }
            et_desc.setText(imUser.getDesc());
            et_phone.setText(imUser.getUsername());
        }
    }

    private void initSexDialog() {
        mSexDialog = DialogManager.getInstance().initView(this, R.layout.dialog_user_sex, Gravity.BOTTOM);

        tv_boy = (TextView) mSexDialog.findViewById(R.id.tv_boy);
        tv_girl = (TextView) mSexDialog.findViewById(R.id.tv_girl);
        tv_sex_cancel = (TextView) mSexDialog.findViewById(R.id.tv_sex_cancel);

        tv_boy.setOnClickListener(this);
        tv_girl.setOnClickListener(this);
        tv_sex_cancel.setOnClickListener(this);

    }

    private void initPhotoDialog() {
        mPhotoDialog = DialogManager.getInstance().initView(this, R.layout.dialog_user_photo, Gravity.BOTTOM);

        tv_camera = (TextView) mPhotoDialog.findViewById(R.id.tv_camera);
        tv_ablum = (TextView) mPhotoDialog.findViewById(R.id.tv_ablum);
        tv_cancel = (TextView) mPhotoDialog.findViewById(R.id.tv_cancel);

        tv_camera.setOnClickListener(this);
        tv_ablum.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.include_title_iv_back:
                finish();
                break;
            case R.id.ll_select_photo:
                DialogManager.getInstance().show(mPhotoDialog);
                break;
            case R.id.title_right_text:
                saveUserInfo();
                break;
            case R.id.tv_camera:
                DialogManager.getInstance().hide(mPhotoDialog);
                toCamera();
                break;
            case R.id.tv_ablum:
                DialogManager.getInstance().hide(mPhotoDialog);
                toAlbum();
                break;
            case R.id.tv_cancel:
                DialogManager.getInstance().hide(mPhotoDialog);
                break;
            case R.id.tv_boy:
                DialogManager.getInstance().hide(mSexDialog);
                tv_sex.setText(getString(R.string.str_common_boy));
                break;
            case R.id.tv_girl:
                DialogManager.getInstance().hide(mSexDialog);
                tv_sex.setText(getString(R.string.str_common_girl));
                break;
            case R.id.tv_sex_cancel:
                DialogManager.getInstance().hide(mSexDialog);
                break;
            case R.id.ll_sex:
                DialogManager.getInstance().show(mSexDialog);
                break;
            case R.id.ll_birthday:
                Toast.makeText(this, "ll_birthday", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_city:
                Toast.makeText(this, "ll_city", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 保存
     */
    private void saveUserInfo() {
        String nickName = et_nickname.getText().toString().trim();
        String sex = tv_sex.getText().toString().trim();
        String age = et_age.getText().toString().trim();
        String birthday = tv_birthday.getText().toString().trim();
        String phone = et_phone.getText().toString().trim();
        String desc = et_desc.getText().toString().trim();
        String city = tv_city.getText().toString().trim();

        final IMUser imUser = IMSDK.getCurrentUser();
        if (!TextUtils.isEmpty(nickName)) {
            imUser.setNickname(nickName);
        }
        if (!TextUtils.isEmpty(sex)) {
            imUser.setSex(sex.equals(getString(R.string.str_common_boy)) ? true : false);
        }
        if (!TextUtils.isEmpty(age)) {
            imUser.setAge(Integer.parseInt(age));
        }
        if (!TextUtils.isEmpty(birthday)) {
            imUser.setBirthday(birthday);
        }
        if (!TextUtils.isEmpty(phone)) {
            imUser.setPhone(birthday);
        }
        if (!TextUtils.isEmpty(desc)) {
            imUser.setDesc(desc);
        }
        if (!TextUtils.isEmpty(city)) {
            imUser.setCity(city);
        }
        if (uploadPhotoFile != null) {
            final BmobFile file = new BmobFile(uploadPhotoFile);
            file.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        imUser.setAvatar(file);
                        updateUser(imUser);
                    } else {
                        IMLog.e(e.toString());
                    }
                }
            });
        } else {
            updateUser(imUser);
        }
    }

    private void updateUser(IMUser imUser) {
        LodingView.getInstance().show();
        IMSDK.updateUser(imUser, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                LodingView.getInstance().hide();
                if (e == null) {
                    CommonUtils.Toast(UserEditActivity.this, getString(R.string.str_toast_save_success));
                    finish();
                } else {
                    IMLog.e(e.toString());
                    CommonUtils.Toast(UserEditActivity.this, getString(R.string.str_toast_save_fail));
                }
            }
        });
    }

    /**
     * 相机
     */
    private void toCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String filename = timeStampFormat.format(new Date());
        tempFile = new File(Environment.getExternalStorageDirectory(), filename + ".jpg");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            // 从文件中创建uri
            imageUri = Uri.fromFile(tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        } else {
            //兼容android7.0 使用共享文件的形式
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, tempFile.getAbsolutePath());
            imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        }
        startActivityForResult(intent, TAKEPHOTO);
    }

    /**
     * 相册
     */
    private void toAlbum() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, TAKEALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == TAKEPHOTO) {
                uploadPhotoFile = tempFile;
            } else if (requestCode == TAKEALBUM) {
                if (data != null) {
                    uri = data.getData();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        path = this.uri.getPath();
                        path = PictureUtils.getPath_above19(this, uri);
                    } else {
                        path = PictureUtils.getFilePath_below19(this, this.uri);
                    }
                    if (!TextUtils.isEmpty(path)) {
                        uploadPhotoFile = new File(path);
                    }
                }
            }
            if (uploadPhotoFile != null) {
                GlideUtils.loadFile(this, uploadPhotoFile, iv_photo);
            }
        }
    }
}
