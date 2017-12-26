package com.prod.sudesi.lotusherbalsnew;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;


/**
 * Created by Mahesh on 12/25/2017.
 */

public class BocBroadcastReceiver extends BroadcastReceiver {

    SharedPreferences sp;
    SharedPreferences.Editor spe;

    @Override
    public void onReceive(Context context, Intent intent) {

        sp = context.getSharedPreferences("Lotus", Context.MODE_PRIVATE);
        spe = sp.edit();

        Log.v("","Boc running");
        boolean boc26 = true;

        /*spe.putBoolean("BOC26", boc26);
        spe.commit();*/

        Intent i   = new Intent();
        i.setClassName("com.prod.sudesi.lotusherbalsnew", "com.prod.sudesi.lotusherbalsnew.DashboardNewActivity");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("Boc26", boc26);
        context.startActivity(i);

        Log.v("Receiver","Working");
       // proposalDashboardListener.onItemRefreshList(true, context);
    }
}
