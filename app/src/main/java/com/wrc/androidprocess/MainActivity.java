package com.wrc.androidprocess;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.wrc.androidprocess.color.ColorSelectActivity;
import com.wrc.androidprocess.color.ColorSelectDialog;
import com.wrc.androidprocess.color.ColorSelectDialogFragment;
import com.wrc.androidprocess.contant.Features;
import com.wrc.androidprocess.dao.RunningProcessDao;
import com.wrc.androidprocess.service.AndroidProcessService;
import com.wrc.androidprocess.service.FloatViewService;
import com.wrc.androidprocess.utils.BackgroundUtil;
import com.wrc.androidprocess.utils.SharePreTool;


public class MainActivity extends AppCompatActivity {
    private RunningProcessDao runDao;
    private View view;
    private ColorSelectDialog colorSelectDialog;
    private int lastColor;
    private TextView textTitle;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        fullScreen(this);
        setContentView(R.layout.activity_main);
        runDao = new RunningProcessDao(this);
        Features.BGK_METHOD = BackgroundUtil.BKGMETHOD_GETUSAGESTATS;
        startService();
        initView();
    }
    private void startService() {
        Features.showForeground = true;
        Intent intent = new Intent(MainActivity.this, AndroidProcessService.class);
        startService(intent);
    
    
        Intent intent2 = new Intent(MainActivity.this, FloatViewService.class);
        //启动FloatViewService
        startService(intent2);
    }
    private void initView() {
        view = findViewById(R.id.view);
        textTitle = findViewById(R.id.title_text);
    }
    
    public void Color(View view) {
        switch (view.getId()) {
            case R.id.button:
                if (colorSelectDialog == null) {
                    colorSelectDialog = new ColorSelectDialog(this);
                    colorSelectDialog.setOnColorSelectListener(new ColorSelectDialog.OnColorSelectListener() {
                        @Override
                        public void onSelectFinish(int color) {
                            SharePreTool.setIntSP(MainActivity.this,SharePreTool.TEXT_COLOR,color);
                            lastColor=color;
                            MainActivity.this.view.setBackgroundColor(lastColor);
                        }
                    });
                }
                colorSelectDialog.setLastColor(lastColor);
                colorSelectDialog.show();
                Toast.makeText(MainActivity.this,"----00000-----",Toast.LENGTH_SHORT).show();
                break;
            case R.id.button2:
                Intent intent = new Intent(this, ColorSelectActivity.class);
                intent.putExtra(ColorSelectActivity.LAST_COLOR,lastColor);
                startActivityForResult(intent, 0);
                Toast.makeText(MainActivity.this,"----11111-----",Toast.LENGTH_SHORT).show();
                break;
            case R.id.button3:
                ColorSelectDialogFragment colorSelectDialogFragment=new ColorSelectDialogFragment();
                colorSelectDialogFragment.setOnColorSelectListener(new ColorSelectDialogFragment.OnColorSelectListener() {
                    @Override
                    public void onSelectFinish(int color) {
                        lastColor=color;
                        MainActivity.this.view.setBackgroundColor(lastColor);
                        refresh(lastColor);
                    }
                });
                colorSelectDialogFragment.setLastColor(lastColor);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                colorSelectDialogFragment.show(ft, "colorSelectDialogFragment");
                Toast.makeText(MainActivity.this,"----22222-----",Toast.LENGTH_SHORT).show();
                break;
        }
        
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                lastColor=data.getIntExtra(ColorSelectActivity.RESULT,0x000000);
                view.setBackgroundColor(lastColor);
                SharePreTool.setIntSP(MainActivity.this,SharePreTool.TEXT_COLOR,lastColor);
                refresh(lastColor);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    /**
     * 通过设置全屏，设置状态栏透明
     *
     * @param activity
     */
    private void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以正常设置
//                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }
    private  void refresh(int color){
        textTitle.setBackgroundColor(color);
        textTitle.setText("悬浮窗");
    }
}
