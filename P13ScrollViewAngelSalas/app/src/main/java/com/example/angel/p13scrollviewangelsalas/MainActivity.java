package com.example.angel.p13scrollviewangelsalas;

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

    //Procedimiento encargado de consultar el id del objeto que ha generado el evento y segun sea
    // muestra un mensaje.
    public void seleccionDeFruta(View view){
        //Dentro del objeto view existe la propiedad que identicida de forma unica el objeto que ha llamado al método
        int idPulsado = view.getId();

        switch (idPulsado){
            case R.id.ibCereza: //Esto devuelve el id del boton ibCereza
                Toast.makeText(this, "Se ha pulsado la cereza", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ibFresa: //Esto devuelve el id del boton ibFresa
                Toast.makeText(this, "Se ha pulsado la fresa", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ibKiwi: //Esto devuelve el id del boton ibKiwi
                Toast.makeText(this, "Se ha pulsado el kiwi", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ibPera: //Esto devuelve el id del boton ibPera
                Toast.makeText(this, "Se ha pulsado la pera", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ibPina: //Esto devuelve el id del boton ibPina
                Toast.makeText(this, "Se ha pulsado la pina", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ibPlatano: //Esto devuelve el id del boton ibPlatano
                Toast.makeText(this, "Se ha pulsado la platano", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ibRemolacha: //Esto devuelve el id del boton ibRemolacha
                Toast.makeText(this, "Se ha pulsado la remolacha", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ibSandia: //Esto devuelve el id del boton ibSandia
                Toast.makeText(this, "Se ha pulsado la sandia", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ibTomate: //Esto devuelve el id del boton ibTomate
                Toast.makeText(this, "Se ha pulsado el tomate", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ibUva: //Esto devuelve el id del boton ibUva
                Toast.makeText(this, "Se ha pulsado la uva", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
