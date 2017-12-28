package com.wrc.androidprocess.utils;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;

import com.wrc.androidprocess.R;
import com.wrc.androidprocess.bean.ShowInfos;

import java.util.List;


/**
 * Created by wrc on 2017/11/30/030.
 */

public class CustomDialogUtil {
    
    public static CustomDialog dailog;
    public static CustomDialog.Builder builder ;
    public  static final  int [] linearArray = new int[]{R.id.linear01,R.id.linear02,R.id.linear03,R.id.linear04};
    public  static final  int [] textArray = new int[]{R.id.dialog_one_text,R.id.dialog_two_text,R.id.dialog_three_text,R.id.dialog_four_text};
    public  static final  int [] imgArray = new int[]{R.id.image01,R.id.image02,R.id.image03,R.id.image04};
    
    
    public static void showDialogChoice(Context context, final boolean flag, final String msg , final List<ShowInfos> data, final OnClickDialog linstener) {
        if (dailog != null && dailog.isShowing()){
            dailog.dismiss();
            return ;
        }
        if (context == null){
            return;
        }
        dailog = new CustomDialog.Builder(context).outSideCancel(flag).view(R.layout.dialog_layout)
                .widthDimenRes(R.dimen.dp200).heightDinmenRes(R.dimen.dp250).style(R.style.Dialog).addViewonclick(R.id.linear01, new View.OnClickListener() {
                    
                    @Override
                    public void onClick(View v) {
                        linstener.view01("com.tencent.tim");
                        dailog.dismiss();
                    }
                })
        .addViewonclick(R.id.linear02, new View.OnClickListener() {
        
            @Override
            public void onClick(View v) {
                linstener.view02("com.tencent.mm");
                dailog.dismiss();
            }
        })
        
        .addViewonclick(R.id.linear03, new View.OnClickListener() {
        
            @Override
            public void onClick(View v) {
                linstener.view03("com.android.mms");
                dailog.dismiss();
            }
        })
        
                .setText(R.id.dialog_one_text,"com.tencent.tim")
                .setText(R.id.dialog_two_text,"com.tencent.mm")
                .setText(R.id.dialog_three_text,"com.android.mms")
    
        
                
                .build();
        dailog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        
        dailog.show();
        
    }
    public static void showApp(Context mContext,  final List<ShowInfos> mData, final OnClickDialog linstener) {
        if (dailog != null && dailog.isShowing()){
            dailog.dismiss();
            return ;
        }
        if (mContext == null){
            return;
        }
        builder = new CustomDialog.Builder(mContext);
        builder.outSideCancel(true).view(R.layout.dialog_layout)
                .widthDimenRes(R.dimen.dp140).heightDinmenRes(getHeight(mData.size())).style(R.style.Dialog);
        
        for (int i = 0; i < mData.size(); i++){
            final int index = i;
            builder.addViewonclick(linearArray[index], new View.OnClickListener() {
    
                @Override
                public void onClick(View v) {
                    if(index == 0){
                        linstener.view01(mData.get(index).getPackageName());
                    }else if (index == 1){
                        linstener.view02(mData.get(index).getPackageName());
                    }else if (index == 2){
                        linstener.view03(mData.get(index).getPackageName());
                    }else if (index == 3){
                        linstener.view04(mData.get(index).getPackageName());
                    }
                    dailog.dismiss();
                }
            })
                    .setDrawable(imgArray[index],mData.get(index).getDrawable())
                    .setText(textArray[index],mData.get(index).getAppName());
        }
        dailog = builder.build();
        dailog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);//系统级别的Dialog
        
        dailog.show();
    }
    public static boolean dismiss() {
        if (dailog != null && dailog.isShowing()){
            dailog.dismiss();
            return true;
        }
        return  false;
    }
    public static int getHeight(int size) {
        int ret = 0;
        if (size == 1){
            ret = R.dimen.dp30;
        }else if (size == 2){
            ret = R.dimen.dp60;
        }else if (size == 3){
            ret = R.dimen.dp90;
        }else if (size == 4){
            ret = R.dimen.dp120;
        }
        return  ret;
    }
}
