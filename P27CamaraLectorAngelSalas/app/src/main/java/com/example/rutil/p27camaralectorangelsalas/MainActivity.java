package com.example.rutil.p27camaralectorangelsalas;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView scanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1000);
        }
    }

    public void Escaner (View view){
        scanner = new ZXingScannerView(getApplicationContext());
        setContentView(scanner);
        //Activa el escaner de la aplicacion
        scanner.setResultHandler(this);
        //Iniciar la camara
        scanner.startCamera();
    }

    @Override
    // Se ejecuta o activa el escaneo
    public void handleResult(Result result) {
        //Sonido escaneo
        MediaPlayer mp = MediaPlayer.create(this, R.raw.roadrunner);
        mp.start();

        //Crear caja de dialogo para el mensaje
        AlertDialog.Builder caja = new AlertDialog.Builder(this);
        caja.setTitle("Resultado"); //Establecer titulo a la ventana

        //Manejar el resultado del escaneo
        if(result.getText().equals("840000725646")){
            caja.setMessage("Producto encontrado: Pasion florar");
            MediaPlayer mp2 = MediaPlayer.create(this, R.raw.pasion);
        }

        else if(result.getText().equals("8422593143780")){
            caja.setMessage("Producto encontrado: Carpeta");
        }

        //Si leemos un qr con una direccion web
        else if(result.getText().contains("http")){
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getText()));
            startActivity(i);
        }
        //Cualquier otro codigo
        else{
            caja.setMessage(result.getText());
        }

        //Mostrar el mensaje creado
        AlertDialog mensaje = caja.create();
        mensaje.show();

        //Detener el scanner
        scanner.resumeCameraPreview(this);
        scanner.stopCamera();
        //Volver al activity principal
        setContentView(R.layout.activity_main);
    }
}
