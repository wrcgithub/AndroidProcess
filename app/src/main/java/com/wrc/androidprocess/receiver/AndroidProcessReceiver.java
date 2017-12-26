package com.wrc.androidprocess.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.wrc.androidprocess.contant.Features;
import com.wrc.androidprocess.service.AndroidProcessService;


/**
 * Created by wenmingvs on 2016/1/13.
 */
public class AndroidProcessReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Features.showForeground) {
            Intent i = new Intent(context, AndroidProcessService.class);
            context.startService(i);
        }

    }
}
