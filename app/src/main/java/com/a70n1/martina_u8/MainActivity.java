package com.a70n1.martina_u8;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private View vista;
    private static final int SOLICITUD_PERMISO_ACCESS_FINE_LOCATION = 0;
    private static final int SOLICITUD_PERMISO_ACCESS_FINE_LOCATION1 = 1;
    private GoogleMap mapa;
    LocationManager locationManager;
    private static final long TIEMPO_MIN = 10 * 1000 ; // 10 segundos
    private static final long DISTANCIA_MIN = 5; // 5 metros

    private boolean permiso_concedido=false;

    private boolean permiso_negado = false;

    private String message = "";
    private String number = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent intent = getIntent();
        message = intent.getStringExtra("mensaje");
        number = intent.getStringExtra("numero");

        // Obtenemos el mapa de forma asíncrona (notificará cuando esté listo)
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);
        vista = findViewById(R.id.content_main);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        //LatLng UPV = new LatLng(39.481106, -0.340987); //Nos ubicamos en la UPV
        //mapa.addMarker(new MarkerOptions().position(UPV).title("Marker UPV"));
        // mapa.moveCamera(CameraUpdateFactory.newLatLng(UPV));

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            ponerLocalizacionMapa();
        } else {
            solicitarPermisoLocalizacion();
        }
    }

    public void solicitarPermisoLocalizacion() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            Snackbar.make(vista, "Sin el permiso de localizacion"
                    + " no puedo indicar la localizacion del dispositivo.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    SOLICITUD_PERMISO_ACCESS_FINE_LOCATION);
                        }
                    })
                    .show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    SOLICITUD_PERMISO_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (requestCode == SOLICITUD_PERMISO_ACCESS_FINE_LOCATION) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                ponerLocalizacionMapa();

            } else {
                Snackbar.make(vista, "Sin el permiso, no puedo realizar la" +
                        "acción", Snackbar.LENGTH_SHORT).show();
                permiso_negado = true;
            }
        }
        if (requestCode == SOLICITUD_PERMISO_ACCESS_FINE_LOCATION1) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                ponerRequestLocationUpdates();

            } else {
                Snackbar.make(vista, "Sin el permiso, no puedo realizar la" +
                        "acción", Snackbar.LENGTH_SHORT).show();
                permiso_negado = true;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!(permiso_negado)) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                ponerRequestLocationUpdates();
            } else {
                solicitarPermisoRequestLocationUpdates();
            }
        }

    }

    public void solicitarPermisoRequestLocationUpdates() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            Snackbar.make(vista, "Sin el permiso de localizacion"
                    + " no puedo indicar la localizacion del dispositivo.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    SOLICITUD_PERMISO_ACCESS_FINE_LOCATION1);
                        }
                    })
                    .show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    SOLICITUD_PERMISO_ACCESS_FINE_LOCATION1);
        }
    }

    private void ponerRequestLocationUpdates(){
        permiso_concedido = true;
        Criteria criterio = new Criteria();
        criterio.setCostAllowed(false);
        criterio.setAltitudeRequired(false);
        criterio.setAccuracy(Criteria.ACCURACY_FINE);
        String proveedor = locationManager.getBestProvider(criterio, true);
        locationManager.requestLocationUpdates(proveedor, TIEMPO_MIN, DISTANCIA_MIN, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (permiso_concedido) locationManager.removeUpdates(this);
    }
    public void onLocationChanged(Location location) {
        mostrarLocalizacionMapa(location);
    }

    private void ponerLocalizacionMapa() {
        permiso_concedido = true;
        mapa.setMyLocationEnabled(true);
        mapa.getUiSettings().setZoomControlsEnabled(false);
        mapa.getUiSettings().setCompassEnabled(true);
//        /*if (mapa.getMyLocation() != null)
//            mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mapa.getMyLocation().getLatitude(), mapa.getMyLocation().getLongitude()), 15));*/
        Criteria criteria = new Criteria();
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        mostrarLocalizacionMapa(location);

    }
    private void mostrarLocalizacionMapa(Location location){
        if (location != null) {
            mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
        mapa.addMarker(new MarkerOptions().position(mapa.getCameraPosition().target));

            if ((message!="") && (message!=null)){
                mapa.addMarker(new MarkerOptions()
                        .position(new LatLng(location.getLatitude(), location.getLongitude()))
                        .title("Mensaje enviado desde" + number + ": " + message)).showInfoWindow();
            }
        }
    }

    private void visualizarContenidoSMS(String contenido) {
        Criteria criteria = new Criteria();
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        mapa.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("SMS Recibido").snippet(contenido));
    }
    public void onProviderDisabled(String proveedor) {
        ponerRequestLocationUpdates();
    }
    public void onProviderEnabled(String proveedor) {
        ponerRequestLocationUpdates();
    }
    public void onStatusChanged(String proveedor, int estado, Bundle extras) {
        ponerRequestLocationUpdates();
    }

}
