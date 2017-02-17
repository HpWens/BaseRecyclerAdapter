package com.example.ecrbtb;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.WindowManager;

import com.example.ecrbtb.receiver.NetworkConnectChangedReceiver;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import org.xutils.BuildConfig;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by boby on 2016/12/9.
 */

public class MyApplication extends Application {

    private static MyApplication app;

    private static final String TAG = "MyApplication";

    private NetworkConnectChangedReceiver mReceiver;

    private static AlertDialog mNetDialog;

    private static AlertDialog mWifiDialog;

    //表示是否连接
    private boolean isConnected;
    //    表示是否是移动网络
    private boolean isMobile;
    //    表示是否是WiFi
    private boolean isWifi;
    //    表示WiFi开关是否打开
    private boolean isEnableWifi;
    //    表示移动网络数据是否打开
    private boolean isEnableMobile;

    //获取storeId
    private int storeId;

    private boolean isLogin;

    @Override
    public void onCreate() {
        super.onCreate();

        app = this;

        initXutils();

        initStetho();

        //initBugly();

        //Logger.init(TAG).logLevel(LogLevel.NONE);
        initNetReceiver();

        initFresco();

        ZXingLibrary.initDisplayOpinion(this);
    }

    private void initFresco() {
        Fresco.initialize(this);
    }

    public static MyApplication getInstance() {
        return app;
    }

    private void initNetReceiver() {

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        mReceiver = new NetworkConnectChangedReceiver();

        registerReceiver(mReceiver, filter);

    }

    // 程序终止的时候执行
    @Override
    public void onTerminate() {
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
        super.onTerminate();
    }

//    private void initBugly() {
//        Context context = getApplicationContext();
//        // 获取当前包名
//        String packageName = context.getPackageName();
//        // 获取当前进程名
//        String processName = getProcessName(android.os.Process.myPid());
//        // 设置是否为上报进程
//        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
//        strategy.setUploadProcess(processName == null || processName.equals(packageName));
//
//        CrashReport.initCrashReport(getApplicationContext(), "c42d5c7f58", true);//建议在测试阶段建议设置成true，发布时设置为false。
//    }

    private void initStetho() {
        Stetho.initializeWithDefaults(this);
    }

    private void initXutils() {
        x.Ext.init(this);

        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    public static void showWifiDlg(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context.getApplicationContext());
        if (mWifiDialog == null) {
            mWifiDialog = builder
                    //.setIcon(R.mipmap.ic_launcher)         //
                    .setTitle("WIFI设置")            //
                    .setMessage("当前无网络").setPositiveButton("设置", new DialogInterface
                            .OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 跳转到系统的网络设置界面
                            Intent intent = null;
                            // 先判断当前系统版本
                            if (android.os.Build.VERSION.SDK_INT > 10) {  // 3.0以上
                                intent = new Intent(android.provider.Settings
                                        .ACTION_WIFI_SETTINGS);
                            } else {
                                intent = new Intent();
                                intent.setClassName("com.android.settings",
                                        Settings.ACTION_WIFI_SETTINGS);
                            }
                            if ((context instanceof Application)) {
                                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                            }
                            context.startActivity(intent);

                        }
                    }).setNegativeButton("知道了", null).create();
            // 设置为系统的Dialog，这样使用Application的时候不会 报错
            mWifiDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        mWifiDialog.show();
    }

    /**
     * 当判断当前手机没有网络时选择是否打开网络设置
     *
     * @param context
     */
    public static void showNoNetWorkDlg(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context.getApplicationContext());
        if (mNetDialog == null) {
            mNetDialog = builder.setIcon(R.mipmap.ic_launcher)         //
                    .setTitle("网络设置")            //
                    .setMessage("当前无网络").setPositiveButton("设置", new DialogInterface
                            .OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 跳转到系统的网络设置界面
                            Intent intent = null;
                            // 先判断当前系统版本
                            if (android.os.Build.VERSION.SDK_INT > 10) {  // 3.0以上
                                intent = new Intent(android.provider.Settings
                                        .ACTION_WIRELESS_SETTINGS);
                            } else {
                                intent = new Intent();
                                intent.setClassName("com.android.settings",
                                        android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                            }
                            if ((context instanceof Application)) {
                                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                            }
                            context.startActivity(intent);

                        }
                    }).setNegativeButton("知道了", null).create();
            // 设置为系统的Dialog，这样使用Application的时候不会 报错
            mNetDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        mNetDialog.show();

    }

    public boolean isConnected() {
        return isWifi || isMobile;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public boolean isMobile() {
        return isMobile;
    }

    public void setMobile(boolean mobile) {
        isMobile = mobile;
    }

    public boolean isWifi() {
        return isWifi;
    }

    public void setWifi(boolean wifi) {
        isWifi = wifi;
    }

    public boolean isEnableWifi() {
        return isEnableWifi;
    }

    public void setEnableWifi(boolean enableWifi) {
        isEnableWifi = enableWifi;
    }

    public boolean isEnableMobile() {
        return isEnableMobile;
    }

    public void setEnableMobile(boolean enableMobile) {
        isEnableMobile = enableMobile;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }


    public boolean isLogin() {
        return isLogin;
    }

    public void setIsLogin(boolean login) {
        isLogin = login;
    }
}
