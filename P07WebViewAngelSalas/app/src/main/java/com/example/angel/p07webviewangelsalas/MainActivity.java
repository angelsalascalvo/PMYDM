package com.example.angel.p07webviewangelsalas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private EditText etUrlM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Referencias
        etUrlM = (EditText) findViewById(R.id.etUrl);
    }

    //Metodo para el clic del boton "ir al sitio web"
    public void accederClick(View view){
        //Declarar activity web
        Intent i = new Intent(this, ActivityWeb.class);

        //Enviar url a la activity web
        i.putExtra("url", etUrlM.getText().toString());

        //Cargar Activity
        startActivity(i);
    }
}
