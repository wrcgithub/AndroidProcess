package com.wrc.androidprocess.bean;

import android.graphics.Bitmap;

import java.io.Serializable;


/**
 * Created by wrc on 2017/12/27/027.
 */

public class ShowInfos implements Serializable{
    
    private  String appName ;//应用名称
    private  String packageName;//包名
    private Bitmap bitmap;//应用图片
    
    
    public String getAppName() {
        
        return appName;
    }
    
    
    public void setAppName(String appName) {
        
        this.appName = appName;
    }
    
    
    public String getPackageName() {
        
        return packageName;
    }
    
    
    public void setPackageName(String packageName) {
        
        this.packageName = packageName;
    }
    
    
    public Bitmap getBitmap() {
        
        return bitmap;
    }
    
    
    public void setBitmap(Bitmap bitmap) {
        
        this.bitmap = bitmap;
    }
}
