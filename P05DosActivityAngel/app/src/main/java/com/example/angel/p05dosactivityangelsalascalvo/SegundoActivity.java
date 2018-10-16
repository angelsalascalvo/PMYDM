package com.example.angel.p05dosactivityangelsalascalvo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SegundoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segundo);
    }

    //Metodo del boton "ANTERIOR"
    public void anteriorClick(View view){
        //Para comunicar ativitys necesitamos hacerlo a traves de la clase Intent
        Intent anterior = new Intent(this, MainActivity.class); //Referenciamos al activity que vamos a cargar
        //Llamada a la nueva activity para que se cargue
        startActivity(anterior);
    }
}
