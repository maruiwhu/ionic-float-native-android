package com.maruiwhu.cordovapluginwindowalert;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import com.maruiwhu.floatwindow.FloatWindowAndroid;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * This class echoes a string called from JavaScript.
 */
public class FloatWindow extends CordovaPlugin {
    private FloatWindowAndroid floatWindowAndroid;
    private ClipboardManager clipboardManager;
    private ClipBoardListener clipBoardListener;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        floatWindowAndroid = new FloatWindowAndroid(cordova.getContext());
        clipboardManager = (ClipboardManager) cordova.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("showFloat")) {
            String content = args.getString(0);
            this.showFloat(content, callbackContext);
            return true;
        } else if (action.equals("hideFloat")) {
            this.hideFloat(callbackContext);
            return true;
        } else if (action.equals("registerClipBoardListener")) {
            this.registerClipBoardListener(callbackContext);
            return true;
        } else if (action.equals("unRegisterClipBoardListener")) {
            this.unRegisterClipBoardListener(callbackContext);
            return true;
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

    private void registerClipBoardListener(final CallbackContext callbackContext) {
        if (clipBoardListener == null) {
            clipBoardListener = new ClipBoardListener(callbackContext);
        }
        clipboardManager.addPrimaryClipChangedListener(clipBoardListener);
        callbackContext.success();
    }

    private void unRegisterClipBoardListener(final CallbackContext callbackContext) {
        clipboardManager.removePrimaryClipChangedListener(clipBoardListener);
        clipBoardListener = null;
        callbackContext.success();
    }

    private class ClipBoardListener implements ClipboardManager.OnPrimaryClipChangedListener {
        private CallbackContext callbackContext;

        public ClipBoardListener(CallbackContext callbackContext) {
            this.callbackContext = callbackContext;
        }

        @Override
        public void onPrimaryClipChanged() {
            try {
                if (clipboardManager.hasPrimaryClip()) {
                    ClipData clipData = clipboardManager.getPrimaryClip();
                    String content = clipData.getItemAt(0).getText().toString();
                    PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, content);
                    pluginResult.setKeepCallback(true);
                    callbackContext.sendPluginResult(pluginResult);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
