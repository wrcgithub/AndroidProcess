package com.wrc.androidprocess.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wrc.androidprocess.R;
import com.wrc.androidprocess.adapter.FloatAdapter;
import com.wrc.androidprocess.bean.RunningProcess;
import com.wrc.androidprocess.bean.ShowInfos;
import com.wrc.androidprocess.callback.UpdateCallback;
import com.wrc.androidprocess.dao.RunningProcessDao;
import com.wrc.androidprocess.utils.AllUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wrc on 2017/12/9/009.
 */

public class FloatViewService extends Service {
    
    private static final String TAG = "FloatViewService";
    //定义浮动窗口布局
    private static RelativeLayout mFloatLayout;
    private static WindowManager.LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象
    private static WindowManager mWindowManager;
    
    private static RelativeLayout mFloatView;
    private  static Context mContext;
    private  static List<ShowInfos> mData = null;
    
    private static ListView mListview;
    private  static FloatAdapter adapter;
    private  static RunningProcessDao runDao ;
    
    public UpdateCallback callback = new UpdateCallback() {
        
        @Override
        public void update() {
    
            notifyData();
        }
    };
    
    
    @Override
    public void onCreate() {
        
        super.onCreate();
        mContext = this;
        runDao = new RunningProcessDao(mContext);
        mData = new ArrayList<>();
        createFloatView();
    }
    
    
    @SuppressWarnings("static-access")
    @SuppressLint("InflateParams")
    private void createFloatView() {
        
        wmParams = new WindowManager.LayoutParams();
        //通过getApplication获取的是WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager) getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        //设置window type
//        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;//TYPE_SYSTEM_ERROR
        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;// 系统提示类型,重要//TYPE_TOAST
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
//        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = 0;
        wmParams.y = 152;

//        DisplayMetrics dm = new DisplayMetrics();
//        //获取屏幕信息
//        mWindowManager.getDefaultDisplay().getMetrics(dm);
//        int screenWidth = dm.widthPixels;
//        int screenHeigh = dm.heightPixels;
//        wmParams.x = screenWidth;
//        wmParams.y = 152;
        
        //设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        
        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局
        mFloatLayout = (RelativeLayout) inflater.inflate(R.layout.float_layout, null);
        //添加mFloatLayout
        mWindowManager.addView(mFloatLayout, wmParams);
        //浮动窗口按钮
        mFloatView = (RelativeLayout) mFloatLayout.findViewById(R.id.alert_window_imagebtn);
        
        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        //设置监听浮动窗口的触摸移动
        mFloatLayout.setOnTouchListener(new View.OnTouchListener() {

            boolean isClick;


            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
//                        mFloatView.setBackgroundResource(R.drawable.circle_red);
                        isClick = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        isClick = true;
                        // getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
                        wmParams.x = (int) event.getRawX()
                                - mFloatLayout.getMeasuredWidth() / 2;
                        // 减25为状态栏的高度
                        wmParams.y = (int) event.getRawY()
                                - mFloatLayout.getMeasuredHeight() / 2 - 75;
                        // 刷新
                        mWindowManager.updateViewLayout(mFloatLayout, wmParams);
                        return true;
                    case MotionEvent.ACTION_UP:
//                        mFloatView.setBackgroundResource(R.drawable.circle_cyan);
                        return isClick;// 此处返回false则属于移动事件，返回true则释放事件，可以出发点击否。

                    default:
                        break;
                }
                return false;
            }
        });
        //设置监听浮动窗口的触摸移动
        
        mFloatView.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                
            }
        });
        
        
        mView = mFloatLayout.findViewById(R.id.float_view);
        mLinearLayout = mFloatLayout.findViewById(R.id.linear_float02);
        mListview = mFloatLayout.findViewById(R.id.float_listview);
        mView.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                
                if (mLinearLayout.getVisibility() == View.GONE) {
                    mLinearLayout.setVisibility(View.VISIBLE);
                    notifyData();
                } else if (mLinearLayout.getVisibility() == View.VISIBLE) {
                    mLinearLayout.setVisibility(View.GONE);
                }
            }
        });
        mData = refresh();
        adapter = new FloatAdapter(mContext, mData);
        mListview.setAdapter(adapter);
        setListViewHeight(mListview);
        adapter.notifyDataSetChanged();
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
    
                try {
    
                    AllUtils.runApp(mContext, mData.get(position).getPackageName());
                }catch (Exception e){
                
                }
                
            }
        });
    
    }
    //为listview动态设置高度（有多少条目就显示多少条目）
    public static void setListViewHeight(ListView listView) {
        //获取listView的adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        //listAdapter.getCount()返回数据项的数目
        for (int i = 0,len = listAdapter.getCount(); i < len; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() *  (listAdapter .getCount() - 1));
        listView.setLayoutParams(params);
    }
    
    
    private static View mView;
    private static LinearLayout mLinearLayout;
    
    
    private static List<ShowInfos>  refresh() {
        List<ShowInfos> data = null;
        if (data == null){
            data = new ArrayList<>();
        }else {
            data.clear();
        }
        List<RunningProcess> list = runDao.queryForAll();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++){
                
                ShowInfos infos = AllUtils.getAppInfoByPackageName(mContext, list.get(i).getProcessName());
                if (infos != null) {
                    data.add(infos);
                }
            }
            
        }
        if (data == null) {
            data = new ArrayList<>();
        }
        return  data;
    }
    
    
    /**
     * 刷新ListView
     */
    private static void notifyData(){
    
        if (mData != null){
            mData.clear();
        }
        mData = refresh();
        if (adapter == null){
            adapter = new FloatAdapter(mContext, mData);
        }else {
            adapter.setData(mData);
        }
        setListViewHeight(mListview);
        adapter.notifyDataSetChanged();
    }
    
    private static PackageManager pm;
    private static ApplicationInfo appInfo;
    
    
    @Override
    public void onDestroy() {
        
        super.onDestroy();
//        if(mFloatLayout != null)
//        {
//            //移除悬浮窗口
//            mWindowManager.removeView(mFloatLayout);
//        }
    }
    
    
    @Override
    public IBinder onBind(Intent intent) {
        
        return null;
    }
    
    
    /**
     * 测试方法
     *
     * @param list
     */
    private void test(List<RunningProcess> list) {
        
        StringBuilder sb = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++){
                sb.append(list.get(i).getProcessName());
                if (list.size() == 2 && i == 0) {
                    sb.append("\n");
                }
            }
            
        } else {
            sb.append("鸡飞蛋打~~~ ");
        }
        Toast.makeText(mContext, sb.toString(), Toast.LENGTH_SHORT).show();
        if (list != null && list.size() > 0 && !TextUtils.isEmpty(list.get(0).getProcessName())) {
            AllUtils.runApp(mContext, list.get(0).getProcessName());
        } else {
            Toast.makeText(mContext, sb.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    
}