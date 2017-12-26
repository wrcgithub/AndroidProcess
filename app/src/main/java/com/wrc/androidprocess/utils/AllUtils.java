package com.wrc.androidprocess.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;

import java.util.List;


/**
 * Created by wrc on 2017/12/20/020.
 */

public class AllUtils {
    
    public static boolean isRunningForeground(Context context) {
        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();
        // 枚举进程
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfos) {
            if (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                if (appProcessInfo.processName.equals(context.getApplicationInfo().processName)) {
                    return true;
                }
            }
        }
        return false;
    }
    
public static void runApp(Context context,String packageName){
    ActivityManager mAm = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
    //获得当前运行的task
    List<ActivityManager.RunningTaskInfo> taskList = mAm.getRunningTasks(100);
    for (ActivityManager.RunningTaskInfo rti : taskList) {
        //找到当前应用的task，并启动task的栈顶activity，达到程序切换到前台
        if (rti.topActivity.getPackageName().equals(packageName)) {
            mAm.moveTaskToFront(rti.id, 0);
            return;
        }
    }
    //若没有找到运行的task，用户结束了task或被系统释放，则重新启动mainactivity
    Intent intent = context.getPackageManager().getLaunchIntentForPackage(
            
            //这个是另外一个应用程序的包名
            
            packageName);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    context.startActivity(intent);
    
    
    }
    
    /**
     * 获取正在运行桌面包名（注：存在多个桌面时且未指定默认桌面时，该方法返回Null,使用时需处理这个情况）
     */
    public static String getLauncherPackageName(Context context) {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        final ResolveInfo res = context.getPackageManager().resolveActivity(intent, 0);
        if (res.activityInfo == null) {
            // should not happen. A home is always installed, isn't it?
            return null;
        }
        if (res.activityInfo.packageName.equals("android")) {
            // 有多个桌面程序存在，且未指定默认项时；
            return null;
        } else {
            return res.activityInfo.packageName;
        }
    }
}
