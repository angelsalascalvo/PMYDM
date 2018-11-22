package com.example.rutil.examenangelsalascalvo;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity {

    private EditText etUsuariom, etContrasenam;
    private String usuario, contrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Referencias
        etUsuariom = (EditText) findViewById(R.id.etUsuario);
        etContrasenam = (EditText) findViewById(R.id.etContrasena);
        //Obtener la ruta del fichero
        File rutaSD = Environment.getExternalStorageDirectory();
        File fichero = new File(rutaSD+"/claves.txt");

        //Si el fichero ya existe de una anterior ejecución de la aplicación, no lo crearemos.
        // En caso contrario si será creado
        if(fichero.exists()== false){
            crearFichero(fichero); //Llamada al metodo que crea el fichero
        }
    }

    /**
     * METODO PARA CREAR FICHERO CLAVES.TXT
     */
    public void crearFichero(File fichero){
        //Valores del fichero
        usuario = "usu";
        contrasena = "123";
        try {
            PrintWriter flujoSalida = new PrintWriter(new FileOutputStream(fichero));
            //Escribir Datos
            flujoSalida.write(usuario+"\n");
            flujoSalida.write(contrasena+"\n");

            flujoSalida.flush();
            flujoSalida.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * METODO DEL BOTON PARA INICIAR SESIÓN
     */
    public void accedeClick(View view){
        //Leer fichero
        File rutaSD = Environment.getExternalStorageDirectory();
        File fichero = new File(rutaSD+"/claves.txt");
        try {
            InputStreamReader flujoEntrada = new InputStreamReader(new FileInputStream(fichero));
            BufferedReader buffer = new BufferedReader(flujoEntrada);
            //Obtener datos del fichero
            String usuLeido = buffer.readLine();
            String contLeido = buffer.readLine();

            //Comprobar que datos leidos e introducidos corresponden
            String usu = etUsuariom.getText().toString();
            String cont = etContrasenam.getText().toString();

            //Comprobar si los datos leidos coinciden con los introducidos
            if(cont.equals(contLeido) && usu.equals(usuLeido)){
                Intent i = new Intent(this, exa2AngelSalas1.class);
                //Pasar dato con el intent
                i.putExtra("usuario", usu);
                startActivity(i);
            //Si no es correcto se notifica al usuario
            }else{
                Toast.makeText(this,"Login Incorrecto", Toast.LENGTH_SHORT).show();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}