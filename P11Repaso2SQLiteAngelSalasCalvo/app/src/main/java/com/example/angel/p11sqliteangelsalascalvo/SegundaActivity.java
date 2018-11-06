package com.example.angel.p11sqliteangelsalascalvo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class SegundaActivity extends AppCompatActivity {

    ListView lvLista;
    ArrayList<String> contenido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda);

        //Referenciar componentes
        lvLista = (ListView) findViewById(R.id.lvListado);

        //Obtenemos el contenido para el listView
        contenido = getIntent().getStringArrayListExtra("contenido");

        //Agregar contenido obtenido al ListView
        ArrayAdapter<String> contenidoLv = new ArrayAdapter<String>(this, R.layout.activity_lista, contenido);
        lvLista.setAdapter(contenidoLv);

    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO PARA VOLVER A LA PANTALLA PRINCIPAL
     * @param view
     */
    public void volverClick(View view){
        finish();
    }
}
