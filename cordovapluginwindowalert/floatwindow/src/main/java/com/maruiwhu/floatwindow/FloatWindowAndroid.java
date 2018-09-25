package com.maruiwhu.floatwindow;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * 悬浮窗控制类
 */
public class FloatWindowAndroid {
    /**
     * 悬浮窗内容
     */
    private View mFloatLayout;
    private TextView mTvContent;
    /**
     *
     */
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowParams;

    /**
     *
     */
    private Context mContext;

    public FloatWindowAndroid(Context context) {
        mContext = context;
        initFloatWindow(context);
        initFloatView(context);
    }

    public void showFloatButton(String content) {
        mTvContent.setText(content);
        if (mFloatLayout.getParent() != null) {
            mWindowManager.updateViewLayout(mFloatLayout, mWindowParams);
        } else {
            mWindowManager.addView(mFloatLayout, mWindowParams);
        }
    }

    public void hideFloatButton() {
        if (mFloatLayout.getParent() != null) {
            mWindowManager.removeView(mFloatLayout);
        }
    }

    private void initFloatWindow(Context context) {
        mWindowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        mWindowParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= 26) {
            mWindowParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mWindowParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        //设置效果为背景透明.
        mWindowParams.format = PixelFormat.RGBA_8888;
        //设置flags.不可聚焦及不可使用按钮对悬浮窗进行操控.
        mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        //设置窗口初始停靠位置.
        mWindowParams.gravity = Gravity.LEFT | Gravity.BOTTOM;
        mWindowParams.x = 80;
        mWindowParams.y = 300;

        mWindowParams.packageName = context.getPackageName();
        mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

    }

    private void initFloatView(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        mFloatLayout = layoutInflater.inflate(R.layout.layout_float_button, null, false);
        mTvContent = mFloatLayout.findViewById(R.id.content);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mFloatLayout.setOnClickListener(onClickListener);
    }

}
