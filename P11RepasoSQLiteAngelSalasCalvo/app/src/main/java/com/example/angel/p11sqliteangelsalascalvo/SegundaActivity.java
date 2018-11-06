package com.example.angel.p11sqliteangelsalascalvo;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda);

        //Referenciar componentes
        lvLista = (ListView) findViewById(R.id.lvListado);

        //Obtenemos el contenido de la base de datos mediante el metodo listarContenido
        ArrayList<String> contenido = listarContenido();

        //Agregar contenido obtenido al ListView
        ArrayAdapter<String> contenidoLv = new ArrayAdapter<String>(this, R.layout.activity_lista, contenido);
        lvLista.setAdapter(contenidoLv);
    }

    //----------------------------------------------------------------------------------------------

    /**
     * METODO PARA OBTENER UN LISTADO CON EL CONTENIDO DE LA TABLA ARTICULOS
     * @return
     */
    public ArrayList<String> listarContenido(){
        //ArrayList donde almacenaremos el contenido
        ArrayList<String> contenido = new ArrayList<String>();

        //Crear la base de datos con los datos correspondientes
        AdminSQLiteOpenHelper administracion = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        //Instancia referenciando base de datos ABIERTA par escritura
        SQLiteDatabase BaseDeDatos = administracion.getReadableDatabase();

            String sentencia = "select * from articulos";
            //Creamos un cursor que es una referencia al conjunto de filas que devuelve un seclect;
            Cursor resultado = BaseDeDatos.rawQuery(sentencia, null); //El null hace referencia a las columnas del cursor con las que queremos trabajar, en este caso todas

            //Mover el cursor al principio de los datos devueltos por la consulta, devuelve si se puede mover correctamente o no (boolean),
            // es decir hay datos en esa posicion
            if(resultado.moveToFirst()) {
                //Seguieremos recorriendo el cursor mientras no nos encontremos en la posicion de despues del ultimo elemento del cursor
                while (!resultado.isAfterLast()){
                    //Obtener los datos del resultado del select (cursor)
                    String cod = resultado.getString(0);
                    String des = resultado.getString(1);
                    String col = resultado.getString(2);
                    String pre = resultado.getString(3);

                    //Añadir el dato leido al listado
                    contenido.add(cod+", "+des+ " ("+col+") - "+pre+"€");

                    //Avanzamos el cursor al siguiente elemento
                    resultado.moveToNext();
                }
            }else
                contenido.add("No se han encontrado datos");

        //Cerramos la base de datos para no dejarla abierta
        BaseDeDatos.close();

        //Devolver el listado de datos
        return contenido;
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
