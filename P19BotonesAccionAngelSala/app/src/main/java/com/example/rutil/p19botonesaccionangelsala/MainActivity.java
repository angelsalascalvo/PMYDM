package com.example.rutil.p19botonesaccionangelsala;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * METODO PARA MOSTRAR Y OCULTAR EL MENU CONTEXTUAL O DE OVERFLOW
     * @param menu
     * @return
     */
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menuacciones, menu);
        return true;
    }

    /**
     * METODO QUE AÑADE FUNCIÓN A LOS BOTONES DEL MENU
     * @param item
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId(); //Obtener el id de la opcion pulsada
        switch (id){
            case R.id.miCompartir:
                Toast.makeText(this, "Compartiendo...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.miBuscar:
                Toast.makeText(this, "Buscando...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.miOpcion1:
                Toast.makeText(this, "Opción 1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.miOpcion2:
                finish();
                break;
        }

        //Devolvemos el valor des metodo padre
        return super.onOptionsItemSelected(item);
    }
}

