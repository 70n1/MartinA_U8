package com.a70n1.martina_u8;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

/**
 * Created by AMARTIN on 14/11/2016.
 */

public class ServicioCirculoPolar extends Service implements LocationListener {

    LocationManager locationManager;
    long TIEMPO_MIN = 10 * 1000 ; // 10 segundos
    long DISTANCIA_MIN = 5; // 5 metros

    @Override
    public void onCreate() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }


    @Override
    public int onStartCommand(Intent intenc, int flags, int idArranque) {

        Criteria criterio = new Criteria();
        criterio.setCostAllowed(false);
        criterio.setAltitudeRequired(false);
        criterio.setAccuracy(Criteria.ACCURACY_FINE);
        String proveedor = locationManager.getBestProvider(criterio, true);
        locationManager.requestLocationUpdates(proveedor, TIEMPO_MIN, DISTANCIA_MIN, this);
        return START_STICKY;

    }


    public void onLocationChanged(Location location) {
       if (location.getLatitude()>66.55) { //estamos en el polo norte

           NotificationCompat.Builder notific = new NotificationCompat.Builder(this) .setContentTitle("Se ha entrado en el Círculo Polar") .setSmallIcon(R.mipmap.ic_launcher)
                   .setWhen(System.currentTimeMillis() + 1000 * 60 * 60)
                   .setContentInfo("más info")
                   .setDefaults(Notification.DEFAULT_SOUND)
                   .setVibrate(new long[] { 0,100,200,300 });


           PendingIntent intencionPendiente = PendingIntent.getActivity( this, 0, (new Intent(this, MainActivity.class)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK), 0);
           notific.setContentIntent(intencionPendiente);

           NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
           notificationManager.notify(1, notific.build());
       }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Criteria criterio = new Criteria();
        criterio.setCostAllowed(false);
        criterio.setAltitudeRequired(false);
        criterio.setAccuracy(Criteria.ACCURACY_FINE);
        String proveedor = locationManager.getBestProvider(criterio, true);
        locationManager.requestLocationUpdates(proveedor, TIEMPO_MIN, DISTANCIA_MIN, this);

    }

    @Override
    public void onProviderEnabled(String provider) {
        Criteria criterio = new Criteria();
        criterio.setCostAllowed(false);
        criterio.setAltitudeRequired(false);
        criterio.setAccuracy(Criteria.ACCURACY_FINE);
        String proveedor = locationManager.getBestProvider(criterio, true);
        locationManager.requestLocationUpdates(proveedor, TIEMPO_MIN, DISTANCIA_MIN, this);

    }

    @Override
    public void onProviderDisabled(String provider) {
        Criteria criterio = new Criteria();
        criterio.setCostAllowed(false);
        criterio.setAltitudeRequired(false);
        criterio.setAccuracy(Criteria.ACCURACY_FINE);
        String proveedor = locationManager.getBestProvider(criterio, true);
        locationManager.requestLocationUpdates(proveedor, TIEMPO_MIN, DISTANCIA_MIN, this);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
