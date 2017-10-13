package com.example.alumno.myapplication;

import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity implements Handler.Callback {
    TextView texto;
    ImageView imagen;
    private MiHilo hTexto;
    private MiHilo hImagen;
    private Handler handler; // manejador para sincronizar la comunicación entre hilos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        texto = (TextView) super.findViewById(R.id.texto);
        imagen = (ImageView) super.findViewById(R.id.imagen);

        handler = new Handler(this);
        hTexto = new MiHilo(handler, 0);
        hImagen = new MiHilo(handler, 1);

        try {
            // crear objeto url, objeto al cualo nos vamos a conectar
            hTexto.setUrl(new URL("http://www.lslutnfra.com/alumnos/practicas/listaPersonas.xml")); // direcciòn a la que vamos a acceder
        } catch (MalformedURLException e) {
            e.printStackTrace();

            Log.d("Pascual", "Error al crear objeto URL");
        }

        try {
            // crear objeto url, objeto al cualo nos vamos a conectar
            hImagen.setUrl(new URL("http://www.small-icons.com/packs/24x24-free-application-icons.png")); // direcciòn a la que vamos a acceder
        } catch (MalformedURLException e) {
            e.printStackTrace();

            Log.d("Pascual", "Error al crear objeto URL");
        }

        hTexto.start();
        hImagen.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        hTexto.interrupt();
        hImagen.interrupt();
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.arg1 == 0) {
            texto.setText((String) msg.obj);
        }
        if (msg.arg1 == 1) {
            byte[] bites = (byte[]) msg.obj;
            imagen.setImageBitmap(BitmapFactory.decodeByteArray(bites, 0, bites.length));
        }
        return false;
    }
}
