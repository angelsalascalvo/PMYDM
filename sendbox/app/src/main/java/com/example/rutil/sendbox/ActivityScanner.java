package com.example.rutil.sendbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ActivityScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView scanner;

    /**
     * SOBRESCRITURA DEL METODO ON CREATE
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        //Crear objeto escanner
        scanner = new ZXingScannerView(getApplicationContext());
        setContentView(scanner);
        scanner.setResultHandler(this);
        //Iniciar la camara
        scanner.startCamera();
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO QUE SE EJECUTA AL ESCANEAR UN CODIGO DE BARRAS PARA OBTENER EL RESULTADO
     * @param resultado
     */
    @Override
    public void handleResult(Result resultado) {
        //Obtenemos el codigo leido
        String res = resultado.getText();

        //Detener el scanner
        scanner.resumeCameraPreview(this);
        scanner.stopCamera();

        //Devolver el codigo leido
        Intent i = new Intent();
        i.putExtra(ActivityCrearPaquete.CODIGO_BARRAS,res);
        setResult(RESULT_OK,i);
        finish();
    }

    //----------------------------------------------------------------------------------------------

    /**
     * SOBRESCRITURA DEL METODO ON STOP
     */
    @Override
    protected void onStop() {
        //Detener el scanner
        scanner.resumeCameraPreview(this);
        scanner.stopCamera();
        finish();
        super.onStop();
    }
}