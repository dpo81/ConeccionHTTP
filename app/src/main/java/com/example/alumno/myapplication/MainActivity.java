package com.example.alumno.myapplication;

import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.widget.ImageView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


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

            List<Persona> personas = parserXml((String) msg.obj);

            Log.d("prueba debug parser xml", "inicio");
            for (Persona p : personas) {
                Log.d("prueba debug parser xml", p.toString());
            }
            Log.d("prueba debug parser xml", "fin");
        }
        if (msg.arg1 == 1) {
            byte[] bites = (byte[]) msg.obj;
            imagen.setImageBitmap(BitmapFactory.decodeByteArray(bites, 0, bites.length));
        }
        return false;
    }

    public List<Persona> parserXml(String xml) {
        List<Persona> personas = new ArrayList<Persona>();
        String tag;

        // creamos el objeto parser
        XmlPullParser parser = Xml.newPullParser();

        // le pasamos un InputStream o un Reader (fuente de datos):
        try {
            parser.setInput(new StringReader(xml));

            // iteramos al parser y leemos los eventos
            int event = parser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag= parser.getName();

                        if (tag.equals("Persona")){
                            //Log.d("prueba debug parser xml", "tag persona encontrado");
                            Persona persona = new Persona();

                            persona.setNombre(parser.getAttributeValue(null, "nombre"));
                            persona.setEdad(Integer.valueOf(parser.getAttributeValue(null, "edad")));

                            personas.add(persona);
                        }

                        break;

                    case XmlPullParser.END_TAG:
                        break;

                    case XmlPullParser.END_DOCUMENT:
                        break;
                }

                try {
                    event = parser.next();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        return personas;
    }

}
