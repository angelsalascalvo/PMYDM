package com.example.rutil.p18actionbarmenuoverflowangelsalas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //IMPORTANTE! En primer lugar crear recurso de tipo menu
    // res (click derecho) -> new Android Resource File -> name "overflow", type "menu"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //1.Crear metodo para ocultar y mostrar el menu

    /**
     * METODO PARA MOSTRAR Y OCULTAR EL MENU OVERFLOW
     * Tiene un nombre especifico y tiene que devolver true porque es una sobrescritura
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        /*
        * En el metodo onCreateOptionsMenu creamos un objeto de la clase MenuInflater mediante
        * el metodo inflate y vinculamos el identificador del archivo recursos R.menu.overflow y
        * el objeto de la clase menu*/

        getMenuInflater().inflate(R.menu.overflow, menu); //Vinculamos Menu con el recurso overflow creado
        return true;
    }

    //2.Crear metodo para dar funcion a cada opcion del menu

    /**
     *  METODO PARA ASIGNAR LA FUNCIONALIDAD A CADA UNA DE LAS OPCIONES
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //Capturamos el item que ha sido pulsado
        int id = item.getItemId();
        //En funcion de la opcion seleccionada haremos una acción
        if(id == R.id.miOpcion1){
            Toast.makeText(this, "Pulsada primera opción", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.miOpcion2){
            Toast.makeText(this, "Pulsada segunda opción", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.miOpcion3){
            finish();
        }

        //Devolvemos el valor del metodo padre
        return super.onOptionsItemSelected(item);
    }
}
