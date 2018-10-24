package com.example.angel.p10ficherostextoangelsalas;

import android.app.Activity;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
/*
        GUARDAR FICHERO EN LA MEMORIA INTERNA DE ANDROID
 */
public class MainActivity extends AppCompatActivity {
    private EditText etNombreFichm, etContenidom;

    //IMPORTANTE añadir el permiso de escritura en el almacenamiento externo en fichero AndroidManifest.xml
    // <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    // Recurso: http://www.sgoliver.net/blog/ficheros-en-android-i-memoria-interna/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Referenciar componentes
        etNombreFichm = (EditText) findViewById(R.id.etNombre);
        etContenidom = (EditText) findViewById(R.id.etContenido);
    }

    /**
     * METODO PARA GUARDAR UN NUEVO FICHERO DE TEXTO
     * @param view
     */
    public void GuardarClick(View view){
        String nomFich = etNombreFichm.getText().toString();
        String contFich = etContenidom.getText().toString();

        try{
            /*
                //Recuperamos la ruta de la targeta SD con la clase "Enviroment"
                //Utilizamos un fichero auxiliar que simplemente utilizaremos para obtener la ruta
                File rutaSD = Environment.getExternalStorageDirectory();
                Toast.makeText(this, "Ruta: "+rutaSD.getPath(), Toast.LENGTH_SHORT).show();
                //Creamos el fichero con el nombre que el usuario ha indicado en la ruta de la SD
                //Unicamete servira para crear el fichero
                File fichero = new File(rutaSD.getPath(),nomFich);
            */

            // Abrir el fichero para escritura //

            //Crear buffer intermediario con un flujo de salida(openFileOutput), indicando por parametro el fichero que vamos a crear y de que modo (Privado)
            // por defecto se crea en la ruta /data/data/paquete.java/files/nombre_fichero
            OutputStreamWriter fs = new OutputStreamWriter(openFileOutput(nomFich, Activity.MODE_PRIVATE));

            //Pasar el contenido al propio fichero
            fs.write(contFich);

            //Limpiar y cerrar Buffer
            fs.flush();
            fs.close();

            //Limpiar componentes pantalla
            limpiar();
        }catch (FileNotFoundException e) {
            Toast.makeText(this, "Archivo no encontrado", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error al guardar fichero", Toast.LENGTH_SHORT).show();
        } ;
    }

    /**
     * METODO PARA LEER UN FICHERO GUARDADO
     * @param view
     */
    public void LeerClick(View view){
        String nomFich = etNombreFichm.getText().toString();
        try{
            /*
            File rutaSD = Environment.getExternalStorageDirectory();
            File fichero = new File(rutaSD.getPath(),nomFich);
            */

            //  Abrir el fichero para lectura //

            //Crear flujo de entrada con el metodo de android (openFileInput), indicando por parametro el fichero que vamos a abrir
            // por defecto se busca en la ruta /data/data/paquete.java/files/nombre_fichero
            InputStreamReader fe = new InputStreamReader(openFileInput(nomFich));
            //Crear buffer intermediario para leer
            BufferedReader be=new BufferedReader(fe);

            //Leer el fichero
            String leidoTotal=""; //Variable acumuladora de lineas
            String lector = be.readLine(); //Variable que leerá cada una de las lineas

            //Recorrer fichero completo
            while(lector!=null) {
                leidoTotal += lector + "\n";
                lector=be.readLine();
            }

            //Cerrar los flujos de entrasa
            be.close();
            fe.close();

            //Establecer el contenido leido a la caja de texto
            etContenidom.setText(leidoTotal);

        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Archivo no encontrado", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "No se puede leer el archivo", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * METODO PARA LIMPIAR EL CONTENIDO DE LOS ELEMENTOS DE LA PANTALLA
     */
    public void limpiar(){
        etNombreFichm.setText("");
        etContenidom.setText("");
    }
}
