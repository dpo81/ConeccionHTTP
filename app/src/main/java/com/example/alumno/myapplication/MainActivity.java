package com.example.alumno.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    MiHilo hilo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView texto = (TextView) super.findViewById(R.id.texto);
        ImageView imagen = (ImageView) super.findViewById(R.id.imagen);

        hilo = new MiHilo();
        hilo.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        hilo.interrupt();
    }
}
