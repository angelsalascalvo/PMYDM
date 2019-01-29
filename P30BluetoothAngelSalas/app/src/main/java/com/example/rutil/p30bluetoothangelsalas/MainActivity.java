package com.example.rutil.p30bluetoothangelsalas;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView lista;
    private BluetoothAdapter mBluetoothAdapter;
    //Variable estática que devuelve el código de la Activity Principal
    private static final int REQUEST_ENABLE_BT = 1;
    private ArrayList miArrayList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Referencias
        lista=(ListView) findViewById(R.id.lista);

        //Comprobar si el dispositivo tiene bluetooth
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter== null)
            Toast.makeText(this, "Tu dispositivo no tiene Bluetooth", Toast.LENGTH_SHORT).show();
        //Si tiene bluetooth y si no está activado, lo activamos
        else if(!mBluetoothAdapter.isEnabled()) {
            Intent habilitarBlue = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(habilitarBlue, REQUEST_ENABLE_BT);
        }

        //Se registra el broadcast register
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO QUE SE EJECUTARÁ AL PULSAR EL BOTON DE ESCANEAR
     * @param view
     */
    public void onClickEscanear(View view){
        //Si el dispositivo esta escaneando se cancela la acción
        if(mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
        }
        //Comenzamos el proceso de escaneo
        mBluetoothAdapter.startDiscovery();

        //Colocar el contenido del arrayList que almacena información de los dispositivos
        // en el listView utilizando el adapter adecuado
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.lista, miArrayList);
        lista.setAdapter(adapter);
    }

    //----------------------------------------------------------------------------------------------

    //Variable instanciada con una clase anonima que recibira el objeto localizado por bluetooth
    private final BroadcastReceiver mReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            //Comprobar que la señal recibida sea un dispositivo y no una conexión diferente
            if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                //Obtener información del dispositivo
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                miArrayList.add(device.getName()+" - "+device.getAddress());
            }
        }
    };
}
