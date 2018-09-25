package com.maruiwhu.cordovapluginwindowalert;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.maruiwhu.floatwindow.FloatWindowAndroid;
import com.maruiwhu.floatwindow.PasteCopyService;

import ezy.assist.compat.SettingsCompat;

public class MainActivity extends AppCompatActivity {
    private FloatWindowAndroid floatWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        floatWindow = new FloatWindowAndroid(this);
        floatWindow.setOnClickListener(new FloatWindowAndroid.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatWindow.hideFloatButton();
            }
        });
        Intent intentService = new Intent();
        intentService.setClass(this, PasteCopyService.class);
        startService(intentService);

        if (Build.VERSION.SDK_INT >= 23) {
            if (SettingsCompat.canDrawOverlays(this)) {
                //有悬浮窗权限开启服务绑定 绑定权限


            } else {
                //没有悬浮窗权限m,去开启悬浮窗权限
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    startActivityForResult(intent, 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } else {
            //默认有悬浮窗权限  但是 华为, 小米,oppo等手机会有自己的一套Android6.0以下  会有自己的一套悬浮窗权限管理 也需要做适配

        }


        findViewById(R.id.show_float).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatWindow.showFloatButton("title");
            }
        });
        findViewById(R.id.hide_float).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatWindow.hideFloatButton();
            }
        });
    }
}
