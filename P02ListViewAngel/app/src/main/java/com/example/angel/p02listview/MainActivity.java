package com.example.angel.p02listview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tvInfo;
    private ListView lvLista;
    private String nombre[]={"Miguel", "Julian", "Javier", "Ángel", "Sergio", "Juan"};
    private String edad[] = {"22","20","33","19","18","20"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Asignar los componentes a las variables
        tvInfo= (TextView) findViewById(R.id.tvInformacion);
        lvLista= (ListView) findViewById(R.id.lvListado);

        //Creamos el adaptador de contenido que le pasaremos al listview, asignando un formato (layout propio)
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item_nombres, nombre);
        lvLista.setAdapter(adapter);

        //Clase anonima dentro del propio metodo en la cual decidimos que acciones realizar al pulsar sobre un elemento de la listView
        lvLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { //La variable position indica el indice o posición que ocupa el item seleccionado dentro del Array
               tvInfo.setText("La edad de "+lvLista.getItemAtPosition(position)+" es "+edad[position]+" años"); //Establecemos el texto correspondiente al seleccionado en el textview correspondiente
            }
        });
    }
}
