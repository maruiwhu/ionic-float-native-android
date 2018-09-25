package com.maruiwhu.floatwindow;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

/**
 * 监听剪切板
 */
public class PasteCopyService extends Service {
    private ClipboardManager clipboardManager;

    public PasteCopyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                try {
                    if (clipboardManager.hasPrimaryClip()) {
                        ClipData clipData = clipboardManager.getPrimaryClip();
                        String content = clipData.getItemAt(0).getText().toString();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clipboardManager = null;
    }
}
