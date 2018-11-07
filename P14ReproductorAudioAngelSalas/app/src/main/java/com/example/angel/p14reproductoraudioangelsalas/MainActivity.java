package com.example.angel.p14reproductoraudioangelsalas;

import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button play;
    SoundPool sp;
    int sonidoReproduccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Referenciar botones
        play = (Button) findViewById(R.id.bSoundpool);

        //Metodo para api menor a la 21
        sp = new SoundPool(1,AudioManager.STREAM_MUSIC,1); //MaxStreams es el numero de sonidos simultaneos que se puede reproducir (con 1 no suenan 2 pistas simultaneas)
        sonidoReproduccion = sp.load(this, R.raw.sonidocorto, 1 );
    }

    /**
     * METODO QUE SE EJECUTARÁ AL HACER CLIC SOBRE EÑ BOTON
     * @param view
     */
    public void AudioSoundpoolClick(View view){
        sp.play(sonidoReproduccion, 1,1,1,0,0); //Rate es la velocidad de reproducción
    }
}
