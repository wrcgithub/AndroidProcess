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
    public  static final  int [] linearArray = new int[]{R.id.linear01,R.id.linear02,R.id.linear03,R.id.linear04};
    public  static final  int [] textArray = new int[]{R.id.dialog_one_text,R.id.dialog_two_text,R.id.dialog_three_text,R.id.dialog_four_text};
    public  static final  int [] imgArray = new int[]{0,1,2};
    
    
    public static void showDialogConfirmImg(Context context, final boolean flag, final String msg , final List<ShowInfos> data, final OnClickDialog linstener) {
        if (context == null){
            return;
        }
        dailog = new CustomDialog.Builder(context).outSideCancel(flag).view(R.layout.dialog_image01)
                .widthDimenRes(R.dimen.dp200).heightDinmenRes(R.dimen.dp250).style(R.style.Dialog).addViewonclick(R.id.linear01, new View.OnClickListener() {
                    
                    @Override
                    public void onClick(View v) {
                        linstener.text01("com.tencent.tim");
                        dailog.dismiss();
                    }
                })
        .addViewonclick(R.id.linear02, new View.OnClickListener() {
        
            @Override
            public void onClick(View v) {
                linstener.text02("com.tencent.mm");
                dailog.dismiss();
            }
        })
        
        .addViewonclick(R.id.linear03, new View.OnClickListener() {
        
            @Override
            public void onClick(View v) {
                linstener.text03("com.android.mms");
                dailog.dismiss();
            }
        })
        
                .setText(R.id.dialog_one_text,"com.tencent.tim")
                .setText(R.id.dialog_two_text,"com.tencent.mm")
                .setText(R.id.dialog_three_text,"com.android.mms")
    
        
                
                .build();
        dailog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        
        dailog.show();
        ;
        
    }
}
