package com.example.angel.p07webviewangelsalas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class ActivityWeb extends AppCompatActivity {
    private WebView wvNavega;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        //Referencias de los objetos de la activity
        wvNavega = (WebView) findViewById(R.id.wvNavegador);

        //Obtener url enviada desde la activity main
        String url = getIntent().getStringExtra("url");

        //Establecer url al WebView
        wvNavega.setWebViewClient(new WebViewClient()); //Activar el navegador WebView
        wvNavega.loadUrl("http://"+url); //Asignar url

        //NO OLVIDARSE DE SOLICITAR PERMISO DE ACCESO A INTERNET EN AndroidManifest.xml
        //  <uses-permission android:name="android.permission.INTERNET"/>
    }

    /**
     * Metodo del boton cerrar
     * @param view
     */
    public void cerrarClick(View view){
        finish(); //Salir del activity (vuelve al anterior)
    }
}
