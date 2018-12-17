package com.example.rutil.pmdmsensorgiroscopio;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    SensorManager sensorManager;
    Sensor giroscopio;
    SensorEventListener escucharGiroscopio;
    ImageView ivVasom;
    TextView tvGradosm;
    Switch sInterruptorm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //REFERENCIAS
        ivVasom = (ImageView) findViewById(R.id.ivImagen);
        sInterruptorm = (Switch) findViewById(R.id.sInterruptor);
        tvGradosm = (TextView) findViewById(R.id.tvGrados);

        //Objeto administrador de sensores
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //Objeto del sensor especifico que vamos a utilizar
        giroscopio = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        //Comprobar si el dispositivo dispone de giroscopio
        if (giroscopio == null) {
            String mensaje = "Su dispositivo no dispone de giroscopio";
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
            sInterruptorm.setEnabled(false);
        }

        //Instanciar objeto de escucha para el sensor
        escucharGiroscopio = new SensorEventListener() {

            /**
             * METODO QUE ACTUARÁ CUANDO EL SENSOR CAMBIE DE VALOR
             * @param sensorEvent
             */
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                //Convertir los datos de sensorEvent en valores de rotación
                float[] rotationMatrix = new float[16];
                SensorManager.getRotationMatrixFromVector(rotationMatrix, sensorEvent.values);

                float[] remappedRotationMatrix = new float[16];
                SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_X,
                    SensorManager.AXIS_Z, remappedRotationMatrix);

                float[] orientations = new float[3];
                SensorManager.getOrientation(remappedRotationMatrix, orientations);

                //Convertir radianes a grados
                for (int i = 0; i < 3; i++) {
                    orientations[i] = (float) (Math.toDegrees(orientations[i]));
                }

                //Rotar imagen en sentido opuesto el numero de grados
                ivVasom.setRotation(orientations[2] * -1);
                //Indicar los grados de inclinación
                tvGradosm.setText((int)orientations[2]+"º");
            }

            /**
             * SOBRESCRITURA DEL METODO onAccuracyChanged
             * @param sensor
             * @param i
             */
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO QUE SE EJECUTARÁ AL PULSAR SOBRE EL SWITCH
     * Activara o desactivará el giroscopio
     * @param view
     */
    public void interruptorClic(View view){
        //Comprobar si el switch está activado
        if(sInterruptorm.isChecked()){
            //Asignaremos el objeto oyente al giroscopio con la tasa de actualización
            sensorManager.registerListener(escucharGiroscopio, giroscopio, SensorManager.SENSOR_DELAY_GAME);
        }else{
            //Establecemos los valores a 0 y desactivamos el oyente
            ivVasom.setRotation(0);
            tvGradosm.setText("0º");
            sensorManager.unregisterListener(escucharGiroscopio);
        }
    }
}