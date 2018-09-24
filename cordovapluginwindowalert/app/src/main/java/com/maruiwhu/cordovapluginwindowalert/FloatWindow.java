package com.maruiwhu.cordovapluginwindowalert;

import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import com.maruiwhu.floatwindow.FloatWindowAndroid;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * This class echoes a string called from JavaScript.
 */
public class FloatWindow extends CordovaPlugin {
    private FloatWindowAndroid floatWindowAndroid;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        floatWindowAndroid = new FloatWindowAndroid(cordova.getContext());
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("showFloat")) {
            String content = args.getString(0);
            this.showFloat(content, callbackContext);
            return true;
        } else if (action.equals("hideFloat")) {
            this.hideFloat(callbackContext);
        }
        return false;
    }

    private void showFloat(final String content, final CallbackContext callbackContext) {
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (content != null && content.length() > 0) {
                    callbackContext.success(content);
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (Settings.canDrawOverlays(cordova.getContext())) {
                            //有悬浮窗权限开启服务绑定 绑定权限
                            floatWindowAndroid.showFloatButton(content);
                        } else {
                            try {
                                Intent intent = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION");
                                cordova.getActivity().startActivityForResult(intent, 1000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                    callbackContext.error("Expected one non-empty string argument.");
                }
            }
        });

    }

    private void hideFloat(final CallbackContext callbackContext) {
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                floatWindowAndroid.hideFloatButton();
                callbackContext.success();
            }
        });
    }

}
