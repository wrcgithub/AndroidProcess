package com.wrc.androidprocess.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by wrc on 2017/11/30/030.
 */

public class CustomDialog extends Dialog {
    private  Context context;
    private int width, height;
    private boolean outSideCancel;
    private View view;
    
    
    public CustomDialog(Builder builder) {
        
        super(builder.context);
        context = builder.context;
        width = builder.width;
        height = builder.height;
        outSideCancel = builder.outSideCancel;
        view = builder.view;
    
    
    }
    
    public CustomDialog(Builder builder,int reStyle){
    super(builder.context,reStyle);
        context = builder.context;
        width = builder.width;
        height = builder.height;
        outSideCancel = builder.outSideCancel;
        view = builder.view;
    
    }
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(view);
        setCanceledOnTouchOutside(outSideCancel);
        Window window = getWindow();
        WindowManager.LayoutParams wml= window.getAttributes();
        wml.gravity = Gravity.CENTER;
        wml.height = height;
        wml.width = width;
        window.setAttributes(wml);
        
        
    }
    
    
    
    public static final class Builder {
        
        private Context context;
        private int width, height;
        private boolean outSideCancel;
        private View view;
        private int reStyle = -1;
        
        
        public Builder(Context context) {
            
            this.context = context;
            
        }
        
        
        public Builder view(int resView) {
            
            view = LayoutInflater.from(context).inflate(resView, null);
            return this;
        }
        
        
        public Builder widthpx(int val) {
            
            width = val;
            return this;
        }
        
        
        public Builder heightpx(int val) {
            
            height = val;
            return this;
            
        }
        
        
        public Builder withdp(int val) {
            
            width = DensityUtil.dip2px(context, val);
            return this;
            
        }
        
        
        public Builder heightdp(int val) {
            
            height = DensityUtil.dip2px(context, val);
            return this;
        }
        
        
        public Builder widthDimenRes(int dimenRes) {
            
            width = context.getResources().getDimensionPixelOffset(dimenRes);
            return this;
        }
        
        
        public Builder heightDinmenRes(int dinmenRes) {
            
            height = context.getResources().getDimensionPixelOffset(dinmenRes);
            return this;
        }
        
        
        public Builder style(int style) {
            
            this.reStyle = style;
            return this;
        }
        
        
        public Builder outSideCancel(boolean val) {
            
            this.outSideCancel = val;
            return this;
        }
        
        
        public Builder addViewonclick(int viewRes, View.OnClickListener listener) {
            
            LinearLayout layout = (LinearLayout)view.findViewById(viewRes);
            layout .setOnClickListener(listener);
            layout.setVisibility(View.VISIBLE);
            return this;
            
        }
        public Builder setText(int viewRes,String msg){
          TextView textView =  (TextView)view.findViewById(viewRes);
          textView.setText(msg);
            return  this;
        }
    
        public Builder setImage(int viewRes,int drawable){
            ImageView imageView =  (ImageView)view.findViewById(viewRes);
            imageView.setImageResource(drawable);
            return  this;
        }
        public Builder setDrawable(int viewRes,Drawable drawable){
            ImageView imageView =  (ImageView)view.findViewById(viewRes);
            imageView.setImageDrawable(drawable);
            return  this;
        }
        public CustomDialog build() {
            
            if (reStyle != -1) {
                return new CustomDialog(this, reStyle);
            } else {
                return new CustomDialog(this);
            }
        }
    }
    
}