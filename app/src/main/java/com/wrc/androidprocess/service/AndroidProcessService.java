package com.wrc.androidprocess.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import com.wrc.androidprocess.bean.RunningProcess;
import com.wrc.androidprocess.contant.Features;
import com.wrc.androidprocess.dao.RunningProcessDao;
import com.wrc.androidprocess.utils.AllUtils;
import com.wrc.androidprocess.utils.BackgroundUtil;
import com.wrc.androidprocess.utils.SharePreTool;

import java.util.List;


public class AndroidProcessService extends Service {
    
    private Context mContext;
    private RunningProcessDao runDao;
    
    
    @Override
    public IBinder onBind(Intent intent) {
        
        return null;
    }
    
    
    @Override
    public void onCreate() {
        
        super.onCreate();
        mContext = this;
        runDao = new RunningProcessDao(mContext);
        startStatus();
    }
    
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    
        if (Features.showForeground) {
            updateStatus();
        } else {
            stopForeground(true);
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
    
    
    private void startStatus() {
        
        String foreground = getAppStatus();
        sharePre(mContext, foreground);

    }
    
    
    private void updateStatus() {
        
        String foreground = getAppStatus();
        sharePre(mContext, foreground);
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
