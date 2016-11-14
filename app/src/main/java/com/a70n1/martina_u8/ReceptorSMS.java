package com.a70n1.martina_u8;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by AMARTIN on 14/11/2016.
 */

public class ReceptorSMS extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

       // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);

                    if (senderNum.equals("555")) {
                        context.startService((new Intent(context,
                                ServicioMusica.class)).putExtra("mensaje", message).putExtra("numero", senderNum));
                        context.startActivity((new Intent(context,
                                MainActivity.class)).putExtra("mensaje", message).putExtra("numero", senderNum).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));


                    }

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }



        /*
        // Sacamos información de la intención
        String estado = "", numero = "";
        Bundle extras = intent.getExtras();
        if (extras != null) {
            estado = extras.getString(TelephonyManager.EXTRA_STATE);
            if (estado.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                numero = extras.getString( TelephonyManager.EXTRA_INCOMING_NUMBER);
                String info = estado + " " + numero;
                Log.d("ReceptorAnuncio", info + " intent=" + intent);
// Creamos Notificación
                NotificationCompat.Builder notificacion = new NotificationCompat.Builder(context) .setContentTitle("Esto es una llamada entrante ")
                        .setContentText(info) .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0));
                ((NotificationManager) context.getSystemService(Context. NOTIFICATION_SERVICE)).notify(1,notificacion.build());
            }
        }*/
    }
}
