package com.example.angel.p05dosactivityangelsalascalvo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Metodo del boton "SIGUIENTE"
    public void siguienteClick(View view){
        //Para comunicar ativitys necesitamos hacerlo a traves de la clase Intent
        Intent siguiente = new Intent(this, SegundoActivity.class); //Referenciamos al activity que vamos a cargar
        //Llamada a la nueva activity para que se cargue
        startActivity(siguiente);
    }
}
