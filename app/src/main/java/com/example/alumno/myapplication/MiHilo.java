package com.example.alumno.myapplication;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by alumno on 12/10/2017.
 */

public class MiHilo extends Thread {
    private Handler handler;
    private URL url;
    private int arg1;

    public MiHilo(Handler handler, int arg1) {
        this.handler = handler;
        this.arg1 = arg1;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    @Override
    public void run() {
        Message message = new Message();
        try {
            if (this.arg1 == 0){
                message.obj = new String(getHttpByteArray(), "UTF-8");
            }
            if (this.arg1 == 1){
                message.obj = getHttpByteArray();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        message.arg1 = this.arg1; // parámetro opcional, lo uso para saber que estoy mandando
        handler.sendMessage(message);
    }

    byte[] getHttpByteArray() {
        try {
            // crear objeto httpurlconnection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection(); // con el open ya estamos estableciendo la conecciòn

            // hacer request
            try {
                urlConnection.setRequestMethod("GET");

                try {
                    urlConnection.connect();
                    int response = urlConnection.getResponseCode();

                    Log.d("http", "Response code:" + response);

                    if (response == 200) {
                        // obtener output stream
                        InputStream is = urlConnection.getInputStream();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024]; // un array de byte puede ser cualquier cosa, texto, imagen, lo que sea
                        int length = 0;

                        // leer respuesta del stream
                        while ((length = is.read(buffer)) != -1) { // acá hay dos sentencias en una, hay una asignación y una comparación
                            baos.write(buffer, 0, length); // si no le paso el inicio y el len siempre voy a escribir 1024 byte
                        }

                        is.close();

                        return baos.toByteArray(); // obtengo lo leido
                    } else {
                        // quiza dió error
                        Log.d("Error al leer http", "Código: " + response);
                    }

                } catch (IOException e) {
                    e.printStackTrace();

                    Log.d("Pascual", "Error al obtener el response");
                }

            } catch (ProtocolException e) {
                e.printStackTrace();

                Log.d("Pascual", "Error al establecer el metodo");
            }

        } catch (IOException e) {
            e.printStackTrace();

            Log.d("Pascual", "Error al conctarse al URL");
        }

        return new byte[1];
    }
}
