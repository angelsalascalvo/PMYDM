package com.example.rutil.p28nfcangelsalas;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    NfcAdapter nfc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instanciar el sensor del NFC
        nfc = NfcAdapter.getDefaultAdapter(this);

        // Si la variable que referencia al sensor de NFC es nula, significa que nuestro dispositivo no tiene NFC
        // y lo indicamos
        if(nfc==null){
            Toast.makeText(this, "Este dispositivo no cuenta con NFC", Toast.LENGTH_SHORT).show();
        }
        //Si el dispositivo si tiene NFC comprobamos que esté activado
        else if(nfc.isEnabled()==false){
            Toast.makeText(this, "Debes activar el NFC para utilizar la aplicacion ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected  void onNewIntent(Intent intent){
        Toast.makeText(this,"Dispositivo NFC encontrado", Toast.LENGTH_SHORT).show();
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume(){
        //Eliminamos la petición de apertura de la pantalla
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, 0);
        IntentFilter[] intentFilters = new IntentFilter[]{};

        if(nfc!=null)
            nfc.enableForegroundDispatch(this, pendingIntent, intentFilters, null);

        super.onResume();
    }

    @Override
    protected void onPause(){
        if(nfc!=null)
            nfc.disableForegroundDispatch(this);
        super.onPause();
    }




}
