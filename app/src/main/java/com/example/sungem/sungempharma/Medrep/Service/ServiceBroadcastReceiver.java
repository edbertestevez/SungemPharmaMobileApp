package com.example.sungem.sungempharma.Medrep.Service;

import android.content.*;

/**
 * Created by trebd on 9/2/2017.
 */

public class ServiceBroadcastReceiver extends android.content.BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, SungemService.class));
    }
}