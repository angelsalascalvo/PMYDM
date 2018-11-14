package com.example.rutil.p15reproductormusicaangelsalas;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageButton ibPlay_pausem, ibLoopm;
    MediaPlayer mp;
    ImageView ivPortadam;
    //Crear un array para almacenar las canciones
    MediaPlayer arrayCanciones[] = new MediaPlayer[3];
    //Variables que referencian si esta en loop y la cancion que estamos reproducciendo
    int repetir = 2, posicion=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Referenciar elementos
        ibPlay_pausem = (ImageButton) findViewById(R.id.ibPlay_Pause);
        ibLoopm = (ImageButton) findViewById(R.id.ibLoop);
        ivPortadam = (ImageView) findViewById(R.id.ibPortada);
        //Añadir los archivos MediaPlayer al array
        arrayCanciones[0]= MediaPlayer.create(this, R.raw.race);
        arrayCanciones[1]= MediaPlayer.create(this, R.raw.sound);
        arrayCanciones[2]= MediaPlayer.create(this, R.raw.tea);

    }

    /**
     * METODO QUE SE EJECUTARÁ AL HACER CLICK SOBRE EL BOTON PLAY/PAUSE
     * @param view
     */
    public void botonPlayPauseClick(View view){
        //Comprobar si se esta reproducciendo la cancion mediante .isPlaying
        if(arrayCanciones[posicion].isPlaying()){
            //Acciones si estaba reproduciendo
            arrayCanciones[posicion].pause();
            ibPlay_pausem.setBackgroundResource(R.mipmap.reproducir);
            Toast.makeText(this,"Canción pausada", Toast.LENGTH_LONG).show();
        }else {
            //Acciones si estaba en pausa
            arrayCanciones[posicion].start();
            ibPlay_pausem.setBackgroundResource(R.mipmap.pausa);
            Toast.makeText(this, "Reproduciendo Canción", Toast.LENGTH_LONG).show();
        }
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO QUE SE EJECUTARÁ AL HACER CLICK SOBRE EL BOTON DETENER
     * @param view
     */
    public void botonDetenerClick(View view){
        //Comprobar que la canción se esta reproduciendo
        if(arrayCanciones[posicion].isPlaying()){
            arrayCanciones[posicion].stop();
            //Cambiar el icono del play/pause para ponerlo a play
            ibPlay_pausem.setBackgroundResource(R.mipmap.reproducir);
            Toast.makeText(this, "Reproducción detenida", Toast.LENGTH_LONG).show();
        }
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO QUE SE EJECUTARÁ AL HACER CLICK SOBRE EL BOTON SIGUIENTE
     * @param view
     */
    public void botonSiguienteClick(View view){
        //Comprobar que podemos seguir avanzando en el array
        if(posicion<arrayCanciones.length-1){
            //Si se esta reproducciendo una cancion
            if(arrayCanciones[posicion].isPlaying()){
                arrayCanciones[posicion].stop(); //Detenemos la reproducción
                posicion++; //Incrementamos la posicion de la cancion
                arrayCanciones[posicion].start(); //Empezamos a reproducir la siguiente cancion
            }else
            //Si no se esta reproducciendo una cancion
                posicion++;

            //ESTABLECEMOS LA PORTADA CORRESPONDIENTE A LA POSICIÓN
            if(posicion==0){
                ivPortadam.setImageResource(R.mipmap.portada1);
            }
            if(posicion==1){
                ivPortadam.setImageResource(R.mipmap.portada2);
            }
            if(posicion==2){
                ivPortadam.setImageResource(R.mipmap.portada3);
            }

        //Si no se puede seguir avanzando en el array porque estamos en la ultima cancion del array
        }else{
            Toast.makeText(this, "No hay mas pistas de audio", Toast.LENGTH_LONG).show(); //Lo indicaremos
        }
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO QUE SE EJECUTARÁ AL HACER CLICK SOBRE EL BOTON ANTERIOR
     * @param view
     */
    public void botonAnteriorClick(View view){
        //Comprobar que estamos como minimo en la primera posicion
        if(posicion>0){
            //Si se esta reproducciendo una cancion
            if(arrayCanciones[posicion].isPlaying()){
                arrayCanciones[posicion].stop(); //Detenemos la reproducción
                posicion--; //Decrementamos la posicion de la cancion
                arrayCanciones[posicion].start(); //Empezamos a reproducir la siguiente cancion
            }else
                //Si no se esta reproducciendo una cancion
                posicion--;

            //ESTABLECEMOS LA PORTADA CORRESPONDIENTE A LA POSICIÓN
            if(posicion==0){
                ivPortadam.setImageResource(R.mipmap.portada1);
            }
            if(posicion==1){
                ivPortadam.setImageResource(R.mipmap.portada2);
            }
            if(posicion==2){
                ivPortadam.setImageResource(R.mipmap.portada3);
            }

            //Si no se puede retroceder porque estamos en la primera posición
        }else{
            Toast.makeText(this, "Estas en la primera pista de audio", Toast.LENGTH_LONG).show(); //Lo indicaremos
        }
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO QUE SE EJECUTARÁ AL HACER CLICK SOBRE EL BOTON REPETIR
     * @param view
     */
   /* if(repetir==1){

    }*/

}
