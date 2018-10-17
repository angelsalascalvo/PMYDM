package com.example.angel.p06pasoinfdosactivitysangelsalas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private EditText etNombM; //La "M" final hace referencia a que viene del main

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Referenciar al elemento de la clase main
        etNombM = (EditText) findViewById(R.id.etNombre);
    }

    /**
     * Metodo para eviar información al segundo activity
     * @param view
     */
    public void enviarClick(View view){
        //Declarar la activity a la que vamos a cambiar
        Intent siguiente = new Intent(this, SegundoActivity.class);

        //Metodo que permite enviar información a otro activity, pasando un nombre para identificar el dato y el propio contenido del dato
        siguiente.putExtra("nombre", etNombM.getText().toString());

        //Pasar a otra activity
        startActivity(siguiente);
    }
}
