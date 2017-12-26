package com.wrc.androidprocess;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wenming.library.BackgroundUtil;
import com.wrc.androidprocess.contant.Features;
import com.wrc.androidprocess.dao.RunningProcessDao;
import com.wrc.androidprocess.service.AndroidProcessService;
import com.wrc.androidprocess.service.FloatViewService;


public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService();
        Features.BGK_METHOD = BackgroundUtil.BKGMETHOD_GETUSAGESTATS;
    
        runDao = new RunningProcessDao(this);
//        saveDB(this,"1234567890");
    }
    private void startService() {
        Features.showForeground = true;
        Intent intent = new Intent(MainActivity.this, AndroidProcessService.class);
        startService(intent);
    
    
        Intent intent2 = new Intent(MainActivity.this, FloatViewService.class);
        //启动FloatViewService
        startService(intent2);
    }
    private RunningProcessDao runDao;
//    private  void saveDB(Context context, String shareInfo){
//
//        List<RunningProcess> list = runDao.queryForAll();
//        if (list != null && list.size()>2){
//            runDao.delete(list.get(0));
//            RunningProcess process = new RunningProcess();
//            process.setProcessName(shareInfo);
//            process.setSaveTime(DateUtil.dateString(System.currentTimeMillis()));
//            process.setRemark("备注");
//            runDao.add(process);
//        }else {
//            RunningProcess process = new RunningProcess();
//            process.setProcessName(shareInfo);
//            process.setSaveTime(DateUtil.dateString(System.currentTimeMillis()));
//            process.setRemark("备注");
//            runDao.add(process);
//        }
//
//        List<RunningProcess> list1 = runDao.queryForAll();
//        Toast.makeText(MainActivity.this,"个数："+list1.size(),Toast.LENGTH_LONG).show();
//        if (list1 != null ) {
//            Log.e("wrc", "***********************************************************************************************数据大小为："+list1.size());
//        }
//
//
//    }
}
