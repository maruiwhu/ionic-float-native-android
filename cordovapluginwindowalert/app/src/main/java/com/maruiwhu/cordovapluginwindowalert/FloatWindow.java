package com.maruiwhu.cordovapluginwindowalert;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;

import com.maruiwhu.floatwindow.FloatWindowAndroid;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import ezy.assist.compat.SettingsCompat;

/**
 * This class echoes a string called from JavaScript.
 */
public class FloatWindow extends CordovaPlugin {
    private FloatWindowAndroid floatWindowAndroid;
    private ClipboardManager clipboardManager;
    private ClipBoardListener clipBoardListener;
    private FloatClickListener floatClickListener;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        floatWindowAndroid = new FloatWindowAndroid(cordova.getActivity());
        clipboardManager = (ClipboardManager) cordova.getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
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
        } else if (action.equals("checkAppInstalled")) {
            String packageName = args.getString(0);
            this.checkAppAvailability(packageName, callbackContext);
            return true;
        } else if (action.equals("startApp")) {
            String packageName = args.getString(0);
            this.startApp(packageName, callbackContext);
            return true;
        } else if (action.equals("checkOverlaysPermission")) {
            this.checkOverlaysPermission(callbackContext);
            return true;
        } else if (action.equals("requestOverlaysPermission")) {
            this.requestOverlaysPermission(callbackContext);
            return true;
        }
        return false;
    }


    private void checkOverlaysPermission(final CallbackContext callbackContext) {
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (SettingsCompat.canDrawOverlays(cordova.getActivity())) {
                    //有悬浮窗权限开启服务绑定 绑定权限
                    callbackContext.success(1);
                } else {
                    callbackContext.error(0);
                }
            }
        });
    }

    private void requestOverlaysPermission(final CallbackContext callbackContext) {
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    SettingsCompat.manageDrawOverlays(cordova.getActivity());
                    callbackContext.success(1);
                } catch (Exception e) {
                    e.printStackTrace();
                    callbackContext.error(0);
                }
            }
        });
    }

    private void showFloat(final String content, final CallbackContext callbackContext) {
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (content != null && content.length() > 0) {
                    if (floatClickListener == null) {
                        floatClickListener = new FloatClickListener(callbackContext);
                        floatWindowAndroid.setOnClickListener(floatClickListener);
                    }
                    if (SettingsCompat.canDrawOverlays(cordova.getActivity())) {
                        //有悬浮窗权限开启服务绑定 绑定权限
                        floatWindowAndroid.showFloatButton(content);
                    } else {
                        try {
                            SettingsCompat.manageDrawOverlays(cordova.getActivity());
                        } catch (Exception e) {
                            e.printStackTrace();
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
                floatClickListener = null;
                callbackContext.success();
            }
        });
    }

    private void registerClipBoardListener(final CallbackContext callbackContext) {
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (clipBoardListener == null) {
                    clipBoardListener = new ClipBoardListener(callbackContext);
                }
                clipboardManager.addPrimaryClipChangedListener(clipBoardListener);
            }
        });

    }

    private void unRegisterClipBoardListener(final CallbackContext callbackContext) {
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                clipboardManager.removePrimaryClipChangedListener(clipBoardListener);
                clipBoardListener = null;
                callbackContext.success();
            }
        });

    }

    private void checkAppAvailability(final String packageName, final CallbackContext callbackContext) {
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                PackageInfo packageInfo = null;
                try {
                    PackageManager packageManager = cordova.getActivity().getPackageManager();
                    packageManager.getPackageInfo(packageName, 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                if (packageInfo == null) {
                    callbackContext.success(1);
                } else {
                    callbackContext.error(0);
                }
            }
        });

    }

    private void startApp(final String packageName, final CallbackContext callbackContext) {
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Intent intent = cordova.getActivity().getPackageManager().getLaunchIntentForPackage(packageName);
                    cordova.getActivity().startActivity(intent);
                    callbackContext.success(1);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    callbackContext.error(0);
                }
            }
        });

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

    private class FloatClickListener implements FloatWindowAndroid.OnClickListener {
        private CallbackContext callbackContext;

        public FloatClickListener(CallbackContext callbackContext) {
            this.callbackContext = callbackContext;
        }

        @Override
        public void onClick(View v) {
            floatWindowAndroid.hideFloatButton();
            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, "click");
            pluginResult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginResult);
        }
    }

}
