package com.example.rutil.p25acelerometroangelsalas;

import android.app.Service;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager manager;
    private Sensor acelerometro;
    private TextView tvTexto;
    private Button bJugar;
    private final float PUNTUACION = 1.123f; //Variable para multiplicar la velocidad y obtener una puntuación
    private SoundPool sp;
    private int rep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //REFERENCIAS
        tvTexto = (TextView) findViewById(R.id.tvTexto);
        bJugar = (Button) findViewById(R.id.bJugar);
        //SONIDO
        sp = new SoundPool(1,AudioManager.STREAM_MUSIC, 1);//Instanciar SoundPool
        rep = sp.load(this, R.raw.grito, 1);

        //SENSORES
        manager = (SensorManager) getSystemService(Service.SENSOR_SERVICE); //Instanciar SensorManager
        acelerometro = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //COMPROBAR SI DISPONEMOS DEL SENSOR
        if(acelerometro==null){
            Toast.makeText(this, "El dispositivo no dispone de acelerometro.", Toast.LENGTH_SHORT).show();
            bJugar.setEnabled(false);
        }
    }
    public void onJugar(View view){
        tvTexto.setText("DALE UN BUEN MENEO!!!");
        bJugar.setEnabled(false);
        //Registrar el sensor
        manager.registerListener(this, acelerometro,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        /**
         * EN EL ARRAY event.values
         * values[0] -> eje X
         * values[1] -> eje Y
         * values[2] -> eje Z
         *
         * Los valores son de tipo float, indicando los metros por segundo a los que se mueve el dispositivo.
         */
        int valor = Math.round(event.values[2]); //Obtenemos el valor redondeado correspondiente con el eje Z del array de values

        //Comprobaciones de movimiento
        if(valor >=15 || valor<=-15){
            int puntuacion = (int) (valor*PUNTUACION);

            //Indicar resultados
            String resultado = "La aceleración ha sido: "+valor+"\n"+
                    "tu puntuación es: "+puntuacion;
            tvTexto.setText(resultado);

            //Reproducir el sonido
            sp.play(rep, 1,1,1,0,0);

            //Finalizar la escucha
            manager.unregisterListener(this);
            bJugar.setEnabled(true); //Resetear boton para poder volver a jugar

        }else if(valor >=13 && valor <15 || valor <= -13 && valor >-15){
            tvTexto.setText("CON MAS FUERZA!");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
