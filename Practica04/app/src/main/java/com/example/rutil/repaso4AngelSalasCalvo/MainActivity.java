package com.example.rutil.repaso4AngelSalasCalvo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout clMiGrabacion;
    ImageButton ibPlay_pausem, ibLoopm, ibC1, ibC2, ibC3, ibC4, ibPistaGrabada, ibGrabarm;
    ImageButton[] arrayBotones = new ImageButton[4];
    MediaPlayer mp;
    MediaRecorder mr;
    boolean pulsadoCancionGrabada=false;
    String fichero;
    //Crear un array para almacenar las canciones
    ArrayList<MediaPlayer> arrayCanciones = new ArrayList<MediaPlayer>();
    //Variables que referencian si esta en loop y la cancion que estamos reproducciendo
    int repetir = 2, posicion=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Referenciar elementos
        ibPlay_pausem = (ImageButton) findViewById(R.id.ibPlay_Pause);
        ibLoopm = (ImageButton) findViewById(R.id.ibLoop);
        ibC1 = (ImageButton) findViewById(R.id.ibCancion1);
        ibC2 = (ImageButton) findViewById(R.id.ibCancion2);
        ibC3 = (ImageButton) findViewById(R.id.ibCancion3);
        ibC4 = (ImageButton) findViewById(R.id.ibCancion4);
        ibPistaGrabada = (ImageButton) findViewById(R.id.ibCancionGrabada);
        ibGrabarm = (ImageButton) findViewById(R.id.ibGrabar);
        clMiGrabacion = (ConstraintLayout) findViewById(R.id.clCancionGrabada);


        //Añadir botones al array
        arrayBotones[0] = ibC1;
        arrayBotones[1] = ibC2;
        arrayBotones[2] = ibC3;
        arrayBotones[3] = ibC4;

        //Añadir los archivos MediaPlayer al array
        arrayCanciones.add(MediaPlayer.create(this, R.raw.porter));
        arrayCanciones.add(MediaPlayer.create(this, R.raw.lonely));
        arrayCanciones.add(MediaPlayer.create(this, R.raw.stockholm));
        arrayCanciones.add(MediaPlayer.create(this, R.raw.stole));
        //Componenmos el nombre completo con la ruta del fichero de salida. Obteniendo el path absoluto (desde la raiz del dispositivo)
        fichero = Environment.getExternalStorageDirectory().getAbsolutePath() + "/miGrabación.mp3";
        File comprobar = new File(fichero);
        //Si el fichero miGrabacion no esta creado, no se mostrará
        if(comprobar.exists()==false){
            clMiGrabacion.setVisibility(View.GONE);
        }else{
            clMiGrabacion.setVisibility(View.VISIBLE);
            //Creamos un mediaplayer con el audio grabado
            MediaPlayer mpGrabado = new MediaPlayer();
            try {
                mpGrabado.setDataSource(fichero);
                mpGrabado.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            arrayCanciones.add(mpGrabado);
        }

        //Comprobar si el usuario NO ha aceptado los permisos, en este caso volverá a solicitar los permisos
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            //Si los permisos no estan concedidos, los volvemos a solicitar
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1000);
        }
    }

    /**
     * METODO PARA BORRRAR LA GRABACION
     * @param view
     */
    public void borrarGrabacionClick(View view){
        if(arrayCanciones.get(4).isPlaying()==false) {
            arrayCanciones.remove(4);
            clMiGrabacion.setVisibility(View.GONE);
            File miGrabacion = new File(fichero);
            miGrabacion.delete();
        }
    }

    /**
     * METODO QUE SE EJECUTA CUANDO SE HACE CLIC EN GRABAR
     * @param view
     */
    public void botonGrabarClic(View view){
        //Si no esta creado, creamos el MediaRecorder
        if(mr == null) {
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
            ibGrabarm.setBackgroundResource(R.mipmap.micro_rojo);
            Toast.makeText(this, "Grabando...", Toast.LENGTH_SHORT).show();
        }else {
            //El MediaRecorder ya existe y lo que quiere el usuario es parar la grabación
            mr.stop();
            //Finalizar grabación
            mr.release();

            //Poner a nulo de nuevo el MediaRecorder para poder volver a grabar
            mr = null;
            //Cambiar icono al boton
            ibGrabarm.setBackgroundResource(R.mipmap.micro);
            clMiGrabacion.setVisibility(View.VISIBLE);
            //Creamos un mediaplayer con el audio grabado
            MediaPlayer mpGrabado = new MediaPlayer();
            try {
                mpGrabado.setDataSource(fichero);
                mpGrabado.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //En funcion de si ya habia antes un audio o es el primero, cambiaremos el contenido de la lista o lo añadiremos
            if(arrayCanciones.size()==5) {
                arrayCanciones.set(4, mpGrabado);
            }else
                arrayCanciones.add(mpGrabado);


            Toast.makeText(this, "Grabación finalizada", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * METODO QUE ACTUA CUANDO SE PULSA UN BOTON DE UNA CANCION
     * @param view
     */
    public void botonListadoClick(View view){
        if(arrayCanciones.get(posicion).isPlaying()){
            arrayCanciones.get(posicion).pause();
        }

        if(posicion<4){
            arrayBotones[posicion].setBackgroundColor(Color.parseColor("#294869"));
        }
        ibPistaGrabada.setBackgroundColor(Color.parseColor("#294869"));
        switch (view.getId()){
            case R.id.ibCancion1:
                posicion=0;
                break;
            case R.id.ibCancion2:
                posicion=1;
                break;
            case R.id.ibCancion3:
                posicion=2;
                break;
            case R.id.ibCancion4:
                posicion=3;
                break;
            case R.id.ibCancionGrabada:
                pulsadoCancionGrabada=true;
                posicion=4;
                break;
        }

        if(pulsadoCancionGrabada){
            ibPlay_pausem.setBackgroundResource(R.mipmap.pausa);
            ibPistaGrabada.setBackgroundColor(Color.parseColor("#759b96"));
            arrayCanciones.get(posicion).seekTo(0);
            arrayCanciones.get(posicion).start();
            pulsadoCancionGrabada=false;
        }else {
            ibPlay_pausem.setBackgroundResource(R.mipmap.pausa);
            arrayBotones[posicion].setBackgroundColor(Color.parseColor("#759b96"));
            arrayCanciones.get(posicion).seekTo(0);
            arrayCanciones.get(posicion).start();
        }
    }

    /**
     * METODO QUE SE EJECUTARÁ AL HACER CLICK SOBRE EL BOTON PLAY/PAUSE
     * @param view
     */
    public void botonPlayPauseClick(View view){
        //Comprobar si se esta reproducciendo la cancion mediante .isPlaying
        if(arrayCanciones.get(posicion).isPlaying()){
            //Acciones si estaba reproduciendo
            arrayCanciones.get(posicion).pause();
            arrayBotones[posicion].setBackgroundColor(Color.parseColor("#759b96"));
            ibPlay_pausem.setBackgroundResource(R.mipmap.reproducir);
            //Por si esta activo, desactivamos el loop
            descLoop();
            //Toast.makeText(this,"Canción pausada", Toast.LENGTH_LONG).show();
        }else {
            //Acciones si estaba en pausa
            arrayCanciones.get(posicion).start();
            arrayBotones[posicion].setBackgroundColor(Color.parseColor("#759b96"));
            ibPlay_pausem.setBackgroundResource(R.mipmap.pausa);
            //Toast.makeText(this, "Reproduciendo Canción", Toast.LENGTH_LONG).show();
        }
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO QUE SE EJECUTARÁ AL HACER CLICK SOBRE EL BOTON DETENER
     * @param view
     */
    public void botonDetenerClick(View view){

        //Comprobar que la canción se esta reproduciendo
        if(arrayCanciones.get(posicion).isPlaying()){
            //Por si esta activo, desactivamos el loop
            descLoop();
            arrayCanciones.get(posicion).pause();
            arrayCanciones.get(posicion).seekTo(0);

            if(posicion<4){
                arrayBotones[posicion].setBackgroundColor(Color.parseColor("#294869"));
            }else {
                ibPistaGrabada.setBackgroundColor(Color.parseColor("#294869"));
            }
            //Cambiar el icono del play/pause para ponerlo a play
            ibPlay_pausem.setBackgroundResource(R.mipmap.reproducir);
            //Toast.makeText(this, "Reproducción detenida", Toast.LENGTH_LONG).show();
        }
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO QUE SE EJECUTARÁ AL HACER CLICK SOBRE EL BOTON SIGUIENTE
     * @param view
     */
    public void botonSiguienteClick(View view){
        //Comprobar que podemos seguir avanzando en el array
        if(posicion<arrayCanciones.size()-1){
            //Por si esta activo, desactivamos el loop
            descLoop();
            //Si se esta reproducciendo una cancion
            if(arrayCanciones.get(posicion).isPlaying()){
                arrayBotones[posicion].setBackgroundColor(Color.parseColor("#294869"));
                arrayCanciones.get(posicion).pause(); //Detenemos la reproducción
                posicion++; //Incrementamos la posicion de la cancion
                //Establecer colores de los botones
                if(posicion==4){
                    ibPistaGrabada.setBackgroundColor(Color.parseColor("#759b96"));
                }else
                    arrayBotones[posicion].setBackgroundColor(Color.parseColor("#759b96"));
                arrayCanciones.get(posicion).start(); //Empezamos a reproducir la siguiente cancion
            }else {
                arrayBotones[posicion].setBackgroundColor(Color.parseColor("#294869"));
                //Si no se esta reproducciendo una cancion
                posicion++;
                if(posicion==4){
                    ibPistaGrabada.setBackgroundColor(Color.parseColor("#759b96"));
                }else
                    arrayBotones[posicion].setBackgroundColor(Color.parseColor("#759b96"));
            }
            arrayCanciones.get(posicion).seekTo(0);
            arrayCanciones.get(posicion).start();
            ibPlay_pausem.setBackgroundResource(R.mipmap.pausa);

        //Si no se puede seguir avanzando en el array porque estamos en la ultima cancion del array
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
            //Por si esta activo, desactivamos el loop
            descLoop();
            //Si se esta reproducciendo una cancion
            if(arrayCanciones.get(posicion).isPlaying()){
                if(posicion==4){
                    ibPistaGrabada.setBackgroundColor(Color.parseColor("#294869"));
                }else
                    arrayBotones[posicion].setBackgroundColor(Color.parseColor("#294869"));
                arrayCanciones.get(posicion).pause(); //Detenemos la reproducción
                posicion--; //Decrementamos la posicion de la cancion
                arrayBotones[posicion].setBackgroundColor(Color.parseColor("#759b96"));
                arrayCanciones.get(posicion).start(); //Empezamos a reproducir la siguiente cancion
            }else {
                if(posicion==4){
                    ibPistaGrabada.setBackgroundColor(Color.parseColor("#294869"));
                }else
                    arrayBotones[posicion].setBackgroundColor(Color.parseColor("#294869"));
                //Si no se esta reproducciendo una cancion
                posicion--;
                arrayBotones[posicion].setBackgroundColor(Color.parseColor("#759b96"));

            }
            arrayCanciones.get(posicion).seekTo(0);
            arrayCanciones.get(posicion).start();
            ibPlay_pausem.setBackgroundResource(R.mipmap.pausa);
            //Si no se puede retroceder porque estamos en la primera posición
        }
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO QUE SE EJECUTARÁ AL HACER CLICK SOBRE EL BOTON REPETIR
     * @param view
     */
    public void loopClick(View view) {
        if(arrayCanciones.get(posicion).isPlaying()) {
            //Si esta repitiendo lo cambiamos a no repetir
            if (repetir == 1) {
                //Establecemos el icono de no repetir
                ibLoopm.setImageResource(R.mipmap.no_repetir);
                //Establecer el looping a falso
                arrayCanciones.get(posicion).setLooping(false);
                //Indicar al usuario
                Toast.makeText(this, "Modo bucle desactivado", Toast.LENGTH_LONG).show(); //Lo indicaremos
                //Establecer valor de la variable


                //Si no esta repitiendo
            } else {
                //Establecemos el icono de repetir
                ibLoopm.setImageResource(R.mipmap.repetir);
                //Establecer el looping a falso
                arrayCanciones.get(posicion).setLooping(true);
                //Indicar al usuario
                Toast.makeText(this, "Modo bucle activado", Toast.LENGTH_LONG).show(); //Lo indicaremos
                //Establecer valor de la variable
                repetir = 1;
            }
        }
    }

    /**
     * METODO PARA DESACTIVAR LOOP
     */
    public void descLoop(){
        ibLoopm.setImageResource(R.mipmap.no_repetir);
        //Establecer el looping a falso
        arrayCanciones.get(posicion).setLooping(false);
        repetir = 2;
    }
}
