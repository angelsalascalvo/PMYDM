package com.example.rutil.p29podometroangelsalas;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    SensorManager sensorManager;
    Sensor sensor;
    SensorEventListener andar;
    EditText etPasosADar;
    Button bEmpezar;
    Button bRendirse;
    TextView tvPasos;
    TextView tvObjetivo;
    ImageView ivPersona;
    int pasosActuales=0, objetivo=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Referencias
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        etPasosADar = (EditText) findViewById(R.id.etPasosADar);
        bEmpezar = (Button) findViewById(R.id.bEmpezar);
        bRendirse= (Button) findViewById(R.id.bRendirse);
        tvPasos = (TextView) findViewById(R.id.tvPasos);
        tvObjetivo = (TextView) findViewById(R.id.tvObjetivo);
        ivPersona = (ImageView) findViewById(R.id.ivPersona);

        //Con esto comprobamos si el teléfono tiene podómetro
        if(sensor == null){
            Toast.makeText(this, "No tienes podómetro", Toast.LENGTH_LONG).show();
            bEmpezar.setEnabled(false);
            bRendirse.setEnabled(false);
        }

        //------------------------------------------------------------------------------------------

        //Crear objeto de escucha con una clase anonima
        andar = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                //Si hemos llegado nos termina y si no suma y sigue
                if(pasosActuales == objetivo){
                    terminar();
                }
                pasosActuales++;
                tvPasos.setText(pasosActuales+"");
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        };

    }

    //----------------------------------------------------------------------------------------------

    /**
     * MUESTRA MENSAJE DE FINALIZACIÓN AL LLEGAR LOS PASOS AL OBJETIVO Y DESHABILITA LOS CONTROLES
     */
    public void terminar(){
        sensorManager.unregisterListener(andar);
        Toast.makeText(this, "FELICICADES POR LA CAMINATA", Toast.LENGTH_LONG).show();

        bEmpezar.setEnabled(true);
        bRendirse.setEnabled(false);
    }

    //----------------------------------------------------------------------------------------------

    public void empezar(View v){
        //Reiniciamos los datos
        pasosActuales = 0;
        tvPasos.setText(0+"");
        bEmpezar.setEnabled(false);
        bRendirse.setEnabled(true);
        tvObjetivo.setTextColor(Color.BLACK);

        //Controlamos la excepción de que si no hay nada no lo pone
        if(etPasosADar.getText().toString().length() != 0){
            //Cogemos los pasos que hemos puesto arriba, los pasamos al objetivo y limpiamos arriba
            objetivo = Integer.parseInt(etPasosADar.getText().toString());
            tvObjetivo.setText(objetivo+"");
            etPasosADar.setText("");

            //Activamos el sensor y limitamos los pasos al objetivo
            //Lo segundo se hace dentro del EventListener
            sensorManager.registerListener(andar, sensor, SensorManager.SENSOR_DELAY_GAME);
        } else {
            Toast.makeText(this, "Introduce un objetivo arriba", Toast.LENGTH_LONG).show();
            bEmpezar.setEnabled(true);
            bRendirse.setEnabled(false);
        }
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO PARA EL BOTON DE RENDIRSE DONDE SE DESACTIVA EL LISTENER
     * @param v
     */
    public void rendirse(View v){
        //Desactivar
        sensorManager.unregisterListener(andar);
        //Cambiar estado de los botones
        Toast.makeText(this, "¿Ya te has cansado?", Toast.LENGTH_LONG).show();
        bRendirse.setEnabled(false);
        bEmpezar.setEnabled(true);
        tvObjetivo.setTextColor(Color.RED);
    }

}
