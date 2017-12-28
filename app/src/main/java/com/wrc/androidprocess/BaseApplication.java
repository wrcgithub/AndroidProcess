package com.wrc.androidprocess;

import android.annotation.SuppressLint;
import android.app.Application;
import android.support.v7.app.AppCompatActivity;

import java.util.LinkedList;
import java.util.List;


public class BaseApplication extends Application {
    
    private static BaseApplication instance;
    private List<AppCompatActivity> mList = new LinkedList<AppCompatActivity>();
    private boolean isInited = false;
    
    
    @SuppressLint("NewApi")
    public void onCreate() {
        
        super.onCreate();
        CrashHandler.getInstance().init(this);
        Thread.setDefaultUncaughtExceptionHandler(CrashHandler.getInstance());
        initApplication();
        
        
    }
    
    
    @Override
    public void onTerminate() {
        
        super.onTerminate();
    }
    
    
    public synchronized static BaseApplication getInstance() {
        
        if (null == instance) {
            instance = new BaseApplication();
        }
        return instance;
    }
    
    
    public void addActivity(AppCompatActivity activity) {
        
        if (mList.size() > 8) {
            mList.get(0).finish();
            mList.remove(0);
        }
        mList.add(activity);
    }
    
    
    public void removeActiviy(AppCompatActivity activity) {
        
        mList.remove(activity);
    }
    
    
    public void exit() {
        
        try {
            for (AppCompatActivity activity : mList){
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        
        }
    }
    
    
    public void onLowMemory() {
        
        super.onLowMemory();
        System.gc();
    }
    
    
    private void initApplication() {
        
        if (!isInited) {
            
            
            
            isInited = true;
        }
        
        
    }
    public AppCompatActivity getTopActivity() {
        
       int size = mList.size();
        if (mList.isEmpty()) {
            return null;
        }
        int index = mList.size() - 1;
        return mList.get(index);
    
    }
    
    public void exitApplication(int exitCode) {
        
        exit();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(exitCode);
        
    }
    
    private int appCount = 0;
    
    
    public int getAppCount() {
        return appCount;
    }
    
    public void setAppCount(int appCount) {
        this.appCount = appCount;
    }
}