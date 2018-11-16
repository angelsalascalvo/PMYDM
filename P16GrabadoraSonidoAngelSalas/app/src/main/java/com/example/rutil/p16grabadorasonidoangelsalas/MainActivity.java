package com.example.rutil.p16grabadorasonidoangelsalas;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private ImageButton ibGrabarm, ibPlaym;
    private MediaRecorder mr;
    private MediaPlayer mp;
    private String fichero=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Referenciar Elementos
        ibGrabarm = (ImageButton) findViewById(R.id.ibGrabar);
        ibPlaym = (ImageButton) findViewById(R.id.ibPlay);

        //IMPORTANTE!!! Pedir permisos para poder utilizar el microfono y guardar el archivo en AndroidManifest.xml

        /* Esto tenemos que comprobarlo cada vez que se inicia la aplicación por si el usuario
        los ha quitado desde ajustes. */

        //Comprobar si el usuario NO ha aceptado los permisos, en este caso volverá a solicitar los permisos
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
           && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            //Si los permisos no estan concedidos, los volvemos a solicitar
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1000);
        }

    }

    public void grabarClick(View view){
        //Si no esta creado, creamos el MediaRecorder
        if(mr == null) {
            //Componenmos el nombre completo con la ruta del fichero de salida. Obteniendo el path absoluto (desde la raiz del dispositivo)
            fichero = Environment.getExternalStorageDirectory().getAbsolutePath() + "/miGrabación.mp3";
            //Instanciar el mr
            mr = new MediaRecorder();
            //Conectar el microfono con el MediaRecorder
            mr.setAudioSource(MediaRecorder.AudioSource.MIC);
            //Configurar el formato de salida
            mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            //Codificador de salida (Seleccionar el codec con el que codificar)
            mr.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            //Configurar fichero de salida, asignando el nombre de fichero de salida al objeto
            mr.setOutputFile(fichero);

            //Grabacion de audio
            try {
                //Preparar objeto para la grabación, comprobando que el microfono este disponible...
                mr.prepare();
                //Comenzar la propia grabación una vez se encuentra preparado
                mr.start();
            } catch (IOException e) {

            }

            //Cambiar icono al boton
            ibGrabarm.setBackgroundResource(R.mipmap.rec);
            Toast.makeText(this, "Grabando...", Toast.LENGTH_SHORT).show();
        }else {
            //El MediaRecorder ya existe y lo que quiere el usuario es parar la grabación
            mr.stop();
            //Finalizar grabación
            mr.release();

            //Poner a nulo de nuevo el MediaRecorder para poder volver a grabar
            mr = null;
            //Cambiar icono al boton
            ibGrabarm.setBackgroundResource(R.mipmap.stop_rec);
            Toast.makeText(this, "Grabación finalizada", Toast.LENGTH_SHORT).show();
        }
    }

    public void playClick(View view){
        mp =new  MediaPlayer();
        if(fichero!=null) {
            try {
                //Asignar archivo al media player
                mp.setDataSource(fichero);
                //Preparar el MediaPlayer
                mp.prepare();
            } catch (IOException e) {
            }

            //Reproducir el sonido grabado
            mp.start();
        }

    }
}