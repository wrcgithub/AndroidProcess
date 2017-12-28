package com.wrc.androidprocess.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.wrc.androidprocess.MainActivity;
import com.wrc.androidprocess.R;
import com.wrc.androidprocess.bean.RunningProcess;
import com.wrc.androidprocess.contant.Features;
import com.wrc.androidprocess.dao.RunningProcessDao;
import com.wrc.androidprocess.receiver.AndroidProcessReceiver;
import com.wrc.androidprocess.utils.AllUtils;
import com.wrc.androidprocess.utils.BackgroundUtil;
import com.wrc.androidprocess.utils.SharePreTool;

import java.util.ArrayList;
import java.util.List;


public class AndroidProcessService extends Service {
    
    private static final float UPDATA_INTERVAL = 0.5f;//in seconds
    private String status;
    private Context mContext;
    private ArrayList<String> mContentList;
    private Notification notification;
    private AlarmManager manager;
    private PendingIntent pendingIntent;
    private NotificationCompat.Builder mBuilder;
    private Intent mIntent;
    private NotificationManager mNotificationManager;
    private static final int NOTICATION_ID = 0x1;
    private RunningProcessDao runDao;
    
    
    @Override
    public IBinder onBind(Intent intent) {
        
        return null;
    }
    
    
    @Override
    public void onCreate() {
        
        super.onCreate();
        mContext = this;
        runDao = new RunningProcessDao(mContext);mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        initContentData();
        startStatus();
    }
    
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    
        if (Features.showForeground) {
            manager = (AlarmManager) getSystemService(ALARM_SERVICE);
            int updateTime = (int) UPDATA_INTERVAL * 1000;
            long triggerAtTime = SystemClock.elapsedRealtime() + updateTime;
            Intent i = new Intent(mContext, AndroidProcessReceiver.class);
            PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, i, 0);
            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
            updateStatus();
        
        } else {
            stopForeground(true);
            mNotificationManager.cancelAll();
            stopSelf();
        }
        return Service.START_NOT_STICKY;
    }
    
    
    @Override
    public void onDestroy() {
        Features.showForeground = false;
        stopForeground(true);
        super.onDestroy();
    }
    
    private void initContentData() {
        mContentList = new ArrayList<String>();
        mContentList.add("通过getRunningTask判断");
        mContentList.add("通过getRunningAppProcess判断");
        mContentList.add("通过ActivityLifecycleCallbacks判断");
        mContentList.add("通过UsageStatsManager判断");
        mContentList.add("通过AccessibilityService判断");
        mContentList.add("通过LinuxCoreInfo判断");
    }
    private void startStatus() {
        
        String foreground = getAppStatus();
        sharePre(mContext, foreground);
        mIntent = new Intent(mContext, MainActivity.class);
        pendingIntent = PendingIntent.getActivity(mContext, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.largeicon)
                .setContentText(mContentList.get(Features.BGK_METHOD))
                .setContentTitle("App处于" + status)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        notification = mBuilder.build();
        startForeground(NOTICATION_ID, notification);
    }
    
    
    private void updateStatus() {
        String foreground = getAppStatus();
        sharePre(mContext, foreground);
        mBuilder.setContentTitle("App处于" + status);
        mBuilder.setContentText(mContentList.get(Features.BGK_METHOD));
        notification = mBuilder.build();
        mNotificationManager.notify(NOTICATION_ID, notification);
    }
    
    
    
    
    private String getAppStatus() {
        
        return BackgroundUtil.isForeground(mContext, Features.BGK_METHOD, mContext.getPackageName());
    }
    
    
    private void sharePre(Context context, String foreground) {
        if (!TextUtils.isEmpty(foreground) && !foreground.equals("null") && !foreground.equals("com.wrc.androidprocess")) {
            String saved = SharePreTool.getStringSP(context, SharePreTool.PACKAGE_NAME, "");
            String launcher = AllUtils.getLauncherPackageName(mContext);
                if (!TextUtils.isEmpty(launcher) && foreground.equals(launcher)) {
                } else {
                    if (!saved.equals(foreground)) {
                       
                        saveDB(context, foreground);
                    }
                }
        }
        
    }
    
    
    private void saveDB(Context context, String packageName) {
    
        
        List<RunningProcess> list = runDao.queryForAll();
        if (list != null && list.size() > 3) {
    
            List<RunningProcess> quer = runDao.queryForPackage(packageName);
            if (quer == null || quer.size() <1){
                SharePreTool.setStringSP(context, SharePreTool.PACKAGE_NAME, packageName);
                runDao.delete(list.get(0));
                RunningProcess process = new RunningProcess();
                process.setProcessName(packageName);
                runDao.add(process);
            }
            
        } else {
            List<RunningProcess> quer = runDao.queryForPackage(packageName);
            if (quer == null || quer.size() <1){
            SharePreTool.setStringSP(context, SharePreTool.PACKAGE_NAME, packageName);
            RunningProcess process = new RunningProcess();
            process.setProcessName(packageName);
            runDao.add(process);
            }
        }
        
        
    }
}
