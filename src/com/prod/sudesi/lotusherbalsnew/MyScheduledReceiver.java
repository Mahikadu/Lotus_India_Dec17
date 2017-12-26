package com.prod.sudesi.lotusherbalsnew;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Mahesh on 12/25/2017.
 */

public class MyScheduledReceiver extends BroadcastReceiver {

    private static final String TAG = "MyScheduledReceiver";
    //DataHelper db;
    //    private Handler screenOFFHandler;
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent background = new Intent(context, BackgroundServiceBOC.class);
        context.startService(background);

        /*try {

            Intent i = new Intent(context, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);


        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}
