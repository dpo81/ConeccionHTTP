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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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

            //List<Persona> personas = parserXml((String) msg.obj);

//            Log.d("prueba debug parser xml", "inicio");
//            for (Persona p : personas) {
//                Log.d("prueba debug parser xml", p.toString());
//            }
//            Log.d("prueba debug parser xml", "fin");

            String jSONPersonas = new String("{personas:[{persona:{nombre:'Juan',edad=25}},{persona:{nombre:'Pedro',edad=22}},{persona:{nombre:'Roberto',edad=33}}]}");

            //String jSONPersonas = "{personas:[{nombre:'Juan',edad=25},{nombre:'Pedro',edad=22},{nombre:'Roberto',edad=33}]}";

            Log.d("prueba parser json", "inicio");
            List<Persona> personas = parserJSON(jSONPersonas);

            for (Persona p : personas) {
                Log.d("prueba debug parser xml", p.toString());
            }
            Log.d("prueba parser json", "fin");

            //"{Personas:[{Persona:{nombre:'Juan',edad=25}},{Persona:{nombre:'Pedro',edad=22}},{Persona:{nombre:'Roberto',edad=33}}]};)
        }

        if (msg.arg1 == 1) {
            byte[] bites = (byte[]) msg.obj;
            imagen.setImageBitmap(BitmapFactory.decodeByteArray(bites, 0, bites.length));
        }
        return false;
    }

    public List<Persona> parserJSON(String json) {
        List<Persona> personas = new ArrayList<Persona>();

        // parseo a JSON
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            JSONArray jPersonas = jsonObject.getJSONArray("personas");

            //Log.d("prueba debug parser xml", String.valueOf(jPersonas.length()));

            for (int i = 0; i < jPersonas.length(); i++) {
                JSONObject jPersona = jPersonas.getJSONObject(i).getJSONObject("persona");

                //Log.d("prueba debug parser xml", "tag persona encontrado");

                Persona persona = new Persona();

                persona.setNombre(jPersona.getString("nombre"));
                persona.setEdad(jPersona.getInt("edad"));

                personas.add(persona);
            }

        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        return personas;
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
                        tag = parser.getName();

                        if (tag.equals("Persona")) {
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
