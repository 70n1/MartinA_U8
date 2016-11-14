package com.a70n1.martina_u8;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by AMARTIN on 14/11/2016.
 */

public class LanzarServicioCirculoPolar extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService((new Intent(context,
                ServicioCirculoPolar.class)));
    }
}
