package com.example.alumno.myapplication;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by alumno on 12/10/2017.
 */

public class MiHilo extends Thread {
    @Override
    public void run(){
        getHttpByteArray();
    }

    void getHttpByteArray() {
        try {
            // crear objeto url, objeto al cualo nos vamos a conectar
            URL url = new URL("http://www.lslutnfra.com/alumnos/practicas/listaPersonas.xml");// direcciòn a la que vamos a acceder

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

                            Log.d("Contenido archivo leido", new String(baos.toByteArray(), "UTF-8")); // obtengo lo leido
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
        } catch (MalformedURLException e) {
            e.printStackTrace();

            Log.d("Pascual", "Error al crear objeto URL");
        }
    }
}
