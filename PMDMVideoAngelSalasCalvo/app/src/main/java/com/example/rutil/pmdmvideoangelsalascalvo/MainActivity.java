package com.example.rutil.pmdmvideoangelsalascalvo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    private VideoView visorVideo;
    static final int REQUEST_VIDEO_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //REFERENCIAS DE LOS ELEMENTOS
        visorVideo = (VideoView) findViewById(R.id.vVisor);

        //Comprobar si estan concedidos los permisos
        if(ContextCompat.checkSelfPermission(getApplicationContext(),
           Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){

            //Si los permisos no estan concedidos, los volvemos a solicitar
            ActivityCompat.requestPermissions(MainActivity.this,
            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
        }
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO PARA ABRIR LA CÁMARA DEL DISPOSITIVO
     * Este método se ejecutará al pulsar el boton "GRABAR"
     * @param view
     */
    public void grabarVideo(View view) {
        //Crear Intent de la app camara en modo video
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        //Al finalizar se comprueba si se ha grabado el video
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            //Si se ha grabado se almacena el sesultado en la constante
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO PARA ASIGNAR EL VIDEO GRABADO AL VISOR (VIDEO VIEW)
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //Comprobar que se ha devuelto un resultado correcto de la grabacion
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = intent.getData(); //Objeto uri que almacena la ruta del video grabado

            visorVideo.setVideoURI(videoUri);//Asignar la uri del video al visor
            visorVideo.setBackground(null); //Eliminar el background para poder visualizar el video
        }
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO PARA REPRODUCIR EL VIDEO GRABADO EN EL VISOR
     * @param view
     */
    public void reproducir(View view){
        //Comprobar si el visor esta enlazado con un video.
        if(visorVideo.getDuration()==-1){
            //Si no lo esta, mostramos un aviso
            Toast.makeText(this, "No se puede reproducir. Debe grabar un video",
                            Toast.LENGTH_SHORT).show();
        }else {
            visorVideo.start(); //Si el video esta asignado, se reproduce
        }
    }
}