package com.liuguilin.im.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.liuguilin.im.R;

/**
 * FileName: LettersView
 * Founder: LiuGuiLin
 * Create Date: 2018/12/19 11:29
 * Email: lgl@szokl.com.cn
 * Profile:字母侧边栏
 */
public class LettersView extends View {

    //TAG
    private static final String TAG = "LettersView";

    //字母数组,#代表未知，比如数字开头
    private String[] strChars = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
    //画笔
    private Paint mPaint;
    //选中字母的下标
    private int checkIndex;
    //字母提示的TextView
    private TextView mTextView;
    //接口回调
    private OnLettersListViewListener onLettersListViewListener;

    public LettersView(Context context) {
        super(context);
        initView();
    }

    public LettersView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LettersView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        //实例化画笔
        mPaint = new Paint();
        //设置style
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        //设置抗锯齿
        mPaint.setAntiAlias(true);
    }

    /**
     * 绘制
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 为了排列26个字母，我们可以用坐标点来计算，X居中，Y为 1/27 的递加计算
         * 首先获取到我们View的宽高
         */
        int viewWidth = getWidth();
        int viewHeight = getHeight();
        //计算一个字母的高度
        int singleHeight = viewHeight / strChars.length;
        //循环绘制字母
        for (int i = 0; i < strChars.length; i++) {
            //设置选中字母的颜色
            if (i == checkIndex) {
                mPaint.setColor(getResources().getColor(R.color.colorPrimary));
                mPaint.setTextSize(60);
            } else {
                mPaint.setColor(Color.BLACK);
                //设置字体大小
                mPaint.setTextSize(40);
            }
            /**
             * 绘制字母
             * x: （view的宽度 - 文本的宽度）/ 2
             * y:  singleHeight * x + singleHeight  //单个字母的高度 + 最上面的字幕空白高度
             */
            float lettersX = (viewWidth - mPaint.measureText(strChars[i])) / 2;
            float lettersY = singleHeight * i + singleHeight;
            //绘制
            canvas.drawText(strChars[i], lettersX, lettersY, mPaint);
            //重绘
            mPaint.reset();
        }
    }

    public void setTextView(TextView mTextView) {
        this.mTextView = mTextView;
    }

    /**
     * 事件分发
     *
     * @param event
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //判断手势
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                setBackgroundColor(getResources().getColor(R.color.color_gary));
                //获取点击的Y坐标，以此来判断选中的字母
                float y = event.getY();
                Log.i(TAG, "y:" + y);
                //第一次被选中的下标
                int oldCheckIndex = checkIndex;
                /**
                 * 计算选中的字母
                 * strChars[当前Y / View的高度 * 字母个数]
                 */
                int c = (int) (y / getHeight() * strChars.length);
                Log.i(TAG, "c:" + c);
                //判断移动
                if (oldCheckIndex != c) {
                    //不能越界
                    if (c >= 0 && c < strChars.length) {
                        //效果联动
                        if (onLettersListViewListener != null) {
                            onLettersListViewListener.onLettersListener(strChars[c]);
                        }
                        if (mTextView != null) {
                            mTextView.setVisibility(View.VISIBLE);
                            mTextView.setText(strChars[c]);
                        }
                    }
                    checkIndex = c;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                //设置透明背景
                setBackgroundResource(android.R.color.transparent);
                //恢复不选中
                checkIndex = -1;
                invalidate();
                //是否显示
                if (mTextView != null) {
                    mTextView.setVisibility(View.INVISIBLE);
                }
                break;
        }
        return true;
    }


    public void setOnLettersListViewListener(OnLettersListViewListener onLettersListViewListener) {
        this.onLettersListViewListener = onLettersListViewListener;
    }

    /**
     * 接口回调/ListView联动
     */
    public interface OnLettersListViewListener {
        void onLettersListener(String s);
    }
}
