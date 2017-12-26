package com.wrc.androidprocess.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import com.wrc.androidprocess.MainActivity;

import java.util.List;


/**
 * Created by wrc on 2017/12/9/009.
 */

public class RunningProcessUtil {
    
    
    /**
     * 判断APP是否在前台运行
     *
     * @param context
     * @return
     */
    public static boolean isRunningForeground(Context context) {
        
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();
        // 枚举进程
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfos){
            if (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                if (appProcessInfo.processName.equals(context.getApplicationInfo().processName)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    
    /**
     * 程序切换到前台
     *
     * @param context
     */
    public void startApp(Context context) {
        //如果APP是在后台运行
//        if (!isRunningForeground(context)) {
            //获取ActivityManager
            ActivityManager mAm = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
            //获得当前运行的task
            List<ActivityManager.RunningTaskInfo> taskList = mAm.getRunningTasks(100);
            for (ActivityManager.RunningTaskInfo rti : taskList){
                //找到当前应用的task，并启动task的栈顶activity，达到程序切换到前台
                if (rti.topActivity.getPackageName().equals(context.getPackageName())) {
                    mAm.moveTaskToFront(rti.id, 0);
                    return;
                }
            }
            //若没有找到运行的task，用户结束了task或被系统释放，则重新启动mainactivity
            Intent resultIntent = new Intent(context, MainActivity.class);
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(resultIntent);
//        }
    }
}
