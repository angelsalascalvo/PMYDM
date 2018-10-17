package com.example.angel.p08almacenamientodatospreferencesangel;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText etEmailM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Referencias
        etEmailM = (EditText) findViewById(R.id.etEmail);

        //Primero recuperamos email en caso de haber sido guardado
        //Creamos un objeto SharedPreferences
        SharedPreferences preferencias = getSharedPreferences("correo", Context.MODE_PRIVATE); //EL unico modo que podemos utilizar es el privado ya que el resto está en desuso

    }
}
