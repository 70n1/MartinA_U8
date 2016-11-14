package com.a70n1.martina_u8;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;


/**
 * Created by motoni on 04/11/2016.
 */

public class ServicioMusica extends Service {
    MediaPlayer reproductor;
    private static final int ID_NOTIFICACION_CREAR = 1;

    private String message = "";
    private String number = "";


    @Override public void onCreate() {
        Toast.makeText(this,"Servicio creado", Toast.LENGTH_SHORT).show();
        reproductor = MediaPlayer.create(this, R.raw.audio);


    }
    @Override
    public int onStartCommand(Intent intenc, int flags, int idArranque) {
        Toast.makeText(this,"Servicio arrancado "+ idArranque, Toast.LENGTH_SHORT).show();
        NotificationCompat.Builder notific = new NotificationCompat.Builder(this) .setContentTitle("Creando Servicio de Música") .setSmallIcon(R.mipmap.ic_launcher) .setContentText("información adicional")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),android.R.drawable.ic_media_play))
                .setWhen(System.currentTimeMillis() + 1000 * 60 * 60)
                .setContentInfo("más info")
                .setTicker("Texto en barra de estado")
                .setDefaults(Notification.DEFAULT_SOUND)
                .setVibrate(new long[] { 0,100,200,300 });

        //.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.explosion));
        // .setDefaults(Notification.DEFAULT_VIBRATE);
        //.setDefaults(Notification.DEFAULT_LIGHTS)
        //.setLights(Color.RED, 3000, 1000);


        message = intenc.getStringExtra("mensaje");
        number = intenc.getStringExtra("numero");


        PendingIntent intencionPendiente = PendingIntent.getActivity( this, 0, (new Intent(this, PararMusica.class)).putExtra("mensaje", message).putExtra("numero", number), 0);
        notific.setContentIntent(intencionPendiente);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ID_NOTIFICACION_CREAR, notific.build());
        reproductor.start();
        return START_STICKY;
    }
    @Override public void onDestroy() {
        Toast.makeText(this,"Servicio detenido", Toast.LENGTH_SHORT).show();
        reproductor.stop();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(ID_NOTIFICACION_CREAR);
    }
    @Override public IBinder onBind(Intent intencion) {
        return null;
    }
}