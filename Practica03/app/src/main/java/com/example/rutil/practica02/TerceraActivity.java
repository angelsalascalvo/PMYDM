package com.example.rutil.practica02;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class TerceraActivity extends AppCompatActivity {

    private TextView tvTit;
    private EditText etConte;
    private String nomFichero="", contenido="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tercera);

        //REFERENCIAR COMPONENTES
        tvTit = (TextView) findViewById(R.id.tvTitulo);
        etConte = (EditText) findViewById(R.id.etContenido);
        nomFichero = getIntent().getStringExtra("fichero");

        //LEER EL FICHERO
        File rutaSD = Environment.getExternalStorageDirectory();
        File fichero = new File(rutaSD.getAbsoluteFile()+"/"+nomFichero);

        try {
            //Creacion del flujo de entrada de datos y el buffer
            InputStreamReader flujoE = new InputStreamReader(new FileInputStream(fichero));
            BufferedReader bufferE = new BufferedReader(flujoE);

            //Lectura Previa del fichero
            String lector = bufferE.readLine();

            while(lector!=null){
                contenido+=lector+"\n"; //Añadir el contenido leido a la variable
                lector = bufferE.readLine();
            }

            //Volcar el contenido sobre el area de texto multilinea
            etConte.setText(contenido);

            //Indicar nombre del fichero en el campo de texto
            tvTit.setText(nomFichero);

        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Fichero de información no encontrado", Toast.LENGTH_SHORT).show();
            finish();
        } catch (IOException e) {
            Toast.makeText(this, "Fichero de información no encontrado", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO PARA VOLVER A LA ACTIVITY ANTERIOR
     *
     * @param view
     */
    public void volver(View view) {
        finish();
    }


}
