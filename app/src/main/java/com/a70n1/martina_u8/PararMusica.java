package com.a70n1.martina_u8;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by AMARTIN on 14/11/2016.
 */

public class PararMusica extends Activity {

    private String message = "";
    private String number = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.parar_musica);



        Intent intent = getIntent();
        message = intent.getStringExtra("mensaje");
        number = intent.getStringExtra("numero");


        Button detener = (Button) findViewById(R.id.bt_parar_servicio);
        detener.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                stopService(new Intent(PararMusica.this,
                        ServicioMusica.class));
            }
        });

        Button pasar_mapa = (Button) findViewById(R.id.bt_pasar_mapa);
        pasar_mapa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity((new Intent(PararMusica.this,
                        MainActivity.class)).putExtra("mensaje", message).putExtra("numero", number));
            }
        });

    }
}
