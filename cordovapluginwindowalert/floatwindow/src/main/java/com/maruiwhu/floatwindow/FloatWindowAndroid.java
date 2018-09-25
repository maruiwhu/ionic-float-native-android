package com.maruiwhu.floatwindow;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * 悬浮窗控制类
 */
public class FloatWindowAndroid {
    private final int screenWidth;
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

    private OnClickListener mClickListener;


    /**
     *
     */
    private Context mContext;

    public FloatWindowAndroid(Context context) {
        mContext = context;
        initFloatWindow(context);
        initFloatView(context);
        screenWidth = mWindowManager.getDefaultDisplay().getWidth();
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

        mFloatLayout.setOnTouchListener(new FloatingOnTouchListener());
    }

    public interface OnClickListener {
        void onClick(View view);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mClickListener = onClickListener;
    }

    private class FloatingOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;
        int oddOffsetX = 0;
        int oddOffsetY = 0;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    oddOffsetX = mWindowParams.x;
                    oddOffsetY = mWindowParams.y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    Log.d("TAG", "onTouch: " + movedY);
                    x = nowX;
                    y = nowY;
                    mWindowParams.x = mWindowParams.x + movedX;
                    mWindowParams.y = mWindowParams.y - movedY;

                    // 更新悬浮窗控件布局
                    mWindowManager.updateViewLayout(view, mWindowParams);
                    break;
                case MotionEvent.ACTION_UP:
                    int newOffsetX = mWindowParams.x;
                    int newOffsetY = mWindowParams.y;
                    if (Math.abs(newOffsetX - oddOffsetX) <= 20 && Math.abs(newOffsetY - oddOffsetY) <= 20) {
                        if (mClickListener != null) {
                            mClickListener.onClick(view);
                        }
                    } else {
                        // 抬起手指时让floatView紧贴屏幕左右边缘
                        mWindowParams.x = mWindowParams.x <= (screenWidth / 2) ? 80 : screenWidth - view.getWidth() - 80;
                        mWindowManager.updateViewLayout(view, mWindowParams);

                    }
                    break;

                default:
                    break;
            }
            return true;
        }
    }


}
