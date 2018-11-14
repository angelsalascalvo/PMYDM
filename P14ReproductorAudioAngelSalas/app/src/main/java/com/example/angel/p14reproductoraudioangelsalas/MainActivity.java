package com.example.angel.p14reproductoraudioangelsalas;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    SoundPool sp;
    int sonidoReproduccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //DECLARAR EL SONIDO DE TIPO SOUNDPOOL EN EL METODO ONCREATE
        //Metodo para api menor a la 21
        sp = new SoundPool(1,AudioManager.STREAM_MUSIC,1); //MaxStreams es el numero de sonidos simultaneos que se puede reproducir (con 1 no suenan 2 pistas simultaneas)
        sonidoReproduccion = sp.load(this, R.raw.sonidocorto, 1 );
    }

    /**
     * METODO QUE SE EJECUTARÁ AL HACER CLIC SOBRE EL BOTON AUDIO SOUNDPOOL
     * (SONIDOS CORTOS)
     * @param view
     */
    public void AudioSoundpoolClick(View view){
        //Reproducir el propio sonido
        sp.play(sonidoReproduccion, 1,1,1,0,0); //Rate es la velocidad de reproducción
    }

    /**
     * METODO QUE SE EJECUTARÁ AL HACER CLIC SOBRE EL BOTON AUDIO MEDIAPLAYER
     * (SONIDOS LARGOS)
     * @param view
     */
    public void MediaPlayerClick(View view){
        //Crearemos un objeto MediaPlayer, pasando el contexto y el sonido(R.raw.sonido.mp3)
        MediaPlayer mp = MediaPlayer.create(this, R.raw.sonidolargo);
        //Crear evento para iniciar el sonido
        mp.start();
    }
}