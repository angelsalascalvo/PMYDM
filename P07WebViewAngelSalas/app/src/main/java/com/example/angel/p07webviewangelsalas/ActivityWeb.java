package com.example.angel.p07webviewangelsalas;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ActivityWeb extends AppCompatActivity {
    private WebView wvNavega;
    private Button pixel;
    private ImageView foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        //Referencias de los objetos de la activity
        wvNavega = (WebView) findViewById(R.id.wvNavegador);
        pixel = (Button) findViewById(R.id.pixel);
        foto = (ImageView) findViewById(R.id.foto);

        pixel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = takeScreenshot();
                foto.setImageBitmap(bitmap);
                int pixel = bitmap.getPixel(500,800);

                int redValue = Color.red(pixel);
                int blueValue = Color.blue(pixel);
                int greenValue = Color.green(pixel);

                mostrar(redValue+", "+blueValue+", "+greenValue);
            }
        });
        //Obtener url enviada desde la activity main
        String url = getIntent().getStringExtra("url");

        //Establecer url al WebView
        wvNavega.setWebViewClient(new WebViewClient()); //Activar el navegador WebView
        wvNavega.loadUrl("http://"+url); //Asignar url

        wvNavega.setFindListener(new WebView.FindListener() {

            @Override
            public void onFindResultReceived(int activeMatchOrdinal, int numberOfMatches, boolean isDoneCounting) {
                Toast.makeText(getApplicationContext(), "Matches: " + numberOfMatches, Toast.LENGTH_LONG).show();
            }
        });





        //NO OLVIDARSE DE SOLICITAR PERMISO DE ACCESO A INTERNET EN AndroidManifest.xml
        //  <uses-permission android:name="android.permission.INTERNET"/>
    }

    public void mostrar(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public Bitmap takeScreenshot() {
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        Bitmap b = rootView.getDrawingCache();
        return b;
    }





    public void buscar(View view){
        wvNavega.findAllAsync("https://www.hola.com");

        Toast.makeText(getApplicationContext(), "reee", Toast.LENGTH_LONG).show();
    }

    public void siguiente(View view){
        wvNavega.findNext(true);
    }

    /**
     * Metodo del boton cerrar
     * @param view
     */
    public void cerrarClick(View view){
        finish(); //Salir del activity (vuelve al anterior)
    }
}
