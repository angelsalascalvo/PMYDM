package com.example.angel.p03imagebuttonangelsalascalvo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Metodo que asociaremos con el boton mano
    public void BotonManoClick (View view){
        //Metodo que responde al evente onClick del boton Mano
        Toast.makeText(this, "Pulsado el Boton Mano", Toast.LENGTH_SHORT).show();
    }

    //Metodo que asociaremos con el boton Libro
    public void BotonLibroClick (View view){
        //Metodo que responde al evente onClick del boton Libro
        Toast.makeText(this, "Pulsado el Boton libro", Toast.LENGTH_SHORT).show();
    }
}
